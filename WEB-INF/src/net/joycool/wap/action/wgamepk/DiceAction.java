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
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgame.WGamePKBean;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class DiceAction extends PKBaseAction {
	public static int BK_NUMBER_PER_PAGE = 5;

	public static int ONLINE_NUMBER_PER_PAGE = 8;
	
	final static int MAX_GAME_POINT = 1000000000;	// 最大下注金额 

	public DiceAction(HttpServletRequest request) {
		super(request);
	}
	public static byte[] lock = new byte[0];
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
//		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		// int pageIndex1 =
//		// StringUtil.toInt(request.getParameter("pageIndex1"));
//		if (pageIndex < 0) {
//			pageIndex = 0;
//		}
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
//		String condition = "mark = " + WGamePKBean.PK_MARK_BKING
//				+ " and game_id = " + WGameBean.PK_DICE;
//		// 取得总数
//		int totalBkCount = pkService.getWGamePKCount(condition);
//		int totalBkPageCount = totalBkCount / BK_NUMBER_PER_PAGE;
//		if (totalBkCount % BK_NUMBER_PER_PAGE != 0) {
//			totalBkPageCount++;
//		}
//		if (pageIndex > totalBkPageCount - 1) {
//			pageIndex = totalBkPageCount - 1;
//		}
//		if (pageIndex < 0) {
//			pageIndex = 0;
//		}

		/**
		 * 取得庄家列表
		 */
//		condition += " order by " + orderBy + " desc limit " + pageIndex
//				* BK_NUMBER_PER_PAGE + ", " + BK_NUMBER_PER_PAGE;
//		Vector bkList = pkService.getWGamePKList(condition);

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
		 * ONLINE_NUMBER_PER_PAGE; // 李北金_2006-06-20_查询优化_end Vector userList =
		 * userService.getUserList(condition);
		 */
		/**
		 * 取得玩家列表
		 */
		// zhul 2006-10-18 优化获取在线用户 start
		// 获取所有在线用户的正序序列
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
//		liuyi 2006-12-02 程序优化 start
		//ArrayList onlineUser=OnlineUtil.getAllOnlineUser();
		List onlineUserIds = UserInfoUtil.getOnlineUserOrderByPKFromCache();
		//zhul 2006-10-20 按PK度进行排序
		
		//分页
		int totalOnlineCount =onlineUserIds.size();
		int totalOnlinePageCount = (totalOnlineCount + ONLINE_NUMBER_PER_PAGE - 1) / ONLINE_NUMBER_PER_PAGE;
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 > totalOnlinePageCount - 1) {
			pageIndex1 = totalOnlinePageCount - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}

		int start=pageIndex1*ONLINE_NUMBER_PER_PAGE;
		int end = pageIndex1*ONLINE_NUMBER_PER_PAGE+ONLINE_NUMBER_PER_PAGE;
		//玩家列表
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
		//liuyi 2006-12-02 程序优化 end	
		
		Iterator itr = userList.iterator();
		UserBean user = null;
		while (itr.hasNext()) {
			user = (UserBean) itr.next();
			user.setUs(getUserStatus(user.getId()));
		}

//		String prefixUrl = "index.jsp?pageIndex1=" + pageIndex1;
		String prefixUrl1 = "index.jsp";

//		request.setAttribute("totalBkCount", new Integer(totalBkCount));
//		request.setAttribute("totalBkPageCount", new Integer(totalBkPageCount));
//		request.setAttribute("pageIndex", new Integer(pageIndex));
//		request.setAttribute("prefixUrl", prefixUrl);
//		request.setAttribute("bkList", bkList);

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
			if (!canBk(loginUser.getId(), WGameBean.PK_DICE)) {
				tip = "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个掷骰子赌局,不能再坐庄";
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
		
		if (!canBk(loginUser.getId(), WGameBean.PK_DICE)) {
			request.setAttribute("tip", "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个掷骰子赌局,不能再坐庄");
			request.setAttribute("result", "failure");
			return;
		}
		
		synchronized(loginUser.getLock()) {
			UserStatusBean us = getUserStatus(loginUser.getId());
			// 乐币不够
			if (us.getGamePoint() < wager) {
				request.setAttribute("tip", "对不起,您乐币不够");
				request.setAttribute("result", "failure");
				return;
			} else if (wager < 10) {	// 乐币太少
				request.setAttribute("tip", "至少要下注10个乐币");
				request.setAttribute("result", "failure");
				return;
			} else if (wager > MAX_GAME_POINT) {
				request.setAttribute("tip", "最多能下注" + MAX_GAME_POINT + "乐币");
				request.setAttribute("result", "failure");
				return;
			}
	
			this.decLoginUserGamePoint(wager);
		}
		WGamePKBean dice = new WGamePKBean();
		dice.setGameId(WGameBean.PK_DICE);
		dice.setLeftUserId(loginUser.getId());
		dice.setLeftNickname(loginUser.getNickName());

		int[] dices = getRandomDices();

		dice.setLeftDices(dices);
		dice.setLeftCardsStr(dices[0] + "," + dices[1] + "," + dices[2]);
		dice.setMark(WGamePKBean.PK_MARK_BKING);
		dice.setWager(wager);
		if (!addWGamePK(dice)) {
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("dice", dice);

	}
	
	public static int[] getRandomDices() {
		return RandomUtil.nextInts(3, 1, 7);
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
		WGamePKBean dice = pkService.getWGamePK(condition);
		UserStatusBean us = getUserStatus(loginUser.getId());
		if (dice != null) {
			this.incLoginUserGamePoint(dice.getWager());
			int wager = dice.getWager() / 10;
			if (wager > us.getGamePoint()) {
				wager = us.getGamePoint();
			}
			request.setAttribute("wager", new Integer(wager));

			// 扣掉乐币
			this.decLoginUserGamePoint(wager);
			pkService.deleteWGamePK("id = " + dice.getId());

			// mcq_1_更改session中用户乐币数
			updatePKCancelInfo();
			// mcq_end

		}

		request.setAttribute("dice", dice);
	}

	/**
	 * 坐庄处理。
	 * 
	 * @param request
	 */
	public void chlStart(HttpServletRequest request) {
		int gameResult = 0;
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
		String condition = "id = " + bkId + " and game_id = " + WGameBean.PK_DICE;
		WGamePKBean dice;
		synchronized(lock) {
			dice = pkService.getWGamePK(condition);
			if (validate(dice, request) == false)
				return;
	
			int[] dices = getRandomDices();
	
			String rightDicesStr = dices[0] + "," + dices[1] + "," + dices[2];
			int[] leftDices = dice.getLeftDices();
			int leftDicesCount = leftDices[0] + leftDices[1] + leftDices[2];
			int rightDicesCount = dices[0] + dices[1] + dices[2];
			int winUserId = 0;
			// 庄家赢
			if (leftDicesCount > rightDicesCount) {
				gameResult = BANKER_WIN;
				updateLeftInfo(dice.getLeftUserId(), Constants.RANK_WIN);
				// mcq_end
				// mcq_1_更新挑战者失败后Session的乐币数和经验值 时间 2006-6-11
				updatePKRightInfo(Constants.RANK_LOSE);
				// mcq_end
				winUserId = dice.getLeftUserId();
			}
			// 庄家输
			else if (leftDicesCount < rightDicesCount) {
				gameResult = BANKER_LOSE;
				winUserId = loginUser.getId();
				// mcq_1_添加坐庄的信息变更
				updateLeftInfo(dice.getLeftUserId(), Constants.RANK_WIN);
				// mcq_end
				// mcq_1_更新挑战者胜利后Session的乐币数和经验值 时间 2006-6-11
				updatePKRightInfo(Constants.RANK_WIN);
				// mcq_end
			} else {
				gameResult = DOGFALL;
	
				// 打平后更新庄家等级积分
				updateLeftInfo(dice.getLeftUserId(), Constants.RANK_DRAW);
				// mcq_end
				// 打平后更新用户等级积分
				updatePKDrawRightInfo();
				// mcq_end
			}
			dice.setRightUserId(loginUser.getId());
			int wager = dice.getWager();
			this.dealResult(gameResult, dice, WGameBean.PK_DICE);
			dice.setRightDices(dices);
			dice.setRightUserId(loginUser.getId());
			dice.setRightNickname(loginUser.getNickName());
			dice.setWinUserId(winUserId);
	
			String set = "right_user_id = " + loginUser.getId()
					+ ", right_nickname = '" + StringUtil.toSql(loginUser.getNickName())
					+ "', right_cards = '" + StringUtil.toSql(rightDicesStr)
					+ "', end_datetime = now(), mark = " + WGamePKBean.PK_MARK_END
					+ ", win_user_id = " + winUserId + ", right_viewed = 1, wager="
					+ wager + ", flag = " + dice.getFlag();
			condition = "id = " + bkId;
			if (!pkService.updateWGamePK(set, condition)) {
				return;
			}
			endWGamePK(dice);
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("dice", dice);

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getChlStartNoticeTitle(loginUser.getNickName(), "掷骰子"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(dice.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamepk/dice/viewPK.jsp?id="
				+ dice.getId());
		//macq_2007-5-16_增加人人对战PK游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME);
		//macq_2007-5-16_增加人人对战PK游戏消息类型_end
		noticeService.addNotice(notice);
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
				// zhul 2006-10-17 优化用户信息查询 start
				if (OnlineUtil.isOnline(userId + ""))
					enemy = UserInfoUtil.getUser(userId);
				// zhul 2006-10-17 优化用户信息查询 end
				// 李北金_2006-06-20_查询优化_end
				if (enemy == null) {
					tip = "对不起,对方已下线";
					result = "failure";
				} else if (pkService.getWGamePKCount("right_user_id = "
						+ userId + " and game_id = " + WGameBean.PK_DICE
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
			condition = "join jc_online_user on user_info.id=jc_online_user.user_id where jc_online_user.user_id = "
					+ userId;
			enemy = userService.getUser(condition);
			// if(OnlineUtil.isOnline(userId+""))
			// enemy=UserInfoUtil.getUser(userId);
			// 李北金_2006-06-20_查询优化_end
			if (enemy == null) {
				tip = "对不起,对方已下线";
				result = "failure";
			} else if (pkService.getWGamePKCount("right_user_id = " + userId
					+ " and game_id = " + WGameBean.PK_DICE + " and mark = "
					+ WGamePKBean.PK_MARK_PKING) > 0) {
				tip = "对不起,对方已经被PK";
				result = "failure";
			}
		}

		int wager = StringUtil.toInt(request.getParameter("wager"));
		UserStatusBean us = getUserStatus(loginUser.getId());
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
		}
		// 李北金_2006-06-27_去掉PK上限
		else if (wager > MAX_GAME_POINT) {
			tip = "最多能下注" + MAX_GAME_POINT + "乐币";
			result = "failure";
		}
		// 李北金_2006-06-27_去掉PK上限
		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
		}
		// 发牌
		else {
			this.decLoginUserGamePoint(wager);
			WGamePKBean dice = new WGamePKBean();
			dice.setLeftUserId(loginUser.getId());
			dice.setLeftNickname(loginUser.getNickName());

			int[] dices = getRandomDices();

			dice.setLeftDices(dices);
			dice.setLeftCardsStr(dices[0] + "," + dices[1] + "," + dices[2]);
			dice.setMark(WGamePKBean.PK_MARK_PKING);
			dice.setWager(wager);
			dice.setRightUserId(enemy.getId());
			dice.setRightNickname(enemy.getNickName());
			dice.setGameId(WGameBean.PK_DICE);
			if (!pkService.addWGamePK(dice)) {
				return;
			}
			dice = pkService.getWGamePK("left_user_id = "
					+ dice.getLeftUserId() + " and right_user_id = "
					+ dice.getRightUserId() + " and mark = "
					+ WGamePKBean.PK_MARK_PKING + " and game_id = "
					+ WGameBean.PK_DICE);
			result = "success";

			// **更新用户状态**
			// **加入消息系统**

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getPKNoticeTitle(loginUser.getNickName(), "掷骰子"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(dice.getRightUserId());
			notice.setHideUrl("");
			notice.setLink("/wgamepk/dice/pkRightStart.jsp?pkId=" + dice.getId());
			//macq_2007-5-16_增加人人对战PK游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME);
			//macq_2007-5-16_增加人人对战PK游戏消息类型_end
			noticeService.addNotice(notice);

			request.setAttribute("enemy", enemy);
			request.setAttribute("enemyUs", enemyUs);
			request.setAttribute("result", result);
			request.setAttribute("dice", dice);
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
		WGamePKBean dice = pkService.getWGamePK(condition);

		request.setAttribute("dice", dice);
	}

	/**
	 * 应战处理。
	 * 
	 * @param request
	 */
	public void pkRightDeal1(HttpServletRequest request) {
		int gameResult = 0;
		int pkId = StringUtil.toInt(request.getParameter("pkId"));
		String condition = "id = " + pkId + " and mark = "
				+ WGamePKBean.PK_MARK_PKING;
		WGamePKBean dice = pkService.getWGamePK(condition);
		// if (dice == null) {
		// return;
		// }
		if (validate(dice, request) == false)
			return;

		// 挑战者的骰子
		int[] dices = getRandomDices();

		dice.setRightDices(dices);
		dice.setRightCardsStr(dices[0] + "," + dices[1] + "," + dices[2]);

		/**
		 * 判断输赢
		 */
		int[] leftDices = dice.getLeftDices();
		int leftDicesCount = leftDices[0] + leftDices[1] + leftDices[2];
		int rightDicesCount = dices[0] + dices[1] + dices[2];
		int winUserId = 0;
		// 庄家赢
		if (leftDicesCount > rightDicesCount) {
			gameResult = BANKER_WIN;
			// 庄家加上乐币
			updateLeftInfo(dice.getLeftUserId(), Constants.RANK_WIN);
			// mcq_end
			// mcq_1_更新挑战者失败后Session的乐币数和经验值 时间 2006-6-11
			updatePKRightInfo(Constants.RANK_LOSE);
			// mcq_end

			winUserId = dice.getLeftUserId();

			// HistoryBean history = new HistoryBean();
			// history.setUserId(dice.getLeftUserId());
			// history.setGameType(WGameBean.GT_PK);
			// history.setGameId(WGameBean.PK_DICE);
			// history.setWinCount(1);
			// history.setMoney(dice.getWager());
			// updateHistory(history);
			//
			// history = new HistoryBean();
			// history.setUserId(loginUser.getId());
			// history.setGameType(WGameBean.GT_PK);
			// history.setGameId(WGameBean.PK_DICE);
			// history.setLoseCount(1);
			// history.setMoney(-dice.getWager());
			// updateHistory(history);
		}
		// 庄家输
		else if (leftDicesCount < rightDicesCount) {
			gameResult = BANKER_LOSE;
			// 庄家减去乐币
			// mcq_1_添加坐庄的信息变更
			updateLeftInfo(dice.getLeftUserId(), Constants.RANK_LOSE);
			// mcq_end
			// mcq_1_更新挑战者胜利后Session的乐币数和经验值 时间 2006-6-11
			updatePKRightInfo(Constants.RANK_WIN);
			// mcq_end
			winUserId = loginUser.getId();

		} else {
			gameResult = DOGFALL;
			// 打平后更新庄家等级积分
			updateLeftInfo(dice.getLeftUserId(), Constants.RANK_DRAW);
			// mcq_end
			// 打平后更新用户等级积分
			updatePKDrawRightInfo();
			// mcq_end
		}
		dice.setRightUserId(loginUser.getId());
		int wager = dice.getWager();
		this.dealResult(gameResult, dice, WGameBean.PK_DICE);

		String set = "right_cards = '" + StringUtil.toSql(dice.getRightCardsStr())
				+ "', end_datetime = now(), mark = " + WGamePKBean.PK_MARK_END
				+ ", win_user_id = " + winUserId + ", right_viewed = 1, wager="
				+ wager + ", flag = " + dice.getFlag();
		condition = "id = " + dice.getId();
		pkService.updateWGamePK(set, condition);

		// **更新用户状态**
		// **加入消息系统**

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getPKRightNoticeTitle(loginUser.getNickName(), "掷骰子"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(dice.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamepk/dice/viewPK.jsp?id="
				+ dice.getId());
		//macq_2007-5-16_增加人人对战PK游戏消息类型_start
		//notice.setMark(NoticeBean.PK_GAME);
		//macq_2007-5-16_增加人人对战PK游戏消息类型_end
		noticeService.addNotice(notice);

		dice.setWinUserId(winUserId);
		request.setAttribute("dice", dice);
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
				WGameBean.PK_DICE);
		request.setAttribute("history", history);
	}
}
