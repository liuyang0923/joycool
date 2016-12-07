/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action.ebook;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IEBookService;
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
public class EBookCataListAction extends Action {

    static final int NUMBER_PAGE = 5;

public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ICatalogService catalogService = ServiceFactory.createCatalogService();
 
        String orderBy = request.getParameter("orderBy");
        if ((orderBy == null) || !(orderBy.equals("id")))
        	//Liq_2007.3.26__更改默认排序规则_start
            orderBy = "download_sum";
            //Liq__2007.3.26_更改默认排序规则_end
    	//mcq_2006-6-20_更改默认排序规则_start
//        orderBy = "download_sum";
        //mcq_2006-6-20_更改默认排序规则_end
        request.setAttribute("orderBy",orderBy);
        int id, rootId;
        rootId = ((CatalogServiceImpl) catalogService).getId("ebook", 0);//得到图片栏目的id；
        String strId = request.getParameter("id");
        if (strId == null || strId.equals("") || strId.equals("0")) {
            id = rootId;
        } else {
            id = Integer.parseInt(strId);
        }

/*        int parentId = this.getParentId(id, catalogService);
        String backTo = request.getParameter("backTo");
        if ((backTo == null) || (backTo.equals(""))) {
            if (parentId == 0) {
                backTo = "EBookCataList.do?id=" + rootId;
            } else {
                backTo = "EBookCataList.do?id=" + parentId;
            }
        }*/
        
        // modify by zhangyi 2006-07-31 for goback start
        String backTo = URLMap.getBacktoURL("/ebook/EBookCataList.do?id=",id);
        request.setAttribute("backTo", backTo);
        // modify by zhangyi 2006-07-31 for goback end
        
        Vector list = ((CatalogServiceImpl) catalogService).getList(id);//根据id
        // 得到以该id
        // 为父id的子类资源的列表；

        //prefixUrl
        String prefixUrl = ("EBookCataList.do?id=" + id
                + "&amp;orderBy=" + orderBy);

        /**
         * 当前的catalog。
         */
        CatalogBean catalog = catalogService.getCatalog("id = " + id);
        request.setAttribute("catalog", catalog);

        request.setAttribute("rootId", rootId + "");
        request.setAttribute("id", id + "");
        //则该资源已经没有子类列表了。
        if ((list == null) || (list.size() < 1)) {
            IEBookService ebookSer = ServiceFactory.createEBookService();

            int length = ebookSer.getEBooksCount("catalog_id = " + id);

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

            Vector currentEBooksList = this.getEBooksList(id, ebookSer,
                    orderBy, currentPage * NUMBER_PAGE, NUMBER_PAGE);//根据id得到以该资源id

            request.setAttribute("totalPageCount", new Integer(totalPageCount));
            request.setAttribute("currentPage", new Integer(currentPage));
            request.setAttribute("prefixUrl", prefixUrl);
            request.setAttribute("ebooksList", currentEBooksList);
            request.setAttribute("name", ((CatalogServiceImpl) catalogService)
                    .getTitle(id));
            return mapping.findForward("ebookList");
        } else {
            request.setAttribute("list", list);//保存list到request中；
            return mapping.findForward("success");
        }
    }    /**
          * @param id
          *            子资源的id
          * @param imageSer
          *            处理电子书的类
          * @return 得到电子书列表
          */
    private Vector getEBooksList(int id, IEBookService ebookSer,
            String orderBy, int index, int count) {
        String condition = " catalog_id = " + id + " ORDER BY " + orderBy
                + " DESC LIMIT " + index + ", " + count;
        Vector ebooks = ebookSer.getEBooksList(condition);
        return ebooks;
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
