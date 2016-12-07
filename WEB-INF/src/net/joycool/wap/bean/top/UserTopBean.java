package net.joycool.wap.bean.top;

import net.joycool.wap.bean.CrownBean;

public class UserTopBean {
	int id;

	int priority;

	int userId;

	int mark;

	int imageId;

	String createDatetime;

	private CrownBean crown;

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
	 * @return Returns the priority.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            The priority to set.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
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
	 * @return Returns the imageId.
	 */
	public int getImageId() {
		return imageId;
	}

	/**
	 * @param imageId
	 *            The imageId to set.
	 */
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return Returns the crown.
	 */
	public CrownBean getCrown() {
		return crown;
	}

	/**
	 * @param crown
	 *            The crown to set.
	 */
	public void setCrown(CrownBean crown) {
		this.crown = crown;
	}
}
