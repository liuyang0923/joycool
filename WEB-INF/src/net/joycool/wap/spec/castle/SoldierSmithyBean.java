package net.joycool.wap.spec.castle;

public class SoldierSmithyBean {
	int id;
	int cid;
	int time;
	int soldierType;
	int attack;
	int defence;
		
	public SoldierSmithyBean() {
	}
	
	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getSoldierType() {
		return soldierType;
	}
	public void setSoldierType(int soldierType) {
		this.soldierType = soldierType;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
		
}
