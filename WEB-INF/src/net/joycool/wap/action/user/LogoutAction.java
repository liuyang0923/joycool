package net.joycool.wap.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.util.Constants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		HttpSession session = request.getSession();
		UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		if(loginUser!=null){
			JoycoolSessionListener.logout(request, loginUser);
		}
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
