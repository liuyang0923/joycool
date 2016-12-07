package net.joycool.wap.spec.mentalpic;

public class MentalPicQuestion {
	int id;
	String title;
	String content;
	String contentPic;
	int del;
	int flag;			// 0:看字选图类  1:看图选字类
	long createTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getContentPic() {
		return contentPic;
	}
	public void setContentPic(String contentPic) {
		this.contentPic = contentPic;
	}
	public int getDel() {
		return del;
	}
	public void setDel(int del) {
		this.del = del;
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
}
