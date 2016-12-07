package net.joycool.wap.bean;

public class UserFriendBean {
	int id;

	int userId;

	int FriendId;
	
	int mark;
	int levelValue;
	String updateDatetime;
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
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the createDatetime.
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime The createDatetime to set.
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the levelValue.
	 */
	public int getLevelValue() {
		return levelValue;
	}

	/**
	 * @param levelValue The levelValue to set.
	 */
	public void setLevelValue(int levelValue) {
		this.levelValue = levelValue;
	}

	/**
	 * @return Returns the updateDatetime.
	 */
	public String getUpdateDatetime() {
		return updateDatetime;
	}

	/**
	 * @param updateDatetime The updateDatetime to set.
	 */
	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
}
