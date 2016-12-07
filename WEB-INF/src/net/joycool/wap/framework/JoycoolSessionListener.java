/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.framework;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.action.bank.BankAction;
import net.joycool.wap.action.chat.JCRoomChatAction;
import net.joycool.wap.action.dhh.DhhAction;
import net.joycool.wap.action.fs.FSAction;
import net.joycool.wap.action.job.fish.FishAction;
import net.joycool.wap.action.pet.PetAction;
import net.joycool.wap.action.stock2.StockAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.OnlineBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.JCRoomOnlineBean;
import net.joycool.wap.bean.chat.RoomUserBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.farm.FarmWorld;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.RoomUtil;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.Util;

/**
 * @author lbj
 * 
 */
public class JoycoolSessionListener implements HttpSessionListener {
	public static int activeSessionCount = 0;

	public static int maxSessionCount = 0;

	public static Hashtable onlineUsers = new Hashtable();

	// public static Hashtable onlineRegisteredUsers;

	public static Hashtable urlHash = new Hashtable();

	public static IUserService userService = ServiceFactory.createUserService();

	public static IChatService chatService = ServiceFactory.createChatService();
	
	public static INoticeService noticeService = ServiceFactory.createNoticeService();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent event) {
		// liuyi 2007-01-09 用户上下线监控 start
		HttpSession session = event.getSession();
		LogUtil.logDebug("created:" + session.getId());
		// liuyi 2007-01-09 用户上下线监控 end
	}

	public static IUserService getUserService() {
		return userService;
	}

	public static IChatService getChatService() {
		return chatService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		UserBean user = (UserBean) session.getAttribute(
				Constants.LOGIN_USER_KEY);
		if (user != null && user.getId() != 0) {
			String onlineKey = String.valueOf(user.getId());
			// liuyi 2007-01-09 用户上下线监控 start
			
			LogUtil.logDebug("destroy:" + session.getId() + ":"
					+ user.getId());
			// liuyi 2007-01-09 用户上下线监控 end
			// zhangyi-用户第一次退出时给用户push信息 2006-06-022 start

			// mcq_2006-7-20_判断不能在23点到9点之间发送push_start
//			Calendar cal = Calendar.getInstance();
//			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
//			// mcq_2006-7-20_判断不能在23点到9点之间发送push_end
//			if (user.getUs().getLoginCount() == 1
//					&& !user.getMobile().equals("") && currentHour >= 9
//					&& currentHour < 23) {
//				PushUtil.pushMsg(user.getMobile(), "欢迎把乐酷存为书签！",
//						"http://wap.joycool.net/Column.do?columnId=902");
//			}

			// zhangyi-用户第一次退出时给用户push信息 2006-06-022 end

			if (JoycoolSessionListener.activeSessionCount > 0) {
				JoycoolSessionListener.activeSessionCount--;
			}
			if (user.getId() != 0) {
				/**
				 * 删除聊天大厅的在线用户。
				 */
				// zhul_2006-08-09 modify 挂线 start
				Vector onlineUserList = getChatService().getOnlineList(
						"user_id=" + user.getId());
				JCRoomChatAction roomAction = new JCRoomChatAction();
				for (int i = 0; i < onlineUserList.size(); i++) {
					JCRoomOnlineBean onlineuser = (JCRoomOnlineBean) onlineUserList
							.get(i);
					roomAction.dealRoomTransform(user, onlineuser.getRoomId(),
							-1);
					RoomUtil.deleteRoomOnlineUser(onlineuser.getRoomId(), user.getId());
				}
				// zhul_2006-08-09 modify 挂线 end

			}
			if(!OnlineUtil.isUserKicked(onlineKey, session.getId())) {
				getUserService().deleteOnlineUser("user_id = " + user.getId());	// 删除在线用户（全站）
				JoycoolSessionListener.removeOnlineUser(onlineKey);
				
				// 在用户退出时，更新用户状态信息：last_logout_time,total_online_time,
				UserInfoUtil.service.updateUserStatus(
								"last_logout_time=now(), total_online_time=total_online_time-30+((UNIX_TIMESTAMP()-UNIX_TIMESTAMP(last_login_time))/60)",
								"user_id=" + user.getId());
				UserStatusBean us = UserInfoUtil.getUserStatus(user.getId());
				if(us != null)
					us.setTotalOnlineTime(SqlUtil.getIntResult("select total_online_time from user_status where user_id=" + user.getId()));
				FarmWorld.logout(user.getId());
			}
			JoycoolSessionListener.removeOnlineUser(session.getId());
		} else {
			// liuyi 2007-01-09 用户上下线监控 start
			LogUtil.logDebug("destroy:" + session.getId() + ":0");
			// liuyi 2007-01-09 用户上下线监控 end
			if (JoycoolSessionListener.activeSessionCount > 0) {
				JoycoolSessionListener.activeSessionCount--;
			}
			JoycoolSessionListener.removeOnlineUser(session.getId());
		}
		// JoycoolSessionListener.getUrlHash().remove(event.getSession().getId());
	}

	public static int getActiveSessionCount(HttpServletRequest request) {
		return activeSessionCount;
	}

	public static Hashtable getOnlineUsers() {
		try {
			onlineUsers.clear();
			Hashtable onlineBeanHash = OnlineUtil.getOnlineHash();
			Enumeration enu = onlineBeanHash.keys();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				UserBean user = (UserBean) OnlineUtil.getOnlineBean(key);
				if (user != null) {
					onlineUsers.put(key, user);
				}
			}
		} catch (Exception e) {
		}

		return onlineUsers;
	}

	public static Hashtable getUrlHash() {
		return urlHash;
	}

	public static void updateOnlineStatus(String key, UserBean user) {
		if (key == null || user == null)
			return;

		OnlineUtil.updateOnlineBean(key, user);
	}

	public static UserBean getOnlineUser(String key) {
		if (key == null)
			return null;

		return (UserBean) OnlineUtil.getOnlineBean(key);
	}

	public static void removeOnlineUser(String key) {
		if (key == null)
			return;

		OnlineUtil.removeOnlineBean(key);
	}

	// liuyi 2006-12-20 在线用户统计修改 start
	/**
	 * 获取在线session数（活跃状态的）
	 * 
	 * @return
	 */
	public static int getOnlineUserCount() {
		int ret = 0;
		String key = "active";
		Integer count = (Integer) OsCacheUtil.get(
				OsCacheUtil.ONLINE_USER_COUNT_GROUP, key,
				OsCacheUtil.ONLINE_USER_COUNT_FLUSH_PERIOD);
		if (count != null) {
			ret = count.intValue();
		} else {
			ret = OnlineUtil.getActiveSessionCount();
			OsCacheUtil.put(key, new Integer(ret),
					OsCacheUtil.ONLINE_USER_COUNT_GROUP);
		}
		return ret;
	}

	// liuyi 2006-12-20 在线用户统计修改 end

	public static void addOnlineUser(HttpServletRequest request, UserBean user) {

		// 非注册用户 key用sessionId
		if (user.getId() == 0) {
			String key = request.getSession().getId();
			if (JoycoolSessionListener.getOnlineUser(key) == null) {
				addOnline();
			}
			JoycoolSessionListener.updateOnlineStatus(key, user);
		}
		// 注册用户 key用用户ID
		else {
			String key = "" + user.getId();
			if (JoycoolSessionListener.getOnlineUser(key) == null) {
				addOnline();
			}

			HttpSession session = request.getSession();
			UserBean loginUser = (UserBean) session
					.getAttribute(Constants.LOGIN_USER_KEY);
			if (loginUser != null) {
				// 判断登录用户是否在聊天室
				int count = getChatService().getOnlineCount(
						"user_id=" + user.getId());
				if (count > 0) {
					Vector onlineUserList = getChatService().getOnlineList(
							"user_id=" + user.getId());
					JCRoomChatAction roomAction = new JCRoomChatAction();
					for (int i = 0; i < onlineUserList.size(); i++) {
						JCRoomOnlineBean onlineuser = (JCRoomOnlineBean) onlineUserList
								.get(i);
						roomAction.dealRoomTransform(user, onlineuser
								.getRoomId(), -1);
					}
					getChatService()
							.deleteOnlineUser("user_id=" + user.getId());
					// zhul_2006-08-09 modify 挂线 end

				}
				removeRoomOnlineData(loginUser);
				loginUser = null;
				session.removeAttribute(Constants.LOGIN_USER_KEY);
			}
			if (loginUser == null) {
				// zhul-增加代码块 start 2006-06-06 将用户状态加入userBean中
				UserStatusBean usb = UserInfoUtil.getUserStatus(user.getId());
				user.setUs(usb);
				// zhul-增加代码块 end 2006-06-06 将用户状态加入userBean中

				// zhul add new code start 2006-06-12
				// 判断用户是否是今天第一次登录，如果是向用户发出一条系统信息告诉他当前的头衔
				boolean bTodayFirstLogin = false;
				String lastLoginTime = usb.getLastLoginTime();
				if (lastLoginTime != null) {
					int logoutYear = Integer.parseInt(lastLoginTime.substring(
							0, 4));
					int logoutMonth = Integer.parseInt(lastLoginTime.substring(
							5, 7));
					int logoutDay = Integer.parseInt(lastLoginTime.substring(8,
							10));
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH) + 1;
					int day = cal.get(Calendar.DAY_OF_MONTH);
					if (logoutYear == year && logoutMonth == month
							&& logoutDay == day) {
						bTodayFirstLogin = false;
					} else {
						bTodayFirstLogin = true;
					}
				} else {
					bTodayFirstLogin = true;
				}

				// liuyi 2007-02-08 新注册用户聊天消息 start
				String lastLogoutTime = usb.getLastLoginTime();
				if (lastLoginTime != null && lastLogoutTime != null
						&& lastLoginTime.equals(lastLogoutTime)) {
					bTodayFirstLogin = true;
				}
				// liuyi 2007-02-08 新注册用户聊天消息 end

				if (bTodayFirstLogin) {

					// zhangy add 2006-06-30 start
					// 判断用户是否是今天第一次登录(用jc_days_login_user表作判断条件)
					// 如果表中没有用户记录,是真正的第一次登录,可以添加记录，加乐币,发消息;如果有，不做任何操作
					int loginCount = getUserService().getDaysLoginUserCount(
							"user_id = " + user.getId());
					if (loginCount < 1) {
						// mcq_2006-8-29_登录用户五步走,第一步,用户每天第一次登录的时候收到消息提示提示用户ID和密码_start
						// 添加1000乐币提示消息
						if (Util.getFlag().equals("first")) {
							NoticeBean notice = new NoticeBean();
							notice.setUserId(user.getId());
							notice.setTitle("乐酷提醒您记住自己的ID和密码");
							notice.setContent("");
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setHideUrl("");
							notice.setLink("/user/notice.jsp");
							noticeService.addNotice(
									notice);
						}
						// mcq_2006-8-29_登录用户五步走,第一步,用户每天第一次登录的时候收到消息提示提示用户ID和密码_end

						// 用户升级到1级之前，每天第一次登录时提示奖励1000乐币（每天都给用户加1000乐币）
//						if ((user.getUs()).getRank() < 1) {
//							// 加1000乐币
//							UserInfoUtil.updateUserCash(user.getId(), 1000,
//									UserCashAction.PRESENT, "每天都给用户加1000乐币");
//							// 添加1000乐币提示消息
//							NoticeBean notice = new NoticeBean();
//							notice.setUserId(user.getId());
//							notice.setTitle("给您奖励1000乐币了！");
//							notice.setContent("恭喜您，给您奖励1000乐币了！");
//							notice.setType(NoticeBean.GENERAL_NOTICE);
//							notice.setHideUrl("");
//							notice.setLink("/user/userInfo.jsp");
//							noticeService.addNotice(notice);
//						}
						// 向jc_days_login_user表中添加标志记录
						userService.addDaysLoginUse(user.getId());
					}
					// zhangy add 2006-06-30 start
				}
				// zhul-增加代码块 start 2006-06-07
				// 在用户登录时更新用户last_login_time,login_count状态值
				UserInfoUtil.service.updateUserStatus(
						"last_login_time=now(), login_count=login_count+1",
						"user_id=" + user.getId());
				usb.setLastLoginTime(DateUtil.formatSqlDatetime(new Date()));
				usb.setLastLoginTime2(System.currentTimeMillis());
				usb.setLoginCount(usb.getLoginCount() + 1);
				// zhul-增加代码块 end 2006-06-07
				// zhul add new code end 2006-06-12
				// zhul-增加代码块 start 2006-06-06 将用户状态加入userBean中
				// fanys2006-08-11
				// usb = getUserService().getUserStatus("user_id=" +
				// user.getId());
				user.setUs(usb);
				// 登陆后，如果密码过于简单，并且登陆次数达到3，则提示
				if(usb.getLoginCount() >= 3 && !SecurityUtil.isEncodedPasswordSecure(user.getPassword()) && !SqlUtil.isTest) {
					NoticeAction.sendNotice(user.getId(), "!!账号密码过于简单请修改",
							NoticeBean.GENERAL_NOTICE, "/user/setpwd.jsp");
				}
				// zhul-增加代码块 end 2006-06-06 将用户状态加入userBean中
				// 判断用户是否是今天第一次登录，如果是向用户发出一条系统信息告诉他当前的头衔
			}
			//获取用户设置状态
			UserSettingBean userSetting = getUserSetting(user);
			//设置用户设置状态到用户信息中
			user.setUserSetting(userSetting);
			session.setAttribute(Constants.LOGIN_USER_KEY, user);
			// liuyi 2007-01-10 用户上下线日志 start
			LogUtil.logDebug("login:" + session.getId() + ":"
					+ user.getId());
			// liuyi 2007-01-10 用户上下线日志 end
			String sessionid = session.getId();
			String userKey = String.valueOf(user.getId());
			JoycoolSessionListener.removeOnlineUser(sessionid);
			JoycoolSessionListener.updateOnlineStatus(userKey , user);
			String allowVisit = (String)session.getAttribute("allowVisit");
			if(!"op".equals(allowVisit))
				OnlineUtil.setOnlineSessionId(userKey, sessionid);
			

			// 将在线用户加入到数据库
			OnlineBean online = new OnlineBean();
			// liuyi 2006-12-01 程序优化 start
			// synchronized (user)
			{
				int olCount = getUserService().getOnlineUserCount(
						"user_id = " + user.getId());
				if (olCount > 0) {
					getUserService().updateOnlineUser(
							"session_id = '" + session.getId()
									+ "'", "user_id = " + user.getId());
				} else {
					online.setUserId(user.getId());
					online.setSessionId(session.getId());
					// macq_2006-12-25_增加在线用户所属帮会id_start(0为没有帮会,-1为申请阶段)
					online.setTongId(user.getUs().getTong());
					// macq_2006-12-25_增加在线用户所属帮会id_end(0为没有帮会,-1为申请阶段)
					getUserService().addOnlineUser(online);
				}
			}
			// liuyi 2006-12-01 程序优化 end

			// mcq_2006-10-30_用户上线后,给在线的金兰发通知说我已上线_start
			// liuyi 2006-11-26 交友优化 start
			List jyFriendIdList = userService.getJyFriendIds(user.getId()); // UserInfoUtil.getUserFriends(user.getId());
			// liuyi 2007-01-09 用户上线后，给夫/妻发通知 start
			UserStatusBean userStatus = (UserStatusBean) UserInfoUtil
					.getUserStatus(user.getId());
			if (userStatus.getMark() == 2) {
				String sql = "select friend_id from user_friend where mark=2 and user_id="
						+ user.getId();
				int id = SqlUtil.getIntResult(sql, Constants.DBShortName);
				if (id > 0) {
					jyFriendIdList.add(new Integer(id));
				}
			}
			// liuyi 2007-01-09 用户上线后，给夫/妻发通知 end
			boolean flag = false;
			for (int i = 0; i < jyFriendIdList.size(); i++) {
				String friendId = ((Integer) jyFriendIdList.get(i)).intValue()
						+ "";
				flag = OnlineUtil.isOnline(friendId);
				if (flag) {
					NoticeBean notice = new NoticeBean();
					notice.setUserId(StringUtil.toInt(friendId));
					notice.setTitle(user.getNickName() + "上线喽!");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					// liuyi 2007-02-09 上线通知链接修改 start
					notice.setHideUrl("/chat/hall.jsp");
					notice.setLink("/chat/post.jsp?toUserId=" + user.getId());
					notice.setMark(NoticeBean.FRIEND_JY);
					// liuyi 2007-02-09 上线通知链接修改 end
					noticeService.addNotice(notice);
				}
			}
			// liuyi 2006-11-26 交友优化 end
			// mcq_2006-10-30_用户上线后,给在线的金兰发通知说我已上线_end

			// macq_2007-5-16_删除所有人人对战系统消息通知_start
//			noticeService.updateNotice(
//					"status=" + NoticeBean.READED,
//					"user_id=" + user.getId() + " and status="
//							+ NoticeBean.NOT_READ + " and mark ="
//							+ NoticeBean.PK_GAME);
			// macq_2007-5-16_删除所有人人多回合对战系统消息通知_start
//			noticeService.updateNotice(
//					"status=" + NoticeBean.READED,
//					"user_id=" + user.getId() + " and status="
//							+ NoticeBean.NOT_READ + " and mark ="
//							+ NoticeBean.PK_GAME_HALL);
			// liq_2007-6-18_删除所有宠物游戏系统消息通知_start
//			noticeService.updateNotice(
//					"status=" + NoticeBean.READED,
//					"user_id=" + user.getId() + " and status="
//							+ NoticeBean.NOT_READ + " and mark ="
//							+ NoticeBean.PET);
			// 清空某个用户普通消息Id的属性缓存。
//			NewNoticeCacheUtil.flushUserNotice(user.getId());
			// macq_2007-5-16_删除所有人人对战系统消息通知_end
			// 将用户加入到聊天室在线
			int count = getChatService().getOnlineCount(
					"user_id=" + user.getId());
			if (count > 0) {
				// zhul_2006-08-09 modify 挂线 start
				Vector onlineUserList = getChatService().getOnlineList(
						"user_id=" + user.getId());
				JCRoomChatAction roomAction = new JCRoomChatAction();
				for (int i = 0; i < onlineUserList.size(); i++) {
					JCRoomOnlineBean onlineuser = (JCRoomOnlineBean) onlineUserList
							.get(i);
					roomAction.dealRoomTransform(user, onlineuser.getRoomId(),
							0);
				}
				// getChatService().deleteOnlineUser("user_id=" + user.getId());
				// zhul_2006-08-09 modify 挂线 end

			} else {
				RoomUserBean roomUser = getChatService().getJCRoomUser(
						"room_id=0 and status=0 or status=2 and user_id="
								+ user.getId());
				if (roomUser == null) {
					// fanys 2006-09-16 start
					// chatService.addOnlineUser("0, " + user.getId() +
					// ",now()");]
					RoomUtil.addRoomOnlineUser(0, user.getId());
					// fanys 2006-09-16 end
					// zhul 2006-07-01 当用户换房间时进行房间转换记录 start
					JCRoomChatAction roomAction = new JCRoomChatAction();
					roomAction.dealRoomTransform(user, -1, 0);
					request.getSession(true).setAttribute("oldRoomId", "0");
				}
				// zhul 2006-07-01 当用户换房间时进行房间转换记录 end
			}

		}
	}
	
//	如果是登陆过的session，自动先登出，以免计算online time的时候错误
	public static void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null)
			return;
		UserBean user = (UserBean)session.getAttribute("loginUser");
		if(user != null)
			logout(request, user);
			
	}

	// ly 20060830 用户注销
	public static void logout(HttpServletRequest request, UserBean user) {
		if (request == null || user == null)
			return;
		// 在用户退出时，更新用户状态信息：last_logout_time,total_online_time,
		UserInfoUtil.service.updateUserStatus(
						"last_logout_time=now(), total_online_time=total_online_time+((UNIX_TIMESTAMP()-UNIX_TIMESTAMP(last_login_time))/60)",
						"user_id=" + user.getId());
		UserStatusBean us = UserInfoUtil.getUserStatus(user.getId());
		if(us != null)
			us.setTotalOnlineTime(SqlUtil.getIntResult("select total_online_time from user_status where user_id=" + user.getId()));
		removeRoomOnlineData(user);
		removeOnlineData(user);

		HttpSession session = request.getSession();
		UserInfoUtil.userInfoCache.srm(user.getId());
		kickout(session);
		FarmWorld.logout(user.getId());
		// liuyi 2007-01-10 用户上下线日志 start
		LogUtil.logDebug("logout:" + session.getId() + ":" + user.getId());
		// liuyi 2007-01-10 用户上下线日志 end
	}
	
	/**
	 * 把用户的这个session踢下线，不影响任何其他数据
	 * @param session
	 */
	public static void kickout(HttpSession session) {
		if (session == null)
			return;

		session.removeAttribute(Constants.LOGIN_USER_KEY);
		session.removeAttribute(BankAction.BANK_PW);
		session.removeAttribute(FSAction.FS_USER_KEY);
		session.removeAttribute(FishAction.FISH_USER_KEY);
		session.removeAttribute(DhhAction.DHH_USER_KEY);
		session.removeAttribute(PetAction.PET_USER_KEY);
		session.removeAttribute(StockAction.STOCK_USER_KEY);
		session.setAttribute(Constants.NOT_REGISTER_KEY, new Integer(1));
		session.setMaxInactiveInterval(180);	// 这个session没用了，但保留3分钟便于用户再去登陆
	}

	// ly 20060830 删除在线数据
	public static void removeOnlineData(UserBean user) {
		if (user == null)
			return;

		removeOnlineUser("" + user.getId());
		getUserService().deleteOnlineUser("user_id=" + user.getId());
	}

	// ly 20060830 删除聊天室在线数据
	public static void removeRoomOnlineData(UserBean user) {
		if (user == null)
			return;

		int count = getChatService().getOnlineCount("user_id=" + user.getId());
		if (count > 0) {
			// zhul_2006-08-09 modify 挂线 start
			Vector onlineUserList = getChatService().getOnlineList(
					"user_id=" + user.getId());
			JCRoomChatAction roomAction = new JCRoomChatAction();
			for (int i = 0; i < onlineUserList.size(); i++) {
				JCRoomOnlineBean onlineuser = (JCRoomOnlineBean) onlineUserList
						.get(i);
				roomAction.dealRoomTransform(user, onlineuser.getRoomId(), -1);
			}
			getChatService().deleteOnlineUser("user_id=" + user.getId());
			// zhul_2006-08-09 modify 挂线 end
		}
	}

	public static void addOnline() {
		JoycoolSessionListener.activeSessionCount++;
		if (maxSessionCount < activeSessionCount) {
			maxSessionCount = activeSessionCount;
		}
	}

	public static Hashtable getUrlMap(String sessionId) {
		// Hashtable urlMap = (Hashtable) getUrlHash().get(sessionId);
		// if (urlMap == null) {
		// urlMap = new Hashtable();
		// getUrlHash().put(sessionId, urlMap);
		// }
		return getUrlHash();
	}

	public static void updateOnlineUser(HttpServletRequest request,
			UserBean user) {
		addOnlineUser(request, user);
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：获取用户设置信息
	 * @datetime:2007-7-23 15:07:22
	 * @param user
	 * @return
	 * @return UserSettingBean
	 */
	public static UserSettingBean  getUserSetting(UserBean user) {
		UserSettingBean userSetting = user.getUserSetting();
		if(userSetting==null){
			userSetting = getUserService().getUserSetting("user_id="+user.getId());
			if(userSetting==null){
				userSetting = new UserSettingBean();
			}else{
				userSetting.setNoticeMark(0);
				//userSetting.setPicMark(0);
			}
		}
		return userSetting;
	}
}
