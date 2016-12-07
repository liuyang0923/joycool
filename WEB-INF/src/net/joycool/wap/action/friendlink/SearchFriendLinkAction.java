package net.joycool.wap.action.friendlink;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 02-22-2006
 * 
 * XDoclet definition:
 * @struts.action path="/search" name="searchForm" input="/form/search.jsp" scope="request" validate="true"
 */
public class SearchFriendLinkAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//SearchForm searchForm = (SearchForm) form;
		String name=request.getParameter("name");
		String url=request.getParameter("url");
		request.setAttribute("name",name);
		request.setAttribute("url",url);
		//System.out.print(nickname);
		//System.out.print(content);
		return mapping.findForward("success");
	}

}

