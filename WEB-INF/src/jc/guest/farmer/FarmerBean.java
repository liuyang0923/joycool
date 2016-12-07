package jc.guest.farmer;

public class FarmerBean {
	int uid;
	int lv1;
	int lv2;
	int lv3;
	int lv4;
	int changeLv;
	long changeTime;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getLv1() {
		return lv1;
	}

	public void setLv1(int lv1) {
		this.lv1 = lv1;
	}

	public int getLv2() {
		return lv2;
	}

	public void setLv2(int lv2) {
		this.lv2 = lv2;
	}

	public int getLv3() {
		return lv3;
	}

	public void setLv3(int lv3) {
		this.lv3 = lv3;
	}

	public int getLv4() {
		return lv4;
	}

	public void setLv4(int lv4) {
		this.lv4 = lv4;
	}

	public int getChangeLv() {
		return changeLv;
	}

	public void setChangeLv(int changeLv) {
		this.changeLv = changeLv;
	}

	public long getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(long changeTime) {
		this.changeTime = changeTime;
	}

	public int getUseLv(int lv) {
		if (lv == 1) {
			return getLv1();
		} else if (lv == 2) {
			return getLv2();
		} else if (lv == 3) {
			return getLv3();
		} else if (lv == 4) {
			return getLv4();
		} else {
			return 0;
		}
	}
}
