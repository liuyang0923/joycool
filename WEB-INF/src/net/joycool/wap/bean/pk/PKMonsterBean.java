package net.joycool.wap.bean.pk;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.action.pk.PKAction;

/**
 * @author macq
 * @datetime 2007-1-30 上午09:59:19
 * @explain
 */
public class PKMonsterBean extends PKRoleBean {
	int id;

	int index;

	String name;

	int experience;

	int physical;
	int initPhysical;	// 初始生命

	int energy;

	int aggressivity;

	int recovery;

	int flying;

	int luck;

	int skillTypeId;

	String dropTypeId;

	String skillId;

	List dropTypeIdList = new ArrayList();

	int rate;

	// boolean isDeath;

	// String deathTime;

	/**
	 * @return skillId
	 */
	public String getSkillId() {
		return skillId;
	}

	/**
	 * @param skillId
	 *            要设置的 skillId
	 */
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}

	/**
	 * @return aggressivity
	 */
	public int getAggressivity() {
		return aggressivity;
	}

	/**
	 * @param aggressivity
	 *            要设置的 aggressivity
	 */
	public void setAggressivity(int aggressivity) {
		this.aggressivity = aggressivity;
	}

	/**
	 * @return Returns the dropTypeId.
	 */
	public String getDropTypeId() {
		return dropTypeId;
	}

	/**
	 * @param dropTypeId
	 *            The dropTypeId to set.
	 */
	public void setDropTypeId(String dropTypeId) {
		this.dropTypeId = dropTypeId;
	}

	/**
	 * @return energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy
	 *            要设置的 energy
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	/**
	 * @return experience
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * @param experience
	 *            要设置的 experience
	 */
	public void setExperience(int experience) {
		this.experience = experience;
	}

	/**
	 * @return flying
	 */
	public int getFlying() {
		return flying;
	}

	/**
	 * @param flying
	 *            要设置的 flying
	 */
	public void setFlying(int flying) {
		this.flying = flying;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return isDeath
	 */
	public boolean isDeath() {
		return isDeath;
	}

	/**
	 * @param isDeath
	 *            要设置的 isDeath
	 */
	public void setDeath(boolean isDeath) {
		this.isDeath = isDeath;
	}

	/**
	 * @return luck
	 */
	public int getLuck() {
		return luck;
	}

	/**
	 * @param luck
	 *            要设置的 luck
	 */
	public void setLuck(int luck) {
		this.luck = luck;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return physical
	 */
	public int getPhysical() {
		return physical;
	}

	/**
	 * @param physical
	 *            要设置的 physical
	 */
	public void setPhysical(int physical) {
		this.physical = physical;
	}

	/**
	 * @return recovery
	 */
	public int getRecovery() {
		return recovery;
	}

	/**
	 * @param recovery
	 *            要设置的 recovery
	 */
	public void setRecovery(int recovery) {
		this.recovery = recovery;
	}

	/**
	 * @return skillTypeId
	 */
	public int getSkillTypeId() {
		return skillTypeId;
	}

	/**
	 * @param skillTypeId
	 *            要设置的 skillTypeId
	 */
	public void setSkillTypeId(int skillTypeId) {
		this.skillTypeId = skillTypeId;
	}

	/**
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            要设置的 index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 怪物属性信息
	 * @datetime:2007-3-12 14:43:08
	 * @return
	 * @return String
	 */
	public String toDetail() {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/monster/" + id
				+ ".gif\" alt=\"怪物图片\"/>");
		sb.append("<br/>");
		sb.append("怪物名称:");
		sb.append(name);
		sb.append("<br/>");
		sb.append("当前体力:");
		sb.append(physical);
		sb.append("<br/>");
		sb.append("当前攻击力:");
		sb.append(aggressivity);
		sb.append("<br/>");
		sb.append("当前防御力:");
		sb.append(recovery);
		sb.append("<br/>");
		return sb.toString();
	}

	/**
	 * @return Returns the dropTypeIdList.
	 */
	public List getDropTypeIdList() {
		return dropTypeIdList;
	}

	/**
	 * @param dropTypeIdList
	 *            The dropTypeIdList to set.
	 */
	public void setDropTypeIdList(List dropTypeIdList) {
		this.dropTypeIdList = dropTypeIdList;
	}

	/**
	 * @return Returns the rate.
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            The rate to set.
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * @return Returns the initPhysical.
	 */
	public int getInitPhysical() {
		return initPhysical;
	}

	/**
	 * @param initPhysical The initPhysical to set.
	 */
	public void setInitPhysical(int initPhysical) {
		this.initPhysical = initPhysical;
	}
}
