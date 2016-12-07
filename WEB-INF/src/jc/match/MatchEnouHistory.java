package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 宣言历史记录
 */
public class MatchEnouHistory {
	int id;
	int userId;
	String enounce;
	long createTime;
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
	public String getEnounce() {
		return enounce;
	}
	public String getEnounceWml() {
		return StringUtil.toWml(enounce);
	}
	public void setEnounce(String enounce) {
		this.enounce = enounce;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
