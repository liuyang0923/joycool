/*
 * Created on 2005-11-28
 *
 */
package net.joycool.wap.action.news;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.service.infc.INewsService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.URLMap;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.news.NewsBean;
import net.joycool.wap.bean.news.NewsAttachBean;

/**
 * @author lIq
 * 
 */
public class GroupCataListAction extends Action {

	static final int NUMBER_PAGE = 5;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ICatalogService catalogService = ServiceFactory.createCatalogService();
		int id, rootId;
		rootId = ((CatalogServiceImpl) catalogService).getId("wapnews", 0);// 得到新闻栏目的id;
		String strId = request.getParameter("id");
		String endlist = request.getParameter("endlist");
		if ((strId == null) || (strId.equals(""))) {
			id = rootId;
		} else {
			id = Integer.parseInt(strId);
		}
		// 从后台过来，是WAP产品
		int jaLineId = StringUtil.toInt(request.getParameter("jaLineId"));
		if (jaLineId == -1) {
			jaLineId = 0;
		}
		// 根返回节点
		String rootBackTo = null;
		if (jaLineId != 0) {
			IJaLineService jaLineService = ServiceFactory.createJaLineService();
			JaLineBean line = jaLineService.getLine(jaLineId);
			rootBackTo = JoycoolSpecialUtil.getRootBackTo(line);
		} else {
			rootBackTo = BaseAction.getBottom(request, response);
		}

		// modify by zhangyi 2006-07-31 for goback start
		String backTo = URLMap.getBacktoURL(
				"http://wap.joycool.net/news/GroupCataList.do?id=", id);
		// modify by zhangyi 2006-07-31 for goback end

		String orderBy = request.getParameter("orderBy");
		if ((orderBy == null) || (orderBy.equals("")))
			orderBy = "id";
		Vector list = ((CatalogServiceImpl) catalogService).getList(id);// 根据id
		// 得到以该id
		// 为父id的子类资源的列表；

		request.setAttribute("backTo", backTo);
		request.setAttribute("rootId", rootId + "");
//是显示新闻组页面,还是显示新闻链接页面
		if (endlist != null) {
			//新闻链接页面
			INewsService newsSer = ServiceFactory.createNewsService();
			CatalogBean nowGroup = ((CatalogServiceImpl) catalogService)
					.getCatalog(id);
			StringBuffer picture = new StringBuffer("");
			request.setAttribute("nowGroup", nowGroup);
			CatalogBean preGroup = getPreGroup(id, catalogService);
			CatalogBean nexGroup = getNexGroup(id, catalogService);
			request.setAttribute("preGroup", preGroup);
			request.setAttribute("nexGroup", nexGroup);
			//取得新闻组图片
			if ((list == null) || (list.size() < 1)) {
			} else {
				Vector childList = getNewsList(((CatalogBean) list.get(0))
						.getId(), newsSer, orderBy, 0, 1);// 根据儿子id得到以该List
				if (childList.size() > 0) {
					NewsBean child = (NewsBean) childList.get(0);
					Vector attachList = child.getAttachList();
					if (attachList.size() > 0) {
						NewsAttachBean attach = (NewsAttachBean) attachList
								.get(0);
						picture.append("<img src=\"" + attach.getFileURL()
								+ "\" alt=\"loading\"/><br/>");
					}
				}
			}
//			System.out.println("picture =" + picture);
			request.setAttribute("picture", picture.toString());

			Vector currentNewsList = this.getNewsList(id, newsSer, orderBy, 0,
					5);// 根据id得到以该资源id
			request.setAttribute("newsList", currentNewsList);

			request.setAttribute("name", ((CatalogServiceImpl) catalogService)
					.getTitle(id));
			request.setAttribute("jaLineId", "" + jaLineId);
			// return mapping.findForward("newsList");
			return mapping.findForward("newsInfo");
		} else {
			int length = list.size();
			int currentPage = StringUtil.toInt(request
					.getParameter("pageIndex"));
			if (currentPage == -1) {
				currentPage = 0;
			}

			int totalPageCount = ((length % NUMBER_PAGE == 0) ? length
					/ NUMBER_PAGE : length / NUMBER_PAGE + 1);
			if (totalPageCount != 0 && currentPage > (totalPageCount - 1)) {
				currentPage = (totalPageCount - 1);
			}
			if (currentPage <= 0) {
				currentPage = 0;
			}
//新闻组列表
			Vector currentNewsList = this.getGrpupList(list, orderBy,
					currentPage * NUMBER_PAGE, NUMBER_PAGE);// 根据id得到以该资源id

			String prefixUrl = "GroupCataList.do?id=" + id + "&amp;jaLineId="
					+ jaLineId;
			// + "&amp;backTo="
			// + URLEncoder.encode(backTo, "UTF-8");
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("currentPage", new Integer(currentPage));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("newsList", currentNewsList);
			request.setAttribute("name", ((CatalogServiceImpl) catalogService)
					.getTitle(id));
			request.setAttribute("rootBackTo", rootBackTo);
			request.setAttribute("jaLineId", "" + jaLineId);
			return mapping.findForward("newsList");
		}
	}

	/**
	 * @param list新闻列表
	 * @param index从多少开始取
	 * @param 取多少个
	 * @return 得到新闻列表
	 */

	private Vector getGrpupList(Vector list, String orderBy, int index,
			int count) {
		Vector news = new Vector();

		int listLength = list.size();
		if (index <= listLength)
			for (int i = 0; i < index; i++)
				list.remove(0);
		else
			return null;

		if (list.size() <= count)
			return list;
		else {
			for (int i = 0; i < count; i++)
				news.add(list.get(i));
			return news;
		}
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param newsSer
	 *            处理新闻的类
	 * @return 取得新闻对象
	 */
	private Vector getNewsList(int id, INewsService newsSer, String orderBy,
			int index, int count) {
		String condition = " catalog_id = " + id + " ORDER BY " + orderBy
				+ " DESC LIMIT " + index + ", " + count;
		Vector news = newsSer.getNewsList(condition);
		return news;
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param newsSer
	 *            处理新闻的类
	 * @return 前一个新闻对象
	 */
	private CatalogBean getPreGroup(int id, ICatalogService catalogService) {
		String condition = " id < " + id + " and parent_id ="
				+ getParentId(id, catalogService) + " order by id desc";
		CatalogBean catalog;
		catalog = ((CatalogServiceImpl) catalogService).getCatalog(condition);

		return catalog;
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param newsSer
	 *            处理新闻的类
	 * @return 后一个新闻对象
	 */
	private CatalogBean getNexGroup(int id, ICatalogService catalogService) {
		String condition = " id > " + id + " and parent_id ="
				+ getParentId(id, catalogService);
		CatalogBean catalog;
		catalog = ((CatalogServiceImpl) catalogService).getCatalog(condition);

		return catalog;
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param catalogService
	 *            数据库操作对象
	 * @return 取得上级的ID
	 */
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
