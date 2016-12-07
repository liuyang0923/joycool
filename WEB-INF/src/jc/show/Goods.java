package jc.show;

public class Goods {
	// public static int FLAG_MALE = (1 << 0); // 男性可以穿戴
	// public static int FLAG_FEMALE = (1 << 1); // 女性可以穿戴
	int issale;// 是否正在出售
	int id;
	int type;// 部位
	int flag;// 性别
	int due;// 物品过期天数
	int del;// 是否删除
	long createTime;// 增加时间
	String bigImg;// 大图
	String name;// 名称
	String bak;// 物品说明

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

	public int getDue() {
		return due;
	}

	public void setDue(int due) {
		this.due = due;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
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

	// public boolean isFlag(int is) {
	// return (flag & (1 << is)) != 0;
	// }

	// public boolean isFlagMale() {
	// return (flag & FLAG_MALE) != 0;
	// }
	//
	// public boolean isFlagFemale() {
	// return (flag & FLAG_FEMALE) != 0;
	// }

	public int getIssale() {
		return issale;
	}

	public void setIssale(int issale) {
		this.issale = issale;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

}
