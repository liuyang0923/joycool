/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action.broadcast;

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
public class AddBroadcastAction extends Action {

    static final int NUMBER_PAGE = 3;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	String broadcaster = request.getParameter("broadcaster");
    	String msg = request.getParameter("msg");
    	if(broadcaster != null && msg != null)
    	{
    		BroadcastServiceImpl service = (BroadcastServiceImpl)ServiceFactory.createBroadcastService();
    		service.addBroadcast(broadcaster, msg);
    	}
    	
        return mapping.findForward("success");
    }


}
