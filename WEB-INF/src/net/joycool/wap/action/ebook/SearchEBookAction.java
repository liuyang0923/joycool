/*
 * Created on 2005-12-7
 *
 */
package net.joycool.wap.action.ebook;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class SearchEBookAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		IEBookService ebookService = ServiceFactory.createEBookService();

		String ebookName = request.getParameter("ebookName");
		if ((ebookName == null) || (ebookName.trim().equals(""))) {
			return mapping.findForward("success");
		}

		// 分页 zhul 2006-09-11
		int NUM_PER_PAGE = 20;
		int totalCount = ebookService.getEBooksCount("name like '%" + StringUtil.toSql(ebookName)
				+ "%'");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector ebooks = ebookService.getEBooksList("name like '%" + StringUtil.toSql(ebookName)
				+ "%' order by id LIMIT " + pageIndex * NUM_PER_PAGE + ","
				+ NUM_PER_PAGE);

		request.setAttribute("ebookName", ebookName);
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("ebooks", ebooks);

		return mapping.findForward("success");
	}

}
