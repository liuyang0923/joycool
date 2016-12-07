package net.joycool.wap.bean.stock2;

import java.sql.Timestamp;

/**
 * @author macq
 * @explain： 股票成交记录
 * @datetime:2007-4-25 14:38:33
 */
public class StockCJBean {
	
	int id;			// 股票持仓记录id

	int userId;		// 用户id

	int stockId;	// 股票id

	int wtId;		// 委托id

	float price;	// 成交价格

	long cjCount;	// 成交数量
	
	long count;		// 成交前数量
	
	long charge;	// 手续费

	int mark;		// 类型 0为卖 1为买

	// 建立时间
	Timestamp createdatetime;

	public Timestamp getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(Timestamp createdatetime) {
		this.createdatetime = createdatetime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getWtId() {
		return wtId;
	}

	public void setWtId(int wtId) {
		this.wtId = wtId;
	}

	public long getCjCount() {
		return cjCount;
	}

	public void setCjCount(long cjCount) {
		this.cjCount = cjCount;
	}

	/**
	 * @return Returns the charge.
	 */
	public long getCharge() {
		return charge;
	}

	/**
	 * @param charge The charge to set.
	 */
	public void setCharge(long charge) {
		this.charge = charge;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
