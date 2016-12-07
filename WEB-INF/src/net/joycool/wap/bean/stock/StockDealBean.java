package net.joycool.wap.bean.stock;

public class StockDealBean {
	int id;

	int userId;

	int mark;

	int stockId;

	int totalCount;

	float price;

	String createDatetime;

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
	 * @return 返回 mark。
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark。
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return 返回 totalCount。
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount 要设置的 totalCount。
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return 返回 price。
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            要设置的 price。
	 */
	public void setPrice(float price) {
		this.price = price;
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
