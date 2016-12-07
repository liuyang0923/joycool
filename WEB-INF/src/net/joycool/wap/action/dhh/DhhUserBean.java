package net.joycool.wap.action.dhh;

import java.util.HashMap;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-26 15:38:13
 */
/**
 * @author s
 *
 */
public class DhhUserBean {
	public static int STATUS_PLAY = 0;
	public static int STATUS_END = 1;
	public static int STATUS_LOSE = 2;
	
	public static int TOTAL_DAY = 100;
	public static int INIT_SHIP = 1;
	public static int INIT_CITY = 2;
	public static int INIT_MONEY = 2000;
	public static int INIT_DAY = 1;
	/**
	 * 
	 */
	int id;

	/**
	 * 用户id
	 */
	int userId;
	
	/**
	 * 用户船的bean
	 */
	int ship;
	
	/**
	 * 船的容量
	 */
	int volume;
	
	/**
	 * 用户所在城市的bean
	 */
	int city;

	/**
	 * 当前的钱
	 */
	int money;

	/**
	 *游戏时间 
	 */
	int date;
	
	/**
	 * 高分记录
	 */
	int highScore;
	
	/**
	 * 最近高分记录
	 */
	int recentHighScore;

	/**
	 * 游戏逝去的时间
	 */
	int pasttime;
	
	boolean rewarded;	// 是否已经拿过奖励
	
	/**
	 * // 当前用户身上物品列表明细
	 */
	HashMap productMap = null;

	// 动作时间
	long actionTime;
	
	boolean change;// 判断是否更换场景

	// 存款
	int saving;

//	// 动作时间
//	long actionTime;

	// 离线标志位offline
	boolean offline;

	// 游戏结束标志位
	int gameStatus;
	
	String gameResultTip;	// 结束游戏后的提示信息

	public DhhUserBean() {
		reset();
	}
	
	public void reset() {
		//开始时间
		date = TOTAL_DAY;
		//初始的现金
		money = INIT_MONEY;
		//初始的船
		ship = INIT_SHIP;
		
		volume = 0;
		
		//初始的城市
		city = INIT_CITY;
		//初始的存款
		saving = 0;
		//初始的状态
		gameStatus = STATUS_PLAY;
		//清空用户物品表
		productMap = new HashMap();
		//开始的时间
		pasttime = INIT_DAY;
		rewarded = false;
		
		gameResultTip = "";
	}

	/**
	 * @return 返回 change。
	 */
	public boolean isChange() {
		return change;
	}

	/**
	 * @param change 要设置的 change。
	 */
	public void setChange(boolean change) {
		this.change = change;
	}

	/**
	 * @return 返回 city。
	 */
	public int getCity() {
		return city;
	}

	/**
	 * @param city 要设置的 city。
	 */
	public void setCity(int city) {
		this.city = city;
	}

	/**
	 * @return 返回 date。
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date 要设置的 date。
	 */
	public void setDate(int date) {
		this.date = date;
	}

	/**
	 * @return 返回 gameResultTip。
	 */
	public String getGameResultTip() {
		return gameResultTip;
	}

	/**
	 * @param gameResultTip 要设置的 gameResultTip。
	 */
	public void setGameResultTip(String gameResultTip) {
		this.gameResultTip = gameResultTip;
	}

	/**
	 * @return 返回 gameStatus。
	 */
	public int getGameStatus() {
		return gameStatus;
	}

	/**
	 * @param gameStatus 要设置的 gameStatus。
	 */
	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}

	/**
	 * @return 返回 highScore。
	 */
	public int getHighScore() {
		return highScore;
	}

	/**
	 * @param highScore 要设置的 highScore。
	 */
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id 要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 money。
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money 要设置的 money。
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return 返回 offline。
	 */
	public boolean isOffline() {
		return offline;
	}

	/**
	 * @param offline 要设置的 offline。
	 */
	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	/**
	 * @return 返回 productMap。
	 */
	public HashMap getProductMap() {
		return productMap;
	}

	/**
	 * @param productMap 要设置的 productMap。
	 */
	public void setProductMap(HashMap productMap) {
		this.productMap = productMap;
	}

	/**
	 * @return 返回 recentHighScore。
	 */
	public int getRecentHighScore() {
		return recentHighScore;
	}

	/**
	 * @param recentHighScore 要设置的 recentHighScore。
	 */
	public void setRecentHighScore(int recentHighScore) {
		this.recentHighScore = recentHighScore;
	}

	/**
	 * @return 返回 saving。
	 */
	public int getSaving() {
		return saving;
	}

	/**
	 * @param saving 要设置的 saving。
	 */
	public void setSaving(int saving) {
		this.saving = saving;
	}

	/**
	 * @return 返回 ship。
	 */
	public int getShip() {
		return ship;
	}

	/**
	 * @param ship 要设置的 ship。
	 */
	public void setShip(int ship) {
		this.ship = ship;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId 要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 pasttime。
	 */
	public int getPasttime() {
		return pasttime;
	}

	/**
	 * @param pasttime 要设置的 pasttime。
	 */
	public void setPasttime(int pasttime) {
		this.pasttime = pasttime;
	}

	public boolean isGameOver() {
		return gameStatus != STATUS_PLAY;
	}
	/**
	 * 
	 * @author macq
	 * @explain： 判断用户是否下线
	 * @datetime:2007-3-17 11:22:33
	 * @return
	 * @return boolean
	 */
	public boolean userOffline() {
		// 需要乐币复活
		if (System.currentTimeMillis() - actionTime > DhhAction.PK_USER_INACTIVE) {
			return true;
		}
		return false;
	}

	/**
	 * @return 返回 actionTime。
	 */
	public long getActionTime() {
		return actionTime;
	}

	/**
	 * @param actionTime 要设置的 actionTime。
	 */
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

	/**
	 * @return 返回 volume。
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * @param volume 要设置的 volume。
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * @return Returns the rewarded.
	 */
	public boolean isRewarded() {
		return rewarded;
	}

	/**
	 * @param rewarded The rewarded to set.
	 */
	public void setRewarded(boolean rewarded) {
		this.rewarded = rewarded;
	}
}
