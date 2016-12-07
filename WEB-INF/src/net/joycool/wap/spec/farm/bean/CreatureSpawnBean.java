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
 * @explain： farm 生物 的随机生长
 * @datetime:1007-10-24
 */
public class CreatureSpawnBean {
	static int FLAG_RANDOM_POS = (1 << 0);		// 随机出现（如果不是，则每个点都刷新一个）
	static int FLAG_CLOSED = (1 << 1);
	
	public static String[] flagString = {"随机出现", "关闭"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	
	int id;
	int templateId;		// 模板id
	String pos;			// 生长的地点
	List posList;		// 生长的地点
	int count;			// 生成的怪物数量
	int cooldown;		// 复活间隔
	int flag;
	
	HashSet creatureList = new HashSet();		// 存放已经生成的怪物

	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagRandomPos() {
		return (flag & FLAG_RANDOM_POS) != 0;
	}
	public boolean isFlagClosed() {
		return (flag & FLAG_CLOSED) != 0;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void init() {
		posList = StringUtil.toInts2(pos);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List getPosList() {
		return posList;
	}

	public void setPosList(List posList) {
		this.posList = posList;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public HashSet getCreatureList() {
		return creatureList;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
}
