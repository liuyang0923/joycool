/*
 * Created on 2005-2-18
 *
 */
package net.joycool.wap.action.video;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IVideoService;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author mcq
 *
 */
public class DownloadVideoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {        
        String id = request.getParameter("id");
        if ((id == null) || (id.trim().equals(""))) {
            return mapping.findForward("success");
        }
        IVideoService videoService = ServiceFactory.createVideoService();
        ICatalogService cataService = ServiceFactory.createCatalogService();
        int catalogId=StringUtil.toInt(request.getParameter("catalog_id"));
        CatalogBean catalog = cataService.getCatalog("id =" + catalogId);
        String catalogname=catalog.getName();
        int videoId = Integer.parseInt(id);
        Vector videoFile = videoService.getVideoFileList("video_id = " + videoId);
        request.setAttribute("videoFile",videoFile);
        request.setAttribute("catalogId",String.valueOf(catalogId));
        request.setAttribute("catalogname",catalogname);
        return mapping.findForward("success");
    }
}

