/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action.cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CartBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Bomb
 *  
 */
public class AddCartAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	String title = request.getParameter("title");
    	String url = request.getParameter("url");
    	int userId = StringUtil.toInt(request.getParameter("userId"));
    	if(userId == -1){
    	    return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
    	} else {
    	    CartBean cart = new CartBean();
    	    cart.setUserId(userId);
    	    cart.setTitle(title);
    	    cart.setUrl(url);
    	    IUserService service = ServiceFactory.createUserService();
    	    if(!service.addCart(cart)){
    	        return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
    	    }
    	}
    	return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
