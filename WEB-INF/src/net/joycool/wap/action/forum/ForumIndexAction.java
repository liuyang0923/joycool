/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.forum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.util.Constants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class ForumIndexAction extends BaseAction {
    int FORUM_MESSAGE_PER_PAGE = 5;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//        IForumMessageService service = ServiceFactory
//                .createForumMessageService();
//        String condition = null;
//        IForumService forumService = ServiceFactory.createForumService();
//
//        //取得参数
//        //论坛id
//        int id = StringUtil.toInt(request.getParameter("id"));
//        //页码
//        int page = StringUtil.toInt(request.getParameter("pageIndex"));
//        if (page == -1) {
//            page = 0;
//        }
//        //order by
//        String ob = request.getParameter("ob");
//        if (ob == null || ob.equals("")) {
//            ob = "id";
//        }
//
//        /**
//         * 取得Forum
//         */
//        condition = "id = " + id;
//        ForumBean forum = forumService.getForum(condition);
//        if (forum == null) {
//            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
//        }
//
//        /**
//         * 分页相关
//         */
//        condition = "forum_id = " + id + " and parent_id = 0";
//        int totalCount = service.getForumMessageCount(condition);
//
//        int totalPageCount = totalCount / FORUM_MESSAGE_PER_PAGE;
//        if (totalCount % FORUM_MESSAGE_PER_PAGE != 0) {
//            totalPageCount++;
//        }
//        if (totalPageCount == 0) {
//            page = 0;
//        } else if (totalPageCount != 0 && page >= totalPageCount) {
//            page = totalPageCount - 1;
//        }
//        String prefixUrl = "ForumIndex.do?id=" + id + "&ob=" + ob;
//
//        /**
//         * 取得主题列表
//         */
//        condition += " ORDER BY " + ob + " DESC LIMIT " + page
//                * FORUM_MESSAGE_PER_PAGE + ", " + FORUM_MESSAGE_PER_PAGE;
//        Vector forumMessageList = service.getForumMessageList(condition);
//
//        /**
//         * 注册标签
//         */
//        request.setAttribute("totalPageCount", new Integer(totalPageCount));
//        request.setAttribute("page", new Integer(page));
//        request.setAttribute("prefixUrl", prefixUrl);
//        request.setAttribute("forum", forum);
//        request.setAttribute("forumMessageList", forumMessageList);

        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
