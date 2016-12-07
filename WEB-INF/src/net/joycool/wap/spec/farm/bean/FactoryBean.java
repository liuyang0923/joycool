package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 加工厂的数据
 * @datetime:1007-10-24
 */
public class FactoryBean extends MapDataBean{
	int id;
	String name;		// 名称
	String info;		// 介绍
	int rank;			// 需要等级
	int interval;		// 加工间隔，单位是分钟
	List processList = new LinkedList();		// 正在处理的加工
	
	int timeLeft = 0;		// 上次运算剩余的时间，用于定时制造加工
	
	// 增加剩余时间
	public void growTime() {
		timeLeft += interval * 60;
	}
	
	// 耗费工厂制造剩余时间
	public void decTime(int dec) {
		timeLeft -= dec;
	}
	
	// 得到未完成数量
	public int getUndoneCount() {
		return processList.size();
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
	 * @return Returns the info.
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * @param info The info to set.
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}
	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	/**
	 * @return Returns the processList.
	 */
	public List getProcessList() {
		return processList;
	}
	public void addProc(FactoryProcBean proc) {
		processList.add(proc);
	}
	/**
	 * @return Returns the interval.
	 */
	public int getInterval() {
		return interval;
	}
	/**
	 * @param interval The interval to set.
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	/**
	 * @return Returns the timeLeft.
	 */
	public int getTimeLeft() {
		return timeLeft;
	}
	/**
	 * @param timeLeft The timeLeft to set.
	 */
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public String getLink(HttpServletResponse response) {
		return "F" + "<a href=\"" + ("npc/factory.jsp?id=" + id) + "\">" + name + "</a>";
	}
	public String getEditLink(HttpServletResponse response) {
		return "F" + "<a href=\"" + ("editFactory.jsp?id=" + id) + "\">" + name + "</a>";
	}
		
}
