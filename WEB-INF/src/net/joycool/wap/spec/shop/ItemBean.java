package net.joycool.wap.spec.shop;

import java.util.Date;

public class ItemBean {
	int id;
	int itemId;
	String name;
	float price;
	Date time;
	int type;
	int funType;
	int funType2;
	int sugguest;
	int present;
	int count;	//	被购买的次数
	int max;
	int odd;
	int hidden;
	String desc;
	String photoUrl;
	int flag;
	int seq;
	int due;	//物品有效期
	int times;	//使用次数
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public int getHidden() {
		return hidden;
	}
	public void setHidden(int hidden) {
		this.hidden = hidden;
	}
	public int getOdd() {
		return odd;
	}
	public void setOdd(int odd) {
		this.odd = odd;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public float getPrice() {
		return price;
	}
	
	public String getPriceString(){
		return ShopUtil.formatPrice(price);
//		float tmp = price;
//		if(price == (float)((int)tmp)) {
//			return ""+(int)price;
//		} else {
//			return ""+price;
//		}
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getFunType() {
		return funType;
	}
	public void setFunType(int funType) {
		this.funType = funType;
	}
	public int getFunType2() {
		return funType2;
	}
	public void setFunType2(int funType2) {
		this.funType2 = funType2;
	}
	public int getSugguest() {
		return sugguest;
	}
	public void setSugguest(int sugguest) {
		this.sugguest = sugguest;
	}
	public int getPresent() {
		return present;
	}
	public void setPresent(int present) {
		this.present = present;
	}
	
	
	public String getLeft(){
		return (odd < 0?"无穷多":(odd==0?"暂无":""+odd));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDue() {
		return due;
	}
	public void setDue(int due) {
		this.due = due;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	
}
