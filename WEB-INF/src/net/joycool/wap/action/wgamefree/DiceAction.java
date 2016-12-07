/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.action.wgamefree;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.wgame.DiceBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 *  
 */
public class DiceAction extends WGameAction {
    public String diceSessionName = "dice";

    public String winsSessionName = "diceWins";

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

        int guess = StringUtil.toInt(request.getParameter("guess"));

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
            
            DiceBean dice = new DiceBean();
            dice.setWager(wager);
            dice.setGuess(guess);
            setSessionObject(request, diceSessionName, dice);
            request.setAttribute("result", result);
            return;
        }
    }

    /**
     * @param request
     */
    public void deal2(HttpServletRequest request) {
        DiceBean dice = (DiceBean) getSessionObject(request, diceSessionName);
        if (dice == null) {
            return;
        }

        //随机产生三个数
        int[] dices = new int[3];
        dices[0] = ((RandomUtil.nextInt(100) + 1)) % 6 + 1;
        dices[1] = ((RandomUtil.nextInt(100) + 1)) % 6 + 1;
        dices[2] = ((RandomUtil.nextInt(100) + 1)) % 6 + 1;

        int sum = dices[0] + dices[1] + dices[2];

        String result = null;

        //赢了
        if ((sum <= 10 && dice.getGuess() == 0)
                || (sum > 10 && dice.getGuess() == 1)) {
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
        request.setAttribute("dices", dices);
        request.setAttribute("dice", dice);
        request.getSession().removeAttribute(diceSessionName);
    }
}
