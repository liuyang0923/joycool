package jc.show;

public class BeanAdv {
	private int id;
	private int commid;// 商品id
	private int place;// 前台广告出现地址
	private int flag;// 备用
	private int del;// 是否删除
	private long createtime;// 增加时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCommid() {
		return commid;
	}

	public void setCommid(int commid) {
		this.commid = commid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}
}
