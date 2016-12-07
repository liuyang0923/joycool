package net.joycool.wap.spec.app;


public class AppTypeBean {
	int id;
	String name;
	String info;
	int count;		// 下属组件数量
	int seq;		// 按顺序显示
	int parentId;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public void addCount(int add) {
		count += add;
		if(count < 0)
			count = 0;
	}
}
