package net.joycool.wap.bean.top;

public class MoneyTopBean {
	int id;

	int userId;

	int gamePoint;

	long bankStore;

	long bankLoad;

	long moneyTotal;

	String createDatetime;

	/**
	 * @return Returns the createDatetime.
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            The createDatetime to set.
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the bankLoad.
	 */
	public long getBankLoad() {
		return bankLoad;
	}

	/**
	 * @param bankLoad
	 *            The bankLoad to set.
	 */
	public void setBankLoad(long bankLoad) {
		this.bankLoad = bankLoad;
	}

	/**
	 * @return Returns the bankStore.
	 */
	public long getBankStore() {
		return bankStore;
	}

	/**
	 * @param bankStore
	 *            The bankStore to set.
	 */
	public void setBankStore(long bankStore) {
		this.bankStore = bankStore;
	}

	/**
	 * @return Returns the gamePoint.
	 */
	public int getGamePoint() {
		return gamePoint;
	}

	/**
	 * @param gamePoint
	 *            The gamePoint to set.
	 */
	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}

	/**
	 * @return Returns the moneyTotal.
	 */
	public long getMoneyTotal() {
		return moneyTotal;
	}

	/**
	 * @param moneyTotal
	 *            The moneyTotal to set.
	 */
	public void setMoneyTotal(long moneyTotal) {
		this.moneyTotal = moneyTotal;
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
}
