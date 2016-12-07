package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 商品兑换记录
 */
public class MatchExch {
	int id;
	int userId;
	int goodId;
	long buyTime;
	String goodName;
	int vote;
	int orderId;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
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
}
