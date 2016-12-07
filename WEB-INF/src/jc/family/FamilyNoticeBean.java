package jc.family;

import java.util.Date;

public class FamilyNoticeBean {
	private int id;
	private int fmid;
	private int userid;
	private String username;
	private String content;
	private Date noticetime;

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

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getNoticetime() {
		return noticetime;
	}

	public void setNoticetime(Date noticetime) {
		this.noticetime = noticetime;
	}

}