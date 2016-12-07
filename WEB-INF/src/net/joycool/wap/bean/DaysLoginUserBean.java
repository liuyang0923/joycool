/*
 * Created on 2006-06-30
 *
 */
package net.joycool.wap.bean;


/**
 * @author zhangyi
 * 
 */

public class DaysLoginUserBean {
	int id;

	int userId;
	
	String date;
	
	String create_datetime;

	/**
	 * @return Returns the create_datetime.
	 */
	public String getCreate_datetime() {
		return create_datetime;
	}

	/**
	 * @param create_datetime The create_datetime to set.
	 */
	public void setCreate_datetime(String create_datetime) {
		this.create_datetime = create_datetime;
	}

	/**
	 * @return Returns the date.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(String date) {
		this.date = date;
	}

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
