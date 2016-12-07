/*
 * Created on 2005-12-7
 *
 */
package net.joycool.wap.action.ebook;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.ebook.EBookBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.util.Constants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *
 */
public class SearchInfoAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IEBookService ebookService = ServiceFactory.createEBookService();
        ICatalogService catalogService = ServiceFactory.createCatalogService();

        /*
         * 取得参数id 电子书id 
         */
        int ebookId = Integer.parseInt(request.getParameter("ebookId"));

        //取得当前电子书及其所属类别
        String condition = "id = " + ebookId;
        EBookBean ebook = ebookService.getEBook(condition);
        int catalogId = ebook.getCatalogId();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = catalogService.getCatalog(buffCondition);

        request.setAttribute("ebook", ebook);
        request.setAttribute("catalog", catalog);
        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
