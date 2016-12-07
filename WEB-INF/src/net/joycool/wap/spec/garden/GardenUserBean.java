package net.joycool.wap.spec.garden;

import java.util.Date;

import net.joycool.wap.util.StringUtil;

public class GardenUserBean {
	int exp;	//经验
	int gold;	//资金
	int bugCount;	//使用虫子的次数
	int grassCount; //使用草的次数
	int fieldCount;	//田的数量
	int uid;		//用户id
	Date createTime;
	Date enterTime;
	int todayExp;
	int msgCount;
	String name;
	
	public String getName() {
		return name;
	}
	public String getNameWml() {
		return StringUtil.toWml(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}
	public int getTodayExp() {
		return todayExp;
	}
	public void setTodayExp(int todayExp) {
		this.todayExp = todayExp;
	}
	public Date getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public GardenUserBean(int uid) {
		this.exp = 0;
		this.gold = 1000;
		this.bugCount = 0;
		this.grassCount = 0;
		this.fieldCount = GardenUtil.defaultCount;	//默认为2块田地
		this.uid = uid;		
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getBugCount() {
		return bugCount;
	}
	public void setBugCount(int bugCount) {
		this.bugCount = bugCount;
	}
	public int getGrassCount() {
		return grassCount;
	}
	public void setGrassCount(int grassCount) {
		this.grassCount = grassCount;
	}
	public int getFieldCount() {
		return fieldCount;
	}
	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}

	public GardenUserBean() {
		super();
	}
	public void addGold(int price) {
		gold += price;
	}
	
	
	
}
