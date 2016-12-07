/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.home.HomeAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.cache.util.HomeCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class OperFriendAction extends BaseAction {
	static IUserService service = ServiceFactory.createUserService();

	static IHomeService homeService = ServiceFactory.createHomeService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}

		int friendId = StringUtil.toInt(request.getParameter("friendId"));
		request.setAttribute("friendId", String.valueOf(friendId));
		// 增加好友
		if (request.getParameter("add") != null) {
			// zhul 2006-10-12 不能加自己为好友_start
			if (loginUser.getId() == friendId) {
				request.setAttribute("myself", "y");
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			// zhul 2006-10-12 不能加自己为好友_end
			// mcq_2006-6-20_判断是否已经添加过该用户_start
			// zhul 2006-10-13 优化好友判断
			ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser
					.getId());
			if (userFriends.size() >= 300) {
				request.setAttribute("max", "");
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			if (userFriends.contains(friendId + "")) {
				request.setAttribute("exist", "exist");
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			UserBean friendUser = UserInfoUtil.getUser(friendId);
			if (friendUser == null) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			UserStatusBean us2 = friendUser.getUs2();
			if(us2 == null || System.currentTimeMillis() - us2.getLastLoginTime2() > DateUtil.MS_IN_DAY * 30) {	// 超过30天没登陆的禁止加好友
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			// mcq_2006-6-20_判断是否已经添加过该用户_end
			if (!service.addFriend(loginUser.getId(), friendId)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			
			

			// zhul 2006-09-25添加好友的同时，如果双方有家园，将其加为我的邻居 start
			if (loginUser.getHome() == 1) {
				// macq_2006-12-20_增加家园的缓存_start
				HomeUserBean homeUser = HomeCacheUtil.getHomeCache(friendId);
				// HomeUserBean homeUser = homeService.getHomeUser("user_id="
				// + friendId);
				// macq_2006-12-20_增加家园的缓存_end
				if (homeUser != null) {
					HomeAction home = new HomeAction(request);
					home.addNeighbor(friendId);
				}
			}
			// zhul 2006-09-25添加好友的同时，如果对方有家园，将其加为我的邻居 end
			if (!service.deleteBadGuy(loginUser.getId(), friendId)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			
			
			ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_BE_FRIEND, "%1把%2加为好友", loginUser.getNickName(), friendId, friendUser.getNickName());

//			IMessageService messageService = ServiceFactory
//					.createMessageService();
//
//			MessageBean message = new MessageBean();
//			message.setFromUserId(loginUser.getId());
//			message.setToUserId(friendId);
//			message.setContent("我把你加为好友啦！哈哈哈哈！");
//			message.setMark(0);
//
//			if (!messageService.addMessage(message)) {
//				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
//			}
		}
		// 删除好友
		else if (request.getParameter("delete") != null) {
			if (!service.deleteFriend(loginUser.getId(), friendId)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}

			// IMessageService messageService = ServiceFactory
			// .createMessageService();
			//
			// MessageBean message = new MessageBean();
			// message.setFromUserId(loginUser.getId());
			// message.setToUserId(friendId);
			// message.setContent("我把你从我的好友列表中删除了！哈哈哈哈！");
			// message.setMark(0);
			//
			// if (!messageService.addMessage(message)) {
			// return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			// }

		}

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
