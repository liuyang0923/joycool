package net.joycool.wap.bean.job;

public class JCLotteryNumberBean {
	public static String HOUR = "20";

	int id;

	int number;

	int guessId;

	long leftWager;

	String lotteryDate;

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
	 * @return 返回 leftWager。
	 */
	public long getLeftWager() {
		return leftWager;
	}

	/**
	 * @param leftWager
	 *            要设置的 leftWager。
	 */
	public void setLeftWager(long leftWager) {
		this.leftWager = leftWager;
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
}
