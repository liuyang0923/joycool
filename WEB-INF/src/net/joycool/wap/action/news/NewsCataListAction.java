/*
 * Created on 2005-11-28
 *
 */
package net.joycool.wap.action.news;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
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

/**
 * @author lbj
 *  
 */
public class NewsCataListAction extends Action {

    static final int NUMBER_PAGE = 10;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ICatalogService catalogService = ServiceFactory.createCatalogService();
        int id, rootId;
        rootId = ((CatalogServiceImpl) catalogService).getId("wapnews", 0);//得到新闻栏目的id;
        String strId = request.getParameter("id");
        if ((strId == null) || (strId.equals(""))) {
            id = rootId;
        } else {
            id = Integer.parseInt(strId);
        }
        //从后台过来，是WAP产品
        int jaLineId = StringUtil.toInt(request.getParameter("jaLineId"));
        if (jaLineId == -1) {
            jaLineId = 0;
        }
        //根返回节点
        String rootBackTo = null;
        if (jaLineId != 0) {
            IJaLineService jaLineService = ServiceFactory.createJaLineService();
            JaLineBean line = jaLineService.getLine(jaLineId);
            rootBackTo = JoycoolSpecialUtil.getRootBackTo(line);
        } else {
            rootBackTo = BaseAction.getBottom(request, response);
        }

/*        int parentId = this.getParentId(id, catalogService);
        String backTo = request.getParameter("backTo");
        if ((backTo == null) || (backTo.equals(""))) {
            if (parentId == 0) {
                backTo = "NewsCataList.do?id=" + rootId;
            } else {
                backTo = "NewsCataList.do?id=" + parentId;
            }
        }*/
        // modify by zhangyi 2006-07-31 for goback start
        String backTo = URLMap.getBacktoURL("http://wap.joycool.net/news/NewsCataList.do?id=",id);
        // modify by zhangyi 2006-07-31 for goback end
        
        String orderBy = request.getParameter("orderBy");
        if ((orderBy == null) || (orderBy.equals("")))
            orderBy = "id";
        Vector list = ((CatalogServiceImpl) catalogService).getList(id);//根据id
        // 得到以该id
        // 为父id的子类资源的列表；

        request.setAttribute("backTo", backTo);
        request.setAttribute("rootId", rootId + "");
        //request.setAttribute("id",id + "");
        if ((list == null) || (list.size() < 1))//则该资源已经没有子类列表了。
        {
            INewsService newsSer = ServiceFactory.createNewsService();

            int length = newsSer.getNewsCount("catalog_id = " + id);

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

            Vector currentNewsList = this.getNewsList(id, newsSer, orderBy,
                    currentPage * NUMBER_PAGE, NUMBER_PAGE);//根据id得到以该资源id

            String prefixUrl = "NewsCataList.do?id=" + id + "&amp;jaLineId="+ jaLineId;
                    //+ "&amp;backTo="
                   // + URLEncoder.encode(backTo, "UTF-8");
            request.setAttribute("totalPageCount", new Integer(totalPageCount));
            request.setAttribute("currentPage", new Integer(currentPage));
            request.setAttribute("prefixUrl", prefixUrl);
            request.setAttribute("newsList", currentNewsList);
            request.setAttribute("name", ((CatalogServiceImpl) catalogService)
                    .getTitle(id));
            request.setAttribute("id", "" + id);
            request.setAttribute("rootBackTo", rootBackTo);
            request.setAttribute("jaLineId", "" + jaLineId);
            return mapping.findForward("newsList");
        } else {
            request.setAttribute("list", list);//保存list到request中；
            request.setAttribute("rootBackTo", rootBackTo);
            request.setAttribute("jaLineId", "" + jaLineId);
            return mapping.findForward("success");
        }
    }

    /**
     * @param id
     *            子资源的id
     * @param newsSer
     *            处理新闻的类
     * @return 得到新闻列表
     */
    private Vector getNewsList(int id, INewsService newsSer, String orderBy,
            int index, int count) {
        String condition = " catalog_id = " + id + " ORDER BY " + orderBy
                + " DESC LIMIT " + index + ", " + count;
        Vector news = newsSer.getNewsList(condition);
        return news;
    }

    /*
     * private String getTitle(int id ,ICatalogService catalogSer){ String
     * condition = "id =" + id; CatalogBean catalog =
     * catalogSer.getCatalog(condition); if(catalog != null) { return
     * catalog.getName(); } else return ""; }
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
