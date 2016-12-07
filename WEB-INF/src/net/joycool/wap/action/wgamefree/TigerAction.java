/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.action.wgamefree;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.wgame.TigerBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 *  
 */
public class TigerAction extends WGameAction {
    public String tigerSessionName = "tiger";

    public String winsSessionName = "tigerWins";

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
            TigerBean tiger = new TigerBean();
            tiger.setWager(wager);
            setSessionObject(request, tigerSessionName, tiger);
            request.setAttribute("result", result);
            return;
        }
    }

    /**
     * @param request
     */
    public void deal2(HttpServletRequest request) {
        TigerBean tiger = (TigerBean) getSessionObject(request,
                tigerSessionName);
        if (tiger == null) {
            return;
        }

        //随机产生三个数
        int[] results = getResults();

        int win = getResult(results);

        tiger.setResults(results);
        tiger.setResult(win);

        String result = null;

        //赢了
        if (win > 0) {
            WGameBean wgame = (WGameBean) getSessionObject(request,
                    winsSessionName);
            if (wgame != null) {
                wgame.setWins(wgame.getWins() + 1);
            }
            result = "win";
        } else {
            request.getSession().removeAttribute(winsSessionName);
            result = "lose";
        }

        request.setAttribute("result", result);
        request.setAttribute("tiger", tiger);
        request.getSession().removeAttribute(tigerSessionName);
    }

    public int[] getResults() {
        int[] results = new int[3];
        results[0] = RandomUtil.nextInt(5) + 1;
        results[1] = RandomUtil.nextInt(5) + 1;
        results[2] = RandomUtil.nextInt(5) + 1;
        return results;
    }

    public int getResult(int[] results) {
        if (results == null || results.length != 3) {
            return 0;
        }

        int c = results[0] * results[1] * results[2];
        if (c == 125) {
            return 80;
        }
        if (c == 100) {
            return 40;
        }
        if (c == 75) {
            return 20;
        }
        if (c == 50) {
            return 10;
        }
        if (c == 25) {
            return 5;
        }
        if (c % 5 == 0) {
            return 1;
        }

        return 0;
    }
}
