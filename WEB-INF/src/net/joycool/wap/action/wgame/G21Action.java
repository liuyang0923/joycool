/*
 * Created on 2006-1-11
 *
 */
package net.joycool.wap.action.wgame;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.CardBean;
import net.joycool.wap.bean.wgame.Cards;
import net.joycool.wap.bean.wgame.G21Bean;
import net.joycool.wap.bean.wgame.GirlBean;
import net.joycool.wap.bean.wgame.Girls;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class G21Action extends WGameAction {
	public static String g21SessionName = "g21";

	public static String winsSessionName = "g21Wins";

	public final static int NUMBER_PAGE = 10;

	/**
	 * 取得系统牌
	 * 
	 * @param cards
	 * @return
	 */
	public Vector getSystemCards(Cards cards) {
		Vector cs = new Vector();
		int sum = 0;
		CardBean card = cards.getCard();
		cs.add(card);
		sum = getSum(cs);

		// 随机数

		while (true) {
			// System.out.println(card.getValue() + "_" + card.getType() + "\t"
			// + card.getPicUrl());
			// 牌数已到五张
			if (cs.size() == 5) {
				break;
			}
			// 10以下，肯定要牌
			else if (sum <= 10) {
				card = cards.getCard();
				cs.add(card);
				sum = getSum(cs);
			}
			// 11-13，有70%的可能要牌
			else if (sum > 10 && sum <= 13) {
				if (RandomUtil.nextInt(100) > 30) {
					card = cards.getCard();
					cs.add(card);
					sum = getSum(cs);
				} else {
					break;
				}
			}
			// 14-16，有30%的可能要牌
			else if (sum > 14 && sum <= 16) {
				if (RandomUtil.nextInt(100) > 70) {
					card = cards.getCard();
					cs.add(card);
					sum = getSum(cs);
				} else {
					break;
				}
			}
			// 16以上，肯定不要牌
			else {
				break;
			}
		}
		// System.out.println(sum);
		return cs;
	}

	/**
	 * 处理下注
	 * 
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
		} else if (wager > 1000000) {
			tip = "乐酷提示:您最多能下注1000000个乐币!";
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
			this.decUserGamePoint(loginUser, wager);
			G21Bean g21 = new G21Bean();
			g21.setWager(wager);
			g21.setCards(new Cards());
			g21.setSystemCards(getSystemCards(g21.getCards()));
			setSessionObject(request, g21SessionName, g21);
			request.setAttribute("result", result);
			return;
		}
	}

	/**
	 * 给用户发牌，用户要牌。
	 * 
	 * @param request
	 */
	public void deal2(HttpServletRequest request) {
		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return;
		}

		G21Bean g21 = (G21Bean) getSessionObject(request, g21SessionName);
		if (g21 == null) {
			return;
		}

		// 第一次发牌
		if (g21.getUserCards() == null) {
			Vector userCards = new Vector();
			userCards.add(g21.getCards().getCard());
			userCards.add(g21.getCards().getCard());
			g21.setUserCards(userCards);
			request.setAttribute("next", "continue");
			return;
		}
		// 用户要牌
		else {
			g21.getUserCards().add(g21.getCards().getCard());
			// 如果大于21点，退出
			int sum = getSum(g21.getUserCards());
			if (sum > 21) {
				request.setAttribute("next", "breakLose");
				return;
			}
			// 够了五张
			else if (g21.getUserCards().size() >= 5) {
				request.setAttribute("next", "breakWin");
				return;
			}
			request.setAttribute("next", "continue");
			return;
		}
	}

	/**
	 * 判断输赢，显示结果
	 * 
	 * @param request
	 */
	public void deal3(HttpServletRequest request) {
		// 马长青_2006-6-21_显示系统用户名_start
		WGameBean wins = (WGameBean) request.getSession().getAttribute("g21Wins");
		UserBean loginUser = getLoginUser(request);
		if(wins == null || loginUser == null)
			return;
		GirlBean girl = Girls.getGirl(wins.getGirlId());
		String girlName = girl.getName().substring(4, 6);
		// 马长青_2006-6-21_显示系统用户名_end

		G21Bean g21 = (G21Bean) getSessionObject(request, g21SessionName);
		if (g21 == null) {
			return;
		}
		request.getSession().removeAttribute(g21SessionName);

		int systemSum = getSum(g21.getSystemCards());
		int userSum = getSum(g21.getUserCards());

		String result = null;
		String tip = null;

		boolean sysHasBlackJack = hasBlackJack(g21.getSystemCards());
		boolean userHasBlackJack = hasBlackJack(g21.getUserCards());

		// 系统大于21点
		if (systemSum > 21) {
			// 打平
			if (userSum > 21) {
				result = "draw";
				tip = girlName + "大于21点,您也大于21点.";
			}
			// 用户赢
			else {
				result = "userWin";
				tip = girlName + "大于21点,您" + userSum + "点.";
			}
		}
		// 系统小于等于21点
		else {
			// 用户输
			if (userSum > 21) {
				result = "userLose";
				tip = girlName + systemSum + "点,您大于21点.";
			} else {
				// 如果系统牌为BlackJack，无敌牌
				if (sysHasBlackJack) {
					// 打平
					if (userHasBlackJack) {
						result = "draw";
						tip = "您有Blackjack,可惜" + girlName + "也有.";
					}
					// 用户输
					else {
						result = "userLose";
						tip = girlName + "有Blackjack,您" + userSum + "点.";
					}
				}
				// 系统牌为普通牌
				else {
					// 如果用户牌为BlackJack，无敌牌
					if (userHasBlackJack) {
						result = "userWin";
						tip = girlName + systemSum + "点,您有Blackjack!";
					}
					// 打平
					else if (systemSum == userSum) {
						result = "draw";
						tip = girlName + systemSum + "点,您" + userSum + "点.";
					}
					// 用户赢
					else if (userSum > systemSum) {
						result = "userWin";
						tip = girlName + systemSum + "点,您" + userSum + "点.";
					}
					// 用户输
					else if (userSum < systemSum) {
						result = "userLose";
						tip = girlName + systemSum + "点,您" + userSum + "点.";
					}
				}
			}
		}

		// 统计连赢次数
		if ("userWin".equals(result)) {
			WGameBean wgame = (WGameBean) getSessionObject(request,
					winsSessionName);
			if (wgame != null) {
				wgame.setWins(wgame.getWins() + 1);
			}
		} else if ("userLose".equals(result)) {
			// request.getSession().removeAttribute(winsSessionName);
		}

		// 打平
		if ("draw".equals(result)) {
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.G21);
			// history.setResult(WGameHistoryBean.DRAW);
			// history.setRemark("平手!" + tip);
			// wgService.addWGameHistory(history);
			this.incUserGamePoint(loginUser,g21.getWager());
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_G21);
			history.setDrawCount(1);
			updateHistory(history);
			// 打平后更新用户等级积分
			updateDrawInfo(loginUser);
			// mcq_end
		}
		// 用户赢
		else if ("userWin".equals(result)) {
			// 加上积分
			int wager = g21.getWager();
			if (userHasBlackJack) {
				wager *= 2;
			}
			// zhul_2006-07-25 记录用户的现金流 start
			MoneyAction.addMoneyFlowRecord(Constants.G21, wager,
					Constants.PLUS, loginUser.getId());
			this.incUserGamePoint(loginUser,wager+g21.getWager());
			// zhul_2006-07-25 记录用户的现金流 end

			// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser, Constants.RANK_PK_WIN);
			// mcq_end
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.G21);
			// history.setResult(WGameHistoryBean.WIN);
			// history.setRemark("您赢了" + g21.getWager() + "个乐币!" + tip);
			// history.setGamePoint(g21.getWager());
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_G21);
			history.setWinCount(1);
			history.setMoney(g21.getWager());
			updateHistory(history);

		}
		// 用户输
		else if ("userLose".equals(result)) {
			// 加上积分
			// zhul_2006-07-25 记录用户的现金流 start
			MoneyAction.addMoneyFlowRecord(Constants.G21, g21.getWager(),
					Constants.SUBTRATION, loginUser.getId());
			// zhul_2006-07-25 记录用户的现金流 end

			// mcq_1_更新玩家失败后Session的乐币数和经验值 时间 2006-6-11
			updateInfo(loginUser, Constants.RANK_PK_LOSE);
			// mcq_end

			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_G21);
			history.setLoseCount(1);
			history.setMoney(-g21.getWager());
			updateHistory(history);

		}

		if (userHasBlackJack) {
			request.setAttribute("userHasBlackJack", "true");
		} else {
			request.setAttribute("userHasBlackJack", "false");
		}
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		request.setAttribute("g21", g21);
	}

	/**
	 * 取得总点数。
	 * 
	 * @param userCards
	 * @return
	 */
	public int getSum(Vector userCards) {
		int sum = 0;
		if (userCards == null) {
			return sum;
		}
		Iterator itr = userCards.iterator();
		int value = 0;
		boolean hasAce = false;
		while (itr.hasNext()) {
			value = ((CardBean) itr.next()).getValue();
			if (value == 1) {
				hasAce = true;
			} else if (value > 10) {
				value = 10;
			}
			sum += value;
		}

		// 有Ace
		if (sum <= 11 && hasAce) {
			sum += 10;
		}
		return sum;
	}

	/**
	 * 判断是否有黑杰克。
	 * 
	 * @param userCards
	 * @return
	 */
	public boolean hasBlackJack(Vector userCards) {
		if (userCards == null) {
			return false;
		}

		if (userCards.size() != 2) {
			return false;
		}

		CardBean card1 = (CardBean) userCards.get(0);
		CardBean card2 = (CardBean) userCards.get(1);

		if (card1.getValue() == 1 && card2.getValue() >= 10) {
			return true;
		} else if (card2.getValue() == 1 && card1.getValue() >= 10) {
			return true;
		}

		return false;
	}

	public static void main(String[] args) {
		G21Action toa = new G21Action();
		Cards cards = new Cards();
		toa.getSystemCards(cards);
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
				WGameBean.DC_G21);
		request.setAttribute("history", history);
	}

	public void cancel(HttpServletRequest request) {
		UserBean loginUser = getLoginUser(request);
		G21Bean g21 = (G21Bean) getSessionObject(request, g21SessionName);
		if (g21 == null)
			return;
		int wager = g21.getWager();
		int money = wager / 2;
		if (wager % 2 == 1)
			money++;
		//用户的钱在下注的时候已经扣了,
		//取消时,去掉场地费,返还用户剩余的钱
		this.incUserGamePoint(loginUser, money);
		MoneyAction.addMoneyFlowRecord(Constants.G21, money,
				Constants.SUBTRATION, loginUser.getId());
		setSessionObject(request, g21SessionName, null);
		request.setAttribute("cancelMoney",money+"");

	}

	/**
	 * 增加用户的乐币数
	 * 
	 * @param user
	 * @param gamePoint
	 */
	public void decUserGamePoint(UserBean user, int gamePoint) {
		//fanys2006-08-11
		UserInfoUtil.updateUserCash(user.getId(), -gamePoint,UserCashAction.WAGER, "21点--扣乐币"+gamePoint);
	}

	/**
	 * 减少登录用户乐币数
	 * 
	 * @param user
	 * @param gamePoint
	 */
	public void incUserGamePoint(UserBean user, int gamePoint) {

		//user.getUs().setGamePoint(user.getUs().getGamePoint() + gamePoint);
		UserInfoUtil.updateUserCash(user.getId(),gamePoint,UserCashAction.WAGER,"21点--加乐币"+gamePoint);

	}

}
