package net.joycool.wap.action.fs;


/**
 * @author bomb
 */
public class FSTopBean {
	
	int id;
	int userId;
	int type = 0;		// 游戏模式

	int highScore = 0;
	int recentHighScore = 0;	// 最近最高分
	/**
	 * @return Returns the highScore.
	 */
	public int getHighScore() {
		return highScore;
	}
	/**
	 * @param highScore The highScore to set.
	 */
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
