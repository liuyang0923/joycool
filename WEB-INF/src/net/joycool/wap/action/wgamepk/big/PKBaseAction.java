package net.joycool.wap.action.wgamepk.big;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgame.big.HistoryBigBean;
import net.joycool.wap.bean.wgame.big.WGamePKBigBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.StaticCacheMap;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.NoticeServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.service.impl.WGamePKServiceImpl;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.UserInfoUtil;

public class PKBaseAction extends CustomAction{

	protected static WGamePKServiceImpl pkService = new WGamePKServiceImpl();

	protected static UserServiceImpl userService = new UserServiceImpl();

	protected static NoticeServiceImpl noticeService = new NoticeServiceImpl();
	
	public static ICacheMap wgamepkCache = CacheManage.addCache(new StaticCacheMap(2), "wgamepkbig");
	
	protected UserBean loginUser;

	protected UserBean leftUser;

	public static double PK_BIG_RATE = 0.995;

	// protected IFriendLevelService friendLevel;

	public PKBaseAction(HttpServletRequest request) {
		super(request);
		loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
	}

	public static UserBean getUser(int userId) {
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		UserBean user = UserInfoUtil.getUser(userId);
		if (user != null) {
			user.setUs(status);
		}
		return user;
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
		RankAction.addPoint(loginUser, point);
	}

	/**
	 * 更改PK挑战者挑战打平后,更改session中经验值.
	 */
	public void updatePKDrawRightInfo() {
		RankAction.addPoint(loginUser, Constants.RANK_DRAW);
	}

	/**
	 * mcq_1_更改庄家状态相关状态 时间:2006-6-11
	 */
	public void updateLeftInfo(int userId, int point) {
		RankAction.addPoint(leftUser, point);
	}

	/**
	 * 减少登录用户乐币数
	 * 
	 * @param gamePoint
	 */
	public void decLoginUserGamePoint(long gamePoint) {
		decUserGamePoint(loginUser, gamePoint,0);
	}

	/**
	 * 减少登录用户乐币数
	 * 
	 * @param user
	 * @param gamePoint
	 */
	public void decUserGamePoint(UserBean user, long gamePoint,int rUserId) {
		BankCacheUtil.updateBankStoreCacheById(-gamePoint, user.getId(),rUserId,Constants.BANK_DAFUHAO_TYPE);
	}

	/**
	 * 增加登录用户的乐币数
	 * 
	 * @param gamePoint
	 */
	public void incLoginUserGamePoint(long gamePoint) {
		incUserGamePoint(loginUser, gamePoint,0);
	}

	/**
	 * 增加用户的乐币数
	 * 
	 * @param user
	 * @param gamePoint
	 */
	public void incUserGamePoint(UserBean user, long gamePoint,int rUserId) {
		BankCacheUtil.updateBankStoreCacheById(gamePoint, user.getId(),rUserId,Constants.BANK_DAFUHAO_TYPE);
	}

	public static final int DOGFALL = 0;// 平局

	public static final int BANKER_WIN = 1;// 庄家赢

	public static final int BANKER_LOSE = 2;// 庄家输

	
	public static int PK_VIP = 5153;
	
	/**
	 * 处理结果
	 * 
	 * @param result
	 * @param pkBean
	 */
	public void dealResult(int result, WGamePKBigBean pkBean, int pkGameType) {
		long challengerLoseWager = 0;
		UserBean banker = getUser(pkBean.getLeftUserId());// 庄家
		UserBean challenger = loginUser;//挑战者
		// fanys2006-08-11
		if (pkBean.getWager() > getUserStore(challenger.getId()))
			challengerLoseWager = getUserStore(challenger.getId());
		else
			challengerLoseWager = pkBean.getWager();
		if (result == DOGFALL) {// 平局
			incUserGamePoint(banker, (long) pkBean.getWager(),challenger.getId());
			// 庄家记录
			recordPKGame(challenger, pkGameType, 0);
			// 挑战者记录
			recordPKGame(banker, pkGameType, 0);
		} else if (result == BANKER_WIN) {// 庄家赢
			// 大富豪色子道具
			int rightUserBagId = UserBagCacheUtil.getUserBagById(24, challenger
					.getId());
			if (rightUserBagId > 1 && RandomUtil.percentRandom(50)) {
				incUserGamePoint(banker, (long) pkBean.getWager(),challenger.getId());
				// 更新用户行囊
				UserBagCacheUtil.UseUserBagCacheById(challenger.getId(),
						rightUserBagId);
				// 使用特殊道具标志位
				pkBean.setFlag(1);
			} else {
				long totalMoney = (long) (challengerLoseWager * PK_BIG_RATE);
				//TODO 处理大富豪vip
				int count = UserBagCacheUtil.getUserBagItemCount(PK_VIP,banker.getId());
				if(count > 0) {
					int useBagId = UserBagCacheUtil.getUserBagById(PK_VIP,banker.getId());
					UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
					if(useBagBean.getTimeLeft() > 0){
						totalMoney = challengerLoseWager;
					}
				}
				
				// 给庄家加钱
				incUserGamePoint(banker, (long) pkBean.getWager() + totalMoney,challenger.getId());
				decUserGamePoint(challenger, (long) challengerLoseWager,banker.getId());
				// 挑战者扣钱记录
				recordPKGame(challenger, pkGameType, -challengerLoseWager);
				// 庄家赢钱记录
				recordPKGame(banker, pkGameType, challengerLoseWager);
				pkBean.setWager(challengerLoseWager);
			}
		} else {// 庄家输
			int leftUserBagId = UserBagCacheUtil.getUserBagById(24, pkBean
					.getLeftUserId());
			if (leftUserBagId > 1 && RandomUtil.percentRandom(50)) {
				// 退还庄家下注
				incUserGamePoint(banker, (long) pkBean.getWager(),challenger.getId());
				// 更新用户行囊
				UserBagCacheUtil.UseUserBagCacheById(banker.getId(),
						leftUserBagId);
				// 使用特殊道具标志位
				pkBean.setFlag(1);
			}else{
				long totalMoney = (long) (pkBean.getWager() * PK_BIG_RATE);
				//TODO 处理大富豪vip
				int count = UserBagCacheUtil.getUserBagItemCount(PK_VIP,challenger.getId());
				if(count > 0) {
					int useBagId = UserBagCacheUtil.getUserBagById(PK_VIP,challenger.getId());
					UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
					if(useBagBean.getTimeLeft() > 0){
						totalMoney = challengerLoseWager;
					}
				}
				
				
				// 给挑战者加钱
				incUserGamePoint(challenger, totalMoney,banker.getId());
				// 庄家扣钱记录
				recordPKGame(banker, pkGameType, -pkBean.getWager());
				// 挑战者赢钱记录
				recordPKGame(challenger, pkGameType, pkBean.getWager());
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取银行存款
	 * @datetime:2007-7-16 17:32:20
	 * @param userId
	 * @return
	 * @return long
	 */
	public long getUserStore(int userId) {
		long storeMoney = 0;
		// macq_2006-11-27_添加存款缓存_strat
		StoreBean storeBean = BankCacheUtil.getBankStoreCache(userId);
		// StoreBean storeBean = bankService.getStore("user_id="
		// + loginUser.getId());
		// macq_2006-11-27_添加存款缓存_end
		if (storeBean != null) {
			// 有存款
			storeMoney = storeBean.getMoney();
		}
		return storeMoney;
	}

	public boolean validate(WGamePKBigBean pkBigBean, HttpServletRequest request) {
		if (pkBigBean == null) {
			request.setAttribute("tip", "该局已被取消");
			request.setAttribute("result", "failure");
			return false;

		}
		if (pkBigBean.getLeftUserId() == loginUser.getId()) {
			request.setAttribute("tip", "您不能挑战自己的庄");
			request.setAttribute("result", "failure");
			return false;

		}
		if (pkBigBean.getMark() == WGamePKBigBean.PK_MARK_END) {
			request.setAttribute("tip", "该局已经结束");
			request.setAttribute("result", "failure");
			return false;

		}
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(loginUser.getId());
		if (storeBean == null || storeBean.getMoney() < pkBigBean.getWager()) {
			request.setAttribute("tip", "您的乐币不够");
			request.setAttribute("result", "failure");
			return false;
		}
		request.setAttribute("result", "success");
		return true;
	}

	public HistoryBigBean getHistory(int userId, int gameType, int gameId) {
		String condition = "user_id = " + userId + " and game_type = "
				+ gameType + " and game_id = " + gameId;
		HistoryBigBean history = pkService.getHistoryBig(condition);
		if (history != null) {
			return history;
		} else {
			history = new HistoryBigBean();
			history.setUserId(userId);
			history.setGameType(gameType);
			history.setGameId(gameId);
			pkService.addHistoryBig(history);
			return history;
		}
	}

	/**
	 * 统计战绩。
	 * 
	 * @param history
	 * @return
	 */
	public void updateHistoryBig(HistoryBigBean history) {
		String condition = "user_id = " + history.getUserId()
				+ " and game_type = " + history.getGameType()
				+ " and game_id = " + history.getGameId();
		int count = pkService.getHistoryBigCount(condition);
		// 已经添加
		if (count > 0) {
			String set = "win_count = win_count + " + history.getWinCount()
					+ ", draw_count = draw_count + " + history.getDrawCount()
					+ ", lose_count = lose_count + " + history.getLoseCount()
					+ ", money = money + " + history.getMoney();
			pkService.updateHistoryBig(set, condition);
		}
		// 尚未添加
		else {
			pkService.addHistoryBig(history);
		}
	}

	/**
	 * 保存PK记录
	 * 
	 * @param user
	 * @param pkGameType
	 * @param wager
	 */
	public void recordPKGame(UserBean user, int pkGameType, long wager) {
		HistoryBigBean history = new HistoryBigBean();
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
		updateHistoryBig(history);
	}

	
	// 通过缓存获取当前坐庄列表
	public static LinkedList getWGamePKBigList(int gameId) {
		Integer key = Integer.valueOf(gameId);
		synchronized(wgamepkCache) {
			LinkedList bkList = (LinkedList)wgamepkCache.get(key);
			if(bkList != null)
				return bkList;

			List list = pkService.getWGamePKBigList("mark=1 and game_id=" + gameId + " order by id desc");
			if(list != null) {
				bkList = new LinkedList(list);
				wgamepkCache.put(key, bkList);
			}

			return bkList;
		}
	}
	// 开始坐庄，加入到列表
	public static boolean addWGamePKBig(WGamePKBigBean bean) {
		if(!pkService.addWGamePKBig(bean))
			return false;
		LinkedList list = getWGamePKBigList(bean.getGameId());
		if(list == null)
			return true;
		synchronized(list) {
			list.addFirst(bean);
		}
		return true;
	}
	// 从坐庄列表删除
	public static void endWGamePKBig(WGamePKBigBean bean) {
		LinkedList list = getWGamePKBigList(bean.getGameId());
		if(list == null)
			return;
		synchronized(list) {
			Iterator iter = list.iterator();
			while(iter.hasNext()) {
				WGamePKBigBean w = (WGamePKBigBean)iter.next();
				if(w.getId() == bean.getId())
					iter.remove();
			}
		}
	}
	// 判断是否可以坐庄（每人限5个）
	public static boolean canBk(int userId, int gameId) {
		LinkedList list = getWGamePKBigList(gameId);
		if(list.size() < WGamePKBigBean.MAX_BK_COUNT)
			return true;
		int sum = 0;
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			WGamePKBigBean w = (WGamePKBigBean)iter.next();
			if(w.getLeftUserId() == userId)
				sum++;
		}
		return sum < WGamePKBigBean.MAX_BK_COUNT;
	}
}
