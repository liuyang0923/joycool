/*
 * Created on 2005-12-7
 *
 */
package net.joycool.wap.action.image;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IImageService;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class SearchImageAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String imageName = request.getParameter("imageName");
		IImageService imageService = ServiceFactory.createImageService();

		if ((imageName == null) || (imageName.trim().equals(""))) {
			return mapping.findForward("success");
		}

		// 分页 zhul 2006-09-11
		int NUM_PER_PAGE = 20;
		int totalCount = imageService.getImagesCount("name like '%" + StringUtil.toSql(imageName)
				+ "%'");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector images = imageService.getImagesList("name like '%" + StringUtil.toSql(imageName)
				+ "%' order by id LIMIT " + pageIndex * NUM_PER_PAGE + ","
				+ NUM_PER_PAGE);

		request.setAttribute("imageName", imageName);
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("images", images);

		return mapping.findForward("success");
	}

}
