package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 用户触发了的触发器
 * @datetime:1007-10-24
 */
public class UserTriggerBean {
	
	int id;
	int triggerId;
	int userId;
	long triggerTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(int triggerId) {
		this.triggerId = triggerId;
	}
	public long getTriggerTime() {
		return triggerTime;
	}
	public void setTriggerTime(long triggerTime) {
		this.triggerTime = triggerTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
