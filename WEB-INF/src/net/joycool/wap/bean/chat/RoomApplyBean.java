package net.joycool.wap.bean.chat;

public class RoomApplyBean {
	int id;

	int userId;

	String roomName;

	String roomSubject;
 
	String roomEnounce;

	int voteCount;

	String applyDatetime;

	int mark;

	/**
	 * @return Returns the applyDatetime.
	 */
	public String getApplyDatetime() {
		return applyDatetime;
	}

	/**
	 * @param applyDatetime
	 *            The applyDatetime to set.
	 */
	public void setApplyDatetime(String applyDatetime) {
		this.applyDatetime = applyDatetime;
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

	/**
	 * @return Returns the roomEnounce.
	 */
	public String getRoomEnounce() {
		return roomEnounce;
	}

	/**
	 * @param roomEnounce
	 *            The roomEnounce to set.
	 */
	public void setRoomEnounce(String roomEnounce) {
		this.roomEnounce = roomEnounce;
	}

	/**
	 * @return Returns the roomName.
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * @param roomName
	 *            The roomName to set.
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/**
	 * @return Returns the roomSubject.
	 */
	public String getRoomSubject() {
		return roomSubject;
	}

	/**
	 * @param roomSubject
	 *            The roomSubject to set.
	 */
	public void setRoomSubject(String roomSubject) {
		this.roomSubject = roomSubject;
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
	 * @return Returns the voteCount.
	 */
	public int getVoteCount() {
		return voteCount;
	}

	/**
	 * @param voteCount
	 *            The voteCount to set.
	 */
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
}

