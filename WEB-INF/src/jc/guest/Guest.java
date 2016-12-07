package jc.guest;

import net.joycool.wap.util.StringUtil;

public class Guest {
	int id;
	int dbId;
	int uid;
	String phone;
	String password;
	String nickName;
	int age;	//0:保密
	int gender;	//1:男 2:女 3:保密
	long createTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDbId() {
		return dbId;
	}
	public void setDbId(int dbId) {
		this.dbId = dbId;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getNickNameWml() {
		return StringUtil.toWml(nickName);
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long time) {
		this.createTime = time;
	}
	public String getGenderString(){
		if (gender == 1){
			return "男";
		} else if (gender == 2){
			return "女";
		} else {
			return "保密";
		}
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
