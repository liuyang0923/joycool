package net.joycool.wap.bean.job;

public class HappyCardSendBean {
	int id;

	int userId;// 发送者ID

	String mobile;// 手机号

	String sender;// 发送者

	String receiver;// 接收者

	int cardId;// 卡号

	String sendDateTime;// 发送时间

	int mark;// 贺卡接收者是否上来过

	int sendMark;// 发送标志位

	int successMark;// 发送是否成功标志位

	int receiverId;// 接收者ID

	int inOrOutMark;// 接收者站内或站外

	int newUserMark;//是否为新用户

	String viewDateTime;// 接收者查看时间

	/**
	 * @return Returns the cardId.
	 */
	public int getCardId() {
		return cardId;
	}

	/**
	 * @param cardId
	 *            The cardId to set.
	 */
	public void setCardId(int cardId) {
		this.cardId = cardId;
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
	 * @return Returns the inOrOutMark.
	 */
	public int getInOrOutMark() {
		return inOrOutMark;
	}

	/**
	 * @param inOrOutMark
	 *            The inOrOutMark to set.
	 */
	public void setInOrOutMark(int inOrOutMark) {
		this.inOrOutMark = inOrOutMark;
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
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            The mobile to set.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return Returns the receiver.
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver
	 *            The receiver to set.
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return Returns the receiverId.
	 */
	public int getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverId
	 *            The receiverId to set.
	 */
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * @return Returns the sendDateTime.
	 */
	public String getSendDateTime() {
		return sendDateTime;
	}

	/**
	 * @param sendDateTime
	 *            The sendDateTime to set.
	 */
	public void setSendDateTime(String sendDateTime) {
		this.sendDateTime = sendDateTime;
	}

	/**
	 * @return Returns the sender.
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            The sender to set.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return Returns the sendMark.
	 */
	public int getSendMark() {
		return sendMark;
	}

	/**
	 * @param sendMark
	 *            The sendMark to set.
	 */
	public void setSendMark(int sendMark) {
		this.sendMark = sendMark;
	}

	/**
	 * @return Returns the successMark.
	 */
	public int getSuccessMark() {
		return successMark;
	}

	/**
	 * @param successMark
	 *            The successMark to set.
	 */
	public void setSuccessMark(int successMark) {
		this.successMark = successMark;
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
	 * @return Returns the viewDateTime.
	 */
	public String getViewDateTime() {
		return viewDateTime;
	}

	/**
	 * @param viewDateTime
	 *            The viewDateTime to set.
	 */
	public void setViewDateTime(String viewDateTime) {
		this.viewDateTime = viewDateTime;
	}

	/**
	 * @return Returns the newUserMark.
	 */
	public int getNewUserMark() {
		return newUserMark;
	}

	/**
	 * @param newUserMark
	 *            The newUserMark to set.
	 */
	public void setNewUserMark(int newUserMark) {
		this.newUserMark = newUserMark;
	}

}
