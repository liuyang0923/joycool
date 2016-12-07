/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.MessageBean;
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
public class ViewMessagesAction extends BaseAction {
	static IMessageService messageService = ServiceFactory.createMessageService();
    final static int MESSAGE_PER_PAGE = 5;
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UserBean loginUser = getLoginUser(request);
        if (loginUser == null) {
            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        }

        //删除消息
        if(request.getParameter("delete") != null){
            int messageId = StringUtil.toInt(request.getParameter("messageId"));
            if(messageId > 0)
            	messageService.updateMessage("flag=1","id = " + messageId);
        }
        //取得参数
//        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));        
//        if (pageIndex == -1) {
//            pageIndex = StringUtil.toInt(request.getParameter("pageIndex1"));  
//            if(pageIndex == -1){
//                pageIndex = 0;
//            } else {
//                pageIndex = pageIndex - 1;
//            }
//        }
//        if(pageIndex < 0 ){
//            pageIndex = 0;
//        }
        String backTo = request.getParameter("backTo");
        if(backTo == null){
            backTo = BaseAction.INDEX_URL;
        }

        //String condition = "to_user_id = " + loginUser.getId() +" and flag != 1";
//        int totalCount = messageService.getMessageCount(condition);
//
//        int totalPageCount = totalCount / MESSAGE_PER_PAGE;
//        if (totalCount % MESSAGE_PER_PAGE != 0) {
//            totalPageCount++;
//        }
//        if (totalPageCount == 0) {
//            pageIndex = 0;
//        } else if (totalPageCount != 0 && pageIndex >= totalPageCount) {
//            pageIndex = totalPageCount - 1;
//        }

//        condition += " ORDER BY id DESC LIMIT " + pageIndex
//                * MESSAGE_PER_PAGE + ", "
//                + MESSAGE_PER_PAGE;
//        Vector messageList = messageService.getMessageList(condition);

//        if (messageList.size() > 0) {
//            condition = "to_user_id = " + loginUser.getId() + " AND mark = 0 AND id <= "
//                    + ((MessageBean) messageList.get(0)).getId()
//                    + " AND id >="
//                    + ((MessageBean) messageList.get(messageList.size() - 1))
//                            .getId();
//            String set = "mark = 1";
//            messageService.updateMessage(set, condition);
//        }

//        request.setAttribute("messageList", messageList);
        request.setAttribute("backTo", backTo);
//        request.setAttribute("totalCount", new Integer(totalCount));
//        request.setAttribute("totalPageCount", new Integer(totalPageCount));
//      request.setAttribute("pageIndex", new Integer(pageIndex));
        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
