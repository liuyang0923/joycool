package net.joycool.wap.bean.tong;

public class TongTransferBean {
	//本帮成员
	public static int TONG_USER = 1;
	//其他帮帮主
	public static int TONG_OTHER_USER = 2;

	int tongId;

	int tongUserId;

	int receiveUserId;

	int mark;

	/**
	 * @return 返回 mark。
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark。
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return 返回 receiveUserId。
	 */
	public int getReceiveUserId() {
		return receiveUserId;
	}

	/**
	 * @param receiveUserId
	 *            要设置的 receiveUserId。
	 */
	public void setReceiveUserId(int receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	/**
	 * @return 返回 tongId。
	 */
	public int getTongId() {
		return tongId;
	}

	/**
	 * @param tongId
	 *            要设置的 tongId。
	 */
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}

	/**
	 * @return 返回 tongUserId。
	 */
	public int getTongUserId() {
		return tongUserId;
	}

	/**
	 * @param tongUserId
	 *            要设置的 tongUserId。
	 */
	public void setTongUserId(int tongUserId) {
		this.tongUserId = tongUserId;
	}
}
