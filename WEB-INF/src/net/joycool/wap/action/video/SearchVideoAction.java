/*
 * Created on 2006-8-15
 *
 */
package net.joycool.wap.action.video;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IVideoService;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author mcq
 * 
 */
public class SearchVideoAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		IVideoService videoService = ServiceFactory.createVideoService();

		String videoName = request.getParameter("videoName");
		if ((videoName == null) || (videoName.trim().equals(""))) {
			return mapping.findForward("success");
		}

		// 分页 zhul 2006-09-11
		int NUM_PER_PAGE = 20;
		int totalCount = videoService.getVideoCount("name like '%" + StringUtil.toSql(videoName)
				+ "%'");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector video = videoService.getVideoList("name like '%" + StringUtil.toSql(videoName)
				+ "%' order by  id DESC LIMIT " + pageIndex * NUM_PER_PAGE
				+ "," + NUM_PER_PAGE);

		request.setAttribute("videoName", videoName);
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("video", video);

		return mapping.findForward("success");
	}
}
