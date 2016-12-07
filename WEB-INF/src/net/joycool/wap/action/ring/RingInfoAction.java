/*
 * Created on 2006-2-18
 *
 */
package net.joycool.wap.action.ring;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.ring.PringBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IRingService;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author mcq
 *
 */
public class RingInfoAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	IRingService ringService = ServiceFactory.createRingService();
        ICatalogService cataService = ServiceFactory.createCatalogService();

        /*
         * 取得参数RingId 游戏id pageIndex 分页码 orderBy 按xxx排序
         */
        int ringId = Integer.parseInt(request.getParameter("ringId"));
        PringBean ring = ringService.getPring("id = " + ringId);
        if(ring == null)
        	return mapping.findForward("success");

        Vector ringFileList=ringService.getPring_filesList("pring_id="+ring.getId());
        //PringFileBean ringFile=ringService.getPring_file("pring_id="+ring.getId());

//        String backTo = request.getParameter("backTo");
//        if ((backTo == null) || (backTo.equals("")))
//            backTo = "RingCataList.do?id=" + ring.getCatalog_id();
        String orderBy = request.getParameter("orderBy");
        if ((orderBy == null) || !(orderBy.equals("id")))
            orderBy = "download_sum";
        int pageIndex = 0;
        if (request.getParameter("pageIndex") != null) {
            pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        }

        //取得根据ringId得到的游戏文件；
        String condition = "id = " + ringId;
        int catalogId = ring.getCatalog_id();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = cataService.getCatalog(buffCondition);

        //取得上一条和下一条
        String prevCondition = null;
        String nextCondition = null;
        PringBean prevring = null;
        PringBean nextring = null;
        if (orderBy.equals("id")) {
            prevCondition = "catalog_id = " + ring.getCatalog_id() + " and "
                    + orderBy + " > " + ring.getId() + " ORDER BY id ASC";
            nextCondition = "catalog_id = " + ring.getCatalog_id() + " and "
                    + orderBy + " < " + ring.getId() + " ORDER BY id DESC";
            prevring = ringService.getPring(prevCondition);
            nextring  = ringService.getPring(nextCondition);
        } else if (orderBy.equals("download_sum")) {
            prevCondition = "catalog_id = " + ring.getCatalog_id() + " and "
                    + orderBy + " >= " + ring.getDownload_sum()+ " and id != "
                    + ring.getId() + " ORDER BY download_sum ASC, id DESC";
            prevring = ringService.getPring(prevCondition);
            if (prevring != null) {
                nextCondition = "catalog_id = " + ring.getCatalog_id()
                        + " and " + orderBy + " <= " + ring.getDownload_sum()
                        + " and id != " + ring.getId() + " and id != "
                        + prevring.getId() + " ORDER BY download_sum DESC, id DESC";
            } else {
                nextCondition = "catalog_id = " + ring.getCatalog_id()
                        + " and " + orderBy + " <= " + ring.getDownload_sum()
                        + " and id != " + ring.getId()
                        + " ORDER BY download_sum DESC, id DESC";
            }
            nextring = ringService.getPring(nextCondition);
        }

        if (prevring != null) {
//            String prevImageLink = ("RingInfo.do?ringId="
//                    + prevring.getId() + "&amp;backTo="
//                    + URLEncoder.encode(backTo, "UTF-8") + "&amp;orderBy="
//                    + orderBy);
            String prevImageLink = ("RingInfo.do?ringId="
                    + prevring.getId() + "&amp;orderBy="
                    + orderBy);
            prevring.setLinkUrl(prevImageLink);
        }
        if (nextring != null) {
//            String nextImageLink = ("RingInfo.do?ringId="
//                    + nextring.getId() + "&amp;backTo="
//                    + URLEncoder.encode(backTo, "UTF-8") + "&amp;orderBy="
//                    + orderBy);
            String nextImageLink = ("RingInfo.do?ringId="
                    + nextring.getId() + "&amp;orderBy="
                    + orderBy);
            nextring.setLinkUrl(nextImageLink);
        }

        //prefixUrl
//        String prefixUrl = ("RingInfo.do?ringId="
//                + ring.getId() + "&amp;backTo="
//                + URLEncoder.encode(backTo, "UTF-8") + "&amp;orderBy="
//                + orderBy);
        String prefixUrl = ("RingInfo.do?ringId="
                + ring.getId() + "&amp;orderBy="
                + orderBy);

        //      更新浏览数
        if (pageIndex == 0) {
            String set = "download_sum = (download_sum + 1)";
            condition = "id = " + ringId;
            ringService.updatePring(set, condition);
        }

        request.setAttribute("ring", ring);
        request.setAttribute("ringFileList",ringFileList);
        request.setAttribute("prefixUrl", prefixUrl);
        request.setAttribute("catalog", catalog);
      //request.setAttribute("backTo", backTo);
        request.setAttribute("prevRing", prevring);
        request.setAttribute("nextRing", nextring);

        return mapping.findForward("success");
    }

}

