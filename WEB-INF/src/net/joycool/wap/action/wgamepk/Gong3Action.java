/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.action.wgamepk;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.CardBean;
import net.joycool.wap.bean.wgame.Cards;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgame.WGamePKBean;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class Gong3Action extends PKBaseAction {
	public int BK_NUMBER_PER_PAGE = 5;

	public int ONLINE_NUMBER_PER_PAGE = 5;

	public Gong3Action(HttpServletRequest request) {
		super(request);
	}
	static byte[] lock = new byte[0];
	/**
	 * 首页。
	 * 
	 * @param request
	 * @param response
	 */
	public void dealIndex(HttpServletRequest request,
			HttpServletResponse response) {
		// 取得参数
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		// int pageIndex1 =
		// StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		// if (pageIndex1 < 0) {
		// pageIndex1 = 0;
		// }

		// 取得坐庄列表
		// String orderBy = request.getParameter("orderBy");
		// if (pageIndex == 0) {
		// orderBy = "id";
		// }
		// if (orderBy == null) {
		// orderBy = "id";
		// }
		// macq_2006-10-18_更改pk游戏排序规则_start
		String orderBy = "id";
		// macq_2006-10-18_更改pk游戏排序规则_end
		String condition = "mark = " + WGamePKBean.PK_MARK_BKING
				+ " and game_id = " + WGameBean.PK_GONG3;
		// 取得总数
		int totalBkCount = pkService.getWGamePKCount(condition);
		int totalBkPageCount = totalBkCount / BK_NUMBER_PER_PAGE;
		if (totalBkCount % BK_NUMBER_PER_PAGE != 0) {
			totalBkPageCount++;
		}
		if (pageIndex > totalBkPageCount - 1) {
			pageIndex = totalBkPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		/**
		 * 取得庄家列表
		 */
		condition += " order by " + orderBy + " desc limit " + pageIndex
				* BK_NUMBER_PER_PAGE + ", " + BK_NUMBER_PER_PAGE;
		Vector bkList = pkService.getWGamePKList(condition);

		/*
		 * // 取得在线玩家列表 orderBy = request.getParameter("orderBy"); if (pageIndex1 ==
		 * 0) { orderBy = "id"; } if (orderBy == null) { orderBy = "id"; }
		 * 
		 * condition = "id > 0"; // 取得总数 int totalOnlineCount =
		 * userService.getOnlineUserCount(condition); int totalOnlinePageCount =
		 * totalOnlineCount / ONLINE_NUMBER_PER_PAGE; if (totalOnlineCount %
		 * ONLINE_NUMBER_PER_PAGE != 0) { totalOnlinePageCount++; } if
		 * (pageIndex1 > totalOnlinePageCount - 1) { pageIndex1 =
		 * totalOnlinePageCount - 1; } if (pageIndex1 < 0) { pageIndex1 = 0; } //
		 * 李北金_2006-06-20_查询优化_start // zhul_2006-09-07 如果是非登录用户也允许用户进入游戏首页
		 * start if (loginUser != null) { condition = "join jc_online_user on
		 * user_info.id=jc_online_user.user_id where user_info.id != " +
		 * loginUser.getId(); } else { condition = "join jc_online_user on
		 * user_info.id=jc_online_user.user_id "; } // zhul_2006-09-07
		 * 如果是非登录用户也允许用户进入游戏首页 end condition += " order by user_info." + orderBy + "
		 * desc limit " + pageIndex1 * ONLINE_NUMBER_PER_PAGE + ", " +
		 * ONLINE_NUMBER_PER_PAGE; Vector userList =
		 * userService.getUserList(condition); // 李北金_2006-06-20_查询优化_end
		 * 
		 */
		/**
		 * 取得玩家列表
		 */
		// zhul 2006-10-18 优化获取在线用户 start
		// 获取所有在线用户的正序序列
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		// liuyi 2006-12-02 程序优化 start
		// ArrayList onlineUser=OnlineUtil.getAllOnlineUser();
		List onlineUserIds = UserInfoUtil.getOnlineUserOrderByPKFromCache();
		// zhul 2006-10-20 按PK度进行排序

		// 分页
		int totalOnlineCount = onlineUserIds.size();
		int totalOnlinePageCount = (totalOnlineCount + ONLINE_NUMBER_PER_PAGE - 1)
				/ ONLINE_NUMBER_PER_PAGE;
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 > totalOnlinePageCount - 1) {
			pageIndex1 = totalOnlinePageCount - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}

		int start = pageIndex1 * ONLINE_NUMBER_PER_PAGE;
		int end = pageIndex1 * ONLINE_NUMBER_PER_PAGE + ONLINE_NUMBER_PER_PAGE;
		// 玩家列表
		Vector userList = new Vector();
		for (int i = start; i < end; i++) {
			if (i > totalOnlineCount - 1)
				break;
			String userId = (String) onlineUserIds.get(i);
			int id = StringUtil.toInt(userId);
			if (id < 1) {
				continue;
			}
			if (loginUser != null && loginUser.getId() == id) {
				continue;
			}
			UserBean user = UserInfoUtil.getUser(id);
			if (user != null)
				userList.add(user);
		}
		// liuyi 2006-12-02 程序优化 end

		Iterator itr = userList.iterator();
		UserBean user = null;
		while (itr.hasNext()) {
			user = (UserBean) itr.next();
			user.setUs(getUserStatus(user.getId()));
		}

		String prefixUrl = "index.jsp?pageIndex1=" + pageIndex1;
		String prefixUrl1 = "index.jsp?pageIndex=" + pageIndex;

		request.setAttribute("totalBkCount", new Integer(totalBkCount));
		request.setAttribute("totalBkPageCount", new Integer(totalBkPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("bkList", bkList);

		request.setAttribute("totalOnlineCount", new Integer(totalOnlineCount));
		request.setAttribute("totalOnlinePageCount", new Integer(
				totalOnlinePageCount));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("userList", userList);
	}

	public UserStatusBean getUserStatus() {
		return getUserStatus(loginUser.getId());
	}

	/**
	 * 坐庄开始。
	 * 
	 * @param request
	 */
	public void bkStart(HttpServletRequest request) {
		UserStatusBean us = getUserStatus(loginUser.getId());
		String tip = null;
		String result = null;
		// 不够10个乐币
		if (us.getGamePoint() < 10) {
			tip = "对不起,您的乐币不够10个,不能坐庄";
			result = "notEnoughMoney";
		} else {
			// 已经坐庄
			String condition = "left_user_id = " + loginUser.getId()
					+ " and mark = " + WGamePKBean.PK_MARK_BKING
					+ " and game_id = " + WGameBean.PK_GONG3;
			if (pkService.getWGamePKCount(condition) >= WGameBean.MAX_BK_COUNT) {
				tip = "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个三公赌局,不能再坐庄";
				result = "hasBk";
			} else {
				result = "continue";
			}
		}
		request.setAttribute("tip", tip);
		request.setAttribute("result", result);
	}

	/**
	 * 坐庄处理。
	 * 
	 * @param request
	 */
	public void bkDeal1(HttpServletRequest request) {
		int wager = StringUtil.toInt(request.getParameter("wager"));
		
		String tip = null;
		String result = null;
		String condition = "left_user_id = " + loginUser.getId()
				+ " and mark = " + WGamePKBean.PK_MARK_BKING
				+ " and game_id = " + WGameBean.PK_GONG3;
		if (pkService.getWGamePKCount(condition) >= WGameBean.MAX_BK_COUNT) {
			request.setAttribute("tip", "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个三公赌局,不能再坐庄");
			request.setAttribute("result", "failure");
			return;
		}
		
		synchronized(loginUser.getLock()) {
			UserStatusBean us = getUserStatus(loginUser.getId());
			int cWager = wager * 5;
			// 乐币不够
			if (us.getGamePoint() < wager) {
				request.setAttribute("tip", "对不起,您乐币不够");
				request.setAttribute("result", "failure");
				return;
			} else if (wager < 10) {
				request.setAttribute("tip", "至少要下注10个乐币");
				request.setAttribute("result", "failure");
				return;
			} else if (wager > Constants.GAME_POINT_MAX_INT) {
				request.setAttribute("tip", "最多能下注" + Constants.GAME_POINT_MAX_INT + "乐币");
				request.setAttribute("result", "failure");
				return;
			} else if (us.getGamePoint() < cWager) {
				request.setAttribute("tip", "对不起，按三公规则你有可能输掉5倍的乐币，坐庄时需要扣除赌注5倍的乐币（赢了或没有输完都会原数返还）。");
				request.setAttribute("result", "failure");
				return;
			}
			this.decLoginUserGamePoint(cWager);
		}

		WGamePKBean gong3 = new WGamePKBean();
		gong3.setGameId(WGameBean.PK_GONG3);
		gong3.setLeftUserId(loginUser.getId());
		gong3.setLeftNickname(loginUser.getNickName());

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
		gong3.setLeftCardsStr(a + "_" + aa + "," + b + "_" + bb + "," + c
				+ "_" + cc);
		gong3.setMark(WGamePKBean.PK_MARK_BKING);
		gong3.setWager(wager);
		if (!pkService.addWGamePK(gong3)) {
			return;
		}
		result = "success";
		request.setAttribute("gong3", gong3);
		request.setAttribute("result", result);
		request.setAttribute("cardList", cardList);
	}

	/**
	 * 取消坐庄
	 * 
	 * @param request
	 */
	public void cancelBk(HttpServletRequest request) {
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		String condition = "left_user_id = " + loginUser.getId()
				+ " and mark = " + WGamePKBean.PK_MARK_BKING + " and id = "
				+ bkId;
		WGamePKBean gong3 = pkService.getWGamePK(condition);
		UserStatusBean us = getUserStatus(loginUser.getId());
		if (gong3 != null) {
			int cWager = gong3.getWager() * 5;
			this.incLoginUserGamePoint(cWager);
			int wager = gong3.getWager() / 10;
			if (wager > us.getGamePoint()) {
				wager = us.getGamePoint();
			}
			request.setAttribute("wager", new Integer(wager));

			// 扣掉乐币
			this.decLoginUserGamePoint(wager);
			pkService.deleteWGamePK("id = " + gong3.getId());
			// mcq_1_更改session中用户乐币数
			updatePKCancelInfo();
			// mcq_end
		}

		request.setAttribute("gong3", gong3);
	}

	/**
	 * 坐庄处理。
	 * 
	 * @param request
	 */

	public void chlStart(HttpServletRequest request) {
		int gameResult = 0;
		// long bankerGamePoint = 0;
		int chanllengerGamePoint = 0;
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		String tip = null;
		String result = null;
		if (bkId == -1) {
			tip = "该局已被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		String condition = "id = " + bkId + " and game_id = " + WGameBean.PK_GONG3;
		WGamePKBean gong3;
		Vector cardList = new Vector();
		Vector cardList1 = new Vector();
		synchronized(lock) {
			gong3 = pkService.getWGamePK(condition);
			if (validate(gong3, request) == false)
				return;
	
			UserBean enemy = null;
			UserStatusBean enemyUs = null;
	
			enemyUs = getUserStatus(gong3.getLeftUserId());
			if (enemyUs == null) {
				return;
			}
			chanllengerGamePoint = loginUser.getUs().getGamePoint();
			// bankerGamePoint = (long)enemyUs.getGamePoint()
			// +(long)gong3.getWager();
	
			// int maxWager = (enemyUs.getGamePoint()+gong3.getWager()) >
			// us.getGamePoint() ? us
			// .getGamePoint() :( enemyUs.getGamePoint()+gong3.getWager());
			// if (gong3.getWager() > maxWager) {
			// gong3.setWager(maxWager);
			// }
	
			CardBean card1 = null;
			CardBean card2 = null;
			CardBean card3 = null;
			// 得到庄家的牌放到一个Vector中
			String leftGong3sStr = gong3.getLeftCardsStr();
			String[] pai = leftGong3sStr.split(",");
			if (pai != null && pai.length == 3) {
				String[] a = pai[0].split("_");
				int aa = Integer.parseInt(a[0]);
				int bb = Integer.parseInt(a[1]);
				String[] b = pai[1].split("_");
				int cc = Integer.parseInt(b[0]);
				int dd = Integer.parseInt(b[1]);
				String[] c = pai[2].split("_");
				int ee = Integer.parseInt(c[0]);
				int ff = Integer.parseInt(c[1]);
				card1 = new CardBean(aa, bb,
						"http://wap.joycool.net/wgame/cardImg/" + aa + "_" + bb
								+ ".gif");
				card2 = new CardBean(cc, dd,
						"http://wap.joycool.net/wgame/cardImg/" + cc + "_" + dd
								+ ".gif");
				card3 = new CardBean(ee, ff,
						"http://wap.joycool.net/wgame/cardImg/" + ee + "_" + ff
								+ ".gif");
				cardList1.add(card1);
				cardList1.add(card2);
				cardList1.add(card3);
			}
			// 生成挑战者的牌（从52张牌里去处庄家的3张牌）放到一个Vector中
			Cards cards = new Cards();
			CardBean card = null;
			int count = 0;
			for (int i = 0; i < 6; i++) {
				card = cards.getCard();
				if (card.compareTo(card1) == 0 || card.compareTo(card2) == 0
						|| card.compareTo(card3) == 0) {
					continue;
				} else
					cardList.add(card);
				count++;
				if (count == 3) {
					break;
				}
			}
	
			CardBean card4 = (CardBean) cardList.get(0);
			int a = card4.getValue();
			int aa = card4.getType();
			CardBean card5 = (CardBean) cardList.get(1);
			int b = card5.getValue();
			int bb = card5.getType();
			CardBean card6 = (CardBean) cardList.get(2);
			int c = card6.getValue();
			int cc = card6.getType();
			gong3.setRightCardsStr(a + "_" + aa + "," + b + "_" + bb + "," + c
					+ "_" + cc);
	
			/**
			 * 判断输赢
			 */
			// 得到庄家和挑战者的牌型
			int banker = this.getCardsType(cardList1);
			int dare = this.getCardsType(cardList);
			int winUserId = 0;
			int bankerTotal = 0;
			int dareTotal = 0;
			int wager = gong3.getWager();
			long changeWager = 0;
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
			// 庄家赢
			if (bankerTotal > dareTotal) {
				// 庄家加上乐币
				// 8点以上乘2
				if (bankerTotal >= 8) {
					// int wager = gong3.getWager();
					changeWager = ((long) wager * 2 > chanllengerGamePoint ? chanllengerGamePoint
							: (long) wager * 2);
					// gong3.setWager(changeWager);
					winUserId = gong3.getLeftUserId();
				} else {
					// int wager = gong3.getWager();
					changeWager = (wager > chanllengerGamePoint ? chanllengerGamePoint
							: wager);
					// gong3.setWager(wager);
					winUserId = gong3.getLeftUserId();
					// 8点以下乘1
				}
	
			} else if (banker > dare) {
				// 庄家加上乐币
				// JQK三条乘以5
				if (banker == 4) {
					// int wager = gong3.getWager();
					changeWager = ((long) wager * 5 > chanllengerGamePoint ? chanllengerGamePoint
							: (long) wager * 5);
					// gong3.setWager(changeWager);
					winUserId = gong3.getLeftUserId();
				} else if (banker == 3) {
					// 12345678910三条乘以4
					// int wager = gong3.getWager();
					changeWager = ((long) wager * 4 > chanllengerGamePoint ? chanllengerGamePoint
							: (long) wager * 4);
					// gong3.setWager(changeWager);
					winUserId = gong3.getLeftUserId();
				} else if (banker == 2) {
					// 混三条乘以3
					// int wager = gong3.getWager();
					changeWager = ((long) wager * 3 > chanllengerGamePoint ? chanllengerGamePoint
							: (long) wager * 3);
					// gong3.setWager(changeWager);
					winUserId = gong3.getLeftUserId();
				}
			}
			// 庄家输
			else if (bankerTotal < dareTotal) {
				// 8点以上乘2
				if (dareTotal >= 8) {
					// 庄家减去乐币
					// int wager = gong3.getWager();
					// changeWager = ((long)wager * 2 > bankerGamePoint ?
					// bankerGamePoint
					// : (long)wager * 2);
					changeWager = (long) wager * 2;
					// gong3.setWager(changeWager);
					winUserId = loginUser.getId();
				} else {
					// 8点以下乘1
					// int wager = gong3.getWager();
					// changeWager = (wager > bankerGamePoint ? bankerGamePoint
					// : wager);
					changeWager = wager;
					// gong3.setWager(wager);
					winUserId = loginUser.getId();
				}
	
			} else if (banker < dare) {
				// 庄家减去乐币
				// JQK三条乘以5
				if (dare == 4) {
					// int wager = gong3.getWager();
					// changeWager = ((long) wager * 5 > bankerGamePoint ?
					// bankerGamePoint
					// : (long) wager * 5);
					changeWager = (long) wager * 5;
					// gong3.setWager(changeWager);
					winUserId = loginUser.getId();
				}// 12345678910三条乘以5
				if (dare == 3) {
					// int wager = gong3.getWager();
					// changeWager = ((long) wager * 4 > bankerGamePoint ?
					// bankerGamePoint
					// : (long) wager * 4);
					changeWager = (long) wager * 4;
					// gong3.setWager(changeWager);
					winUserId = loginUser.getId();
				}// 混三条乘以3
				if (dare == 2) {
					// int wager = gong3.getWager();
					// changeWager = ((long) wager * 3 > bankerGamePoint ?
					// bankerGamePoint
					// : (long) wager * 3);
					changeWager = (long) wager * 3;
					// gong3.setWager(changeWager);
					winUserId = loginUser.getId();
				}
	
			} else {
				// 打平
	
				gameResult = DOGFALL;

				// 打平后更新庄家等级积分
				updateLeftInfo(gong3.getLeftUserId(), Constants.RANK_DRAW);
				// mcq_end
				// 打平后更新用户等级积分
				updatePKDrawRightInfo();
				// mcq_end
			}
			// 庄家输了
			if (winUserId == loginUser.getId()) {
				gameResult = BANKER_LOSE;
				gong3.setWinUserId(winUserId);
				winUserId = loginUser.getId();
				// mcq_1_添加坐庄的信息变更
				updateLeftInfo(gong3.getLeftUserId(), Constants.RANK_LOSE);
				// mcq_end
				// mcq_1_更新挑战者胜利后Session的乐币数和经验值 时间 2006-6-11
				updatePKRightInfo(Constants.RANK_WIN);
				// mcq_end

			}// 庄家赢了
			else if (winUserId == gong3.getLeftUserId()) {
				gameResult = BANKER_WIN;
				gong3.setWinUserId(winUserId);
				winUserId = gong3.getLeftUserId();
				// mcq_1_添加坐庄的信息变更
				updateLeftInfo(gong3.getLeftUserId(), Constants.RANK_WIN);
				// mcq_end
				// mcq_1_更新挑战者失败后Session的乐币数和经验值 时间 2006-6-11
				updatePKRightInfo(Constants.RANK_LOSE);
				// mcq_end
			}
			gong3.setRightUserId(loginUser.getId());
			gong3.setRightNickname(loginUser.getNickName());
			gong3.setWinUserId(winUserId);
			this.dealResult(gameResult, gong3, changeWager);
	
			String set = "right_user_id = " + loginUser.getId()
					+ ", right_nickname = '" + StringUtil.toSql(loginUser.getNickName())
					+ "', right_cards = '" + gong3.getRightCardsStr()
					+ "', end_datetime = now(), mark = " + WGamePKBean.PK_MARK_END
					+ ", win_user_id = " + winUserId + ", right_viewed = 1, wager="
					+ gong3.getWager();
			condition = "id = " + bkId;
			if (!pkService.updateWGamePK(set, condition)) {
				return;
			}
			gong3.setWager((int)changeWager);
		}
		
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("gong3", gong3);
		request.setAttribute("cardList", cardList);
		request.setAttribute("cardList1", cardList1);

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getChlStartNoticeTitle(loginUser.getNickName(), "三公"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(gong3.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamepk/3gong/viewPK.jsp?id="
				+ gong3.getId());
		// macq_2007-5-16_增加人人对战PK游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME);
		// macq_2007-5-16_增加人人对战PK游戏消息类型_end
		noticeService.addNotice(notice);
		// }
	}

	/**
	 * PK开始。
	 * 
	 * @param request
	 */
	public void pkStart(HttpServletRequest request) {
		int userId = StringUtil.toInt(request.getParameter("userId"));
		if (userId == -1) {
			return;
		}
		UserBean onlineUser = (UserBean) OnlineUtil.getOnlineBean(userId + "");
		UserBean enemy = null;
		UserStatusBean us = getUserStatus(loginUser.getId());
		UserStatusBean enemyUs = null;
		String tip = null;
		String result = null;
		// 不够100个乐币
		if (us.getGamePoint() < 100) {
			tip = "您的乐币不够100个,不能PK";
			result = "notEnoughMoney";
		} else if (onlineUser != null && onlineUser.noticeMark()) {
			// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_start
			tip = "对方设置自己状态为免打扰.不能pk！";
			result = "failure";
			// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_end
		} else {
			// 已经在PK中
			String condition = "left_user_id = " + loginUser.getId()
					+ " and mark = " + WGamePKBean.PK_MARK_PKING;
			if (pkService.getWGamePKCount(condition) >= WGameBean.MAX_PK_COUNT) {
				tip = "对不起,您PK了五人!请等待他们应战";
				result = "hasPk";
			} else {
				// 李北金_2006-06-20_查询优化_start
				// condition = "join jc_online_user on
				// user_info.id=jc_online_user.user_id where
				// jc_online_user.user_id = "
				// + userId;
				// enemy = userService.getUser(condition);
				// zhul 2006-10-17 优化用户信息查询 start
				if (OnlineUtil.isOnline(userId + ""))
					enemy = UserInfoUtil.getUser(userId);
				// zhul 2006-10-17 优化用户信息查询 end
				// 李北金_2006-06-20_查询优化_end
				if (enemy == null) {
					tip = "对不起,对方已下线";
					result = "failure";
				} else if (pkService.getWGamePKCount("right_user_id = "
						+ userId + " and game_id = " + WGameBean.PK_GONG3
						+ " and mark = " + WGamePKBean.PK_MARK_PKING) > 0) {
					tip = "对不起,对方已经被PK";
					result = "failure";
				} else {
					enemyUs = getUserStatus(userId);
					result = "continue";
				}
			}
		}

		request.setAttribute("enemy", enemy);
		request.setAttribute("enemyUs", enemyUs);
		request.setAttribute("tip", tip);
		request.setAttribute("result", result);
	}

	/**
	 * PK处理。
	 * 
	 * @param request
	 */
	public void pkDeal1(HttpServletRequest request) {
		int userId = StringUtil.toInt(request.getParameter("userId"));
		if (userId == -1) {
			return;
		}

		String tip = null;
		String result = null;

		// 对手
		UserBean enemy = null;
		UserStatusBean enemyUs = null;
		enemyUs = getUserStatus(userId);

		String condition = "left_user_id = " + loginUser.getId()
				+ " and mark = " + WGamePKBean.PK_MARK_PKING;
		if (pkService.getWGamePKCount(condition) >= WGameBean.MAX_PK_COUNT) {
			tip = "对不起,您PK了五人!请等待他们应战";
			result = "failure";
		} else {
			// 李北金_2006-06-20_查询优化_start
			// condition = "join jc_online_user on
			// user_info.id=jc_online_user.user_id where jc_online_user.user_id
			// = "
			// + userId;
			// enemy = userService.getUser(condition);
			// zhul 2006-10-17 优化用户信息查询 start
			if (OnlineUtil.isOnline(userId + ""))
				enemy = UserInfoUtil.getUser(userId);
			// zhul 2006-10-17 优化用户信息查询 end
			// 李北金_2006-06-20_查询优化_end
			if (enemy == null) {
				tip = "对不起,对方已下线";
				result = "failure";
			} else if (pkService.getWGamePKCount("right_user_id = " + userId
					+ " and game_id = " + WGameBean.PK_GONG3 + " and mark = "
					+ WGamePKBean.PK_MARK_PKING) > 0) {
				tip = "对不起,对方已经被PK";
				result = "failure";
			}
		}

		int wager = StringUtil.toInt(request.getParameter("wager"));
		UserStatusBean us = getUserStatus(loginUser.getId());
		int cWager = wager * 5;
		// 乐币不够
		if (us.getGamePoint() < wager) {
			tip = "您的乐币不够";
			result = "failure";
		} else if (enemyUs.getGamePoint() < wager) {
			tip = "对手的乐币不够";
			result = "failure";
		}
		// 乐币太少
		else if (wager < 10) {
			tip = "至少下注10个乐币";
			result = "failure";
		} else if (wager > Constants.GAME_POINT_MAX_INT) {
			tip = "最多能下注" + Constants.GAME_POINT_MAX_INT + "乐币";
			result = "failure";
		}
		// 坐庄时扣除5倍上限
		else if (us.getGamePoint() < cWager) {
			tip = "对不起，按三公规则你有可能输掉5倍的乐币，坐庄时需要扣除赌注5倍的乐币（赢了或没有输完都会原数返还）。";
			result = "failure";
		}
		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
		}
		// 发牌
		else {
			this.decLoginUserGamePoint(cWager);
			WGamePKBean gong3 = new WGamePKBean();
			gong3.setGameId(WGameBean.PK_GONG3);
			gong3.setLeftUserId(loginUser.getId());
			gong3.setLeftNickname(loginUser.getNickName());

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
			gong3.setLeftCardsStr(a + "_" + aa + "," + b + "_" + bb + "," + c
					+ "_" + cc);
			gong3.setMark(WGamePKBean.PK_MARK_PKING);
			gong3.setWager(wager);
			gong3.setRightUserId(enemy.getId());
			gong3.setRightNickname(enemy.getNickName());
			if (!pkService.addWGamePK(gong3)) {
				return;
			}

			result = "success";

			gong3 = pkService.getWGamePK("left_user_id = "
					+ gong3.getLeftUserId() + " and right_user_id = "
					+ gong3.getRightUserId() + " and mark = "
					+ WGamePKBean.PK_MARK_PKING + " and game_id = "
					+ WGameBean.PK_GONG3);

			// **更新用户状态**
			// **加入消息系统**

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getPKNoticeTitle(loginUser.getNickName(), "三公"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(gong3.getRightUserId());
			notice.setHideUrl("");
			notice.setLink("/wgamepk/3gong/pkRightStart.jsp?pkId=" + gong3.getId());
			// macq_2007-5-16_增加人人对战PK游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME);
			// macq_2007-5-16_增加人人对战PK游戏消息类型_end
			noticeService.addNotice(notice);

			request.setAttribute("enemy", enemy);
			request.setAttribute("enemyUs", enemyUs);
			request.setAttribute("result", result);
			request.setAttribute("gong3", gong3);
			request.setAttribute("cardList", cardList);
		}
	}

	/**
	 * PK应战。
	 * 
	 * @param request
	 */
	public void pkRightStart(HttpServletRequest request) {
		int pkId = StringUtil.toInt(request.getParameter("pkId"));
		String condition = "id = " + pkId + " and mark = "
				+ WGamePKBean.PK_MARK_PKING;
		WGamePKBean gong3 = pkService.getWGamePK(condition);

		request.setAttribute("gong3", gong3);
	}

	/**
	 * 应战处理。
	 * 
	 * @param request
	 */
	public void pkRightDeal1(HttpServletRequest request) {

		int gameResult = 0;
		// long bankerGamePoint = 0;
		int chanllengerGamePoint = 0;

		int pkId = StringUtil.toInt(request.getParameter("pkId"));
		String condition = "id = " + pkId + " and mark = "
				+ WGamePKBean.PK_MARK_PKING;
		WGamePKBean gong3 = pkService.getWGamePK(condition);
		if (validate(gong3, request) == false) {
			return;
		}

		UserStatusBean us = getUserStatus(loginUser.getId());

		// 对手
		UserStatusBean enemyUs = null;
		enemyUs = getUserStatus(gong3.getLeftUserId());

		chanllengerGamePoint = us.getGamePoint();
		int wager = gong3.getWager();
		long changeWager = 0;
		CardBean card1 = null;
		CardBean card2 = null;
		CardBean card3 = null;
		// 得到庄家的牌放到一个Vector中
		Vector cardList1 = new Vector();
		String leftGong3sStr = gong3.getLeftCardsStr();
		String[] pai = leftGong3sStr.split(",");
		if (pai != null && pai.length == 3) {
			String[] a = pai[0].split("_");
			int aa = Integer.parseInt(a[0]);
			int bb = Integer.parseInt(a[1]);
			String[] b = pai[1].split("_");
			int cc = Integer.parseInt(b[0]);
			int dd = Integer.parseInt(b[1]);
			String[] c = pai[2].split("_");
			int ee = Integer.parseInt(c[0]);
			int ff = Integer.parseInt(c[1]);
			card1 = new CardBean(aa, bb,
					"http://wap.joycool.net/wgame/cardImg/" + aa + "_" + bb
							+ ".gif");
			card2 = new CardBean(cc, dd,
					"http://wap.joycool.net/wgame/cardImg/" + cc + "_" + dd
							+ ".gif");
			card3 = new CardBean(ee, ff,
					"http://wap.joycool.net/wgame/cardImg/" + ee + "_" + ff
							+ ".gif");
			cardList1.add(card1);
			cardList1.add(card2);
			cardList1.add(card3);
		}
		// 生成挑战者的牌（从52张牌里去处庄家的3张牌）放到一个Vector中
		Vector cardList = new Vector();
		Cards cards = new Cards();
		CardBean card = null;
		int count = 0;
		for (int i = 0; i < 6; i++) {
			card = cards.getCard();
			// 判断生成挑战者的牌和庄家的牌是否一样，如果一样重新生成
			if (card.compareTo(card1) == 0 || card.compareTo(card2) == 0
					|| card.compareTo(card3) == 0) {
				continue;
			} else
				cardList.add(card);
			// 如果生成挑战者的牌没有和庄家的牌一样，利用计数器退出
			count++;
			if (count == 3) {
				break;
			}
		}

		CardBean card4 = (CardBean) cardList.get(0);
		int a = card4.getValue();
		int aa = card4.getType();
		CardBean card5 = (CardBean) cardList.get(1);
		int b = card5.getValue();
		int bb = card5.getType();
		CardBean card6 = (CardBean) cardList.get(2);
		int c = card6.getValue();
		int cc = card6.getType();
		gong3.setRightCardsStr(a + "_" + aa + "," + b + "_" + bb + "," + c
				+ "_" + cc);

		/**
		 * 判断输赢
		 */
		// 得到庄家和挑战者的牌型
		int banker = this.getCardsType(cardList1);
		int dare = this.getCardsType(cardList);
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
		// 庄家赢
		if (bankerTotal > dareTotal) {
			// 庄家加上乐币
			// 8点以上乘2
			if (bankerTotal >= 8) {
				// int wager = gong3.getWager();
				changeWager = ((long) wager * 2 > chanllengerGamePoint ? chanllengerGamePoint
						: (long) wager * 2);
				// gong3.setWager(changeWager);
				winUserId = gong3.getLeftUserId();
			} else {
				// int wager = gong3.getWager();
				// gong3.setWager(wager);
				changeWager = (wager > chanllengerGamePoint ? chanllengerGamePoint
						: wager);
				winUserId = gong3.getLeftUserId();
				// 8点以下乘1
			}

		} else if (banker > dare) {
			// 庄家加上乐币
			// JQK三条乘以5
			if (banker == 4) {
				// int wager = gong3.getWager();
				changeWager = ((long) wager * 5 > chanllengerGamePoint ? chanllengerGamePoint
						: (long) wager * 5);
				// gong3.setWager(changeWager);
				winUserId = gong3.getLeftUserId();
			} else if (banker == 3) {
				// 12345678910三条乘以4
				// int wager = gong3.getWager();
				changeWager = ((long) wager * 4 > chanllengerGamePoint ? chanllengerGamePoint
						: (long) wager * 4);
				// gong3.setWager(changeWager);
				winUserId = gong3.getLeftUserId();
			} else if (banker == 2) {
				// 混三条乘以3
				// int wager = gong3.getWager();
				changeWager = ((long) wager * 3 > chanllengerGamePoint ? chanllengerGamePoint
						: (long) wager * 3);
				// gong3.setWager(changeWager);
				winUserId = gong3.getLeftUserId();
			}
		}
		// 庄家输
		else if (bankerTotal < dareTotal) {
			// 8点以上乘2
			if (dareTotal >= 8) {
				// 庄家减去乐币
				// int wager = gong3.getWager();
				// changeWager = ((long) wager * 2 > bankerGamePoint ?
				// bankerGamePoint
				// : (long) wager * 2);
				changeWager = (long) wager * 2;
				// gong3.setWager(changeWager);
				winUserId = loginUser.getId();
			} else {
				// 8点以下乘1
				// int wager = gong3.getWager();
				// changeWager = (wager > bankerGamePoint ? bankerGamePoint
				// : wager);
				changeWager = wager;
				// gong3.setWager(wager);
				winUserId = loginUser.getId();
			}

		} else if (banker < dare) {
			// 庄家减去乐币
			// JQK三条乘以5
			if (dare == 4) {
				// int wager = gong3.getWager();
				// changeWager = ((long) wager * 5 > bankerGamePoint ?
				// bankerGamePoint
				// : (long) wager * 5);
				changeWager = (long) wager * 5;
				// gong3.setWager(changeWager);
				winUserId = loginUser.getId();
			}// 12345678910三条乘以5
			if (dare == 3) {
				// int wager = gong3.getWager();
				// changeWager = ((long) wager * 4 > bankerGamePoint ?
				// bankerGamePoint
				// : (long) wager * 4);
				changeWager = (long) wager * 4;
				// gong3.setWager(changeWager);
				winUserId = loginUser.getId();
			}// 混三条乘以3
			if (dare == 2) {
				// int wager = gong3.getWager();
				// changeWager = ((long) wager * 3 > bankerGamePoint ?
				// bankerGamePoint
				// : (long) wager * 3);
				changeWager = (long) wager * 3;
				// gong3.setWager(changeWager);
				winUserId = loginUser.getId();
			}

		} else {
			gameResult = DOGFALL;
			// HistoryBean history = new HistoryBean();
			// history.setUserId(gong3.getLeftUserId());
			// history.setGameType(WGameBean.GT_PK);
			// history.setGameId(WGameBean.PK_GONG3);
			// history.setDrawCount(1);
			// updateHistory(history);

			// history = new HistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameType(WGameBean.GT_PK);
			// history.setGameId(WGameBean.PK_GONG3);
			// history.setDrawCount(1);
			// updateHistory(history);
			// 打平后更新庄家等级积分
			updateLeftInfo(gong3.getLeftUserId(), Constants.RANK_DRAW);
			// mcq_end
			// 打平后更新用户等级积分
			updatePKDrawRightInfo();
			// mcq_end
		}
		// 庄家输了
		if (winUserId == loginUser.getId()) {
			gameResult = BANKER_LOSE;
			gong3.setWinUserId(winUserId);
			winUserId = loginUser.getId();
			// mcq_1_添加坐庄的信息变更
			updateLeftInfo(gong3.getLeftUserId(), Constants.RANK_LOSE);
			// mcq_end
			// mcq_1_更新挑战者胜利后Session的乐币数和经验值 时间 2006-6-11
			updatePKRightInfo(Constants.RANK_WIN);
			// mcq_end
		}// 庄家赢了
		else if (winUserId == gong3.getLeftUserId()) {
			gameResult = BANKER_WIN;
			gong3.setWinUserId(winUserId);
			winUserId = gong3.getLeftUserId();
			// mcq_1_添加坐庄的信息变更
			updateLeftInfo(gong3.getLeftUserId(), Constants.RANK_WIN);
			// mcq_end
			// mcq_1_更新挑战者失败后Session的乐币数和经验值 时间 2006-6-11
			updatePKRightInfo(Constants.RANK_LOSE);
			// mcq_end
		}
		gong3.setRightUserId(loginUser.getId());
		this.dealResult(gameResult, gong3, changeWager);

		String set = "right_cards = '" + gong3.getRightCardsStr()
				+ "', end_datetime = now(), mark = " + WGamePKBean.PK_MARK_END
				+ ", win_user_id = " + winUserId + ", right_viewed = 1, wager="
				+ gong3.getWager();
		condition = "id = " + gong3.getId();
		pkService.updateWGamePK(set, condition);

		// **更新用户状态**
		// **加入消息系统**

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getPKRightNoticeTitle(loginUser.getNickName(), "三公"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(gong3.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamepk/3gong/viewPK.jsp?id="
				+ gong3.getId());
		// macq_2007-5-16_增加人人对战PK游戏消息类型_start
		// notice.setMark(NoticeBean.PK_GAME);
		// macq_2007-5-16_增加人人对战PK游戏消息类型_end
		noticeService.addNotice(notice);

		gong3.setWinUserId(winUserId);
		gong3.setWager(StringUtil.toInt(changeWager + ""));
		request.setAttribute("gong3", gong3);
		request.setAttribute("cardList", cardList);
		request.setAttribute("cardList1", cardList1);
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
	 * 看坐庄结果。
	 * 
	 * @param request
	 * @return
	 */
	public void viewWGamePK(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			return;
		}

		String condition = "id = " + id;
		WGamePKBean pk = pkService.getWGamePK(condition);
		request.setAttribute("pk", pk);
	}

	/**
	 * @param request
	 */
	public void history(HttpServletRequest request) {
		HistoryBean history = getHistory(loginUser.getId(), WGameBean.GT_PK,
				WGameBean.PK_GONG3);
		request.setAttribute("history", history);
	}

	public void dealResult(int result, WGamePKBean pkBean,
			long challengerLoseWager) {
		UserBean banker = getUser(pkBean.getLeftUserId());// 庄家
		long cWager = (long)(pkBean.getWager()*5);
		UserBean challenger = loginUser;// 挑战者
		if (result == DOGFALL) {// 平局
			incUserGamePoint(banker, cWager);
			recordPKGame(banker, WGameBean.PK_GONG3, 0);
			recordPKGame(challenger, WGameBean.PK_GONG3, 0);
		} else if (result == BANKER_WIN) {// 庄家赢
			incUserGamePoint(banker,
					(long) (cWager + challengerLoseWager));
			decUserGamePoint(challenger, challengerLoseWager);
			recordPKGame(banker, WGameBean.PK_GONG3, challengerLoseWager);
			recordPKGame(challenger, WGameBean.PK_GONG3, -challengerLoseWager);
		} else {// 庄家输
			incUserGamePoint(banker, cWager-challengerLoseWager);
			incUserGamePoint(challenger, challengerLoseWager);
			recordPKGame(banker, WGameBean.PK_GONG3, -challengerLoseWager);
			recordPKGame(challenger, WGameBean.PK_GONG3, challengerLoseWager);
		}
	}

	
	public boolean validate(WGamePKBean pkBean, HttpServletRequest request) {
		if (pkBean == null) {
			request.setAttribute("tip", "该局已被取消");
			request.setAttribute("result", "failure");
			return false;

		}
		if (pkBean.getMark() == WGamePKBean.PK_MARK_END) {
			request.setAttribute("tip", "该局已经结束");
			request.setAttribute("result", "failure");
			return false;

		}
		UserStatusBean us = getUserStatus(loginUser.getId());
		loginUser.setUs(us);
		// 乐币不够
		// fanys206-08-11
		if (UserInfoUtil.getUserStatus(loginUser.getId()).getGamePoint() < pkBean
				.getWager()*5) {
			// if (loginUser.getUs().getGamePoint() < pkBean.getWager()) {
			request.setAttribute("tip", "对不起，按三公规则你有可能输掉5倍的乐币，挑庄或应战时需要准备赌注5倍的乐币");
			request.setAttribute("result", "failure");
			return false;
		}
		request.setAttribute("result", "success");
		return true;
	}
}
