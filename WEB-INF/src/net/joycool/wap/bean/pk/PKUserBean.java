package net.joycool.wap.bean.pk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.joycool.wap.action.pk.PKAction;
import net.joycool.wap.action.pk.PKWorld;

/**
 * @author macq
 * @datetime 2007-1-30 上午09:56:46
 * @explain
 */
public class PKUserBean extends PKRoleBean {
	int id;

	int userId;

	// 经验
	int experience;

	// 战斗经验等级
	int rank;

	int basePhysical;

	int baseEnergy;

	int baseAggressivity;

	int baseRecovery;

	int baseFlying;

	int baseLuck;

	// 当前体力
	int currentPhysical;

	// 默认+装备+被动技能得到的体力
	int physical;

	// 当前气力
	int currentEnergy;

	// 默认+装备+被动技能得到的气力
	int energy;

	// 默认+装备+被动技能得到的攻击力
	int aggressivity;

	// 攻击力增长随机数
	int aggressivityRnd;

	// 默认+装备+被动技能得到的防御力
	int recovery;

	// 防御力增长随机数
	int recoveryRnd;

	// 默认+装备+被动技能得到的轻功
	int flying;

	// 默认+装备+被动技能得到的吉运
	int luck;

	// 用户所有技能
	List userSkillList = new ArrayList();

	// 所在场景Id
	int sceneId;

	// 用户行囊
	int bag;

	// 用户行囊明细
	Vector userBagList = new Vector();

	// 正在进行的任务
	String missionStart;

	// 已经完成的任务
	String missionEnd;

	Set mStartSet = new HashSet();

	Set mEndSet = new HashSet();

	// 今天杀人数量
	int kCount;

	// 昨天杀人数量
	int oldKCount;

	/**
	 * @return 返回 bag。
	 */
	public int getBag() {
		return bag;
	}

	/**
	 * @param bag
	 *            要设置的 bag。
	 */
	public void setBag(int bag) {
		this.bag = bag;
	}

	/**
	 * @return 返回 baseAggressivity。
	 */
	public int getBaseAggressivity() {
		return baseAggressivity;
	}

	/**
	 * @param baseAggressivity
	 *            要设置的 baseAggressivity。
	 */
	public void setBaseAggressivity(int baseAggressivity) {
		this.baseAggressivity = baseAggressivity;
	}

	/**
	 * @return 返回 baseEnergy。
	 */
	public int getBaseEnergy() {
		return baseEnergy;
	}

	/**
	 * @param baseEnergy
	 *            要设置的 baseEnergy。
	 */
	public void setBaseEnergy(int baseEnergy) {
		this.baseEnergy = baseEnergy;
	}

	/**
	 * @return 返回 baseFlying。
	 */
	public int getBaseFlying() {
		return baseFlying;
	}

	/**
	 * @param baseFlying
	 *            要设置的 baseFlying。
	 */
	public void setBaseFlying(int baseFlying) {
		this.baseFlying = baseFlying;
	}

	/**
	 * @return 返回 baseLuck。
	 */
	public int getBaseLuck() {
		return baseLuck;
	}

	/**
	 * @param baseLuck
	 *            要设置的 baseLuck。
	 */
	public void setBaseLuck(int baseLuck) {
		this.baseLuck = baseLuck;
	}

	/**
	 * @return 返回 basePhysical。
	 */
	public int getBasePhysical() {
		return basePhysical;
	}

	/**
	 * @param basePhysical
	 *            要设置的 basePhysical。
	 */
	public void setBasePhysical(int basePhysical) {
		this.basePhysical = basePhysical;
	}

	/**
	 * @return 返回 baseRecovery。
	 */
	public int getBaseRecovery() {
		return baseRecovery;
	}

	/**
	 * @param baseRecovery
	 *            要设置的 baseRecovery。
	 */
	public void setBaseRecovery(int baseRecovery) {
		this.baseRecovery = baseRecovery;
	}

	/**
	 * @return 返回 currentEnergy。
	 */
	public int getCurrentEnergy() {
		return currentEnergy;
	}

	/**
	 * @param currentEnergy
	 *            要设置的 currentEnergy。
	 */
	public void setCurrentEnergy(int currentEnergy) {
		this.currentEnergy = currentEnergy;
	}

	/**
	 * @return 返回 currentPhysical。
	 */
	public int getCurrentPhysical() {
		return currentPhysical;
	}

	/**
	 * @param currentPhysical
	 *            要设置的 currentPhysical。
	 */
	public void setCurrentPhysical(int currentPhysical) {
		this.currentPhysical = currentPhysical;
	}

	/**
	 * @return 返回 experience。
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * @param experience
	 *            要设置的 experience。
	 */
	public void setExperience(int experience) {
		this.experience = experience;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 sceneId。
	 */
	public int getSceneId() {
		return sceneId;
	}

	/**
	 * @param sceneId
	 *            要设置的 sceneId。
	 */
	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	/**
	 * @return 返回 userBagList。
	 */
	public Vector getUserBagList() {
		return userBagList;
	}

	/**
	 * @param userBagList
	 *            要设置的 userBagList。
	 */
	public void setUserBagList(Vector userBagList) {
		this.userBagList = userBagList;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 userSkillList。
	 */
	public List getUserSkillList() {
		return userSkillList;
	}

	/**
	 * @param userSkillList
	 *            要设置的 userSkillList。
	 */
	public void setUserSkillList(List userSkillList) {
		this.userSkillList = userSkillList;
	}

	/**
	 * @return 返回 aggressivity。
	 */
	public int getAggressivity() {
		return aggressivity;
	}

	/**
	 * @param aggressivity
	 *            要设置的 aggressivity。
	 */
	public void setAggressivity(int aggressivity) {
		this.aggressivity = aggressivity;
	}

	/**
	 * @return 返回 aggressivityRnd。
	 */
	public int getAggressivityRnd() {
		return aggressivityRnd;
	}

	/**
	 * @param aggressivityRnd
	 *            要设置的 aggressivityRnd。
	 */
	public void setAggressivityRnd(int aggressivityRnd) {
		this.aggressivityRnd = aggressivityRnd;
	}

	/**
	 * @return 返回 energy。
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy
	 *            要设置的 energy。
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	/**
	 * @return 返回 flying。
	 */
	public int getFlying() {
		return flying;
	}

	/**
	 * @param flying
	 *            要设置的 flying。
	 */
	public void setFlying(int flying) {
		this.flying = flying;
	}

	/**
	 * @return 返回 luck。
	 */
	public int getLuck() {
		return luck;
	}

	/**
	 * @param luck
	 *            要设置的 luck。
	 */
	public void setLuck(int luck) {
		this.luck = luck;
	}

	/**
	 * @return 返回 physical。
	 */
	public int getPhysical() {
		return physical;
	}

	/**
	 * @param physical
	 *            要设置的 physical。
	 */
	public void setPhysical(int physical) {
		this.physical = physical;
	}

	/**
	 * @return 返回 recovery。
	 */
	public int getRecovery() {
		return recovery;
	}

	/**
	 * @param recovery
	 *            要设置的 recovery。
	 */
	public void setRecovery(int recovery) {
		this.recovery = recovery;
	}

	/**
	 * @return 返回 recoveryRnd。
	 */
	public int getRecoveryRnd() {
		return recoveryRnd;
	}

	/**
	 * @param recoveryRnd
	 *            要设置的 recoveryRnd。
	 */
	public void setRecoveryRnd(int recoveryRnd) {
		this.recoveryRnd = recoveryRnd;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 计算用户各项属性
	 * @datetime:2007-3-13 15:55:49
	 * @return void
	 */
	public void countPkUser() {
		// 默认体力
		physical = basePhysical;
		// 默认气力
		energy = baseEnergy;
		// 默认攻击力
		aggressivity = baseAggressivity;
		// 默认攻击力增长随机数为零
		aggressivityRnd = 0;
		// 默认防御力
		recovery = baseRecovery;
		// 默认防御力增长随机数为零
		recoveryRnd = 0;
		// 默认轻功
		flying = baseFlying;
		// 默认吉运
		luck = baseFlying;
		// 获取用户行囊所有物品
		PKUserBagBean userBag = null;
		for (int i = 0; i < userBagList.size(); i++) {
			userBag = (PKUserBagBean) userBagList.get(i);
			// 获取用户身上装备
			if (userBag.getSiteId() > 0
					&& userBag.getType() == PKAction.PK_EQUIP) {
				PKEquipBean equip = (PKEquipBean) userBag.getPorto();
				physical += equip.getPhysicalGrowthRadix();
				energy += equip.getEnergyGrowthRadix();
				aggressivity += equip.getAggressGrowthRadix();
				aggressivityRnd += equip.getAggressMaxRadix();
				recovery += equip.getRecoveryGrowthRadix();
				recoveryRnd += equip.getRecoveryMaxRadix();
				flying += equip.getFlyingGrowthRadix();
				luck += equip.getLuckGrowthRadix();
			}
		}
		// 获取用户所有技能
		PKUserHSkillBean userSkill = null;
		for (int i = 0; i < userSkillList.size(); i++) {
			userSkill = (PKUserHSkillBean) userSkillList.get(i);
			// 获取所有被动技能
			if (userSkill.getSkillType() == 0) {
				PKUserBSkillBean userBSkill = (PKUserBSkillBean) PKWorld
						.getUserBSkill().get(
								new Integer(userSkill.getSkillId()));
				int rank = userSkill.getRank() - 1;
				physical += userBSkill.getPhysicalGrowthRadix()[rank];
				energy += userBSkill.getEnergyGrowthRadix()[rank];
				aggressivity += userBSkill.getAggressGrowthRadix()[rank];
				aggressivityRnd += userBSkill.getAggressMaxRadix()[rank];
				recovery += userBSkill.getRecoveryGrowthRadix()[rank];
				recoveryRnd += userBSkill.getRecoveryMaxRadix()[rank];
				flying += userBSkill.getFlyingGrowthRadix()[rank];
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：展示用户信息
	 * @datetime:2007-3-14 9:35:14
	 * @return
	 * @return String
	 */
	public String toDetail() {
		StringBuffer sb = new StringBuffer();
		sb.append("体力:");
		sb.append(currentPhysical);
		sb.append("/");
		sb.append(physical);
		sb.append("<br/>");
		sb.append("气力:");
		sb.append(currentEnergy);
		sb.append("/");
		sb.append(energy);
		sb.append("<br/>");
		sb.append("战斗经验:");
		sb.append(experience);
		sb.append("<br/>");
		sb.append("攻击力:");
		sb.append(aggressivity);
		sb.append("<br/>");
		sb.append("防御力:");
		sb.append(recovery);
		sb.append("<br/>");
		sb.append("轻功:");
		sb.append(flying);
		sb.append("<br/>");
		sb.append("吉运:");
		sb.append(luck);
		sb.append("<br/>");
		return sb.toString();
	}

	/**
	 * 
	 * @author macq
	 * @explain： 判断复生方式
	 * @datetime:2007-3-15 11:44:17
	 * @return
	 * @return boolean
	 */
	public boolean userRevival() {
		// 需要乐币复活
		if (System.currentTimeMillis() - deathTime < PKAction.USER_REVIVE_INTERVAL) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 判断用户是否下线
	 * @datetime:2007-3-17 11:22:33
	 * @return
	 * @return boolean
	 */
	public boolean userOffline() {
		// 需要乐币复活
		if (System.currentTimeMillis() - actionTime > PKAction.PK_USER_INACTIVE) {
			return true;
		}
		return false;
	}

	/**
	 * @return Returns the missionEnd.
	 */
	public String getMissionEnd() {
		return missionEnd;
	}

	/**
	 * @param missionEnd
	 *            The missionEnd to set.
	 */
	public void setMissionEnd(String missionEnd) {
		this.missionEnd = missionEnd;
	}

	/**
	 * @return Returns the missionStart.
	 */
	public String getMissionStart() {
		return missionStart;
	}

	/**
	 * @param missionStart
	 *            The missionStart to set.
	 */
	public void setMissionStart(String missionStart) {
		this.missionStart = missionStart;
	}

	/**
	 * @return Returns the mEndSet.
	 */
	public Set getMEndSet() {
		return mEndSet;
	}

	/**
	 * @param endSet
	 *            The mEndSet to set.
	 */
	public void setMEndSet(Set endSet) {
		mEndSet = endSet;
	}

	/**
	 * @return Returns the mStartSet.
	 */
	public Set getMStartSet() {
		return mStartSet;
	}

	/**
	 * @param startSet
	 *            The mStartSet to set.
	 */
	public void setMStartSet(Set startSet) {
		mStartSet = startSet;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getKCount() {
		return kCount;
	}

	public void setKCount(int count) {
		kCount = count;
	}

	public int getOldKCount() {
		return oldKCount;
	}

	public void setOldKCount(int oldKCount) {
		this.oldKCount = oldKCount;
	}
}
