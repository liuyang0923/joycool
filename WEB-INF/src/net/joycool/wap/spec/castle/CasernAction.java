package net.joycool.wap.spec.castle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.SqlUtil;

public class CasernAction extends CastleBaseAction {
	CacheService cacheService = CacheService.getInstance();
	public CasernAction() {
		super();
	}

	public CasernAction(HttpServletRequest request) {
		super(request);
	}

	public CasernAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	String[] defenseUpMsg = {"攻击正在升级中", "防御正在升级中", "兵种正在研发中"};
	//攻防升级
	public boolean defenceUpgrade() {
		int type = getParameterInt("t");
		int stype = getParameterInt("s");
		if(type != 0 && type != 1)	type = 2;
		
		SmithyThreadBean sBean = new SmithyThreadBean();
		sBean.setCid(castle.getId());
		
		int grade = 0;
		if(type == 0) {
			grade = userResBean.getBuildingGrade(ResNeed.SMITHY_BUILD);
		} else if(type == 1) {
			grade = userResBean.getBuildingGrade(ResNeed.GUN_ROOM_BUILD);
		} else {
			grade = userResBean.getBuildingGrade(ResNeed.RESEARCH_BUILD);
		}
		
		if(grade == 0) {
			request.setAttribute("msg", "建筑还未建造完成.");
			return false;
		}

		SoldierSmithyBean ssBean = castleService.getSoldierSmithy(castle.getId(), stype);
		SoldierResBean soldier = ResNeed.getSoldierRes(castleUser.getRace(), stype);
		if(type == 2) {
			if(ssBean != null) {
				request.setAttribute("msg", "兵种已经研发");
				return false;
			}	
		} else {
			if(ssBean == null) {
				request.setAttribute("msg", "兵种还没有研发");
				return false;
			}
			if(soldier.isFlagNoUpgrade()) {
				request.setAttribute("msg", "无法升级");
				return false;
			}	
		}

		long now = System.currentTimeMillis();
		
		ResTBean res;
		if(type == 0) {		//升级攻击
			if(grade <= ssBean.getAttack()) {
				request.setAttribute("msg", "建筑等级不够");
				return false;
			}
			res = ResNeed.getAttackResource(castleUser.getRace(), stype, ssBean.getAttack() + 1);
			// 根据建筑等级不同，升级速度不同，但是研发速度相同
			res.setTime(ResNeed.getGradeTime(grade, res.getTime()));
		} else if(type == 1) {	//升级防御
			if(grade <= ssBean.getDefence()) {
				request.setAttribute("msg", "建筑等级不够");
				return false;
			}
			res = ResNeed.getDefenceResource(castleUser.getRace(), stype, ssBean.getDefence() + 1);
			res.setTime(ResNeed.getGradeTime(grade, res.getTime()));
		} else {
			if(!userResBean.canBuild(soldier.getPreList())) {
				request.setAttribute("msg", "没有达到研发条件");
				return false;
			}
			res = new ResTBean(soldier.getWood2(), soldier.getFe2(), 
					soldier.getGrain2(), soldier.getStone2(), soldier.getTime2());
		}
		synchronized(userResBean) {
			if(cacheService.containSmithy(castle.getId(), type)) {
				request.setAttribute("msg", defenseUpMsg[type]);
				return false;
			}
			
			if(userResBean.getWood(now) < res.getWood() 
					|| userResBean.getFe(now) < res.getFe()
					|| userResBean.getGrain(now) < res.getGrain()
					|| userResBean.getStone(now) < res.getStone()) {
				request.setAttribute("msg", "资源不足");
				return false;
			}
			

			sBean.setEndTime(now + res.getTime() * 1000);
			if(SqlUtil.isTest)	sBean.setEndTime(now + 5 * 1000);
			sBean.setSmithyType(type);
			sBean.setSoldierType(stype);
			sBean.setStartTime(now);
			
			cacheService.addCacheSmithy(sBean);
		}
		CastleUtil.decreaseUserRes(userResBean, res.getWood(), res.getFe(), res.getGrain(), res.getStone());
		
		return true;
	}
	
}
