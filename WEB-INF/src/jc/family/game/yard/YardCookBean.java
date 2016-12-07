package jc.family.game.yard;

/**
 * 正在烹饪
 * @author maning
 *
 */
public class YardCookBean {
	int id;
	int fmid;
	long createTime;
	int recipeid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFmid() {
		return fmid;
	}
	public void setFmid(int fmid) {
		this.fmid = fmid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getRecipeid() {
		return recipeid;
	}
	public void setRecipeid(int recipeid) {
		this.recipeid = recipeid;
	}
}
