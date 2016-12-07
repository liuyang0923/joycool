package jc.family.game.pvz;

// 植物实例
public class PlantBean {
	int id;// 类型
	int hp; // 血量
	int uid; // 谁放置的
	int state; // 有特殊效果的植物,要有一个状态来确定他的动作
	long actionTime; // 攻击时间
	PlantProtoBean proto;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public PlantProtoBean getProto() {
		return proto;
	}

	public void setProto(PlantProtoBean proto) {
		this.proto = proto;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
