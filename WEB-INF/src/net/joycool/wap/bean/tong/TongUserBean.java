package net.joycool.wap.bean.tong;

/**
 * @author macq
 * @datetime 2006-12-24 下午01:59:27
 * @explain 城帮系统
 */
public class TongUserBean {
	int id;

	int tongId;

	int userId;

	int mark;
	
	int donation;

	String createDatetime;
	
	String honor;

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
	 * @return donation
	 */
	public int getDonation() {
		return donation;
	}

	/**
	 * @param donation
	 *            要设置的 donation
	 */
	public void setDonation(int donation) {
		this.donation = donation;
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
	 * @return mark
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark
	 */
	public void setMark(int mark) {
		this.mark = mark;
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
	 * @return honor
	 */
	public String getHonor() {
		return honor;
	}

	/**
	 * @param honor 要设置的 honor
	 */
	public void setHonor(String honor) {
		this.honor = honor;
	}
}
