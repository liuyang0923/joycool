package jc.family.game.snow;

import java.util.HashMap;

import jc.family.FamilyAction;
import net.joycool.wap.util.StringUtil;

public class SnowBean {

	int id;// 表主键
	int mid;// 赛事表主键
	int fid1; // 家族id
	int fid2; // 对手家族id
	int numTotal = 0;// 参赛人数
	int rank = 0;// 名次（1赢;2输;3和4都代表平手,差别就是存入数据的先后不同先3后4,方便页面查看）
	int type;// 1(龙舟)2(雪仗)3(问答)
	long spendTime = 0;// 用时
	long prize = 0;// 雪币花费/奖金
	int gamePoint = 0;// 游戏中家族获得的游戏经验值
	String holdTime;// 举行时间
	int score = 0;// 获得积分
	HashMap memberBean = new HashMap();// 参加游戏的家族用户
	HashMap toolsBean = new HashMap();// 家族的道具

	String fmName1;// 家族1的名字
	String fmName2;// 家族2的名字
	int snowAccumulation = 0;// 自己积雪量
	int contribution = 0;// 个人在比赛得到的贡献值
	int hitNum=0;// 本家族总共击中对方次数

	public int getHitNum() {
		return hitNum;
	}

	public void setHitNum(int hitNum) {
		this.hitNum = hitNum;
	}

	public int getGamePoint() {
		return gamePoint;
	}

	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
}
