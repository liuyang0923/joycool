package net.joycool.wap.spec.castle;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class SoldierAction extends CastleBaseAction {

	public SoldierAction() {
		super();
	}

	public SoldierAction(HttpServletRequest request) {
		super(request);
	}

	public SoldierAction(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	static CacheService cacheService = CacheService.getInstance();
	/**
	 * 生产士兵
	 * @return
	 */
	public boolean produceSoldier() {
		int type = getParameterInt("type");
		int buildingType = getParameterInt("bt");
		SoldierResBean res = ResNeed.getSoldierRes(castleUser.getRace(), type);
		if(res == null || getSmithys()[type] == null && !res.isFlagResearched()) {
			request.setAttribute("msg", "兵种还没有研发");
			return false;
		}
		int buildingGrade = userResBean.getBuildingGrade(buildingType);
		if(buildingGrade == 0 || ResNeed.canTrainTypes[buildingType] != res.getBuildType()) {
			request.setAttribute("msg", "建筑还未建造");
			return false;
		}
		
		int count = getParameterInt("count");
		if(count <= 0 || count > 10000) {
			request.setAttribute("msg", "数量输入错误");
			return false;
		}
		float factor = ResNeed.getSoldierSpeedFactor(buildingGrade);
		
		int add = ResNeed.trainCosts[buildingType];
		int woodNeed = res.getWood() * count * add;
		int feNeed = res.getFe() * count * add;
		int grainNeed = res.getGrain() * count * add;
		int stoneNeed = res.getStone() * count * add;
		
		int time = (int) (res.getTime() * factor);
		int art = CastleUtil.getActiveArtType(castle);
		if(art == 6)	// 拥有神器，训练时间减少一半
			time = time / 2;
		else if(art == 16)
			time = time * 2 / 3;
		if(SqlUtil.isTest)	time = 10;
		long now = System.currentTimeMillis();
		int people = count * res.getPeople();	// 粮食消耗
		if(castle.getRace() == 5)	// 纳塔的士兵不消耗粮食
			people = 0;	
		
		if(userResBean.getGrainRealSpeed() < people) {
			request.setAttribute("msg", "粮食产量不足,需要先建造一个粮田.");
			return false;
		}
		synchronized(userResBean) {
			if(res.getBuildType() == 20) {		// 行宫或者皇宫
				int canBuild = CastleUtil.calcTrainCount(castle, type, buildingType, buildingGrade);
				if(count > canBuild) {
					request.setAttribute("msg", "无法训练,当前最多能训练的数量是:" + canBuild);
					return false;
				}
			}
			if(userResBean.getWood(now) < woodNeed 
					|| userResBean.getFe(now) < feNeed
					|| userResBean.getGrain(now) < grainNeed
					|| userResBean.getStone(now) < stoneNeed) {
				request.setAttribute("msg", "资源不够");
				return false;
			}
	
			SoldierThreadBean bean = new SoldierThreadBean(castle.getId(), type, count, time);
			bean.setType(buildingType);
			// 如果之前有兵正在训练，把时间加到之后
			List cacheList = cacheService.getCacheSoldierByCid(castle.getId(), buildingType);
			if(cacheList.size() > 0) {
				SoldierThreadBean cache = (SoldierThreadBean)cacheList.get(cacheList.size() - 1);
				bean.setEndTime(cache.getAllEndTime() + time * 1000);
			}
	
			cacheService.addCacheSoldier(bean);
			
			CastleUtil.decreaseUserRes(userResBean, woodNeed, feNeed, grainNeed, 
					stoneNeed, people);
		}
		return true;
	}
	
	public boolean produceTrap() {
		int type = 101;	// 陷阱
		int buildingType = 34;	// 陷阱机

		int buildingGrade = userResBean.getBuildingGrade(ResNeed.TRAP_BUILD);
		if(buildingGrade == 0 || buildingGrade * 10 <= userResBean.getTrap()) {
			request.setAttribute("msg", "无法建造");
			return false;
		}
		
		int count = getParameterInt("count");
		if(count <= 0 || count > 100) {
			request.setAttribute("msg", "数量输入错误");
			return false;
		}
		float factor = ResNeed.getSoldierSpeedFactor(buildingGrade);
		
		int time = (int) (300 * factor);
		if(SqlUtil.isTest)	time = 10;
		long now = System.currentTimeMillis();
		
		int sum = 0;
		synchronized(userResBean) {
			List cacheList = cacheService.getCacheSoldierByCid(castle.getId(), buildingType);
			for(int i = 0; i < cacheList.size(); i++) {
				SoldierThreadBean sol = (SoldierThreadBean)cacheList.get(i);
				sum += sol.getCount();
			}
			int max = buildingGrade * 10 - sum - userResBean.getTrap();
			if(count > max)
				count = max;
			if(count <= 0) {
				request.setAttribute("msg", "无法建造更多陷阱");
				return false;
			}
			
			int woodNeed = 20 * count;
			int stoneNeed = 30 * count;
			int feNeed = 10 * count;
			int grainNeed = 20 * count;
			
			if(userResBean.getWood(now) < woodNeed 
					|| userResBean.getFe(now) < feNeed
					|| userResBean.getGrain(now) < grainNeed
					|| userResBean.getStone(now) < stoneNeed) {
				request.setAttribute("msg", "资源不够");
				return false;
			}
	
			SoldierThreadBean bean = new SoldierThreadBean(castle.getId(), type, count, time);
			bean.setType(buildingType);
			// 如果之前有陷阱在制造，加到之后
			
			if(cacheList.size() > 0) {
				SoldierThreadBean cache = (SoldierThreadBean)cacheList.get(cacheList.size() - 1);
				bean.setEndTime(cache.getAllEndTime() + time * 1000);
			}
	
			cacheService.addCacheSoldier(bean);
		
			CastleUtil.decreaseUserRes(userResBean, woodNeed, feNeed, grainNeed, 
					stoneNeed);
		}
		
		return true;
	}
	
	public void hero() {
		if(userResBean.getBuildingGrade(ResNeed.HERO_BUILD) == 0) {
			tip("tip", "建筑还没有建造完成");
			return;
		}
		HeroBean hero = castleUser.getHero();
		int act = getParameterInt("a");
		if(act == 0) {
			
			if(hero == null || !hero.isAlive())
				return;
			int st = getParameterInt("st");	// 增加属性点
			if(st == 6 && hero.getRank() != 0)
				return;
			if(st != 6 && hero.getFreePoint() <= 0)
				return;
			boolean changed = false;
			switch(st) {
			case 1:
				if(hero.stat1 >= 100)
					return;
				hero.stat1++;
				changed = true;
				break;
			case 2:
				if(hero.stat2 >= 100)
					return;
				hero.stat2++;
				changed = true;
				break;
			case 3:
				if(hero.stat3 >= 100)
					return;
				hero.stat3++;
				changed = true;
				break;
			case 4:
				if(hero.stat4 >= 100)
					return;
				hero.stat4++;
				changed = true;
				break;
			case 5:
				if(hero.stat5 >= 100)
					return;
				hero.reCalc(System.currentTimeMillis());
				hero.stat5++;
				changed = true;
				break;
			case 6:		// 等级0的时候重置
				hero.reCalc(System.currentTimeMillis());
				hero.stat1 = hero.stat2 = hero.stat3 = hero.stat4 = hero.stat5 = 0;
				changed = true;
				break;
			}
			if(changed) {
				castleService.updateHero(hero);
			}
			
		} else if(act == 1){	// 训练
			int type = getParameterInt("t");	// 训练的兵种
			if(hero != null || type <= 0 || type > 5) {
				tip("tip", "无法开始训练");
				return;
			}

			synchronized(castleUser) {
				
				CastleArmyBean army = CastleUtil.getCastleArmy(castle.getId());
				
				int left = army.getCount(type);
				if(left == 0) {
					request.setAttribute("msg", "无法开始训练");
					return;
				}
				
				if(cacheService.containCommon2(castleUser.getUid(), 5)) {
					tip("tip", "无法开始训练");
					return;
				}
				int[] res = HeroBean.getReviveRes(ResNeed.getSoldierRes(castle.getRace(), type), 0);
				if(!userResBean.decreaseRes(res[0], res[1], res[2], res[3])) {
					tip("tip", "资源不足");
					return;
				}
				army.addCount(type, -1);
				castleService.updateSoldierCount(army);
				
				int people = ResNeed.getSoldierRes(castle.getRace(), type).getPeople();
				userResBean.addPeople2Calc(-people, System.currentTimeMillis());
				
				hero = new HeroBean();
				hero.setUid(castleUser.getUid());
				hero.setRace(castleUser.getRace());
				hero.setType(type);
				hero.setRace(castleUser.getRace());
				hero.setName(castleUser.getName());
				hero.setStatus(3);
				castleService.addHero(hero);
				
				CommonThreadBean commonThreadBean = new CommonThreadBean(userBean.getId(), castle.getId(), 5, res[4] / 2, hero.getId());
				cacheService.addCacheCommon(commonThreadBean);
			}
			tip("tip", "你的指挥官开始训练了");
		} else if(act == 2){	// 复活
			int id = getParameterInt("id");	// 复活指定id的英雄
			if(hero != null || id <= 0) {
				tip("tip", "无法开始训练");
				return;
			}
			synchronized(castleUser) {
				if(cacheService.containCommon2(castleUser.getUid(), 5)) {
					tip("tip", "无法开始训练");
					return;
				} 
				hero = castleService.getHero("id=" + id);
				if(hero == null || hero.getUid() != castleUser.getUid()) {
					tip("tip", "无法开始训练");
					return;
				}
				int[] res = hero.getReviveRes();
				if(!userResBean.decreaseRes(res[0], res[1], res[2], res[3])) {
					tip("tip", "资源不足");
					return;
				}
				CommonThreadBean commonThreadBean = new CommonThreadBean(userBean.getId(), castle.getId(), 5, res[4], id);
				cacheService.addCacheCommon(commonThreadBean);
			}
			tip("tip", "你的指挥官开始重新训练了");
		} else if(act == 3){	// 删除
			int id = getParameterInt("id");
			if(hero != null || id <= 0) {
				tip("tip", "无法解散");
				return;
			}
			synchronized(castleUser) {
				if(cacheService.containCommon2(castleUser.getUid(), 5)) {
					tip("tip", "无法解散");
					return;
				}
				hero = castleService.getHero("id=" + id);
				if(hero == null || hero.getUid() != castleUser.getUid()) {
					tip("tip", "无法解散");
					return;
				}
				SqlUtil.executeUpdate("delete from castle_hero where id=" + hero.getId(), 5);
				castleUser.deleteHero();
				tip("tip", "你的指挥官已经被解散");
			}
		}
	}
}
