package jc.family.game.fightbox;

import jc.family.game.vs.VsUserBean;

public class BoxUserBean extends VsUserBean implements Comparable {
	int life = 1; // 生命数
	int rank; // 排名
	int blood; // 血(初始为10)
	int weapon; // 武器
	int currI; // 当前位置
	int currJ; // 当前位置
	int aimI; // 目标位置
	int aimJ; // 目标位置
	int mapState; // 页面显示状态
	int aimAtack; // 攻击方向
	int dieRound = 0; // 第几回合死的
	int hitFriendBlood = 0; // 击杀好友血量
	int hitEnemyBlood = 0; // 击杀敌人血量
	int hitFriendTime = 0; // 击杀好友次数
	int hitEnemyTime = 0; // 击杀敌人次数
	int[][] atackRange = new int[4][2]; // 攻击范围
	boolean isHidden; // 战斗信息是否隐藏
	boolean isDefense = true; // 是否防御(0防御,1未防御)
	boolean isDeath; // 是否已经阵亡

	public void decLife() {
		life--;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	public int getWeapon() {
		return weapon;
	}

	public void setWeapon(int weapon) {
		this.weapon = weapon;
	}

	public int getCurrI() {
		return currI;
	}

	public void setCurrI(int currI) {
		this.currI = currI;
	}

	public int getCurrJ() {
		return currJ;
	}

	public void setCurrJ(int currJ) {
		this.currJ = currJ;
	}

	public int getAimI() {
		return aimI;
	}

	public void setAimI(int aimI) {
		this.aimI = aimI;
	}

	public int getAimJ() {
		return aimJ;
	}

	public void setAimJ(int aimJ) {
		this.aimJ = aimJ;
	}

	public int getMapState() {
		return mapState;
	}

	public void setMapState(int mapState) {
		this.mapState = mapState;
	}

	public int getAimAtack() {
		return aimAtack;
	}

	public void setAimAtack(int aimAtack) {
		this.aimAtack = aimAtack;
	}

	public int getDieRound() {
		return dieRound;
	}

	public void setDieRound(int dieRound) {
		this.dieRound = dieRound;
	}

	public int getHitFriendBlood() {
		return hitFriendBlood;
	}

	public void setHitFriendBlood(int hitFriendBlood) {
		this.hitFriendBlood = hitFriendBlood;
	}

	public int getHitEnemyBlood() {
		return hitEnemyBlood;
	}

	public void setHitEnemyBlood(int hitEnemyBlood) {
		this.hitEnemyBlood = hitEnemyBlood;
	}

	public int getHitFriendTime() {
		return hitFriendTime;
	}

	public void setHitFriendTime(int hitFriendTime) {
		this.hitFriendTime = hitFriendTime;
	}

	public int getHitEnemyTime() {
		return hitEnemyTime;
	}

	public void setHitEnemyTime(int hitEnemyTime) {
		this.hitEnemyTime = hitEnemyTime;
	}

	public int[][] getAtackRange() {
		return atackRange;
	}

	public void setAtackRange(int[][] atackRange) {
		this.atackRange = atackRange;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isDefense() {
		return isDefense;
	}

	public void setDefense(boolean isDefense) {
		this.isDefense = isDefense;
	}

	public boolean isDeath() {
		return isDeath;
	}

	public void setDeath(boolean isDeath) {
		this.isDeath = isDeath;
	}

	public int compareTo(Object o) {
		if (o instanceof BoxUserBean) {
			if (hitEnemyTime < ((BoxUserBean) o).getHitEnemyTime()) {
				return 1;
			} else if (hitEnemyTime > ((BoxUserBean) o).getHitEnemyTime()) {
				return -1;
			} else {
				return 0;
			}
		} else {
			throw new ClassCastException("Can't compare");
		}
	}

}
