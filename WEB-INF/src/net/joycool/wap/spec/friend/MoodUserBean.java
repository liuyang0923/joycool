package net.joycool.wap.spec.friend;

public class MoodUserBean {
	int 	userId;		//用户ID
	String 	mood;		//心情
	long 	createTime;	//创建时间
	int 	type;		//类型
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	

	public String getTypeName() {
		return (String)MoodAction.moodType.get(type);
	}
}
