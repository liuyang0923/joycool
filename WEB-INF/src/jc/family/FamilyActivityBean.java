package jc.family;

import java.util.Date;

/**
 * 家族动态
 * @author qiuranke
 *
 */
public class FamilyActivityBean {

	private int id;
	private int userid;
	private String username;
	private int fm_id;
	private String fm_name;
	private String movement;
	private Date movement_time;
	private String fm_url;
	private int fm_state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getFm_id() {
		return fm_id;
	}

	public void setFm_id(int fm_id) {
		this.fm_id = fm_id;
	}

	public String getFm_name() {
		return fm_name;
	}

	public void setFm_name(String fm_name) {
		this.fm_name = fm_name;
	}

	public String getMovement() {
		return movement;
	}

	public void setMovement(String movement) {
		this.movement = movement;
	}

	public Date getMovement_time() {
		return movement_time;
	}

	public void setMovement_time(Date movement_time) {
		this.movement_time = movement_time;
	}

	public String getFm_url() {
		return fm_url;
	}

	public void setFm_url(String fm_url) {
		this.fm_url = fm_url;
	}

	public int getFm_state() {
		return fm_state;
	}

	public void setFm_state(int fm_state) {
		this.fm_state = fm_state;
	}

}