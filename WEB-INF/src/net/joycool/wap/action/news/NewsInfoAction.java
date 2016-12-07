/*
 * Created on 2005-11-25
 *
 */
package net.joycool.wap.action.news;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.news.NewsAttachBean;
import net.joycool.wap.bean.news.NewsBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.service.infc.INewsService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class NewsInfoAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		INewsService newsService = ServiceFactory.createNewsService();
		ICatalogService cataService = ServiceFactory.createCatalogService();
		/*
		 * 取得参数 newsId 新闻Id pageIndex 分页码 backTo 返回上一级 orderBy 按xxx排序
		 */
		int newsId = Integer.parseInt(request.getParameter("newsId"));
		// String backTo = request.getParameter("backTo");
		String orderBy = request.getParameter("orderBy");
		if ((orderBy == null) || (orderBy.equals("")))
			orderBy = "id";
		int pageIndex = 0;
		if (request.getParameter("pageIndex") != null) {
			pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		}
		// 后台id，可能是Catalog的id，用于取得rootBackTo；可能是news的id
		int jaLineId = StringUtil.toInt(request.getParameter("jaLineId"));
		if (jaLineId == -1) {
			jaLineId = 0;
		}
		// 根返回节点
		String rootBackTo = null;
		JaLineBean originLine = null;
		if (jaLineId != 0) {
			IJaLineService jaLineService = ServiceFactory.createJaLineService();
			originLine = jaLineService.getLine(jaLineId);
			rootBackTo = JoycoolSpecialUtil.getRootBackTo(originLine);
			// 业务类型
			int wapType = JoycoolSpecialUtil.getWapType(originLine);
			if (wapType != 0) {
				request.setAttribute("wapType", new Integer(wapType));
			}
		} else {
			rootBackTo = BaseAction.getBottom(request, response);
		}

		String prefixUrl = null;

		// 取得当前新闻
		String condition = "id = " + newsId;
		NewsBean news = newsService.getNews(condition);
		if(news == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		// if ((backTo == null) || (backTo.equals(""))) {
		// backTo = "NewsCataList.do?id=" + news.getCatalogId();
		// }
		int catalogId = news.getCatalogId();
		String buffCondition = "id = " + catalogId;
		CatalogBean catalog = cataService.getCatalog(buffCondition);

		int totalLength = news.getContent().length();
		int totalPageCount = totalLength / Constants.NEWS_WORD_PER_PAGE;
		if (totalLength % Constants.NEWS_WORD_PER_PAGE != 0) {
			totalPageCount++;
		}
//		news.setContent(news.getContent().substring(
//				pageIndex * Constants.NEWS_WORD_PER_PAGE));
//		news.setContent(StringUtil.cutString(news.getContent(),
//				Constants.NEWS_WORD_PER_PAGE));

		// 取得上一条和下一条
		String prevCondition = null;
		String nextCondition = null;
		NewsBean prevNews = null;
		NewsBean nextNews = null;
		if (orderBy.equals("id")) {
			prevCondition = "catalog_id = " + news.getCatalogId() + " and "
					+ orderBy + " > " + news.getId() + " ORDER BY id ASC";
			nextCondition = "catalog_id = " + news.getCatalogId() + " and "
					+ orderBy + " < " + news.getId() + " ORDER BY id DESC";
			prevNews = newsService.getNews(prevCondition);
			nextNews = newsService.getNews(nextCondition);
		} else if (orderBy.equals("hits")) {
			prevCondition = "catalog_id = " + news.getCatalogId() + " and "
					+ orderBy + " >= " + news.getHits() + " and id != "
					+ news.getId() + " ORDER BY hits ASC, id DESC";
			prevNews = newsService.getNews(prevCondition);
			if (prevNews != null) {
				nextCondition = "catalog_id = " + news.getCatalogId() + " and "
						+ orderBy + " <= " + news.getHits() + " and id != "
						+ news.getId() + " and id != " + prevNews.getId()
						+ " ORDER BY hits DESC, id DESC";
			} else {
				nextCondition = "catalog_id = " + news.getCatalogId() + " and "
						+ orderBy + " <= " + news.getHits() + " and id != "
						+ news.getId() + " ORDER BY hits DESC, id DESC";
			}
			nextNews = newsService.getNews(nextCondition);
		}
		// 从后台过来
		else if (orderBy.equals("line_index")) {
			IJaLineService jaLineService = ServiceFactory.createJaLineService();
			JaLineBean line = jaLineService.getLine(jaLineId);
			String prevLineCondition = "link_type = " + JaLineBean.LT_NEWS
					+ " and parent_id = " + line.getParentId()
					+ " and line_index <= " + line.getLineIndex()
					+ " and id != " + jaLineId + " ORDER BY line_index DESC";
			String nextLineCondition = "link_type = " + JaLineBean.LT_NEWS
					+ " and parent_id = " + line.getParentId()
					+ " and line_index >= " + line.getLineIndex()
					+ " and id != " + jaLineId + " ORDER BY line_index ASC";
			JaLineBean prevLine = jaLineService.getLine(prevLineCondition);
			JaLineBean nextLine = jaLineService.getLine(nextLineCondition);

			if (prevLine != null) {
				prevNews = newsService.getNews("id = " + prevLine.getLink());
			}
			if (nextLine != null) {
				nextNews = newsService.getNews("id = " + nextLine.getLink());
			}

			if (prevNews != null) {
				String prevNewsLink = response
						.encodeURL("http://wap.joycool.net/news/NewsInfo.do?newsId="
								+ prevNews.getId()
								// + "&amp;backTo="
								// + URLEncoder.encode(backTo, "UTF-8")
								+ "&amp;orderBy="
								+ orderBy
								+ "&amp;jaLineId="
								+ prevLine.getId());
				/**
				 * wap业务
				 */
				// int wapType = JoycoolSpecialUtil.getWapType(originLine);
				// if (wapType != 0) {
				// String unique = null;
				// WapServiceBean wapService = WapServiceUtil
				// .getWapServiceById(wapType);
				// if (wapService != null) {
				// unique = StringUtil.getUnique();
				// Hashtable urlMap = JoycoolSessionListener
				// .getUrlMap(request.getSession().getId());
				// if (urlMap != null) {
				// urlMap.put(unique, prevNewsLink.replace("&amp;",
				// "&"));
				// }
				// prevNewsLink = wapService.getOrderAddress()
				// + "?unique=" + unique;
				// }
				// }
				prevNews.setLinkUrl(prevNewsLink);
			}
			if (nextNews != null) {
				String nextNewsLink = response
						.encodeURL("http://wap.joycool.net/news/NewsInfo.do?newsId="
								+ nextNews.getId()
								// + "&amp;backTo="
								// + URLEncoder.encode(backTo, "UTF-8")
								+ "&amp;orderBy="
								+ orderBy
								+ "&amp;jaLineId="
								+ nextLine.getId());
				/**
				 * wap业务
				 */
				// int wapType = JoycoolSpecialUtil.getWapType(originLine);
				// if (wapType != 0) {
				// String unique = null;
				// WapServiceBean wapService = WapServiceUtil
				// .getWapServiceById(wapType);
				// if (wapService != null) {
				// unique = StringUtil.getUnique();
				// Hashtable urlMap = JoycoolSessionListener
				// .getUrlMap(request.getSession().getId());
				// if (urlMap != null) {
				// urlMap.put(unique, nextNewsLink.replace("&amp;",
				// "&"));
				// }
				// nextNewsLink = wapService.getOrderAddress()
				// + "?unique=" + unique;
				// }
				// }
				nextNews.setLinkUrl(nextNewsLink);
			}

			prefixUrl = ("NewsInfo.do?newsId=" + news.getId()
					+ "&amp;jaLineId=" + jaLineId + "&amp;orderBy=" + orderBy);
		}
		// zhul_2006-08-01 modify backto to delete code:+ "&amp;backTo="+
		// URLEncoder.encode(backTo, "UTF-8")
		if (!orderBy.equals("line_index")) {
			if (prevNews != null) {
				String prevNewsLink = ("NewsInfo.do?newsId="
						+ prevNews.getId() + "&amp;jaLineId=" + jaLineId
						// + "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8")
						+ "&amp;orderBy=" + orderBy);
				prevNews.setLinkUrl(prevNewsLink);
			}
			if (nextNews != null) {
				String nextNewsLink = ("NewsInfo.do?newsId="
						+ nextNews.getId() + "&amp;jaLineId=" + jaLineId
						// + "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8")
						+ "&amp;orderBy=" + orderBy);
				nextNews.setLinkUrl(nextNewsLink);
			}
			prefixUrl = ("NewsInfo.do?newsId=" + news.getId()
					+ "&amp;jaLineId=" + jaLineId + "&amp;orderBy=" + orderBy);
		}
		// zhul_2006-08-01 modify backto to delete code:+ "&amp;backTo="+
		// URLEncoder.encode(backTo, "UTF-8")
		/**
		 * 显示附件代码
		 */
		Vector attachList = news.getAttachList();
		int i, count;
		count = attachList.size();
		StringBuffer attachCode = new StringBuffer();
		// 没有附件
		if (count == 0) {
		} else {
			NewsAttachBean attach = null;
			// 附件数目跟分页数目一样
			if (count == totalPageCount) {
				attach = (NewsAttachBean) attachList.get(pageIndex);
				attachCode.append("<img src=\"" + attach.getFileURL()
						+ "\" alt=\"lodading\"/><br/>");
			}
			// 附件数目多于分页数目
			else if (count > totalPageCount) {
				// 不是最后一页
				if (pageIndex < totalPageCount - 1) {
					attach = (NewsAttachBean) attachList.get(pageIndex);
					attachCode.append("<img src=\"" + attach.getFileURL()
							+ "\" alt=\"lodading\"/><br/>");
				}
				// 最后一页
				else {
					for (i = pageIndex; i < count; i++) {
						attach = (NewsAttachBean) attachList.get(i);
						attachCode.append("<img src=\"" + attach.getFileURL()
								+ "\" alt=\"lodading\"/><br/>");
					}
				}
			}
			// 附件数目少于分页数目
			else {
				// 不是最后一个附件
				if (pageIndex < count - 1) {
					attach = (NewsAttachBean) attachList.get(pageIndex);
					attachCode.append("<img src=\"" + attach.getFileURL()
							+ "\" alt=\"lodading\"/><br/>");
				}
				// 最后一个附件
				else {
					attach = (NewsAttachBean) attachList.get(count - 1);
					attachCode.append("<img src=\"" + attach.getFileURL()
							+ "\" alt=\"lodading\"/><br/>");
				}
			}
		}

		// 更新浏览数
		if (pageIndex == 0) {
			String set = "hits = (hits + 1)";
			condition = "id = " + newsId;
			newsService.updateNews(set, condition);
		}
		// mcq_1_增加用户经验值 时间:2006-6-11
		// 增加用户经验值
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session
				.getAttribute(Constants.LOGIN_USER_KEY);
		RankAction.addPoint(user, Constants.RANK_GENERAL);
		// mcq_end

		request.setAttribute("news", news);
		request.setAttribute("catalog", catalog);
		request.setAttribute("prevNews", prevNews);
		request.setAttribute("nextNews", nextNews);
		request.setAttribute("attachCode", attachCode.toString());
		request.setAttribute("rootBackTo", rootBackTo);

		// 分页相关
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		// System.out.println(backTo);
		// request.setAttribute("backTo", backTo);

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
