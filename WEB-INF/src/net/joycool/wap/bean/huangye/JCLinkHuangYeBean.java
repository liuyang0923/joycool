package net.joycool.wap.bean.huangye;

public class JCLinkHuangYeBean {
	int id;

	int number;

	int linkId;

	int mark;

	String enterDateTime;
	
	String linkURL;
	
	String linkName;

	/**
	 * @return Returns the linkURL.
	 */
	public String getLinkURL() {
		return linkURL;
	}

	/**
	 * @param linkURL The linkURL to set.
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	public String getEnterDateTime() {
		return enterDateTime;
	}

	public void setEnterDateTime(String enterDateTime) {
		this.enterDateTime = enterDateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
}
