/*
 * Created on 2005-11-16
 *
 */
package net.joycool.wap.action.user;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class ViewBadGuysAction extends BaseAction {
	static IUserService service = ServiceFactory.createUserService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		int userId = -1;
		if (request.getParameter("userId") != null) {
			userId = Integer.parseInt(request.getParameter("userId"));
		} else {
			UserBean loginUser = getLoginUser(request);
			if (loginUser == null) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			userId = loginUser.getId();
		}
		List userList = SqlUtil.getIntList("select badguy_id from user_blacklist where user_id = " + userId);

		request.setAttribute("userList", userList);
		// request.setAttribute("backTo", backTo);
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
