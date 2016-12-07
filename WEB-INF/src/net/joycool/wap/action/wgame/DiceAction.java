/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.action.wgame;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.DiceBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
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
		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return;
		}
//		fanys2006-08-11
		UserStatusBean status =UserInfoUtil.getUserStatus(loginUser.getId());
//		UserStatusBean status = getUserStatus(loginUser.getId());

		String tip = null;
		String result = "success";
		// 取得参数
		int wager = StringUtil.toInt(request.getParameter("wager"));
		if (wager <= 0) {
			tip = "赌注不能小于等于零!";
			result = "failure";
		} else if (wager > status.getGamePoint()) {
			tip = "您的乐币不够了!";
			result = "failure";
		} else if (wager > Constants.GAME_POINT_MAX_INT) {
			 tip = "最多能下注" + Constants.GAME_POINT_MAX_INT + "乐币";
			 result = "failure";
		}
		int guess = StringUtil.toInt(request.getParameter("guess"));

		// 有错
		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		} else {
			// 选择美女
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
        UserBean loginUser = getLoginUser(request);
        if (loginUser == null) {
            return;
        }
        DiceBean dice = (DiceBean) getSessionObject(request, diceSessionName);
        if (dice == null) {
            return;
        }
        request.getSession().removeAttribute(diceSessionName);

        // 随机产生三个数
        int[] dices = RandomUtil.nextInts(3, 1, 7);

        int sum = dices[0] + dices[1] + dices[2];

        String result = null;
        // 赢了
        if ((sum <= 10 && dice.getGuess() == 0)
                || (sum > 10 && dice.getGuess() == 1)) {
            // 加上积分
        	//fanys2006-08-11
        	UserInfoUtil.updateUserCash(loginUser.getId(), dice.getWager(), UserCashAction.WAGER,"wgame--骰子比大小--加乐币"+dice.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.DICE,dice.getWager(),Constants.PLUS,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end

			// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser,Constants.RANK_PK_WIN);
			// mcq_end
            // 历史记录
            // WGameHistoryBean history = new WGameHistoryBean();
            // history.setUserId(loginUser.getId());
            // history.setGameId(WGameHistoryBean.DICE);
            // history.setResult(WGameHistoryBean.WIN);
            // history.setRemark("您赢了" + dice.getWager() + "个乐币!骰子" + dices[0]
            // + "、" + dices[1] + "、" + dices[2] + "点,您开了"
            // + (dice.getGuess() == 1 ? "大" : "小") + ".");
            // history.setGamePoint(dice.getWager());
            // wgService.addWGameHistory(history);
            HistoryBean history = new HistoryBean();
            history.setUserId(loginUser.getId());
            history.setGameType(WGameBean.GT_DC);
            history.setGameId(WGameBean.DC_DICE);
            history.setWinCount(1);
            history.setMoney(dice.getWager());
            updateHistory(history);

            WGameBean wgame = (WGameBean) getSessionObject(request,
                    winsSessionName);
            if (wgame != null) {
                wgame.setWins(wgame.getWins() + 1);
            }

            result = "win";
        } else {
            // 减去积分
        	UserInfoUtil.updateUserCash(loginUser.getId(), -dice.getWager(), UserCashAction.WAGER,"wagme--骰子比大小--减乐币"+dice.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.DICE,dice.getWager(),Constants.SUBTRATION,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
        	        	
			// mcq_1_更新玩家失败后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser,Constants.RANK_PK_LOSE);
			// mcq_end

            // 历史记录
            // WGameHistoryBean history = new WGameHistoryBean();
            // history.setUserId(loginUser.getId());
            // history.setGameId(WGameHistoryBean.DICE);
            // history.setResult(WGameHistoryBean.LOSE);
            // history.setRemark("您输了" + dice.getWager() + "个乐币!骰子" + dices[0]
            // + "、" + dices[1] + "、" + dices[2] + "点,您开了"
            // + (dice.getGuess() == 1 ? "大" : "小") + ".");
            // history.setGamePoint(dice.getWager());
            // wgService.addWGameHistory(history);
            HistoryBean history = new HistoryBean();
            history.setUserId(loginUser.getId());
            history.setGameType(WGameBean.GT_DC);
            history.setGameId(WGameBean.DC_DICE);
            history.setLoseCount(1);
            history.setMoney(-dice.getWager());
            updateHistory(history);

           // request.getSession().removeAttribute(winsSessionName);

            result = "win";
            result = "lose";
        }

        request.setAttribute("result", result);
        request.setAttribute("dices", dices);
        request.setAttribute("dice", dice);
    }
	/**
	 * @param request
	 */
	public void history(HttpServletRequest request) {
		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return;
		}

		HistoryBean history = getHistory(loginUser.getId(), WGameBean.GT_DC,
				WGameBean.DC_DICE);
		request.setAttribute("history", history);
	}
}
