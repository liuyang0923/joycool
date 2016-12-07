package net.joycool.wap.bean.charitarian;

public class CharitarianBean {
	int id;

	int userId;

	long money;
	
	int count;
	
	String createDatetime;

	/**
	 * @return 返回 count。
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count 要设置的 count。
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return 返回 createDatetime。
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime 要设置的 createDatetime。
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
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
	 * @return 返回 money。
	 */
	public long getMoney() {
		return money;
	}

	/**
	 * @param money 要设置的 money。
	 */
	public void setMoney(long money) {
		this.money = money;
	}
}
