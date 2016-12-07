package net.joycool.wap.bean.friend;

public class FriendVoteBean {
	int id;
	
	int userId;
	
	long Count;
	
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
	 * @return 返回 count。
	 */
	public long getCount() {
		return Count;
	}

	/**
	 * @param count 要设置的 count。
	 */
	public void setCount(long count) {
		Count = count;
	}
}
