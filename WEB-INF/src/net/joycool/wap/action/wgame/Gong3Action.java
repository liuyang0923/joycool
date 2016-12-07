package net.joycool.wap.action.wgame;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.CardBean;
import net.joycool.wap.bean.wgame.Cards;
import net.joycool.wap.bean.wgame.Gong3Bean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class Gong3Action extends WGameAction {
	/**
	 * @author macq
	 */
	public String gong3SessionName = "gong3";

	public String winsSessionName = "gong3Wins";

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
		} else if (wager * 3 > status.getGamePoint()) {
			tip = "您的乐币不够了，身上的钱至少是三倍赌注!";
			result = "failure";
		} else if (wager > Constants.GAME_POINT_MAX_INT) {
			 tip = "最多能下注" + Constants.GAME_POINT_MAX_INT + "乐币";
			 result = "failure";
		}

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
			Gong3Bean gong3 = new Gong3Bean();
			gong3.setWager(wager);
			setSessionObject(request, gong3SessionName, gong3);
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
		Gong3Bean gong3 = (Gong3Bean) getSessionObject(request,
				gong3SessionName);
		if (gong3 == null) {
			return;
		}
		request.getSession().removeAttribute(gong3SessionName);

		// 随机产生三张牌
		Vector cardList = new Vector();
		Cards cards = new Cards();
		cardList.add(cards.getCard());
		cardList.add(cards.getCard());
		cardList.add(cards.getCard());
		CardBean card1 = (CardBean) cardList.get(0);
		int a = card1.getValue();
		int aa = card1.getType();
		CardBean card2 = (CardBean) cardList.get(1);
		int b = card2.getValue();
		int bb = card2.getType();
		CardBean card3 = (CardBean) cardList.get(2);
		int c = card3.getValue();
		int cc = card3.getType();
		card1 = new CardBean(a, aa, "http://wap.joycool.net/wgame/cardImg/" + a
				+ "_" + aa + ".gif");
		card2 = new CardBean(b, bb, "http://wap.joycool.net/wgame/cardImg/" + b
				+ "_" + bb + ".gif");
		card3 = new CardBean(c, cc, "http://wap.joycool.net/wgame/cardImg/" + c
				+ "_" + cc + ".gif");
		cardList.add(card1);
		cardList.add(card2);
		cardList.add(card3);
		// 生成挑战者的牌（从52张牌里去处庄家的3张牌）放到一个Vector中
		Vector cardList1 = new Vector();
		Cards cardsa = new Cards();
		CardBean cardb = null;
		int count = 0;
		for (int i = 0; i < 6; i++) {
			cardb = cardsa.getCard();
			if (cardb.compareTo(card1) == 0 || cardb.compareTo(card2) == 0
					|| cardb.compareTo(card3) == 0) {
				continue;
			} else
				cardList1.add(cardb);
			count++;
			if (count == 3) {
				break;
			}
		}

		CardBean card4 = (CardBean) cardList1.get(0);
		int d = card4.getValue();
		int dd = card4.getType();
		CardBean card5 = (CardBean) cardList1.get(1);
		int e = card5.getValue();
		int ee = card5.getType();
		CardBean card6 = (CardBean) cardList1.get(2);
		int f = card6.getValue();
		int ff = card6.getType();

		/**
		 * 判断输赢
		 */
		// 得到庄家和挑战者的牌型
		int banker = this.getCardsType(cardList);
		int dare = this.getCardsType(cardList1);
		int winUserId = 0;
		int bankerTotal = 0;
		int dareTotal = 0;
		if (banker == 1 && dare == 1) {
			int value1 = (card1.getValue() >= 11 ? 0 : card1.getValue());
			int value2 = (card2.getValue() >= 11 ? 0 : card2.getValue());
			int value3 = (card3.getValue() >= 11 ? 0 : card3.getValue());
			int value4 = (card4.getValue() >= 11 ? 0 : card4.getValue());
			int value5 = (card5.getValue() >= 11 ? 0 : card5.getValue());
			int value6 = (card6.getValue() >= 11 ? 0 : card6.getValue());
			int bankerCount = value1 + value2 + value3;
			bankerTotal = bankerCount % 10;
			int dareCount = value4 + value5 + value6;
			dareTotal = dareCount % 10;
		}
		// mcq_2006-6-20_判断用户下注十万以上,系统生成结果是否和用户下注结果一致_start
		if (gong3.getWager() >= Constants.GAMEPLAYERWIN) {
			for (int i = 0; i < Constants.LOOK_COUNT; i++) {
				if (bankerTotal > dareTotal || banker > dare) {
					// 生成挑战者的牌（从52张牌里去处庄家的3张牌）放到一个Vector中
					cardList1 = new Vector();
					cardsa = new Cards();
					cardb = null;
					count = 0;
					for (int j = 0; j < 6; j++) {
						cardb = cardsa.getCard();
						if (cardb.compareTo(card1) == 0 || cardb.compareTo(card2) == 0
								|| cardb.compareTo(card3) == 0) {
							continue;
						} else
							cardList1.add(cardb);
						count++;
						if (count == 3) {
							break;
						}
					}
					card4 = (CardBean) cardList1.get(0);
					d = card4.getValue();
					dd = card4.getType();
					card5 = (CardBean) cardList1.get(1);
					e = card5.getValue();
					ee = card5.getType();
					card6 = (CardBean) cardList1.get(2);
					f = card6.getValue();
					ff = card6.getType();

					// 得到庄家和挑战者的牌型
					banker = this.getCardsType(cardList);
					dare = this.getCardsType(cardList1);
					winUserId = 0;
					bankerTotal = 0;
					dareTotal = 0;
					if (banker == 1 && dare == 1) {
						int value1 = (card1.getValue() >= 11 ? 0 : card1.getValue());
						int value2 = (card2.getValue() >= 11 ? 0 : card2.getValue());
						int value3 = (card3.getValue() >= 11 ? 0 : card3.getValue());
						int value4 = (card4.getValue() >= 11 ? 0 : card4.getValue());
						int value5 = (card5.getValue() >= 11 ? 0 : card5.getValue());
						int value6 = (card6.getValue() >= 11 ? 0 : card6.getValue());
						int bankerCount = value1 + value2 + value3;
						bankerTotal = bankerCount % 10;
						int dareCount = value4 + value5 + value6;
						dareTotal = dareCount % 10;
					}
					continue;
				}
				break;
			}
		}
		// mcq_2006-6-20_判断用户下注十万以上,系统生成结果是否和用户下注结果一致_end
//		fanys2006-08-11
		UserStatusBean userStatus =UserInfoUtil.getUserStatus(loginUser.getId());
		int uPoint = userStatus.getGamePoint();
		String result = null;
		String tip = null;
		int flag = 0;
		if (bankerTotal > dareTotal) {
			// 庄家加上乐币
			// 8点以上乘2
			if (bankerTotal >= 8) {
				int wager = gong3.getWager();
				flag = 2;
				int changeWager = wager * flag;
				gong3.setWager(changeWager);
				result = "win";
			} else {
				int wager = gong3.getWager();
				flag = 1;
				int changeWager = wager * flag;
				gong3.setWager(wager);
				result = "win";
				// 8点以下乘1
			}

		} else if (banker > dare) {
			// 庄家加上乐币
			// JQK三条乘以5
			if (banker == 4) {
				int wager = gong3.getWager();
				flag = 5;
				int changeWager = wager * flag;
				gong3.setWager(changeWager);
				result = "win";
			} else if (banker == 3) {
				// 12345678910三条乘以4
				int wager = gong3.getWager();
				flag = 4;
				int changeWager = wager * flag;
				gong3.setWager(changeWager);
				result = "win";
			} else if (banker == 2) {
				// 混三条乘以3
				int wager = gong3.getWager();
				flag = 3;
				int changeWager = wager * flag;
				gong3.setWager(changeWager);
				result = "win";
			}
		}
		// 庄家输
		else if (bankerTotal < dareTotal) {
			// 8点以上乘2
			if (dareTotal >= 8) {
				// 庄家减去乐币
				int wager = gong3.getWager();
				flag = 2;
				int changeWager = (wager * flag > uPoint ? uPoint : wager
						* flag);
				gong3.setWager(changeWager);
				result = "lose";
			} else {
				// 8点以下乘1
				int wager = gong3.getWager();
				flag = 1;
				int changeWager = (wager * flag > uPoint ? uPoint : wager
						* flag);
				gong3.setWager(changeWager);
				result = "lose";
			}

		} else if (banker < dare) {
			// 庄家减去乐币
			// JQK三条乘以5
			if (dare == 4) {
				int wager = gong3.getWager();
				flag = 5;
				int changeWager = (wager * flag > uPoint ? uPoint : wager
						* flag);
				gong3.setWager(changeWager);
				result = "lose";
			}// 12345678910三条乘以4
			if (dare == 3) {
				int wager = gong3.getWager();
				flag = 4;
				int changeWager = (wager * flag > uPoint ? uPoint : wager
						* flag);
				gong3.setWager(changeWager);
				result = "lose";
			}// 混三条乘以3
			if (dare == 2) {
				int wager = gong3.getWager();
				flag = 3;
				int changeWager = (wager * flag > uPoint ? uPoint : wager
						* flag);
				gong3.setWager(changeWager);
				result = "lose";
			}

		} else {
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.GONG3);
			// history.setResult(WGameHistoryBean.DRAW);
			// tip = "平手!你们的牌型一样";
			// history.setRemark(tip);
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_GONG3);
			history.setDrawCount(1);
			updateHistory(history);
			// 打平后更新用户等级积分
			updateDrawInfo(loginUser);
			// mcq_end
			result = "draw";
		}

		// 赢了
		if (result.equals("win")) {
			// 加上积分
			//fanys2006-08-11
			UserInfoUtil.updateUserCash(loginUser.getId(),gong3.getWager(),UserCashAction.WAGER,"wgame--3公--加乐币"+ gong3.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.GONG3,gong3.getWager(),Constants.PLUS,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			//mcq_1_更新玩家胜利后Session的乐币数和经验值  时间 2006-6-11
			updateInfo(loginUser,Constants.RANK_PK_WIN);
			//mcq_end
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.GONG3);
			// history.setResult(WGameHistoryBean.WIN);
			// history.setRemark("您赢了" + gong3.getWager() + "个乐币!赔率是1:" + flag +
			// ".");
			// history.setGamePoint(gong3.getWager());
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_GONG3);
			history.setWinCount(1);
			history.setMoney(gong3.getWager());
			updateHistory(history);

			WGameBean wgame = (WGameBean) getSessionObject(request,
					winsSessionName);
			if (wgame != null) {
				wgame.setWins(wgame.getWins() + 1);
			}

			result = "win";
		} else if (result.equals("lose")) {
			// 减去积分
			
			//fanys2006-08-11
			UserInfoUtil.updateUserCash(loginUser.getId(),-gong3.getWager(),UserCashAction.WAGER,"wgame--三公--减乐币"+ gong3.getWager());
            //zhul_2006-07-25 记录用户的现金流 start
            MoneyAction.addMoneyFlowRecord(Constants.GONG3,gong3.getWager(),Constants.SUBTRATION,loginUser.getId());
            //zhul_2006-07-25 记录用户的现金流 end
			
			//mcq_1_更新玩家失败后Session的乐币数和经验值  时间 2006-6-11
			updateInfo(loginUser,Constants.RANK_PK_LOSE);
			//mcq_end
			// 记录
			// WGameHistoryBean history = new WGameHistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameId(WGameHistoryBean.GONG3);
			// history.setResult(WGameHistoryBean.LOSE);
			// history.setRemark("您输了" + gong3.getWager() + "个乐币!赔率是1:" + flag +
			// ".");
			// history.setGamePoint(gong3.getWager());
			// wgService.addWGameHistory(history);
			// wgService.addWGameHistory(history);
			HistoryBean history = new HistoryBean();
			history.setUserId(loginUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_GONG3);
			history.setLoseCount(1);
			history.setMoney(-gong3.getWager());
			updateHistory(history);

			//request.getSession().removeAttribute(winsSessionName);
			result = "lose";
		}
		request.setAttribute("result", result);
		request.setAttribute("cardList1", cardList1);
		request.setAttribute("cardList", cardList);
		request.setAttribute("gong3", gong3);
	}

	public int getCardsType(Vector cards) {
		if (cards == null || cards.size() < 3) {
			return 0;
		}
		CardBean card1 = (CardBean) cards.get(0);
		CardBean card2 = (CardBean) cards.get(1);
		CardBean card3 = (CardBean) cards.get(2);
		// 判断是否是JQK三条
		if (card1.getValue() == card2.getValue()
				&& card1.getValue() == card3.getValue()
				&& card1.getValue() >= 11) {
			return 4;
		}
		// 判断是否是12345678910三条
		else if (card1.getValue() == card2.getValue()
				&& card1.getValue() == card3.getValue()
				&& card1.getValue() <= 10) {
			return 3;
		}
		// 判断是否是JQK混三公
		else if (card1.getValue() >= 11 && card2.getValue() >= 11
				&& card3.getValue() >= 11) {
			return 2;
		}
		// 判断为普通牌
		return 1;
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
				WGameBean.DC_GONG3);
		request.setAttribute("history", history);
	}
}
