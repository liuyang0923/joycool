package jc.family.game.boat;

public class BoatBean {
	int id;
	int fmBoatId;
	int speed; // 船速
	int boatType; // 船类型
	int speAngleReset; // 角度复位技能
	int useTime; // 使用次数
	int rentType; // 租金类型(0乐币,1酷币)
	int maxSpeed; // 最大速度
	int point; // 积分限制
	float rent; // 租金
	long speEffectTime; // 特殊技能使用时间
	String name; // 船名字
	String bak; // 说明

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeAngleReset() {
		return speAngleReset;
	}

	public void setSpeAngleReset(int speAngleReset) {
		this.speAngleReset = speAngleReset;
	}

	public int getUseTime() {
		return useTime;
	}

	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}

	public int getRentType() {
		return rentType;
	}

	public void setRentType(int rentType) {
		this.rentType = rentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public int getBoatType() {
		return boatType;
	}

	public void setBoatType(int boatType) {
		this.boatType = boatType;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

	public long getSpeEffectTime() {
		return speEffectTime;
	}

	public void setSpeEffectTime(long speEffectTime) {
		this.speEffectTime = speEffectTime;
	}

	public int getFmBoatId() {
		return fmBoatId;
	}

	public void setFmBoatId(int fmBoatId) {
		this.fmBoatId = fmBoatId;
	}

	public float getRent() {
		return rent;
	}

	public void setRent(float rent) {
		this.rent = rent;
	}
}
