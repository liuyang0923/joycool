package net.joycool.wap.bean.home;

public class HomePlayerVote {
	int id;
	int fromUid;
	int toUid;
	long voteTime;
	int del;
	int flag;
	public int getDel() {
		return del;
	}
	public void setDel(int del) {
		this.del = del;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getFromUid() {
		return fromUid;
	}
	public void setFromUid(int fromUid) {
		this.fromUid = fromUid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getToUid() {
		return toUid;
	}
	public void setToUid(int toUid) {
		this.toUid = toUid;
	}
	public long getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(long voteTime) {
		this.voteTime = voteTime;
	}
	
}
