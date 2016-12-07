package net.joycool.wap.bean.stock2;

/**
 * @author macq
 * @explain：股票委托记录
 * @datetime:2007-4-25 14:38:22
 */
public class StockWTBean {
	// 股票持仓记录id
	int id;

	// 用户id
	int userId;

	// 股票id
	int stockId;

	// 委托价格
	float price;

	// 委托数量
	long count;

	// 成交记录
	long cjCount;

	// 类型 0为卖 1为买
	int mark;

	// 建立时间
	String createdatetime;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(String createdatetime) {
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

	public long getCjCount() {
		return cjCount;
	}

	public void setCjCount(long cjCount) {
		this.cjCount = cjCount;
	}

}
