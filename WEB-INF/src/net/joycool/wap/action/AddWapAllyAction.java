/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.BroadcastServiceImpl;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Bomb
 *  
 */
public class AddWapAllyAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	String title = request.getParameter("title");
    	String url = request.getParameter("url");
    	String contact = request.getParameter("contact");
    	String name = request.getParameter("name");
    	
    	BroadcastServiceImpl service = (BroadcastServiceImpl)ServiceFactory.createBroadcastService();
    	boolean ret = service.addWapAlly(name, title, url, contact);
    	if(!ret)
    	{
    		request.setAttribute("error", "error");
            return mapping.findForward("failure");
    	}
        
        return mapping.findForward("success");

    }


}
