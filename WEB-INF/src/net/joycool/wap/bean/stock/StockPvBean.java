package net.joycool.wap.bean.stock;

public class StockPvBean {
	int id;

	int stockId;

	int pv;

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
	 * @return 返回 pv。
	 */
	public int getPv() {
		return pv;
	}

	/**
	 * @param pv
	 *            要设置的 pv。
	 */
	public void setPv(int pv) {
		this.pv = pv;
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
}
