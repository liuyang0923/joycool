/*
 * Created on 2006-6-26
 *
 */
package net.joycool.wap.action.chat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.RoomPaymentBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RoomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 该类实现聊天室的修改功能
 * 
 * 只有用户现在所拥有钱大于当前"设置的费用方式"所需钱数 和 原有"设置的费用方式"剩余钱数之差,才允许修改; 如果当前"设置的费用方式"所需钱数 小于
 * 原有"设置的费用方式"剩余钱数, 进行修改房间，但不给用户退钱（update user_status）,也不给jc_room_paynet添记录;
 * 如果当前"设置的费用方式"所需钱数 大于 原有"设置的费用方式"剩余钱数, 进行修改房间，给用户减钱（update user_status）,
 * 给jc_room_paynet添记录;
 * 
 * editType1 修改聊天室房间名称 editType2 修改聊天室房间大小及购买方式 editType3 修改聊天室房间图片 editType4
 * 修改聊天室房间受权情况
 * 
 * @author zhangyi
 */
public class EditHallAction extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}

		DynaActionForm dynaForm = (DynaActionForm) form;

		// 页面返回参数
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}

		IChatService chatService = ServiceFactory.createChatService();

		// 准备页面返回参数
		String update = request.getParameter("update");
		String editType = request.getParameter("editType");
		int roomId = StringUtil.toInt(request.getParameter("roomId"));
		JCRoomBean room = room = chatService.getJCRoomSpec("id = " + roomId);

		// 设置页面返回参数
		request.setAttribute("update", update);
		request.setAttribute("editType", editType);
		request.setAttribute("roomId", String.valueOf(roomId));
		request.getSession().setAttribute("Room", room);

		if (update == null) {
			// 进入编辑页
			return mapping.findForward("enterEdit");
		} else {
			// 开始编辑
			// 参数验证
			if (checkForm(request, dynaForm, room.getName()) != -1) {
				return mapping.findForward("enterEdit");
			}

			boolean result = false;
			String condition = "id = " + roomId;
			if (("1").equals(editType)) {
				// 更改房间名称
				String name = dynaForm.getString("name");
				String set = "name = '" + StringUtil.toSql(name) + "'";
				result = chatService.updateJCRoom(set, condition);

			}
			if (("2").equals(editType)) {
				// 更改房间面积及付款方式

				// query相关表(user_status,jc_room)
				// insert相关表(jc_room_payment)
				// update相关表(jc_room,user_status)

				int maxOnlineCount = Integer.parseInt(dynaForm
						.getString("maxOnlineCount"));
				int payWay = Integer.parseInt(dynaForm.getString("payWay"));
				int payDay = 0;
				if (!("").equals(dynaForm.getString("payDays"))) {
					payDay = Integer.parseInt(dynaForm.getString("payDays"));
				}

				// 计算用户当前"设置的费用方式"所需钱数
				int currentTotalMoney = getPayWayMoney(payWay, maxOnlineCount,
						payDay);
				// 计算用户"原有设置的费用方式"剩余钱数
				int oldTotalMoney = getPayWayMoney(room.getPayWay(), room
						.getMaxOnlineCount(), room.getPayDays());
				// 计算当前"设置的费用方式"所需钱数 和 原有"设置的费用方式"剩余钱数之差
				// 既设置后还需的钱数
				int kaoNum = currentTotalMoney - oldTotalMoney;

				// 查找用户是否有足够的钱更改房间
				IUserService userService = ServiceFactory.createUserService();
				// UserStatusBean userStatusBean = userService
				// .getUserStatus("user_id = " + loginUser.getId());
				// zhul_modify us _2006-08-14_获取用户状态信息 start
				UserStatusBean userStatusBean = UserInfoUtil
						.getUserStatus(loginUser.getId());
				// zhul_modify us _2006-08-14_获取用户状态信息 end
				if (userStatusBean != null) {
					int userMoney = userStatusBean.getGamePoint();
					if (userMoney < kaoNum) {

						// 用户钱小于设置后还需的钱数
						request.setAttribute("message", "您的乐币不够，请缩小房间或赶紧赚钱 ");
						return mapping.findForward("enterEdit");
					}

				} else {
					return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
				}
				// 通过后开始更新JCRoom
				// 设置修改jc_room参数
				String set = "";
				if (dynaForm.getString("payWay").equals("1")) {
					// 租用
					int payDays = Integer.parseInt(dynaForm
							.getString("payDays"));
					set = "max_online_count = " + maxOnlineCount
							+ " , pay_way = " + payWay
							+ " , expire_datetime=DATE_ADD(now(), INTERVAL "
							+ payDays + " DAY) ";
				} else {
					// 购买
					set = "max_online_count = " + maxOnlineCount
							+ " , pay_way = " + payWay
							+ " , expire_datetime=DATE_ADD(now(), INTERVAL "
							+ Constants.EXPIRE_DAYS + " DAY) ";
				}
				result = chatService.updateJCRoom(set, condition);

				if (result) {
					// 更新成功后

					// 开始扣除用户钱从user_status

					if (kaoNum > 0) {
						// 如果当前"设置的费用方式"所需钱数 > 原有"设置的费用方式"剩余钱数
						// 扣去差值,添加JCRoomPayment表

						userService = ServiceFactory.createUserService();
						// 扣钱（user_status）
						// userService.updateUserStatus("game_point=game_point-"
						// + kaoNum, "user_id=" + loginUser.getId());
						// zhul_2006-08-11 modify userstatus start
						UserInfoUtil
								.updateUserStatus("game_point=game_point-"
										+ kaoNum, "user_id="
										+ loginUser.getId(), loginUser.getId(),UserCashAction.OTHERS,
										"editHall :如果当前设置的费用方式所需钱数 > 原有设置的费用方式剩余钱数,扣去差值"+kaoNum+"乐币");
						// zhul_2006-08-11 modify userstatus end

						// 更改session中用户的乐币数
						// loginUser.getUs().setGamePoint(
						// loginUser.getUs().getGamePoint() - kaoNum);

						// add by zhangyi 2006-07-24 for stat user money history
						// start
						MoneyAction.addMoneyFlowRecord(Constants.OTHER, kaoNum,
								Constants.SUBTRATION, loginUser.getId());
						// add by zhangyi 2006-07-24 for stat user money history
						// end

						// 添加jc_room_payment表
						RoomPaymentBean roomPayment = new RoomPaymentBean();
						roomPayment.setUserId(loginUser.getId());
						roomPayment.setRoomId(roomId);
						/*
						 * int payType = 2; if (payWay == 0) { // 购买扩容 payType =
						 * 2; } else if (payWay == 1){ // 租赁扩容 payType = 3; }
						 */
						// 用更改后的付款方式给聊天室购买、冲值记录表
						roomPayment.setPayType(payWay);
						roomPayment.setMoney(kaoNum);
						roomPayment.setRemark("");
						// 添加JCRoomPayment
						chatService.addJCRoomPayment(roomPayment);
					}
				}
			}
			if (("3").equals(editType)) {
				// 更改房间图片
				String thumbnail = dynaForm.getString("thumbnail");
				String set = "thumbnail = '" + StringUtil.toSql(thumbnail) + "'";
				result = chatService.updateJCRoom(set, condition);

			}
			if (("4").equals(editType)) {
				// 更改房间授权模式
				String name = dynaForm.getString("grantMode");
				String set = "grant_mode = " + StringUtil.toSql(name) + "";
				result = chatService.updateJCRoom(set, condition);

			}

			if (result) {
				// 更改成功后，供更改成功后页面显示
				request.setAttribute("editType", "5");
				request.getSession().setAttribute("NewRoom",
						chatService.getJCRoomSpec("id = " + roomId));
				// mcq_2006_6_30_更改房间信息_start
				RoomUtil.addChangedRoom(roomId);
				// mcq_2006_6_30_更改房间信息_end
				return mapping.findForward("enterEdit");
			} else {
				// 系统database出错
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
		}
	}

	/**
	 * 检测输入参数，如果不合法返回原输入页
	 * 
	 * @param request
	 * @param dynaForm
	 * @param roomName
	 * @return
	 */
	private int checkForm(HttpServletRequest request, DynaActionForm dynaForm,
			String roomName) {

		IChatService chatService = ServiceFactory.createChatService();
		int rsFlag = -1;
		String editType = request.getParameter("editType");
		if (editType == null) {
			editType = "1";
		}
		StringBuffer message = new StringBuffer();

		if (("1").equals(editType)) {
			if (isEmpty(dynaForm.getString("name"))) {
				message.append("聊天室名称不能为空!<br/>");
				rsFlag = 1;
			}
			// 看房间名是否已存在
			if (!roomName.equals(dynaForm.getString("name"))) {
				int count = chatService.getJCRoomCount("name ='"
						+ dynaForm.getString("name") + "'");
				if (count > 0) {
					message.append("聊天室名称已经存在!<br/>");
					rsFlag = 1;
				}
			}

		} else if (("2").equals(editType)) {
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
		} else if (("4").equals(editType)) {
			if (isEmpty(dynaForm.getString("grantMode"))) {
				message.append("授权模式不能为空!<br/>");
				rsFlag = 4;
			}
			// 防止重复提交
			// JCRoomBean currentRoom =
			// (JCRoomBean)request.getSession().getAttribute("NewRoom");
			/*
			 * int count = chatService.getJCRoomCount("name ='" +
			 * dynaForm.getString("name") + "'"); if (count > 0) {
			 * message.append("聊天室名称已经存在!<br/>"); rsFlag = 1; }
			 */
		}

		if (message.length() > 0) {
			request.setAttribute("message", message.toString());
		}
		return rsFlag;
	}

	private boolean isEmpty(String str) {
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
