package jc.family.game.fightbox;

public class BoxShowBean {
	int side; // 玩家属于哪一边
	int weapon; // 玩家所使用的武器
	int personNum;

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public int getWeapon() {
		return weapon;
	}

	public void setWeapon(int weapon) {
		this.weapon = weapon;
	}

	public int getPersonNum() {
		return personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
}
