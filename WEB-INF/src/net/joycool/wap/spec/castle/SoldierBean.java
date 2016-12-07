package net.joycool.wap.spec.castle;

public class SoldierBean {

	private int uid;
	private int soldierType;	//士兵类型，长枪兵，剑兵等。。
	private int count;			//某个类型的士兵数量
	
	//升级所需要的材料和时间,通过ResNeed类来计算
	private int wood;
	private int fe;
	private int grain;
	private int stone;
	private int interval;
	
	public SoldierBean(){}
	
	public SoldierBean(int race, int type, int uid, float factor) {
		ResTBean res = ResNeed.getSoldierNeedRes(race, type);
		soldierType = type;
		this.uid = uid;
		interval = (int)(res.getTime() * factor);
		this.wood = res.getWood();
		this.fe = res.getFe();
		this.grain = res.getGrain();
		this.stone = res.getStone();
	}

	public String getSoldierName(int race) {
		return ResNeed.getSoldierType(race, soldierType);
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getSoldierType() {
		return soldierType;
	}
	public void setSoldierType(int soldierType) {
		this.soldierType = soldierType;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getFe() {
		return fe;
	}

	public void setFe(int fe) {
		this.fe = fe;
	}

	public int getGrain() {
		return grain;
	}

	public void setGrain(int grain) {
		this.grain = grain;
	}

	public int getInterval() {
		return interval;
	}

	public void setTime(int interval) {
		this.interval = interval;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}
	
}
