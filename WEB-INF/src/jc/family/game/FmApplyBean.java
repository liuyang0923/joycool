package jc.family.game;

public class FmApplyBean {
	int id;
	int mid;// 赛事id
	int fid;// 家族id
	int del;// 1表示不参加此次活动,默认为0;
	int totalApply; // 总报名人数

	public int getTotalApply() {
		return totalApply;
	}

	public void setTotalApply(int totalApply) {
		this.totalApply = totalApply;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}
}
