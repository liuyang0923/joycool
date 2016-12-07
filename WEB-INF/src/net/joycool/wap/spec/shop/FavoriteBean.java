package net.joycool.wap.spec.shop;

import java.util.Date;

public class FavoriteBean {

	int id;
	int uid;
	int itemId;
	Date time;
	
	public FavoriteBean(){}
	public FavoriteBean(int uid, int itemId) {
		this.uid = uid;
		this.itemId = itemId;
		this.time = new Date();
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
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
