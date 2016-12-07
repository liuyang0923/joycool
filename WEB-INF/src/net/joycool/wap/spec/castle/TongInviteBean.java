package net.joycool.wap.spec.castle;

public class TongInviteBean {

	int id;
	int fromUid;
	int toUid;
	int tongId;
	
	public TongInviteBean() {
	}
	public TongInviteBean(int fromUid, int toUid, int tongId) {
		this.fromUid = fromUid;
		this.toUid = toUid;
		this.tongId = tongId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFromUid() {
		return fromUid;
	}
	public void setFromUid(int fromUid) {
		this.fromUid = fromUid;
	}
	public int getToUid() {
		return toUid;
	}
	public void setToUid(int toUid) {
		this.toUid = toUid;
	}
	public int getTongId() {
		return tongId;
	}
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}
	
	
	
}
