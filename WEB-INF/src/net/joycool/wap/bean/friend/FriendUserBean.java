package net.joycool.wap.bean.friend;

public class FriendUserBean {
	int id;

	int userId;

	int FriendId;
	
	int mark;
	
	int drinkId;

	String createDatetime;

	/**
	 * @return 返回 friendId。
	 */
	public int getFriendId() {
		return FriendId;
	}

	/**
	 * @param friendId
	 *            要设置的 friendId。
	 */
	public void setFriendId(int friendId) {
		FriendId = friendId;
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
	 * @return 返回 mark。
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark 要设置的 mark。
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getDrinkId() {
		return drinkId;
	}

	public void setDrinkId(int drinkId) {
		this.drinkId = drinkId;
	}
}
