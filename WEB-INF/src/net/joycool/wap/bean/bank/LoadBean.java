package net.joycool.wap.bean.bank;

/**
 * 
 * @author Administrator
 * 贷款记录对象
 */
public class LoadBean {
	
	// 记录id
	private int id;
	
	// 用户id
	private int userId;
	
	// 贷款时间
	private String createTime;
	
	// 到期时间
	private String ExpireTime;
	
	// 用户贷款额(含贷款利息)
	private long money;
	
	// 贷款时间
	private int delayTime;

	// 数据库当前时间
	private String currentTime; 
	
	// 还款剩余时间
	private int leaveTime;
	
	/**
	 * @return Returns the createTime.
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime The createTime to set.
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return Returns the expireTime.
	 */
	public String getExpireTime() {
		return ExpireTime;
	}

	/**
	 * @param expireTime The expireTime to set.
	 */
	public void setExpireTime(String expireTime) {
		ExpireTime = expireTime;
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
	 * @return Returns the money.
	 */
	public long getMoney() {
		return money;
	}

	/**
	 * @param money The money to set.
	 */
	public void setMoney(long money) {
		this.money = money;
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
	 * @return Returns the delayTime.
	 */
	public int getDelayTime() {
		return delayTime;
	}

	/**
	 * @param delayTime The delayTime to set.
	 */
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * @return Returns the leaveTime.
	 */
	public int getLeaveTime() {
		return leaveTime;
	}

	/**
	 * @param leaveTime The leaveTime to set.
	 */
	public void setLeaveTime(int leaveTime) {
		this.leaveTime = leaveTime;
	}

	/**
	 * fanys 2006-08-03 格式为****-**-** **:** 如2006-08-02 16:00
	 * 
	 * @return
	 */
	public String getCreateTime2() {
		String temp = createTime;
		if (temp != null) {
			if (temp.lastIndexOf(":") != -1) {
				temp = temp.substring(0, temp.lastIndexOf(":"));
			}
		}
		return temp;

	}

	/**
	 * @return Returns the currentTime.
	 */
	public String getCurrentTime() {
		return currentTime;
	}

	/**
	 * @param currentTime The currentTime to set.
	 */
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

}
