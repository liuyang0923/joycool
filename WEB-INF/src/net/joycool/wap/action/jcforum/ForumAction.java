package net.joycool.wap.action.jcforum;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.CartBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.bean.jcforum.ForumRcmdBean;
import net.joycool.wap.bean.jcforum.ForumRcmdBean2;
import net.joycool.wap.bean.jcforum.ForumReplyBean;
import net.joycool.wap.bean.jcforum.ForumUserBean;
import net.joycool.wap.bean.jcforum.ForumVoteBean;
import net.joycool.wap.bean.jcforum.PrimeCatBean;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.JcForumServiceImpl;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.servlet.SqlThread;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class ForumAction extends CustomAction{

	public static String ATTACH_ROOT = "/usr/local/joycool-rep/jcforum";

	// public static String ATTACH_ROOT =
	// "E:/eclipse/workspace/joycool-portal/img/jcforum";

	public static String ATTACH_URL_ROOT = "/rep";

	// public static String ATTACH_URL_ROOT =
	// "/img/jcforum/";

	UserBean loginUser = null;
	ForumUserBean forumUser = null;
	public static int PERIOD_INTERVAL_DAY = 30;

	public static int NUMBER_PER_PAGE = 10;

	public static HashMap rcmdMap = null;		// 精贴推荐Map
	public static HashMap rcmdMap2 = null;	// 论坛推荐管理
	
	public static byte[] initLock = new byte[0];
	
	static JcForumServiceImpl forumService = new JcForumServiceImpl();

	static IUserService userService = ServiceFactory.createUserService();

	public ForumAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
		if(loginUser != null && loginUser.getId() > 0) {
			forumUser = ForumCacheUtil.getAddForumUser(loginUser.getId());
		}
	}

	public static IUserService getUserService() {
		return userService;
	}

	public static JcForumServiceImpl getForumService() {
		return forumService;
	}

	// 取得精贴推荐
	public static HashMap getRcmdMap() {
		if (rcmdMap != null){
			return rcmdMap;
		}
		synchronized (initLock){
			if (rcmdMap != null){
				return rcmdMap;
			}
			rcmdMap = forumService.getRcmdMap(" 1 limit 50;");
		}
		return rcmdMap;
	}
	
	// 把精贴的map转为list
	public static List getRcmdList(){
		if (rcmdMap == null){
			return new ArrayList();
		} else {
			return new ArrayList(rcmdMap.keySet());
		}
	}
	
	// 根据精贴的KEY取得一个精贴
	public static ForumRcmdBean getRcmd(int key){
		return (ForumRcmdBean)rcmdMap.get(new Integer(key));
	}
	
	// 论坛推荐管理
	public static HashMap getRcmdMap2() {
		if (rcmdMap2 != null){
			return rcmdMap2;
		}
		synchronized (initLock){
			if (rcmdMap2 != null){
				return rcmdMap2;
			}
			rcmdMap2 = forumService.getRcmdMap2(" 1;");
		}
		return rcmdMap2;
	}
	
	// 把论坛推荐管理的map转为list
	public static List getRcmdList2(){
		if (rcmdMap2 == null){
			return new ArrayList();
		} else {
			return new ArrayList(rcmdMap2.keySet());
		}
	}
	
	// 根据论坛推荐管理的KEY取得一个精贴
	public static ForumRcmdBean2 getRcmd2(int key){
		return (ForumRcmdBean2)rcmdMap2.get(new Integer(key));
	}
	
	/**
	 * 取得一个随机数
	 * total:总范围,count:要显示的数量
	 */
	public static int getRandomInt(int start,int total,int count){
		if (total == 0 || count == 0){
			return 0;
		} else if (total <= count){
			return 0;
		}
		int end = total - count;
		return RandomUtil.nextInt(start, end);
	}
	
	/**
	 * 随机取得N个乐酷精贴
	 */
	public static List getRcmdContent(int count){
		if (count <= 0){
			return new ArrayList();
		} else {
			int start = getRandomInt(0,rcmdMap.size(),count);
			int end = start + count;
			if (rcmdMap.size() <= count){
				end = rcmdMap.size();
			}
			List tmpList = getRcmdList();
			List list = new ArrayList();
			if (tmpList != null){
				for (int i = start ; i < end ; i++){
					list.add(getRcmd(((Integer)tmpList.get(i)).intValue()));
				}
			}
			return list;
		}
	}
	
	/**
	 * 随机取得N个推荐论坛(count:每个论坛下显示几个帖子。如果count<=0则令count=3)
	 */
	public static List getRcmdForum(int count){
		if (count <=0){
			count = 3;
		}
		// 取得今天的星期
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int week = c.get(java.util.Calendar.DAY_OF_WEEK);
		if (week == 1){
			week = 7;
		} else {
			week--;
		}
		ForumRcmdBean2 rcmdBean2 = null;
		List list = new ArrayList();
		List tmpList = getRcmdList2();
		if (tmpList != null){
			for (int i = 0 ; i < tmpList.size() ; i++){
				rcmdBean2 = getRcmd2(((Integer)tmpList.get(i)).intValue());
				if (rcmdBean2 != null && rcmdBean2.getWeek() == week){
					list.add(rcmdBean2);
				}
			}
		}
		return list;
	}
	
	/**
	 * 取得某一论坛的N个精品贴
	 */
	public List getPrime(int forumId,int count){
		if (forumId <= 0 || count <= 0){
			return new ArrayList();
		}
		return SqlUtil.getIntList("select id from jc_forum_prime where forum_id=" + forumId + " order by id desc limit " + count,2);
	}
	
	/**
	 * 
	 * @author macq
	 * @explain： 我的主题
	 * @datetime:2007-6-11 1:30:17
	 * @param request
	 * @param response
	 * @return void
	 */
	public void myTopic(HttpServletRequest request, HttpServletResponse response) {
		String tip = null;
		if (loginUser == null) {
			String url = response
					.encodeURL("/user/login.jsp?backTo=/jcforum/index.jsp");
			String link = "<a href=\"" + url + "\">登录</a>";
			tip = "您还没有登录，请" + link + "后查看您所发表的主题!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}
		int forumId = getParameterIntS("f");
		if (forumId <= 0) {
			tip = "参数错误！";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}

		ForumBean forum = ForumCacheUtil.getForumCache(forumId);
		if (forum == null) {
			tip = "参数错误！";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}
		String sql = "select id from jc_forum_content where forum_id="
				+ forumId + " and user_id=" + loginUser.getId()
				+ " and del_mark=0 order by id desc";
		List forumContentIdList = SqlUtil
				.getIntList(sql, 2);

		int totalCount = forumContentIdList.size();
		
		PagingBean page = new PagingBean(this, totalCount, NUMBER_PER_PAGE, "p");
		request.setAttribute("page", page);

		int start = page.getStartIndex();
		int end = page.getEndIndex();
		List forumContentIdList1 = forumContentIdList.subList(start, end);

		request.setAttribute("forumContentIdList", forumContentIdList1);
		request.setAttribute("forum", forum);
		request.setAttribute("result", "success");
		return;
	}
	
	/**
	 * 搜索用户的所有主题
	 * @param request
	 * @param response
	 */
	public void userTopic() {
		String tip = null;

		int userId = getParameterInt("u");			// 如果是0则查询自己的？
		UserBean user = null;
		if(userId > 0)
			user = UserInfoUtil.getUser(userId);
			
		if (user == null) {
			tip(null, "该用户不存在！");
			return;
		}
		setAttribute("user", user);

		String sql = "select id from jc_forum_content where user_id=" + userId
				+ " and del_mark=0 order by id desc";
		List forumContentIdList = SqlUtil
				.getIntList(sql, 2);

		int totalCount = forumContentIdList.size();
		
		PagingBean page = new PagingBean(this, totalCount, NUMBER_PER_PAGE, "p");
		request.setAttribute("page", page);

		int start = page.getStartIndex();
		int end = page.getEndIndex();
		List forumContentIdList1 = forumContentIdList.subList(start, end);

		request.setAttribute("forumContentIdList", forumContentIdList1);
		tip("success");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：查询一个用户的发贴
	 * @datetime:2007-7-25 10:32:16
	 * @param request
	 * @param response
	 * @return void
	 */
	public void searchUserRs(HttpServletRequest request,
			HttpServletResponse response) {
		String tip = null;
		int forumId = getParameterInt("forumId");
		if (forumId <= 0) {
			doTip(null, null);
			return;
		}
		ForumBean forum = ForumCacheUtil.getForumCache(forumId);
		if (forum != null) {
			request.setAttribute("forum", forum);
		}
		String cond;
		int userId = getParameterInt("userId");
		String key = getParameterNoEnter("key");
		if (userId > 0) {

			UserBean user = UserInfoUtil.getUser(userId);
			if (user == null) {
				doTip(null, "该用户不存在");
				return;
			}
			
			cond = "forum_id="
				+ forumId + " and user_id=" + userId
				+ " and del_mark=0";
			request.setAttribute("prefixUrl", "searchUserRs.jsp?forumId=" + forumId + "&amp;userId=" + userId);
		} else if(key != null && key.length() > 0) {
			int period = getParameterInt("per");
			cond = "forum_id="
				+ forumId + " and last_re_time between date_sub(now(),interval " + ((period+1)*PERIOD_INTERVAL_DAY) + " day) and date_sub(now(),interval " + (period*PERIOD_INTERVAL_DAY) + " day) and title like '%" + StringUtil.toSql(key)
				+ "%' and del_mark=0";
			request.setAttribute("prefixUrl", "searchUserRs.jsp?forumId=" + forumId + "&amp;key=" + StringUtil.toWml(key));
		} else {
			doTip(null, null);
			return;
		}

		String sql = "select count(id) from jc_forum_content where " + cond;
		int totalCount = SqlUtil.getIntResult(sql, 2);
		
		PagingBean page = new PagingBean(this, totalCount, NUMBER_PER_PAGE, "p");
		request.setAttribute("page", page);
		
		sql = "select id from jc_forum_content where " + cond + " order by id desc limit " + page.getStartIndex() + "," + NUMBER_PER_PAGE;
		List forumContentIdList = SqlUtil.getIntList(sql, 2);

		String prefixUrl = "searchUserRs.jsp?forumId=" + forumId
				+ "&amp;userId=" + userId;
		request.setAttribute("forumContentIdList", forumContentIdList);
		
		request.setAttribute("result", "success");
	}

	public void forum(HttpServletRequest request, HttpServletResponse response) {
 		int forumId = getParameterInt("forumId");
		if(forumId<=0){
			forumId=1;
		}
		request.setAttribute("forumId", String.valueOf(forumId));
		String contents = request.getParameter("content");
		String title = getParameterNoEnter("title");
		String n = request.getParameter("n");
		int sid = this.getParameterInt("sid");
		
		String tip = null;
		if (contents != null || title != null) {
			contents = StringUtil.removeCtrlAsc(contents.trim());
			int type = getParameterInt("t");
			if (contents == null || contents.length() < 10) {
				tip = "帖子内容不能少于10个字!";
				request.setAttribute("tip", tip);
				return;
			}
			if (contents.length() > 5000) {
				contents = contents.substring(0, 5000);
			}
			if (title == null || title.length() == 0) {
				tip = "主题标题不能为空!";
				request.setAttribute("tip", tip);
				return;
			}
			
			if (title.length() > 30) {
				title = title.substring(0, 30);
			}
			if (session.getAttribute("forumcontent") != null) {
				String infos = (String) session.getAttribute("fourmrepeat");
				String info = title;
				if (loginUser != null) {
					info = title + " " + loginUser.getId();
				}
				if (!(info.equals(infos))) {
					if (forumId > 0) {
						ForumBean forum = ForumCacheUtil.getForumCache(forumId);
						if (!isCooldown("fourmSTime", 20 * 1000)) {
							tip = "你的发文太快了！请先休息一会再继续！";
							request.setAttribute("tip", tip);
							return;
						}
						if (forum.getType() == 1 && loginUser == null) {
							String url = response
									.encodeURL("/user/login.jsp?backTo=/jcforum/index.jsp");
							String link = "<a href=\"" + url + "\">登录</a>";
							tip = "此版面不允许游客发言，请" + link + "后在发言!";
							request.setAttribute("tip", tip);
							return;
						}
						if(forum.getType() == 2) {
							request.setAttribute("tip", "此版面不允许发言!");
							return;
						}
						ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("forum",loginUser.getId());
						if(forbid != null) {
							doTip("tip", "已经被禁止发贴 - " + forbid.getBak());
							return;
						}
						forbid = ForbidUtil.getForbid("f" + forumId, loginUser.getId());
						if(forbid != null) {
							doTip("tip", "已经被禁止发贴 - " + forbid.getBak());
							return;
						}
						
						ForumContentBean contentBean = new ForumContentBean();
						contentBean.setForumId(forumId);
						contentBean.setType(type);
						//增加匿名发帖 start
						if(n != null && n.equals("n") && type == 0) {
							int userBagId = UserBagCacheUtil.getUserBagById(
									ForumContentBean.NICK_ITEM, loginUser.getId());
							if(userBagId <= 0) {
								tip = "没有匿名主题卡,不能发表匿名帖";
								request.setAttribute("tip", tip);
								return;
							}
							// 更新物品使用次数
							UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),userBagId);
							contentBean.setNickTopic();
						}
						//增加匿名发帖 end
						
						if(sid > 0 && type == 0) {
							int userBagId = UserBagCacheUtil.getUserBagById(
									ForumContentBean.SPARK_ITEM, loginUser.getId());
							if(userBagId <= 0) {
								tip = "没论坛闪耀卡,不能发闪耀帖";
								request.setAttribute("tip", tip);
								return;
							}
							// 更新物品使用次数
							UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),userBagId);
							contentBean.setVipType(sid);
						}
						contents = contents.replace(session.getId(), "abcd01");
						if(type == 0) {
							contentBean.setContent(contents);
						} else if(type == 1){
							StringBuilder sb = new StringBuilder(64);
							sb.append(contents);
							sb.append("||");
							String options[] = request.getParameterValues("option");
							options[0] = StringUtil.noEnter(options[0]);
							if(options.length > 0)
								sb.append(options[0]);

							int i = 1;
							for(;i < options.length;i++) {
								options[i] = StringUtil.noEnter(options[i]);
								if(options[i].length() == 0)
									break;
								sb.append("|");
								sb.append(options[i]);
							}
							if(i < 2) {
								tip = "投票选项太少，请重新设置！";
								request.setAttribute("tip", tip);
								return;	
							}
							contentBean.setContent(sb.toString());
						} else {
							request.setAttribute("tip", "无法发表.");
							return;
						}
						if (loginUser != null) {
							contentBean.setUserId(loginUser.getId());
						} else {
							contentBean.setUserId(0);
						}

						contentBean.setTitle(title);

						ForumCacheUtil.addForumContent(contentBean, forum);
						if(!contentBean.isNickTopic()) {
							if(type == 0)
								ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_FORUM_CONTENT, "%1发表了帖子%3", loginUser.getNickName(), title, "/jcforum/viewContent.jsp?contentId="+contentBean.getId());			
							else if(type == 1)
								ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_FORUM_CONTENT, "%1发起投票%3", loginUser.getNickName(), title, "/jcforum/viewContent.jsp?contentId="+contentBean.getId());
						}
						if (loginUser != null) {
							// 更改发贴赠送积分为3分
							RankAction.addPoint(loginUser, 3);
							forumUser.addThreadCount();
							ForumCacheUtil.updateForumUser(forumUser);
						}
						session.removeAttribute("forumcontent");
						session.setAttribute("fourmrepeat", info);
						request.setAttribute("result", "setvar");
					}
				} else {
					tip = "包含相同的主题，不能连续发两遍.";
					request.setAttribute("tip", tip);
					return;
				}
			}

		}
		// 删帖
		String del = request.getParameter("del");
		if (del != null) {
			int delId = StringUtil.toInt(del);
			if (delId <= 0) {
				tip = "参数错误!";
				request.setAttribute("tip", tip);
				request.setAttribute("forumId", forumId + "");
				return;
			}
			ForumContentBean cons = ForumCacheUtil.getForumContent(delId);
			if (cons == null) {
				tip = "该帖不存在!";
				request.setAttribute("tip", tip);
				request.setAttribute("forumId", forumId + "");
				return;
			}
			if (cons.getCreateTime().getTime() - System.currentTimeMillis() < -oldTime) {
				tip = "主题太古老，无法删除";
				request.setAttribute("tip", tip);
				request.setAttribute("forumId", forumId + "");
				return;
			}
			if (cons.getMark1() == 1 || cons.isPeak()) {
				tip = "无法删除精华帖或者置顶贴";
				request.setAttribute("tip", tip);
				request.setAttribute("forumId", forumId + "");
				return;
			}
			if (cons.isNews()) {
				tip = "无法删除新闻帖";
				request.setAttribute("tip", tip);
				request.setAttribute("forumId", forumId + "");
				return;
			}
			if (cons.isReadonly()) {
				tip = "该帖已设置为只读，无法删除";
				request.setAttribute("tip", tip);
				request.setAttribute("forumId", forumId + "");
				return;
			}
			if(loginUser.getUserSetting() == null || !loginUser.getUserSetting().isFlagTopicDelete() && !hasParam("c")) {
				tip("confirm");
				request.setAttribute("cons", cons);
				return;
			}
			ForumBean forum = ForumCacheUtil.getForumCache(cons.getForumId());
			// 系统管理员直接删除
			if (isSuperAdmin(loginUser.getId())) {
				ForumCacheUtil.deleteForumContent(cons, forum, loginUser.getId());
				ForumUserBean fu = ForumCacheUtil.getForumUser(cons.getUserId());
				if(fu != null) {
					fu.decExp(10);
					if(fu.getThreadCount() > 0)
						fu.setThreadCount(fu.getThreadCount() - 1);
					ForumCacheUtil.updateForumUser(fu);
				}
			} else if (forum.getUserIdSet().contains(
					new Integer(loginUser.getId()))) {
				ForumCacheUtil.deleteForumContent(cons, forum, loginUser.getId());
				ForumUserBean fu = ForumCacheUtil.getForumUser(cons.getUserId());
				if(fu != null) {
					fu.decExp(5);
					if(fu.getThreadCount() > 0)
						fu.setThreadCount(fu.getThreadCount() - 1);
					ForumCacheUtil.updateForumUser(fu);
				}
			} else if(loginUser.getId() == cons.getUserId()){// 否则帖子标题跟内容志空
				ForumCacheUtil.updateForumContent(
						"content='',attach=''",
						"id=" + cons.getId(), cons.getId());
//				if(cons.getAttach().length() > 5) {	// o.gif等待审核
//					File f = new File(ForumAction.ATTACH_ROOT, cons.getAttach());
//					if (f.exists())
//						f.delete();
//				}
			}

		}
		String prefixUrl = "forum.jsp?forumId=" + forumId;

		List toplist = ForumCacheUtil.getForumPeakMap(0);
		if (toplist == null)
			toplist = new Vector();
		List peaklist = ForumCacheUtil.getForumPeakMap(forumId);
		if (peaklist == null)
			peaklist = new Vector();

		String cond = "";
		String from = " from jc_forum_content where forum_id=" + forumId;
		int type = getParameterInt("type");
		ForumBean forum = null;
		int totalCount = 0;
		if (forumId > 0) {
			forum = ForumCacheUtil.getForumCache(forumId);
			if(forum == null)
				return;
			if (type == 2) {
				cond = from + " and mark2=0 and del_mark=0";
				
				prefixUrl = "forum.jsp?type=2&amp;forumId=" + forumId;
//				totalCount = forum.getTotalCount();
//				if(totalCount > 50)
//					totalCount = 50;
			} else if (type == 1) {

				cond = " from jc_forum_prime where forum_id=" + forumId;
				
				prefixUrl = "forum.jsp?type=1&amp;forumId=" + forumId;
				totalCount = forum.getPrimeCount();
			} else {
				cond = from + " and mark2=0 and del_mark=0";
				totalCount = forum.getTotalCount() + toplist.size() + peaklist.size();
			}

		} else 
			return;
		if(totalCount > 10000)
			totalCount = 10000;	// 最多显示最近的10000帖，超过的无法翻页查看
		PagingBean page = new PagingBean(this, totalCount, NUMBER_PER_PAGE, "p", "go");
		request.setAttribute("page", page);
		
		Vector contentL = new Vector();
		
		int start = page.getStartIndex();
		int end = page.getEndIndex();
		// 由于置顶和总置顶最多15记录，所以第1，2页需要进行特别处理
		if(type == 0) {
			if(start == 0) {	// 第一页，显示总置顶，外加10条
				for (int i = 0; i < toplist.size(); i++) {
					int content = ((Integer) toplist.get(i)).intValue();
					ForumContentBean cons = ForumCacheUtil.getForumContent(content);
					if (cons != null)
						contentL.add(cons);
				}
			}
			
			for (int i = start; i < peaklist.size() && i < end; i++) {
				int content = ((Integer) peaklist.get(i)).intValue();
	
				ForumContentBean cons = ForumCacheUtil.getForumContent(content);
				if (cons != null)
					contentL.add(cons);
	
			}
		}
		
		int lstart = start;
		if(type == 0)
			lstart -= peaklist.size();
		int llimit = NUMBER_PER_PAGE;
		if(lstart < 0) {
			llimit += lstart ;
			lstart = 0;
			if(llimit < 0)
				llimit = 0;
		}
		
		String limit = " limit " + lstart + "," + llimit;
		List contentList = null;
		if (forumId > 0) {
			if (type == 2) {
				// 取得最近1000帖的前100人气排行
				contentList = SqlUtil.getIntListCache("select id from (select id,`count`" + cond + " order by last_re_time desc limit 1000) a order by `count` desc limit 100", 3600, 2);
				page = new PagingBean(this, contentList.size(), NUMBER_PER_PAGE, "p", "go");
				request.setAttribute("page", page);
				contentList = contentList.subList(page.getStartIndex(), page.getEndIndex());
				
				prefixUrl = "forum.jsp?type=2&amp;forumId=" + forumId;
			} else if (type == 1) {

				contentList = SqlUtil.getIntList("select id" + cond + " order by id desc" + limit, 2);
				
				prefixUrl = "forum.jsp?type=1&amp;forumId=" + forumId;
			} else {
				contentList = ForumCacheUtil.getContentsCache(forum.getId(), lstart, llimit);
				if(contentList == null)
					contentList = SqlUtil.getIntList("select id" + cond + " order by last_re_time desc" + limit, 2);
			}

		}

		for (int i = 0; i < contentList.size(); i++) {
			int content = ((Integer) contentList.get(i)).intValue();

			ForumContentBean cons = ForumCacheUtil.getForumContent(content);
			if (cons != null)
				contentL.add(cons);

		}
		
		String popedom = null;
		String users[] = forum.getUserId().split("_");
		boolean manager = false;
		if (loginUser != null) {
			for (int i = 0; i < users.length; i++) {
				if (loginUser.getId() == StringUtil.toInt(users[i])) {
					manager = true;
					break;
				}

			}

			if (isSuperAdmin(loginUser.getId())) {
				popedom = "super";
			} else if (manager) {
				popedom = "manager";
			} else {
				popedom = "normal";
			}
		}

		request.setAttribute("forumId", forumId + "");
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("contentList", contentL);
		request.setAttribute("forum", forum);
		request.setAttribute("popedom", popedom);
	}
	
	public void top10(HttpServletRequest request) {
		List top10 = ForumCacheUtil.getForumTop10();
		request.setAttribute("top10", top10);
	}
	
	public void history() {
 		int forumId = getParameterInt("forumId");

		String tip = null;

		String from = " from jc_forum_content_his where forum_id=" + forumId;
		String cond = from;
		ForumBean forum = ForumCacheUtil.getForumCache(forumId);
		
		Vector contentL = new Vector();

		// 取得要显示的消息列表
		int totalCount = SqlUtil.getIntResultCache("select count(*) " + cond, 60 * 6, 2);
		PagingBean page = new PagingBean(this, totalCount, NUMBER_PER_PAGE, "p");
		request.setAttribute("page", page);
		
		int start = page.getStartIndex();
		
		String limit = " limit " + start + "," + NUMBER_PER_PAGE;
		List contentList = SqlUtil.getIntList("select id" + cond + " order by id desc" + limit, 2);

		for (int i = 0; i < contentList.size(); i++) {
			int content = ((Integer) contentList.get(i)).intValue();

			ForumContentBean cons = ForumCacheUtil.getForumContentHis(content);
			if (cons != null)
				contentL.add(cons);
		}

		request.setAttribute("forumId", String.valueOf(forumId));
		request.setAttribute("contentList", contentL);
		request.setAttribute("forum", forum);
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-5-24 5:46:23
	 * @param request
	 * @return void
	 */
	public void bUser(HttpServletRequest request) {
		String tip = null;
		int forumId = StringUtil.toInt(request.getParameter("forumId"));
		if (forumId <= 0) {
			tip = "参数错误!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}
		ForumBean forum = ForumCacheUtil.getForumCache(forumId);
		if (forum == null) {
			tip = "参数错误!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}
		// String users[] = forum.getUserId().split("_");
		// boolean manager = false;
		if (loginUser == null) {
			tip = "您必须登录才可以执行此操作!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}
		ForbidUtil.ForbidGroup fgroup = ForbidUtil.getGroup("f" + forumId);
		request.setAttribute("fgroup", fgroup);
		String op = request.getParameter("op");
		if (op != null) {
			if (!forum.getUserIdSet().contains(new Integer(loginUser.getId()))) {
				if (!isSuperAdmin(loginUser.getId())) {
					tip = "您不是管理员不能执行此操作!";
					request.setAttribute("tip", tip);
					request.setAttribute("result", "failure");
					return;
				}
			}
			int userId = StringUtil.toInt(request.getParameter("userId"));
			if (userId <= 0) {
				tip = "用户id输入有误！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "op");
				request.setAttribute("forum", forum);
				return;
			}
			if (userId == 519610 || userId == 431 || userId == 914727 || userId == 100) {	// 只加管理员其他特殊帐号不算
				tip = "系统管理员不允许被封禁!";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "op");
				request.setAttribute("forum", forum);
				return;
			}
			if (forum.getUserIdSet().contains(new Integer(userId))) {
				tip = "版主不允许被封禁!";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "op");
				request.setAttribute("forum", forum);
				return;
			}
			UserBean user = UserInfoUtil.getUser(userId);
			if (user == null) {
				tip = "封禁用户不存在！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "op");
				request.setAttribute("forum", forum);
				return;
			}
			
			if (op.equals("add")) {// 添加封禁列表
				if (fgroup.isForbid(user.getId())) {
					tip = "该用户已在封禁列表中！";
					request.setAttribute("tip", tip);
					request.setAttribute("result", "op");
					request.setAttribute("forum", forum);
					return;
				}
				String bak = getParameterNoCtrl("bak");
				if(bak == null)
					bak = "";
				int interval = getParameterInt("per");
				if(interval < 60) interval = 60;
				if(interval>43200) interval = 43200;
				fgroup.addForbid(new ForbidUtil.ForbidBean(userId, loginUser.getId(), bak), interval);
				AdminAction.addForbidLog(forum.getTitle() + forum.getId(), userId, loginUser.getId(), bak, interval, 0);

				tip = "封禁操作成功！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "op");
				request.setAttribute("forum", forum);
				return;
			} else if (op.equals("del")) {// 删除封禁列表
				if (!fgroup.isForbid(user.getId())) {
					tip = "改用户不在封禁列表中！";
					request.setAttribute("tip", tip);
					request.setAttribute("result", "op");
					request.setAttribute("forum", forum);
					return;
				}
				fgroup.deleteForbid(user.getId());
				AdminAction.addForbidLog(forum.getTitle() + forum.getId(), userId, loginUser.getId(), "", 0, 1);
				request.setAttribute("tip", "解封操作成功！");
				request.setAttribute("result", "op");
				request.setAttribute("forum", forum);
				return;
			}
		}
		request.setAttribute("forum", forum);
		request.setAttribute("result", "success");
	}
	
	public void rule() {
		String tip = null;
		int forumId = StringUtil.toInt(request.getParameter("forumId"));
		if (forumId <= 0) {
			doTip();
			return;
		}
		ForumBean forum = ForumCacheUtil.getForumCache(forumId);
		if (forum == null) {
			doTip();
			return;
		}
		// String users[] = forum.getUserId().split("_");
		// boolean manager = false;
		if (loginUser == null) {
			doTip(null, "您必须登录才可以执行此操作!");
			return;
		}
		request.setAttribute("forum", forum);
		
		ForbidUtil.ForbidGroup fgroup = ForbidUtil.getGroup("f" + forumId);
		request.setAttribute("fgroup", fgroup);
		String rule = getParameterString("rule");
		
		boolean isAdmin = true;
		if (!forum.getUserIdSet().contains(new Integer(loginUser.getId()))) {
			if (!isSuperAdmin(loginUser.getId())) {
				isAdmin = false;
			}
		}
		if(isAdmin)
			setAttribute("admin", "");
		
		if (rule != null) {
			if (!isAdmin) {
				doTip(null, "只有版主才能执行此操作!");
				return;
			}
			if(rule.length() > 1000)
				rule = rule.substring(0, 1000);
			forum.setRule(rule);
			forumService.updateForum("rule='" + StringUtil.toSql(rule) + "'", "id=" + forum.getId());
			doTip("op", "修改版规成功!");
			return;
		}
		request.setAttribute("result", "success");
		
	}

	/**
	 * 
	 * @author macq
	 * @explain：查看版主
	 * @datetime:2007-7-16 13:28:44
	 * @param request
	 * @return void
	 */
	public void viewAdmin(HttpServletRequest request) {
		String tip = null;
		int forumId = StringUtil.toInt(request.getParameter("forumId"));
		if (forumId <= 0) {
			doTip(null, null);
			return;
		}
		ForumBean forum = ForumCacheUtil.getForumCache(forumId);
		if (forum == null) {
			doTip(null, null);
			return;
		}
		// String users[] = forum.getUserId().split("_");
		// boolean manager = false;
		request.setAttribute("forum", forum);
		request.setAttribute("result", "success");
	}

	/**
	 * 
	 * @author macq
	 * @explain：帖子续写
	 * @datetime:2007-7-6 9:40:47
	 * @param request
	 * @return void
	 */
	public void contentWrite(HttpServletRequest request) {
		String tip = null;
		if (loginUser == null) {
			tip = "您必须登录才可以执行此操作!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}
		int contentId = StringUtil.toInt(request.getParameter("contentId"));
		if (contentId <= 0) {
			doTip(null, null);
			return;
		}
		ForumContentBean content = ForumCacheUtil.getForumContent(contentId);
		if (content == null) {
			doTip(null, null);
			return;
		}
		if (content.getUserId() != loginUser.getId()) {
			doTip(null, null);
			return;
		}
		request.setAttribute("content", content);
		if(content.getContent().length() > 50000) {
			doTip("failure", "帖子内容已经太长,无法续写");
			return;
		}
		
		request.setAttribute("result", "success");
	}

	/**
	 * 
	 * @author macq
	 * @explain：帖子续写结果
	 * @datetime:2007-7-6 9:49:35
	 * @param request
	 * @return void
	 */
	public void contentResult(HttpServletRequest request) {
		String tip = null;
		if (loginUser == null) {
			tip = "您必须登录才可以执行此操作!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
			return;
		}
		int contentId = StringUtil.toInt(request.getParameter("contentId"));
		if (contentId <= 0) {
			doTip(null, null);
			return;
		}
		ForumContentBean content = ForumCacheUtil.getForumContent(contentId);
		if (content == null) {
			doTip(null, null);
			return;
		}
		if (content.getUserId() != loginUser.getId()) {
			doTip(null, null);
			return;
		}

		if (!isCooldown("fourmSTime", 10 * 1000)) {
			tip = "你的发文太快了！请先休息一会再继续！";
			request.setAttribute("tip", tip);
			request.setAttribute("content", content);
			request.setAttribute("result", "success");
			return;
		}
		session.setAttribute("fourmSTime", System.currentTimeMillis() + "");
		String contents = request.getParameter("content");
		if(contents != null)
			contents = StringUtil.removeCtrlAsc(contents.trim());
		if (contents == null || contents.length() == 0) {
			tip = "续写内容不能为空!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "success");
			request.setAttribute("content", content);
			return;
		}
		if(content.getContent().length() > 0)
			contents = content.getContent() + "\n\r" + contents;
		if(contents.length() > 60000) {
			contents = contents.substring(0, 60000);
		}
		contents = contents.replace(session.getId(), "abcd01");
		ForumCacheUtil.updateForumContentNoFlush("content='" + StringUtil.toSql(contents) + "'",
				"id=" + content.getId());
		content.setContent(contents);
		request.setAttribute("content", content);
		tip = "续写成功!";
		request.setAttribute("tip", tip);
		request.setAttribute("result", "success");

	}
	
	public static int getFirstContentId(int forum) {
		List contentList = ForumCacheUtil.getContentsCache(forum, 0, 1);
		if(contentList == null || contentList.size() == 0)
			return 0;
		return ((Integer)contentList.get(0)).intValue();
	}

	public static long oldTime = 86400000l * 300;
	
	public void viewContent(HttpServletRequest request,
			HttpServletResponse response) {
		int contentId = getParameterInt("contentId");
		ForumContentBean con = ForumCacheUtil.getForumContent(contentId);
		if (con == null) {
			return;
		}
		if(hasParam("next")) {
			int newId = SqlUtil.getIntResult("select id from jc_forum_content where forum_id=" + con.getForumId() + " and last_re_time<'" + DateUtil.formatSqlDatetime(con.getLastReTime()) + "' and mark2=0 and del_mark=0 order by last_re_time desc limit 1", 2);
			if(newId != -1) {
				contentId = newId;
				con = ForumCacheUtil.getForumContent(contentId);
			}
		} else if(hasParam("prev")) {
			int newId = SqlUtil.getIntResult("select id from jc_forum_content where forum_id=" + con.getForumId() + " and last_re_time>'" + DateUtil.formatSqlDatetime(con.getLastReTime()) + "' and mark2=0 and del_mark=0 order by last_re_time limit 1", 2);
			if(newId != -1) {
				contentId = newId;
				con = ForumCacheUtil.getForumContent(contentId);
			}
		}
		ForumBean forum = ForumCacheUtil.getForumCache(con.getForumId());
		String popedom = null;
		String isDel = null;// 是否有删除回复的权限

		String users[] = forum.getUserId().split("_");
		boolean manager = false;
		if (loginUser != null) {
			for (int i = 0; i < users.length; i++) {
				if (loginUser.getId() == StringUtil.toInt(users[i])) {
					manager = true;
					break;
				}
			}

			if (isSuperAdmin(loginUser.getId())) {
				popedom = "super";
				isDel = "1";
			} else if (manager) {
				popedom = "manager";
				isDel = "1";

			} else if (loginUser.getId() == con.getUserId()) {
				popedom = "mycontent";
				isDel = "1";
			} else {
				popedom = "normal";
			}
		} else
			popedom = "guest";
		String tip = null;
		String title = getParameterNoEnter("title");
		if (title != null) {
			String contents = getParameterNoCtrl("content");
			if (contents != null && ( contents.length() == 0 || contents.length() > 50000)) {
				contents = contents.trim();
				tip = "主题内容过长！";
				request.setAttribute("tip", tip);
				request.setAttribute("con", con);
				request.setAttribute("result", "error");
				return;
			} else if (title.length() == 0 || title.length() > 30) {
				tip = "主题标题长度必须是1到30个字！";
				request.setAttribute("tip", tip);
				request.setAttribute("con", con);
				request.setAttribute("result", "error");
				return;
			} else {
				boolean record = true;	// 需要记录修改人
				if(con.isNews()) {	// 新闻只有新闻管理员才能修改
					if(loginUser != null && !ForbidUtil.isForbid("newsa2", loginUser.getId())) {
						tip = "无法修改主题！";
						request.setAttribute("tip", tip);
						request.setAttribute("con", con);
						request.setAttribute("result", "error");
						return;
					}
					record = false;
				} else {
					if(popedom.equals("normal") || popedom.equals("guest") || popedom.equals("mycontent") && DateUtil.timepast(con.getCreateTime(),300)) {
						tip = "无法修改主题！";
						request.setAttribute("tip", tip);
						request.setAttribute("con", con);
						request.setAttribute("result", "error");
						return;
					}
					if(popedom.equals("super"))
						record = false;
				}
				String sql = "title='" + StringUtil.toSql(title) + "'";
				if(contents != null) {
					contents = contents.replace(session.getId(), "abcd01");
					if(record)
						contents += "\r\n[" + loginUser.getNickName() + "]于" + DateUtil.formatDate2(new Date()) + "修改过本帖";
					sql += ",content='" + StringUtil.toSql(contents) + "'";
				}
				if (ForumCacheUtil.updateForumContentNoFlush(sql, "id=" + contentId)) {
					con.setTitle(title);
					if(contents != null)
						con.setContent(contents);
					tip = "您已经成功修改主题！";
					request.setAttribute("tip", tip);
					request.setAttribute("con", con);
					request.setAttribute("result", "success");
					return;

				} else {
					tip = "修改主题失败！";
					request.setAttribute("tip", tip);
					request.setAttribute("con", con);
					request.setAttribute("result", "error");
					return;
				}
			}
		}

		String contents = request.getParameter("content");

		// 回复
		if (contents != null) {
			
			String n = request.getParameter("n");
			
			request.setAttribute("con", con);
			contents = StringUtil.removeCtrlAsc(contents.trim());
			if (con.getCreateTime().getTime() - System.currentTimeMillis() < -oldTime) {
				tip = "这个主题太古老了，不能回复！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			if (forum.getType() == 1 && loginUser == null) {
				String url = response
						.encodeURL("/user/login.jsp?backTo=/jcforum/index.jsp");
				String link = "<a href=\"" + url + "\">登录</a>";
				tip = "此版面不允许游客发言，请" + link + "后在发言!";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			if (contents.length() == 0) {
				tip = "回复不能为空！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			if (contents.length() > 1000) {
				contents = contents.substring(0, 1000);
			}
			if (con.isReadonly()) {
				tip = "这篇帖子无法回复！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			String infos = (String) session
					.getAttribute("forumreplyrepeat");
			String info = contents;
			if (loginUser != null) {
				info = contents + " " + loginUser.getId();
			}
			if ((info.equals(infos))) {
				doTip("error", "回复重复.");
				return;
			}
			if (!isCooldown("fourmSTime", 10 * 1000)) {
				tip = "你的发文太快了！请先休息一会再继续！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("forum",loginUser.getId());
			if(forbid != null) {
				doTip("error", "已经被禁止发贴 - " + forbid.getBak());
				return;
			}
			forbid = ForbidUtil.getForbid("f" + con.getForumId(), loginUser.getId());
			if(forbid != null) {
				doTip("tip", "已经被禁止发贴 - " + forbid.getBak());
				return;
			}
			
			ForumReplyBean replyBean = new ForumReplyBean();
			String cType = request.getParameter("cType");
			if (cType != null) {
				replyBean.setCType(1);
			}
			
			//增加论坛动作卡
			int action = this.getParameterInt("action");
			if(action > 0) {
				replyBean.setAction();
			}
			contents = contents.replace(session.getId(), "abcd01");
			replyBean.setContent(contents);
			if (loginUser != null) {
				replyBean.setUserId(loginUser.getId());
			} else {
				replyBean.setUserId(0);
			}
			replyBean.setContentId(contentId);
			
			//增加匿名回复 start
			if(n != null && n.equals("n")) {
				int userBagId = UserBagCacheUtil.getUserBagById(
						ForumReplyBean.REPLY_ITEM, loginUser.getId());
				if(userBagId <= 0) {
					tip = "没有匿名回复卡,不能匿名回复";
					request.setAttribute("tip", tip);
					request.setAttribute("result", "error");
					return;
				}
				// 更新物品使用次数
				UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),userBagId);
				replyBean.setNickReply();
				//contentBean.setNickTopic();
			}
			//增加匿名回复 end
			
			
			ForumCacheUtil.addForumReply(replyBean, con);
			if (loginUser != null) {
				RankAction.addPoint(loginUser, 1);
				if(replyBean.getCType() == 0)	// 回复表情之类，不加经验值也不算回复数
					forumUser.addReplyCount();
				ForumCacheUtil.updateForumUser(forumUser);
			}
			session.setAttribute("fourmSTime", System
					.currentTimeMillis()
					+ "");
			session.setAttribute("forumreplyrepeat", info);

			doTip("success", "回帖发表成功!");
			return;
		}
		

		// 删除回复
		int del = getParameterInt("del");
		if (del > 0) {
			ForumReplyBean reply = ForumCacheUtil.getForumReply(del);
			if (reply == null) {
				doTip("error", "该回复不存在!");
				request.setAttribute("con", con);
				return;
			}
			if(reply.getCreateTime().getTime() - System.currentTimeMillis() < -oldTime) {
				doTip("error", "回复太古老无法删除!");
				request.setAttribute("con", con);
				return;			
			}
			if(loginUser.getUserSetting() == null || !loginUser.getUserSetting().isFlagReplyDelete() && !hasParam("c")) {
				tip("confirm");
				request.setAttribute("reply", reply);
				request.setAttribute("con", con);
				return;
			}
			if(popedom.equals("super")) {
				ForumCacheUtil.deleteForumReply(reply);
				ForumUserBean fu = ForumCacheUtil.getForumUser(reply.getUserId());
				if(fu != null) {
					fu.decExp(2);
					fu.setReplyCount(fu.getReplyCount() - 1);
					ForumCacheUtil.updateForumUser(fu);
				}
			} else if(reply.getUserId() == loginUser.getId() || popedom.equals("manager"))
				ForumCacheUtil.deleteMyForumReply("id=" + del, contentId, del);
		}

		
		if(!popedom.equals("super")) {
			con.setCount(con.getCount() + 1);
			if(con.getCount() <= 100 && con.getCount() % 10 == 0
					|| con.getCount() <= 300 && con.getCount() % 20 == 0
					|| con.getCount() % 50 == 0)		// 点击n次才保存一次，因为不是很重要的数据
				SqlThread.addSql("update jc_forum_content set count=" + con.getCount() + " where id=" + contentId);
		}
		boolean reverse = (loginUser != null && loginUser.getUserSetting().isFlagVorder()) ^ hasParam("v");

		PagingBean page = new PagingBean(this, con.getReply(), NUMBER_PER_PAGE, "p", "go");
		request.setAttribute("page", page);
		
		List replyList = ForumCacheUtil.getReplys(con, page.getStartIndex(), page.getEndIndex() - page.getStartIndex(), reverse);

		List contentL = new ArrayList();

		if (replyList != null) {
			for (int i = 0; i < replyList.size(); i++) {

				int content = ((Integer) replyList.get(i)).intValue();

				ForumReplyBean cons = ForumCacheUtil.getForumReply(content);
				if (cons != null)
					contentL.add(cons);

			}
		}

		request.setAttribute("con", con);
		request.setAttribute("replyList", contentL);
		request.setAttribute("popedom", popedom);
		request.setAttribute("isDel", isDel);
		// request.setAttribute("tip", tip);
		if (request.getParameter("sUserId") != null) {	// 根据用户搜索本版面帖子的结果，查找上一条下一条
			int userId = StringUtil.toInt(request.getParameter("sUserId"));
			if (userId > 0 && UserInfoUtil.getUser(userId) != null) {
				String where = "where forum_id="
						+ con.getForumId() + " and user_id=" + userId
						+ " and del_mark=0";
				int[] near = SqlUtil.getNearId("jc_forum_content", where, con.getId(), 2);
				int prev = near[0];
				int next = near[1];
				
				if (prev > 0) {
					ForumContentBean prevForumContent = ForumCacheUtil
							.getForumContent(prev);
					if (prevForumContent != null) {
						request.setAttribute("prevForumContent",
								prevForumContent);
						request.setAttribute("sUserId", String.valueOf(userId));
					}
				}
				if (next > 0) {
					ForumContentBean nextForumContent = ForumCacheUtil
							.getForumContent(next);
					if (nextForumContent != null) {
						request.setAttribute("nextForumContent",
								nextForumContent);
						request.setAttribute("sUserId", String.valueOf(userId));
					}
				}
			}
			return;
		}

		if (request.getParameter("myTopic") != null) {// 根据用户搜索全部帖子的结果，查找上一条下一条
			String where = "where forum_id=" + con.getForumId()
					+ " and user_id=" + loginUser.getId()
					+ " and del_mark=0";
			int[] near = SqlUtil.getNearId("jc_forum_content", where, con.getId(), 2);
			int prev = near[0];
			int next = near[1];
			if (prev > 0) {
				ForumContentBean prevForumContent = ForumCacheUtil
						.getForumContent(prev);
				if (prevForumContent != null) {
					request.setAttribute("prevForumContent", prevForumContent);
					request.setAttribute("myTopic", "true");
				}
			}
			if (next > 0) {
				ForumContentBean nextForumContent = ForumCacheUtil
						.getForumContent(next);
				if (nextForumContent != null) {
					request.setAttribute("nextForumContent", nextForumContent);
					request.setAttribute("myTopic", "true");
				}
			}
			return;
		}
/*
		if (con.getMark2() == 0 && con.getMark3() == 0) {
			int prev = 0;
			int next = 0;
			String sql = " jc_forum_content where forum_id="
					+ con.getForumId()
					+ " and mark2=0 "
					+ "and mark3=0 and del_mark=0 order by last_re_time desc , id desc";
			DbOperation dbOp = new DbOperation();
			dbOp.init(Constants.DBShortName);
			try {
				SqlUtil.createRownum(dbOp, sql);
				int rownum = SqlUtil.getRownumById(dbOp, con.getId());
				prev = SqlUtil.getIdByRownum(dbOp, rownum - 1);
				next = SqlUtil.getIdByRownum(dbOp, rownum + 1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbOp.release();
			}

			// // 取得上一条和下一条
			// ForumContentBean prevForumContent = getForumService()
			// .getForumContent(
			// " id < "
			// + con.getId()
			// + " and forum_id="
			// + con.getForumId()
			// + " and mark1=0 and mark2=0 and mark3=0 and del_mark="
			// + 0 + " ORDER BY id desc");
			// ForumContentBean nextForumContent = getForumService()
			// .getForumContent(
			// " id > "
			// + con.getId()
			// + " and forum_id="
			// + con.getForumId()
			// + " and mark1=0 and mark2=0 and mark3=0 and del_mark="
			// + 0 + " ORDER BY id asc");
			// request.setAttribute("prevForumContent", prevForumContent);
			// request.setAttribute("nextForumContent", nextForumContent);
			if (prev > 0) {
				ForumContentBean prevForumContent = ForumCacheUtil
						.getForumContent(prev);
				if (prevForumContent != null) {
					request.setAttribute("prevForumContent", prevForumContent);
				}
			}
			if (next > 0) {
				ForumContentBean nextForumContent = ForumCacheUtil
						.getForumContent(next);
				if (nextForumContent != null) {
					request.setAttribute("nextForumContent", nextForumContent);
				}
			}

			return;
		}*/
		if (con.getMark1() == 1) {
			int prev = 0;
			int next = 0;
			String sql = "select id from jc_forum_prime where forum_id="
					+ con.getForumId();
			DbOperation dbOp = new DbOperation(2);
			try {
				next = dbOp.getIntResult(sql + " and id < " + con.getId() + " order by id desc limit 1");
				prev = dbOp.getIntResult(sql + " and id > " + con.getId() + " order by id limit 1");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbOp.release();
			}
			if (prev > 0) {
				ForumContentBean prevJHForumContent = ForumCacheUtil
						.getForumContent(prev);
				if (prevJHForumContent != null) {
					request.setAttribute("prevJHForumContent",
							prevJHForumContent);
				}
			}
			if (next > 0) {
				ForumContentBean nextJHForumContent = ForumCacheUtil
						.getForumContent(next);
				if (nextJHForumContent != null) {
					request.setAttribute("nextJHForumContent",
							nextJHForumContent);
				}
			}
			return;
		}
	}
	public void viewContentHis(HttpServletRequest request,
			HttpServletResponse response) {
		int contentId = getParameterInt("contentId");
		ForumContentBean con = ForumCacheUtil.getForumContent(contentId);
		if (con == null) {
			return;
		}
		ForumBean forum = ForumCacheUtil.getForumCache(con.getForumId());
		String tip = null;
		

		String popedom = null;
		String isDel = null;// 是否有删除回复的权限

		String users[] = forum.getUserId().split("_");
		boolean manager = false;
		if (loginUser != null) {
			for (int i = 0; i < users.length; i++) {
				if (loginUser.getId() == StringUtil.toInt(users[i])) {
					manager = true;
					break;
				}

			}

			if (isSuperAdmin(loginUser.getId())) {
				popedom = "super";
				isDel = "1";
			} else if (manager) {
				popedom = "manager";
				isDel = "1";

			} else if (loginUser.getId() == con.getUserId()) {
				popedom = "mycontent";
				isDel = "1";
			} else {
				popedom = "normal";
			}
		} else
			popedom = "guest";

		// 删除回复
		int del = getParameterInt("del");
		if (del > 0) {
			if (del <= 0) {
				request.setAttribute("con", con);
				doTip("error", "参数错误!");
				return;
			}
			ForumReplyBean reply = ForumCacheUtil.getForumReply(del);
			if (reply == null) {
				doTip("error", "该回复不存在!");
				request.setAttribute("con", con);
				return;
			}
			if(popedom.equals("super") || popedom.equals("manager"))
				ForumCacheUtil.deleteForumReply(reply);
			else
				ForumCacheUtil.deleteMyForumReply("id=" + del, contentId, del);
		}

		
		if(!popedom.equals("super")) {
			ForumCacheUtil.updateForumContentNoFlush("count=count+1", "id=" + contentId);
			con.setCount(con.getCount() + 1);
		}

		String order = "id";
		if(loginUser != null && loginUser.getUserSetting().isFlagVorder())
			order = "id desc";

		PagingBean page = new PagingBean(this, con.getReply(), NUMBER_PER_PAGE, "p", "go");
		request.setAttribute("page", page);
		
		int start = page.getStartIndex();
		int end = page.getEndIndex();
		
		List replyList = forumService.getForumReplyHisList("content_id=" + contentId +
				" and del_mark=0 order by " + order + " limit " + start + "," + NUMBER_PER_PAGE);

		request.setAttribute("con", con);
		request.setAttribute("replyList", replyList);
		request.setAttribute("popedom", popedom);
		request.setAttribute("isDel", isDel);
	}
	/**
	 * 
	 * @author macq
	 * @explain：帖子内容分页
	 * @datetime:2007-7-30 13:50:12
	 * @param request
	 * @return void
	 */
	public String contentTransform(ForumContentBean bean,
			HttpServletRequest request, HttpServletResponse response) {
		return contentTransform(bean, request, response, true);
	}
	public static int NUM_PAGE = 1000;
	public String contentTransform(ForumContentBean bean,
			HttpServletRequest request, HttpServletResponse response, boolean showSend) {
		if (bean == null)
			return "";

		String content = bean.getContent();	// 帖子内容
		StringBuilder sb = new StringBuilder(100);
		if(bean.getType() == 0) {	// 普通帖子要分页显示
			
			// 帖子字数
			
			int length = bean.getContent().length();
			if (length > NUM_PAGE) {
				// 获取当前页码
				PagingBean paging = new PagingBean(this, length, NUM_PAGE, "p2");

				content = content.substring(paging.getStartIndex(), paging.getEndIndex());
	
				sb.append(StringUtil.toWml(content));
				sb.append("<br/>");
				String url = null;
				if(paging.getTotalPageCount() < 3) {
					if (paging.getCurrentPageIndex() < paging.getTotalPageCount() - 1) {
						url = ("viewContent.jsp?contentId=" + bean.getId()
								+ "&amp;forumId" + bean.getForumId() + "&amp;p2="
								+ (paging.getCurrentPageIndex() + 1));
						sb.append("<a href=\"" + url + "\">下一页</a>");
						sb.append('|');
					}
					if (paging.getCurrentPageIndex() > 0) {
						url = ("viewContent.jsp?contentId=" + bean.getId()
								+ "&amp;forumId" + bean.getForumId() + "&amp;p2="
								+ (paging.getCurrentPageIndex() - 1));
						sb.append("<a href=\"" + url + "\">上一页</a>");
					}
				} else {
					sb.append(paging.shuzifenye("viewContent.jsp?contentId=" + bean.getId()
							+ "&amp;forumId" + bean.getForumId(), true, "|", null));
				}
				sb.append("<br/>");
			} else {
				sb.append(StringUtil.toWml(content));
				sb.append("<br/>");
			}
			if (bean.getAttach() != null & !("").equals(bean.getAttach())) {
				if(showSend){
						sb.append("<img src=\"" + Constants.IMG_ROOT_URL
								+ bean.getAttach() + "\" alt=\"x\" />");
				}else{
					sb.append("(图片)");
				}
				sb.append("<a href=\"");
					sb.append(Constants.IMG_ROOT_URL);
					sb.append(bean.getAttach());
				sb.append("\">下载</a>");
				sb.append("<br/>");
			}
			
		} else if(bean.getType() == 1) {		// 单选投票
			String[] cs = content.split("\\|\\|");	// 分别为：内容、投票选项、结果
			ForumVoteBean v = ForumCacheUtil.getVoteData(bean.getId());
			int[] count = v.getVoteCount();
			if(cs.length > 1) {
				if(cs[0].length() > 0) {
					sb.append(StringUtil.toWml(cs[0]));
					sb.append("<br/>");
				}

				if(cs.length > 2) {
					sb.append(StringUtil.toWml(cs[2]));
					sb.append("<br/>");
				}
				String[] vs = cs[1].split("\\|", count.length);
				for(int i = 0;i < vs.length;i++) {
					sb.append((char)('A' + i));
					sb.append('.');
					sb.append(StringUtil.toWml(vs[i]));
					sb.append('(');
					sb.append(count[i]);
					sb.append("人)<a href=\"");
					sb.append(("vote.jsp?v=" + i + "&amp;cid=" + bean.getId()));
					sb.append("\">");
					sb.append("投</a><br/>");
				}
			}
		} else {		// 打分投票
			
		}
		return sb.toString();
	}
	
	public void vote() {
		int contentId = getParameterInt("cid");
		int vote = getParameterInt("v");
		if(vote < 0 || vote > 20)
			vote = 0;
		ForumContentBean con = ForumCacheUtil.getForumContent(contentId);
		if(con.getType() != 1)
			tip("success", "该主题无法投票！");
		else {
			ForumCacheUtil.vote(loginUser, con, vote);
			tip("success", "感谢您的投票！");
		}
	}

	public void result() {
		String tip = null;

		int contentId = getParameterIntS("contentId");
		
		ForumContentBean con = ForumCacheUtil.getForumContent(contentId);
		if(con == null) {
			request.setAttribute("tip", "不存在的主题");
			return;
		}
		request.setAttribute("con", con);
		if (session.getAttribute("viewContent") != null)
			session.removeAttribute("viewContent");
		else {
			return;
		}
		
		ForumBean forum = ForumCacheUtil.getForumCache(con.getForumId());
		request.setAttribute("forum", forum);

		int popedom = 0;

		String users[] = forum.getUserId().split("_");
		boolean manager = false;
		if (loginUser != null) {
			for (int i = 0; i < users.length; i++) {
				if (loginUser.getId() == StringUtil.toInt(users[i])) {
					manager = true;
					break;
				}

			}

			if (isSuperAdmin(loginUser.getId())) {
				popedom = 4;
			} else if (manager) {
				popedom = 3;

			} else if (loginUser.getId() == con.getUserId()) {
				popedom = 2;
			} else {
				popedom = 1;
			}
		}
		

		if (hasParam("prime") && popedom > 2) {		// 加精
			if (con.getMark1() == 1) {
				tip = "本主题已经加入精华区,无需再加入！";
			} else if (ForumCacheUtil.addPrime(con, forum)) {
				tip = "您已经成功将本主题加入精华区！";
			} else {
				tip = "本主题加入精华区失败！";
			}
		}

		if (hasParam("peak") && popedom > 2) {			// 置顶
			if (con.isPeak()) {
				tip = "本主题已经置顶,无需再置顶！";
			} else if (ForumCacheUtil.addPeak(con)) {
				tip = "您已经成功将本主题置顶！";
			} else {
				tip = "每版除了与总置顶相同的置顶主题外，最多10主题置顶，本主题置顶失败！";
			}
		}

		if (hasParam("toppeak") && popedom > 3) {			// 总置顶，已经置顶的帖子，必须先撤销置顶，才能总置顶
			if (con.isPeak()) {
				tip = "本主题已经置顶！";
			} else if (ForumCacheUtil.addTopPeak(con)) {
				tip = "您已经成功将本主题总置顶！";
				ForumCacheUtil.flushForumPeakMap(0);
			} else {
				tip = "总置顶失败！";
			}
		}

		if (hasParam("delprime") && popedom > 2) {		// 去经精
			if (con.getMark1() == 0) {
				tip = "本主题不在精华区,无法去精！";
			} else if (ForumCacheUtil.cancelPrime(con, forum)) {
				tip = "您已经成功将本主题移出精华区！";

			} else {
				tip = "本主题移出精华区失败！";
			}
		}

		if (hasParam("delpeak") && popedom > 2) {			// 取消置顶
			if (!con.isPeak()) {
				tip = "本主题没有置顶,无法取消置顶！";
			} else if (ForumCacheUtil.cancelPeak(con)) {
				tip = "您已经成功将本主题取消置顶！";
			} else {
				tip = "本主题取消置顶失败！";
			}
		}

		if (hasParam("deltoppeak") && popedom > 3) {			// 取消总置顶
			if (!con.isTopPeak()) {
				tip = "本主题没有总置顶,无法取消总置顶！";
			} else if (ForumCacheUtil.cancelTopPeak(con)) {
				tip = "您已经成功将本主题取消总置顶！";
			} else {
				tip = "本主题取消总置顶失败！";
			}
		}
		
		if (hasParam("delro") && (popedom > 2 || ForbidUtil.isForbid("foruma",loginUser.getId()))) {			// 取消只读
			if (!con.isReadonly()) {
				tip = "本主题没有只读,无法取消只读！";
			} else if (ForumCacheUtil.cancelReadonly(con)) {
				tip = "您已经成功将本主题取消只读！";
			} else {
				tip = "本主题取消只读失败！";
			}
		}
		if (hasParam("ro") && (popedom > 2 || ForbidUtil.isForbid("foruma",loginUser.getId()))) {			// 只读
			if (con.isReadonly()) {
				tip = "本主题已经是只读！";
			} else if (ForumCacheUtil.setReadonly(con)) {
				tip = "您已经成功将本主题设置为只读！";
			} else {
				tip = "本主题设置只读失败！";
			}
		}
		
		
		if (hasParam("ev") && popedom > 1) {			// 结束投票
			if (con.getType() == 0) {
				tip = "非投票帖或者投票已经结束！";
			} else {
				List list = SqlUtil.getIntsList("select vote,count(id) from jc_forum_vote where content_id=" + con.getId() +
						" group by vote", 2);
				HashMap map = new HashMap();
				Iterator iter = list.iterator();
				while(iter.hasNext()) {
					int[] is = (int[])iter.next();
					map.put(Integer.valueOf(is[0]), Integer.valueOf(is[1]));
				}
				
				StringBuilder sb = new StringBuilder(64);
				
				String[] cs = con.getContent().split("\\|\\|");	// 分别为：内容、投票选项、结果
				if(cs.length > 1) {
					if(cs[0].length() > 0) {
						sb.append(cs[0]);
						sb.append("\n");
					}
					sb.append("投票已结束，结果如下：\n");

					String[] vs = cs[1].split("\\|");
					for(int i = 0;i < vs.length;i++) {
						sb.append((char)('A' + i));
						sb.append('.');
						sb.append(vs[i]);
						sb.append('(');
						Integer c = (Integer)map.get(Integer.valueOf(i));
						if(c == null)
							sb.append(0);
						else
							sb.append(c);
						sb.append("票)\n");
					}
					sb.append("一共有");
					sb.append(ForumCacheUtil.getVoteCount(con.getId()));
					sb.append("人参与了投票");
				}
				
				con.setContent(sb.toString());
				con.setType(0);
				
				forumService.updateForumContent("type=0,content='" + StringUtil.toSql(con.getContent()) + "'", "id=" + con.getId());
				
				tip = "投票中止成功！";

			}
		}
		
		if (hasParam("cart")) {			// 加收藏
			if (loginUser != null) {
				UserStatusBean status = (UserStatusBean) UserInfoUtil
						.getUserStatus(loginUser.getId());
				if (status.getRank() < 3) {
					tip = "对不起,您的等级不够！";

				} else {
					String title = "帖子:" + con.getTitle();
					String url = "/jcforum/viewContent.jsp?contentId="
							+ con.getId();

					if (loginUser.getId() == -1) {
						tip = "本主题总收藏失败！";
					} else {
						CartBean cartbean = new CartBean();
						cartbean.setUserId(loginUser.getId());
						cartbean.setTitle(title);
						cartbean.setUrl(url);

						if (!getUserService().addCart(cartbean)) {
							tip = "本主题总收藏失败！";
						} else {
							tip = "您已经成功收藏本主题！";

						}
					}
					request.setAttribute("cart", "");

				}
			}
		}
		request.setAttribute("tip", tip);
	}

	/**
	 * @return Returns the forumUser.
	 */
	public ForumUserBean getForumUser() {
		return forumUser;
	}
	
	/**
	 * 处理内容中特殊的部分，例如替换表情等
	 * @param content
	 * @return
	 */
	private static Pattern facePattern = Pattern.compile("\\[/([0-9]{1,3})\\]");
	private static Pattern urlPattern = Pattern.compile("\\[@([^|\\]]*)\\|([^\\]]*)\\]");
	public static String speContent(HttpServletResponse response, String content) {
		return speContent(response, content, true);
	}
	public static String speContent(HttpServletResponse response, String content, boolean showFace) {
		int limitFace = 3;
		Matcher m = facePattern.matcher(content);
		boolean res = m.find();
		if(res) {
		
	        StringBuilder sb = new StringBuilder(64);
	        int pos = 0;
	        while (res && limitFace > 0) {
	            sb.append(content.substring(pos, m.start(0)));
	            if(showFace) {
		            sb.append("<img src=\"/img/jcforum/action/");
		            sb.append(m.group(1));
		            sb.append(".gif\" alt=\"x\"/>");
	            } else {
	            	sb.append('(');
	            	sb.append(getFaceString(m.group(1)));
	            	sb.append(')');
	            }
	            pos = m.end(0);
	            limitFace--;
	            res = m.find();
	        }
	        
	        sb.append(content.substring(pos));
	        content = sb.toString();
		}
		
		m = urlPattern.matcher(content);
		res = m.find();
		if(res) {
		
	        StringBuilder sb = new StringBuilder(64);
	        int pos = 0;
	        while (res) {
	        	sb.append(content.substring(pos, m.start(0)));
	        	String url = m.group(2);
	        	if(url.indexOf('$') == -1 && url.indexOf(';') == -1 && url.indexOf(':') == -1) {
		            sb.append("<a href=\"");
		            sb.append(url);
		            sb.append("\">");
		            sb.append(m.group(1));
		            sb.append("</a>");
	        	}
	            pos = m.end(0);
	            res = m.find();
	        }
	        
	        sb.append(content.substring(pos));
	        content = sb.toString();
		}
		
		return content;
	}
	
	// 创建分类精华，
	public static boolean addPrimeCat(ForumBean forum, PrimeCatBean cat) {
		cat.setForumId(forum.getId());
		if (!forumService.addPrimeCat(cat))
			return false;
		if (cat.getParentId() != 0)
			SqlUtil.executeUpdate("update jc_forum_prime_cat set cat_count=cat_count+1 where id=" + cat.getParentId(), 2);
		return true;
	}
	// 删除分类精华（分类保留，只是论坛不再关联）
	public static boolean removePrimeCat(ForumBean forum) {
		if(forum.getPrimeCat() == 0)
			return false;
		
		forum.setPrimeCat(0);
		SqlUtil.executeUpdate("update jc_forum set prime_cat=0 where id=" + forum.getId(), 2);
		return true;
	}
	// 移动选中的内容到某个分类
	public static boolean moveSelect(List select, PrimeCatBean cat, ForumBean forum) {
		StringBuilder sb1 = new StringBuilder(select.size() * 10);
		StringBuilder sb2 = new StringBuilder(select.size() * 10);
		for(int i = 0;i < select.size();i++) {
			Integer iid = (Integer)select.get(i);
			if(iid.intValue() > 0) {
				if(sb1.length() > 0)
					sb1.append(',');
				sb1.append(iid);
			} else {
				if(sb2.length() > 0)
					sb2.append(',');
				sb2.append(-iid.intValue());	// 目录保存的是负数
			}
		}
		if(sb1.length() > 0) {	// 有需要移动的帖子
			DbOperation db = new DbOperation(2);
			db.executeUpdate("create temporary table tmp_cat engine=memory select cat_id from jc_forum_prime where id in(" + sb1.toString() + ") and forum_id=" + forum.getId());
			db.executeUpdate("update jc_forum_prime set cat_id=" + cat.getId() + " where id in(" + sb1.toString() + ") and forum_id=" + forum.getId());
			db.executeUpdate("update (select cat_id,count(cat_id) cnt from tmp_cat group by cat_id) a,jc_forum_prime_cat b set b.thread_count=b.thread_count-a.cnt where a.cat_id=b.id");	// 所在的目录帖子数都-1
			db.executeUpdate("update jc_forum_prime_cat set thread_count=thread_count+(select count(*) from tmp_cat) where id=" + cat.getId());
			db.executeUpdate("drop table tmp_cat");
			db.release();
		}
		
		if(sb2.length() > 0) {	// 有需要移动的分类
		// 找出cat和之上的所有分类，这些分类不允许被移动
			StringBuilder sb3 = new StringBuilder(32);
			sb3.append("0,");// 不能移动根目录
			sb3.append(cat.getId());	// 首先不能移动到自己分类里，也不能移动到自己的子分类
			PrimeCatBean tmp = cat;
			while(tmp.getParentId() != 0) {
				sb3.append(',');
				sb3.append(tmp.getParentId());
				tmp = forumService.getPrimeCat("id=" + tmp.getParentId());
			}
			DbOperation db = new DbOperation(2);
			db.executeUpdate("create temporary table tmp_cat engine=memory select parent_id from jc_forum_prime_cat where id in(" + sb2.toString() + ") and id not in(" + sb3.toString() + ")");
			db.executeUpdate("update jc_forum_prime_cat set parent_id=" + cat.getId() + " where id in(" + sb2.toString() + ") and id not in(" + sb3.toString() + ")");
			db.executeUpdate("update (select parent_id,count(parent_id) cnt from tmp_cat group by parent_id) a,jc_forum_prime_cat b set b.cat_count=b.cat_count-a.cnt where a.parent_id=b.id");	// 所在的目录分类数都-1
			db.executeUpdate("update jc_forum_prime_cat set cat_count=cat_count+(select count(*) from tmp_cat) where id=" + cat.getId());
			db.executeUpdate("drop table tmp_cat");
			db.release();
		}
		return true;
	}

	public static boolean isSuperAdmin(int userId) {
		return ForbidUtil.isForbid("foruma2", userId);
	}
	// 是管理员或者本版面版主
	public static boolean isAdmin(ForumBean forum, int userId) {
		return forum.getUserIdSet().contains(new Integer(userId)) || isSuperAdmin(userId);
	}
	// 查看精华帖
	public void viewPrime() {
		int contentId = getParameterInt("id");
		ForumContentBean con = ForumCacheUtil.getForumContent(contentId);
		if (con == null) {
			return;
		}
		int catId = SqlUtil.getIntResult("select cat_id from jc_forum_prime where id=" + contentId, 2);
		if(catId == -1)
			return;
		setAttribute("cat", catId);

		ForumBean forum = ForumCacheUtil.getForumCache(con.getForumId());
		String popedom = null;
		String isDel = null;// 是否有删除回复的权限

		boolean manager = false;
		if (loginUser != null) {
			if (isSuperAdmin(loginUser.getId())) {
				popedom = "super";
			} else if (forum.getUserIdSet().contains(new Integer(loginUser.getId()))) {
				popedom = "manager";
			} else {
				popedom = "normal";
			}
		} else
			popedom = "guest";
		

		PagingBean page = new PagingBean(this, con.getReply(), NUMBER_PER_PAGE, "p", "go");
		request.setAttribute("page", page);
		
		List replyList = ForumCacheUtil.getReplys(con, page.getStartIndex(), page.getEndIndex() - page.getStartIndex(), false);

		List contentL = new ArrayList();

		if (replyList != null) {
			for (int i = 0; i < replyList.size(); i++) {

				int content = ((Integer) replyList.get(i)).intValue();

				ForumReplyBean cons = ForumCacheUtil.getForumReply(content);
				if (cons != null)
					contentL.add(cons);

			}
		}

		request.setAttribute("con", con);
		request.setAttribute("replyList", contentL);
		request.setAttribute("popedom", popedom);

		int prev = 0;
		int next = 0;
		String sql = "select id from jc_forum_prime where cat_id="
				+ catId;
		DbOperation dbOp = new DbOperation(2);
		try {
			next = dbOp.getIntResult(sql + " and id < " + con.getId() + " order by id desc limit 1");
			prev = dbOp.getIntResult(sql + " and id > " + con.getId() + " order by id limit 1");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		if (prev > 0) {
			ForumContentBean prevJHForumContent = ForumCacheUtil
					.getForumContent(prev);
			if (prevJHForumContent != null) {
				request.setAttribute("prevJHForumContent",
						prevJHForumContent);
			}
		}
		if (next > 0) {
			ForumContentBean nextJHForumContent = ForumCacheUtil
					.getForumContent(next);
			if (nextJHForumContent != null) {
				request.setAttribute("nextJHForumContent",
						nextJHForumContent);
			}
		}
		return;
	}
	/**
	 * @return Returns the loginUser.
	 */
	public UserBean getLoginUser() {
		return loginUser;
	}
	
	public static String[] faceString = {"无","愤怒","吃惊","呕吐","调皮","大哭","难过","鲜花","害怕","厉害","害羞","支持","握手","大笑","色狼","沉思","不屑","疑问","冷汗","疲倦","狗屎"
	};
	public static String getFaceString(String content) {
		int id = StringUtil.toInt(content);
		if(id>0&&id<faceString.length)
			return faceString[id];
		return "";
	}
}
