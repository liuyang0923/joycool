package jc.guest.sd;

public class ShuDuUser {
	int uid;
	int lvl1;
	int lvl2;
	int lvl3;
	int lvl4;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getLvl1() {
		return lvl1;
	}

	public void setLvl1(int lvl1) {
		this.lvl1 = lvl1;
	}

	public int getLvl2() {
		return lvl2;
	}

	public void setLvl2(int lvl2) {
		this.lvl2 = lvl2;
	}

	public int getLvl3() {
		return lvl3;
	}

	public void setLvl3(int lvl3) {
		this.lvl3 = lvl3;
	}

	public int getLvl4() {
		return lvl4;
	}

	public void setLvl4(int lvl4) {
		this.lvl4 = lvl4;
	}

	public int getUseLvlValue(int lvl) {
		int value = 0;
		if(lvl == 1) {
			value = this.getLvl1();
		} else if (lvl == 2) {
			value = this.getLvl2();
		} else if (lvl == 3) {
			value = this.getLvl3();
		} else if (lvl == 4) {
			value = this.getLvl4();
		}
		return value;
	}
	
}
