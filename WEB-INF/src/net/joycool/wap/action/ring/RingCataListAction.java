/*
 * Created on 2006-2-16
 *
 */
package net.joycool.wap.action.ring;

import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
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
public class RingCataListAction extends Action {
	static final int NUMBER_PAGE = 5;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getParameter("fileType") != null) {
			int ITEM_PER_PAGE = 20;
			int itemCount = 0;
			int totalPageCount = 0;
			int pageIndex = 0;
			if (request.getParameter("pageIndex") != null)
				pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			String fileType = request.getParameter("fileType").trim()
					.toLowerCase();

			Vector ringList = null;
			IRingService ringService = ServiceFactory.createRingService();
			// 收费铃声的catalogID
			String[] chargeRingCatalogIds = new String[] { "642", "643", "644" };
			String strWhere = " file_type like '" + fileType + "%'";
			// 排除收费的铃声分类id
			String catalogWhere = " and catalog_id not in(";
			for (int i = 0; i < chargeRingCatalogIds.length; i++) {
				catalogWhere += chargeRingCatalogIds[i] + ",";
			}
			catalogWhere = catalogWhere.substring(0, catalogWhere.length() - 1)
					+ ")";
			strWhere = strWhere + catalogWhere;

			itemCount = ringService
					.getPring_filesCount("select count(*) as c_id from (select  distinct a.id from   pring as a join pring_file as b on a.id=b.pring_id where "
							+ strWhere + " ) as temp");
			totalPageCount = itemCount / ITEM_PER_PAGE;
			if (itemCount % ITEM_PER_PAGE > 0)
				totalPageCount++;
			pageIndex = Math.min(totalPageCount - 1, pageIndex);
			pageIndex = Math.max(0, pageIndex);
			ringList = ringService
					.getPringsList("select  distinct a.* from   pring as a join pring_file as b on a.id=b.pring_id where "
							+ strWhere
							+ " order by download_sum desc ,a.name "
							+ " limit "
							+ pageIndex
							* ITEM_PER_PAGE
							+ ","
							+ ITEM_PER_PAGE);

			request.setAttribute("totalPageCount", totalPageCount + "");
			request.setAttribute("pageIndex", pageIndex + "");
			if (ringList != null) {
				request.setAttribute("ringList", ringList);
			}
			return mapping.findForward("fileTypeRingList");
		}

		ICatalogService catalogService = ServiceFactory.createCatalogService();

		String orderBy = request.getParameter("orderBy");
		if ((orderBy == null) || (!orderBy.equals("id")))	// 除了按照id排列就是按照download_sum
			orderBy = "download_sum";
		request.setAttribute("orderBy", orderBy);
		int id, rootId;
		// 得到炫铃栏目的id；
		rootId = ((CatalogServiceImpl) catalogService).getId("ring", 0);
		String strId = request.getParameter("id");
		if ((strId == null) || (strId.equals(""))) {
			id = rootId;
		} else {
			id = Integer.parseInt(strId);
		}
		// 获得ParentId的ID
		int parentId = this.getParentId(id, catalogService);
		String backTo = request.getParameter("backTo");
		if ((backTo == null) || (backTo.equals(""))) {
			// 判断是否是树根如果是传入得到的栏目ID，否则传入得到的ParentId
			if (parentId == 0) {
				backTo = "RingCataList.do?id=" + rootId;
			} else {
				backTo = "RingCataList.do?id=" + parentId;
			}
		}
		// 根据id得到对应的catalogList
		Vector list = ((CatalogServiceImpl) catalogService).getList(id);
		// 得到以该id
		// 为父id的子类资源的列表；

		// prefixUrl
		String prefixUrl = ("RingCataList.do?id=" + id
				+ "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8")
				+ "&amp;orderBy=" + orderBy);

		request.setAttribute("backTo", backTo);
		request.setAttribute("rootId", rootId + "");
		request.setAttribute("id", id + "");
		if ((list == null) || (list.size() < 1))// 则该资源已经没有子类列表了。
		{
			IRingService ringSer = ServiceFactory.createRingService();

			// 为类别id的炫铃列表数量；(第一次这个ID就是得到炫铃栏目的id)
			int length = ringSer.getPringsCount("catalog_id = " + id);

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

			Vector currentRingsList = this.getRingsList(id, ringSer, orderBy,
					currentPage * NUMBER_PAGE, NUMBER_PAGE);// 根据id得到以该资源id

			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("currentPage", new Integer(currentPage));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("ringsList", currentRingsList);
			request.setAttribute("name", ((CatalogServiceImpl) catalogService)
					.getTitle(id));
			return mapping.findForward("ringList");
		} else {
			request.setAttribute("list", list);// 保存list到request中；
			return mapping.findForward("success");
		}
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param imageSer
	 *            处理炫铃的类
	 * @return 得到炫铃列表
	 */
	private Vector getRingsList(int id, IRingService ringSer, String orderBy,
			int index, int count) {
		String condition = " catalog_id = " + id + " ORDER BY " + orderBy
				+ " DESC LIMIT " + index + ", " + count;
		Vector rings = ringSer.getPringsList(condition);
		return rings;
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
