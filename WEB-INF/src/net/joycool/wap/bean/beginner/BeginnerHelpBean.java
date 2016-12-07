package net.joycool.wap.bean.beginner;

public class BeginnerHelpBean {
	
	int id;

	int userId;

	int sendCount;

	int receiveCount;

	String createDatetime;

	/**
	 * @return 返回 createDatetime。
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime 要设置的 createDatetime。
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id 要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 receiveCount。
	 */
	public int getReceiveCount() {
		return receiveCount;
	}

	/**
	 * @param receiveCount 要设置的 receiveCount。
	 */
	public void setReceiveCount(int receiveCount) {
		this.receiveCount = receiveCount;
	}

	/**
	 * @return 返回 sendCount。
	 */
	public int getSendCount() {
		return sendCount;
	}

	/**
	 * @param sendCount 要设置的 sendCount。
	 */
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId 要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
