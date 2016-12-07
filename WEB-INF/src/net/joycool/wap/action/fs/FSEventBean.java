package net.joycool.wap.action.fs;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-26 15:43:55
 */
public class FSEventBean {
	int id;

	int productId;

	String description;

	float priceChange;

	/**
	 * @return 返回 description。
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            要设置的 description。
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return 返回 priceChange。
	 */
	public float getPriceChange() {
		return priceChange;
	}

	/**
	 * @param priceChange
	 *            要设置的 priceChange。
	 */
	public void setPriceChange(float priceChange) {
		this.priceChange = priceChange;
	}

	/**
	 * @return 返回 productId。
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            要设置的 productId。
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
