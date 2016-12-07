package jc.family.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class MatchBean {

	int id;// 表主键
	int fmCount = 0;// 家族参加数
	int type;// 1(龙舟)2(雪仗)3(问答)
	int state; // 0报名阶段,1表示开始游戏,2表示游戏结束
	int state2; // 0表示正常赛事,1表示管理员强制关闭
	Date starttime;// 比赛开始时间
	Date endtime;// 比赛结束时间
	long createTime;
	long endTime;

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date startTime) {
		starttime = startTime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public java.sql.Timestamp getEndSQLtime() {
		return new java.sql.Timestamp(endtime.getTime());
	}

	public java.sql.Timestamp getStartSQLtime() {
		return new java.sql.Timestamp(starttime.getTime());
	}

	public void setEndtime(Date endTime) {
		endtime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFmCount() {
		return fmCount;
	}

	public void setFmCount(int fmCount) {
		this.fmCount = fmCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	List FmList = null; // 所有报名家族
	HashMap gameMap = new HashMap(); // 子类游戏用,key=家族id,value=家族游戏信息
	GameBean gameBean = null; // 用于展示家族所有赛事
	long totalCoin = 0; // 赛事总报名费用
	int rankNum = 0; // 排名到第几了

	public int getRankNum() {
		return rankNum;
	}

	public void setRankNum(int rankNum) {
		this.rankNum = rankNum;
	}

	public long getTotalCoin() {
		return totalCoin;
	}

	public void setTotalCoin(long totalCoin) {
		this.totalCoin = totalCoin;
	}

	public List getFmList() {
		return FmList;
	}

	public void setFmList(List fmList) {
		FmList = fmList;
	}

	public GameBean getGameBean() {
		return gameBean;
	}

	public void setGameBean(GameBean gameBean) {
		this.gameBean = gameBean;
	}

	public HashMap getGameMap() {
		return gameMap;
	}

	public void setGameMap(HashMap gameMap) {
		this.gameMap = gameMap;
	}

	// 龙舟用
	List completeList = new ArrayList();
	TimerTask boatTask; // 8秒整理玩家数据
	TimerTask boatTask2; // 1分钟随机事件

	public List getCompleteList() {
		return completeList;
	}

	public void setCompleteList(List completeList) {
		this.completeList = completeList;
	}

	public TimerTask getBoatTask() {
		return boatTask;
	}

	public void setBoatTask(TimerTask boatTask) {
		this.boatTask = boatTask;
	}

	public TimerTask getBoatTask2() {
		return boatTask2;
	}

	public void setBoatTask2(TimerTask boatTask2) {
		this.boatTask2 = boatTask2;
	}

	public int getState2() {
		return state2;
	}

	public void setState2(int state2) {
		this.state2 = state2;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
