package jc.family.game.boat;

public class AccidentBean {
	int id;
	int angle1; // 角度改变1
	int angle2; // 角度改变2
	int speed1;// 速度改变1
	int speed2;// 速度改变2
	int distance1;// 位置改变1
	int distance2;// 位置改变2
	int angleType;// 0没有,1有
	int speedType;// 1提高速度,2降低速度
	int distanceType;// 1前进,2后退
	int percent;// 出现概率
	String name;// 事件名称
	String bigImg; // 图片地址
	String bak;// 描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAngle1() {
		return angle1;
	}

	public void setAngle1(int angle1) {
		this.angle1 = angle1;
	}

	public int getSpeed1() {
		return speed1;
	}

	public void setSpeed1(int speed1) {
		this.speed1 = speed1;
	}

	public int getDistance1() {
		return distance1;
	}

	public void setDistance1(int distance1) {
		this.distance1 = distance1;
	}

	public int getAngle2() {
		return angle2;
	}

	public void setAngle2(int angle2) {
		this.angle2 = angle2;
	}

	public int getSpeed2() {
		return speed2;
	}

	public void setSpeed2(int speed2) {
		this.speed2 = speed2;
	}

	public int getDistance2() {
		return distance2;
	}

	public void setDistance2(int distance2) {
		this.distance2 = distance2;
	}

	public int getPercent() {
		return percent;
	}

	public int getAngleType() {
		return angleType;
	}

	public void setAngleType(int angleType) {
		this.angleType = angleType;
	}

	public int getSpeedType() {
		return speedType;
	}

	public void setSpeedType(int speedType) {
		this.speedType = speedType;
	}

	public int getDistanceType() {
		return distanceType;
	}

	public void setDistanceType(int distanceType) {
		this.distanceType = distanceType;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBigImg() {
		return bigImg;
	}

	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}
}
