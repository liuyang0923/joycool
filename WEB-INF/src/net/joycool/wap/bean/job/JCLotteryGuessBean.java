package net.joycool.wap.bean.job;

public class JCLotteryGuessBean {
	int id;

	int userId;

	int guessId;

	int number;

	long wager;

	String guessDatetime;

	/**
	 * @return Returns the guessDatetime.
	 */
	public String getGuessDatetime() {
		return guessDatetime;
	}

	/**
	 * @param guessDatetime
	 *            The guessDatetime to set.
	 */
	public void setGuessDatetime(String guessDatetime) {
		this.guessDatetime = guessDatetime;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            The number to set.
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the wager.
	 */
	/**
	 * @return Returns the guessId.
	 */
	public int getGuessId() {
		return guessId;
	}

	/**
	 * @param guessId
	 *            The guessId to set.
	 */
	public void setGuessId(int guessId) {
		this.guessId = guessId;
	}

	/**
	 * @return 返回 wager。
	 */
	public long getWager() {
		return wager;
	}

	/**
	 * @param wager
	 *            要设置的 wager。
	 */
	public void setWager(long wager) {
		this.wager = wager;
	}

}
