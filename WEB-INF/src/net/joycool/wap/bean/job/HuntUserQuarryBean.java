package net.joycool.wap.bean.job;

public class HuntUserQuarryBean {

	int id;

	int userId;

	int quarryId;

	int quarryCount;

	// macq_2006-12-29_城帮拍卖物品类型字段_start
	int goodsTpye;
	// macq_2006-12-29_城帮拍卖物品类型字段_end

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the quarryCount.
	 */
	public int getQuarryCount() {
		return quarryCount;
	}

	/**
	 * @param quarryCount
	 *            The quarryCount to set.
	 */
	public void setQuarryCount(int quarryCount) {
		this.quarryCount = quarryCount;
	}

	/**
	 * @return Returns the quarryId.
	 */
	public int getQuarryId() {
		return quarryId;
	}

	/**
	 * @param quarryId
	 *            The quarryId to set.
	 */
	public void setQuarryId(int quarryId) {
		this.quarryId = quarryId;
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return goodsTpye
	 */
	public int getGoodsTpye() {
		return goodsTpye;
	}

	/**
	 * @param goodsTpye
	 *            要设置的 goodsTpye
	 */
	public void setGoodsTpye(int goodsTpye) {
		this.goodsTpye = goodsTpye;
	}

}
