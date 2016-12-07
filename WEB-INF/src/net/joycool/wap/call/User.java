package net.joycool.wap.call;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 用户相关
 * 
 * @author bomb
 *  
 */
public class User {

    /**
     * 在线人数
     */
    public static String onlineCount(CallParam callParam) {
        return String.valueOf(JoycoolSessionListener.getOnlineUserCount() * 10 + RandomUtil.nextInt(10));
    }
    
    /**
     * 登陆条
     */
    public static String joycoolLogin(CallParam callParam) {
    	return JoycoolSpecialUtil.getLoginMessage(callParam.request, callParam.response);
    }

    public static String nickname(CallParam callParam) {
    	HttpSession session = callParam.request.getSession(false);
    	if(session != null) {
    		UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
    		if(user != null)
    			return user.getNickNameWml();
    	}
    	return "";
    }
}