package jc.user;

import net.joycool.wap.util.StringUtil;

public class UserBean2 {
	int id;
	String mobile;
	String password;
	String nickName;
	int gender;
	int age;
	int checked;	// 0=未审核;1=已审核，但未确认基本信息;
	long createTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public String getNickNameWml(){
		return StringUtil.toWml(this.nickName);
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getGender() {
		return gender;
	}
	public String getGenderText(){
		if(this.gender == 0){
			return "女";
		} else if( gender == 1){
			return "男";
		} else {
			return "保密";
		}
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
