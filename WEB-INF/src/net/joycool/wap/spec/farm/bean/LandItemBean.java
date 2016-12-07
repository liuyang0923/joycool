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
 * @explain： 采集的物品
 * @datetime:1007-10-24
 */
public class LandItemBean {
	int id;
	int itemId;			// 得到的物品id
	int rank;			// 需要的采集专业等级
	int proId;			// 需要的采集专业
	int min;
	int max;			// 出现物品的最大个数，每次刷新取随机
	String name;		// 拣起来之前的名字
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
	 * @return Returns the itemId.
	 */
	public int getItemId() {
		return itemId;
	}
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return Returns the max.
	 */
	public int getMax() {
		return max;
	}
	/**
	 * @param max The max to set.
	 */
	public void setMax(int max) {
		this.max = max;
	}
	/**
	 * @return Returns the min.
	 */
	public int getMin() {
		return min;
	}
	/**
	 * @param min The min to set.
	 */
	public void setMin(int min) {
		this.min = min;
	}
	/**
	 * @return Returns the proId.
	 */
	public int getProId() {
		return proId;
	}
	/**
	 * @param proId The proId to set.
	 */
	public void setProId(int proId) {
		this.proId = proId;
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
	public int getRandomCount() {
		return RandomUtil.nextInt(min, max);
	}
	
}
