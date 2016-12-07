package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.spec.farm.FarmNpcWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 绑定灵魂的石头，或者其他石头，在大地图上有特殊功能
 * @datetime:1007-10-24
 */
public class FarmStoneBean extends MapDataBean {
	int id;
	String name;
	String info;
	int type;		// 石头类型
	String value;	// 石头的参数
	List valueList;
	
	public void init() {
		valueList = StringUtil.toIntss(value);
	}

	public String getLink(HttpServletResponse response) {
		return "S" + "<a href=\"" + ("npc/st.jsp?id=" + id) + "\">" + name + "</a>";
	}

	public String getEditLink(HttpServletResponse response) {
		return "S" + "<a href=\"" + ("editStone.jsp?id=" + id) + "\">" + name + "</a>";
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List getValueList() {
		return valueList;
	}

	public void setValueList(List valueList) {
		this.valueList = valueList;
	}
	
	public String getSimpleName() {
		return getName();
	}
}
