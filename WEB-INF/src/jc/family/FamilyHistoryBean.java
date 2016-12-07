package jc.family;

import java.util.Date;

public class FamilyHistoryBean {

	private int id;
	private int fm_id;
	private String event;
	private Date event_time;
	private int fm_state;

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

	public int getFm_state() {
		return fm_state;
	}

	public void setFm_state(int fm_state) {
		this.fm_state = fm_state;
	}

}