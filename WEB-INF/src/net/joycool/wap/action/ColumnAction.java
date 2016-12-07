package net.joycool.wap.action;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.WapServiceBean;
import net.joycool.wap.bean.image.ImageBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.CacheUtil;
import net.joycool.wap.cache.ColumnCache;
import net.joycool.wap.call.CallParam;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IImageService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.Encoder;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.WapServiceUtil;
import net.wxsj.util.WxsjConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class ColumnAction extends BaseAction {
	private boolean canCache;

	private boolean useCache;

	static IJaLineService lineService = ServiceFactory.createJaLineService();
	static IImageService imageService = ServiceFactory.createImageService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		canCache = true;
		useCache = false;
		
		String condition = null;
		// 取得参数
		int columnId = StringUtil.toId(request.getParameter("columnId"));
		// liuyi 2006-09-26 首页id修改 start
		if (columnId == 0 || columnId == 2085 || columnId == 6459) {
			columnId = Constants.INDEX_ID;
		}
		// liuyi 2006-09-26 首页id修改 end

		JaLineBean column = null;
		if (columnId != 0) {
			column = lineService.getLine(columnId);
			if (column == null) {
				columnId = Constants.INDEX_ID;
				column = lineService.getLine(columnId);
			}
			if (column.getChildWapType() != 0) {
				useCache = false;
			}
			if (column.getMark() == 1) {
				response.setHeader("ServiceMonitor", "1");
			}

			if (column.getMark() == 1 && column.getWapType() == 4) {
				BaseAction.sendRedirect("/sp_forum/Index.do?"
						+ request.getQueryString(), response);
				return null;
			}
		}

		/**
		 * WAP业务 清除缓存的url对应表
		 */
		// HttpSession session = request.getSession();
		// Hashtable urlMap = JoycoolSessionListener.getUrlMap(session.getId());
		// if (urlMap != null) {
		// urlMap.clear();
		// }
		// 判断是否已经缓存
		String queryString = request.getQueryString();
		String cacheKey = null;
		if (useCache && queryString != null && !queryString.equals("")) {
			cacheKey = Encoder.getMD5_Base64(queryString);
			ColumnCache cc = (ColumnCache) CacheUtil.get(cacheKey);
			if (cc != null) {
				System.out.println("getCache:" + queryString);
				request.setAttribute("totalPageCount", new Integer(cc
						.getTotalPageCount()));
				request.setAttribute("pageIndex",
						new Integer(cc.getPageIndex()));
				request.setAttribute("prefixUrl", cc.getPrefixUrl());
				request.setAttribute("blockList", cc.getBlockList());
				request.setAttribute("column", cc.getColumn());
				request.setAttribute("backTo", cc.getBackTo());
				request.setAttribute("isRoot", new Integer(cc.getIsRoot()));
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
		}

		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}

		/**
		 * 根节点
		 */
		if (columnId == 0) {
			condition = "parent_id = 0 and if_display = 1 ORDER BY line_index";
			Vector lineList = lineService.getLineList(condition);
			Vector blockList = new Vector();
			Iterator itr = lineList.iterator();
			while (itr.hasNext()) {
				blockList.add(getLineWml((JaLineBean) itr.next(), request,
						response, getLoginUser(request)));
			}
			request.setAttribute("isRoot", new Integer(1));
			request.setAttribute("blockList", blockList);
		}
		/**
		 * 不是根节点
		 */
		else {
			// 取得当前column
			if (column == null) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			column.setRootBackTo(JoycoolSpecialUtil.getRootBackTo(column));

			// 取得lineCount
			condition = "if_display = 1 and parent_id = " + columnId;
			int totalCount = lineService.getLineCount(condition);
			int totalPageCount = 1;
			if(column.getChildCountPerPage() > 0) {
				totalPageCount = totalCount / column.getChildCountPerPage();
				if (totalCount % column.getChildCountPerPage() != 0) {
					totalPageCount++;
				}
			}
			String prefixUrl = "Column.do?columnId=" + columnId;

			/**
			 * 取得行列表
			 */
			condition += " and if_display = 1 ORDER BY line_index";
			Vector lineList = lineService.getLineList(condition);
			Vector blockList = new Vector();
			Iterator itr = lineList.iterator();
			JaLineBean line = null;
			String lineWml = null;
			while (itr.hasNext()) {
				line = (JaLineBean) itr.next();
				lineWml = getLineWml(line, request, response, getLoginUser(request));
				blockList.add(lineWml);
			}

			// 返回链接
			String backTo = null;
			if (column.getBackTo() != null && column.getBackTo().equals("null")) {
				backTo = null;
			} else if (column.getBackTo() != null
					&& !column.getBackTo().equals("")) {
				backTo = column.getBackTo();
				if (!backTo.startsWith("http://")) {
					backTo = "http://" + backTo;
				}
			} else {
				backTo = "Column.do?columnId=" + column.getParentId();
			}

			// 如果能够缓存，放到缓存里面
			if (useCache && canCache) {
				ColumnCache cache = new ColumnCache();
				cache.setBackTo(backTo);
				cache.setBlockList(blockList);
				cache.setColumn(column);
				cache.setIsRoot(0);
				cache.setPageIndex(pageIndex);
				cache.setPrefixUrl(prefixUrl);
				cache.setTotalPageCount(totalPageCount);
				if (cacheKey != null) {
					System.out.println("putCache:" + queryString);
					CacheUtil.put(cacheKey, cache);
					CacheUtil.putUrl(cacheKey, queryString);
				}
			}
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("blockList", blockList);
			request.setAttribute("column", column);
			request.setAttribute("backTo", backTo);
			request.setAttribute("isRoot", new Integer(0));

		}
		/**
		 * 注册标签
		 */
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}

	public String getLineWml(JaLineBean line, HttpServletRequest request,
			HttpServletResponse response, UserBean loginUser) {
		String condition = null;

		// 登录限制
		switch(line.getLoginRestrict()) {
		case 1:
			canCache = false;
			if (loginUser == null) {
				return "";
			}
			break;
		case 2:
			canCache = false;
			if (loginUser != null) {
				return "";
			}
			break;
		case 3: {
			canCache = false;
			if (loginUser == null)
				return "";
			UserStatusBean us = loginUser.getUs();
			if(us != null && us.getLoginCount() > 15)
				return "";
		} break;
		case 4: {
			canCache = false;
			if (loginUser == null)
				return "";
			UserStatusBean us = loginUser.getUs();
			if(us == null || us.getLoginCount() <= 15)
				return "";
		} break;
		}
		/*
		// UA限制
		if (line.getUaRestrict() != null && !line.getUaRestrict().equals("")) {
			canCache = false;
			String ua = request.getHeader("User-Agent");
			if (ua != null) {
				if (!match(ua, line.getUaRestrict())) {
					return "";
				}
			} else {
				return "";
			}
		}
		// IP限制
		if (line.getIpRestrict() != null && !line.getIpRestrict().equals("")) {
			canCache = false;
			String ip = request.getRemoteAddr();
			if (ip != null) {
				if (!match(ip, line.getIpRestrict())) {
					return "";
				}
			} else {
				return "";
			}
		}
		// 日期限制
		if (line.getDateControl() == 1) {
			canCache = false;
			condition = "id = " + line.getId()
					+ " and dateStart < now() and dateEnd > now()";
			if (lineService.getLineCount(condition) == 0) {
				return "";
			}
		}
		// 时间限制
		if (line.getTimeControl() == 1) {
			canCache = false;
			Calendar cal = Calendar.getInstance();
			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
			if (line.getTimeStart() < line.getTimeEnd()) {
				if (currentHour < line.getTimeStart()
						|| currentHour > line.getTimeEnd()) {
					return "";
				}
			} else if (line.getTimeStart() > line.getTimeEnd()) {
				if (!((currentHour >= line.getTimeStart() && currentHour <= 23) || (currentHour >= 0 && currentHour <= line
						.getTimeEnd()))) {
					return "";
				}
			} else {
				if (currentHour != line.getTimeStart()) {
					return "";
				}
			}
		}
		*/
        if(line.getDisplayType() == JaLineBean.DT_NULL_FUNC_NULL) {
        	String[] content = line.getCenterWap10().split("&&");
        	String ret = "";
        	for(int i = 0;i < content.length;i++) {
        		content[i] = CacheManage.callMethod.call(content[i], new CallParam(request, response, line.getId()));
        		if(content[i] != null)
        			ret += content[i];
        	}
        	if(ret.length() > 0) {
        		if(line.getLinkType() == JaLineBean.LT_LINK) {
        			StringBuilder sb = new StringBuilder(50 + ret.length());
        			sb.append("<a href=\"");
        			sb.append(line.getLinkURL());
        			sb.append("\">");
        			sb.append(ret);
        			sb.append("</a>");
        			sb.append(line.getLineEnd());
        			return sb.toString();
        		}
        		ret += line.getLineEnd();
        	}
        	return ret;
        }

		String content = line.getWap10Content();
		// 对特殊标记进行处理
		if (content.indexOf("%JCSP%RANDOM_RECORD") != -1) {
			content = JoycoolSpecialUtil.getRandomRecord(response, content);
		} else if (content.indexOf(Constants.SPECIAL_MARK) != -1) {
			canCache = false;
			content = content.replace(Constants.SPECIAL_MONTH_MARK, ""
					+ JoycoolSpecialUtil.getMonth());
			content = content.replace(Constants.SPECIAL_DAY_MARK, ""
					+ JoycoolSpecialUtil.getDayOfMonth());
			content = content.replace(
					Constants.SPECIAL_ONLINE_USER_COUNT_MARK, ""
							+ JoycoolSpecialUtil.getOnlineUserCount(request));
			// macq_12-20_增加交友中心人数_start
			content = content.replace("%JCSP%FRIEND_ONLINE_USER_COUNT", ""
					+ JoycoolSpecialUtil.getFriendOnlineUserCount(request));
			// macq_12-20_增加交友中心人数_end
			content = content.replace("%JCSP%REAL_ONLINE_USER_COUNT", ""
					+ JoycoolSpecialUtil.getRealOnlineUserCount(request));
			content = content.replace("%JCSP%WORLD_CUP_POST_COUNT", ""
					+ JoycoolSpecialUtil.getWorldCupPostCount(request));
			content = content.replace("%JCSP%CURRENT_TIME", DateUtil
					.getCurrentTimeAsStr());
			if (content.equals(Constants.SPECIAL_INDEX_SEARCH_MARK)) {
				return JoycoolSpecialUtil.getIndexSearch(response)
						+ line.getLineEnd();
			}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Liq_2007-3-16_在女性频道中添加动态聊天室信息_start
			if (content.equals(Constants.SPECIAL_RANDOM_CHAT_MESSAGE_POST_THREE)) {
				return JoycoolSpecialUtil.getChatMessage(request,response,0,3);
			}
			//guip在书城添加一条随机聊天记录
			if (content.equals(Constants.SPECIAL_RANDOM_CHAT_MESSAGE_POST_ONE)) {
				return JoycoolSpecialUtil.getChatMessage(request,response,0,1);
			}
			// Liq_2007-3-16_在女性频道中添加动态聊天室信息_end
			
			if (content.equals(Constants.SPECIAL_WGAME_HALL_JINHUA_POST)) {
				return JoycoolSpecialUtil.getWGameHallJinhuaCount(response);
			}
			
			//Liq_2007-3-19-随机获取美容美体最新更新的6条信息_start
			if (content.equals(Constants.SPECIAL_RANDOM_WOMAN_MEIRONG)) {
				return JoycoolSpecialUtil.getWomanMessage(request,response,0);
			}
			//Liq_2007-3-19-随机获取美容美体最新更新的6条信息_end
			
			// Liq_2007-3-19-随机获取美容美体最新更新的6条信息_start
			if (content.equals(Constants.SPECIAL_RANDOM_WOMAN_XINQING)) {
				return JoycoolSpecialUtil.getWomanMessage(request,response,1);
			}
			//Liq_2007-3-19-随机获取美容美体最新更新的6条信息_end
			
//		  Liq_2007-3-19_取得电子书	都市言情栏目最新20本书的随机一本_start
			if (content.equals(Constants.SPECIAL_RANDOM_BOOK_DUSHI)) {
				return JoycoolSpecialUtil.getBook(response,20);
			}
//			  Liq_2007-3-19_取得电子书	都市言情栏目最新20本书的随机一本_end
			
//			  Liq_2007-3-19_取得电子书	西方浪漫栏目最新20本书的随机一本_start
			if (content.equals(Constants.SPECIAL_RANDOM_BOOK_LANGMAN)) {
				return JoycoolSpecialUtil.getBook(response,17);
			}
//			  Liq_2007-3-19_取得电子书	西方浪漫栏目最新20本书的随机一本_end
			
//			  Liq_2007-3-19_取得电子书	审美系列栏目最新20本书的随机一本_start
			if (content.equals(Constants.SPECIAL_RANDOM_BOOK_XILIE)) {
				return JoycoolSpecialUtil.getBook(response,18);
			}
//			  Liq_2007-3-19_取得电子书	审美系列栏目最新20本书的随机一本_end
			
//			  Liq_2007-3-19_取得电子书	网络文学栏目最新20本书的随机一本_start
			if (content.equals(Constants.SPECIAL_RANDOM_BOOK_WANGLUO)) {
				return JoycoolSpecialUtil.getBook(response,24);
			}
//			  Liq_2007-3-19_取得电子书	网络文学栏目最新20本书的随机一本_end
			
//			  Liq_2007-3-19_取得电子书	武侠经典栏目最新20本书的随机一本_start
			if (content.equals(Constants.SPECIAL_RANDOM_BOOK_WUXIA)) {
				return JoycoolSpecialUtil.getBook(response,25);
			}
//			  Liq_2007-3-19_取得电子书	武侠经典栏目最新20本书的随机一本_end
			
			//  Liq_2007-3-19_取得 两性常识,性爱技巧 栏目最新更新的五条内容_start
			if (content.equals(Constants.SPECIAL_NEW_SEX_JIQIAO)) {
				return JoycoolSpecialUtil.getSex(request,response,1);
			}
			//  Liq_2007-3-19_取得 两性常识,性爱技巧 栏目最新更新的五条内容_end
			
			//  Liq_2007-3-19_取得 性健康与性疾病 栏目最新更新的五条内容__start
			if (content.equals(Constants.SPECIAL_NEW_SEX_JIANKANG)) {
				return JoycoolSpecialUtil.getSex(request,response,2);
			}
			//  Liq_2007-3-19_取得 性健康与性疾病 栏目最新更新的五条内容__end
			
			//  Liq_2007-3-19_取得 两性心里，情感故事 栏目最新更新的五条内容_start
			if (content.equals(Constants.SPECIAL_NEW_SEX_QINGGAN)) {
				return JoycoolSpecialUtil.getSex(request,response,3);
			}
			//  Liq_2007-3-19_取得 两性心里，情感故事 栏目最新更新的五条内容_end
			
			// Liq_2007-3-19_在两性频道添加午夜私语动态聊天室信息_start
			if (content.equals(Constants.SPECIAL_RANDOM_CHAT_MESSAGE_SEX_THREE)) {
				return JoycoolSpecialUtil.getSexForum(request,response);
			}
			// Liq_2007-3-19_在两性频道添加午夜私语动态聊天室信息_end
			
			// Liq_2007-3-19_在最新上传的80本书中随机选取5本_start
			if (content.equals(Constants.SPECIAL_RANDOM_EBOOK_80_5)) {
				return JoycoolSpecialUtil.getRandBook(request,response);
			}
			// Liq_2007-3-19_在最新上传的80本书中随机选取5本_end
			
			// Liq_2007-3-19_随机选择5种图书,每种图书选一本_start
			if (content.equals(Constants.SPECIAL_RANDOM_EBOOK_TYPE_5)) {
				return JoycoolSpecialUtil.getTypeBook(request,response);
			}
			// Liq_2007-3-19_随机选择5种图书,每种图书选一本_end
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//  Liq_2007-3-28-乐酷主页,判断是否登录,显示不同的链接_start
			if (content.equals(Constants.SPECIAL_JOYCOOL_LOGIN)) {
				return JoycoolSpecialUtil.getLoginMessage(request,response);
			}
//		  Liq_2007-3-28-乐酷主页,显示两张图片_start
			if (content.equals(Constants.SPECIAL_JOYCOOL_PICTURE)) {
				return JoycoolSpecialUtil.getPhoto(request,response);
			}
			//  Liq_2007-3-28-乐酷主页,显示拍卖大厅一条信息_start
			if (content.equals(Constants.SPECIAL_JOYCOOL_STOCK)) {
				return JoycoolSpecialUtil.getPaimai(request,response);
			}
//			Liq_2007-3-16_在首页中任意一条论坛信息_start
			if (content.equals(Constants.SPECIAL_RANDOM_CHAT_MESSAGE)) {
				return JoycoolSpecialUtil.getRandForum(request,response);
			}
			
//			Liq_2007-3-16_在首页中任意一条交友信息_start
			if (content.equals(Constants.SPECIAL_RANDOM_FRIEND)) {
				return JoycoolSpecialUtil.getFriend(request,response);
			}
//			Liq_2007-3-16_在首页中添加第一的帮会信息_start
			if (content.equals(Constants.SPECIAL_FIRST_TONG)) {
				return JoycoolSpecialUtil.getTong(request,response);
			}
//			Liq_2007-3-16_在首页中添加日记信息_start
			if (content.equals(Constants.SPECIAL_FIRST_DIARY)) {
				return JoycoolSpecialUtil.getDiary(request,response);
			}
//			  Liq_2007-3-19_取得电子书	禁书20本书的随机一本_start
			if (content.equals(Constants.SPECIAL_RANDOM_BOOK_SEX)) {
				return JoycoolSpecialUtil.getBook(response,19);
			}
//		  Liq_2007-3-19_取得游戏栏目最新20个中的随机一个_start
			if (content.equals(Constants.SPECIAL_RANDOM_GAME)) {
				return JoycoolSpecialUtil.getGame(response);
			}
//  Liq_2007-3-19_取得铃声栏目最新20个中的随机一个_start
			if (content.equals(Constants.SPECIAL_RANDOM_RING)) {
				return JoycoolSpecialUtil.getRing(response);
			}
//		  Liq_2007-3-19_首页添加大盘指数_start
			if (content.equals(Constants.SPECIAL__STOCK)) {
				return JoycoolSpecialUtil.getSTOCK(request,response);
			}
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// mcq_2006-8-18_添加游戏搜索_start
			if (content.equals(Constants.SPECIAL_GAME_SEARCH_POST)) {
				return JoycoolSpecialUtil.getGameSearchPost(response);
			}
			// mcq_2006-8-18_添加游戏搜索_end

			// mcq_2006-8-18_添加手机游戏下载搜索_start
			if (content.equals(Constants.SPECIAL_GAME_MOBILE_POST)) {
				return JoycoolSpecialUtil.getGameMobileListPost(response);
			}
			// mcq_2006-8-18_添加手机游戏下载搜索_end
			
			// mcq_2007-3-6_添加动态聊天室信息_start
			if (content.equals(Constants.SPECIAL_RANDOM_CHAT_MESSAGE_POST)) {
				return JoycoolSpecialUtil.getChatMessage(request,response,0,2);
			}
			// mcq_2007-3-6_添加动态聊天室信息_end
			
			// mcq_2007-3-6_推荐网友图片_start
			if (content.equals(Constants.SPECIAL_PHOTO_MARK_POST)) {
				return JoycoolSpecialUtil.getPhotoMark(response);
			}
			// mcq_2007-3-6_推荐网友图片_end
			
			// mcq_2006-8-18_添加铃声下载搜索_start
			if (content.equals(Constants.SPECIAL_RING_SEARCH)) {
				return JoycoolSpecialUtil.getRingSearch(response);
			}
			// mcq_2006-8-18_添加铃声下载搜索_end
			
			// lbj_2007_08_07 随机取卖场信息_start
			if (content.indexOf(WxsjConstants.SPECIAL_RANDOM_MALL_INFO) != -1) {
				return WxsjConstants.getRandomMallInfo(content, response);
			}
			// lbj_2007_08_07 随机取卖场信息_end

			// liuyi 2007-02-05 自建站推广链接 start
			content = content.replace("%JCSP%RANDOM_WAPSITE", ""
					+ JoycoolSpecialUtil.getRandomLink(response));
			// liuyi 2007-02-05 自建站推广链接 end

			// liuyi 2007-02-13 随机刷新项目 start
			content = content.replace("%JCSP%RANDOM_ONE_LINK1", ""
					+ JoycoolSpecialUtil.getRandomLink((response),
							JoycoolSpecialUtil.randomLinks1));
			// liuyi 2007-02-13 随机刷新项目 end
		}
		String linkAddress = line.getLinkURL();

		if (line.getLinkURL().equals("")) {
			// 数据中有backT0
			return content + line.getLineEnd();
		}

		if (line.getLinkType() == JaLineBean.LT_LINK
				|| line.getLinkType() == JaLineBean.LT_IMAGE
				|| line.getLinkType() == JaLineBean.LT_IMAGE_NO_THUMBNAIL
				|| line.getLinkType() == JaLineBean.LT_IMAGE_CATALOG
				|| line.getLinkType() == JaLineBean.LT_NEWS
				|| line.getLinkType() == JaLineBean.LT_NEWS_CATALOG
				|| line.getLinkType() == JaLineBean.LT_EBOOK
				|| line.getLinkType() == JaLineBean.LT_EBOOK_CATALOG
				|| line.getLinkType() == JaLineBean.LT_GAME
				|| line.getLinkType() == JaLineBean.LT_GAME_CATALOG
				|| line.getLinkType() == JaLineBean.LT_RING
				|| line.getLinkType() == JaLineBean.LT_RING_CATALOG
				|| line.getLinkType() == JaLineBean.LT_VIDEO
				|| line.getLinkType() == JaLineBean.LT_VIDEO_CATALOG) {
			/*
			 * if (linkAddress.indexOf("?") != -1) { linkAddress +=
			 * "&amp;jaLineId=" + line.getId() + "&amp;backTo=" +
			 * PageUtil.getBackTo(request); } else { linkAddress += "?jaLineId=" +
			 * line.getId() + "&amp;backTo=" + PageUtil.getBackTo(request); }
			 */

			// liuyi 2007-02-13 wtai处理 start
//			if (linkAddress.indexOf("?") != -1) {
//				if (linkAddress.indexOf("wtai") == -1) {
//					linkAddress += "&amp;jaLineId=" + line.getId();
//				}
//			} else {
//				if (linkAddress.indexOf("wtai") == -1) {
//					linkAddress += "?jaLineId=" + line.getId();
//				}
//			}
			// liuyi 2007-02-13 wtai处理 end
		}

		/**
		 * WAP业务 对链接地址进行处理
		 */
		String unique = null;
		int wapType = JoycoolSpecialUtil.getWapType(line);
		if (wapType != 0) {
			canCache = false;
			HttpSession session = request.getSession(false);
			if(session != null) {
				WapServiceBean wapService = WapServiceUtil
						.getWapServiceById(wapType);
				if (wapService != null) {
					unique = StringUtil.getUnique();
					Hashtable urlMap = JoycoolSessionListener.getUrlMap(session.getId());
					if (urlMap != null) {
						urlMap.put(unique, linkAddress.replace("&amp;", "&"));
					}
					linkAddress = wapService.getOrderAddress() + "?unique="
							+ unique;
				}
			}
		}

		// add by zhangyi
		if (linkAddress.indexOf("amp;backTo") != -1) {
			linkAddress = linkAddress.substring(0, linkAddress
					.indexOf("amp;backTo") - 1);
		}
		if (linkAddress.indexOf("backTo") != -1) {
			linkAddress = linkAddress.substring(0, linkAddress
					.indexOf("backTo") - 1);
		}

		// 读缩略图显示
		if (line.getLinkType() == JaLineBean.LT_IMAGE
				&& line.getDisplayType() == JaLineBean.DT_NULL_TEXT_NULL) {
			StringBuilder sb = new StringBuilder();
			sb.append("<a href=\"" + (linkAddress) + "\">"
					+ content + "</a><br/>");
			ImageBean image = imageService.getImage("id = " + line.getLink());
			if (image != null && image.getFile7070() != null) {
				sb.append("<img src=\"" + image.getFile7070().getRealFileUrl()
						+ "\" alt=\"" + image.getName() + "\"/>");
				sb.append(line.getLineEnd());
			}
			return sb.toString();

		} else {
			StringBuilder sb = new StringBuilder(60);
			sb.append("<a href=\"");
			sb.append(linkAddress);
			sb.append("\">");
			sb.append(content);
			sb.append("</a>");
			sb.append(line.getLineEnd());
			return  sb.toString();
		}
	}

	public boolean match(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
}
