package net.joycool.wap.spec.mental;

public class MentalUser {
	int userId;
	String answer;
	long createTime;
	int flag;
	int queNow;		// 最后一次做的题目ID。+1 就是下一题的ID了。
	public int getQueNow() {
		return queNow;
	}
	public void setQueNow(int queNow) {
		this.queNow = queNow;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
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
