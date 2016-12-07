package net.joycool.wap.action.friendlink;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.db.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class DeleteFriendLinkAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//DeleteForm deleteForm = (DeleteForm) form;
		String id=request.getParameter("id");
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "delete FROM link_record where id="+id;
		dbOp.executeUpdate(sql);
		dbOp.release();
		return mapping.findForward("ok");
	}

}