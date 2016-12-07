package net.joycool.wap.bean;

import java.util.Date;

public class UserNoteBean {

	int id = 0;
	int userId;
	int toUserId;
	String shortNote;
	String note;
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return Returns the shortNote.
	 */
	public String getShortNote() {
		return shortNote;
	}
	/**
	 * @param shortNote The shortNote to set.
	 */
	public void setShortNote(String shortNote) {
		this.shortNote = shortNote;
	}
	/**
	 * @return Returns the toUserId.
	 */
	public int getToUserId() {
		return toUserId;
	}
	/**
	 * @param toUserId The toUserId to set.
	 */
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
