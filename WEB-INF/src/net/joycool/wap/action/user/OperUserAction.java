/*
 * Created on 2006-4-5
 *
 */
package net.joycool.wap.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.chat.JCRoomChatAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.wgame.WGameAction;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserMoneyLogBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class OperUserAction {
	public static void operUser(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserBean loginUser = (UserBean) session
				.getAttribute(Constants.LOGIN_USER_KEY);
		if (loginUser == null) {
			return;
		}

		// 取得参数
		String action = request.getParameter("action");
		int toUserId = StringUtil.toInt(request.getParameter("toUserId"));
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}

		// 邀请对方聊天
		if ("inviteChat".equals(action)) {
			if (toUserId == -1) {
				return;
			} else {
				// IMessageService messageService = ServiceFactory
				// .createMessageService();
				//
				// MessageBean message = new MessageBean();
				// message.setFromUserId(loginUser.getId());
				// message.setToUserId(toUserId);
				// message.setContent("<a href=\""
				// + (
				// + "/chat/hall.jsp") + "\">快来聊天室找我吧</a>");
				// message.setMark(0);
				//
				// if (!messageService.addMessage(message)) {
				// return;
				// }
				IUserService userService = ServiceFactory.createUserService();
				IChatService chatService = ServiceFactory.createChatService();
				// UserBean toUser = userService.getUser("id = " + toUserId);
				// zhul 2006-10-12_优化用户信息查询
				UserBean toUser = UserInfoUtil.getUser(toUserId);
				if (toUser == null) {
					return;
				}

				// 增加一条聊天发言
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setContent("你好啊，我们聊聊吧");
				jcRoomContent.setFromId(loginUser.getId());
				jcRoomContent.setFromNickName(loginUser.getNickName());
				jcRoomContent.setToId(toUser.getId());
				jcRoomContent.setToNickName(toUser.getNickName());
				jcRoomContent.setAttach("");
				// zhul_2006-08-22 add for chat model start
				int[] secRoomId = JCRoomChatAction.getSecondRoomId(loginUser
						.getId(), toUser.getId(), jcRoomContent.getRoomId());
				if (secRoomId[0] == -1 && secRoomId[1] == -1) {
					jcRoomContent.setIsPrivate(1);
					request.setAttribute("privateNotice", "提示："
							+ StringUtil.toWml(toUser.getNickName())
							+ "不在当前聊天室，您的发言转为私聊。");
				}
				jcRoomContent.setRoomId(secRoomId[0]);
				jcRoomContent.setSecRoomId(secRoomId[1]);
				// zhul_2006-08-22 add for chat model end
				// liuyi 2006-09-16 聊天室加缓存 start
				jcRoomContent.setMark(0);
				// chatService.addContent(jcRoomContent.getFromId() + ","
				// + jcRoomContent.getToId() + ",'"
				// + jcRoomContent.getFromNickName() + "','"
				// + jcRoomContent.getToNickName() + "','"
				// + jcRoomContent.getContent() + "','"
				// + jcRoomContent.getAttach() + "',now(),"
				// + jcRoomContent.getIsPrivate() + ","
				// + jcRoomContent.getRoomId() + ","
				// + jcRoomContent.getSecRoomId() + ",0");
				chatService.addContent(jcRoomContent);
				// liuyi 2006-09-16 聊天室加缓存 end
				// 清空聊天室在map中的记录
				// RoomCacheUtil.flushRoomContentId(jcRoomContent.getRoomId());
				// RoomCacheUtil.flushRoomContentId(jcRoomContent.getSecRoomId());
				// 清空聊天室在map中的记录

				// 加入消息系统
				NoticeBean notice = new NoticeBean();
				notice.setTitle(NoticeUtil.getInviteNoticeTitle(loginUser
						.getNickName()));
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setUserId(toUserId);
				notice.setHideUrl("/chat/hall.jsp");
				notice.setLink("/chat/hall.jsp");
				//macq_2007-5-16_增加聊天消息类型_start
				notice.setMark(NoticeBean.CHAT);
				//macq_2007-5-16_增加聊天消息类型_end
				NoticeUtil.addNotice(notice);

				request.setAttribute("action", action);
				request.setAttribute("goTo", response
						.encodeURL("/chat/hall.jsp"));
			}

		}
		// 邀请对方游戏
		else if ("inviteGame".equals(action)) {
			IUserService userService = ServiceFactory.createUserService();
			// 进入页
			if (loginUser.getId() != 431 && loginUser.getId() != 519610 && loginUser.getId() != 914727 &&(userService.isUserBadGuy(toUserId, loginUser
					.getId())))
			{		
			request.setAttribute("action", action);
			request.setAttribute("result", "failure");
			request.setAttribute("backTo", backTo);
			request.setAttribute("toUserId", "" + toUserId);
			request.setAttribute("tip", "你在对方的黑名单里，不能邀请他PK!");
			return;
				
		}
         else if (request.getParameter("game") == null) {
				request.setAttribute("action", action);
				request.setAttribute("toUserId", "" + toUserId);
				request.setAttribute("backTo", backTo);
			}

			// 处理页
			else {

				String game = request.getParameter("game");
				String content = null;
				String goTo = null;
				// 五子棋
				if ("hallGobang".equals(game)) {
					goTo = ("/wgamehall/gobang/index.jsp");
					content = "<a href=\"" + goTo + "\">五子棋K死你，敢不敢来，我等着你</a>";
				}
				// 黑白棋
				else if ("hallOthello".equals(game)) {
					goTo = ("/wgamehall/othello/index.jsp");
					content = "<a href=\"" + goTo + "\">你会玩黑白棋吗？笨蛋，来找我呀</a>";
				}
				// 点球决战
				else if ("hallFootball".equals(game)) {
					goTo = ("/wgamehall/football/index.jsp");
					content = "<a href=\"" + goTo + "\">点球决战好好玩的哦，快来吧我等着你</a>";
				}
				// PK射门
				else if ("pkFootball".equals(game)) {
					goTo = ("/wgamepk/football/index.jsp");
					content = "<a href=\"" + goTo + "\">快来，我射死你(是射门，hoho)</a>";
				}
				// PK三公
				else if ("pk3gong".equals(game)) {
					goTo = ("/wgamepk/3gong/index.jsp");
					content = "<a href=\"" + goTo
							+ "\">三公好像是一个广东的游戏，来玩玩吗，我等你</a>";
				}
				// PK掷骰子
				else if ("pkDice".equals(game)) {
					goTo = ("/wgamepk/dice/index.jsp");
					content = "<a href=\"" + goTo + "\">简简单单掷骰子比大小，快来找我</a>";
				}
				// PK剪刀石头布
				else if ("pkJsb".equals(game)) {
					goTo = ("/wgamepk/jsb/index.jsp");
					content = "<a href=\"" + goTo
							+ "\">好简单的剪刀石头布，这你都不敢跟我玩？来呀</a>";
				}

				IMessageService messageService = ServiceFactory
						.createMessageService();

				MessageBean message = new MessageBean();
				message.setFromUserId(loginUser.getId());
				message.setToUserId(toUserId);
				message.setContent(content);
				message.setMark(0);

				if (!messageService.addMessage(message)) {
					return;
				}

				request.setAttribute("action", action);
				request.setAttribute("result", "success");
				request.setAttribute("goTo", goTo);
			}
		}
		// 送乐币给对方
		else if ("sendMoney".equals(action)) {

			WGameAction wa = new WGameAction();
			IUserService userService = ServiceFactory.createUserService();
			// fanys2006-08-11
			
			// UserStatusBean us = wa.getUserStatus(loginUser.getId());
			// 进入页
			if (request.getParameter("money") == null) {
				UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
				request.setAttribute("us", us);
				request.setAttribute("action", action);
				request.setAttribute("toUserId", "" + toUserId);
				request.setAttribute("backTo", backTo);

			}
			// 处理页
			else {
				int money = StringUtil.toInt(request.getParameter("money"));
				// Timestamp date=(Timestamp
				// )session.getAttribute(loginUser.getId()+" "+toUserId);
				String lasttime = (String) session.getAttribute(loginUser
						.getId()
						+ " " + toUserId);
				long timeinfo = 0;
				if (lasttime != null) {
					Long time = Long.valueOf(lasttime);
					timeinfo = time.longValue();

				}

				long currentTime = System.currentTimeMillis() / 1000;
				request.setAttribute("action", action);
				request.setAttribute("backTo", backTo);
				if ((session.getAttribute(loginUser.getId() + " " + toUserId) != null)
						&& (currentTime - timeinfo < 180)) {
					request.setAttribute("result", "failure");
					request.setAttribute("toUserId", "" + toUserId);
					request.setAttribute("tip", "给同一个用户送乐币必须间隔3分钟");
					return;
				} else if (toUserId == -1 || money < 0 || money == 0) {
					request.setAttribute("result", "failure");
					request.setAttribute("toUserId", "" + toUserId);
					request.setAttribute("tip", "乐币数要多于0个");
					return;
				}
				if (loginUser.getId() != 431
						&& loginUser.getId() != 519610
						&& loginUser.getId() != 914727
						&& (userService.isUserBadGuy(toUserId, loginUser
								.getId()))) {
					request.setAttribute("result", "failure");
					request.setAttribute("toUserId", "" + toUserId);
					request.setAttribute("tip", "你在对方的黑名单里，不能给他送乐币!");
					return;
				} 
				UserStatusBean us;
				synchronized(loginUser.getLock()) {
					us = UserInfoUtil.getUserStatus(loginUser.getId());
					if (money > us.getGamePoint()) {
						request.setAttribute("result", "failure");
						request.setAttribute("toUserId", "" + toUserId);
						request.setAttribute("tip", "您的乐币不够");
						return;
					}
					// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_start
					UserBean onlineUser = (UserBean) OnlineUtil
							.getOnlineBean(toUserId + "");
					if (onlineUser != null) {
						if (onlineUser.noticeMark()) {
							request.setAttribute("action", action);
							request.setAttribute("result", "failure");
							request.setAttribute("backTo", backTo);
							request.setAttribute("toUserId", "" + toUserId);
							request
									.setAttribute("tip",
											"对方设置自己状态为免打扰，您不能送他乐币！");
							return;
						}
					}
					// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_end
					// fanys2006-08-11
					// UserStatusBean tus =
					// UserInfoUtil.getUserStatus(toUserId);
					// UserStatusBean tus = wa.getUserStatus(toUserId);
					UserInfoUtil.updateUserCash(loginUser.getId(), -money, UserCashAction.PRESENT, "送给别人("+ toUserId + ")"
									+ StringUtil.bigNumberFormat(money) + "乐币");
					UserInfoUtil.updateUserCash(toUserId, money,
							UserCashAction.PRESENT, "接收别人赠送乐币" + money);

				}

				String info = loginUser.getId() + " " + toUserId;
				session.removeAttribute(info);
				session.setAttribute(info, String.valueOf(currentTime));
				
				UserMoneyLogBean userMoneyLog = new UserMoneyLogBean();
				userMoneyLog.setFromId(loginUser.getId());
				userMoneyLog.setToId(toUserId);
				userMoneyLog.setMoney(money);
				ServiceFactory.createUserService().addMoneyLog(userMoneyLog);
				//macq_2007-6-19_增加赠送乐币日志_end
				NoticeBean notice = new NoticeBean();
				notice.setUserId(toUserId);
				notice.setTitle(loginUser.getNickName() + "送给你" + money
						+ "个乐币");
				notice.setContent("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setHideUrl("/chat/hall.jsp");
				notice.setLink("/chat/post.jsp?toUserId=" + loginUser.getId());
				ServiceFactory.createNoticeService().addNotice(notice);
				// mcq_2006-9-6_更改送乐币通知方式(信件改为消息)_end
				us = UserInfoUtil.getUserStatus(loginUser.getId());
				request.setAttribute("us", us);
				request.setAttribute("toUserId", "" + toUserId);
				request.setAttribute("result", "success");
			}
		}
	}
}
