/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.forum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.forum.ForumMessageBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IForumMessageService;
import net.joycool.wap.util.Constants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class PostAction extends BaseAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IForumMessageService service = ServiceFactory.createForumMessageService();

        UserBean loginUser = getLoginUser(request);
        String tip = null;
        boolean flag = true;
        /**
         * 取得参数
         */
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int forumId = Integer.parseInt(request.getParameter("forumId"));
        int parent = Integer.parseInt(request.getParameter("parent"));

        /**
         * 检查参数
         */
        if (title == null || title.equals("")) {
            tip = "标题不能为空！";
            flag = false;
        } else if (content == null || content.equals("")) {
            tip = "内容不能为空！";
            flag = false;
        }

        //填写信息不正确
        if (flag == false) {
            request.setAttribute("result", "failure");
            request.setAttribute("tip", tip);
            request.setAttribute("forumId", new Integer(forumId));
            request.setAttribute("parent", new Integer(parent));
        }
        //填写信息正确
        else {
            ForumMessageBean forumMessage = new ForumMessageBean();
            forumMessage.setContent(content);
            forumMessage.setForumId(forumId);
            forumMessage.setParentId(parent);
            forumMessage.setTitle(title);
            forumMessage.setUserId(loginUser.getId());
            forumMessage.setUserNickname(loginUser.getNickName());            

            if (!service.addForumMessage(forumMessage)) {
                return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
            }
            request.setAttribute("result", "success");
            request.setAttribute("forumId", new Integer(forumId));
            request.setAttribute("parent", new Integer(parent));
        }

        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
