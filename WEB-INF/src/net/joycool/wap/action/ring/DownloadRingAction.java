/*
 * Created on 2005-2-18
 *
 */
package net.joycool.wap.action.ring;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.service.factory.ServiceFactory;
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
public class DownloadRingAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {        
        String ringId = request.getParameter("id");
        if ((ringId == null) || (ringId.trim().equals(""))) {
            return mapping.findForward("success");
        }
        IRingService ringService = ServiceFactory.createRingService();
        ICatalogService cataService = ServiceFactory.createCatalogService();
        int catalog_id=StringUtil.toInt(request.getParameter("catalog_id"));
        CatalogBean catalog = cataService.getCatalog("id =" + catalog_id);
        String catalogname=catalog.getName();
        int pring_id = Integer.parseInt(ringId);
        Vector pring_file = ringService.getPring_filesList("pring_id = " + pring_id);
        request.setAttribute("pring_file",pring_file);
        request.setAttribute("catalog_id",String.valueOf(catalog_id));
        request.setAttribute("catalogname",catalogname);
        return mapping.findForward("success");
    }
}

