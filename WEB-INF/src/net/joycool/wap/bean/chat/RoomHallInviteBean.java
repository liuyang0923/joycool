/**
 * 作者:mcq
 * 日期:2006-7-12
 */
package net.joycool.wap.bean.chat;

public class RoomHallInviteBean {
	int id;

	int userId;

	int toId;

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
	 * @return Returns the toId.
	 */
	public int getToId() {
		return toId;
	}

	/**
	 * @param toId
	 *            The toId to set.
	 */
	public void setToId(int toId) {
		this.toId = toId;
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
