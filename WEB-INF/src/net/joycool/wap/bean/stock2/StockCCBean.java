package net.joycool.wap.bean.stock2;

/**
 * @author macq
 * @explain：股票用户持仓表记录
 * @datetime:2007-4-25 14:38:13
 */
public class StockCCBean {
	// 股票持仓记录id
	int id;

	// 用户id
	int userId;

	// 股票id
	int stockId;

	// 持有数量
	long count;

	// 冻结数量
	long countF;
	
	long cost;	// 成本

	// 建立时间
	String createdatetime;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		//判断股市持有股票数量是否小于零。满足条件者持有股票数量等于0
		if (count < 0) {
			count = 0;
		}
		this.count = count;
	}

	public long getCountF() {
		return countF;
	}

	public void setCountF(long countF) {
		//判断股市冻结股票数量是否小于零。满足条件者冻结股票数量等于0
		if (countF < 0) {
			countF = 0;
		}
		this.countF = countF;
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

	/**
	 * @return Returns the cost.
	 */
	public long getCost() {
		return cost;
	}

	/**
	 * @param cost The cost to set.
	 */
	public void setCost(long cost) {
		this.cost = cost;
	}
	
	/**
	 * @return 成本价
	 */
	public float getAveCost() {
		return (float)cost / (count + countF);
	}
}
