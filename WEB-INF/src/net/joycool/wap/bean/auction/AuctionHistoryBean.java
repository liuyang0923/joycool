package net.joycool.wap.bean.auction;

/**
 * @author macq
 * @datetime 2006-12-12 下午04:44:40
 * @explain 拍卖物品历史记录表
 */
public class AuctionHistoryBean {
	int id;

	int auctionId;
	
	int productId;
	
	int dummyId;

	int userId;

	long price;

	String createDatetime;

	/**
	 * @return auctionId
	 */
	public int getAuctionId() {
		return auctionId;
	}

	/**
	 * @param auctionId
	 *            要设置的 auctionId
	 */
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}

	/**
	 * @return createDatetime
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            要设置的 createDatetime
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return price
	 */
	public long getPrice() {
		return price;
	}

	/**
	 * @param price 要设置的 price
	 */
	public void setPrice(long price) {
		this.price = price;
	}

	/**
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return dummyId
	 */
	public int getDummyId() {
		return dummyId;
	}

	/**
	 * @param dummyId 要设置的 dummyId
	 */
	public void setDummyId(int dummyId) {
		this.dummyId = dummyId;
	}

	/**
	 * @return productId
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * @param productId 要设置的 productId
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
