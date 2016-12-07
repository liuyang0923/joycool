package jc.family.game;

public class ApplyBean {
	int id;// 主键
	int uid;// 用户id
	int mid;// 赛事id
	int fid; // 家族id
	int type;// 1(龙舟)2(雪仗)3(问答)
	int state;// 0(审批通过)1(不通过)
	int isPay;// 0没扣钱,1已经扣钱

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
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

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}
}
