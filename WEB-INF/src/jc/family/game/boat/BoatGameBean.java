package jc.family.game.boat;

import java.util.*;

public class BoatGameBean {
	int id;// 表主键
	int mid;// 赛事表主键
	int fid1; // 家族id
	int type;// 1(龙舟)2(雪仗)3(问答)
	int rank = 0;// 名次(龙舟)/获胜的家族id/名次
	int score = 0; // 积分
	int numTotal = 0;// 参赛人数
	int shipId;// 龙舟id / 获得积分(雪仗)
	int isover = 0; // 赛事是否完成
	int angle = 0;// 当前角度(负数表示向左,否则表示向右)
	int speedNum = 0; // 速度操作数;
	int gamePoint = 0;// 游戏中家族获得的游戏经验值
	int allDistance = 8000; // 全程
	float distance = 0;// 行驶路程
	float lineSpeed = 0; // 直线速度
	float speed = 0; // 当前速度
	float maxSpeed;// 最大速度
	long spendTime = 0;// 用时
	long prize = 0;// 奖金
	long totalCoin = 0; // 本家族扣钱数
	String fmName = "";// 家族名字
	String accident = ""; // 随机事件提示信息(页面用给用户看)
	String accidentImg = ""; // 随机事件图片
	Object[] seat = new Object[11];// 龙舟座位
	HashMap memberMap = new HashMap(); // 家族参赛(已扣钱)成员
	BoatBean boat; // 使用的龙舟
	int boatType; // 龙舟类型

	public int getBoatType() {
		return boatType;
	}

	public void setBoatType(int boatType) {
		this.boatType = boatType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getFid1() {
		return fid1;
	}

	public void setFid1(int fid1) {
		this.fid1 = fid1;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(int numTotal) {
		this.numTotal = numTotal;
	}

	public int getShipId() {
		return shipId;
	}

	public void setShipId(int shipId) {
		this.shipId = shipId;
	}

	public int getIsover() {
		return isover;
	}

	public void setIsover(int isover) {
		this.isover = isover;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getSpeedNum() {
		return speedNum;
	}

	public void setSpeedNum(int speedNum) {
		this.speedNum = speedNum;
	}

	public int getGamePoint() {
		return gamePoint;
	}

	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}

	public int getAllDistance() {
		return allDistance;
	}

	public void setAllDistance(int allDistance) {
		this.allDistance = allDistance;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getLineSpeed() {
		return lineSpeed;
	}

	public void setLineSpeed(float lineSpeed) {
		this.lineSpeed = lineSpeed;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public long getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}

	public long getPrize() {
		return prize;
	}

	public void setPrize(long prize) {
		this.prize = prize;
	}

	public long getTotalCoin() {
		return totalCoin;
	}

	public void setTotalCoin(long totalCoin) {
		this.totalCoin = totalCoin;
	}

	public String getFmName() {
		return fmName;
	}

	public void setFmName(String fmName) {
		this.fmName = fmName;
	}

	public String getAccident() {
		return accident;
	}

	public void setAccident(String accident) {
		this.accident = accident;
	}

	public String getAccidentImg() {
		return accidentImg;
	}

	public void setAccidentImg(String accidentImg) {
		this.accidentImg = accidentImg;
	}

	public Object[] getSeat() {
		return seat;
	}

	public void setSeat(Object[] seat) {
		this.seat = seat;
	}

	public HashMap getMemberMap() {
		return memberMap;
	}

	public void setMemberMap(HashMap memberMap) {
		this.memberMap = memberMap;
	}

	public BoatBean getBoat() {
		return boat;
	}

	public void setBoat(BoatBean boat) {
		this.boat = boat;
	}
}
