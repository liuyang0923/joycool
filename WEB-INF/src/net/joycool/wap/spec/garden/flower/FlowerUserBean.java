package net.joycool.wap.spec.garden.flower;

import java.util.ArrayList;

public class FlowerUserBean {
	int userId;
	int exp;
	int usedExp;
	int fieldCount;
	ArrayList stealList = new ArrayList();
	long stealTime;		// 踩的时间
	long stealTime2;	// 被踩的时间
	int taskNum;
	boolean isTaskComplete = false;
	int stealCount;		// 踩的次数
	int stealCount2;	// 被踩的次数
	
	public int getStealCount() {
		return stealCount;
	}
	public void setStealCount(int stealCount) {
		this.stealCount = stealCount;
	}
	public boolean isTaskComplete() {
		return isTaskComplete;
	}
	public void setTaskComplete(boolean isTaskComplete) {
		this.isTaskComplete = isTaskComplete;
	}
	public int getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	public long getStealTime() {
		return stealTime;
	}
	public void setStealTime(long stealTime) {
		this.stealTime = stealTime;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getFieldCount() {
		return fieldCount;
	}
	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public ArrayList getStealList() {
		return stealList;
	}
	public void setStealList(ArrayList stealList) {
		this.stealList = stealList;
	}
	public int getUsedExp() {
		return usedExp;
	}
	public void setUsedExp(int usedExp) {
		this.usedExp = usedExp;
	}
	public long getStealTime2() {
		return stealTime2;
	}
	public void setStealTime2(long stealTime2) {
		this.stealTime2 = stealTime2;
	}
	public int getStealCount2() {
		return stealCount2;
	}
	public void setStealCount2(int stealCount2) {
		this.stealCount2 = stealCount2;
	}
}