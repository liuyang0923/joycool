package jc.family.game.vs;

import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;

public class Challenge {

	/**
	 * 未处理
	 */
	public static int undisposed = 0;
	/**
	 * 接受
	 */
	public static int agree = 1;
	/**
	 * 拒绝
	 */
	public static int refuse = 2;

	/**
	 * 通知处理时间
	 */
	public static long dealTime = 1000 * 60 * 5;

	int id;
	int fmA;
	int fmB;
	long cTime;
	int gameId;
	int status;// 0:未处理 1:接受 2:拒绝
	long dTime;
	int score;
	int wager;
	int userA;
	int userB;

	public Challenge() {
		;
	}

	public Challenge(int fmA, int fmB, int gameId, int wager, int userA, int status) {
		this.fmA = fmA;
		this.fmB = fmB;
		this.gameId = gameId;
		this.wager = wager;
		this.userA = userA;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFmA() {
		return fmA;
	}

	public void setFmA(int fmA) {
		this.fmA = fmA;
	}

	public String getFmANameWml() {
		FamilyHomeBean fmHome = FamilyAction.getFmByID(fmA);
		if (fmHome == null) {
			return "";
		}
		return fmHome.getFm_nameWml();
	}

	public int getFmB() {
		return fmB;
	}

	public String getFmBNameWml() {
		FamilyHomeBean fmHome = FamilyAction.getFmByID(fmB);
		if (fmHome == null) {
			return "";
		}
		return fmHome.getFm_nameWml();
	}

	public void setFmB(int fmB) {
		this.fmB = fmB;
	}

	public long getCTime() {
		return cTime;
	}

	public void setCTime(long cTime) {
		this.cTime = cTime;
	}

	public int getGameId() {
		return gameId;
	}

	public String getChallGameIdName() {
		return VsGameBean.getGameIdName(gameId);
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getDTime() {
		return dTime;
	}

	public void setDTime(long dTime) {
		this.dTime = dTime;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWager() {
		return wager;
	}

	public void setWager(int wager) {
		this.wager = wager;
	}

	public int getUserA() {
		return userA;
	}

	public void setUserA(int userA) {
		this.userA = userA;
	}

	public int getUserB() {
		return userB;
	}

	public void setUserB(int userB) {
		this.userB = userB;
	}

}
