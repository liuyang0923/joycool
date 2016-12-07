/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class ViewUserInfoAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IUserService service = ServiceFactory.createUserService();
		UserBean loginUser = getLoginUser(request);
		// 取得参数
		//macq_2007-4-13_增加用户id判断_start;
		int userId = StringUtil.toInt(request.getParameter("userId"));
		if (userId ==-1) {
			if(loginUser != null)
				userId = loginUser.getId();
			else
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		//macq_2007-4-13_增加用户id判断_start;
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
//		UserBean user = service.getUser("id = " + userId);
		//zhul 2006-10-12_优化用户信息查询
		UserBean user = UserInfoUtil.getUser(userId);
		if (user == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		
		if(loginUser == null) {
			return new ActionForward("/user/guestViewInfo.jsp?userId=" + userId);
		}
		// mcq_4_查看某人信息时,显示等级, 投降,经验 时间 2006-6-7
		// fanys2006-08-11
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(userId);
		// UserStatusBean userStatus=service.getUserStatus("user_id="+userId);
		if (userStatus == null || loginUser == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		user.setUs(userStatus);
		//zhul 2006-10-17 优化好友判断
		ArrayList userFriends=UserInfoUtil.getUserFriends(loginUser.getId());
		// mcq_end
		if (loginUser != null) {
			//if (service.isUserFriend(loginUser.getId(), user.getId())) {//zhul 2006-10-17 优化好友判断
			if (userFriends.contains(user.getId()+"")) {
				request.setAttribute("isFriend", new Integer(1));
			} else {
				request.setAttribute("isFriend", new Integer(0));
			}
			if (service.isUserBadGuy(loginUser.getId(), user.getId())) {
				request.setAttribute("isBadGuy", new Integer(1));
			} else {
				request.setAttribute("isBadGuy", new Integer(0));
			}
		}
		// fanys 2006-08-22
//		Vector rankList = LoadResource.getLastWeekInivteRank();
//
//		RoomInviteRankBean rankBean = null;
//		for (int i = 0; i < rankList.size(); i++) {
//			rankBean = (RoomInviteRankBean) rankList.get(i);
//			if (userId == rankBean.getUserId()) {
//				request.setAttribute("inviteImage", rankBean.getCrown()
//						.getImage());
//			}
//		}

		request.setAttribute("user", user);
		request.setAttribute("backTo", backTo);
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
