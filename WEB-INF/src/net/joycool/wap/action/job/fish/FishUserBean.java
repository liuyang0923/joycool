package net.joycool.wap.action.job.fish;

import net.joycool.wap.util.SimpleGameLog;


/**
 * @author bomb
 *
 */
public class FishUserBean {
	public static int STATUS_PLAY = 0;
	public static int STATUS_END = 1;
	public static int STATUS_LOSE = 2;
	
	int id;
	int userId;

	AreaBean area = null;		// 所属区域
	FishBean fish = null;		// 当前上钩的鱼

	int highScore;
	int recentHighScore;	// 最近最高分

	// 动作时间
	long actionTime;

	// 离线标志位offline
	boolean offline;

	// 游戏结束标志位
	int gameStatus;
	
	String gameResultTip;	// 结束游戏后的提示信息
	
	SimpleGameLog log = new SimpleGameLog(20);

	public FishUserBean() {
		offline = false;
		reset();
	}
	
	public void reset() {
		gameStatus = STATUS_PLAY;
		
		gameResultTip = "";
	}

	/**
	 * @return 返回 offline。
	 */
	public boolean isOffline() {
		return offline;
	}

	/**
	 * @param offline
	 *            要设置的 offline。
	 */
	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	/**
	 * @return 返回 actionTime。
	 */
	public long getActionTime() {
		return actionTime;
	}

	/**
	 * @param actionTime
	 *            要设置的 actionTime。
	 */
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

	/**
	 * @return 返回 highScore。
	 */
	public int getHighScore() {
		return highScore;
	}

	/**
	 * @param highScore
	 *            要设置的 highScore。
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
	 * @param id
	 *            要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
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
		if (System.currentTimeMillis() - actionTime > FishAction.USER_INACTIVE) {
			return true;
		}
		return false;
	}

	/**
	 * @return Returns the gameStatus.
	 */
	public int getGameStatus() {
		return gameStatus;
	}

	/**
	 * @param gameStatus The gameStatus to set.
	 */
	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}

	public boolean isGameOver() {
		return gameStatus != STATUS_PLAY;
	}
	
	/**
	 * @return Returns the gameResultTip.
	 */
	public String getGameResultTip() {
		return gameResultTip;
	}

	/**
	 * @param gameResultTip The gameResultTip to set.
	 */
	public void setGameResultTip(String gameResultTip) {
		this.gameResultTip = gameResultTip;
	}

	/**
	 * @return Returns the recentHighScore.
	 */
	public int getRecentHighScore() {
		return recentHighScore;
	}

	/**
	 * @param recentHighScore The recentHighScore to set.
	 */
	public void setRecentHighScore(int recentHighScore) {
		this.recentHighScore = recentHighScore;
	}

	/**
	 * @return Returns the area.
	 */
	public AreaBean getArea() {
		return area;
	}

	/**
	 * @param area The area to set.
	 */
	public void setArea(AreaBean area) {
		this.area = area;
	}

	/**
	 * @return Returns the fish.
	 */
	public FishBean getFish() {
		return fish;
	}

	/**
	 * @param fish The fish to set.
	 */
	public void setFish(FishBean fish) {
		this.fish = fish;
	}
	
	public String getLogString() {
		return log.getLogString(5);
	}

	public void addLog(String content) {
		log.add(content);
	}
}
