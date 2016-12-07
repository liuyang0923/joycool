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
 * @explain： 地图上的元素，宝箱啊书本啊npc啊等等
 * @datetime:1007-10-24
 */
public abstract class MapDataBean extends FarmObject {

	int pos;
	
	public abstract String getLink(HttpServletResponse response);
	public abstract String getEditLink(HttpServletResponse response);
	
	public boolean isVisible() {
		return true;
	}
	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public String getSimpleName() {
		return "@";
	}
		
}
