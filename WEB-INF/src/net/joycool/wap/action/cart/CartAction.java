/*
 * Created on 2006-2-20
 *
 */
package net.joycool.wap.action.cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;

/**
 * @author lbj
 *  
 */
public class CartAction {
    public static String getAddEntry(String title, String url,
            HttpServletRequest request, HttpServletResponse response) {
        //登录用户
        UserBean loginUser = (UserBean) request.getSession().getAttribute(
                Constants.LOGIN_USER_KEY);
        IUserService service = ServiceFactory.createUserService();
        //未登录
        if (loginUser == null) {
            return "";
        }
//        //已经加入收藏
//        else if (service.getCartCount("url = '" + url + "'") > 0) {
//            return "";
//        }
        //加入收藏
        else {
            StringBuffer sb = new StringBuffer();
            String postAddress = response
                    .encodeURL("/cart/AddCart.do");

            sb.append("<anchor title=\"addCart\">加入收藏");
            sb.append("<go href=\"" + postAddress + "\" method=\"post\">");
            sb.append("<postfield name=\"userId\" value=\"" + loginUser.getId()
                    + "\"/>");
            sb.append("<postfield name=\"title\" value=\"" + title + "\"/>");
            sb.append("<postfield name=\"url\" value=\"" + url + "\"/>");
            sb.append("</go>");
            sb.append("</anchor>");
            sb.append("<br/>");

            return sb.toString();
        }
    }
}
