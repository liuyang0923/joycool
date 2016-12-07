/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.action.wgamepk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgame.WGamePKBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.StaticCacheMap;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.NoticeServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.service.impl.WGamePKServiceImpl;
import net.joycool.wap.service.impl.WGameServiceImpl;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class PKBaseAction extends CustomAction{
	protected static WGamePKServiceImpl pkService = new WGamePKServiceImpl();

	protected static UserServiceImpl userService = new UserServiceImpl();

	protected static WGameServiceImpl wgService = new WGameServiceImpl();

	protected static NoticeServiceImpl noticeService = new NoticeServiceImpl();
	
	public static ICacheMap wgamepkCache = CacheManage.addCache(new StaticCacheMap(15), "wgamepk");

	protected UserBean loginUser;

	protected UserBean leftUser;

	// protected IFriendLevelService friendLevel;

	public PKBaseAction(HttpServletRequest request) {
		super(request);
		loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	}

	/**
	 * 取得顶部。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String getTop(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuffer top = new StringBuffer();
		return top.toString();
	}

	public static UserStatusBean getUserStatus(int userId) {
		// fanys2006-08-11
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		// UserStatusBean status = userService
		// .getUserStatus("user_id = " + userId);
		if (status != null) {
			return status;
		} else {
			status = new UserStatusBean();
			status.setUserId(userId);
			status.setGamePoint(10000);
			status.setPoint(100);
			userService.addUserStatus(status);
			// add by mcq 2006-07-24 for stat user money history start
			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER, 10000,
					Constants.PLUS, userId);
			// add by mcq 2006-07-24 for stat user money history end
			return status;
		}
	}

	public static UserBean getUser(int userId) {
		// fanys2006-08-11
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		// System.out.println(status);
		// UserStatusBean status = userService
		// .getUserStatus("user_id = " + userId);
		// UserBean user = userService.getUser("id = " + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean user = UserInfoUtil.getUser(userId);
		// System.out.println(user);
		if (user != null) {
			user.setUs(status);
		}
		return user;
		// } else {
		// status = new UserStatusBean();
		// status.setUserId(userId);
		// status.setGamePoint(10000);
		// status.setPoint(100);
		// userService.addUserStatus(status);
		// return status;
		// }
	}

	public HistoryBean getHistory(int userId, int gameType, int gameId) {
		String condition = "user_id = " + userId + " and game_type = "
				+ gameType + " and game_id = " + gameId;
		HistoryBean history = wgService.getHistory(condition);
		if (history != null) {
			return history;
		} else {
			history = new HistoryBean();
			history.setUserId(userId);
			history.setGameType(gameType);
			history.setGameId(gameId);
			wgService.addHistory(history);
			return history;
		}
	}

	public void getRandImg(HttpServletRequest request) {
		String fileUrl = null;
		int catalogId = StringUtil.toInt(request.getParameter("type"));
		if (catalogId == -1) {
			catalogId = 91;
		}

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select file_url from ppicture where catalog_id = "
				+ catalogId + " order by rand() limit 0, 1";
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				fileUrl = "/rep/picture/"
						+ rs.getString("file_url");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();

		request.setAttribute("fileUrl", fileUrl);
	}

	/**
	 * 统计战绩。
	 * 
	 * @param history
	 * @return
	 */
	public void updateHistory(HistoryBean history) {
		String condition = "user_id = " + history.getUserId()
				+ " and game_type = " + history.getGameType()
				+ " and game_id = " + history.getGameId();
		int count = wgService.getHistoryCount(condition);
		// 已经添加
		if (count > 0) {
			String set = "win_count = win_count + " + history.getWinCount()
					+ ", draw_count = draw_count + " + history.getDrawCount()
					+ ", lose_count = lose_count + " + history.getLoseCount()
					+ ", money = money + " + history.getMoney();
			wgService.updateHistory(set, condition);
		}
		// 尚未添加
		else {
			wgService.addHistory(history);
		}
	}

	/**
	 * 取得挑战坐庄消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameId
	 */
	public String getChlStartNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "挑战你坐庄的" + gameName + "赌局!";
	}

	/**
	 * 取得强行PK消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameName
	 * @return
	 */
	public String getPKNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "向你挑战" + gameName;
	}

	/**
	 * 取得强行PK应战消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameName
	 * @return
	 */
	public String getPKRightNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "应战你挑战的" + gameName;
	}

	/**
	 * 取得超时没应战消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameName
	 * @return
	 */
	public String getPKTimeoutNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "超时没应战你挑战的" + gameName;
	}

	/**
	 * 更改PK挑战者显示结果后,更改session中乐币数和更新经验值.
	 */
	public void updatePKRightInfo(int point) {
		// fanys2006-08-11start
		// mcq_1_用户更改过状态信息时间:2006-6-11
		// UserInfoUtil.addUserInfo(loginUser.getId());
		// // mcq_1_判断用户乐币是否更改过 时间:2006-6-11
		// loginUser = UserInfoUtil.getUserInfo(loginUser);
		// mcq_1_增加用户经验值 时间:2006-6-11
		// fanys2006-08-11end
		RankAction.addPoint(loginUser, point);
		// mcq_end
	}

	/**
	 * 更改PK挑战者挑战打平后,更改session中经验值.
	 */
	public void updatePKDrawRightInfo() {
		// mcq_1_判断用户乐币是否更改过 时间:2006-6-11
		// fanys2006-08-11
		// loginUser = UserInfoUtil.getUserInfo(loginUser);
		// mcq_1_增加用户经验值 时间:2006-6-11
		RankAction.addPoint(loginUser, Constants.RANK_DRAW);
		// mcq_end
	}

	/**
	 * 取消坐庄,更改session中经验值.
	 */
	public void updatePKCancelInfo() {
		// fanys2006-08-11
		// mcq_1_用户更改过状态信息 时间:2006-6-11
		// UserInfoUtil.addUserInfo(loginUser.getId());
		// mcq_1_判断用户乐币是否更改过 时间:2006-6-11
		// loginUser = UserInfoUtil.getUserInfo(loginUser);
	}

	/**
	 * mcq_1_更改庄家状态相关状态 时间:2006-6-11
	 */
	public void updateLeftInfo(int userId, int point) {
		// fanys2006-08-11
		// UserInfoUtil.addUserInfo(userId);
		leftUser = getUser(userId);
		RankAction.addPoint(leftUser, point);
	}

	/**
	 * 增加登录用户的乐币数
	 * 
	 * @param gamePoint
	 */
	public void decLoginUserGamePoint(int gamePoint) {
		decUserGamePoint(loginUser, gamePoint);
	}

	/**
	 * 增加用户的乐币数
	 * 
	 * @param user
	 * @param gamePoint
	 */
	public void decUserGamePoint(UserBean user, long gamePoint) {
		// fanys2006-08-11
		UserInfoUtil.updateUserCash(user.getId(), -gamePoint, UserCashAction.WAGER,
				"PK单回合游戏扣除用户乐币" + gamePoint);
	}

	/**
	 * 减少登录用户乐币数
	 * 
	 * @param gamePoint
	 */
	public void incLoginUserGamePoint(int gamePoint) {
		incUserGamePoint(loginUser, gamePoint);
	}

	/**
	 * 减少登录用户乐币数
	 * 
	 * @param user
	 * @param gamePoint
	 */
	public void incUserGamePoint(UserBean user, long gamePoint) {

		// fanys2006-08-11
		UserInfoUtil.updateUserCash(user.getId(), gamePoint, UserCashAction.WAGER,
				"PK单回合游戏增加用户乐币" + gamePoint);
	}

	public static final int DOGFALL = 0;// 平局

	public static final int BANKER_WIN = 1;// 庄家赢

	public static final int BANKER_LOSE = 2;// 庄家输

	/**
	 * 处理结果
	 * 
	 * @param result
	 * @param pkBean
	 */
	public void dealResult(int result, WGamePKBean pkBean, int pkGameType) {
		int challengerLoseWager = 0;
		UserBean banker = getUser(pkBean.getLeftUserId());// 庄家
		UserBean challenger = loginUser;
		// fanys2006-08-11
		if (pkBean.getWager() > UserInfoUtil.getUserStatus(challenger.getId())
				.getGamePoint())
			// if (pkBean.getWager() > challenger.getUs().getGamePoint())
			challengerLoseWager = challenger.getUs().getGamePoint();
		else
			challengerLoseWager = pkBean.getWager();
		if (result == DOGFALL) {// 平局
			incUserGamePoint(banker, (long) pkBean.getWager());
			// 庄家记录
			recordPKGame(challenger, pkGameType, 0);
			// 挑战者记录
			recordPKGame(banker, pkGameType, 0);
		} else if (result == BANKER_WIN) {// 庄家赢
			// 给庄家加钱
			incUserGamePoint(banker, (long) pkBean.getWager()
					+ (long) challengerLoseWager);
			// macq_2007-7-9_判断挑战者是否拥有特殊道具
			int rightUserBagId = UserBagCacheUtil.getUserBagById(17, loginUser
					.getId());
			if (rightUserBagId > 1 && RandomUtil.percentRandom(20)) {
				// 更新用户行囊
				UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),
						rightUserBagId);
				// 使用特殊道具标志位
				pkBean.setFlag(1);

			} else {
				// 扣除挑战者钱
				decUserGamePoint(challenger, (long) challengerLoseWager);
				// 挑战者扣钱记录
				recordPKGame(challenger, pkGameType, -challengerLoseWager);
			}
			// 庄家赢钱记录
			recordPKGame(banker, pkGameType, challengerLoseWager);
			pkBean.setWager(challengerLoseWager);
		} else {// 庄家输
			// 给挑战者加钱
			incUserGamePoint(challenger, (long) pkBean.getWager());
			// macq_2007-7-9_判断庄家是否拥有特殊道具
			int leftUserBagId = UserBagCacheUtil.getUserBagById(17, pkBean
					.getLeftUserId());
			if (leftUserBagId > 1 && RandomUtil.percentRandom(20)) {
				// 退还庄家下注
				incUserGamePoint(banker, (long) pkBean.getWager());
				// 更新用户行囊
				UserBagCacheUtil.UseUserBagCacheById(banker.getId(),
						leftUserBagId);
				// 使用特殊道具标志位
				pkBean.setFlag(1);
			} else {
				// 庄家扣钱记录
				recordPKGame(banker, pkGameType, -pkBean.getWager());
			}
			// 挑战者赢钱记录
			recordPKGame(challenger, pkGameType, pkBean.getWager());
		}
		// int challengerLoseWager = 0;
		//
		// UserBean banker = getUser(pkBean.getLeftUserId());// 庄家
		// UserBean challenger = loginUser;
		// // fanys2006-08-11
		// if (pkBean.getWager() >
		// UserInfoUtil.getUserStatus(challenger.getId())
		// .getGamePoint())
		// // if (pkBean.getWager() > challenger.getUs().getGamePoint())
		// challengerLoseWager = challenger.getUs().getGamePoint();
		// else
		// challengerLoseWager = pkBean.getWager();
		// if (result == DOGFALL) {// 平局
		// incUserGamePoint(banker, (long)pkBean.getWager());
		// // 庄家记录
		// recordPKGame(challenger, pkGameType, 0);
		// // 挑战者记录
		// recordPKGame(banker, pkGameType, 0);
		// } else if (result == BANKER_WIN) {// 庄家赢
		//
		// incUserGamePoint(banker, (long)pkBean.getWager() +
		// (long)challengerLoseWager);
		// decUserGamePoint(challenger, (long)challengerLoseWager);
		// // 庄家赢钱记录
		// recordPKGame(banker, pkGameType, challengerLoseWager);
		// // 挑战者扣钱记录
		// recordPKGame(challenger, pkGameType, -challengerLoseWager);
		// pkBean.setWager(challengerLoseWager);
		// } else {// 庄家输
		// incUserGamePoint(challenger, (long)pkBean.getWager());
		// // 庄家扣钱记录
		// recordPKGame(challenger, pkGameType, pkBean.getWager());
		// // 挑战者赢钱记录
		// recordPKGame(banker, pkGameType, -pkBean.getWager());
		// }
	}

	/**
	 * 保存PK记录
	 * 
	 * @param user
	 * @param pkGameType
	 * @param wager
	 */
	public void recordPKGame(UserBean user, int pkGameType, long wager) {
		HistoryBean history = new HistoryBean();
		history.setUserId(user.getId());
		history.setGameType(WGameBean.GT_PK);
		history.setGameId(pkGameType);
		if (wager < 0)
			history.setLoseCount(1);
		else if (wager > 0)
			history.setWinCount(1);
		else
			history.setDrawCount(1);
		history.setMoney(wager);
		updateHistory(history);
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
				.getWager()) {
			// if (loginUser.getUs().getGamePoint() < pkBean.getWager()) {
			request.setAttribute("tip", "您的乐币不够");
			request.setAttribute("result", "failure");
			return false;
		}
		request.setAttribute("result", "success");
		return true;
	}
	// 通过缓存获取当前坐庄列表
	public static LinkedList getWGamePKList(int gameId) {
		Integer key = Integer.valueOf(gameId);
		synchronized(wgamepkCache) {
			LinkedList bkList = (LinkedList)wgamepkCache.get(key);
			if(bkList != null)
				return bkList;

			List list = pkService.getWGamePKList("mark=1 and game_id=" + gameId + " order by id desc");
			if(list != null) {
				bkList = new LinkedList(list);
				wgamepkCache.put(key, bkList);
			}

			return bkList;
		}
	}
	// 开始坐庄，加入到列表
	public static boolean addWGamePK(WGamePKBean bean) {
		if(!pkService.addWGamePK(bean))
			return false;
		LinkedList list = getWGamePKList(bean.getGameId());
		if(list == null)
			return true;
		synchronized(list) {
			list.addFirst(bean);
		}
		return true;
	}
	// 从坐庄列表删除
	public static void endWGamePK(WGamePKBean bean) {
		LinkedList list = getWGamePKList(bean.getGameId());
		if(list == null)
			return;
		synchronized(list) {
			Iterator iter = list.iterator();
			while(iter.hasNext()) {
				WGamePKBean w = (WGamePKBean)iter.next();
				if(w.getId() == bean.getId())
					iter.remove();
			}
		}
	}
	// 判断是否可以坐庄（每人限5个）
	public static boolean canBk(int userId, int gameId) {
		LinkedList list = getWGamePKList(gameId);
		if(list.size() < WGameBean.MAX_BK_COUNT)
			return true;
		int sum = 0;
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			WGamePKBean w = (WGamePKBean)iter.next();
			if(w.getLeftUserId() == userId)
				sum++;
		}
		return sum < WGameBean.MAX_BK_COUNT;
	}
}
