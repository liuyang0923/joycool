/*
 * Created on 2005-11-16
 *
 */
package net.joycool.wap.action.user;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class ChooseFriendAction extends BaseAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IUserService service = ServiceFactory.createUserService();

        //取得参数
        String backTo = request.getParameter("backTo");
        if(backTo == null){
            backTo = BaseAction.INDEX_URL;
        }
        String toPage = request.getParameter("toPage");
        
        int userId;
        UserBean loginUser = getLoginUser(request);
        if (loginUser == null) {
            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        }
        userId = loginUser.getId();
        Vector userList = new Vector();
        userList = service.getFriends("user_id = " + userId);

        request.setAttribute("userList", userList);
        request.setAttribute("backTo", backTo);
        request.setAttribute("toPage", toPage);
        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
