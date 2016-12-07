/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class ViewSendMessagesAction extends BaseAction {
	static IMessageService messageService = ServiceFactory.createMessageService();
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UserBean loginUser = getLoginUser(request);
        if (loginUser == null) {
            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        }        
        
        
        String backTo = request.getParameter("backTo");
        if(backTo == null){
            backTo = BaseAction.INDEX_URL;
        }
        

        request.setAttribute("backTo", backTo);
        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
