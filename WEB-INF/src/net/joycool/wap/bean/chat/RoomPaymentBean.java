/*
 *作者:mcq
 *日期:2006-6-26
 *功能:聊天室基础bean 
 */
package net.joycool.wap.bean.chat;

public class RoomPaymentBean {
	int id;

	int userId;

	int roomId;

	int payType;

	int money;

	String payDatetime;

	String remark;

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
	 * @return Returns the money.
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            The money to set.
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return Returns the payDatetime.
	 */
	public String getPayDatetime() {
		return payDatetime;
	}

	/**
	 * @param payDatetime
	 *            The payDatetime to set.
	 */
	public void setPayDatetime(String payDatetime) {
		this.payDatetime = payDatetime;
	}

	/**
	 * @return Returns the payType.
	 */
	public int getPayType() {
		return payType;
	}

	/**
	 * @param payType
	 *            The payType to set.
	 */
	public void setPayType(int payType) {
		this.payType = payType;
	}

	/**
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
