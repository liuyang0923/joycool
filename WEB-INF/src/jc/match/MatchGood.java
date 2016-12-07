package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 商品表
 */
public class MatchGood {
	int id;
	String goodName;	//品名(20字内)
	int price;			//MMB价格(专柜价格)
	int price2;			//靓点价格
	String photo;
	int count;			//库存
	String describe;	//描述(200字内)
	int flag;			//0：兑换商品.1,2,3：分别是第一、二、三名的奖品.4:第4-6名的奖品.5:7-10名的兑换奖品
	int hide;			//隐藏:0:显示,1:隐藏
	int prio;			//优先级，1-10.10的优先级最高
	int countNow;		//现在库存
	int buyCount;		//购买数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice2() {
		return price2;
	}
	public void setPrice2(int price2) {
		this.price2 = price2;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDescribe() {
		return describe;
	}
	public String getDescribeWml() {
		return StringUtil.toWml(describe);
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getHide() {
		return hide;
	}
	public void setHide(int hide) {
		this.hide = hide;
	}
	public int getPrio() {
		return prio;
	}
	public void setPrio(int prio) {
		this.prio = prio;
	}
	public int getCountNow() {
		return countNow;
	}
	public void setCountNow(int countNow) {
		this.countNow = countNow;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
}
