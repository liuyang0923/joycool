package net.joycool.wap.bean.stock;

public class StockBean {
	int id;

	String name;

	String introduction;

	// liuyi 2006-12-28 股票bug修改 start
	long totalCount;

	long soldCount;

	// liuyi 2006-12-28 股票bug修改 end

	float price;

	String create_datetime;

	long soldIn;

	long soldOut;

	/**
	 * @param soldIn
	 *            The soldIn to set.
	 */
	public void setSoldIn(long soldIn) {
		this.soldIn = soldIn;
	}

	/**
	 * @param soldOut
	 *            The soldOut to set.
	 */
	public void setSoldOut(long soldOut) {
		this.soldOut = soldOut;
	}

	/**
	 * @param soldOut
	 *            The soldOut to set.
	 */
	public void setSoldOut(int soldOut) {
		this.soldOut = soldOut;
	}

	/**
	 * @return Returns the soldIn.
	 */
	public long getSoldIn() {
		return soldIn;
	}

	/**
	 * @return Returns the soldOut.
	 */
	public long getSoldOut() {
		return soldOut;
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
	 * @return 返回 create_datetime。
	 */
	public String getCreate_datetime() {
		return create_datetime;
	}

	/**
	 * @param create_datetime
	 *            要设置的 create_datetime。
	 */
	public void setCreate_datetime(String create_datetime) {
		this.create_datetime = create_datetime;
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
	 * @return 返回 introduction。
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction
	 *            要设置的 introduction。
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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
	 * @return 返回 soldCount。
	 */
	public long getSoldCount() {
		return soldCount;
	}

	/**
	 * @param soldCount
	 *            要设置的 soldCount。
	 */
	public void setSoldCount(long soldCount) {
		this.soldCount = soldCount;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

}
