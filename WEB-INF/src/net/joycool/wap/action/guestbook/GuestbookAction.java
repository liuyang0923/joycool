/*
 * Created on 2005-12-26
 *
 */
package net.joycool.wap.action.guestbook;

import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.bean.guestbook.BoardBean;
import net.joycool.wap.bean.guestbook.ContentBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IGuestbookService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class GuestbookAction extends BaseAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IGuestbookService service = ServiceFactory.createGuestbookService();

        //取得参数
        int boardId = StringUtil.toInt(request.getParameter("boardId"));
        if (boardId == -1) {
            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        }
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if (pageIndex == -1) {
            pageIndex = 0;
        }
        String backTo = request.getParameter("backTo");
        if (backTo == null || backTo.equals("")) {
            backTo = BaseAction.INDEX_URL;
        }
        //从后台过来，是WAP产品
        int jaLineId = StringUtil.toInt(request.getParameter("jaLineId"));
        if (jaLineId == -1) {
            jaLineId = 0;
        }
        //根返回节点
        String rootBackTo = null;
        JaLineBean originLine = null;
        if (jaLineId != 0) {
            IJaLineService jaLineService = ServiceFactory.createJaLineService();
            originLine = jaLineService.getLine(jaLineId);
            rootBackTo = JoycoolSpecialUtil.getRootBackTo(originLine);
        } else {
            rootBackTo = BaseAction.getBottom(request, response);
        }

        /**
         * 发表留言
         */
        if(request.getParameter("action") != null){
            String nickname = request.getParameter("nickname");
            String content = request.getParameter("content");
            if(nickname == null || nickname.equals("")){
                nickname = "访客";
            }
            if(content == null || content.equals("")){                
            } else {
                ContentBean newContent = new ContentBean();
                newContent.setBoardId(boardId);
                newContent.setContent(content);
                newContent.setNickname(nickname);
                service.addContent(newContent);
            }
        }
                
        String condition = null;

        /**
         * 取得板块
         */
        condition = "id = " + boardId;
        BoardBean board = service.getBoard(condition);
        if (board == null) {
            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        }

        /**
         * 分页相关
         */
        condition = "board_id = " + boardId;
        int totalCount = service.getContentCount(condition);

        int totalPageCount = totalCount / Constants.GUESTBOOK_ARTICLE_PER_PAGE;
        if (totalCount % Constants.GUESTBOOK_ARTICLE_PER_PAGE != 0) {
            totalPageCount++;
        }
        if (totalPageCount == 0) {
            pageIndex = 0;
        } else if (totalPageCount != 0 && pageIndex >= totalPageCount) {
            pageIndex = totalPageCount - 1;
        }
        String prefixUrl = Constants.URL_PREFIX
                + "guestbook/Guestbook.do?boardId=" + boardId + "&backTo="
                + URLEncoder.encode(backTo, "UTF-8") + "&jaLineId=" + jaLineId;

        /**
         * 取得内容列表
         */
        condition += " ORDER BY id DESC LIMIT " + pageIndex
                * Constants.GUESTBOOK_ARTICLE_PER_PAGE + ", "
                + Constants.GUESTBOOK_ARTICLE_PER_PAGE;
        Vector contentList = service.getContentList(condition);

        /**
         * 注册标签
         */
        request.setAttribute("totalPageCount", new Integer(totalPageCount));
        request.setAttribute("pageIndex", new Integer(pageIndex));
        request.setAttribute("prefixUrl", prefixUrl);
        request.setAttribute("board", board);
        request.setAttribute("contentList", contentList);
        request.setAttribute("backTo", backTo);
        request.setAttribute("rootBackTo", rootBackTo);
        
        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
