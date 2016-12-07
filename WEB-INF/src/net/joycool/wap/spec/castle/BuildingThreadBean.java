package net.joycool.wap.spec.castle;

import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;


public class BuildingThreadBean {
	static CastleService castleService = CastleService.getInstance();
	static CacheService cacheService = CacheService.getInstance();
	
	int id;
	int buildPos;
	int type;
	int cid;
	int grade;
	int people;
	long startTime;
	int interval;
	long endTime;
	//建造，升级不同操作
	int state;
	
	public BuildingThreadBean(){}
	// oldEnd是上一个建筑结束的时间，如果大于0表示是队列建造
	public BuildingThreadBean(BuildingTBean bean, int time, int cid, int pos, long oldEnd) {
		buildPos = pos;
		type = bean.getBuildType();
		grade = bean.getGrade();
		people = bean.getPeople();
		this.cid = cid;
		interval = time;
		if(oldEnd == 0) {
			startTime =  System.currentTimeMillis();
			endTime = startTime + interval * 1000;
		} else {
			startTime =  oldEnd + 60000;	// 之前的建筑完成后等待60秒开始建造
			endTime = startTime + interval * 1000 + 60000;
		}
		
	}

	
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void startBuild(){
		
		UserResBean userResBean = CastleUtil.getUserResBeanById(cid);
		int uid = userResBean.getUserId();
		
		BuildingBean bean = null;
		BuildingTBean bt = ResNeed.getBuildingT(type);		// 对应的建筑
		BuildingTBean bt1 = null;	// 建筑当前级别
				

		bean = castleService.getBuildingBeanByPos(cid, buildPos);
		if(bean == null) {
			cacheService.deleteCacheBuilding(id);
			return;
		}
		grade = bean.getGrade() + 1;	// 根据当前建筑等级，+1
		if(grade > bt.getMaxGrade()) {
			cacheService.deleteCacheBuilding(id);
			return;
		}
		BuildingTBean bt2 = ResNeed.getBuildingT(type, grade);		// 建筑升级后级别
		
		int addValue = bt2.getValue();
		
		bean = new BuildingBean(type, grade, cid, people, buildPos);
		castleService.updateBuilding(bean);		
		
		if(grade == 1) {
			userResBean.addCivil(bt2.getCivil());		// 更新某个城堡的文明增长速度 
			CastleUtil.addUserCivil(uid, bt2.getCivil(), bt2.getPeople());	// 更新玩家（所有城堡）的文明度增长，同时更新人口
		} else {
			bt1 = ResNeed.getBuildingT(type, grade - 1);
			
			addValue -= bt1.getValue();
			userResBean.addCivil(bt2.getCivil() - bt1.getCivil());
			CastleUtil.addUserCivil(uid, bt2.getCivil() - bt1.getCivil(), bt2.getPeople());
		}
		
		long now = System.currentTimeMillis();
		userResBean.reCalc(now);
		
		userResBean.setPeople(userResBean.getPeople() + bt2.getPeople());		// 建筑完成后增加人口

		userResBean.updateBuilding(type, grade);
		
		synchronized(CacheManage.castleUserRes) {
			switch(bean.getBuildType()) {
				case ResNeed.CITY_BUILD:
					break;
				case ResNeed.WOOD_BUILD:
					if(grade == 1) addValue -= ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setWoodSpeed(userResBean.getWoodSpeed() + addValue);
					break;
				case ResNeed.FE_BUILD:
					if(grade == 1) addValue -= ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setFeSpeed(userResBean.getFeSpeed() + addValue);
					break;
				case ResNeed.GRAIN_BUILD:
					if(grade == 1) addValue -= ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setGrainSpeed(userResBean.getGrainSpeed() + addValue);
					break;
				case ResNeed.STONE_BUILD:
					if(grade == 1) addValue -= ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setStoneSpeed(userResBean.getStoneSpeed() + addValue);
					break;
				case ResNeed.WOOD_FACTORY_BUILD:
					userResBean.setOtherWoodSpeed(userResBean.getOtherWoodSpeed() + addValue);
					break;
				case ResNeed.STONE_FACTORY_BUILD:
					userResBean.setOtherStoneSpeed(userResBean.getOtherStoneSpeed() + addValue);
					break;
				case ResNeed.FOUNDRY_BUILD:
					userResBean.setOtherFeSpeed(userResBean.getOtherFeSpeed() + addValue);
					break;
				case ResNeed.MOFANG_BUILD:
				case ResNeed.BREAD_BUILD:
					userResBean.setOtherGrainSpeed(userResBean.getOtherGrainSpeed() + addValue);
					break;
				case ResNeed.STORAGE_BUILD:
				case ResNeed.STORAGE2_BUILD:
					if(userResBean.getMaxRes() == 800) {	// 初始状态，升级后变成1200，如果是第二个仓库，直接+1200
						userResBean.setMaxRes(addValue);
					} else {
						userResBean.setMaxRes(userResBean.getMaxRes() + addValue);
					}
					break;
				case ResNeed.BARN_BUILD:
				case ResNeed.BARN2_BUILD:
					if(userResBean.getMaxGrain() == 800) {
						userResBean.setMaxGrain(addValue);
					} else {
						userResBean.setMaxGrain(userResBean.getMaxGrain() + addValue);
					}
					break;
				case ResNeed.CAVE_BUILD:
					userResBean.setCave(userResBean.getCave() + addValue);
					break;
				case ResNeed.WALL_BUILD:
				case ResNeed.WALL2_BUILD:
				case ResNeed.WALL3_BUILD:
					userResBean.setWall(bt2.getValue());
					break;
				case ResNeed.PALACE_BUILD:
				case ResNeed.PALACE2_BUILD:
					userResBean.reCalcLoyal(now);
					if(userResBean.getLoyal() != UserResBean.MAX_LOYAL) {	// 如果本身已经满了，不论如何升级建筑，还是满的，所以不用保存
						userResBean.setLoyalSpeed(grade * 10000);
						castleService.updateLoyal(userResBean);
					}						
					break;
				case ResNeed.WONDER_BUILD:
					String soldier = ResNeed.natarAttacks[grade];
					if(soldier != null) {	// 纳塔的进攻
						CastleUserBean natar = CastleUtil.getNatarUser();
						AttackThreadBean att = new AttackThreadBean();		
						att.setCid(natar.getMain());
						att.setFromCid(natar.getMain());
						att.setToCid(cid);
						att.setStartTime(now);
						att.setEndTime(now + DateUtil.MS_IN_DAY / 2);
						att.setType(0);
						att.setSoldierCount(soldier);
						cacheService.addCacheAttack(att);

						att.setOpt(38);
						att.setSoldierCount(ResNeed.natarAttacks2[grade]);
						att.setEndTime(now + DateUtil.MS_IN_DAY / 2 + 60000);	// 延迟1分钟的投石部队
						cacheService.addCacheAttack(att);
					}
					SqlUtil.executeUpdate("update castle_ww set lvl=" + grade + " where cid=" + cid, 5);
					if(grade == 100) {	// 奇迹建造完成+
						// TODO 如果有两个奇迹同时完成怎么办
						List list = castleService.getWWList("1");
						DbOperation db = new DbOperation(5);
						try {
	//						 让奇迹城的所有建造停止
							for(int i = 0;i < list.size();i++) {
								WWBean ww = (WWBean)list.get(i);
								CastleBean castle = CastleUtil.getCastleById(ww.getCid());
								
								db.executeUpdate("delete from cache_building where cid=" + ww.getCid() + " and type=" + ResNeed.WONDER_BUILD);
							}
	//						 删除所有图纸，扔给纳塔主城
							int natar = CastleUtil.getNatarUser().getMain();
							int natarId = CastleUtil.getNatarUser().getUid();
							
							List list2 = CastleUtil.getArtList();
							for(int i = 0;i < list2.size();i++) {
								ArtBean art = (ArtBean)list2.get(i);
								if(art.getType() != 1) continue;
								CastleUtil.artMap.remove(new Integer(art.getCid()));
								art.setCid(natar);
								art.setStatus(0);
								art.setUid(natarId);
								db.executeUpdate("update castle_art set cid=" + natar + ",status=0 where id=" + art.getId());
							}
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							db.release();
						}
					}
					break;
				default:
					break;
			}
			castleService.updateUserResAll(userResBean);
		}
			
		cacheService.deleteCacheBuilding(id);

		
		//加入建筑完毕消息
		StringBuilder content = new StringBuilder();// + "建筑成功";
		content.append(bt.getName());
		content.append("等级");
		content.append(grade);
		content.append("完成建造");
		CastleMessage castleMessage = new CastleMessage(content.toString(), uid);
		castleService.addCastleMessage(castleMessage);
		
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getState(){
		if(grade <= 1){
			return "正在建造中...";
		} else {
			return "正在升级中...";
		}
	}
	
	
	public String getTimeLeft(){
		
		long now = System.currentTimeMillis();
		
		
		return DateUtil.formatTimeInterval2((int) ((endTime - now) / 1000));
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
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

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public int getBuildPos() {
		return buildPos;
	}

	public void setBuildPos(int buildPos) {
		this.buildPos = buildPos;
	}


	
}
