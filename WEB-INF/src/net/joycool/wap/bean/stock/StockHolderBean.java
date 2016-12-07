package net.joycool.wap.bean.stock;

public class StockHolderBean {
	int id;

	int userId;

	// liuyi 2006-12-28 股票bug修改 start
	long totalCount;

	// liuyi 2006-12-28 股票bug修改 end

	int stockId;

	float averagePrice;

	String createDatetime;

	/**
	 * @return 返回 averagePrice。
	 */
	public float getAveragePrice() {
		return averagePrice;
	}

	/**
	 * @param averagePrice
	 *            要设置的 averagePrice。
	 */
	public void setAveragePrice(float averagePrice) {
		this.averagePrice = averagePrice;
	}

	/**
	 * @return 返回 totalCount。
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            要设置的 totalCount。
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return 返回 createDatetime。
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            要设置的 createDatetime。
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
	 * @param id
	 *            要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 stockId。
	 */
	public int getStockId() {
		return stockId;
	}

	/**
	 * @param stockId
	 *            要设置的 stockId。
	 */
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
