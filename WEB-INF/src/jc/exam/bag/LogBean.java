package jc.exam.bag;

public class LogBean {
	int id;
	int voteId;
	int userId;
	long createTime;
	int del;
	
	public int getDel() {
		return del;
	}
	public void setDel(int del) {
		this.del = del;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVoteId() {
		return voteId;
	}
	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}