package jc.match;

/**
 * 动态表
 */
public class MatchTrends {
	int id;
	int leftUid;
	int rightUid;
	String content;
	String link;
	int readed;		// 0：未读 1：已读
	long createTime;
	int flag;
	/*
	 * 关于flag的说明：
	 * 0:赠送类
	 * 1:购买类
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLeftUid() {
		return leftUid;
	}
	public void setLeftUid(int leftUid) {
		this.leftUid = leftUid;
	}
	public int getRightUid() {
		return rightUid;
	}
	public void setRightUid(int rightUid) {
		this.rightUid = rightUid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getReaded() {
		return readed;
	}
	public void setReaded(int readed) {
		this.readed = readed;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
