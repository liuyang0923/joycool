/*
 * 2006-10-8
 * WUCX
 * 乐酷现金流追踪
 */
package net.joycool.wap.bean.jcadmin;


public class UserCashBean {
	int id;

	int userId;

	int type;

	String reason;

	String createDatetime;

	/**
	 * @return Returns the flowdate.
	 */

	/**
	 * @return Returns the reason.
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            The reason to set.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return Returns the userid.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userid
	 *            The userid to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
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
	 * @return Returns the createDatetime.
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime The createDatetime to set.
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}



}