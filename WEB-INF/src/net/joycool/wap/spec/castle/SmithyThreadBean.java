package net.joycool.wap.spec.castle;

public class SmithyThreadBean {
	static CastleService castleService = CastleService.getInstance();
	static CacheService cacheService = CacheService.getInstance();

	int id;
	int cid;
	long startTime;
	long endTime;
	//0表示攻击...1表示防御...
	int smithyType;	//1标识升级攻击，2标识升级防御，3标识兵种研发
	int soldierType;
	
	public SmithyThreadBean(int cid, int smithyType, int soldierType, int interval) {
		this.cid = cid;
		this.smithyType = smithyType;
		this.soldierType = soldierType;
		this.startTime = System.currentTimeMillis();
		//TODO interval=>5000
		this.endTime = startTime + interval;
	}
	
	
	public SmithyThreadBean() {
	}
	public void upgrade(){
//		SoldierResBean soldier = ResNeed.getSoldierRes(soldierType);
		SoldierSmithyBean[] smithys = CastleBaseAction.getSmithys(cid);
//		StringBuilder content = new StringBuilder();
//		content.append(soldier.getSoldierName());
		if(smithyType == 0) {
			if(smithys[soldierType] != null) {
				castleService.updateSoldierAttack(cid, soldierType);
//				content.append("攻击升级完成");
				smithys[soldierType].setAttack(smithys[soldierType].getAttack() + 1);
			}
		} else if(smithyType == 1) {
			if(smithys[soldierType] != null) {
				castleService.updateSoldierDefence(cid, soldierType);
//				content.append("防御升级完成");
				smithys[soldierType].setDefence(smithys[soldierType].getDefence() + 1);
			}
		} else if(smithyType == 2) {
			if(smithys[soldierType] == null) {
				SoldierSmithyBean ssBean = new SoldierSmithyBean();
				ssBean.setCid(cid);
				ssBean.setSoldierType(soldierType);
				castleService.addSoldierSmithy(ssBean);
//				content.append("研发完成");
				smithys[soldierType] = ssBean;
			}
		}
//		CastleMessage castleMessage = new CastleMessage(content.toString(), uid);
//		castleService.addCastleMessage(castleMessage);
		cacheService.deleteSmithy(id);
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getSmithyType() {
		return smithyType;
	}
	public void setSmithyType(int smithyType) {
		this.smithyType = smithyType;
	}
	public int getSoldierType() {
		return soldierType;
	}
	public void setSoldierType(int soldierType) {
		this.soldierType = soldierType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}

