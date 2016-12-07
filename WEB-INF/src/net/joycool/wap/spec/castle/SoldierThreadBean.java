package net.joycool.wap.spec.castle;

public class SoldierThreadBean {
	static CacheService cacheService = CacheService.getInstance();
	static CastleService castleService = CastleService.getInstance();
	int id;
	int cid;
	int soldierType;	//士兵类型，长枪兵，剑兵等。。	101表示陷阱
	int count;			//某个类型的士兵数量
	long startTime;
	int interval;
	long endTime;
	int type;		// 从哪个建筑物建造的
	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SoldierThreadBean() {}

	public SoldierThreadBean(int cid, int type, int count, int interval){
		this.cid = cid;
		this.soldierType = type;
		this.count = count;
		this.interval = interval;
		this.startTime =  System.currentTimeMillis();
		this.endTime = startTime + interval * 1000;
	}
	
	//生产士兵
	public void produceSoldier() {
		if(count > 1) {
			cacheService.updateCacheSoldier(id);
			count -= 1;
			endTime += interval * 1000;
		} else {
			count = 0;
			cacheService.deleteCacheSoldier(id);
		}
		if(soldierType <= 100) {		// 造兵
			CastleArmyBean army = CastleUtil.getCastleArmy(cid);
			if(army == null)		// 不应该出现
				return;
			army.addCount(soldierType);
			castleService.updateSoldierCount(army, soldierType);
			
		} else if(soldierType == 101) {	// 制造陷阱
			UserResBean userRes = CastleUtil.getUserResBeanById(cid);
			userRes.addTrapDB(1);
		}
	}

	public int getSoldierType() {
		return soldierType;
	}

	public void setSoldierType(int soldierType) {
		this.soldierType = soldierType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getAllEndTime() {
		return endTime + interval * 1000 * (count - 1);
	}
}
