package jc.guest.farmer;

public class FarmGameBean {
	int id;
	int uid; // 游戏人id
	int bombs; // 炸弹
	int steps; // 步数
	int index; // 人所在地点
	int size; // 正方形场地边长
	int[] farm; // 场地

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

	public int getBombs() {
		return bombs;
	}

	public void setBombs(int bombs) {
		this.bombs = bombs;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int[] getFarm() {
		return farm;
	}

	public void setFarm(int[] farm) {
		this.farm = farm;
	}

}
