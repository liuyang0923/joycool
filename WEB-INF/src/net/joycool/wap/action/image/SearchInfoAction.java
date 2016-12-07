/*
 * Created on 2005-12-7
 *
 */
package net.joycool.wap.action.image;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.image.ImageBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IImageService;
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
        IImageService imageService = ServiceFactory.createImageService();
        ICatalogService cataService = ServiceFactory.createCatalogService();

        /*
         * 取得参数id 图片id 
         */
        int imageId = Integer.parseInt(request.getParameter("imageId"));
        ImageBean image = imageService.getImage("id = " + imageId);

        String code = image.getCode();

        //取得根据code得到的图片文件；
        String condition = "code = '" + code + "'";
        int catalogId = image.getCatalogId();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = cataService.getCatalog(buffCondition);

        

        //更新浏览数
        String set = "hits = (hits + 1)";
        condition = "code = " + code;
        imageService.updateImage(set, condition);

        request.setAttribute("image", image);
        request.setAttribute("catalog", catalog);
        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
