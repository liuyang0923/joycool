package net.joycool.wap.bean.jcforum;

/**
 * 推荐的精品贴
 */
public class ForumRcmdBean {
	int id;
	int contentId;
	long rcmdTime;
	public long getRcmdTime() {
		return rcmdTime;
	}
	public void setRcmdTime(long rcmdTime) {
		this.rcmdTime = rcmdTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
}
