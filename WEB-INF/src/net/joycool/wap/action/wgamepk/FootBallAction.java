/*
 * Created on 2006-3-17
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
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author macq
 * 
 */
public class FootBallAction extends PKBaseAction {
	public int BK_NUMBER_PER_PAGE = 5;

	public int ONLINE_NUMBER_PER_PAGE = 5;

	public FootBallAction(HttpServletRequest request) {
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
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码

		if (pageIndex < 0) {
			pageIndex = 0;
		}

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
				+ " and game_id = " + WGameBean.PK_FOOTBALL;
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

		// 取得在线玩家列表
		orderBy = request.getParameter("orderBy");
		if (orderBy == null) {
			orderBy = "id";
		}
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
					+ " and game_id = " + WGameBean.PK_FOOTBALL;
			if (pkService.getWGamePKCount(condition) >= WGameBean.MAX_BK_COUNT) {
				tip = "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个射门赌局,不能再坐庄";
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
		String action = (String) request.getParameter("action");
		
		String tip = null;
		String result = null;
		String condition = "left_user_id = " + loginUser.getId()
				+ " and mark = " + WGamePKBean.PK_MARK_BKING
				+ " and game_id = " + WGameBean.PK_FOOTBALL;
		if (pkService.getWGamePKCount(condition) >= WGameBean.MAX_BK_COUNT) {
			request.setAttribute("tip", "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个射门赌局,不能再坐庄");
			request.setAttribute("result", "failure");
			return;
		}
		
		synchronized(loginUser.getLock()) {
			UserStatusBean us = getUserStatus(loginUser.getId());
	
			// 乐币不够
			if (us.getGamePoint() < wager * 2) {
				request.setAttribute("tip", "对不起，根据射门规则下注时将暂扣下注额两倍的乐币，你的乐币不够");
				request.setAttribute("result", "failure");
				return;
			} else if (wager < 10) {
				request.setAttribute("tip", "至少下注10个乐币");
				request.setAttribute("result", "failure");
				return;
			} else if (wager > Constants.GAME_POINT_MAX_INT) {
				request.setAttribute("tip", "最多能下注" + Constants.GAME_POINT_MAX_INT + "乐币");
				request.setAttribute("result", "failure");
				return;
			}

			this.decLoginUserGamePoint(wager*2);
		}
		
		WGamePKBean football = new WGamePKBean();
		football.setGameId(WGameBean.PK_FOOTBALL);
		football.setLeftUserId(loginUser.getId());
		football.setLeftNickname(loginUser.getNickName());
		football.setLeftCardsStr(action);
		football.setMark(WGamePKBean.PK_MARK_BKING);
		football.setWager(wager);
		if (!pkService.addWGamePK(football)) {
			return;
		}
		result = "success";
		request.setAttribute("football", football);
		request.setAttribute("result", result);

	}

	/*
	 * public boolean userWin(String ua, String sa) { if (ua.equals("j") &&
	 * sa.equals("b")) { return true; } else if (ua.equals("s") &&
	 * sa.equals("j")) { return true; } else if (ua.equals("b") &&
	 * sa.equals("s")) { return true; } return false; }
	 */

	public String getName(String action) {
		if (action.equals("l")) {
			return "左";
		} else if (action.equals("m")) {
			return "中";
		} else if (action.equals("r")) {
			return "右";
		}
		return null;
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
		WGamePKBean football = pkService.getWGamePK(condition);
		UserStatusBean us = getUserStatus(loginUser.getId());
		if (football != null) {
			this.incLoginUserGamePoint(football.getWager()*2);
			int wager = football.getWager() / 10;
			if (wager > us.getGamePoint()) {
				wager = us.getGamePoint();
			}
			request.setAttribute("wager", new Integer(wager));
			this.decLoginUserGamePoint(wager);
			// 扣掉乐币
			pkService.deleteWGamePK("id = " + football.getId());
			// mcq_1_更改session中用户乐币数
			updatePKCancelInfo();
			// mcq_end
		}

		request.setAttribute("football", football);
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

				if (enemy == null) {
					tip = "对不起,对方已下线";
					result = "failure";
				} else if (pkService.getWGamePKCount("right_user_id = "
						+ userId + " and game_id = " + WGameBean.PK_FOOTBALL
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
					+ " and game_id = " + WGameBean.PK_FOOTBALL
					+ " and mark = " + WGamePKBean.PK_MARK_PKING) > 0) {
				tip = "对不起,对方已经被PK";
				result = "failure";
			}
		}

		String action = (String) request.getParameter("action");
		int wager = StringUtil.toInt(request.getParameter("wager"));
		UserStatusBean us = getUserStatus(loginUser.getId());

		// 乐币不够
		if (us.getGamePoint() < wager*2) {
			tip = "对不起，根据射门规则下注时将暂扣下注额两倍的乐币，你的乐币不够";
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
		else if (wager > Constants.GAME_POINT_MAX_INT) {
			tip = "最多能下注" + Constants.GAME_POINT_MAX_INT + "乐币";
			result = "failure";
		}
		// 李北金_2006-06-27_去掉PK上限
		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
		}
		// 发牌
		else {
			this.decLoginUserGamePoint(wager*2);
			WGamePKBean football = new WGamePKBean();
			football.setGameId(WGameBean.PK_FOOTBALL);
			football.setLeftUserId(loginUser.getId());
			football.setLeftNickname(loginUser.getNickName());
			football.setLeftCardsStr(action);
			football.setMark(WGamePKBean.PK_MARK_PKING);
			football.setWager(wager);
			football.setRightUserId(enemy.getId());
			football.setRightNickname(enemy.getNickName());
			if (!pkService.addWGamePK(football)) {
				return;
			}

			result = "success";

			football = pkService.getWGamePK("left_user_id = "
					+ football.getLeftUserId() + " and right_user_id = "
					+ football.getRightUserId() + " and mark = "
					+ WGamePKBean.PK_MARK_PKING + " and game_id = "
					+ WGameBean.PK_FOOTBALL);

			// **更新用户状态**
			// **加入消息系统**

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getPKNoticeTitle(loginUser.getNickName(), "射门"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(football.getRightUserId());
			notice.setHideUrl("");
			notice.setLink("/wgamepk/football/pkRightStart.jsp?pkId="
					+ football.getId());
			// macq_2007-5-16_增加人人对战PK游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME);
			// macq_2007-5-16_增加人人对战PK游戏消息类型_end
			noticeService.addNotice(notice);

			request.setAttribute("enemy", enemy);
			request.setAttribute("enemyUs", enemyUs);
			request.setAttribute("result", result);
			request.setAttribute("football", football);
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
		WGamePKBean football = pkService.getWGamePK(condition);

		request.setAttribute("football", football);
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
		WGamePKBean football = pkService.getWGamePK(condition);
		if (validate(football, request) == false)
			return;

		UserStatusBean us = getUserStatus(loginUser.getId());

		int winUserId = 0;
		String a = football.getLeftCardsStr();
		String action = request.getParameter("action");
		football.setRightCardsStr(action);
		String b = action;
		/**
		 * 判断输赢
		 */
		// 庄家输了
		int changeWager = football.getWager();
		if (a.equals(b)) {
			changeWager = football.getWager() * 2 ;
			gameResult = BANKER_LOSE;
			winUserId = loginUser.getId();
		}// 庄家赢
		else {
			winUserId = football.getLeftUserId();
			gameResult = BANKER_WIN;
		}
		football.setRightUserId(loginUser.getId());
		this.dealResult(gameResult, football, changeWager);
		// 庄家输了
		if (winUserId == loginUser.getId()) {

			winUserId = loginUser.getId();
			// mcq_1_添加坐庄的信息变更
			updateLeftInfo(football.getLeftUserId(), Constants.RANK_LOSE);
			// mcq_end
			// mcq_1_更新挑战者胜利后Session的乐币数和经验值 时间 2006-6-11
			updatePKRightInfo(Constants.RANK_WIN);
			// mcq_end
		}// 庄家赢了
		else if (winUserId == football.getLeftUserId()) {

			winUserId = football.getLeftUserId();
			// mcq_1_添加坐庄的信息变更
			updateLeftInfo(football.getLeftUserId(), Constants.RANK_WIN);
			// mcq_end
			// mcq_1_更新挑战者失败后Session的乐币数和经验值 时间 2006-6-11
			updatePKRightInfo(Constants.RANK_LOSE);
			// mcq_end
		}

		String set = "right_cards = '" + football.getRightCardsStr()
				+ "', end_datetime = now(), mark = " + WGamePKBean.PK_MARK_END
				+ ", win_user_id = " + winUserId + ", right_viewed = 1, wager="
				+ football.getWager() + ", flag = " + football.getFlag();
		condition = "id = " + football.getId();
		pkService.updateWGamePK(set, condition);

		// **更新用户状态**
		// **加入消息系统**

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getPKRightNoticeTitle(loginUser.getNickName(), "射门"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(football.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamepk/football/viewPK.jsp?id=" + football.getId());
		// macq_2007-5-16_增加人人对战PK游戏消息类型_start
		// notice.setMark(NoticeBean.PK_GAME);
		// macq_2007-5-16_增加人人对战PK游戏消息类型_end
		noticeService.addNotice(notice);

		football.setWinUserId(winUserId);
		request.setAttribute("football", football);
	}

	/**
	 * 坐庄处理。
	 * 
	 * @param request
	 */

	public void chlStart(HttpServletRequest request) {
		int gameResult = 0;

		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		String action = (String) request.getParameter("action");
		if (bkId == -1) {
			return;
		}
		String condition = "id = " + bkId + " and game_id = " + WGameBean.PK_FOOTBALL;
		WGamePKBean football;
		synchronized(lock) {
			football = pkService.getWGamePK(condition);
			if (validate(football, request) == false) {
				return;
			}
			// if (football == null) {
			// return;
			// }
			// if (football.getMark() == WGamePKBean.PK_MARK_END) {
			// return;
			// }
			UserStatusBean us = getUserStatus(loginUser.getId());
	
			String a = football.getLeftCardsStr();
	
			// 生成挑战者的牌
			football.setRightCardsStr(action);
			String b = football.getRightCardsStr();
	
			/**
			 * 判断输赢
			 */
			// 庄家输了
			int winUserId = 0;
			int changeWager=football.getWager();
			if (a.equals(b)) {
				changeWager = football.getWager() * 2 ;
				winUserId = loginUser.getId();
				gameResult = BANKER_LOSE;
			}// 庄家赢了
			else {
				winUserId = football.getLeftUserId();
				gameResult = BANKER_WIN;
			}
			football.setRightUserId(loginUser.getId());
			this.dealResult(gameResult, football, changeWager);
			// 庄家输了
			if (winUserId == loginUser.getId()) {
				winUserId = loginUser.getId();
				// mcq_1_添加坐庄的信息变更
				updateLeftInfo(football.getLeftUserId(), Constants.RANK_LOSE);
				// mcq_end
				// mcq_1_更新挑战者胜利后Session的乐币数和经验值 时间 2006-6-11
				updatePKRightInfo(Constants.RANK_WIN);
				// mcq_end
			}// 庄家赢了
			else if (winUserId == football.getLeftUserId()) {
				updateLeftInfo(football.getLeftUserId(), Constants.RANK_WIN);
				// mcq_end
				// mcq_1_更新挑战者失败后Session的乐币数和经验值 时间 2006-6-11
				updatePKRightInfo(Constants.RANK_LOSE);
				// mcq_end
			}
			football.setRightUserId(loginUser.getId());
			football.setRightNickname(loginUser.getNickName());
			football.setWinUserId(winUserId);
	
			String set = "right_user_id = " + loginUser.getId()
					+ ", right_nickname = '" + StringUtil.toSql(loginUser.getNickName())
					+ "', right_cards = '" + football.getRightCardsStr()
					+ "', end_datetime = now(), mark = " + WGamePKBean.PK_MARK_END
					+ ", win_user_id = " + winUserId + ", right_viewed = 1, wager="
					+ football.getWager() + ", flag = " + football.getFlag();
			condition = "id = " + bkId;
			if (!pkService.updateWGamePK(set, condition)) {
				return;
			}
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getChlStartNoticeTitle(loginUser.getNickName(), "射门"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(football.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamepk/football/viewPK.jsp?id=" + football.getId());
		// macq_2007-5-16_增加人人对战PK游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME);
		// macq_2007-5-16_增加人人对战PK游戏消息类型_end
		noticeService.addNotice(notice);
		String result = "success";
		request.setAttribute("result", result);
		request.setAttribute("football", football);
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
	 * 挑战者选择手式。
	 * 
	 * @param request
	 */

	public void pkOut(HttpServletRequest request) {
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		String tip = null;
		String result = null;
		if (bkId == -1) {
			tip = "该局已经被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		String condition = "id = " + bkId;
		WGamePKBean football = pkService.getWGamePK(condition);
		if (football == null) {
			tip = "该局已经被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (football.getMark() == WGamePKBean.PK_MARK_END) {
			tip = "该局已经结束";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		UserStatusBean us = getUserStatus(loginUser.getId());
		// 乐币不够
		if (us.getGamePoint() < football.getWager()) {
			tip = "您的乐币不够";
			result = "failure";
		}

		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
		}
		// 发牌
		else {
			String bkIdStr = String.valueOf(bkId);
			result = "success";
			request.setAttribute("result", result);
			request.setAttribute("bkId", bkIdStr);
		}
	}

	/**
	 * @param request
	 */
	public void history(HttpServletRequest request) {
		HistoryBean history = getHistory(loginUser.getId(), WGameBean.GT_PK,
				WGameBean.PK_FOOTBALL);
		request.setAttribute("history", history);
	}

	public void dealResult(int result, WGamePKBean pkBean, int challengerLoseWager) {
		UserBean banker = getUser(pkBean.getLeftUserId());// 庄家
		UserBean challenger = loginUser;
		if (result == DOGFALL) {// 平局
			incUserGamePoint(banker, (long) pkBean.getWager());
			// 庄家记录
			recordPKGame(challenger, WGameBean.PK_FOOTBALL, 0);
			// 挑战者记录
			recordPKGame(banker, WGameBean.PK_FOOTBALL, 0);
		} else if (result == BANKER_WIN) {// 庄家赢
			// 给庄家加钱
			incUserGamePoint(banker, pkBean.getWager()*2 +  pkBean.getWager());
			// 扣除挑战者钱
			decUserGamePoint(challenger, pkBean.getWager());
			// 挑战者扣钱记录
			recordPKGame(challenger, WGameBean.PK_FOOTBALL, -pkBean.getWager());
			// 庄家赢钱记录
			recordPKGame(banker, WGameBean.PK_FOOTBALL, pkBean.getWager());
		} else {// 庄家输
			// 给挑战者加钱
			incUserGamePoint(challenger, challengerLoseWager);
			// 庄家扣钱记录
			recordPKGame(banker, WGameBean.PK_FOOTBALL, -challengerLoseWager);
			// 挑战者赢钱记录
			recordPKGame(challenger, WGameBean.PK_FOOTBALL, challengerLoseWager);
		}
	}
}
