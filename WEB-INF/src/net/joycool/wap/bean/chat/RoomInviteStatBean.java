package net.joycool.wap.bean.chat;

public class RoomInviteStatBean {
	int id;

	String statDatetime;

	int inviteCount;// 下发总数

	int acceptNewCount;// 接收的新用户数

	int replyCount;// 回复的用户总数

	int replyNewCount;// 回复的新用户数

	int reachLimitCount;// 发送达到上限的人数

	/**
	 * @return Returns the acceptNewCount.
	 */
	public int getAcceptNewCount() {
		return acceptNewCount;
	}

	/**
	 * @param acceptNewCount The acceptNewCount to set.
	 */
	public void setAcceptNewCount(int acceptNewCount) {
		this.acceptNewCount = acceptNewCount;
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
	 * @return Returns the inviteCount.
	 */
	public int getInviteCount() {
		return inviteCount;
	}

	/**
	 * @param inviteCount The inviteCount to set.
	 */
	public void setInviteCount(int inviteCount) {
		this.inviteCount = inviteCount;
	}

	/**
	 * @return Returns the reachLimitCount.
	 */
	public int getReachLimitCount() {
		return reachLimitCount;
	}

	/**
	 * @param reachLimitCount The reachLimitCount to set.
	 */
	public void setReachLimitCount(int reachLimitCount) {
		this.reachLimitCount = reachLimitCount;
	}

	/**
	 * @return Returns the replyCount.
	 */
	public int getReplyCount() {
		return replyCount;
	}

	/**
	 * @param replyCount The replyCount to set.
	 */
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	/**
	 * @return Returns the replyNewCount.
	 */
	public int getReplyNewCount() {
		return replyNewCount;
	}

	/**
	 * @param replyNewCount The replyNewCount to set.
	 */
	public void setReplyNewCount(int replyNewCount) {
		this.replyNewCount = replyNewCount;
	}

	/**
	 * @return Returns the statDatetime.
	 */
	public String getStatDatetime() {
		return statDatetime;
	}

	/**
	 * @param statDatetime The statDatetime to set.
	 */
	public void setStatDatetime(String statDatetime) {
		this.statDatetime = statDatetime;
	}

}
