/*
 * mcq_1_total 2006-6-6
 */
package net.joycool.wap.bean;

public class ActionRecordBean {
	int id;

	int fromId;

	int toId;

	int actionId;

	int mark;

	String actionDatetime;

	/**
	 * @return Returns the actionDatetime.
	 */
	public String getActionDatetime() {
		return actionDatetime;
	}

	/**
	 * @param actionDatetime
	 *            The actionDatetime to set.
	 */
	public void setActionDatetime(String actionDatetime) {
		this.actionDatetime = actionDatetime;
	}

	/**
	 * @return Returns the actionId.
	 */
	public int getActionId() {
		return actionId;
	}

	/**
	 * @param actionId
	 *            The actionId to set.
	 */
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return Returns the fromId.
	 */
	public int getFromId() {
		return fromId;
	}

	/**
	 * @param fromId
	 *            The fromId to set.
	 */
	public void setFromId(int fromId) {
		this.fromId = fromId;
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
}
