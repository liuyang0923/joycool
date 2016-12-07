/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.action.wgamehall;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.service.infc.IWGameHallService;
import net.joycool.wap.service.infc.IWGameService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class HallBaseAction {
	protected IWGameHallService hallService;

	protected IWGameService wgService;

	protected UserBean loginUser;

	protected IUserService userService;

	protected INoticeService noticeService;

	protected UserBean user;

	public HallBaseAction(HttpServletRequest request) {
		hallService = ServiceFactory.createWGameHallService();
		wgService = ServiceFactory.createWGameService();
		userService = ServiceFactory.createUserService();
		noticeService = ServiceFactory.createNoticeService();
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		// 判断用户乐币是否更改过
//		loginUser = UserInfoUtil.getUserInfo(loginUser);
	}

	public HallBaseAction() {
	}

	public UserStatusBean getUserStatus(int userId) {
		IUserService userService = ServiceFactory.createUserService();
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

	public UserStatusBean getUserStatus() {
		return getUserStatus(loginUser.getId());
	}

	// public void updateHistory(int userId, int result, int gameId) {
	// String condition = "user_id = " + userId + " and game_id = " + gameId;
	// HallHistoryBean history = hallService.getHallHistory(condition);
	// if (history == null) {
	// history = new HallHistoryBean();
	// switch (result) {
	// case 0:
	// history.setLoseCount(1);
	// break;
	// case 1:
	// history.setWinCount(1);
	// break;
	// case 2:
	// history.setDrawCount(1);
	// break;
	// }
	// history.setUserId(userId);
	// history.setGameId(gameId);
	// hallService.addHallHistory(history);
	// } else {
	// String set = null;
	// switch (result) {
	// case 0:
	// set = "lose_count = lose_count + 1";
	// break;
	// case 1:
	// set = "win_count = win_count + 1";
	// break;
	// case 2:
	// set = "draw_count = draw_count + 1";
	// break;
	// }
	// condition = "id = " + history.getId();
	// hallService.updateHallHistory(set, condition);
	// }
	// }

	/**
	 * 统计战绩。
	 * 
	 * @param history
	 * @return
	 */
	public static void updateHistory(HistoryBean history) {
		IWGameService wgService = ServiceFactory.createWGameService();
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
	 * 取得邀请消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameName
	 * @return
	 */
	public String getInviteNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "邀请你玩" + gameName;
	}

	/**
	 * 拒绝邀请消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameName
	 * @return
	 */
	public String getDenyInviteNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "拒绝了你的" + gameName + "邀请";
	}

	/**
	 * 接受邀请消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameName
	 * @return
	 */
	public String getAcceptInviteNoticeTitle(String enemyNickname,
			String gameName) {
		return enemyNickname + "应邀进入" + gameName + "游戏";
	}

	/**
	 * 接受邀请消息系统标题。
	 * 
	 * @param enemyNickname
	 * @param gameName
	 * @return
	 */
	public String getActionNoticeTitle(String enemyNickname, String gameName,
			String action) {
		return gameName + ":" + enemyNickname + action;
	}

	public String getHallTimeoutNoticeTitle(String enemyNickname,
			String gameName, String action) {
		return gameName + ":" + enemyNickname + "超时没" + action;
	}

	public HistoryBean getHistory(int userId, int gameType, int gameId) {
		IWGameService service = ServiceFactory.createWGameService();
		String condition = "user_id = " + userId + " and game_type = "
				+ gameType + " and game_id = " + gameId;
		HistoryBean history = service.getHistory(condition);
		if (history != null) {
			return history;
		} else {
			history = new HistoryBean();
			history.setUserId(userId);
			history.setGameType(gameType);
			history.setGameId(gameId);
			service.addHistory(history);
			return history;
		}
	}

	public UserBean getUser(int userId) {
		IUserService userService = ServiceFactory.createUserService();
//		fanys2006-08-11
		UserStatusBean status =UserInfoUtil.getUserStatus(loginUser.getId());
//		UserStatusBean status = userService
//				.getUserStatus("user_id = " + userId);
//		UserBean user = userService.getUser("id = " + userId);
		//zhul 2006-10-12_优化用户信息查询
		UserBean user = UserInfoUtil.getUser(userId);
		// if (status != null) {
		if(user!=null)	user.setUs(status);
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

	/**
	 * 更改玩家游戏后,更改session中乐币数和更新经验值.
	 */
	public void updateInfo(int userId, int point) {
		// 添加用户更改过状态信息
		// UserInfoUtil.addUserInfo(userId);
		// 得到用户列表
		user = getUser(userId);
		// mcq_1_增加用户更改过经验值
		// user=UserInfoUtil.getUserInfo(user);
		// mcq_1_增加用户经验值 时间:2006-6-11
		// 增加用户经验值
		RankAction.addPoint(user, point);
		// mcq_end
	}

	/**
	 * 超时后,更改session中乐币数.
	 */
	public void updateTimeOut() {
		// mcq_1_增加用户更改过经验值
//		UserInfoUtil.addUserInfo(loginUser.getId());
//		// 判断用户乐币是否更改过
//		loginUser = UserInfoUtil.getUserInfo(loginUser);
		// 增加用户经验值
		RankAction.addPoint(loginUser, Constants.RANK_DRAW);
		// mcq_end
	}

}
