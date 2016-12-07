/*
 * Created on 2006-2-17
 *
 */
package net.joycool.wap.action.ring;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IRingService;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author mcq
 * 
 */
public class SearchRingAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		IRingService ringService = ServiceFactory.createRingService();

		String ringName = request.getParameter("ringName");
		if ((ringName == null) || (ringName.trim().equals(""))) {
			return mapping.findForward("success");
		}

		// 分页 zhul 2006-09-11
		int NUM_PER_PAGE = 20;
		int totalCount = ringService.getPringsCount("name like '%" + StringUtil.toSql(ringName)
				+ "%' order by  id DESC");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector rings = ringService.getPringsList("name like '%" + StringUtil.toSql(ringName)
				+ "%' order by  id DESC LIMIT " + pageIndex * NUM_PER_PAGE
				+ "," + NUM_PER_PAGE);

		request.setAttribute("ringName", ringName);
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("rings", rings);

		return mapping.findForward("success");
	}
}
