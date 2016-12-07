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
 * @explain： 驿站
 * @datetime:1007-10-24
 */
public class FarmCarBean {
	static int FLAG_NOSTOP = (1 << 0);		// 不可以中途下车
	
	public static String[] flagString = {"禁止下车"};
	public static int FLAG_COUNT = flagString.length;
	
	int id;
	String name;
	String info;
	String line;		// 航线结点id列表
	List lineList;
	int money;			// 需要的铜板
	int questId;		// 先修任务，完成后才能使用这个驿站，如果是0表示无先修
	int flag;
	int cooldown;		// 每个结点的停留时间
	
	public int getStartPos() {
		if(lineList.size() == 0)
			return 0;
		return ((Integer)lineList.get(0)).intValue();
	}
	public int getEndPos() {
		if(lineList.size() == 0)
			return 0;
		return ((Integer)lineList.get(lineList.size() - 1)).intValue();
	}
	public int getPos(int carPos) {
		if(lineList.size() <= carPos)
			return 0;
		return ((Integer)lineList.get(carPos)).intValue();
	}
	
	public void init() {
		lineList = StringUtil.toInts(line);
	}
	
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagNoStop() {
		return (flag & FLAG_NOSTOP) != 0;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
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

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public List getLineList() {
		return lineList;
	}

	public void setLineList(List lineList) {
		this.lineList = lineList;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getLineCount() {
		return lineList.size();
	}

}
