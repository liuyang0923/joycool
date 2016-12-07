package net.joycool.wap.spec.castle;

public class TongPowerBean {

	public static final int POWER_TOP = 1 << 0;	//是否可以任命其他人
	public static final int POWER_INVITE = 1 << 1;	//是否可以邀请他人
	public static final int POWER_DELETE = 1 << 2;	//开除
	public static final int POWER_INTRO = 1 << 3;	//修改描述
	public static final int POWER_NAME = 1 << 4;	//修改名称
	public static final int POWER_DIP = 1 << 5;		//外交
	
	
	public static final int POWER_ALL = 63;
	
	int uid;
	String powerName;
	int power;
	int tongId;
	
	public void addPower(int power) {
		this.power |= power;
	}
	
	public void deletePower(int power) {
		this.power &= ~power;
	}
	
	public boolean isPowerDip(){
		return (power & POWER_DIP) != 0;
	}
	
	public boolean isPowerTop() {
		return (power & POWER_TOP) != 0;
	}
	
	public boolean isPowerInvite(){
		return (power & POWER_INVITE) != 0;
	}
	
	public boolean isPowerDelete(){
		return (power & POWER_DELETE) != 0;
	}
	
	public boolean isPowerIntro(){
		return (power & POWER_INTRO) != 0;
	}
	
	public boolean isPowerName(){
		return (power & POWER_NAME) != 0;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getPowerName() {
		return powerName;
	}
	public void setPowerName(String powerName) {
		this.powerName = powerName;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getTongId() {
		return tongId;
	}
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}
	public void togglePower(int is) {
		power ^= (1 << is);
	}
}
