package jc.guest.wgame;

import javax.servlet.http.HttpServletRequest;

import jc.guest.GuestHallAction;
import jc.guest.GuestUserInfo;

import net.joycool.wap.bean.wgame.DiceDXBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

public class DiceDXAction extends WGameAction {
	public String dicedxSessionName = "dicedx";

	public String winsSessionName = "dicedxWins";

	public int NUMBER_PAGE = 10;

	/**
	 * @param request
	 */
	public void deal1(HttpServletRequest request,GuestUserInfo guestUser) {
		if (guestUser == null) {
			return;
		}
//		fanys2006-08-11
//		UserStatusBean status = getUserStatus(loginUser.getId());

		String tip = null;
		String result = "success";
		// 取得参数
		int wager = StringUtil.toInt(request.getParameter("wager"));
		if (wager <= 0) {
			tip = "赌注不能小于等于零!";
			result = "failure";
		} else if (wager > guestUser.getMoney()) {
			tip = "您的游币不够了!";
			result = "failure";
		} else if (wager > GuestHallAction.MAX_MONEY) {
			 tip = "最多能下注" + GuestHallAction.MAX_MONEY + "游币";
			 result = "failure";
		}
//		int guess = StringUtil.toInt(request.getParameter("guess"));

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
	public void deal2(HttpServletRequest request,GuestUserInfo guestUser) {
		if (guestUser == null) {
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
		// 赢了
		if (sum > sum1) {
			// 加钱
			GuestHallAction.updateMoney(guestUser.getId(), diceDX.getWager());
			HistoryBean history = new HistoryBean();
			history.setUserId(guestUser.getId());
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
			// 减钱
			GuestHallAction.updateMoney(guestUser.getId(), 0-diceDX.getWager());
			HistoryBean history = new HistoryBean();
			history.setUserId(guestUser.getId());
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
			history.setUserId(guestUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_DICEDX);
			history.setDrawCount(1);
			updateHistory(history);
			// 打平后更新用户等级积分
//			updateDrawInfo(guestUser);
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
	public void history(HttpServletRequest request,GuestUserInfo guestUser) {
		if (guestUser == null) {
			return;
		}
		HistoryBean history = getHistory(guestUser.getId(), WGameBean.GT_DC,
				WGameBean.DC_DICEDX);
		request.setAttribute("history", history);
	}
}
