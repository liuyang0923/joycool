package net.joycool.wap.spec.friend;

public class MoodBean {
	int 	id;			//心情ID
	int 	userId;		//用户ID
	String 	mood;		//心情
	long 	createTime;	//心情发布时间
	int 	viewCount;	//浏览次数
	int 	replyCount;	//回复的次数
	int		type;		//类型
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public String getTypeName() {
		return (String)MoodAction.moodType.get(type);
	}
}
