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
 * @explain： farm 生物（怪物和npc）模板
 * @datetime:1007-10-24
 */
public class ItemSetBean {
	int id;
	String name;		// 名称
	String info;		// 介绍
	String items;		// 套装内容
	List itemList;
	
	String attribute;	// 套装奖励属性
	List attributeList;
	
	String count;	// 套装奖励要求数量
	List countList;
	
	int flag;		// 标志
	
	// 本套装数量
	public int getItemCount() {
		return itemList.size();
	}
	public void init() {
    	attributeList = StringUtil.toIntss(attribute);
    	itemList = StringUtil.toInts(items);
    	countList = StringUtil.toInts(count);
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public List getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List attributeList) {
		this.attributeList = attributeList;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List getItemList() {
		return itemList;
	}

	public void setItemList(List itemList) {
		this.itemList = itemList;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List getCountList() {
		return countList;
	}

	public void setCountList(List countList) {
		this.countList = countList;
	}

}
