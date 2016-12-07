package net.joycool.wap.bean.chat;

public class RoomVoteBean {
	int id;

	int userId;
	
	int applyId;
	
	String voteDatetime;

	/**
	 * 
	 * @return Returns the applyId.
	 */
	public int getApplyId() {
		return applyId;
	}

	/**
	 * @param applyId The applyId to set.
	 */
	public void setApplyId(int applyId) {
		this.applyId = applyId;
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

	/**
	 * @return Returns the voteDatetime.
	 */
	public String getVoteDatetime() {
		return voteDatetime;
	}

	/**
	 * @param voteDatetime The voteDatetime to set.
	 */
	public void setVoteDatetime(String voteDatetime) {
		this.voteDatetime = voteDatetime;
	}
}

