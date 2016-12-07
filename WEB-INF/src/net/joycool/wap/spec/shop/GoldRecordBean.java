package net.joycool.wap.spec.shop;

import java.util.Date;

public class GoldRecordBean {

	int id;
	int uid;
	float gold;
	int type;
	int itemId;
	Date time;
	int userId;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public float getGold() {
		return gold;
	}
	public void setGold(float gold) {
		this.gold = gold;
	}
	
	public String getGoldString(){
		return ShopUtil.formatPrice3(gold);
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
