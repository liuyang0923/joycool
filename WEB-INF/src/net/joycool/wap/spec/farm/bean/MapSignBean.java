package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： farm 路标
 * @datetime:1007-10-24
 */
public class MapSignBean {
	
	static int FLAG_HIDE = (1 << 0);		// 隐藏，不可见
	
	public static String[] flagString = {"隐藏"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	int id;
	String name;		// 名称
	String info;		// 介绍
	int posId;			// 所在位置的地图结点id
	MapNodeBean node;	// 所在位置的地图结点
	int flag;	// 标志位
	int distance;		// 距离多远可以看到（是x*x+y*y)
	
	// 在某个位置是否可见
	public boolean isVisible(MapNodeBean from) {
		if(isFlagHide())
			return false;
		return (from.x - node.x) * (from.x - node.x) + (from.y - node.y) * (from.y - node.y) < distance + 0.01f;		// 浮点精度总是不够的
	}
	
	public void init() {
		
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagHide() {
		return (flag & FLAG_HIDE) != 0;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public MapNodeBean getNode() {
		return node;
	}
	public void setNode(MapNodeBean node) {
		this.node = node;
	}
	public int getPosId() {
		return posId;
	}
	public void setPosId(int posId) {
		this.posId = posId;
	}
}
