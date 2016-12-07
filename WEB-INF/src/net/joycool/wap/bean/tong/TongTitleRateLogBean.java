package net.joycool.wap.bean.tong;

/**
 * @author macq
 * @datetime 2006-12-24 下午02:04:32
 * @explain 城帮系统
 */
public class TongTitleRateLogBean {
	int id;

	int userId;

	int tongId;

	int goodsId;

	int goodsType;

	int money;

	int rateMoney;

	String createDatetime;

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
	 * @return goodsId
	 */
	public int getGoodsId() {
		return goodsId;
	}

	/**
	 * @param goodsId
	 *            要设置的 goodsId
	 */
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * @return goodsType
	 */
	public int getGoodsType() {
		return goodsType;
	}

	/**
	 * @param goodsType
	 *            要设置的 goodsType
	 */
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
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
	 * @return money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            要设置的 money
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return rateMoney
	 */
	public int getRateMoney() {
		return rateMoney;
	}

	/**
	 * @param rateMoney
	 *            要设置的 rateMoney
	 */
	public void setRateMoney(int rateMoney) {
		this.rateMoney = rateMoney;
	}

	/**
	 * @return tongId
	 */
	public int getTongId() {
		return tongId;
	}

	/**
	 * @param tongId
	 *            要设置的 tongId
	 */
	public void setTongId(int tongId) {
		this.tongId = tongId;
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

}
