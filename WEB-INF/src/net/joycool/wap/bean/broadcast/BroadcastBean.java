/*
 * Created on 2005-7-26
 *
 */
package net.joycool.wap.bean.broadcast;

import java.sql.Time;


/**
 * @author Bomb
 *
 */
public class BroadcastBean {
	int id;
	String broadcaster;
	String msg;
	Time time;
	/**
	 * @return Returns the broadcaster.
	 */
	public String getBroadcaster() {
		return broadcaster;
	}
	/**
	 * @param broadcaster The broadcaster to set.
	 */
	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
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
	 * @return Returns the msg.
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg The msg to set.
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @return Returns the time.
	 */
	public Time getTime() {
		return time;
	}
	/**
	 * @param time The time to set.
	 */
	public void setTime(Time time) {
		this.time = time;
	}
	
}
