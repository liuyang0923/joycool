package net.joycool.wap.bean.chat;

public class JCRoomOnlineBean {

	int id;

	int roomId;

	int userId;

	String enterDateTime;

	/**
	 * @return Returns the enterDateTime.
	 */
	public String getEnterDateTime() {
		return enterDateTime;
	}

	/**
	 * @param enterDateTime
	 *            The enterDateTime to set.
	 */
	public void setEnterDateTime(String enterDateTime) {
		this.enterDateTime = enterDateTime;
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
	 * @return Returns the roomId.
	 */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId
	 *            The roomId to set.
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
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
