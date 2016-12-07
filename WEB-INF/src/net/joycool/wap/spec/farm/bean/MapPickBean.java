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
 * @explain： 大地图采集
 * @datetime:1007-10-24
 */
public class MapPickBean extends MapDataBean{
	int id;
	String name;		// 名称
	String info;		// 描述
	String items;		// 拣到的物品
	int questId;		// 是否要拥有某个任务才有效

	List itemList;

	static int[] itemDef = {0, 1};
	
	// 根据数据库读入的内容初始化
	public void init() {
		itemList = StringUtil.toIntss(items, 2, itemDef);
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

	public String getLink(HttpServletResponse response) {
		return "[" + "<a href=\"" + ("pick.jsp?id=" + id) + "\">" + name + "</a>" + "]";
	}
	public String getEditLink(HttpServletResponse response) {
		return "[" + "<a href=\"" + ("editPick.jsp?id=" + id) + "\">" + name + "</a>" + "]";
	}

	/**
	 * @return Returns the questId.
	 */
	public int getQuestId() {
		return questId;
	}

	/**
	 * @param questId The questId to set.
	 */
	public void setQuestId(int questId) {
		this.questId = questId;
	}

	/**
	 * @return Returns the itemList.
	 */
	public List getItemList() {
		return itemList;
	}

	/**
	 * @param itemList The itemList to set.
	 */
	public void setItemList(List itemList) {
		this.itemList = itemList;
	}

	public String getSimpleName() {
		return name;
	}
}
