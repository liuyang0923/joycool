package net.joycool.wap.spec.buyfriends;

import java.util.Date;

import net.joycool.wap.util.DateUtil;

public class BeanMaster {

	private int uid;
	private String nickName;	
	private int money;			//用户拥有的钱
	private int price;			//用户的身价
	private int slaveCount;		//用户的奴隶数量
	private Date ransomTime;	//赎身时间
	long salaryTime;
	
	int rank;					//未知
	int honor;					//未知
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getSlaveCount() {
		return slaveCount;
	}
	public void setSlaveCount(int slaveCount) {
		this.slaveCount = slaveCount;
	}
	public Date getRansomTime() {
		return ransomTime;
	}
	public void setRansomTime(Date ransomTime) {
		this.ransomTime = ransomTime;
	}
	public long getSalaryTime() {
		return salaryTime;
	}
	public void setSalaryTime(long salaryTime) {
		this.salaryTime = salaryTime;
	}
	public boolean isSalaryTime(long now) {
		return DateUtil.dayDiff(now, salaryTime) != 0;
	}
	public boolean isSalaryTime() {
		return DateUtil.dayDiff(System.currentTimeMillis(), salaryTime) != 0;
	}
}
