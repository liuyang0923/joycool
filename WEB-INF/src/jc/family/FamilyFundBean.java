package jc.family;

import java.util.Date;

/**
 * 家族基金使用记录
 * @author qiuranke
 *
 */
public class FamilyFundBean {

	private int id;
	private int fm_id;
	private int userid;
	private String username;
	private String event;
	private Date event_time;
	private int fm_State;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFm_id() {
		return fm_id;
	}

	public void setFm_id(int fm_id) {
		this.fm_id = fm_id;
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

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Date getEvent_time() {
		return event_time;
	}

	public void setEvent_time(Date event_time) {
		this.event_time = event_time;
	}

	public int getFm_State() {
		return fm_State;
	}

	public void setFm_State(int fm_State) {
		this.fm_State = fm_State;
	}
}
