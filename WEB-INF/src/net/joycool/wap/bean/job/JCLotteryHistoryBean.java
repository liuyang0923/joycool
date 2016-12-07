package net.joycool.wap.bean.job;

public class JCLotteryHistoryBean {
	int id;

	int guessId;

	int guessNumber;

	int userId;

	long wager;

	String lotteryDate;

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
	 * @return Returns the guessNumber.
	 */
	public int getGuessNumber() {
		return guessNumber;
	}

	/**
	 * @param guessNumber
	 *            The guessNumber to set.
	 */
	public void setGuessNumber(int guessNumber) {
		this.guessNumber = guessNumber;
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
	 * @return Returns the lotteryDate.
	 */
	public String getLotteryDate() {
		return lotteryDate;
	}

	/**
	 * @param lotteryDate
	 *            The lotteryDate to set.
	 */
	public void setLotteryDate(String lotteryDate) {
		this.lotteryDate = lotteryDate;
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
