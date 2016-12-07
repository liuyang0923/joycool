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
public class UpdateBroadcastAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		String strId = request.getParameter("id");
		String broadcaster = request.getParameter("broadcaster");
		String msg = request.getParameter("msg");
		int id = -1;
		try{
			id = Integer.parseInt(strId);
		} catch(Exception e){}
		
		if(id != -1)
		{
			BroadcastServiceImpl service = (BroadcastServiceImpl)ServiceFactory.createBroadcastService();
			service.updateBroadcast(id, broadcaster, msg);
		}
    	
        return mapping.findForward("success");
    }


}
