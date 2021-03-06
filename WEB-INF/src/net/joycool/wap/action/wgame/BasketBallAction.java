package net.joycool.wap.action.wgame;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.BasketBallBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class BasketBallAction extends WGameAction {
	/**
	 * @author macq
	 */
	public String basketballSessionName = "basketball";

	public String winsSessionName = "basketballWins";

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
			tip = "对不起，根据篮球规则下注时将暂扣下注额两倍的乐币，你的乐币不够。";
			result = "failure";
		}else if (wager > Constants.GAME_POINT_MAX_INT) {
			 tip = "最多能下注" + Constants.GAME_POINT_MAX_INT + "乐币";
			 result = "failure";
		}
		String action = request.getParameter("action");

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

			BasketBallBean basketball = new BasketBallBean();
			basketball.setWager(wager);
			basketball.setAction(action);
			setSessionObject(request, basketballSessionName, basketball);
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
		BasketBallBean basketball = (BasketBallBean) getSessionObject(request,
				basketballSessionName);
		if (basketball == null) {
			return;
		}
		request.getSession().removeAttribute(basketballSessionName);

		// 随机产生三个数
		int r = RandomUtil.nextInt(4);
		String systemAction = null;
		switch (r) {
		case 0:
			systemAction = "l";
			break;
		case 1:
			systemAction = "m";
			break;
		case 2:
			systemAction = "r";
			break;
		case 3:
			systemAction = basketball.getAction();
			break;	
		}
		
//		if ((!systemAction.equals(basketball.getAction())) && (basketball.getWager() >= Constants.GAMEPLAYERWIN)) {
//			int rand = RandomUtil.nextInt(20) + 1;
//			if (rand % 2 == 0)
//				systemAction = basketball.getAction();
//
//		}
		
		String result = null;
		String tip = null;

		// 玩家输了
		if (systemAction.equals(basketball.getAction())) {
			//request.getSession().removeAttribute(winsSessionName);

			// 减去积分
//			fanys2006-08-11
			UserInfoUtil.updateUserCash(loginUser.getId(), -basketball.getWager(), UserCashAction.WAGER, "basketball减去乐币"+basketball.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.BASKETBALL,basketball.getWager(),Constants.SUBTRATION,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			//mcq_1_更新玩家失败后Session的乐币数和经验值  时间 2006-6-11
			updateInfo(loginUser,Constants.RANK_PK_LOSE);
			//mcq_end
			
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.FOOTBALL);
			// history.setResult(WGameHistoryBean.LOSE);
			// tip = "您输了" + football.getWager() + "个乐币!您的射门方向是"
			// + getName(football.getAction()) + ",系统的守门方向是"
			// + getName(systemAction);
			// history.setRemark(tip);
			// history.setGamePoint(football.getWager());
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_BASKETBALL);
			history.setLoseCount(1);
			history.setMoney(-basketball.getWager());
			updateHistory(history);

			result = "lose";
		}
		// 玩家赢了
		else {
			WGameBean wgame = (WGameBean) getSessionObject(request,
					winsSessionName);
			if (wgame != null) {
				wgame.setWins(wgame.getWins() + 1);
			}
			// 加上积分
			//fanys2006-08-11
			UserInfoUtil.updateUserCash(loginUser.getId(), basketball.getWager(), UserCashAction.WAGER, "basketball加上乐币"+basketball.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.BASKETBALL,basketball.getWager(),Constants.PLUS,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			//mcq_1_更新玩家胜利后Session的乐币数和经验值  时间 2006-6-11
			updateInfo(loginUser,Constants.RANK_PK_WIN);
			//mcq_end
			
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.FOOTBALL);
			// history.setResult(WGameHistoryBean.WIN);
			// tip = "您赢了" + football.getWager() + "个乐币!您的射门方向是"
			// + getName(football.getAction()) + ",系统的守门方向是"
			// + getName(systemAction);
			// history.setRemark(tip);
			// history.setGamePoint(football.getWager());
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_BASKETBALL);
			history.setWinCount(1);
			history.setMoney(basketball.getWager());
			updateHistory(history);

			result = "win";
		}

		request.setAttribute("result", result);
		request.setAttribute("systemAction", systemAction);
		request.setAttribute("basketball", basketball);
	}

	public String getAttackName(String action) {
		if (action.equals("l")) {
			return "突破上篮";
		} else if (action.equals("m")) {
			return "三分远投";
		} else if (action.equals("r")) {
			return "飞身扣篮";
		}
		return null;
	}

	public String getDefenseName(String action) {
		if (action.equals("l")) {
			return "滑步防守";
		} else if (action.equals("m")) {
			return "跃起封盖";
		} else if (action.equals("r")) {
			return "造进攻犯";
		}
		return null;
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
				WGameBean.DC_BASKETBALL);
		request.setAttribute("history", history);
	}
}
