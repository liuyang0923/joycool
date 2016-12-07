package jc.family.game.emperor;

import jc.family.game.vs.VsUserBean;

public class EmperorUserBean extends VsUserBean implements Comparable {
	int life = 1; // 生命数
	int blood;
	int dieRound = 0; // 第几回合死的
	int operation; // 0表示无操作,1表示普通攻击,2表示技能攻击,3表示辅助技能
	int contribute; // 贡献值
	int beSimpleHitTime; // 被普通击杀的次数
	int hasVampire; // 噬血
	int bePoision; // 被毒
	boolean isAhead; // 是否为出战状态
	boolean isDeath; // 是否已经阵亡
	boolean isHiddenFightInfo; // 战斗信息是否隐藏
	boolean hasSit; // 已经坐下
	boolean hasAddToWait; // 是否已经加到wait状态
	EmperorUserBean copySkillUser; // 使用别人的技能,每次清空
	EmperorUserBean addHurtUser; // 增加普通伤害
	EmperorUserBean addSpecilHurtUser; // 造成的特殊伤害+2
	EmperorUserBean reductionHurtUser; // 使自己伤害减少的人
	EmperorUserBean noHitUser; // 使自己技能击杀无效
	// EmperorUserBean posionUser; // 使自己中毒的人
	EmperorUserBean addBloodUser; // 给自己加血的人
	EmperorUserBean addDefUser; // 给自己加防御的人
	EmperorUserBean effectUser; // 自己发动技能使用对象
	EmperorRoleBean role = null; // 自己使用的人物角色属性

	public void decLife() {
		life--;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getDieRound() {
		return dieRound;
	}

	public void setDieRound(int dieRound) {
		this.dieRound = dieRound;
	}

	public int getContribute() {
		return contribute;
	}

	public void setContribute(int contribute) {
		this.contribute = contribute;
	}

	public int getBeSimpleHitTime() {
		return beSimpleHitTime;
	}

	public void setBeSimpleHitTime(int beSimpleHitTime) {
		this.beSimpleHitTime = beSimpleHitTime;
	}

	public int getHasVampire() {
		return hasVampire;
	}

	public void setHasVampire(int hasVampire) {
		this.hasVampire = hasVampire;
	}

	public int getBePoision() {
		return bePoision;
	}

	public void setBePoision(int bePoision) {
		this.bePoision = bePoision;
	}

	public boolean isAhead() {
		return isAhead;
	}

	public void setAhead(boolean isAhead) {
		this.isAhead = isAhead;
	}

	public boolean isDeath() {
		return isDeath;
	}

	public void setDeath(boolean isDeath) {
		this.isDeath = isDeath;
	}

	public boolean isHiddenFightInfo() {
		return isHiddenFightInfo;
	}

	public void setHiddenFightInfo(boolean isHiddenFightInfo) {
		this.isHiddenFightInfo = isHiddenFightInfo;
	}

	public boolean isHasSit() {
		return hasSit;
	}

	public void setHasSit(boolean hasSit) {
		this.hasSit = hasSit;
	}

	public boolean isHasAddToWait() {
		return hasAddToWait;
	}

	public void setHasAddToWait(boolean hasAddToWait) {
		this.hasAddToWait = hasAddToWait;
	}

	public EmperorUserBean getCopySkillUser() {
		return copySkillUser;
	}

	public void setCopySkillUser(EmperorUserBean copySkillUser) {
		this.copySkillUser = copySkillUser;
	}

	public EmperorUserBean getAddHurtUser() {
		return addHurtUser;
	}

	public void setAddHurtUser(EmperorUserBean addHurtUser) {
		this.addHurtUser = addHurtUser;
	}

	public EmperorUserBean getAddSpecilHurtUser() {
		return addSpecilHurtUser;
	}

	public void setAddSpecilHurtUser(EmperorUserBean addSpecilHurtUser) {
		this.addSpecilHurtUser = addSpecilHurtUser;
	}

	public EmperorUserBean getReductionHurtUser() {
		return reductionHurtUser;
	}

	public void setReductionHurtUser(EmperorUserBean reductionHurtUser) {
		this.reductionHurtUser = reductionHurtUser;
	}

	public EmperorUserBean getNoHitUser() {
		return noHitUser;
	}

	public void setNoHitUser(EmperorUserBean noHitUser) {
		this.noHitUser = noHitUser;
	}

	// public EmperorUserBean getPosionUser() {
	// return posionUser;
	// }
	//
	// public void setPosionUser(EmperorUserBean posionUser) {
	// this.posionUser = posionUser;
	// }

	public EmperorUserBean getAddBloodUser() {
		return addBloodUser;
	}

	public void setAddBloodUser(EmperorUserBean addBloodUser) {
		this.addBloodUser = addBloodUser;
	}

	public EmperorUserBean getAddDefUser() {
		return addDefUser;
	}

	public void setAddDefUser(EmperorUserBean addDefUser) {
		this.addDefUser = addDefUser;
	}

	public EmperorUserBean getEffectUser() {
		return effectUser;
	}

	public void setEffectUser(EmperorUserBean effectUser) {
		this.effectUser = effectUser;
	}

	public EmperorRoleBean getRole() {
		return role;
	}

	public void setRole(EmperorRoleBean role) {
		this.role = role;
	}

	public int compareTo(Object o) {
		if (o instanceof EmperorUserBean) {
			if (contribute < ((EmperorUserBean) o).getContribute()) {
				return 1;
			} else if (contribute > ((EmperorUserBean) o).getContribute()) {
				return -1;
			} else {
				return 0;
			}
		} else {
			throw new ClassCastException("Can't compare");
		}
	}

}
