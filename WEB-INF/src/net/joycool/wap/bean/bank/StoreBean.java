package net.joycool.wap.bean.bank;
/**
 * 
 * @author Administrator
 * 存款记录对象
 */
public class StoreBean {
	
	// 记录id
	private int id;
	
	// 用户id
	private int userId;
	
	// 最后存款时间
	private String time;
	
	// 用户存款额(含存款利息)
	private long money;

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
	 * @return Returns the time.
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time The time to set.
	 */
	public void setTime(String time) {
		this.time = time;
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
