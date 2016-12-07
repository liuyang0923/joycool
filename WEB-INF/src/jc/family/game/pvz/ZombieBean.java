package jc.family.game.pvz;

// 僵尸实例
public class ZombieBean {
	int currI;
	int currJ;
	int hp; // 当前血量
	int uid; // 谁种的僵尸
	int state; // 有特殊效果的僵尸,要有一个状态来确定他的动作
	long actionTime; // 攻击,移动共用冷却时间
	ZombieProtoBean proto;

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

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public long getActionTime() {
		return actionTime;
	}

	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

	public ZombieProtoBean getProto() {
		return proto;
	}

	public void setProto(ZombieProtoBean proto) {
		this.proto = proto;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
