package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.joycool.wap.spec.farm.FarmWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： farm 生物（怪物和npc）模板
 * @datetime:1007-10-24
 */
public class PickTBean {
	public static String[] typeName = {"人形", "野兽", "鬼", "妖", "元素"};
	
	static int FLAG_ELITE = (1 << 0);
	static int FLAG_ATTACK = (1 << 1);
	static int FLAG_NO_LEVEL = (1 << 2);
	static int FLAG_RARE = (1 << 3);
	static int FLAG_NO_HP = (1 << 4);
	static int FLAG_RECOVER = (1 << 5);
	
	public static String[] flagString = {"精英", "主动攻击", "隐藏等级", "稀有","隐藏血","自动回复"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	int id;
	String items;		// 可能出现的物品
	String pos;			// 所有要出现的位置，每个位置都会产生一个pickbean
	
	List itemList;
	List posList;
	
	List pickList = new ArrayList();		// 存放已经生成的物品
	
	int cooldown = 60000;	// 刷新间隔

	public int getCooldown() {
		return cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public void init() {
		itemList = StringUtil.toInts(items);
		posList = StringUtil.toInts(pos);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PickBean create() {
		if(itemList.size() == 0)
			return null;
		PickBean p = new PickBean(this);
		Integer iid = (Integer)RandomUtil.randomObject(itemList);
		LandItemBean litem = FarmWorld.one.getLandItem(iid.intValue());
		p.setLandItem(litem);
		p.spawn();
		return p;
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
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public List getPosList() {
		return posList;
	}
	public void setPosList(List posList) {
		this.posList = posList;
	}
	public List getPickList() {
		return pickList;
	}
	public void setPickList(List pickList) {
		this.pickList = pickList;
	}
}
