/*
 * Created on 2005-12-19
 *
 */
package net.joycool.wap.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IDiyService;
import net.joycool.wap.util.Constants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *
 */
public class DiyIndexAction extends BaseAction{
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UserBean loginUser = getLoginUser(request);
        
        //未登录
        if(loginUser == null){
            return mapping.findForward("hasNotLogin");
        }
        //已登录
        IDiyService diyService = ServiceFactory.createDiyService();
        String condition = "user_id = " + loginUser.getId() + " ORDER BY display_order";
        Vector diyList = diyService.getDiyList(condition);
        
        request.setAttribute("diyList", diyList);
        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
