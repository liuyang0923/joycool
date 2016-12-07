/*
 *作者:mcq
 *日期:2006-6-26
 *功能:聊天室基础bean 
 */
package net.joycool.wap.bean.chat;

public class RoomManagerBean {
	int id;

	int roomId;

	int userId;
	
	int mark;

	String grantDatetime;

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
}
