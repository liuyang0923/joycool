package net.joycool.wap.spec.farm;

import java.util.*;

import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 用户采集专业
 * @datetime:1007-10-24
 */
public class FarmUserProBean {
	int id;
	int userId;
	int pro;		// 本职业id
	int exp = 0;		// 经验值
	int rank = 1;		// 等级
	int maxRank;		// 等级上限
	String skill = "";	// 特殊技能学习，类似配方，逗号间隔
	List skills = new ArrayList();
	
	static int[] expRank = {0,20,50,100,180,300,440,600,780,1000,
		1250,1600,2000,2500,3150,4000,5000,6000,7000,8000,
		9000,10000,11000,12500,14000,15500,17000,19000,21000,
		23000,25000,28000,31000,34000,38000,42000,46000,51000,56000};		// 升级所需要的经验值
	
	public List getSkillList() {
		return skills;
	}
	
	public boolean addExp(int add) {
		if(add >= 0) {
			if(exp >= expRank[rank])
				return false;
			exp += add;
			if(exp >= expRank[rank])
				exp = expRank[rank];
		} else {
			if(exp <= expRank[rank - 1])
				return false;
			exp += add;
			if(exp < expRank[rank - 1])
				exp = expRank[rank - 1];
		}
		return true;
	}
	/**
	 * @return Returns the exp.
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * @param exp The exp to set.
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the pro.
	 */
	public int getPro() {
		return pro;
	}

	/**
	 * @param pro The pro to set.
	 */
	public void setPro(int pro) {
		this.pro = pro;
	}

	public void addRank(int add) {
		rank += add;
	}
	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return Returns the skill.
	 */
	public String getSkill() {
		return skill;
	}

	/**
	 * @param skill The skill to set.
	 */
	public void setSkill(String skill) {
		this.skill = skill;
		skills = StringUtil.toInts(skill);
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	// 判断是否可以升级
	public boolean canUpgrade() {
		return expRank[rank] <= exp;
	}
	public boolean canUpgrade(FarmProBean p) {
		return expRank[rank] <= exp && maxRank > rank;
	}
	// 下个等级需要的经验值
	public int getUpgradeExp() {
		return expRank[rank];
	}
	// 这个等级的开始经验值
	public int getRankExp() {
		return expRank[rank - 1];
	}
	// 升级需要的经验值
	public int getUpgradeExpAdd() {
		return getUpgradeExp() - getRankExp();
	}
	
	public void addSkill(int skillId) {
		skills.add(Integer.valueOf(skillId));
		skill += skillId + ",";
	}
	
	public boolean hasSkill(int skillId) {
		return skills.contains(Integer.valueOf(skillId));
	}

	public int getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}
}
