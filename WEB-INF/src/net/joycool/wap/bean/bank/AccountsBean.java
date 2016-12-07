package net.joycool.wap.bean.bank;
/**
 * 
 * @author Administrator
 * 银行每日帐目表记录对象
 */
public class AccountsBean {
	
	// 记录id
	private int id;
	
	// 日期
	private String time;
	
	// 当日存款总额
	private long storeMoney;
	
	// 当日取款总额
	private long withdrawMoney;
	
	// 当日贷款总额
	private long loadMoney;
	
	// 当日还款总额
	private long returnMoney;

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
	 * @return Returns the loadMoney.
	 */
	public long getLoadMoney() {
		return loadMoney;
	}

	/**
	 * @param loadMoney The loadMoney to set.
	 */
	public void setLoadMoney(long loadMoney) {
		this.loadMoney = loadMoney;
	}

	/**
	 * @return Returns the returnMoney.
	 */
	public long getReturnMoney() {
		return returnMoney;
	}

	/**
	 * @param returnMoney The returnMoney to set.
	 */
	public void setReturnMoney(long returnMoney) {
		this.returnMoney = returnMoney;
	}

	/**
	 * @return Returns the storeMoney.
	 */
	public long getStoreMoney() {
		return storeMoney;
	}

	/**
	 * @param storeMoney The storeMoney to set.
	 */
	public void setStoreMoney(long storeMoney) {
		this.storeMoney = storeMoney;
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
	 * @return Returns the withdraw_Money.
	 */
	public long getWithdrawMoney() {
		return withdrawMoney;
	}

	/**
	 * @param withdraw_Money The withdraw_Money to set.
	 */
	public void setWithdrawMoney(long withdrawMoney) {
		this.withdrawMoney = withdrawMoney;
	}
}
