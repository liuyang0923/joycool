/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action.broadcast;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.broadcast.BroadcastBean;
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
public class AdminAction extends Action {

    static final int NUMBER_PAGE = 10;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	BroadcastServiceImpl service = (BroadcastServiceImpl)ServiceFactory.createBroadcastService();

    	String action = request.getParameter("action");
    	if(action != null)
    	{
    		String strId = request.getParameter("id");
    		int id = -1;
    		try{
    			id = Integer.parseInt(strId);
    		} catch(Exception e){}
    		
    		if(id != -1)
    		{
        		if(action.equals("d"))
        			service.deleteBroadcast(id);
        		else if(action.equals("u"))
        		{
        	        BroadcastBean broadcast = service.getBroadcast(id);
        	        request.setAttribute("broadcast", broadcast);
        	        return mapping.findForward("update");
        		}
    		}

    	}
    	
        List list = service.getBroadcast(0, NUMBER_PAGE);

        request.setAttribute("list", list);
        
        list = service.getBroadcast(0, 1);
        BroadcastBean broadcast = (BroadcastBean)list.get(0);
        if(broadcast == null)
        	broadcast = new BroadcastBean();
        
        request.setAttribute("broadcast", broadcast);
    	
        return mapping.findForward("success");
    }


}
