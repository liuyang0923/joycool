package net.joycool.wap.spec.friend;

public class MoodReply {
	int 	id;			//id
	int 	moodId;		//心情id，与MoodBean中的id对应
	int 	userId;		//回复者的用户id
	long 	createTime;	//回复的时间
	String 	reply;		//回复内容
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMoodId() {
		return moodId;
	}
	public void setMoodId(int moodId) {
		this.moodId = moodId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserID(int userId) {
		this.userId = userId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	
}
