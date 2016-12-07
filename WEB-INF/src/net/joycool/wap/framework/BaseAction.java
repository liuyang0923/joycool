package net.joycool.wap.framework;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jc.family.FamilyAction;

import net.joycool.wap.bean.AdverBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.PositionBean;
import net.joycool.wap.bean.ShortcutBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.NoticeCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IAdverServic;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.spec.buyfriends.ServiceTrend;
import net.joycool.wap.spec.team.TeamAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LinkBuffer;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.NewNoticeCacheUtil;
import net.joycool.wap.util.PageUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

import org.apache.struts.action.Action;

/**
 * @author lbj
 * 
 */
public class BaseAction extends Action {
	public final static String URL_PREFIX = "";

	public static String INDEX_URL = "/Column.do?columnId="
			+ Constants.INDEX_ID;

	public static String NO_NOTICE = "noNotice";
	
	public static long shutdownTime = 0;		// 服务器重启的时间
	
	static INoticeService noticeService = ServiceFactory.createNoticeService();

	public void setSessionObject(HttpServletRequest request, String attrName,
			Object value) {
		HttpSession session = request.getSession();
		session.setAttribute(attrName, value);
	}

	public void setApplicationObject(HttpServletRequest request,
			String attrName, Object value) {
		ServletContext ctx = request.getSession().getServletContext();
		if (ctx.getAttribute(attrName) == null) {
			ctx.setAttribute(attrName, value);
		}
	}

	public Object getSessionObject(HttpServletRequest request, String attrName) {
		HttpSession session = request.getSession(false);
		if(session == null)
			return null;
		return session.getAttribute(attrName);
	}

	public UserBean getLoginUser(HttpServletRequest request) {
		return (UserBean) getSessionObject(request, Constants.LOGIN_USER_KEY);
	}

	/**
	 * liuyi 2007-01-30 重定向url处理
	 * 
	 * @param relativeUrl
	 * @param response
	 * @return
	 */
	public static void sendRedirect(String relativeUrl,
			HttpServletResponse response) {
		if (response == null) {
			return;
		}
		if (relativeUrl == null || relativeUrl.equals("")) {
			relativeUrl = "/wapIndex.jsp";
		}
		try {
			response.sendRedirect((relativeUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *  
	 * @author guip
	 * @explain：快捷通道修改
	 * @datetime:2007-8-24 13:38:23
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	public static String separator = "————————<br/>";
	public static String guestLink = "<a href=\"/register.jsp\">注册更好玩!</a>.<a href=\"/user/login.jsp\">老用户登陆</a><br/>";
	public static String userALink = "<a href=\"/guest/user/findpw.jsp\">升级送大礼!</a>.<a href=\"/user/login.jsp\">老用户登陆</a><br/>";
	public static String bottomShort = "<a href=\"/bottom.jsp\">ME</a>" +
				"|<a href=\"/lswjs/index.jsp\">导航</a>|" +
				"<a href=\"/wapIndex.jsp\">乐酷首页</a><br/>";
	public static String notLoginBottom = "<a href=\"/jcforum/index.jsp\">论坛</a>"+
				"|<a href=\"/chat/hall.jsp\">聊天</a>|"+
				"<a href=\"/lswjs/gameIndex.jsp\">娱乐城</a>|"+
				"<a href=\"/lswjs/wagerHall.jsp\">通吃岛</a><br/>"+
				"<a href=\"/bottom.jsp\">ME</a>|"+
				"<a href=\"/lswjs/index.jsp\">导航</a>|"+
				"<a href=\"/wapIndex.jsp\">乐酷首页</a><br/>";
	public static String getBottomShort(HttpServletRequest request,
			HttpServletResponse response) {
//		StringBuilder wml = new StringBuilder();
//		wml.append("wap.joycool.net<br/>");
//		wml.append("<a href=\"/bottom.jsp\">ME</a>");
//		wml.append("|<a href=\"/lswjs/index.jsp\">导航</a>|");
//		wml.append("<a href=\"http://wap.yytun.com/adult/Column.do?columnId=12579&amp;fr=1\">买卖宝</a><br/>");
//		wml.append(DateUtil.getCurrentDatetimeAsStr());
//		return wml.toString();
		
		StringBuilder wml = new StringBuilder();
		HttpSession session = request.getSession(false);
		UserBean loginUser = null;
		if(session != null)
			loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		wml.append(separator);
		if(loginUser == null) {
//			if (session.getAttribute(UserAction2.LOGIN_USER_KEY2) == null){
				// 游客
				wml.append(guestLink);
//				wml.append("<a href=\"/guest/index.jsp\">游乐园</a>|");
//			} 
//			else {
//				// A用户
//				wml.append(userALink);
//			}
		}
		wml.append(bottomShort);
		wml.append(DateUtil.getCurrentDatetimeAsStr());
		return wml.toString();
	}
	static ICacheMap friendTrendCache = CacheManage.friendTrend;
	static ServiceTrend serviceTrend = ServiceTrend.getInstance();
	public static String getBottom(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		// zhangyi add noticeBack control 2006/06/20 start
/*		boolean isExistNoticeBack = false;
		String backTo = "";
		String backTitle = "";
		if (session.getAttribute("noticeBackLinkMap") != null) {
			// session有值时才做处理
			HashMap noticeBackLinkMap = (HashMap) session
					.getAttribute("noticeBackLinkMap");
			int displayTimes = ((Integer) noticeBackLinkMap.get("displayTimes"))
					.intValue();
			if (displayTimes > 5) {
				// 大于5次后不做处理
				session.removeAttribute("noticeBackLinkMap");
			} else {
				// 处理开始，页面显示参数的取得
				noticeBackLinkMap.put("displayTimes", new Integer(
						displayTimes + 1));
				backTo = (String) noticeBackLinkMap.get("backTo");
				backTitle = getTitleByuri(backTo);
				isExistNoticeBack = true;

				// 取得当前页url，如果当前页和session中返回页url相同，没有必要显示通知返回链接
				String currentPage = StringUtil.getCurrentPageURL(request);
				if (currentPage.equals(backTo)) {
					isExistNoticeBack = false;
				}
			}
		}*/
		// zhangyi add noticeBack control 2006/06/20 start

		UserBean loginUser = null;
		if(session != null)
			loginUser = (UserBean) session.getAttribute("loginUser");

		int friendCount = 0;
		String friend = "";
		if (loginUser != null) {
			friendCount = UserInfoUtil.getUserOnlineFriendsCount(loginUser
					.getId());
		}
		if (friendCount != 0) {
			friend = String.valueOf(friendCount);
		}
		// macq_2006-10-20_增加好友数量判断_start
		// macq_2006-10-20_增加好友数量判断_end
		StringBuilder wml = new StringBuilder(512);
		// Vector ms = ChatDataAction.getMessageList();
		// Vector rs = WGameDataAction.getRumorList();
		// ChatAction action = new ChatAction(request);

		/*
		 * if (isExistNoticeBack) { wml.append("<a href=\"" +
		 * ( + "/noticeBack.jsp") + "\">" +
		 * backTitle + "</a><br/>"); }
		 */

		// zhul 2006-09-07 定义点好友进入登陆后返回页 start
		String reURL = request.getRequestURL().toString();
		String queryStr = request.getQueryString();
		if(session != null)
			session.setAttribute("loginReturnURL", queryStr == null ? reURL : reURL + "?" + queryStr);
		// zhul 2006-09-07 定义点好友进入登陆后返回页 end
		wml.append(separator);
		if(loginUser == null) {
//			if (session.getAttribute(UserAction2.LOGIN_USER_KEY2) == null){
//				// 游客
				wml.append(guestLink);
//			} 
//			else {
//				// A用户
//				wml.append(userALink);
//			}
		}
		// String roomId = RoomRateAction.getRandomRoomId();
		// if(roomId==3)roomId=0;
		// lIQ 20070330 修改页面下面的链接
		// wml.append("<a href=\""
		// + (
		// + "/chat/hall.jsp?roomId=" + roomId) + "\">聊天</a>");
		// macq_2006-11-28_增加家园和新手的链接地址(判断用户是否建立家园)_start
		// liuyi 2007-02-01 添加论坛链接 start

		// liuyi 2006-12-26 在线乐酷管理员 start
		// lIQ 20070330 修改页面下面的链接
		if (loginUser == null) {
			wml.append(notLoginBottom);
			// liuyi 2006-12-26 在线乐酷管理员 end
		} else {
			String shortcut = getUserShortcutString(loginUser, response);
			wml.append(shortcut);
			if(shortcut.length() > 0)
				wml.append("<br/>");
			wml.append("<a href=\"/user/ViewFriends.do\">好友" + friend
					+ "</a>");
			wml.append("|<a href=\"/bottom.jsp\">ME</a>");
			wml.append("|<a href=\"/lswjs/index.jsp\">导航</a>|");
			wml.append("<a href=\"/wapIndex.jsp\">首页</a><br/>");
			if(loginUser.getHome() == 1) {
				LinkedList ft = (LinkedList)friendTrendCache.get(new Integer(loginUser.getId()));
				if(ft != null && ft.size() > 0) {
					Integer iid = (Integer)ft.get(0);
					if(loginUser.latestTrend < iid.intValue()) {
						BeanTrend trend = serviceTrend.getTrendById(iid);
						wml.append("*<a href=\"/home/home.jsp\">");
						wml.append(trend.getContentNoLink());
						wml.append("</a><br/>");
					}
				}
			}
			// macq_2007-1-16_增加帮会链接_end
		}
		// lIQ 20070330 修改页面下面的链接
		// macq_2006-11-28_增加家园和新手的链接地址(判断用户是否建立家园)_end
		// liuyi 2007-02-01 添加论坛链接 end
		// liuyi 2006-12-04 start
		// if (loginUser == null || loginUser.getId() < 1) {
		// wml
		// .append("<a href=\""
		// + (
		// + "/jcadmin/tempLogin.jsp")
		// + "\">紧急！找回你的ID和密码</a>");
		// } else {
		// // 找回你的ID|修改密码
		// // liuyi 2007-01-10 最热频道修改 start
		// // wml.append("<a href=\""
		// // + (
		// // + "/jcadmin/tempLogin.jsp") + "\">找回你的ID</a>");
		// // wml.append("|");
		// wml.append("<a href=\""
		// + (
		// + "/user/userInfo.jsp") + "\">修改密码</a>");
		// wml.append("|");
		// wml.append("<a href=\""
		// + (
		// + "/enter/index.jsp") + "\">直接登陆乐酷</a>");
		// // liuyi 2007-01-10 最热频道修改 end
		// }
		// liuyi 2006-12-04 end

		wml.append(DateUtil.getCurrentDatetimeAsStr());
//		if (session.getAttribute(Constants.JC_MID) == null
//				&& SecurityUtil.isMobile(request)) {
//			session.setAttribute(Constants.JC_MID, "");
//			wml.append("<img src=\""
//					+ (Constants.URL_PREFIX + "fpic.jsp")
//					+ "\" alt=\"\"/>");
//		}
		
		/*Integer mobileGetInt = (Integer)session.getAttribute("mobileGet");
		if (session.getAttribute("userMobile") == null
				&& SecurityUtil.isMobile(request)) {
			int mobileGet = 3;
			if(mobileGetInt != null)
				mobileGet = mobileGetInt.intValue();
			if(mobileGet > 0) {
				session.setAttribute("mobileGet", Integer.valueOf(mobileGet));
				wml.append("<img src=\""
						+ (Constants.URL_PREFIX + "fpic2.jsp")
						+ "\" alt=\"\"/>");
			}
		}*/
		
		// wml.append("<select ivalue=\"0\">");
		// if (loginUser != null) {
		// wml.append("<option onpick=\""
		// + response
		// .encodeURL(URL_PREFIX + "/Column.do?columnId=880")
		// + "\">我的个人空间</option>");
		// }
		// wml.append("<option onpick=\""
		// + (URL_PREFIX + "/lswjs/index.jsp")
		// + "\">拉斯维加斯不夜城</option>");
		// wml.append("<option onpick=\""
		// + (URL_PREFIX + "/chat/hall.jsp")
		// + "\">激情聊天大厅</option>");
		// wml.append("<option onpick=\""
		// + (URL_PREFIX + "/ebook/EBookCataList.do")
		// + "\">乐酷电子书城</option>");
		// wml.append("<option onpick=\""
		// + (URL_PREFIX + "/Column.do?columnId=979")
		// + "\">明星写真自拍</option>");
		// wml.append("<option onpick=\""
		// + (URL_PREFIX + "/game/GameCataList.do")
		// + "\">精品游戏下载</option>");
		// wml.append("<option onpick=\""
		// + (URL_PREFIX + "/Column.do?columnId=1099")
		// + "\">新闻八卦娱乐</option>");
		// wml.append("</select>");
		// wml.append("<br/>");

//		// liuyi 2006-11-07 体坛周报wap版返回 start
//		int unionId = 0;
//		Integer unionIdObject = (Integer) session.getAttribute("unionId");
//		if (unionIdObject != null) {
//			unionId = unionIdObject.intValue();
//		}
//		if (unionId == 2) {
//			wml.append("<br/>");
//			wml.append("<a href=\""
//					+ ("http://wap.tsport.cn?unionId=2")
//					+ "\">返回体坛周报ｗａｐ版</a>");
//			wml.append("<br/>");
//		}
//		// liuyi 2006-11-07 体坛周报wap版返回 end

		return wml.toString();
	}

	public static String getBottom11(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuilder wml = new StringBuilder();

		UserBean user = new BaseAction().getLoginUser(request);
		if (user != null) {
			// wml.append("<a href=\""
			// + ("http://wap.joycool.net/DiyIndex.do")
			// + "\">我的定制首页</a>");
			// wml.append("<br/>");
			wml
					.append("<a href=\""
							+ response
									.encodeURL("/wgame/index.jsp")
							+ "\">美女赌场-一品芳泽</a>");
			wml.append("<br/>");
			wml
					.append("<a href=\""
							+ response
									.encodeURL("/Column.do?columnId=939")
							+ "\">激情敏感空间>></a>");
			wml.append("<br/>");
			wml
					.append("<a href=\""
							+ response
									.encodeURL("/Column.do?columnId=880")
							+ "\">我的地盘看我秀>></a>");
			wml.append("<br/>");
			wml.append("<a href=\""
					+ ("/mycart.jsp")
					+ "\">我的收藏夹</a>");
			wml.append("<br/>");
		} else {
			wml
					.append("<a href=\""
							+ response
									.encodeURL("/wgamefree/index.jsp")
							+ "\">美女赌场-一品芳泽</a>");
			wml.append("<br/>");
			wml
					.append("<a href=\""
							+ response
									.encodeURL("/Column.do?columnId=939")
							+ "\">激情敏感空间>></a>");
			wml.append("<br/>");
			wml
					.append("<a href=\""
							+ response
									.encodeURL("/user/register.jsp?backTo="
											+ PageUtil.getBackTo(request))
							+ "\">我的地盘看我秀>></a>");
			wml.append("<br/>");
		}
		wml.append(DateUtil.getCurrentDatetimeAsStr());
		wml.append("<br/>");
		wml.append("<a href=\""
				+ ("/wapIndex.jsp")
				+ "\">乐酷门户</a>");
		wml.append("<br/>");
		wml.append(separator);
		return wml.toString();
	}

	public static String getTop(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuilder wml = new StringBuilder();
		HttpSession session = request.getSession(false);
		UserBean loginUser = null;
		if(session != null)
			loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		String url = request.getRequestURI();

		if (loginUser != null) {
			if(shutdownTime > 0) {		// 服务器即将重启
				long dis = shutdownTime - System.currentTimeMillis();
				if(dis > 0) {
					wml.append("!!服务器将在");
					wml.append(DateUtil.formatTimeInterval(dis));
					wml.append("后重启<br/>");
				}
			}
			if(TeamAction.isNewChatUser(loginUser.getId()) && !loginUser.noticeMark()) {
				wml.append("<a href=\"");
				wml.append(("/team/index.jsp"));
				wml.append("\">圈子里有新消息</a><br/>");
			}
			int[] noticeCounts = UserInfoUtil.getUser(loginUser.getId()).notice;
			boolean familyNotice = FamilyAction.newChatUser.contains(new Integer(loginUser.getId()));
			if(noticeCounts[1] != 0 || (noticeCounts[0] != 0 || familyNotice) && !loginUser.noticeMark()) {	// 如果有信件则强制显示，否则，如果设置免打扰则不显示
				if(noticeCounts[0] == 0) {
					wml.append("*聊天");
				} else {
					wml.append("*<a href=\"/chat/hall.jsp\">聊天(");
					wml.append(noticeCounts[0]);
					wml.append(")</a>");
				}
				if(noticeCounts[1] == 0) {
					wml.append("*信件");
				} else {
					wml.append("*<a href=\"/user/ViewMessages.do\">信件(");
					wml.append(noticeCounts[1]);
					wml.append(")</a>");
				}
				if(familyNotice) {
					wml.append("*<a href=\"/fm/chat/chat.jsp\">家族</a>");
				}
				wml.append("<br/>");
			}
			// lbj_2006-08-07_提高效率_start
			// 如果用户没有新通知，不再查询通知表
			if (!NoticeCacheUtil.isInNoNoticeList(loginUser.getId())) {
				int noticeCount = 0;
				Integer unc = (Integer)session.getAttribute(Constants.USER_NOTICE_COUNT);
				if (unc != null) {
					noticeCount = unc.intValue();
				}
				if (noticeCount >= 3) {
					int systemNoticeCount = 0;
					// 取得系统通知，一条
					Vector noticeList = NewNoticeCacheUtil
							.getSystemNoticeReadedCountMap(loginUser);
					// 获取系统消息数量
					systemNoticeCount = noticeList.size();
					if (systemNoticeCount > 1) {
						systemNoticeCount = 1;
					}
					Integer noticeId = null;
					NoticeBean notice = null;
					if (systemNoticeCount > 0) {
						wml.append("<a href=\""
								+ ("/user/userMessageList.jsp") + "\">");
						wml.append("查看所有通知("+getUserMessageCount(loginUser)+"条)");
						wml.append("</a>");
						wml.append("<br/>");
					}
					// 取得要显示的普通通知，一条条
					noticeList = NewNoticeCacheUtil
							.getUserGeneralNoticeCountMap(loginUser.getId());
					// 得到用户消息id列表
					int userNoticeCount = noticeList.size();
					if (userNoticeCount >= 1) {
						userNoticeCount = systemNoticeCount > 0 ? 0 : 1;
					}
					if (userNoticeCount > 0) {
						wml.append("<a href=\""
								+ ("/user/userMessageList.jsp") + "\">");
						wml.append("查看所有通知("+getUserMessageCount(loginUser)+"条)");
						wml.append("</a>");
						wml.append("<br/>");
					}
				} else {
					int systemNoticeCount = 0;

					Vector noticeList = NewNoticeCacheUtil
							.getSystemNoticeReadedCountMap(loginUser);
					// 获取系统消息数量
					systemNoticeCount = noticeList.size();
					if (systemNoticeCount > 3) {
						systemNoticeCount = 3;
					}
					Integer noticeId = null;
					NoticeBean notice = null;
					for (int i = 0; i < systemNoticeCount; i++) {
						// 如果系统通知等于3条，那第三条变为导航到通知消息列表中
						if (i == 2) {
							wml.append("<a href=\""
									+ ("/user/userMessageList.jsp")
									+ "\">");
							wml.append("查看所有通知("+getUserMessageCount(loginUser)+"条)");
							wml.append("</a>");
							wml.append("<br/>");
							continue;
						}
						// 取得系统消息id
						noticeId = (Integer) noticeList.get(i);
						// liuyi 2006-12-26 空指针异常修复 start
						if (noticeId == null) {
							continue;
						}
						// 从缓存中取得对应系统信息id对应bean
						notice = NewNoticeCacheUtil.getNotice(noticeId
								.intValue());
						// liuyi 2006-12-25 消息缓存问题修改 start
						if (notice == null) {
							continue;
						}
						// liuyi 2006-12-25 消息缓存问题修改 end
						String content = notice.getTitle();
						if (content != null && content.length() > 18)
							content = content.substring(0, 18) + "...";
						wml.append("<a href=\""
								+ ("/viewNotice.jsp?noticeId="
										+ notice.getId() + "&amp;backTo="
										+ PageUtil.getBackTo(request)) + "\">");
						wml.append(StringUtil.toWml(content));
						wml.append("</a>");
						wml.append("<br/>");
						// liuyi 2006-12-26 空指针异常修复 end
					}

					// 取得用户消息通知
					noticeList = NewNoticeCacheUtil
							.getUserGeneralNoticeCountMap(loginUser.getId());
					Vector newNoticeList = new Vector();
					newNoticeList.addAll(noticeList);
					// 得到用户消息id列表
					int userNoticeCount = newNoticeList.size();
					userNoticeCount = getUserNoticeCount(userNoticeCount,
							systemNoticeCount);
					for (int i = 0; i < userNoticeCount; i++) {
						noticeId = (Integer) newNoticeList.get(i);
						if (noticeId == null) {
							continue;
						}
						notice = NewNoticeCacheUtil.getNotice(noticeId
								.intValue());
						if (notice == null) {
							continue;
						}
						
						// 如果用户普通通知等于3条，那第三条变为导航到通知消息列表中
						if (i == 2) {
							wml.append("<a href=\""
									+ ("/user/userMessageList.jsp")
									+ "\">");
							wml.append("查看所有通知("+getUserMessageCount(loginUser)+"条)");
							wml.append("</a>");
							wml.append("<br/>");
							continue;
						}
						// 判断系统消息和用户普通消息混合显示（是否显示通知消息列表）系统消息等于0或者大于等于3条时不做判断
						else if (systemNoticeCount == 1) {
							if (userNoticeCount == 2) {
								if (i == 1) {
									wml
											.append("<a href=\""
													+ response
															.encodeURL("/user/userMessageList.jsp")
													+ "\">");
									wml.append("查看所有通知("+getUserMessageCount(loginUser)+"条)");
									wml.append("</a>");
									wml.append("<br/>");
									continue;
								}
							}
						} else if (systemNoticeCount == 2) {
							if (userNoticeCount == 1) {
								if (i == 0) {
									wml
											.append("<a href=\""
													+ response
															.encodeURL("/user/userMessageList.jsp")
													+ "\">");
									wml.append("查看所有通知("+getUserMessageCount(loginUser)+"条)");
									wml.append("</a>");
									wml.append("<br/>");
									continue;
								}
							}
						}
						//macq_2007-5-17_jy上线通知更改，只显示一次上线_end
						if (url.equals(notice.getHideUrl())) {
							String condition = "id = " + notice.getId();
							noticeService.updateNotice("status = "
									+ NoticeBean.READED, condition);
							NewNoticeCacheUtil.updateUserNoticeById(notice
									.getUserId(), notice.getId());
							// userNoticeCount = NewNoticeCacheUtil
							// .getUserGeneralNoticeCountMap(
							// loginUser.getId()).size();
							// userNoticeCount = getUserNoticeCount(
							// userNoticeCount, systemNoticeCount);
							// i--;
							continue;
						}
						// macq_2007-5-17_jy上线通知更改，只显示一次上线_start
						if (notice.getMark() == NoticeBean.FRIEND_JY) {
							String condition = "id = " + notice.getId();
							noticeService.updateNotice("status = "
									+ NoticeBean.READED, condition);
							NewNoticeCacheUtil.updateUserNoticeById(notice
									.getUserId(), notice.getId());
						}
						String content = notice.getTitle();
						if (content != null && content.length() > 20)
							content = content.substring(0, 18) + "...";
						wml.append("<a href=\""
								+ ("/viewNotice.jsp?noticeId="
										+ notice.getId() + "&amp;backTo="
										+ PageUtil.getBackTo(request)) + "\">");
						wml.append(StringUtil.toWml(content));
						wml.append("</a>");
						wml.append("<br/>");
						// liuyi 2006-12-26 空指针异常修复 end
					}
					// 李北金_2006-06-27_查询优化
					// macq_2006-10-20_优化判断用户未读信息是否大于3条_start
					if (userNoticeCount + systemNoticeCount >= 3) {
						session.setAttribute(Constants.USER_NOTICE_COUNT,
								new Integer(++noticeCount));
					}
					// macq_2006-10-20_优化判断用户未读信息是否大于3条_end
					if (wml.length() == 0) {
						NoticeCacheUtil.addNoNoticeUserId(loginUser.getId());
					}
				}
				// lbj_2006-08-07_提高效率_end
			}
		} else {
			// liuyi 2006-12-20 头信息修改 start
/*			String requestUrl = PageUtil.getCurrentPageURL(request);
			String message = "访客您好,登陆后享受互动游戏/社交之快乐";
			String linkUrl = "/user/login.jsp";
			String mobile = null;
			if(session != null)
				mobile = (String) session.getAttribute("userMobile");
			if (mobile != null) { // 能够取到手机号，都显示头信息
				int id = 0;
				if(mobile.length() > 5) {
					Integer userId = (Integer) OsCacheUtil.get(mobile,
							OsCacheUtil.USER_ID_GROUP,
							OsCacheUtil.USER_ID_FLUSH_PERIOD);
					if (userId == null) {
						String sql = "select id from user_info where mobile='"
								+ mobile + "'";
						id = SqlUtil.getIntResult(sql, Constants.DBShortName);
						OsCacheUtil.put(mobile, new Integer(id),
								OsCacheUtil.USER_ID_GROUP);
					} else {
						id = userId.intValue();
					}
				}
				if (id < 1) { // 新手机号
					message = "欢迎来乐酷，请免费注册拿大红包玩得爽呀！";
					linkUrl = "/user/login.jsp";
				} else { // 老手机号
					message = "老朋友来啦！您处于未登陆状态，没法聊天哦";
					linkUrl = "/user/login.jsp";
				}
				wml.append("<a href=\"");
				wml.append(linkUrl);
				wml.append("\">");
				wml.append(message);
				wml.append("</a>");
				wml.append("<br/>");
			} else { // 取不到手机号，对于首页不显示头信息
				int position = requestUrl.indexOf("backTo");
				if (position > 0) {
					requestUrl = requestUrl.substring(0, position);
				}
				if (requestUrl.indexOf("columnId=" + Constants.INDEX_ID) == -1) { // 非首页
					wml.append("<a href=\""
							+ (linkUrl) + "\">");
					wml.append(message);
					wml.append("</a>");
					wml.append("<br/>");
				}
			}*/
			// liuyi 2006-12-20 头信息修改 end
		}
		// 是否A级用户
//		Integer usera = (Integer)session.getAttribute(UserAction2.LOGIN_USER_KEY2);
//		if (usera != null){
//			UserBean2 user2 = UserAction2.getUser2(usera.intValue());
//			if (user2 != null && user2.getChecked() == 1){
//				wml.append("恭喜,您发送的短信已确认,请<a href=\"/guest/user/modifyA.jsp\">确认</a>基本信息.<br/>");
//			}
//		}
		return wml.toString();
	}

	/**
	 * 判断要显示的用户消息数量
	 * 
	 * @param userNoticeCount
	 * @param systemNoticeCount
	 * @return
	 */
	public static int getUserNoticeCount(int userNoticeCount,
			int systemNoticeCount) {
		int count = userNoticeCount;
		if (userNoticeCount == 0) {
			return count;
		} else if (systemNoticeCount >= 3) {
			count = 0;
		} else {
			if (userNoticeCount >= 3) {
				if (systemNoticeCount == 0) {
					count = 3;
				} else if (systemNoticeCount == 1) {
					count = 2;
				} else {
					count = 1;
				}
			} else if (userNoticeCount == 2) {
				if (systemNoticeCount == 2) {
					count = 1;
				} else {
					count = 2;
				}
			} else {
				count = 1;
			}
		}
		return count;
	}

	public static String getRandomRecord(int type, int date, int count,
			HttpServletRequest request, HttpServletResponse response) {
		StringBuilder wml = new StringBuilder();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select title, address from random_record where type = "
				+ type + " and (to_days(now()) - to_days(create_datetime) <= "
				+ date + ") order by rand() limit 0, " + count;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				wml.append("<a href=\""
						+ (rs.getString("address")) + "\">"
						+ rs.getString("title") + "</a>");
				wml.append("<br/>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbOp.release();
		return wml.toString();
	}

	/**
	 * 根据uri取得系统模块名
	 * 
	 * @param uri
	 * @return
	 */
	private static String getTitleByuri(String uri) {
		String backTitle = "返回进入页";

		// 得到请求url的地址,不包括http://wap.joycool.net
		if (uri.startsWith(URL_PREFIX)) {
			uri = uri.substring(URL_PREFIX.length());
		}
		// 判断从第一个字符往后的uri中包含/标志前的字符个数
		int count = uri.indexOf("/", 1);
		// 如果个数等于0,让个数等于1
		if (count == -1) {
			count = 1;
		}
		// 截取字符串
		uri = uri.substring(1, count);

		// 从MAP中得到位置信息
		PositionBean positionBean = LoadResource.getPostionByUri(uri);
		if (positionBean != null) {
			backTitle = "返回" + positionBean.getPositionInfo();
		}
		return backTitle;
	}

	/**
	 * 
	 * @author Liq
	 * @explain:// Liq_2007-4-12_在JSP网页中加广告链接_start
	 * @datetime:
	 * @param response
	 * @param group
	 *            区别是哪个广告组,并刷
	 */
	static IAdverServic adverSer = ServiceFactory.createAdverService();
	
	public static String getAdver(int group, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		Vector adverList = adverSer.getAdverList("groupid = " + group);
		if (adverList.size() > 0) {
			int temp = RandomUtil.nextInt(adverList.size());
			AdverBean adverbean = (AdverBean) adverList.get(temp);
			if (adverbean != null) {
				ret.append(StringUtil.toWml(adverbean.getName()));
				ret.append("<a href=\"");
				ret.append((adverbean.getUrl().replace(
						"&", "&amp;")));
				ret.append("\">");
				ret.append(StringUtil.toWml(adverbean.getTitle()));
				ret.append("</a><br/>");
			}
		}
		return ret.toString();
	}
	
	public static int getUserMessageCount(UserBean user) {
		//获取系统信息id列表
		Vector systemNoticeList = NewNoticeCacheUtil
				.getSystemNoticeReadedCountMap(user);
		// 取得用户普通消息通知列表
		Vector userNoticeList = NewNoticeCacheUtil
				.getUserGeneralNoticeCountMap(user.getId());
		return systemNoticeList.size()+userNoticeList.size();
	}
	
	static ICacheMap shortcutCache = CacheManage.shortcut;
	public static String getUserShortcutString(UserBean user, HttpServletResponse response) {
		int userId = 0;
		if(user != null)
			userId = user.getId();
		Integer key = Integer.valueOf(userId);
		List s = null;
		synchronized(shortcutCache) {
			s = (List) shortcutCache.get(key);
			if(s == null) {
				HashMap map = UserInfoUtil.getShortcutMap();
				String shortcut = "1";
				if(userId != 0 && user.getUserSetting() != null)
					shortcut = user.getUserSetting().getShortcut();
				String[] ss = shortcut.split(",");
				s = new ArrayList();
				int col = 0;
				for(int i = 0;i < ss.length;i++) {
					int ssid = StringUtil.toInt(ss[i]);
					if(ssid > 0) {
						ShortcutBean bean = (ShortcutBean)map.get(Integer.valueOf(ssid));
						s.add(bean);
					}
				}			
				shortcutCache.put(key, s);
			}
		}
		if(s == null)
			return "";
		
		LinkBuffer lb = new LinkBuffer(response);
		int col = 0;
		Iterator iter = s.iterator();
		while(iter.hasNext()) {
			ShortcutBean bean = (ShortcutBean)iter.next();
			lb.appendLink(bean.getUrl(), bean.getShortName());
		}
		
		return lb.toString();
	}
}
