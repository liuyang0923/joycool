package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.pk.PKActBean;
import net.joycool.wap.bean.pk.PKEquipBean;
import net.joycool.wap.bean.pk.PKMObjBean;
import net.joycool.wap.bean.pk.PKMedicineBean;
import net.joycool.wap.bean.pk.PKMissionBean;
import net.joycool.wap.bean.pk.PKMonsterBean;
import net.joycool.wap.bean.pk.PKMonsterSkillBean;
import net.joycool.wap.bean.pk.PKNpcBean;
import net.joycool.wap.bean.pk.PKSceneBean;
import net.joycool.wap.bean.pk.PKUserBSkillBean;
import net.joycool.wap.bean.pk.PKUserBagBean;
import net.joycool.wap.bean.pk.PKUserBean;
import net.joycool.wap.bean.pk.PKUserHSkillBean;
import net.joycool.wap.bean.pk.PKUserSkillBean;

/**
 * @author macq
 * @datetime 2007-1-29 下午04:06:47
 * @explain
 */
public interface IPKService {
	public PKMonsterBean getPKMonster(String condition);
	public Vector getPKMonsterList(String condition);
	public boolean addPKMonster(PKMonsterBean bean);
	public boolean delPKMonster(String condition);
	public boolean updatePKMonster(String set, String condition);
	public int getPKMonsterCount(String condition);
	
	public PKMonsterSkillBean getPKMonsterSkill(String condition);
	public Vector getPKMonsterSkillList(String condition);
	public boolean addPKMonsterSkill(PKMonsterSkillBean bean);
	public boolean delPKMonsterSkill(String condition);
	public boolean updatePKMonsterSkill(String set, String condition);
	public int getPKMonsterSkillCount(String condition);
	
	public PKUserBean getPKUser(String condition);
	public Vector getPKUserList(String condition);
	public boolean addPKUser(PKUserBean bean);
	public boolean delPKUser(String condition);
	public boolean updatePKUser(String set, String condition);
	public int getPKUserCount(String condition);
	
	public PKUserSkillBean getPKUserSkill(String condition);
	public Vector getPKUserSkillList(String condition);
	public boolean addPKUserSkill(PKUserSkillBean bean);
	public boolean delPKUserSkill(String condition);
	public boolean updatePKUserSkill(String set, String condition);
	public int getPKUserSkillCount(String condition);
	
	public PKActBean getPKAct(String condition);
	public Vector getPKActList(String condition);
	public boolean addPKScene(PKSceneBean bean);
	public boolean delPKScene(String condition);
	public boolean updatePKScene(String set, String condition);
	public int getPKSceneCount(String condition);
	
	public PKUserHSkillBean getPKUserHSkill(String condition);
	public Vector getPKUserHSkillList(String condition);
	public boolean addPKUserHSkill(PKUserHSkillBean bean);
	public boolean delPKUserHSkill(String condition);
	public boolean updatePKUserHSkill(String set, String condition);
	public int getPKUserHSkillCount(String condition);
	
	public PKNpcBean getPKNpc(String condition);
	public Vector getPKNpcList(String condition);
	public boolean addPKNpc(PKNpcBean bean);
	public boolean delPKNpc(String condition);
	public boolean updatePKNpc(String set, String condition);
	public int getPKNpcCount(String condition);
	
	public PKUserBagBean getPKUserBag(String condition);
	public Vector getPKUserBagList(String condition);
	public boolean addPKUserBag(PKUserBagBean bean);
	public boolean delPKUserBag(String condition);
	public boolean updatePKUserBag(String set, String condition);
	public int getPKUserBagCount(String condition);
	
	public PKEquipBean getPKEuqip(String condition);
	public Vector getPKEuqipList(String condition);
	public boolean addPKEuqip(PKEquipBean bean);
	public boolean delPKEuqip(String condition);
	public boolean updatePKEuqip(String set, String condition);
	public int getPKEuqipCount(String condition);
	
	public PKMedicineBean getPKMedicine(String condition);
	public Vector getPKMedicineList(String condition);
	public boolean addPKMedicine(PKMedicineBean bean);
	public boolean delPKMedicine(String condition);
	public boolean updatePKMedicine(String set, String condition);
	public int getPKMedicineCount(String condition);
	
	public PKUserBSkillBean getPKUserBSkill(String condition);
	public Vector getPKUserBSkillList(String condition);
	public boolean addPKUserBSkill(PKUserBSkillBean bean);
	public boolean delPKUserBSkill(String condition);
	public boolean updatePKUserBSkill(String set, String condition);
	public int getPKUserBSkillCount(String condition);
	
	public PKMissionBean getPKMission(String condition);
	public Vector getPKMissionList(String condition);
	public boolean addPKMission(PKMissionBean bean);
	public boolean delPKMission(String condition);
	public boolean updatePKMission(String set, String condition);
	public int getPKMissionCount(String condition);
	
	public PKMObjBean getPKMObj(String condition);
	public Vector getPKMObjList(String condition);
	public boolean addPKMObj(PKMObjBean bean);
	public boolean delPKMObj(String condition);
	public boolean updatePKMObj(String set, String condition);
	public int getPKMObjCount(String condition);
}
