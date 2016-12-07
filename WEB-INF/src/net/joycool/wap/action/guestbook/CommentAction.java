/*
 * Created on 2006-2-20
 *
 */
package net.joycool.wap.action.guestbook;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.guestbook.BoardBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IGuestbookService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *  
 */
public class CommentAction {
    public static String getCommentEntry(int type, int id, String name,
            String backTo, HttpServletResponse response) {
        int boardId = type * 100000000 + id;
        IGuestbookService service = ServiceFactory.createGuestbookService();
        BoardBean board = service.getBoard("id = " + boardId);
        int commentCount = 0;
        String title = "相关评论";
        if (board == null) {
            DbOperation dbOp = new DbOperation();
            dbOp.init();
            String query = "insert into sp_guestbook_board(id, name) values("
                    + boardId + ", '" + StringUtil.toSql(name) + "')";
            dbOp.executeUpdate(query);
            dbOp.release();
        } else {
            commentCount = service.getContentCount("board_id = " + boardId);
            title += "（" + commentCount + "条）";
        }
        title += "|发表";
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"" + ("/guestbook/Guestbook.do?boardId="
                		+ boardId + "&amp;backTo=" + backTo) + "\" title=\"go\">");
        sb.append(title);
        sb.append("</a>");
        return sb.toString();
    }
}
