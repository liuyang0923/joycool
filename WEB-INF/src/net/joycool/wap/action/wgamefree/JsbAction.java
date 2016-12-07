/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.action.wgamefree;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.wgame.JsbBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 *  
 */
public class JsbAction extends WGameAction {
    public String jsbSessionName = "jsb";
    
    public String winsSessionName = "jsbWins";

    public int NUMBER_PAGE = 10;

    /**
     * @param request
     */
    public void deal1(HttpServletRequest request) {
        String tip = null;
        String result = "success";
        //取得参数
        int wager = StringUtil.toInt(request.getParameter("wager"));
        if (wager <= 0) {
            tip = "赌注不能小于等于零！";
            result = "failure";
        }
        
        String action = request.getParameter("action");

        //有错
        if ("failure".equals(result)) {
            request.setAttribute("tip", tip);
            request.setAttribute("result", result);
            return;
        } else {
            //选择美女
            if (getSessionObject(request, winsSessionName) == null) {
                int girlId = StringUtil.toInt(request.getParameter("girlId"));
                if (girlId <= 0) {
                    girlId = 1;
                }
                WGameBean wins = new WGameBean();
                wins.setGirlId(girlId);
                setSessionObject(request, winsSessionName, wins);
            }
            
            JsbBean jsb = new JsbBean();
            jsb.setWager(wager);
            jsb.setAction(action);
            setSessionObject(request, jsbSessionName, jsb);
            request.setAttribute("result", result);
            return;
        }
    }

    /**
     * @param request
     */
    public void deal2(HttpServletRequest request) {       
        JsbBean jsb = (JsbBean) getSessionObject(request, jsbSessionName);
        if (jsb == null) {
            return;
        }

        //随机产生三个数
        int r = RandomUtil.nextInt(3) + 1;
        String systemAction = null;
        switch (r) {
        case 1:
            systemAction = "j";
            break;
        case 2:
            systemAction = "s";
            break;
        case 3:
            systemAction = "b";
            break;
        }

        String result = null;
        String tip = null;

        //平了
        if (systemAction.equals(jsb.getAction())) {
            result = "draw";
        }
        //赢了
        else if (userWin(jsb.getAction(), systemAction)) {
            WGameBean wgame = (WGameBean) getSessionObject(request,
                    winsSessionName);
            if(wgame != null){
                wgame.setWins(wgame.getWins() + 1);
            }
            result = "win";
        } else {
            request.getSession().removeAttribute(winsSessionName);
            result = "lose";
        }

        request.setAttribute("result", result);
        request.setAttribute("systemAction", systemAction);
        request.setAttribute("jsb", jsb);
        request.getSession().removeAttribute(jsbSessionName);
    }

    public String getName(String action) {
        if (action.equals("j")) {
            return "剪刀";
        } else if (action.equals("s")) {
            return "石头";
        } else if (action.equals("b")) {
            return "布";
        }
        return null;
    }

    public boolean userWin(String ua, String sa) {
        if (ua.equals("j") && sa.equals("b")) {
            return true;
        } else if (ua.equals("s") && sa.equals("j")) {
            return true;
        } else if (ua.equals("b") && sa.equals("s")) {
            return true;
        }
        return false;
    }
}
