/*
 * Created on  20067-4-10
 *
 */
package net.joycool.wap.action.wgame;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.LgjBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author Liq
 * 
 */
public class LgjAction extends WGameAction {
	public String lgjSessionName = "lgj";

	public String winsSessionName = "lgjWins";

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

			LgjBean lgj = new LgjBean();
			lgj.setWager(wager);
			lgj.setAction(action);
			setSessionObject(request, lgjSessionName, lgj);
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
		LgjBean lgj = (LgjBean) getSessionObject(request, lgjSessionName);
		if (lgj == null) {
			return;
		}
		request.getSession().removeAttribute(lgjSessionName);

		// 随机产生三个数
		int r = RandomUtil.nextInt(4) + 1;
		String systemAction = null;
		switch (r) {
		case 1:
			systemAction = "a";
			break;
		case 2:
			systemAction = "b";
			break;
		case 3:
			systemAction = "c";
			break;
		case 4:
			systemAction = "d";
			break;
		}

		String result = null;
		String tip = null;
		// mcq_2006-6-20_判断用户下注十万以上,系统生成结果是否和用户下注结果一致_start
		if (lgj.getWager() >= Constants.GAMEPLAYERWIN) {
			for (int i = 0; i < Constants.LOOK_COUNT; i++) {
				if (userWin(lgj.getAction(), systemAction)) {
					// 随机产生三个数
					 r = RandomUtil.nextInt(3) + 1;
					switch (r) {
					case 1:
						systemAction = "a";
						break;
					case 2:
						systemAction = "b";
						break;
					case 3:
						systemAction = "c";
						break;
					case 4:
						systemAction = "d";
						break;
					}
					continue;
				}
				break;
			}
		}
//		// mcq_2006-6-20_判断用户下注十万以上,系统生成结果是否和用户下注结果一致_end
//System.out.println(lgj.getAction());
//System.out.println(systemAction);
		// 平了   杠子>老虎>鸡>虫子>杠子
		if (systemAction.equals(lgj.getAction())
				||((systemAction.equals("a"))&&(lgj.getAction().equals("c")))
				||((systemAction.equals("c"))&&(lgj.getAction().equals("a")))
				||((systemAction.equals("b"))&&(lgj.getAction().equals("d")))
				||((systemAction.equals("d"))&&(lgj.getAction().equals("b")))
				) {
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.JSB);
			// history.setResult(WGameHistoryBean.DRAW);
			// tip = "平手!你们出的都是" + getName(systemAction);
			// history.setRemark(tip);
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_LGJ);
			history.setDrawCount(1);
			updateHistory(history);
			// 打平后更新用户等级积分
			updateDrawInfo(loginUser);
			// mcq_end
			result = "draw";
		}
		// 赢了
		else if (userWin(lgj.getAction(), systemAction)) {
			WGameBean wgame = (WGameBean) getSessionObject(request,
					winsSessionName);
			if (wgame != null) {
				wgame.setWins(wgame.getWins() + 1);
			}

			// 加上积分
			//fanys2006-08-11
			UserInfoUtil.updateUserCash(loginUser.getId(),lgj.getWager(),UserCashAction.WAGER,"wgame--老虎杠子鸡--加乐币"+lgj.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.LGJ,lgj.getWager(),Constants.PLUS,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser, Constants.RANK_PK_WIN);
			// mcq_end
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.JSB);
			// history.setResult(WGameHistoryBean.WIN);
			// tip = "您赢了" + jsb.getWager() + "个乐币!您出的是"
			// + getName(jsb.getAction()) + ",系统出的是"
			// + getName(systemAction);
			// history.setRemark(tip);
			// history.setGamePoint(jsb.getWager());
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_LGJ);
			history.setWinCount(1);
			history.setMoney(lgj.getWager());
			updateHistory(history);
			result = "win";
		} else {
			// request.getSession().removeAttribute(winsSessionName);

			// 减去积分
//			fanys2006-08-11
			UserInfoUtil.updateUserCash(loginUser.getId(),-lgj.getWager(),UserCashAction.WAGER,"wgame--老虎杠子鸡--减乐币"+ lgj.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.LGJ,lgj.getWager(),Constants.SUBTRATION,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser, Constants.RANK_PK_LOSE);
			// mcq_end
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.JSB);
			// history.setResult(WGameHistoryBean.LOSE);
			// tip = "您输了" + jsb.getWager() + "个乐币!您出的是"
			// + getName(jsb.getAction()) + ",系统出的是"
			// + getName(systemAction);
			// history.setRemark(tip);
			// history.setGamePoint(jsb.getWager());
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_LGJ);
			history.setLoseCount(1);
			history.setMoney(-lgj.getWager());
			updateHistory(history);

			result = "lose";
		}

		request.setAttribute("result", result);
		request.setAttribute("systemAction", systemAction);
		request.setAttribute("lgj", lgj);
	}

	public String getName(String action) {
		if (action.equals("a")) {
			return "杠子";
		} else if (action.equals("b")) {
			return "老虎";
		} else if (action.equals("c")) {
			return "鸡";
		} else if (action.equals("d")) {
			return "虫子";
		}
		return null;
	}

	public boolean userWin(String ua, String sa) {
		if (ua.equals("a") && sa.equals("b")) {
			return true;
		} else if (ua.equals("b") && sa.equals("c")) {
			return true;
		} else if (ua.equals("c") && sa.equals("d")) {
			return true;
		} else if (ua.equals("d") && sa.equals("a")) {
			return true;
		}
		return false;
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
				WGameBean.DC_LGJ);
		request.setAttribute("history", history);
	}
}
