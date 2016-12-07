/*
 *mcq_4_用户发送动作列表 日期 2006-6-8 
 */
package net.joycool.wap.action.user;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.chat.JCRoomChatAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.RankActionBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ContentList;
import net.joycool.wap.util.NewNoticeCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class SendAction extends CustomAction{
	UserBean loginUser;

	public final static int NUMBER_PER_PAGE = 10;
	
	public static int ACTION_A_ITEM = 5076;
	public static int ACTION_B_ITEM = 5077;
	public static int ACTION_C_ITEM = 5078;
	public static int ACTION_D_ITEM = 5079;
	public static int ACTION_E_ITEM = 5080;

	//手铐道具
	public static HashMap handcuff = new HashMap();
	
	public class CuffUser{
		public CuffUser(){}
		public int fromUid;
		public long time;
	}
	
	static IUserService service = ServiceFactory.createUserService();;

	// macq_2006-12-6_群发每条所需乐币_start
	final static int GROUP_SEND_MESSAGE_GAME_POINT = 10000;

	// macq_2006-12-6_群发每条所需乐币_start
	// macq_2006-12-6_群发时间间隔_start
	final static int GROUP_SEND_MESSAGE_TIME = 1000 * 60 * 10;

	// macq_2006-12-6_群发时间间隔_start
	UserBean us;

	static INoticeService noticeService = ServiceFactory.createNoticeService();

	// macq_2006-12-6_增加聊天接口_start
	static IChatService chatService = ServiceFactory.createChatService();

	// macq_2006-12-6_增加聊天接口_end

	// 构造函数
	public SendAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
		// fanys2006-08-11
		// loginUser = UserInfoUtil.getUserInfo(loginUser);
		loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
	}

	// fanys--start 2006-06-16

	/*
	 * 发送动作
	 */
	public void send(HttpServletRequest request) {
		// 接收页面传值,值为接收动作的用户ID
		String userId = request.getParameter("toUserId");
		// 得到接收动作用户的信息
		us = this.getUser(userId);
		String roomId = request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
		
		int totalCount = getRankActionLowCount(loginUser.getUs().getRank());
		PagingBean paging = new PagingBean(this, totalCount, NUMBER_PER_PAGE, "p");
		
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		String prefixUrl = "sendAction.jsp?roomId=" + roomId + "&amp;toUserId="
				+ userId + "&amp;backTo=" + URLEncoder.encode(backTo);

		// 取得要显示的消息列表
//		int start = paging.getStartIndex();
//		int end = NUMBER_PER_PAGE;

		// fanys--start 2006-06-16

		// 获取用户可以发送的动作，按照动作发送次数多少和动作等级排序
//		String strsql = " select d.* from "
//				+ " (SELECT * from jc_rank_action as  a join  "
//				+ " (select action_id,count(action_id) as countid from jc_action_record "
//				+ " where from_id='" + loginUser.getId()
//				+ "' group by action_id ) "
//				+ " as b on a.id=b.action_id )as c "
//				+ " right join (SELECT * from jc_rank_action where rank_id<="
//				+ loginUser.getUs().getRank() + ") as d "
//				+ " on c.id=d.id order by c.countid desc, rank_id asc "
//				+ " limit " + start + ", " + end;
		List actionList = getRankActionListOrderByAction(loginUser.getUs().getRank());
		actionList = actionList.subList(paging.getStartIndex(), paging.getEndIndex());
		// fanys--end 2006-06-16

		request.setAttribute("paging", paging);
		request.setAttribute("backTo", backTo);
		request.setAttribute("prefixUrl", prefixUrl);
		// 页面传递用户可以发送得动作list
		request.setAttribute("actionList", actionList);

		// 取得用户升级后可以发送的工作
		List updateActionList = getRankActionList(loginUser.getUs().getRank() + 1);
		// 页面传递用户可以发送得动作list
		request.setAttribute("updateActionList", updateActionList);
		// 页面传递接收动作者的信息
		request.setAttribute("toUser", us);
	}

	static ICacheMap rankActionCache = CacheManage.rankAction;
	public static RankActionBean getRankAction(int id) {
		return getRankAction(Integer.valueOf(id));
	}
	// 返回对应的动作，如果id>0返回对应id的动作
	public static RankActionBean getRankAction(Integer iid) {
		synchronized(rankActionCache) {
			RankActionBean bean = (RankActionBean)rankActionCache.get(iid);
			if(bean == null) {
				bean = service.getRankAction("id=" + iid);
				if(bean != null)
					rankActionCache.put(iid, bean);
			}
			return bean;
		}
	}
	// 返回对应rank_id的动作列表
	public static List getRankActionList(int rankId) {
		String iid = "c" + rankId;
		synchronized(rankActionCache) {
			List list = (List)rankActionCache.get(iid);
			if(list == null) {
				list = service.getRankActionList("rank_id=" + rankId);
				if(list != null)
					rankActionCache.put(iid, list);
			}
			return list;
		}
	}
	// 返回小于rank_id的动作数量
	public static int getRankActionLowCount(int rankId) {
		String iid = "d" + rankId;
		synchronized(rankActionCache) {
			Integer ic = (Integer)rankActionCache.get(iid);
			if(ic == null) {
				int count = service.getRankActionCount(" where rank_id<=" + rankId);
				if(count > 0) {
					rankActionCache.put(iid, new Integer(count));
					return count;
				} else {
					return 0;
				}
			}
			return ic.intValue();
		}
	}
	// 返回小于rank_id的动作数量，并且排序
	public static List getRankActionListOrderByAction(int rankId) {
		String iid = "e" + rankId;
		synchronized(rankActionCache) {
			List list = (List)rankActionCache.get(iid);
			if(list == null) {
				list = service.getRankActionList("rank_id<=" + rankId + " order by seq desc, rank_id asc");
				if(list != null)
					rankActionCache.put(iid, list);
			}
			return list;
		}
	}
	/*
	 * 处理动作请求
	 */
	public void result(HttpServletRequest request, HttpServletResponse response) {
		String tip = null;
		String result = "success";
		String roomId = request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		// 判断用户是否登陆
		if (loginUser != null) {
			// 等到接收动作的用户ID
			String toUserId = request.getParameter("toUserId");
			// 得到用户信息
			us = this.getUser(toUserId);
			request.setAttribute("toUser", us);
			if (!isCooldown("chat", 5000)) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "你的发言太快了！请先休息一会再继续。");
				return;
			}
			// 得到要发送动作的动作ID号
			int actionId = getParameterInt("actionId");
			if (us == null || actionId <= 0 || us.getId() == loginUser.getId()) {
				tip = "您没有发送此动作的权限";
				result = "failure";
				request.setAttribute("tip", tip);
				request.setAttribute("result", result);
				return;
			}
			
			//递烟动作
			if(actionId == 200) {
				
				int[] secRoomId = JCRoomChatAction.getSecondRoomId(
						loginUser.getId(), us.getId(), Integer
								.parseInt(roomId));
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setFromId(loginUser.getId());
				jcRoomContent.setToId(us.getId());
				jcRoomContent.setFromNickName(loginUser.getNickName());
				jcRoomContent.setToNickName(us.getNickName());
				jcRoomContent.setContent("" + actionId);
				jcRoomContent.setAttach("");
				jcRoomContent.setIsPrivate(0);
				jcRoomContent.setRoomId(secRoomId[0]);
				jcRoomContent.setSecRoomId(secRoomId[1]);
				jcRoomContent.setMark(1);
				// ServiceFactory.createChatService().addContent(
				// loginUser.getId() + "," + us.getId() + ",'"
				// + loginUser.getNickName() + "','"
				// + us.getNickName() + "','" + actionId
				// + "','',now(),0," + secRoomId[0] + ","
				// + secRoomId[1] + ",1");
				ServiceFactory.createChatService().addContent(
						jcRoomContent);
				return;
			}
			
			// 读取SESSION中的用户等级
			loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
			int rank = loginUser.getUs().getRank();
			// 通过接收的动作ID得到对应的动作等级记录
			RankActionBean checkAcion = getRankAction(actionId);
			// int a=userAcion.getNeedGamePoint();
			// int b=loginUser.getUs().getGamePoint();
			// 判断用户等级ID是否小于要发送动作等级
			if (rank < checkAcion.getRankId()) {
				tip = "您没有发送此动作的权限";
				result = "failure";
				// 判断用户乐币是否满足发送动作所需乐币数
				// fanys2006-08-11
			} else if (false && UserInfoUtil.getUserStatus(loginUser.getId())
					.getGamePoint() < checkAcion
			// } else if (loginUser.getUs().getGamePoint() < checkAcion
					.getNeedGamePoint()) {
				tip = "您没有足够的乐币发送此动作";
				result = "failure";
			}
			// 武翠霞2006-10-20黑名单里的用户不能发送动作 start
			else if ((service.isUserBadGuy(StringUtil.toInt(toUserId),
							loginUser.getId()))) {
				result = "failure";
				tip = "你在对方的黑名单里，不能给他发送动作！";

			}
			// 武翠霞2006-10-20黑名单里的用户不能发送动作 end
			// 判断是否出错,如果出错则返回
			if ("failure".equals(result)) {
				request.setAttribute("tip", tip);
				request.setAttribute("result", result);
				return;
			} else {
				// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_start
				UserBean onlineUser = (UserBean) OnlineUtil
						.getOnlineBean(toUserId + "");
				if (onlineUser != null) {
					if (onlineUser.noticeMark()) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "对方设置自己状态为免打扰，您不能给他发送动作！");
						return;
					}
				}


				// fanys2006-08-11
				// loginUser.getUs().setGamePoint(point);
				// 加入发送动作历史记录
//				ActionRecordBean record = new ActionRecordBean();
//				record.setFromId(loginUser.getId());
//				record.setToId(us.getId());
//				record.setActionId(checkAcion.getId());
//				service.addActionRecord(record);
				// 加入消息系统
				String title = checkAcion.getReceiveMessage();
				// mcq_更改显示用户昵称 时间 2006-6-19
				title = title.replace("XXX", loginUser.getNickName());
				// mcq_end
				us.notice[0]++;
				/*
				NoticeBean notice = new NoticeBean();
				notice.setUserId(us.getId());
				notice.setTitle(title);
				notice.setMark(notice.SENDACTION);
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setHideUrl("/chat/hall.jsp");
				notice.setLink("/chat/hall.jsp");
				noticeService.addNotice(notice);
				*/
				String isPrivate = request.getParameter("isPrivate");
				// zhul_2006-08-22 add for chat model start
				int[] secRoomId = JCRoomChatAction.getSecondRoomId(
						loginUser.getId(), us.getId(), Integer
								.parseInt(roomId));
				// zhul_2006-08-22 add for chat model end
				if (isPrivate.equals("no")) {
					// mcq_2006-6-26_增加一条聊天记录_start
					// liuyi 2006-09-16 聊天室加缓存 start
					JCRoomContentBean jcRoomContent = new JCRoomContentBean();
					jcRoomContent.setFromId(loginUser.getId());
					jcRoomContent.setToId(us.getId());
					jcRoomContent.setFromNickName(loginUser.getNickName());
					jcRoomContent.setToNickName(us.getNickName());
					jcRoomContent.setContent("" + actionId);
					jcRoomContent.setAttach("");
					jcRoomContent.setIsPrivate(0);
					jcRoomContent.setRoomId(secRoomId[0]);
					jcRoomContent.setSecRoomId(secRoomId[1]);
					jcRoomContent.setMark(1);
					// ServiceFactory.createChatService().addContent(
					// loginUser.getId() + "," + us.getId() + ",'"
					// + loginUser.getNickName() + "','"
					// + us.getNickName() + "','" + actionId
					// + "','',now(),0," + secRoomId[0] + ","
					// + secRoomId[1] + ",1");
					ServiceFactory.createChatService().addContent(
							jcRoomContent);
					// liuyi 2006-09-16 聊天室加缓存 end
					// 清空聊天室在map中的记录
					// RoomCacheUtil.flushRoomContentId(secRoomId[0]);
					// RoomCacheUtil.flushRoomContentId(secRoomId[1]);
					// 清空聊天室在map中的记录
					// mcq_2006-6-26_增加一条聊天记录_end
				} else {
					// liuyi 2006-09-16 聊天室加缓存 start
					JCRoomContentBean jcRoomContent = new JCRoomContentBean();
					jcRoomContent.setFromId(loginUser.getId());
					jcRoomContent.setToId(us.getId());
					jcRoomContent.setFromNickName(loginUser.getNickName());
					jcRoomContent.setToNickName(us.getNickName());
					jcRoomContent.setContent("" + actionId);
					jcRoomContent.setAttach("");
					jcRoomContent.setIsPrivate(1);
					jcRoomContent.setRoomId(secRoomId[0]);
					jcRoomContent.setSecRoomId(secRoomId[1]);
					jcRoomContent.setMark(1);
					// ServiceFactory.createChatService().addContent(
					// loginUser.getId() + "," + us.getId() + ",'"
					// + loginUser.getNickName() + "','"
					// + us.getNickName() + "','" + actionId
					// + "','',now(),1," + secRoomId[0] + ","
					// + secRoomId[1] + ",1");
					ServiceFactory.createChatService().addContent(
							jcRoomContent);
					// liuyi 2006-09-16 聊天室加缓存 end
				}
				// 增加用户经验值
				RankAction.addPoint(loginUser, Constants.RANK_ACTION);

			}
			// 传值到页面
			request.setAttribute("rankAcion", checkAcion);
			request.setAttribute("result", result);
		}
	}

	/*
	 * 查看别人发送给自己的动作
	 */
	public void receiveHistory(HttpServletRequest request,
			HttpServletResponse response) {
		if (loginUser != null) {
			int totalCount = service.getActionRecordCount(" where to_id="
					+ loginUser.getId());
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "receiveHistory.jsp?backTo="
					+ URLEncoder.encode(backTo);

			// 取得要显示的消息列表
			int start = pageIndex * NUMBER_PER_PAGE;
			int end = NUMBER_PER_PAGE;
			Vector recordList = service.getActionRecordList("to_id="
					+ loginUser.getId()
					+ " order by action_datetime desc limit " + start + ", "
					+ end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("backTo", backTo);
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("recordList", recordList);
		}
	}

	/*
	 * 查看自己发送给别人的动作
	 */
	public void sendHistory(HttpServletRequest request,
			HttpServletResponse response) {
		if (loginUser != null) {
			int totalCount = service.getActionRecordCount(" where from_id="
					+ loginUser.getId());
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "sendHistory.jsp?backTo="
					+ URLEncoder.encode(backTo);

			// 取得要显示的消息列表
			int start = pageIndex * NUMBER_PER_PAGE;
			int end = NUMBER_PER_PAGE;
			Vector recordList = service.getActionRecordList("from_id="
					+ loginUser.getId()
					+ " order by action_datetime desc limit " + start + ", "
					+ end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("backTo", backTo);
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("recordList", recordList);
		}
	}

	/**
	 * groupSendMsg.jsp 站内消息群发，进入页面 作者:macq 时间:2006-12-6
	 * 
	 * @param request
	 */
	public void groupSendMsg(HttpServletRequest request) {
		// 获取非在线用户
		Vector offlineFriendsList = UserInfoUtil
				.getUserOfflineFriendsList(loginUser.getId());
		// 获取在线用户
		Vector onlineFriendsList = UserInfoUtil
				.getUserOnlineFriendsList(loginUser.getId());
		request.setAttribute("onlineFriendsList", onlineFriendsList);
		request.setAttribute("offlineFriendsList", offlineFriendsList);
		session.setAttribute("groupSendMsg", "ok");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：删除用户接受信件
	 * @datetime:2007-6-21 3:18:05
	 * @return void
	 */
	public void delMessage(HttpServletRequest request) {
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String id = request.getParameter("id");
		int idLength = id.length();
		id = id.substring(0, idLength - 1);
		IMessageService messageService = ServiceFactory.createMessageService();
		//messageService.deleteMessage("to_user_id=" + loginUser.getId()
		//		+ " and id in(" + id + ")");
		messageService.updateMessage(" flag=1","to_user_id=" + loginUser.getId()
						+ " and id in(" + id + ")");
		request.setAttribute("tip", "删除成功");
		request.setAttribute("pageIndex", String.valueOf(pageIndex));
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：删除用户发送信件
	 * @datetime:2007-6-21 3:20:49
	 * @return void
	 */
	public void delSendMessage(HttpServletRequest request) {
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String id = request.getParameter("id");
		int idLength = id.length();
		id = id.substring(0, idLength - 1);
		IMessageService messageService = ServiceFactory.createMessageService();
		//messageService.deleteMessage("from_user_id=" + loginUser.getId()
		//		+ " and id in(" + id + ")");
		messageService.updateMessage(" flag=1","from_user_id=" + loginUser.getId()
				+ " and id in(" + id + ")");
		request.setAttribute("tip", "删除成功");
		request.setAttribute("pageIndex", String.valueOf(pageIndex));
	}

	/**
	 * groupSendMsgResult.jsp 站内消息群发结果页面，进入页面 作者:macq 时间:2006-12-6
	 * 
	 * @param request
	 */
	public void groupSendMsgResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 判断是否刷新
		
		// 获取发言内容
		String content = StringUtil.removeCtrlAsc(StringUtil.trim(request.getParameter("content")));
		if (content == null || content.trim().length() == 0) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "发言内容不能为空");
			return;
		}
		if (content.length() > 100) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "每条信息不能超过100字。");
			return;
		}
		
		if (session.getAttribute("groupSendMsg") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("groupSendMsg");
		// 判断群发时间(限制10分钟发送一次)
		if (session.getAttribute("groupSendMsgTime") != null) {
			long groupSendMsg = StringUtil.toLong((String) session
					.getAttribute("groupSendMsgTime"));
			if (groupSendMsg == -1) {
				result = "failure";
				tip = "参数错误";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			if (System.currentTimeMillis() - groupSendMsg < GROUP_SEND_MESSAGE_TIME) {
				result = "timeError";
				tip = "此功能每10分钟才可使用一次";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		}
		// 判断脏话
		Vector contentlist = ContentList.getContentList();
		if (contentlist != null) {
			String conName = null;
			for (int i = 0; i < contentlist.size(); i++) {
				conName = (String) contentlist.get(i);
				if (content.contains(conName)) {
					result = "failure";
					tip = "请注意您的发言内容";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				}
			}
		}
		// 获取接受信息的用户清单
		String onlineFriends = request.getParameter("onlineFriends");
		String offlineFriends = request.getParameter("offlineFriends");
		if (onlineFriends == null || offlineFriends == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String sendFriends = null;
		if (!onlineFriends.equals("")) {
			sendFriends = onlineFriends + ";" + offlineFriends;
		} else {
			sendFriends = offlineFriends;
		}
		if (sendFriends.replace(" ", "").equals("")) {
			result = "failure";
			tip = "请选择一位好友";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 拆分待发送用户id列表
		String[] sendFriendsSplit = sendFriends.split(";");
		// 获取列表长度
		int sendFriendsCount = sendFriendsSplit.length;
		// 获取用户状态信息
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		// 判断用户有足够的钱群发信息
		int sendGamePoint = sendFriendsCount * GROUP_SEND_MESSAGE_GAME_POINT;
		if (userStatus.getGamePoint() < sendGamePoint) {
			result = "failure";
			tip = "对不起,您的乐币不够!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int userId = 0;
		UserBean toUser = null;
		JCRoomContentBean jcRoomContent = null;
		NoticeBean notice = null;
		// macq_2007-1-22_获取所有加过发送者黑名单的用户id列表_start
		String sql = "SELECT user_id FROM user_blacklist where badguy_id="
				+ loginUser.getId();
		List userList = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (userList == null) {
			userList = new ArrayList();
		}
		HashMap userMap = new HashMap();
		for (int j = 0; j < userList.size(); j++) {
			userMap.put(userList.get(j), "");
		}
		// macq_2007-1-22_获取所有加过发送者黑名单的用户id列表_end
		// 循环给每个接收者发送聊天信息
		for (int i = 0; i < sendFriendsSplit.length; i++) {
			// 获取用户ID
			userId = StringUtil.toInt((String) sendFriendsSplit[i]);
			if (userId < 0) {
				continue;
			}
			// 获取用户信息
			toUser = UserInfoUtil.getUser(userId);
			if (toUser == null) {
				continue;
			}
			// macq_2007-11-22_判断消息接收者是否加发送者黑名单_start
			if (userMap.containsKey(new Integer(userId))) {
				continue;
			}
			// macq_2007-11-22_判断消息接收者是否加发送者黑名单_end
			// set聊天信息
			jcRoomContent = new JCRoomContentBean();
			jcRoomContent.setContent("(群发)" + content);
			jcRoomContent.setFromId(loginUser.getId());
			if (loginUser.getNickName() == null
					|| loginUser.getNickName().equals("v")
					|| loginUser.getNickName().length() == 0) {
				jcRoomContent.setFromNickName("乐客" + loginUser.getId());
			} else {
				jcRoomContent.setFromNickName(loginUser.getNickName());
			}
			jcRoomContent.setToId(userId);
			if (toUser.getNickName() == null
					|| toUser.getNickName().equals("v")
					|| toUser.getNickName().replace(" ", "").equals("")) {
				jcRoomContent.setToNickName("乐客" + loginUser.getId());
			} else {
				jcRoomContent.setToNickName(toUser.getNickName());
			}
			jcRoomContent.setAttach("");
			jcRoomContent.setIsPrivate(1);
			/*
			 * 第一个参数 fromUserId发言者 第二个参数 toUserId接收者 第三个参数
			 * fromUserRoomId发言者所在聊天室
			 */
			int[] secRoomId = JCRoomChatAction.getSecondRoomId(loginUser
					.getId(), 0, 0);
			jcRoomContent.setRoomId(secRoomId[0]);
			jcRoomContent.setSecRoomId(secRoomId[1]);
			jcRoomContent.setMark(0);
			// 发言
			chatService.addContent(jcRoomContent);
			// 加入消息系统
			toUser.notice[0]++;
/*
			notice = new NoticeBean();
			notice.setTitle(NoticeUtil.getChatNoticeTitle(loginUser
					.getNickName(), content));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(toUser.getId());
			notice.setHideUrl("/chat/hall.jsp");
			notice.setLink("/chat/hall.jsp?roomId=0");
			// macq_2007-5-16_增加聊天消息类型_start
			notice.setMark(NoticeBean.CHAT);
			// macq_2007-5-16_增加聊天消息类型_end
			NoticeUtil.addNotice(notice);
*/
		}
		// 扣除用户发送通知所需乐币
		UserInfoUtil.updateUserCash(loginUser.getId(), -sendGamePoint, 
				UserCashAction.OTHERS, "群发扣除用户乐币");
		session.setAttribute("groupSendMsgTime", System.currentTimeMillis()
				+ "");
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	// 得到登陆用户信息
	public UserBean getLoginUser() {
		return loginUser;
	}

	// 得到接收动作用户信息
	public UserBean getUser(String userId) {
		// UserBean us = service.getUser("id=" + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean us = UserInfoUtil.getUser(StringUtil.toInt(userId));
		return us;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 用户所有消息通知列表
	 * @datetime:2007-7-2 1:16:45
	 * @param request
	 * @return void
	 */
	public void userMessageList(HttpServletRequest request) {
		// 获取要显示的系统信息id列表
		Vector systemNoticeList = NewNoticeCacheUtil
				.getSystemNoticeReadedCountMap(loginUser);
		// 取得要显示的用户普通消息通知列表
		Vector userNoticeList = NewNoticeCacheUtil
				.getUserGeneralNoticeCountMap(loginUser.getId());
		Vector userMessageList = new Vector();
		userMessageList.addAll(systemNoticeList);
		userMessageList.addAll(userNoticeList);
		int totalCount = userMessageList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "userMessageList.jsp";
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List userMessageList1 = userMessageList.subList(startIndex, endIndex);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userMessageList", userMessageList1);
		session.setAttribute("userMessageCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户消息通知处理
	 * @datetime:2007-7-2 1:17:13
	 * @param request
	 * @return void
	 */
	public void userMessageResult(HttpServletRequest request) {
		if (session.getAttribute("userMessageCheck") == null) {
			request.setAttribute("result", "refrush");
			return;
		}
		session.removeAttribute("userMessageCheck");
		// 取得参数
		int noticeId = StringUtil.toInt(request.getParameter("noticeId"));
		if (noticeId == -1) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "参数错误");
			return;
		}
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		// mcq_2006-10-20_清空用户未读取通知消息计数器_start
		request.getSession().setAttribute(Constants.USER_NOTICE_COUNT,
				new Integer(0));
		// mcq_2006-10-20_清空用户未读取通知消息计数器_end
		// mcq_2006-10-18_从缓存中获取通知信息_start
		NoticeBean notice = NewNoticeCacheUtil.getNotice(noticeId);
		if (notice == null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "该通知不存在");
			return;
		}
		// mcq_2006-10-18_从缓存中获取通知信息_end
		INoticeService noticeService = ServiceFactory.createNoticeService();
		String condition = "id = " + noticeId;

		if (notice.getType() == NoticeBean.GENERAL_NOTICE) {
			noticeService.updateNotice("status = " + NoticeBean.READED,
					condition);
			// 处理用户消息缓存
			NewNoticeCacheUtil.updateUserNoticeById(notice.getUserId(), notice
					.getId());
		} else if (notice.getType() == NoticeBean.SYSTEM_NOTICE) {
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 记录用户已经读取系统通知
			String query = "insert into jc_notice_history set notice_id = "
					+ notice.getId() + ", user_id = " + loginUser.getId()
					+ ", read_datetime = now()";
			dbOp.executeUpdate(query);
			dbOp.release();
			// 处理系统消息缓存
			NewNoticeCacheUtil.updateSystemNoticeById(loginUser.getId(), notice
					.getId());
		}
		request.setAttribute("result", "success");
	}

	/**
	 * 
	 * @author macq
	 * @explain：删除用户所有普通通知
	 * @datetime:2007-7-2 3:32:11
	 * @param request
	 * @return void
	 */
	public void userMessageDel(HttpServletRequest request) {
		if (session.getAttribute("userMessageCheck") == null) {
			request.setAttribute("result", "refrush");
			return;
		}
		session.removeAttribute("userMessageCheck");
		// mcq_2006-10-20_清空用户未读取通知消息计数器_start
		request.getSession().setAttribute(Constants.USER_NOTICE_COUNT,
				new Integer(0));
		// mcq_2006-10-20_清空用户未读取通知消息计数器_end
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 记录用户已经读取系统通知
		String query = "UPDATE jc_notice SET status = " + NoticeBean.READED
				+ " where user_id=" + loginUser.getId() + " and type="
				+ NoticeBean.GENERAL_NOTICE + " and status="
				+ NoticeBean.NOT_READ;
		dbOp.executeUpdate(query);
		dbOp.release();
		// 清空用户普通消息缓存
		NewNoticeCacheUtil.flushUserNotice(loginUser.getId());
		request.setAttribute("result", "success");
	}

	/**
	 * 
	 * @author macq
	 * @explain：免打扰设置
	 * @datetime:2007-7-23 14:48:12
	 * @param request
	 * @return void
	 */
	public void noticeMark(HttpServletRequest request) {
		int mark = StringUtil.toInt(request.getParameter("mark"));
		if (mark < 0 || mark > 1) {
			mark = 0;
		}
		// UserSettingBean userSetting =
		// service.getUserSetting("user_id="+loginUser.getId());
		// if(userSetting ==null){
		// userSetting = new UserSettingBean();
		// userSetting.setUserId(loginUser.getId());
		// userSetting.setNoticeMark(mark);
		// userSetting.setPicMark(0);
		// userSetting.setBankPw("");
		// service.addUserSetting(userSetting);
		// }else{
		// service.updateUserSetting("notice_mark="+mark,"user_id="+loginUser.getId());
		// }
		// UserSettingBean userSetting = loginUser.getUserSetting();
		UserBean user = (UserBean) OnlineUtil.getOnlineBean(String
				.valueOf(loginUser.getId()));
		if (mark == 1) {
			loginUser.getUserSetting().setNoticeMark(1);
			user.getUserSetting().setNoticeMark(1);
			if(loginUser.status == null)
				loginUser.status = "免扰";
		} else {
			loginUser.getUserSetting().setNoticeMark(0);
			user.getUserSetting().setNoticeMark(0);
			if("免扰".equals(loginUser.status))
				loginUser.status = null;
		}
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：图片显示设置
	 * @datetime:2007-7-23 14:48:45
	 * @param request
	 * @return void
	 */
	public void picMark(HttpServletRequest request) {
		int mark = getParameterIntS("mark");
		if (mark < 0 || mark >= 30)
			return;
		
		
		UserSettingBean userSetting = service.getUserSetting("user_id="
				+ loginUser.getId());
		if (userSetting == null) {
			userSetting = new UserSettingBean();
			userSetting.setUserId(loginUser.getId());
			userSetting.toggleFlag(mark);
			service.addUserSetting(userSetting);
		} else {
			userSetting.toggleFlag(mark);
			service.updateUserSetting("pic_mark=" + userSetting.getPicMark(), "user_id="
					+ loginUser.getId());
		}

		loginUser.getUserSetting().toggleFlag(mark);
	}

}
