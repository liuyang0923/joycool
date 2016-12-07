package net.joycool.wap.bean.friend;

public class FriendProposalBean {
	int id;

	int fromId;

	int toId;

	int fingerRingId;

	int mark;

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
	 * @return Returns the fingerRingId.
	 */
	public int getFingerRingId() {
		return fingerRingId;
	}

	/**
	 * @param fingerRingId
	 *            The fingerRingId to set.
	 */
	public void setFingerRingId(int fingerRingId) {
		this.fingerRingId = fingerRingId;
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
