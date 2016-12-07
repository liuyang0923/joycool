package jc.guest;

import net.joycool.wap.util.StringUtil;

/**
 * 游客的基本信息
 */
public class GuestUserInfo {
	int id;
	String userName;
	String password;
	int age;
	int gender;		//0:女,1:男,2:保密
	String mobile;
	int flag;	// 0:创建了用户，但用户没有更改密码
	long createTime;
	int point;	// 经验
	int money;	// 铜钱
	int focus;	// 0:允许任何人关注、1:拒绝任何人关注、2:需要身份验证
	int level;  // 等级
	int buid;	// 如果正式用户来注册游客的话，这里记录正式用户的UID，同时算做此游客信息与此uid绑定
	int award;	// 领取每日奖励的日期。比如，08月24日领取了奖励，则记录为数字824,08月3日记录为数字803。
	String myTitle;	// 记录我获得的称号的ID。以“，”分割。如“1，2，5”，则带表分别获得过“呱呱落地”,“安全第一”和“小财主”这三个称号。
	int titleNow;	// 当前称号
	int special;	// 特殊奖励标记.0:没领过任何特殊奖励;1:游客第一次领取今日奖励;2:注册用户第一次领取奖励
	long lastTime;	// 上次登陆时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserNameWml() {
		return StringUtil.toWml(userName);
	}
	/**
	 * 此方法将在名子后加上称号
	 * @return
	 */
	public String getUserNameWml2() {
		StringBuilder sb = new StringBuilder();
		sb.append(userName);
		if (titleNow > 0 && titleNow < GuestHallAction.title.length){
			sb.append("[");
			sb.append(GuestHallAction.title[titleNow]);
			sb.append("]");
		}
		return StringUtil.toWml(sb.toString());
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 此方法将在名子后加上称号
	 * @return
	 */
	public String getUserName2() {
		StringBuilder sb = new StringBuilder();
		sb.append(userName);
		if (titleNow > 0 && titleNow < GuestHallAction.title.length){
			sb.append("[");
			sb.append(GuestHallAction.title[titleNow]);
			sb.append("]");
		}
		return sb.toString();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getGenderStr(){
		if (this.getGender() == 0){
			return "女";
		} else if (this.getGender() == 1){
			return "男";
		} else {
			return "保密";
		}
	}
	public String getGenderStr2(){
		if (this.getGender() == 0){
			return "她";
		} else if (this.getGender() == 1){
			return "他";
		} else {
			return "TA";
		}
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getFocus() {
		return focus;
	}
	public String getFocusString(){
		if (this.focus == 0){
			return "允许任何人关注";
		} else if (this.focus == 1){
			return "拒绝任何人关注";
		} else {
			return "需要身份验证";
		}
	}
	public void setFocus(int focus) {
		this.focus = focus;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getBuid() {
		return buid;
	}
	public void setBuid(int buid) {
		this.buid = buid;
	}
	public int getAward() {
		return award;
	}
	public void setAward(int award) {
		this.award = award;
	}
	public String getMyTitle() {
		return myTitle;
	}
	public void setMyTitle(String myTitle) {
		this.myTitle = myTitle;
	}
	public int getTitleNow() {
		return titleNow;
	}
	public void setTitleNow(int titleNow) {
		this.titleNow = titleNow;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
}
