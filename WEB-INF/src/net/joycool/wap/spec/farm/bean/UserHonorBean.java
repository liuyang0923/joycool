package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.spec.farm.FarmAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： farm 玩家的荣誉，pvp才能得到
 * @datetime:1007-10-24
 */
public class UserHonorBean {
	int id;
	int arena;		// 竞技场的map id，如果是0表示非竞技场
	int userId;
	int honor;			// 累计
	int honorWeek;		// 本周
	int honorLast;		// 上周
	int honorHigh;		// 一周最高
	long createTime;	// 创建时间
	public int getArena() {
		return arena;
	}
	public void setArena(int arena) {
		this.arena = arena;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getHonor() {
		return honor;
	}
	public void setHonor(int honor) {
		this.honor = honor;
	}
	public int getHonorHigh() {
		return honorHigh;
	}
	public void setHonorHigh(int honorHigh) {
		this.honorHigh = honorHigh;
	}
	public int getHonorLast() {
		return honorLast;
	}
	public void setHonorLast(int honorLast) {
		this.honorLast = honorLast;
	}
	public int getHonorWeek() {
		return honorWeek;
	}
	public void setHonorWeek(int honorWeek) {
		this.honorWeek = honorWeek;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
