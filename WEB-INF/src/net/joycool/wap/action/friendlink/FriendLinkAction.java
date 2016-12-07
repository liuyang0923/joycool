package net.joycool.wap.action.friendlink;

/*
 * Created on 2006-2-24
 *
 */
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendLinkService;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class FriendLinkAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IFriendLinkService friendlinkService = ServiceFactory.createFriendLinkService();
		Vector list = friendlinkService.getLinkTypeList();
		request.setAttribute("list", list);
		return mapping.findForward("success");
	}
}
