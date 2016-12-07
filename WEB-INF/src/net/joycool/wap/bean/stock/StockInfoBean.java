package net.joycool.wap.bean.stock;

public class StockInfoBean {
	int id;

	int stockId;

	float todayPrice;

	float yesterdayPrice;

	float rateValue;

	float rate;

	float highPrice;

	float lowPrice;

	int baseLine;

	String createDatetime;

	/**
	 * @return 返回 baseLine。
	 */
	public int getBaseLine() {
		return baseLine;
	}

	/**
	 * @param baseLine
	 *            要设置的 baseLine。
	 */
	public void setBaseLine(int baseLine) {
		this.baseLine = baseLine;
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
	 * @return 返回 highPrice。
	 */
	public float getHighPrice() {
		return highPrice;
	}

	/**
	 * @param highPrice
	 *            要设置的 highPrice。
	 */
	public void setHighPrice(float highPrice) {
		this.highPrice = highPrice;
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
	 * @return 返回 lowPrice。
	 */
	public float getLowPrice() {
		return lowPrice;
	}

	/**
	 * @param lowPrice
	 *            要设置的 lowPrice。
	 */
	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
	}

	/**
	 * @return 返回 rate。
	 */
	public float getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            要设置的 rate。
	 */
	public void setRate(float rate) {
		this.rate = rate;
	}

	/**
	 * @return 返回 rateValue。
	 */
	public float getRateValue() {
		return rateValue;
	}

	/**
	 * @param rateValue
	 *            要设置的 rateValue。
	 */
	public void setRateValue(float rateValue) {
		this.rateValue = rateValue;
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
	 * @return 返回 todayPrice。
	 */
	public float getTodayPrice() {
		return todayPrice;
	}

	/**
	 * @param todayPrice
	 *            要设置的 todayPrice。
	 */
	public void setTodayPrice(float todayPrice) {
		this.todayPrice = todayPrice;
	}

	/**
	 * @return 返回 yesterdayPrice。
	 */
	public float getYesterdayPrice() {
		return yesterdayPrice;
	}

	/**
	 * @param yesterdayPrice
	 *            要设置的 yesterdayPrice。
	 */
	public void setYesterdayPrice(float yesterdayPrice) {
		this.yesterdayPrice = yesterdayPrice;
	}

}
