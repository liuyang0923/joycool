package net.joycool.wap.spec.castle;

import java.util.Date;

import net.joycool.wap.util.StringUtil;

public class TradeBean {
	public static String[] resString = {"", "木材", "石头", "铁块", "粮食"};
	int id;
	int cid;
	int supply;	// 1 wood 2 stone 3 fe 4 grain
	int need;
	int supplyRes;
	int needRes;
	int supplyMerchant;		// 提供的商人
	int needMerchant;		// 返回的商人
	int distance;	// 最大距离的平方，0表示无限制
	int x, y;		// 出发点坐标
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getNeedRes() {
		return needRes;
	}
	public void setNeedRes(int needRes) {
		this.needRes = needRes;
	}

	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getNeed() {
		return need;
	}
	public void setNeed(int need) {
		this.need = need;
	}
	public int getSupply() {
		return supply;
	}
	public void setSupply(int supply) {
		this.supply = supply;
	}
	public int getSupplyMerchant() {
		return supplyMerchant;
	}
	public void setSupplyMerchant(int supplyMerchant) {
		this.supplyMerchant = supplyMerchant;
	}
	public int getSupplyRes() {
		return supplyRes;
	}
	public void setSupplyRes(int supplyRes) {
		this.supplyRes = supplyRes;
	}
	
	public String getNeedName() {
		return resString[need];
	}
	public String getSupplyName() {
		return resString[supply];
	}
	public int getNeedMerchant() {
		return needMerchant;
	}
	public void setNeedMerchant(int needMerchant) {
		this.needMerchant = needMerchant;
	}
	public int getNeedMerchant(int carry) {
		return (needRes - 1) / carry + 1;
	}
}
