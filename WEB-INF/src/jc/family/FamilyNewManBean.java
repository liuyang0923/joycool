package jc.family;

import java.util.Date;

public class FamilyNewManBean {

	private int id;
	private int fm_id;
	private int userid;
	private String username;
	private Date _time;

	private String invitename;
	private String fm_name;

	public FamilyNewManBean() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFm_id() {
		return fm_id;
	}

	public void setFm_id(int fmId) {
		fm_id = fmId;
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

	public Date get_time() {
		return _time;
	}

	public void set_time(Date time) {
		_time = time;
	}

	public String getFm_name() {
		return fm_name;
	}

	public void setFm_name(String fmName) {
		fm_name = fmName;
	}

	public String getInvitename() {
		return invitename;
	}

	public void setInvitename(String invitename) {
		this.invitename = invitename;
	}
}