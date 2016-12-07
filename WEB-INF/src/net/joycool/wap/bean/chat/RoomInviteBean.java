package net.joycool.wap.bean.chat;

/**
 * fanys 2006-08-15
 * 
 * @author Administrator
 * 
 */
public class RoomInviteBean {
	int id;

	int userId;

	String mobile;

	String name;

	String content;

	String sendDatetime;// 发送时间

	int mark;// 标识被邀请用户是否上来过

	// fanys 2006-08-15
	int sendMark;// 这条消息是否已经发送标志

	int successMark;// 是否发送成功标志

	int inviteeId;// 被邀请者的ID

	String loginDateTime;// 被邀请者登陆时间

	int newUserMark;// 标识被邀请者是否为新用户，如果手机号没有在注册用户中出现过，则认为为新用户

	// fanys 2006-08-15

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

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the sendDatetime.
	 */
	public String getSendDatetime() {
		return sendDatetime;
	}

	/**
	 * @param sendDatetime
	 *            The sendDatetime to set.
	 */
	public void setSendDatetime(String sendDatetime) {
		this.sendDatetime = sendDatetime;
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
	 * @return Returns the inviteeId.
	 */
	public int getInviteeId() {
		return inviteeId;
	}

	/**
	 * @param inviteeId
	 *            The inviteeId to set.
	 */
	public void setInviteeId(int inviteeId) {
		this.inviteeId = inviteeId;
	}

	/**
	 * @return Returns the loginDateTime.
	 */
	public String getLoginDateTime() {
		return loginDateTime;
	}

	/**
	 * @param loginDateTime
	 *            The loginDateTime to set.
	 */
	public void setLoginDateTime(String loginDateTime) {
		this.loginDateTime = loginDateTime;
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
}
