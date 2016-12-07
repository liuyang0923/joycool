package net.joycool.wap.action.wgame;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.DiceDXBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class DiceDXAction extends WGameAction {
	public String dicedxSessionName = "dicedx";

	public String winsSessionName = "dicedxWins";

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
			DiceDXBean diceDX = new DiceDXBean();
			diceDX.setWager(wager);
			setSessionObject(request, dicedxSessionName, diceDX);
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
		DiceDXBean diceDX = (DiceDXBean) getSessionObject(request,
				dicedxSessionName);
		if (diceDX == null) {
			return;
		}
		request.getSession().removeAttribute(dicedxSessionName);

		// 随机产生玩家的三个股子
		int[] diceDXs = RandomUtil.nextInts(3, 1, 7);
		// 随机产生系统的三个股子
		int[] diceDXs1 = RandomUtil.nextInts(3, 1, 7);

		int sum = diceDXs[0] + diceDXs[1] + diceDXs[2];
		int sum1 = diceDXs1[0] + diceDXs1[1] + diceDXs1[2];

		String result = null;
		String tip = null;
		// 赢了
		if (sum > sum1) {
			// 加上积分
			//fanys2006-08-11
			UserInfoUtil.updateUserCash(loginUser.getId(), diceDX.getWager(), UserCashAction.WAGER,"DiceDX--骰子比大小--加上乐币"+diceDX.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.DICEDX,diceDX.getWager(),Constants.PLUS,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser, Constants.RANK_PK_WIN);
			// mcq_end

			// 历史记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.DICEDX);
			// history.setResult(WGameHistoryBean.WIN);
			// history.setRemark("您赢了" + diceDX.getWager() + "个乐币!骰子是" +
			// diceDXs[0]
			// + "、" + diceDXs[1] + "、" + diceDXs[2] + ",点数为"+sum+".");
			// history.setGamePoint(diceDX.getWager());
			// wgService.addWGameHistory(history);
			//
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_DICEDX);
			history.setWinCount(1);
			history.setMoney(diceDX.getWager());
			updateHistory(history);

			WGameBean wgame = (WGameBean) getSessionObject(request,
					winsSessionName);
			if (wgame != null) {
				wgame.setWins(wgame.getWins() + 1);
			}

			result = "win";
			result = "win";
		} else if (sum < sum1) {
			// 减去积分
			//fanys2006-08-11
			UserInfoUtil
			.updateUserCash(loginUser.getId(), -diceDX.getWager(), UserCashAction.WAGER,"DiceDx--骰子比大小--减乐币"+diceDX.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.DICEDX,diceDX.getWager(),Constants.SUBTRATION,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			// mcq_1_更新玩家失败后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser, Constants.RANK_PK_LOSE);
			// mcq_end
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_DICEDX);
			history.setLoseCount(1);
			history.setMoney(-diceDX.getWager());
			updateHistory(history);

			// request.getSession().removeAttribute(winsSessionName);

			result = "win";
			result = "lose";
		} else {
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_DICEDX);
			history.setDrawCount(1);
			updateHistory(history);
			// 打平后更新用户等级积分
			updateDrawInfo(loginUser);
			// mcq_end
			result = "win";
			result = "draw";
		}

		request.setAttribute("result", result);
		request.setAttribute("diceDXs", diceDXs);
		request.setAttribute("diceDXs1", diceDXs1);
		request.setAttribute("diceDX", diceDX);
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
				WGameBean.DC_DICEDX);
		request.setAttribute("history", history);
	}
}
