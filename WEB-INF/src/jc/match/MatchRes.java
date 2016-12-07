package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 虚拟物品表
 */
public class MatchRes {
	int id;
	String resName;	//物品名称
	int prices;		//价格
	int point;		//折合多少积分
	int cur;  		//币种 0：乐币 1：酷币
	String photo;	//图片
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getResName() {
		return resName;
	}
	public String getResNameWml() {
		return StringUtil.toWml(resName);
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public int getPrices() {
		return prices;
	}
	public void setPrices(int prices) {
		this.prices = prices;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getCur() {
		return cur;
	}
	public void setCur(int cur) {
		this.cur = cur;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}
