/*
 *作者:mcq
 *日期:2006-6-26
 *功能:聊天室基础bean 
 */
package net.joycool.wap.bean.chat;

public class RoomGrantBean {
	int id;

	int roomId;

	int userId;

	int ManagerId;

	String grantDatetime;

	int grantType;

	/**
	 * @return Returns the grantDatetime.
	 */
	public String getGrantDatetime() {
		return grantDatetime;
	}

	/**
	 * @param grantDatetime
	 *            The grantDatetime to set.
	 */
	public void setGrantDatetime(String grantDatetime) {
		this.grantDatetime = grantDatetime;
	}

	/**
	 * @return Returns the grantType.
	 */
	public int getGrantType() {
		return grantType;
	}

	/**
	 * @param grantType
	 *            The grantType to set.
	 */
	public void setGrantType(int grantType) {
		this.grantType = grantType;
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
	 * @return Returns the managerId.
	 */
	public int getManagerId() {
		return ManagerId;
	}

	/**
	 * @param managerId
	 *            The managerId to set.
	 */
	public void setManagerId(int managerId) {
		ManagerId = managerId;
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
