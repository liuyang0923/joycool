/*
 * Created on 2006-12-9
 *
 */
package net.wxsj.framework;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.PageUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-9
 * 
 * 说明：与乐酷的接口
 */
public class JoycoolInfc {
    public static int GAME_POINT = 1;

    public static int POINT = 2;
    
    public static int DOWNLOAD_CARD_ID = 60;

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：取得当前登陆用户
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @return
     */
    public static UserBean getLoginUser(HttpServletRequest request) {
        return (UserBean) getSessionObject(request, Constants.LOGIN_USER_KEY);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：取得当前登陆用户的用户状态
     * 
     * 参数及返回值说明：
     * 
     * @param userId
     * @param request
     * @return
     */
    public static UserStatusBean getUserStatus(int userId,
            HttpServletRequest request) {
        UserStatusBean status = UserInfoUtil.getUserStatus(userId);
        return status;
    }

    public static UserBean getUser(int userId) {
        return UserInfoUtil.getUser(userId);
    }
    
    public static String getFetchedMobile(HttpServletRequest request) {
        return (String) getSessionObject(request, "userMobile");        
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：更新用户状态
     * 
     * 参数及返回值说明：
     * 
     * @param userId
     * @param statusTypes：要更新的状态列表，如GAME_POINT，POINT
     * @param statusValues：要更新的状态的值的列表，如1000，-100
     * @param request
     * @return
     */
    public static boolean updateUserStatus(int userId, int[] statusTypes,
            int[] statusValues, HttpServletRequest request) {
        UserBean loginUser = getLoginUser(request);

        int i, count;
        count = statusValues.length;
        for (i = 0; i < count; i++) {
            if (statusTypes[i] == GAME_POINT) {
                UserInfoUtil.updateUserCash(userId, statusValues[i],
                        UserCashAction.OTHERS, "B2C实验");
            } else if (statusTypes[i] == POINT) {
                RankAction.addPoint(loginUser, statusValues[i]);
            }
        }
        return true;
    }

    public static Object getSessionObject(HttpServletRequest request,
            String attrName) {
        HttpSession session = request.getSession();
        return session.getAttribute(attrName);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：检查是否登录
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     * @param redirect
     * @return
     */
    public static boolean checkLogin(HttpServletRequest request,
            HttpServletResponse response, boolean redirect) {
        UserBean user = getLoginUser(request);
        if (user != null && user.getId() != 0) {
            return true;
        }

        if (redirect) {
            String backTo = PageUtil.getCurrentPageURL(request);
            try {
                response.sendRedirect("/user/login.jsp?backTo="
                        + URLEncoder.encode(backTo, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean checkLogin(HttpServletRequest request,
            HttpServletResponse response, boolean redirect, String backTo) {
        UserBean user = getLoginUser(request);
        if (user != null && user.getId() != 0) {
            return true;
        }

        if (redirect) {
            if (backTo == null) {
                backTo = PageUtil.getCurrentPageURL(request);
            }
            try {
                response.sendRedirect("/user/login.jsp?backTo="
                        + URLEncoder.encode(backTo, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public static DummyProductBean addUserBag(int userId, int itemId){
        return UserBagCacheUtil.addUserBagCache(userId, itemId);
    }
}
