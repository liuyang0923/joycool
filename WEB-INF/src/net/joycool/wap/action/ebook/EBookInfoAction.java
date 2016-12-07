/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action.ebook;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.ebook.EBookBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.CatalogServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * @author lbj
 *
 */
public class EBookInfoAction extends Action {
    static IEBookService ebookService = ServiceFactory.createEBookService();
    static ICatalogService catalogService = ServiceFactory.createCatalogService();    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        /*
         * 取得参数 ebookId 电子书Id pageIndex 分页码 orderBy 按xxx排序
         */
        int ebookId = StringUtil.toInt(request.getParameter("ebookId"));
        String backTo = request.getParameter("backTo");
        String orderBy = request.getParameter("orderBy");
        if((orderBy == null)||!(orderBy.equals("id")))
            orderBy = "download_sum";
        int pageIndex = 0;
        if (request.getParameter("pageIndex") != null) {
            pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        }
        
        int rootId = ((CatalogServiceImpl) catalogService).getId("ebook", 0);//得到图片栏目的id；
    
        //取得当前电子书及其所属类别
        String condition = "id = " + ebookId;
        EBookBean ebook = ebookService.getEBook(condition);
        if(ebook == null)
        	return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        int catalogId = ebook.getCatalogId();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = catalogService.getCatalog(buffCondition);
        
        //取得上一条和下一条
        String prevCondition = null;
        String nextCondition = null;
        EBookBean prevEBook = null;
        EBookBean nextEBook = null;
        if (orderBy.equals("id")) {
            prevCondition = "catalog_id = " + ebook.getCatalogId() + " and "
                    + orderBy + " > " + ebook.getId() + " ORDER BY id ASC";
            nextCondition = "catalog_id = " + ebook.getCatalogId() + " and "
                    + orderBy + " < " + ebook.getId() + " ORDER BY id DESC";
            prevEBook = ebookService.getEBook(prevCondition);
            nextEBook = ebookService.getEBook(nextCondition);
        } else if (orderBy.equals("hits")) {
            prevCondition = "catalog_id = " + ebook.getCatalogId() + " and "
                    + orderBy + " >= " + ebook.getDownloadSum() + " and id != "
                    + ebook.getId() + " ORDER BY hits ASC, id DESC";
            prevEBook = ebookService.getEBook(prevCondition);
            if (prevEBook != null) {
                nextCondition = "catalog_id = " + ebook.getCatalogId() + " and "
                        + orderBy + " <= " + ebook.getDownloadSum() + " and id != "
                        + ebook.getId() + " and id != " + prevEBook.getId()
                        + " ORDER BY hits DESC, id DESC";
            } else {
                nextCondition = "catalog_id = " + ebook.getCatalogId() + " and "
                        + orderBy + " <= " + ebook.getDownloadSum() + " and id != "
                        + ebook.getId() + " ORDER BY hits DESC, id DESC";
            }
            nextEBook = ebookService.getEBook(nextCondition);
        }
    
        if (prevEBook != null) {
            String prevNewsLink = ("EBookInfo.do?ebookId="
                    + prevEBook.getId() + "&amp;orderBy="
                    + orderBy);
            prevEBook.setLinkUrl(prevNewsLink);
        }
        if (nextEBook != null) {
            String nextNewsLink = ("EBookInfo.do?ebookId="
                    + nextEBook.getId() + "&amp;orderBy="
                    + orderBy);
            nextEBook.setLinkUrl(nextNewsLink);
        }
        
//      Liq 2007.3.26  新书1周之后才提供下载选项

        
		/*Calendar cal = Calendar.getInstance();
		cal.roll(Calendar.DAY_OF_YEAR,-7);
		
		String time = ebook.getCreateDateTime();
        int book_year = StringUtil.toInt(time.substring(0,4));
        int book_month = StringUtil.toInt(time.substring(5,7));
        int book_day = StringUtil.toInt(time.substring(8,10));
        
        int now_year = cal.get(Calendar.YEAR);
        int now_month = cal.get(Calendar.MONTH) + 1;
        int now_day = cal.get(Calendar.DAY_OF_MONTH);
        if(book_year*10000+book_month*100+book_day < now_year*10000+now_month*100+now_day)
        	request.setAttribute("ifdown","true");*/

//      Liq 2007.3.26  新书1周之后才提供下载选项
     
        //更新浏览次数
        String set = "download_sum = (download_sum + 1)";
        condition = "id = " + ebookId;
        ebookService.updateEBook(set, condition);
        //mcq_1_增加用户经验值  时间:2006-6-11
        //增加用户经验值
        HttpSession session =  request.getSession();
        UserBean user= (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
        RankAction.addPoint(user,Constants.RANK_EBOOK);
        //mcq_end
        request.setAttribute("rootId",rootId + "");
        request.setAttribute("ebook", ebook);
        request.setAttribute("backTo", backTo);
        request.setAttribute("catalog",catalog);
        request.setAttribute("prevEBook", prevEBook);
        request.setAttribute("nextEBook", nextEBook);
    
       return mapping.findForward("success");
    }
}
