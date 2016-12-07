package jc.show;

import java.util.List;

import net.joycool.wap.util.StringUtil;

public class Commodity {
	int id;
	int Iid;// item_id
	int due;// 商品过期天数
	int type;// 部位 = part
	int catalog;	// 商品分类
	int count;// 卖出总数
	int state;// 备用
	int gender;// 性别要求
	int del;// 是否隐藏
	float price;// 商品价格
	long createTime;// 增加时间
	String name;// 商品名称
	String bak;// 商品描述
	String bigImg = "";// 商品展示图
	
	int levelLayer;		// 以下同beanpart
	int levelShow;
	int index;
	String partName;
	
	String partOther;		// 覆盖其他part位置，例如套装要覆盖裤子
	int partOtherCount;		// 覆盖几个位置，0表示没有
	int[] partOtherIndex;		// partOther解析的结果，并对应找到index
	
	int next;	// 这个物品连接到另一个物品（例如披风需要两个图片）0表示没有
	
	String catalogName;
	
	// 以下从goods中复制过来
	String goodsImg = "";	// 物品用于合成的图片

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public int getLevelLayer() {
		return levelLayer;
	}

	public void setLevelLayer(int levelLayer) {
		this.levelLayer = levelLayer;
	}

	public int getLevelShow() {
		return levelShow;
	}

	public void setLevelShow(int levelShow) {
		this.levelShow = levelShow;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public float getPrice3() {
		return price * 2;
	}

	public float getPrice6() {
		return price * 3;
	}

	public float getPrice12() {
		return price * 5;
	}

	public float getPrice(int i) {
		if (i == 3)
			return price * 2;
		else if (i == 6)
			return price * 3;
		else if (i == 12)
			return price * 5;
		else
			return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getIid() {
		return id;
	}

	public void setIid(int iid) {
		Iid = iid;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getBigImg() {
		return bigImg;
	}

	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getDue() {
		return due;
	}

	public void setDue(int due) {
		this.due = due;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public String getPartOther() {
		return partOther;
	}

	public void setPartOther(String partOther) {
		partOther = StringUtil.noEnter(partOther);
		this.partOther = partOther;
		List list = StringUtil.toInts(partOther);
		partOtherCount = list.size();
		int[] p = new int[partOtherCount];
		for(int i = 0;i < partOtherCount;i++) {
			Integer iid = (Integer)list.get(i);
			p[i] = CoolShowAction.getPart(iid.intValue()).getIndex();
		}
		partOtherIndex = p;
	}

	public int getPartOtherCount() {
		return partOtherCount;
	}

	public void setPartOtherCount(int partOtherCount) {
		this.partOtherCount = partOtherCount;
	}

	public int[] getPartOtherIndex() {
		return partOtherIndex;
	}

	public void setPartOtherIndex(int[] partOtherIndex) {
		this.partOtherIndex = partOtherIndex;
	}

}
