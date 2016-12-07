package net.joycool.wap.bean;

public class SmsBean {
	int id;
	String mobile;
	String content;
	String line;		// 用户上行的号码后缀，如果是空表示短信猫，如果是5开头的表示信息机
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile The mobile to set.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}

	
}
