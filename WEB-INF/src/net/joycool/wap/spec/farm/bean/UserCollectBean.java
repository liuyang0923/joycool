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
 * @explain： farm 玩家的收藏品
 * @datetime:1007-10-24
 */
public class UserCollectBean {
	int id;
	int userId;
	int collectId;
	int count;		// 收藏数量
	int collected;	// 收藏标志，二进制保存是否收藏
	long startTime;
	long finishTime;	// 开始收藏时间和收藏完成时间
	
	// 判断是否收藏了某个物品
	public boolean hasCollect(int index) {
		return ((1 << index) & collected) != 0;
	}
	
	public boolean addCollect(int index) {
		if(!hasCollect(index)) {
			collected |= 1 << index;
			count++;
			return true;
		}
		return false;
	}
	
	public long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public int getCollected() {
		return collected;
	}
	public void setCollected(int collected) {
		this.collected = collected;
	}
	public int getCollectId() {
		return collectId;
	}
	public void setCollectId(int collectId) {
		this.collectId = collectId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
