package jc.family.game;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameHistoryBean {

	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-M-d");

	int id;// 表主键
	int mid;// 赛事表主键
	int fid1; // 家族id
	int fid2; // 家族id2(雪仗用)
	long spendTime = 0;// 用时
	long prize = 0;// 奖金
	int numTotal = 0;// 参赛人数
	int rank = 0;// 名次(龙舟)/获胜的家族id/名次
	int shipId;// 龙舟id / 获得积分(雪仗)
	float maxSpeed;// 最大速度
	int type;// 1(龙舟)2(雪仗)3(问答)
	long holdTime;// 举行时间
	float distance = 0;// 行驶路程
	int score = 0;// 获得积分/问答积分
	int askscore;// 问答积分
	int snow_score;// 雪仗积分
	int gamePoint = 0;// 游戏中家族获得的游戏经验值

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

	public int getFid2() {
		return fid2;
	}

	public void setFid2(int fid2) {
		this.fid2 = fid2;
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

	public int getNumTotal() {
		return numTotal;
	}

	public void setNumTotal(int numTotal) {
		this.numTotal = numTotal;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getShipId() {
		return shipId;
	}

	public void setShipId(int shipId) {
		this.shipId = shipId;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getHoldTime() {
		return holdTime;
	}

	public String getHoldTimeToString() {
		return sdf2.format(new Date(holdTime));
	}

	public void setHoldTime(long holdTime) {
		this.holdTime = holdTime;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAskscore() {
		return askscore;
	}

	public void setAskscore(int askscore) {
		this.askscore = askscore;
	}

	public int getSnow_score() {
		return snow_score;
	}

	public void setSnow_score(int snowScore) {
		snow_score = snowScore;
	}

	public int getGamePoint() {
		return gamePoint;
	}

	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}
}
