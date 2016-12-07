package net.joycool.wap.bean.jcforum;

import net.joycool.wap.util.StringUtil;

public class ForumRcmdBean2 {
	int id;
	int forumId;
	String content;
	long rcmdTime;
	int week;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getForumId() {
		return forumId;
	}
	public void setForumId(int forumId) {
		this.forumId = forumId;
	}
	public String getContent() {
		return content;
	}
	public String getContentWml() {
		return StringUtil.toWml(content);
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getRcmdTime() {
		return rcmdTime;
	}
	public void setRcmdTime(long rcmdTime) {
		this.rcmdTime = rcmdTime;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	
}
