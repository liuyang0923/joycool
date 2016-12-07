/*
 * Created on 2005-10-10
 *
 */
package net.joycool.wap.framework;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.Constants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author lbj
 *  
 */
public class MyExceptionHandler extends ExceptionHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.ExceptionHandler#execute(java.lang.Exception,
     *      org.apache.struts.config.ExceptionConfig,
     *      org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(Exception exception, ExceptionConfig ec,
            ActionMapping am, ActionForm af, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        exception.printStackTrace();
        
        return am.findForward(Constants.SYSTEM_FAILURE_KEY);
    }

    public static void main(String[] args) {
    }
}
