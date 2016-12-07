package net.joycool.wap.spec.castle;

import java.util.List;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;


/**
 * @author bomb
 * 常用的队列，例如拆毁、删号、举行活动
 */
public class CommonThreadBean {
	
	static CastleService castleService = CastleService.getInstance();
	static CacheService cacheService = CacheService.getInstance();
	
	int id;
	int type;
	int uid;
	int cid;
	long startTime;
	int interval;
	long endTime;
	int value;
	
	public CommonThreadBean(){}
	
	public CommonThreadBean(int uid, int cid, int type, int time, int value) {
		this.type = type;
		this.uid = uid;
		this.cid = cid;

		interval = time;
		if(SqlUtil.isTest)	interval = 20;
		startTime = System.currentTimeMillis();
		endTime = startTime + interval * 1000;
		this.value = value;
	}

	
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	// 执行时间到，执行
	public void execute(){
//		UserResBean userResBean = CastleUtil.getUserResBeanByUid(uid);
		try {
			switch(type) {
			case 1:		// 举行活动
				celebrate();
				break;
			case 2:		// 拆毁建筑
				destroyBuilding();
				break;
			case 3:		// 删号
				destroyAccount();
				break;
			case 4:		//资源产量增加或者攻防增加
				updateUserFlag();
				break;
			case 5:	// 英雄复活或者训练
				updateHero();
				break;
			case 6:	// 放弃绿洲
				abandonOasis();
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		cacheService.deleteCacheCommon(id);
		
//		//加入建筑完毕消息
//		StringBuilder content = new StringBuilder();// + "建筑成功";
//		content.append(bt.getName());
//		content.append("等级");
//		content.append(grade);
//		content.append("完成建造");
//		CastleMessage castleMessage = new CastleMessage(content.toString(), uid);
//		castleService.addCastleMessage(castleMessage);
		
	}
	
	private void abandonOasis() {
		CastleBean castle = CastleUtil.getCastleById(cid);
		if(castle == null)
			return;
		OasisBean oasis = CastleUtil.getOasisById(value);
		if(oasis == null || oasis.getCid() != cid)
			return;
		CastleUtil.abandonOasis(castle, oasis);
	}

	private void updateHero() {
		CastleUserBean user = CastleUtil.getCastleUser(uid);
		long now = System.currentTimeMillis();
		if(value > 0) {	// 复活
			HeroBean hero = castleService.getHero("id=" + value);
			if(hero != null) {
				hero.setHealth(100f);
				hero.setStatus(HeroBean.STATUS_ALIVE);
				castleService.updateHeroSimple(hero);
				user.setHero(hero);
				
				UserResBean userRes = CastleUtil.getUserResBeanById(cid);
				userRes.addPeople2Calc(ResNeed.heroGrainCost, now);
				
				CastleArmyBean army = CastleUtil.getCastleArmy(cid);
				if(army != null) {
					army.setHero(1);
					castleService.updateArmyHero(army);
				}
			}
		} else {	// 训练英雄
			int stype = -value;	// -1表示要训练兵种类型为1的指挥官
			if(user.getHero() != null)
				return;
			HeroBean hero = new HeroBean();
			hero.setUid(uid);
			hero.setRace(user.getRace());
			hero.setType(stype);
			hero.setCreateTime(now);
			hero.setRace(user.getRace());
			hero.setName(user.getName());
			castleService.addHero(hero);
			user.setHero(hero);
			
			UserResBean userRes = CastleUtil.getUserResBeanById(cid);
			userRes.addPeople2Calc(ResNeed.heroGrainCost, now);
			
			CastleArmyBean army = CastleUtil.getCastleArmy(cid);
			if(army != null) {
				army.setHero(1);
				castleService.updateArmyHero(army);
			}
		}
		
	}

	private void updateUserFlag() {
		CastleUserBean user = CastleUtil.getCastleUser(uid);
		List list = user.getCastleList();
		for(int i = 0;i < list.size();i++) {
			Integer iid = (Integer)list.get(i);
			UserResBean userResBean = CastleUtil.getUserResBeanById(iid.intValue());
		
			userResBean.reCalc(System.currentTimeMillis());
			userResBean.deleteFlag(value);
			castleService.updateUserResFlag(userResBean);
		}
	}

	public void celebrate() {
		CastleUserBean user = CastleUtil.getCastleUser(uid);
		if(user == null)
			return;
		user.addCivil(value);
		castleService.updateUserCivil(user);
	}
	
	public void destroyBuilding() {
		BuildingBean bean = castleService.getBuildingBeanByPos(cid, value);
		if(bean == null)
			return;

		CastleUtil.destroyBuilding(uid, bean, bean.getGrade() - 1);
	}
	
	public void destroyAccount() {
		CastleUtil.delAccount(uid);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	public String getTimeLeft(){
		long now = System.currentTimeMillis();
		return DateUtil.formatTimeInterval2((int) ((endTime - now) / 1000));
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
}
