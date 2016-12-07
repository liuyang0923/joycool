package net.joycool.wap.bean.charitarian;

public class CharitarianHistoryBean {
	int id;

	int charitarianId;

	int receiveId;

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
	 * @return 返回 charitarianId。
	 */
	public int getCharitarianId() {
		return charitarianId;
	}

	/**
	 * @param charitarianId
	 *            要设置的 charitarianId。
	 */
	public void setCharitarianId(int charitarianId) {
		this.charitarianId = charitarianId;
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
	 * @return 返回 receiveId。
	 */
	public int getReceiveId() {
		return receiveId;
	}

	/**
	 * @param receiveId
	 *            要设置的 receiveId。
	 */
	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}
}
