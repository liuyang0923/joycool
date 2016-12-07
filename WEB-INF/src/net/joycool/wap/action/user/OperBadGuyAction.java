/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class OperBadGuyAction extends BaseAction {
	static UserServiceImpl service = new UserServiceImpl();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		

		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}

		// 取得参数
		int badGuyId = StringUtil.toInt(request.getParameter("badGuyId"));
		if (badGuyId == loginUser.getId()) {	// 不能加自己到黑名单
			request.setAttribute("friendId", String.valueOf(badGuyId));
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		if(badGuyId <= 100 || UserInfoUtil.getUser(badGuyId) == null) {	// 无法处理
			request.setAttribute("friendId", String.valueOf(loginUser.getId()));
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		
		// 增加黑名单
		if (request.getParameter("add") != null) {
			boolean isABadGuys=service.isUserBadGuy(loginUser.getId(), badGuyId);
			if(isABadGuys || badGuyId == loginUser.getId())
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			// add by zhangyi for forfid add manager into blackList start
			if (badGuyId == 431 || badGuyId == 519610 || badGuyId == 914727) {
				request.setAttribute("manager", "true");
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			// 如果是好友，先删除好友
			if (!service.deleteFriend(loginUser.getId(), badGuyId)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			// add by zhangyi for forfid add manager into blackList start
			if (!service.addBadGuy(loginUser.getId(), badGuyId)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}

			// IMessageService messageService =
			// ServiceFactory.createMessageService();

			// MessageBean message = new MessageBean();
			// message.setFromUserId(loginUser.getId());
			// message.setToUserId(badGuyId);
			// message.setContent("我把你加入黑名单啦！哈哈哈哈！");
			// message.setMark(0);

			// if(!messageService.addMessage(message)){
			// return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			// }
		}
		// 删除黑名单
		else if (request.getParameter("delete") != null) {
			if (!service.deleteBadGuy(loginUser.getId(), badGuyId)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}

			// IMessageService messageService =
			// ServiceFactory.createMessageService();
			//    
			// MessageBean message = new MessageBean();
			// message.setFromUserId(loginUser.getId());
			// message.setToUserId(badGuyId);
			// message.setContent("我把你从我的黑名单中删除了！哈哈哈哈！");
			// message.setMark(0);
			// if(!messageService.addMessage(message)){
			// return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			// }
		}

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
