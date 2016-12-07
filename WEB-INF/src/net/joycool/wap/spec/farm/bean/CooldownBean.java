package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author zhouj
 * @explain： 冷却时间
 * @datetime:1007-10-24
 */
public class CooldownBean {
	int userId;
	int cooldownId;		// cooldown id
	long time;			// 下一次冷却时间
	/**
	 * @return Returns the cooldownId.
	 */
	public int getCooldownId() {
		return cooldownId;
	}
	/**
	 * @param cooldownId The cooldownId to set.
	 */
	public void setCooldownId(int cooldownId) {
		this.cooldownId = cooldownId;
	}
	/**
	 * @return Returns the time.
	 */
	public long getTime() {
		return time;
	}
	/**
	 * @param time The time to set.
	 */
	public void setTime(long time) {
		this.time = time;
	}
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
