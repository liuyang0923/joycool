package jc.family.game.pvz;

/**
 * 植物or僵尸(内存)
 * 
 * @author qiuranke
 * 
 */
public class PorZ {

	int id;// 类型
	int hp;
	int userid;
	long moveCd;
	long attackCd;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public long getMoveCd() {
		return moveCd;
	}

	public void setMoveCd(long moveCd) {
		this.moveCd = moveCd;
	}

	public long getAttackCd() {
		return attackCd;
	}

	public void setAttackCd(long attackCd) {
		this.attackCd = attackCd;
	}

}
