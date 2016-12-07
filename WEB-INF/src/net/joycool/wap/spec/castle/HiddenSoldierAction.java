package net.joycool.wap.spec.castle;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HiddenSoldierAction extends CastleBaseAction {

	public HiddenSoldierAction() {
		super();
	}

	public HiddenSoldierAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public HiddenSoldierAction(HttpServletRequest request) {
		super(request);
	}

	//藏兵洞取兵
	public boolean catchSoldier(){
		int[] countSoldier = new int[ResNeed.soldierTypeCount + 1];
		int sum = 0;
		for(int i = 1;i < countSoldier.length;i++) {
			countSoldier[i] = getParameterInt("count" + i);
			if(countSoldier[i] < 0) {
				request.setAttribute("msg", "士兵数不能小于0");
				return false;
			}
			sum += countSoldier[i];
		}	
		if(sum == 0)
			return true;
		
		//更新藏兵洞士兵的数量
		CastleArmyBean hiddenArmy = CastleUtil.getCastleHiddenArmy(castle.getId());
		if(!hiddenArmy.decrease(countSoldier)) {
			request.setAttribute("msg", "藏兵洞士兵数量不够");
			return false;
		}
		castleService.updateHiddenSoldierCount(hiddenArmy);
		
		//更新士兵的数量
		CastleArmyBean army = CastleUtil.getCastleArmy(castle.getId());
		for(int i = 1;i < countSoldier.length;i++) {
			countSoldier[i] = -countSoldier[i];
		}	
		army.decrease(countSoldier);
		castleService.updateSoldierCount(army);
		
		return true;
	}
	
	//藏兵洞藏兵
	public boolean hiddenSoldier() {
		
		int[] countSoldier = new int[ResNeed.soldierTypeCount + 1];
		int sum = 0;
		for(int i = 1;i < 7;i++) {	// 最后四种兵无法藏
			countSoldier[i] = getParameterInt("count" + i);
			if(countSoldier[i] < 0) {
				request.setAttribute("msg", "士兵数不能小于0");
				return false;
			}
			sum += countSoldier[i];
		}		
		if(sum == 0)
			return true;
		int grade = userResBean.getBuildingGrade(ResNeed.HIDDEN_BUILD);
		int count = castleService.getHiddenSoldierCount(castle.getId());
		
		if(ResNeed.getHiddenCount(grade) < sum + count) {
			request.setAttribute("msg", "藏兵洞容量不足");
			return false;
		}
		
		CastleArmyBean army = CastleUtil.getCastleArmy(castle.getId());
		
		//判断出兵是否藏兵，如果够就直接扣除
		if(!army.decrease(countSoldier)) {
			request.setAttribute("msg", "士兵的数量不够");
			return false;
		}
		castleService.updateSoldierCount(army);
		
		CastleArmyBean hiddenArmy = CastleUtil.getCastleHiddenArmy(castle.getId());
		castleService.updateHiddenSoldierCount(hiddenArmy.getId(), countSoldier);
		
		return true;
	}
	
}
