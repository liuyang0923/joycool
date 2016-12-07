/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.action.broadcast;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.broadcast.BStatusBean;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Bomb
 *  
 */
public class UpdateBStatusAction extends Action {

    static final int NUMBER_PAGE = 3;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	String line1 = request.getParameter("line1");
    	String line2 = request.getParameter("line2");
    	if(line1 != null && line2 != null)
    	{
    		BStatusBean.line1 = line1;
    		BStatusBean.line2 = line2;
    	}
    	
        return mapping.findForward("success");
    }


}
