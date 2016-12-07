package net.joycool.wap.spec.app;

public class AppUserBean {
	int userId;
	String favor;
	long createTime;
	int count;
	int flag;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFavor() {
		return favor;
	}
	public void setFavor(String favor) {
		this.favor = favor;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}