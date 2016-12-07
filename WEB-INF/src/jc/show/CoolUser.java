package jc.show;

public class CoolUser {
	private int id;
	private int uid;// user_id
	private int sumMyGoods;// 我的物品总数
	private int genderUseing;// 使用的形象性别
	private String imgurl = "";// 使用的形象地址
	private String imgurlM = "";// 试穿的男形象地址
	private String imgurlF = "";// 试穿的女形象地址
	private String curItem;// 当前穿戴物品
	private long endTime;// 过期时间

	public String getCurItem() {
		return curItem;
	}

	public void setCurItem(String curItem) {
		this.curItem = curItem;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
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

	public int getGenderUseing() {
		return genderUseing;
	}

	public void setGenderUseing(int genderUseing) {
		this.genderUseing = genderUseing;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getImgurlM() {
		return imgurlM;
	}

	public void setImgurlM(String imgurlM) {
		this.imgurlM = imgurlM;
	}

	public String getImgurlF() {
		return imgurlF;
	}

	public void setImgurlF(String imgurlF) {
		this.imgurlF = imgurlF;
	}

	public int getSumMyGoods() {
		return sumMyGoods;
	}

	public void setSumMyGoods(int sumMyGoods) {
		this.sumMyGoods = sumMyGoods;
	}

}
