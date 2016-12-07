package net.joycool.wap.spec.bottle;

import java.util.ArrayList;

public class BottleBean {
	
	int 		id;				//瓶子id
	int 		userId;			//瓶子创建者id
	String 		title;			//瓶子标签
	String 		content;		//内容
	String 		mood;			//心情
	long 		createTime;		//瓶子创建时间
	long 		lastPickupTime;	//瓶子最后被拾起的时间
	int			replyCount;		//此瓶子的总回复数
	int 		status;			//瓶子状态，1、未被销毁；2、已经销毁
	ArrayList   reply;			//留言信息
	int type;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public long getLastPickupTime() {
		return lastPickupTime;
	}
	public void setLastPickupTime(long lastPickupTime) {
		this.lastPickupTime = lastPickupTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public ArrayList getReply() {
		return reply;
	}
	public void setReply(ArrayList reply) {
		this.reply = reply;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
}
