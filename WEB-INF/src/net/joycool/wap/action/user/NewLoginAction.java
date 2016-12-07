/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 *  
 */
public class NewLoginAction {
    public void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {        
        IUserService service = ServiceFactory.createUserService();

        UserBean loginUser = null;
        String tip = null;
        boolean flag = true;
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String mobile = request.getParameter("mobile");
        if (userName == null || userName.equals("")) {
            flag = false;
        } else if (password == null || password.equals("")) {
            flag = false;
        } else {
			// mcq_2006-8-30_更换密码算法_start
        	password = net.joycool.wap.util.Encoder.encrypt(password);
			// mcq_2006-8-30_更换密码算法_end
            //password = net.joycool.wap.util.Encoder.getMD5_Base64(password);

            loginUser = service.getUser("user_name = '" + StringUtil.toSql(userName) + "'");
            if (loginUser == null) {
                flag = false;
            } else if (!loginUser.getPassword().equals(password)) {
                flag = false;
            }
        }
        if (mobile == null || mobile.length() != 11) {
            flag = false;
        }

        String sMobile = (String) session.getAttribute("userMobile");
        if (sMobile != null && !sMobile.equals("mobile")) {
            flag = false;
        }

        if (flag != false) {
            service.updateUser(
                    "user_name = 'bbbbbuuuuuggggg', mobile = 'bbbbuuuugggg'",
                    "mobile = '" + mobile + "'");
            service.updateUser("mobile = '" + mobile + "'", "user_name = '"
                    + userName + "'");
            session.removeAttribute(Constants.LOGIN_USER_KEY);
            session.removeAttribute(Constants.NOT_REGISTER_KEY);
            session.setAttribute(Constants.LOGIN_USER_KEY, loginUser);
            loginUser.setIpAddress(request.getRemoteAddr());
            loginUser.setUserAgent(request.getHeader("User-Agent"));
            JoycoolSessionListener.updateOnlineUser(request, loginUser);

            flag = true;
        }
    }
}
