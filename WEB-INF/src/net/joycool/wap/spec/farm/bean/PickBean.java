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
 * @explain： farm 大地图采集 实例
 * @datetime:1007-10-24
 */
public class PickBean extends MapDataBean {
	PickTBean template;		// 模板
	long lifeTime;		// 这个时间之后才有效
	LandItemBean landItem;		// 这次的物品
	int count;			// 可采集剩余数量

	public void decCount() {
		count--;
		if(count <= 0)
			spawn();
	}
	
	public String getName() {
		return landItem.getName();
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public LandItemBean getLandItem() {
		return landItem;
	}

	public void setLandItem(LandItemBean landItem) {
		this.landItem = landItem;
	}

	public long getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

	public PickTBean getTemplate() {
		return template;
	}

	public void setTemplate(PickTBean template) {
		this.template = template;
	}

	public PickBean(PickTBean bean) {
		template = bean;
	}

	public void init() {
	}
	

	public String getLink(HttpServletResponse response) {
		return "i" + "<a href=\"" + ("pick2.jsp?id=" + gid) + "\">" + landItem.getName() + "</a>";
	}
	// 编辑对应的模板
	public String getEditLink(HttpServletResponse response) {
		return "i" + "<a href=\"" + ("editPick2.jsp?id=" + 
				template.getId()) + "\">" + landItem.getName() + "</a>(" + count + ")";
	}

	public void spawn() {
		lifeTime = FarmAction.now + template.getCooldown();
		count = landItem.getRandomCount();
	}

	public boolean isAlive() {
		return lifeTime <= FarmAction.now;
	}
	public boolean isVisible() {
		return isAlive();
	}
	public String getSimpleName() {
		return "i" + getName();
	}
}
