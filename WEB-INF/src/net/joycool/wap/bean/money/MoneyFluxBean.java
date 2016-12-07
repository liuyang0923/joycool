package net.joycool.wap.bean.money;

public class MoneyFluxBean {
	int id;

	String createDate;

	long totalIn;

	long totalOut;
	
	int type;

	/**
	 * @return Returns the createDate.
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate The createDate to set.
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the totalIn.
	 */
	public long getTotalIn() {
		return totalIn;
	}

	/**
	 * @param totalIn The totalIn to set.
	 */
	public void setTotalIn(long totalIn) {
		this.totalIn = totalIn;
	}

	/**
	 * @return Returns the totalOut.
	 */
	public long getTotalOut() {
		return totalOut;
	}

	/**
	 * @param totalOut The totalOut to set.
	 */
	public void setTotalOut(long totalOut) {
		this.totalOut = totalOut;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
