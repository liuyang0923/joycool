package net.joycool.wap.spec.farm.bean;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.spec.farm.FarmAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： farm 触发器，触发剧情
 * @datetime:1007-10-24
 */
public class TriggerBean {
	static int FLAG_REDO = (1 << 0);			// 可重复触发
	static int FLAG_TIME_REDO = (1 << 1);		// 定时可重复触发
	static int FLAG_CLOSED = (1 << 2);			// 暂时禁用
	public static String[] flagString = {"重复触发", "定时触发", "关闭"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	int id;
	int flag;
	String name;		// 名称
	String bak;			// 备注
	String info;		// 触发后给用户显示
	long interval;		// 触发间隔，定期触发的时候用

	String event;		// 触发事件
	String condition;	// 触发条件
	String action;		// 执行
	
	List eventList;
	List conditionList;
	List actionList;
	
	public void init() {
		eventList = StringUtil.toIntss(event);
		conditionList = StringUtil.toIntss(condition);
		actionList = StringUtil.toIntss(action);
	}
	
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagRedo() {
		return (flag & FLAG_REDO) != 0;
	}
	public boolean isFlagClosed() {
		return (flag & FLAG_CLOSED) != 0;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public List getActionList() {
		return actionList;
	}


	public void setActionList(List actionList) {
		this.actionList = actionList;
	}


	public String getBak() {
		return bak;
	}


	public void setBak(String bak) {
		this.bak = bak;
	}


	public String getCondition() {
		return condition;
	}


	public void setCondition(String condition) {
		this.condition = condition;
	}


	public List getConditionList() {
		return conditionList;
	}


	public void setConditionList(List conditionList) {
		this.conditionList = conditionList;
	}


	public String getEvent() {
		return event;
	}


	public void setEvent(String event) {
		this.event = event;
	}


	public List getEventList() {
		return eventList;
	}


	public void setEventList(List eventList) {
		this.eventList = eventList;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public long getInterval() {
		return interval;
	}

	public int getIntervalMinute() {
		return (int)(interval / 60000);
	}
	public void setInterval(int interval) {
		this.interval = interval * 60000;
	}

}
