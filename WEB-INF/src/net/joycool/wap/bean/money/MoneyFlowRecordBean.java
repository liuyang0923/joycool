package net.joycool.wap.bean.money;

public class MoneyFlowRecordBean {
	int id;

	int typeId;

	// liuyi 2007-01-03 现金流统计修改 start
	long money;

	// liuyi 2007-01-03 现金流统计修改 end

	int mark;

	int userId;

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
	 * @return Returns the money.
	 */
	public long getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            The money to set.
	 */
	public void setMoney(long money) {
		this.money = money;
	}

	/**
	 * @return Returns the typeId.
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
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
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

}
