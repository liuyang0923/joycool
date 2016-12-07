/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.action.wgamefree;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *  
 */
public class WGameAction {
    public void setSessionObject(HttpServletRequest request, String attrName,
            Object value) {
        HttpSession session = request.getSession();
        session.setAttribute(attrName, value);
    }

    public void setApplicationObject(HttpServletRequest request,
            String attrName, Object value) {
        ServletContext ctx = request.getSession().getServletContext();
        if (ctx.getAttribute(attrName) == null) {
            ctx.setAttribute(attrName, value);
        }
    }

    public Object getSessionObject(HttpServletRequest request, String attrName) {
        HttpSession session = request.getSession();
        return session.getAttribute(attrName);
    }

//    public UserBean getLoginUser(HttpServletRequest request) {
//        return (UserBean) getSessionObject(request, Constants.LOGIN_USER_KEY);
//    }
//
//    public UserStatusBean getUserStatus(int userId) {
//        IWGameService service = ServiceFactory.createWGameService();
//        UserStatusBean status = service.getUserStatus("user_id = " + userId);
//        if (status != null) {
//            return status;
//        } else {
//            status = new UserStatusBean();
//            status.setUserId(userId);
//            status.setGamePoint(1000);
//            service.addUserStatusBean(status);
//            return status;
//        }
//    }
//
//    /**
//     * @param request
//     * @return
//     */
//    public UserStatusBean getUserStatus(HttpServletRequest request) {
//        UserBean loginUser = getLoginUser(request);
//        if (loginUser == null) {
//            return null;
//        } else {
//            return getUserStatus(loginUser.getId());
//        }
//    }

    public void getRandImg(HttpServletRequest request) {
        String fileUrl = null;
        int catalogId = StringUtil.toInt(request.getParameter("type"));
        if (catalogId == -1) {
            catalogId = 91;
        }

        DbOperation dbOp = new DbOperation();
        dbOp.init();
        String query = "select file_url from ppicture where catalog_id = "
                + catalogId + " order by rand() limit 0, 1";
        ResultSet rs = dbOp.executeQuery(query);
        try {
            if (rs.next()) {
                fileUrl = "/rep/picture/"
                        + rs.getString("file_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbOp.release();

        request.setAttribute("fileUrl", fileUrl);
    }
}
