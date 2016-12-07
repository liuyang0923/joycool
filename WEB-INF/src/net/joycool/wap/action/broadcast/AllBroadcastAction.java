/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action.broadcast;

import java.util.List;

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
public class AllBroadcastAction extends Action {

    static final int NUMBER_PAGE = 10;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	BroadcastServiceImpl service = (BroadcastServiceImpl)ServiceFactory.createBroadcastService();
    	String strPage = request.getParameter("page");
    	int page = 0;
    	try{
    		page = Integer.parseInt(strPage);
    	} catch(Exception e) {}
    	
        List list = service.getBroadcast(page * NUMBER_PAGE, NUMBER_PAGE);
                
        request.setAttribute("list", list);
        request.setAttribute("total", Integer.valueOf(service.getBroadcastNum()));
        request.setAttribute("page", Integer.valueOf(page));
        return mapping.findForward("success");

    }


}
