package jc.family.game.emperor;

public class EmperorRoleBean {
	int id;
	int skillId; // 人物技能id
	int skillType; // 技能类型(1主动,2辅助,3被动)
	int simpleHurt = 2; // 普通伤害血量,暂定等于2
	int skillSpecilHurt; // 技能特殊伤害量
	int skillSimpleHurt; // 技能普通伤害量
	int simpleHurtContribute = 2; // 普杀贡献点,暂定等于2
	int skillHurtContribute; // 技能贡献点
//	int poison; // 毒
//	int vampire; // 噬血
	int sumBlood; // 最大血
	int effectSide; // 1对敌技能,0对友技能
//	int effectOther; // 技能是否有附带作用到其他人(0否,1是)
	int effectRange; // 技能作用范围:(1不包括防御状态的人,0所有人)
	String name; // 人物名字
	String skillName; // 技能名字
	String peopleIntroduction; // 人物介绍
	String skillIntroduction; // 技能介绍

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getSkillType() {
		return skillType;
	}

	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}

	public int getSimpleHurt() {
		return simpleHurt;
	}

	public void setSimpleHurt(int simpleHurt) {
		this.simpleHurt = simpleHurt;
	}

	public int getSkillSpecilHurt() {
		return skillSpecilHurt;
	}

	public void setSkillSpecilHurt(int skillSpecilHurt) {
		this.skillSpecilHurt = skillSpecilHurt;
	}

	public int getSkillSimpleHurt() {
		return skillSimpleHurt;
	}

	public void setSkillSimpleHurt(int skillSimpleHurt) {
		this.skillSimpleHurt = skillSimpleHurt;
	}

	public int getSimpleHurtContribute() {
		return simpleHurtContribute;
	}

	public void setSimpleHurtContribute(int simpleHurtContribute) {
		this.simpleHurtContribute = simpleHurtContribute;
	}

	public int getSkillHurtContribute() {
		return skillHurtContribute;
	}

	public void setSkillHurtContribute(int skillHurtContribute) {
		this.skillHurtContribute = skillHurtContribute;
	}
//
//	public int getPoison() {
//		return poison;
//	}
//
//	public void setPoison(int poison) {
//		this.poison = poison;
//	}
//
//	public int getVampire() {
//		return vampire;
//	}
//
//	public void setVampire(int vampire) {
//		this.vampire = vampire;
//	}

	public int getSumBlood() {
		return sumBlood;
	}

	public void setSumBlood(int sumBlood) {
		this.sumBlood = sumBlood;
	}

	public int getEffectSide() {
		return effectSide;
	}

	public void setEffectSide(int effectSide) {
		this.effectSide = effectSide;
	}

//	public int getEffectOther() {
//		return effectOther;
//	}
//
//	public void setEffectOther(int effectOther) {
//		this.effectOther = effectOther;
//	}

	public int getEffectRange() {
		return effectRange;
	}

	public void setEffectRange(int effectRange) {
		this.effectRange = effectRange;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getPeopleIntroduction() {
		return peopleIntroduction;
	}

	public void setPeopleIntroduction(String peopleIntroduction) {
		this.peopleIntroduction = peopleIntroduction;
	}

	public String getSkillIntroduction() {
		return skillIntroduction;
	}

	public void setSkillIntroduction(String skillIntroduction) {
		this.skillIntroduction = skillIntroduction;
	}

}
