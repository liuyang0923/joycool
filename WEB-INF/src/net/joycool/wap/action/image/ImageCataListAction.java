/*
 * Created on 2005-11-30
 *
 */
package net.joycool.wap.action.image;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.bean.WapServiceBean;
import net.joycool.wap.bean.image.ImageBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IImageService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.WapServiceUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class ImageCataListAction extends Action {

	static final int NUMBER_PAGE = 5;
	static IJaLineService jaLineService = ServiceFactory.createJaLineService();
	static ICatalogService catalogService = ServiceFactory.createCatalogService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String orderBy = request.getParameter("orderBy");
		if ((orderBy == null) || !(orderBy.equals("id")))
			orderBy = "hits";
		int id, rootId;
		rootId = ((CatalogServiceImpl) catalogService).getId("image", 0);// 得到图片栏目的id；
		String strId = request.getParameter("id");
		if ((strId == null) || (strId.equals(""))) {
			id = rootId;
		} else {
			id = Integer.parseInt(strId);
		}

		int parentId = this.getParentId(id, catalogService);
		/*
		 * String backTo = request.getParameter("backTo"); if ((backTo == null) ||
		 * (backTo.equals(""))) { if (parentId == 0) { backTo =
		 * "ImageCataList.do?id=" + rootId; } else { backTo =
		 * "ImageCataList.do?id=" + parentId; } }
		 */

		// 从后台过来，是WAP产品
		int jaLineId = StringUtil.toInt(request.getParameter("jaLineId"));
		if (jaLineId == -1) {
			jaLineId = 0;
		}
		// 根返回节点
		String rootBackTo = null;
		JaLineBean originLine = null;
		if (jaLineId != 0) {
			
			originLine = jaLineService.getLine(jaLineId);
			if(originLine == null)
				rootBackTo = BaseAction.getBottom(request, response);
			else
				rootBackTo = JoycoolSpecialUtil.getRootBackTo(originLine);
		} else {
			rootBackTo = BaseAction.getBottom(request, response);
		}

		Vector list = ((CatalogServiceImpl) catalogService).getList(id);// 根据id
		// 得到以该id
		// 为父id的子类资源的列表；

		// prefixUrl
		// String prefixUrl = ("ImageCataList.do?id=" + id
		// + "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8")
		// + "&amp;orderBy=" + orderBy + "&amp;jaLineId=" + jaLineId);

		String prefixUrl = ("ImageCataList.do?id=" + id
			+ "&amp;orderBy=" + orderBy + "&amp;jaLineId=" + jaLineId);
		// request.setAttribute("backTo", backTo);
		request.setAttribute("rootId", rootId + "");
		request.setAttribute("id", id + "");
		if ((list == null) || (list.size() < 1))// 则该资源已经没有子类列表了。
		{
			IImageService imageSer = ServiceFactory.createImageService();

			int length = imageSer.getImagesCount("catalog_id = " + id);

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

			Vector currentImagesList = this.getImagesList(id, imageSer,
					orderBy, currentPage * NUMBER_PAGE, NUMBER_PAGE);// 根据id得到以该资源id
			Iterator itr = currentImagesList.iterator();
			ImageBean image = null;
			String linkUrl = null;

			/**
			 * wap业务
			 */
			int wapType = JoycoolSpecialUtil.getWapType(originLine);
			WapServiceBean wapService = null;
			String unique = null;
			if (wapType != 0) {
				wapService = WapServiceUtil.getWapServiceById(wapType);
			}
			Hashtable urlMap = JoycoolSessionListener.getUrlMap(request
					.getSession().getId());
			while (itr.hasNext()) {
				image = (ImageBean) itr.next();
				linkUrl = "/image/ImageInfo.do?imageId="
						+ image.getId() + "&amp;jaLineId=" + jaLineId
						+ "&amp;orderBy=" + orderBy ;
				if (wapService != null) {
					if (urlMap != null) {
						urlMap.put(unique, linkUrl.replace("&amp;", "&"));
					}
					linkUrl = wapService.getOrderAddress();
				}
				image.setLinkUrl(linkUrl);
			}

			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("currentPage", new Integer(currentPage));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("imagesList", currentImagesList);
			request.setAttribute("name", ((CatalogServiceImpl) catalogService)
					.getTitle(id));
			request.setAttribute("rootBackTo", rootBackTo);
			request.setAttribute("orderBy", orderBy);
			request.setAttribute("jaLineId", "" + jaLineId);
			request.setAttribute("wapType", new Integer(wapType));
			return mapping.findForward("imageList");
		} else {
			request.setAttribute("list", list);// 保存list到request中；
			request.setAttribute("rootBackTo", rootBackTo);
			request.setAttribute("jaLineId", "" + jaLineId);
			return mapping.findForward("success");
		}
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param imageSer
	 *            处理图片的类
	 * @return 得到图片列表
	 */
	private Vector getImagesList(int id, IImageService imageSer,
			String orderBy, int index, int count) {
		String condition = " catalog_id = " + id + " ORDER BY " + orderBy
				+ " DESC LIMIT " + index + ", " + count;
		Vector images = imageSer.getImagesList(condition);
		return images;
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
