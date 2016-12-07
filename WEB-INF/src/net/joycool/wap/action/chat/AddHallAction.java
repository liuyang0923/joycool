/*
 * Created on 2006-6-26
 *
 */
package net.joycool.wap.action.chat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomContentCountBean;
import net.joycool.wap.bean.chat.RoomGrantBean;
import net.joycool.wap.bean.chat.RoomManagerBean;
import net.joycool.wap.bean.chat.RoomPaymentBean;
import net.joycool.wap.bean.chat.RoomUserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 该类实现聊天室的添加功能
 * 
 * 在session中做1-4步 step1 添加聊天室房间进入页 step2 添加聊天室房间名称 step3 添加聊天室房间大小及购买方式 step4
 * 添加聊天室房间图片 step5 添加聊天室房间受权情况 在step5在进数据库操作
 * 
 * @author zhangyi
 * 
 */
public class AddHallAction extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}

		DynaActionForm dynaForm = (DynaActionForm) form;

		HttpSession session = request.getSession();
		if (session.getAttribute("NewRoom") == null) {
			session.setAttribute("NewRoom", new JCRoomBean());
		}

		if (checkForm(request, response, dynaForm) != -1) {
			return mapping.findForward("addHallFailure"
					+ checkForm(request, response, dynaForm));
		}

		// 取得添加步骤
		String step = request.getParameter("step");
		if (step == null) {
			step = "1";
		}
		// 页面返回参数
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		// 当前session对象
		Object currentSessionRoom = session.getAttribute("NewRoom");

		if (("2").equals(step)) {
			// 进入第2步前保存第1步到seesion中
			((JCRoomBean) currentSessionRoom).setName(dynaForm
					.getString("name"));
			// currentSessionRoom.setAttribute(room);
		}
		if (("3").equals(step)) {
			// 进入第3步保存第2步到seesion中
			if (currentSessionRoom != null) {
				((JCRoomBean) currentSessionRoom).setMaxOnlineCount(Integer
						.parseInt(dynaForm.getString("maxOnlineCount")));
				((JCRoomBean) currentSessionRoom).setPayWay(Integer
						.parseInt(dynaForm.getString("payWay")));
				if (dynaForm.getString("payWay").equals("1")) {
					((JCRoomBean) currentSessionRoom).setPayDays(Integer
							.parseInt(dynaForm.getString("payDays")));
				}

				int maxOnlineCount = Integer.parseInt(dynaForm
						.getString("maxOnlineCount"));
				int payWay = Integer.parseInt(dynaForm.getString("payWay"));
				int payDays = 0;
				if (!("").equals(dynaForm.getString("payDays"))) {
					payDays = Integer.parseInt(dynaForm.getString("payDays"));
				}
				// 计算用户当前"设置的费用方式"所需钱数
				int currentTotalMoney = getPayWayMoney(payWay, maxOnlineCount,
						payDays);

				// 查找用户是否有足够的钱建立房间
				IUserService userService = ServiceFactory.createUserService();
				// UserStatusBean userStatusBean = userService
				// .getUserStatus("user_id = " + loginUser.getId());
				// zhul_modify us _2006-08-14_获取用户状态信息 start
				UserStatusBean userStatusBean = UserInfoUtil
						.getUserStatus(loginUser.getId());
				// zhul_modify us _2006-08-14_获取用户状态信息 end
				if (userStatusBean != null) {
					int userMoney = userStatusBean.getGamePoint();
					if (userMoney < currentTotalMoney) {
						// 用户钱小于设置后所需的钱数
						request.setAttribute("message", "您的乐币不够，请缩小房间或赶紧赚钱");
						return mapping.findForward("addHallFailure2");
					}

				} else {
					return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
				}

			} else {
				// 系统session出错
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
		}
		if (("4").equals(step)) {
			// 进入第4步保存第3步到seesion中
			if (currentSessionRoom != null) {
				// String thumbnail = null;
				// if (!"".equals(dynaForm.getString("thumbnail"))) {
				String thumbnail = dynaForm.getString("thumbnail");
				// }
				((JCRoomBean) currentSessionRoom).setThumbnail(thumbnail);
			} else {
				// 系统session出错
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
		}

		if (("5").equals(step)) {

			// query相关表(user_status,jc_room)
			// insert相关表(jc_room,jc_room_payment,jc_room_manager,jc_room_user,jc_room_grant)
			// update相关表(user_status)
			// 构造页面显示对象

			boolean result = false;
			boolean resultPayment = false;
			boolean resultOther = false;

			if (currentSessionRoom != null) {

				// 为insert构造room对象
				JCRoomBean currentRoom = (JCRoomBean) currentSessionRoom;
				currentRoom.setCreatorId(loginUser.getId());
				currentRoom.setGrantMode(Integer.parseInt(dynaForm
						.getString("grantMode")));

				// insert数据 jc_room table start
				IChatService chatService = ServiceFactory.createChatService();
				result = chatService.addJCRoom(currentRoom);
				// insert数据 jc_room table end

				if (result) {
					// 根据creator_id&name取得刚建立的对象(room)
					// query data obj
					String condition = "name='" + currentRoom.getName()
							+ "' and creator_id=" + currentRoom.getCreatorId();
					JCRoomBean foundRoom = chatService.getJCRoomSpec(condition);

					// 添加jc_room_payment表
					RoomPaymentBean roomPayment = new RoomPaymentBean();
					roomPayment.setUserId(foundRoom.getCreatorId());
					roomPayment.setRoomId(foundRoom.getId());
					int payType = foundRoom.getPayWay();

					// 计算用户"设置的费用方式"所需钱数
					int currentTotalMoney = getPayWayMoney(payType, foundRoom
							.getMaxOnlineCount(), foundRoom.getPayDays());

					roomPayment.setPayType(payType);
					roomPayment.setMoney(currentTotalMoney);
					roomPayment.setRemark("");
					resultPayment = chatService.addJCRoomPayment(roomPayment);

					if (resultPayment) {
						// 添加 jc_room_manager 表
						RoomManagerBean roomManager = new RoomManagerBean();
						roomManager.setRoomId(foundRoom.getId());
						roomManager.setUserId(foundRoom.getCreatorId());
						roomManager.setMark(1);
						resultOther = chatService.addJCRoomManager(roomManager);

						// if (foundRoom.getGrantMode() == 1) {
						// 添加 jc_room_user 表
						RoomUserBean roomUser = new RoomUserBean();
						roomUser.setUserId(loginUser.getId());
						roomUser.setRoomId(foundRoom.getId());
						roomUser.setManagerId(loginUser.getId());
						roomUser.setStatus(1);

						resultOther = chatService.addJCRoomUser(roomUser);

						// 添加 jc_room_grant 表
						RoomGrantBean roomGrant = new RoomGrantBean();
						roomGrant.setUserId(loginUser.getId());
						roomGrant.setRoomId(foundRoom.getId());
						roomGrant.setManagerId(foundRoom.getCreatorId());
						roomGrant.setGrantType(2);
						resultOther = chatService.addJCRoomGrant(roomGrant);

						// 添加 jc_room_content_count 表
						JCRoomContentCountBean roomContentCount = new JCRoomContentCountBean();
						roomContentCount.setRoomId(foundRoom.getId());
						chatService.addJCRoomContentCount(roomContentCount);
						// }

						if (!resultOther) {
							return mapping
									.findForward(Constants.SYSTEM_FAILURE_KEY);
						} else {
							// 表添加完成
							// 开始扣除用户钱从user_status
							IUserService userService = ServiceFactory
									.createUserService();
							// userService.updateUserStatus(
							// "game_point=game_point-" + currentTotalMoney,
							// "user_id=" + loginUser.getId());
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point=game_point-"
											+ currentTotalMoney, "user_id="
											+ loginUser.getId(), loginUser
											.getId(),UserCashAction.OTHERS, "买聊天室扣钱"+currentTotalMoney+"乐币");
							// zhul_2006-08-11 modify userstatus end

							// 更改session中用户的乐币数
							// loginUser.getUs().setGamePoint(
							// loginUser.getUs().getGamePoint()
							// - currentTotalMoney);

							// add by zhangyi 2006-07-24 for stat user money
							// history start
							MoneyAction.addMoneyFlowRecord(Constants.OTHER,
									currentTotalMoney, Constants.SUBTRATION,
									loginUser.getId());
							// add by zhangyi 2006-07-24 for stat user money
							// history end
						}
					}
					// 供添加成功后页面显示
					session.setAttribute("foundRoom", foundRoom);
					// 释放掉session，不能后退操作再添加
					session.removeAttribute("NewRoom");
					//response.sendRedirect("addHall.jsp?step=5");
					BaseAction.sendRedirect("/chat/addHall.jsp?step=5", response);
					return null;

				} else {
					// 系统database出错
					return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
				}
			} else {
				// 系统session出错
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}

		}

		request.setAttribute("step", step);
		request.setAttribute("backTo", backTo);

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);

	}

	/**
	 * 检测输入参数，如果不合法返回原输入页
	 * 
	 * @param request
	 * @param dynaForm
	 * @return
	 */
	private int checkForm(HttpServletRequest request,
			HttpServletResponse response, DynaActionForm dynaForm) {

		IChatService chatService = ServiceFactory.createChatService();
		int rsFlag = -1;
		String step = request.getParameter("step");
		if (step == null) {
			step = "1";
		}
		StringBuffer message = new StringBuffer();

		if (("2").equals(step)) {
			if (isEmpty(dynaForm.getString("name"))) {
				message.append("聊天室名称不能为空!<br/>");
				rsFlag = 1;
			}
			// 看房间名是否已存在
			int count = chatService.getJCRoomCount("name ='"
					+ StringUtil.toSql(dynaForm.getString("name")) + "'");
			if (count > 0) {
				message.append("聊天室名称已经存在!<br/>");
				rsFlag = 1;
			}
		} else if (("3").equals(step)) {
			if (isEmpty(dynaForm.getString("maxOnlineCount"))) {
				message.append("房间大小不能为空!<br/>");
				rsFlag = 2;
			} else if (StringUtil.toInt(dynaForm.getString("maxOnlineCount")) < 5) {
				message.append("房间大小应不小于5!<br/>");
				rsFlag = 2;
			} else if (StringUtil.toInt(dynaForm.getString("maxOnlineCount")) > 200) {
				message.append("房间大小不能超过200!<br/>");
				rsFlag = 2;
			}
			if (isEmpty(dynaForm.getString("payWay"))) {
				message.append("付费方式不能为空!<br/>");
				rsFlag = 2;
			} else if (dynaForm.getString("payWay").equals("1")) {
				if (isEmpty(dynaForm.getString("payDays"))) {
					message.append("租用时间不能为空!<br/>");
					rsFlag = 2;
				} else if (StringUtil.toInt(dynaForm.getString("payDays")) < 5) {
					message.append("租用时间应不小于5!<br/>");
					rsFlag = 2;
				}
			}
		} else if (("5").equals(step)) {
			if (isEmpty(dynaForm.getString("grantMode"))) {
				message.append("授权模式不能为空!<br/>");
				rsFlag = 4;
			}
			// 不允许后退操作
			if (((JCRoomBean) request.getSession().getAttribute("NewRoom"))
					.getName() == null) {
				// 添加已结束

				message.append("房间<b>" + dynaForm.getString("name")
						+ "</b>已创建成功，如果还想创建，请重新从头开始吧！");
				message.append(" <a href=\""
						+ ("/chat/editHallLink.jsp?roomId="
								+ ((JCRoomBean) request.getSession()
										.getAttribute("foundRoom")).getId()
								+ "&amp;backTo=addHall.jsp"
								+ "\">我想修改</a><br/>"));
				rsFlag = 1;
			}

			/*
			 * // 从其它页进入第5步，不做任何操作 int count = chatService.getJCRoomCount("name
			 * ='" + dynaForm.getString("name") + "'"); if (count > 0) {
			 * //message.append("该聊天室已经存在！<br/>"); rsFlag = 5; }
			 */
		}

		if (message.length() > 0) {
			request.setAttribute("message", message.toString());
		}
		return rsFlag;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	private int getPayWayMoney(int payWay, int maxOnlineCount, int payDays) {
		int totalMoney = 0;
		if (payWay == 1) {
			// 租用;
			totalMoney = maxOnlineCount * Constants.CHAT_LEND_MONEY * payDays;
		} else if (payWay == 0) {
			// 购买
			totalMoney = maxOnlineCount * Constants.CHAT_BUY_MONEY;
		}
		return totalMoney;
	}
}
