package net.joycool.wap.bean.friend;

public class FriendBadUserBean {
	int id;

	int userId;

	int friendId;

	String createDatetime;

	/**
	 * @return 返回 friendId。
	 */
	public int getFriendId() {
		return friendId;
	}

	/**
	 * @param friendId
	 *            要设置的 friendId。
	 */
	public void setFriendId(int friendId) {
		this.friendId = friendId;
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
}
