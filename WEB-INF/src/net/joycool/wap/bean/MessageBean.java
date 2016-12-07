/*
 * Created on 2005-11-21
 *
 */
package net.joycool.wap.bean;

/**
 * @author lbj
 * 
 */
public class MessageBean {
	public static int NEW_MESSAGE = 0;

	public static int OLD_MESSAGE = 1;

	int id;

	int fromUserId;

	int toUserId;

	UserBean fromUser;

	UserBean toUser;

	String content;

	String sendDatetime;

	int mark;

	int flag;

	/**
	 * @return 返回 flag。
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            要设置的 flag。
	 */
	public void setFlag(int flag) {
		this.flag = flag;
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
	 * @return Returns the fromUser.
	 */
	public UserBean getFromUser() {
		return fromUser;
	}

	/**
	 * @param fromUser
	 *            The fromUser to set.
	 */
	public void setFromUser(UserBean fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * @return Returns the fromUserId.
	 */
	public int getFromUserId() {
		return fromUserId;
	}

	/**
	 * @param fromUserId
	 *            The fromUserId to set.
	 */
	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
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
	 * @return Returns the sendDatetime.
	 */
	public String getSendDatetime() {
		if (sendDatetime != null && sendDatetime.lastIndexOf(":") != -1) {
			sendDatetime = sendDatetime.substring(0, sendDatetime
					.lastIndexOf(":"));
		}
		if (sendDatetime != null && sendDatetime.indexOf("-") != -1) {
			sendDatetime = sendDatetime
					.substring(sendDatetime.indexOf("-") + 1);
		}
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
	 * @return Returns the toUser.
	 */
	public UserBean getToUser() {
		return toUser;
	}

	/**
	 * @param toUser
	 *            The toUser to set.
	 */
	public void setToUser(UserBean toUser) {
		this.toUser = toUser;
	}

	/**
	 * @return Returns the toUserId.
	 */
	public int getToUserId() {
		return toUserId;
	}

	/**
	 * @param toUserId
	 *            The toUserId to set.
	 */
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
}
