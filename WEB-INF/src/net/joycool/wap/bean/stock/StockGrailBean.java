package net.joycool.wap.bean.stock;

public class StockGrailBean {
	int id;

	float nowPrice;

	float todayPrice;

	float yesterdayPrice;

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
	 * @return 返回 nowPrice。
	 */
	public float getNowPrice() {
		return nowPrice;
	}

	/**
	 * @param nowPrice
	 *            要设置的 nowPrice。
	 */
	public void setNowPrice(float nowPrice) {
		this.nowPrice = nowPrice;
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
