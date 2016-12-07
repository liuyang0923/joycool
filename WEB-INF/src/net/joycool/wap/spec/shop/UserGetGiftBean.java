package net.joycool.wap.spec.shop;

import java.util.Date;

public class UserGetGiftBean {

	int id;
	int fromUid;
	int toUid;
	int itemId;
	Date time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFromUid() {
		return fromUid;
	}
	public void setFromUid(int fromUid) {
		this.fromUid = fromUid;
	}
	public int getToUid() {
		return toUid;
	}
	public void setToUid(int toUid) {
		this.toUid = toUid;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
