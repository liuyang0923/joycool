package jc.family.game;

import java.util.HashMap;

import jc.family.FamilyAction;
import jc.family.FamilyService;
import jc.family.game.boat.BoatBean;
import net.joycool.wap.util.StringUtil;

public class GameBean {

	int id;// 表主键
	int mid;// 赛事表主键
	int fid1; // 家族id
	int fid2; // 家族id2(雪仗用)
	int numTotal = 0;// 参赛人数
	int rank = 0;// 名次(龙舟)/获胜的家族id/名次
	int shipId;// 龙舟id / 获得积分(雪仗)
	int type;// 1(龙舟)2(雪仗)3(问答)
	float maxSpeed;// 最大速度
	long spendTime = 0;// 用时
	long prize = 0;// 奖金
	int gamePoint = 0;// 游戏中家族获得的游戏经验值

	public int getGamePoint() {
		return gamePoint;
	}

	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}

	public int getFid2() {
		return fid2;
	}

	public void setFid2(int fid2) {
		this.fid2 = fid2;
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

	// 雪仗
	String holdTime;// 举行时间
	int score = 0;// 获得积分/问答积分
	HashMap memberBean = new HashMap();// 参加游戏的家族用户
	HashMap toolsBean = new HashMap();// 家族的道具

	String fmName1;// 家族1的名字
	String fmName2;// 家族2的名字
	int snowAccumulation = 0;// 自己积雪量
	int contribution = 0;// 个人在比赛得到的贡献值

	public String getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public HashMap getMemberBean() {
		return memberBean;
	}

	public void setMemberBean(HashMap memberBean) {
		this.memberBean = memberBean;
	}

	public HashMap getToolsBean() {
		return toolsBean;
	}

	public void setToolsBean(HashMap toolsBean) {
		this.toolsBean = toolsBean;
	}

	public int getSnowAccumulation() {
		return snowAccumulation;
	}

	public void setSnowAccumulation(int snowAccumulation) {
		this.snowAccumulation = snowAccumulation;
	}

	public int getContribution() {
		return contribution;
	}

	public void setContribution(int contribution) {
		this.contribution = contribution;
	}

	public String getFmName1() {
		fmName1 = FamilyAction.getFmByID(fid1).getFm_name();
		return StringUtil.toWml(fmName1);
	}

	public void setFmName1(String fmName1) {
		this.fmName1 = fmName1;
	}

	public String getFmName2() {
		if (fid2 == 0) {
			return "";
		}
		fmName2 = FamilyAction.getFmByID(fid2).getFm_name();
		return StringUtil.toWml(fmName2);
	}

	public void setFmName2(String fmName2) {
		this.fmName2 = fmName2;
	}

	// 龙舟用
	int allDistance = 8000; // 全程
	int isover = 0; // 赛事是否完成
	int speedNum = 0; // 速度操作数;
	int angle = 0;// 当前角度(负数表示向左,否则表示向右)
	float lineSpeed = 0; // 直线速度
	float speed = 0; // 当前速度
	float distance = 0;// 行驶路程
	String fmName = "";// 家族名字
	String accident = ""; // 随机事件提示信息(页面用给用户看)
	Object[] seat = new Object[11];// 参赛队员
	BoatBean boat; // 游戏中龙舟信息;
	long totalCoin = 0; // 本家族扣钱数

	public int getAllDistance() {
		return allDistance;
	}

	public void setAllDistance(int allDistance) {
		this.allDistance = allDistance;
	}

	public int getIsover() {
		return isover;
	}

	public void setIsover(int isover) {
		this.isover = isover;
	}

	public int getSpeedNum() {
		return speedNum;
	}

	public void setSpeedNum(int speedNum) {
		this.speedNum = speedNum;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
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

	public Object[] getSeat() {
		return seat;
	}

	public void setSeat(Object[] seat) {
		this.seat = seat;
	}

	public BoatBean getBoat() {
		return boat;
	}

	public void setBoat(BoatBean boat) {
		this.boat = boat;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public float getLineSpeed() {
		return lineSpeed;
	}

	public void setLineSpeed(float lineSpeed) {
		this.lineSpeed = lineSpeed;
	}

	public long getTotalCoin() {
		return totalCoin;
	}

	public void setTotalCoin(long totalCoin) {
		this.totalCoin = totalCoin;
	}
}
