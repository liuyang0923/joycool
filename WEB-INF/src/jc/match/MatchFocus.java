package jc.match;

/**
 * 关注(uid将uid2加为了关注)
 * @author Administrator
 *
 */
public class MatchFocus {
	int id;
	int uid;
	int uid2;
	long focusTime;
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
	public int getUid2() {
		return uid2;
	}
	public void setUid2(int uid2) {
		this.uid2 = uid2;
	}
	public long getFocusTime() {
		return focusTime;
	}
	public void setFocusTime(long focusTime) {
		this.focusTime = focusTime;
	}
}
