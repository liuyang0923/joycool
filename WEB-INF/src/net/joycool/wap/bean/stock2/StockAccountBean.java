package net.joycool.wap.bean.stock2;

import java.sql.Timestamp;

/**
 * @author macq
 * @explain：股票账户
 * @datetime:2007-4-25 14:38:00
 */
public class StockAccountBean {
	// 账户id
	int id;

	// 用户id
	int userId;

	// 账户内金额
	long money;

	// 冻结金额
	long moneyF;
	
	long asset;	// 总资产
	long stockPrice;	// 总市值

	Timestamp createDatetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public long getMoneyF() {
		return moneyF;
	}

	public void setMoneyF(long moneyF) {
		this.moneyF = moneyF;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the createDatetime.
	 */
	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime The createDatetime to set.
	 */
	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the asset.
	 */
	public long getAsset() {
		return asset;
	}

	/**
	 * @param asset The asset to set.
	 */
	public void setAsset(long asset) {
		this.asset = asset;
	}

	/**
	 * @return Returns the stockPrice.
	 */
	public long getStockPrice() {
		return stockPrice;
	}

	/**
	 * @param stockPrice The stockPrice to set.
	 */
	public void setStockPrice(long stockPrice) {
		this.stockPrice = stockPrice;
	}

	public void calcAsset() {
		asset = money + moneyF + stockPrice;
	}
}
