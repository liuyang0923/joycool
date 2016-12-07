package net.joycool.wap.bean.tong;

/**
 * @author macq
 * @datetime 2006-12-24 下午01:58:08
 * @explain 城帮系统
 */
public class TongApplyBean {
	int id;

	int tongId;

	int userId;

	int managerId;

	String content;

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
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            要设置的 content
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return managerId
	 */
	public int getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId
	 *            要设置的 managerId
	 */
	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

}
