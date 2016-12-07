package net.joycool.wap.bean.tong;

/**
 * @author macq
 * @datetime 2006-12-24 下午02:05:38
 * @explain 城帮系统
 */
public class TongManageLogBean {
	int id;

	int userId;

	int receiveUserId;

	int tongId;

	int mark;

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

	public int getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(int receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

}
