/*
 * Created on 2005-12-2
 *
 */
package net.joycool.wap.action.image;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.image.ImageBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IImageService;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *
 */
public class ImageDownLoadAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IImageService imageService = ServiceFactory.createImageService();
        String code = request.getParameter("code");
        // 取得根据code得到的图片文件；
        String condition = "code = '" + code + "'";
        ImageBean image = imageService.getImage(condition);
        request.setAttribute("image",image);
        return mapping.findForward("success");
    }

}
