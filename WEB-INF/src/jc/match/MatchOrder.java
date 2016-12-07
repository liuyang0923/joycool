package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 用户订单
 */
public class MatchOrder {
	int id;
	int userId;
	String phone;		//手机
	String userName;	//姓名
	String address;		//地址
	String goodName;	//商品名
	int goodId;			//商品ID
	long buyTime;		//购买时间
	long sendTime;		//发货时间
	int price;			//价格
	int actualPrice;	//实扣价格
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserNameWml(){
		return StringUtil.toWml(userName);
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public String getAddressWml() {
		return StringUtil.toWml(address);
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGoodName() {
		return goodName;
	}
	public String getGoodNameWml() {
		return StringUtil.toWml(goodName);
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public int getGoodId() {
		return goodId;
	}
	public void setGoodId(int goodId) {
		this.goodId = goodId;
	}
	public long getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(int actualPrice) {
		this.actualPrice = actualPrice;
	}
}
