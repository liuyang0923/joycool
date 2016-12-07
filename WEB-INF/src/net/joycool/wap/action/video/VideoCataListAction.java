/*
 * Created on 2006-8-15
 *
 */
package net.joycool.wap.action.video;

import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
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
public class VideoCataListAction extends Action {
	static final int NUMBER_PAGE = 5;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ICatalogService catalogService = ServiceFactory.createCatalogService();

		String orderBy = request.getParameter("orderBy");
		if ((orderBy == null) || !(orderBy.equals("id")))
			orderBy = "download_sum";
        request.setAttribute("orderBy",orderBy);
		int id, rootId;
        //得到视频栏目的id；
		rootId = ((CatalogServiceImpl) catalogService).getId("video", 0);
		String strId = request.getParameter("id");
		if ((strId == null) || (strId.equals(""))) {
			id = rootId;
		} else {
			id = Integer.parseInt(strId);
		}
        //获得ParentId的ID
		int parentId = this.getParentId(id, catalogService);
		String backTo = request.getParameter("backTo");
		if ((backTo == null) || (backTo.equals(""))) {
			//判断是否是树根如果是传入得到的栏目ID，否则传入得到的ParentId
			if (parentId == 0) {
				backTo = "VideoCataList.do?id=" + rootId;
			} else {
				backTo = "VideoCataList.do?id=" + parentId;
			}
		}
        //根据id得到对应的catalogList
		Vector list = ((CatalogServiceImpl) catalogService).getList(id);
		// 得到以该id
		// 为父id的子类资源的列表；

		//prefixUrl
		String prefixUrl = ("VideoCataList.do?id=" + id
				+ "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8")
				+ "&amp;orderBy=" + orderBy);

		request.setAttribute("backTo", backTo);
		request.setAttribute("rootId", rootId + "");
		request.setAttribute("id", id + "");
		if ((list == null) || (list.size() < 1))//则该资源已经没有子类列表了。
		{
			IVideoService videoSer = ServiceFactory.createVideoService();

			//为类别id的视频列表数量；(第一次这个ID就是得到视频栏目的id)
			int length = videoSer.getVideoCount("catalog_id = " + id);
            
			int currentPage = StringUtil.toInt(request
					.getParameter("pageIndex"));
			if (currentPage == -1) {
				currentPage = StringUtil.toInt(request
						.getParameter("pageIndex1"));
				if (currentPage == -1) {
					currentPage = 0;
				} else {
					currentPage = currentPage - 1;
				}
			}
			int totalPageCount = ((length % NUMBER_PAGE == 0) ? length
					/ NUMBER_PAGE : length / NUMBER_PAGE + 1);
			if (totalPageCount != 0 && currentPage > (totalPageCount - 1)) {
				currentPage = (totalPageCount - 1);
			}
			if (currentPage <= 0) {
				currentPage = 0;
			}

			Vector currentVideoList = this.getVideoList(id, videoSer, orderBy,
					currentPage * NUMBER_PAGE, NUMBER_PAGE);//根据id得到以该资源id

			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("currentPage", new Integer(currentPage));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("videoList", currentVideoList);
			request.setAttribute("name", ((CatalogServiceImpl) catalogService)
					.getTitle(id));
			return mapping.findForward("videoList");
		} else {
			request.setAttribute("list", list);//保存list到request中；
			return mapping.findForward("success");
		}
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param imageSer
	 *            处理视频的类
	 * @return 得到视频列表
	 */
	private Vector getVideoList(int id, IVideoService videoSer, String orderBy,
			int index, int count) {
		String condition = " catalog_id = " + id + " ORDER BY " + orderBy
				+ " DESC LIMIT " + index + ", " + count;
		Vector video = videoSer.getVideoList(condition);
		return video;
	}

	private int getParentId(int id, ICatalogService catalogService) {
		String condition = " id=" + id;
		CatalogBean catalog;
		catalog = ((CatalogServiceImpl) catalogService).getCatalog(condition);
		if (catalog == null) {
			return -1;
		} else {
			return catalog.getParentId();
		}
	}

}
