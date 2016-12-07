package net.joycool.wap.bean.tong;

import java.util.Date;

public class TongHonorBean {
	int userId;
	int tongId;
	int flag;
	Date createDatetime;
	/**
	 * @return Returns the createDatetime.
	 */
	public Date getCreateDatetime() {
		return createDatetime;
	}
	/**
	 * @param createDatetime The createDatetime to set.
	 */
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	/**
	 * @return Returns the flag.
	 */
	public int getFlag() {
		return flag;
	}
	/**
	 * @param flag The flag to set.
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	/**
	 * @return Returns the tongId.
	 */
	public int getTongId() {
		return tongId;
	}
	/**
	 * @param tongId The tongId to set.
	 */
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
