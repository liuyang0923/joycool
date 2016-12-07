package net.joycool.wap.bean.tong;

public class TongFriendBean {
	//结盟标志1为已经结盟2为拒绝结盟
	public static int ALLYMARK = 1;

	int id;

	int mark;

	int tongId;

	int fTongId;

	public int getFTongId() {
		return fTongId;
	}

	public void setFTongId(int tongId) {
		fTongId = tongId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getTongId() {
		return tongId;
	}

	public void setTongId(int tongId) {
		this.tongId = tongId;
	}

}
