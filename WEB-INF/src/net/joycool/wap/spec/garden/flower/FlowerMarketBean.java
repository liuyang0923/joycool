package net.joycool.wap.spec.garden.flower;

public class FlowerMarketBean {
	int id;
	int flowerId;
	int type;
	int price;
	int state;
	int SellerUid;
	int buyerUid;
	long releaseTime;
	long buyTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFlowerId() {
		return flowerId;
	}
	public void setFlowerId(int flowerId) {
		this.flowerId = flowerId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSellerUid() {
		return SellerUid;
	}
	public void setSellerUid(int sellerUid) {
		SellerUid = sellerUid;
	}
	public int getBuyerUid() {
		return buyerUid;
	}
	public void setBuyerUid(int buyerUid) {
		this.buyerUid = buyerUid;
	}
	public long getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}
	public long getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}
}