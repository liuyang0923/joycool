package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 比赛基本信息.(比赛开始时间，投票总数等)
 */
public class MatchInfo {
	int id;
	String title;
	int pointCount;			//当前乐币靓点
	int gamePointCount;		//当前酷币靓点
	int voteCount;			//当前投票靓点
	long startTime;			//开赛时间
	String startTimeStr;	//开赛时间(后右录入时用)
	long endTime;			//闭赛时间
	String endTimeStr;		//闭赛时间(后台录入时用)
	int falg;				//0:还未开始 1:比赛中 2:结束
	int userCount;			//用户总数
	int fansCount;			//当前投票人数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public String getTitleWml() {
		return StringUtil.toWml(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPointCount() {
		return pointCount;
	}
	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
	}
	public int getGamePointCount() {
		return gamePointCount;
	}
	public void setGamePointCount(int gamePointCount) {
		this.gamePointCount = gamePointCount;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getFalg() {
		return falg;
	}
	public void setFalg(int falg) {
		this.falg = falg;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public int getFansCount() {
		return fansCount;
	}
	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
}