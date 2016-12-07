/*
 * Created on 2005-12-8
 *
 */
package net.joycool.wap.action.game;

import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IGameService;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.URLMap;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class GameCataListAction extends Action {

	static final int NUMBER_PAGE = 5;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getParameter("gameName") != null) {
			int ITEM_PER_PAGE = 20;
			int itemCount = 0;
			int totalPageCount = 0;
			int pageIndex = 0;
			if (request.getParameter("pageIndex") != null)
				pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			String gameName = request.getParameter("gameName").trim();
			Vector gameList = null;
			IGameService gameService = ServiceFactory.createGameService();
			String strWhere = "";
			if (!gameName.equals("")) {
				strWhere = "name like '%" + StringUtil.toSql(gameName) + "%' order by id DESC";

			} else {
				strWhere = " 1=1 order by id DESC";
			}

			itemCount = gameService.getGamesCount(strWhere);
			totalPageCount = itemCount / ITEM_PER_PAGE;
			if (itemCount % ITEM_PER_PAGE > 0)
				totalPageCount++;
			pageIndex = Math.min(totalPageCount - 1, pageIndex);
			pageIndex = Math.max(0, pageIndex);
			gameList = gameService.getGamesList(strWhere + " limit "
					+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);

			request.setAttribute("totalPageCount", totalPageCount + "");
			request.setAttribute("pageIndex", pageIndex + "");
			// gameList = gameService.getGamesList(strWhere);
			if (gameList != null) {
				request.setAttribute("gameList", gameList);
			}
			return mapping.findForward("gameList2");

		} else if (request.getParameter("mobileType") != null) {
			int ITEM_PER_PAGE = 20;
			int itemCount = 0;
			int totalPageCount = 0;
			int pageIndex = 0;
			if (request.getParameter("pageIndex") != null)
				pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			String mobileTypeName = request.getParameter("mobileType").trim();
			String mobileType = (String) LoadResource.getMobileMap().get(
					mobileTypeName);
			if(mobileType == null)
				mobileType = "all";
			Vector gameList = null;
			IGameService gameService = ServiceFactory.createGameService();
			String strWhere = "";
			if (!(mobileType.equals("") || mobileType.toLowerCase().equals(
					"all"))) {
				strWhere = "fit_mobile like '%" + mobileType
						+ "%' order by id DESC";

			} else {
				strWhere = " 1=1 order by id DESC";
			}
			itemCount = gameService.getGamesCount(strWhere);
			totalPageCount = itemCount / ITEM_PER_PAGE;
			if (itemCount % ITEM_PER_PAGE > 0)
				totalPageCount++;
			pageIndex = Math.min(totalPageCount - 1, pageIndex);
			pageIndex = Math.max(0, pageIndex);
			gameList = gameService.getGamesList(strWhere + " limit "
					+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);

			request.setAttribute("totalPageCount", totalPageCount + "");
			request.setAttribute("pageIndex", pageIndex + "");
			if (gameList != null) {
				request.setAttribute("gameList", gameList);
			}
			return mapping.findForward("gameMobileList");
		}
		ICatalogService catalogService = ServiceFactory.createCatalogService();

		String orderBy = request.getParameter("orderBy");
		if ((orderBy == null) || !(orderBy.equals("id")))
			// mcq_2006-6-20_更改默认排序规则_start
			orderBy = "hits";
		// mcq_2006-6-20_更改默认排序规则_end
		request.setAttribute("orderBy", orderBy);
		int id, rootId;
		rootId = ((CatalogServiceImpl) catalogService).getId("wapgame", 0);// 得到游戏栏目的id；
		String strId = request.getParameter("id");
		if ((strId == null) || (strId.equals(""))) {
			id = rootId;
		} else {
			id = Integer.parseInt(strId);
		}

		// int parentId = this.getParentId(id, catalogService);
		// String backTo = request.getParameter("backTo");
		// if ((backTo == null) || (backTo.equals(""))) {
		// if (parentId == 0) {
		// backTo = "GameCataList.do?id=" + rootId;
		// } else {
		// backTo = "GameCataList.do?id=" + parentId;
		// }
		// }
		// zhul_2006-07-31 修改backTo问题 start
		String backTo = URLMap.getBacktoURL("/GameCataList.do?id=", id);
		// zhul_2006-07-31 修改backTo问题 end
		Vector list = ((CatalogServiceImpl) catalogService).getList(id);// 根据id
		// 得到以该id
		// 为父id的子类资源的列表；

		// prefixUrl
		String prefixUrl = ("GameCataList.do?id=" + id
				+ "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8")
				+ "&amp;orderBy=" + orderBy);

		request.setAttribute("backTo", backTo);
		request.setAttribute("rootId", rootId + "");
		request.setAttribute("id", id + "");
		if ((list == null) || (list.size() < 1))// 则该资源已经没有子类列表了。
		{
			IGameService gameSer = ServiceFactory.createGameService();

			// 为类别id的游戏列表；
			int length = gameSer.getGamesCount("catalog_id = " + id);

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

			Vector currentGamesList = this.getGamesList(id, gameSer, orderBy,
					currentPage * NUMBER_PAGE, NUMBER_PAGE);// 根据id得到以该资源id

			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("currentPage", new Integer(currentPage));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("gamesList", currentGamesList);
			request.setAttribute("name", ((CatalogServiceImpl) catalogService)
					.getTitle(id));
			return mapping.findForward("gameList");
		} else {
			request.setAttribute("list", list);// 保存list到request中；
			return mapping.findForward("success");
		}
	}

	/**
	 * @param id
	 *            子资源的id
	 * @param imageSer
	 *            处理游戏的类
	 * @return 得到游戏列表
	 */
	private Vector getGamesList(int id, IGameService gameSer, String orderBy,
			int index, int count) {
		String condition = " catalog_id = " + id + " ORDER BY " + orderBy
				+ " DESC LIMIT " + index + ", " + count;
		Vector games = gameSer.getGamesList(condition);
		return games;
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

	public static String pagination(int totalPageCount, int currentPageIndex,
			int pageCount, String prefixUrl, String separator,
			HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / pageCount) * pageCount;
		int endIndex = (currentPageIndex / pageCount + 1) * pageCount - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\"" + (prefixUrl));
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

}
