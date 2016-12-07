package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.spec.farm.FarmNpcWorld;
import net.joycool.wap.spec.farm.FarmWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 一个简单的链接的物体
 * @datetime:1007-10-24
 */
public class SimpleDataBean extends MapDataBean {
	int id;
	String name;
	String link;
	int type;		// 类型

	public String getLink(HttpServletResponse response) {
		return "g" + "<a href=\"" + (link) + "\">" + name + "</a>";
	}

	public String getEditLink(HttpServletResponse response) {
		return "g" + "<a href=\"" + ("editSimpleData.jsp?id=" + id) + "\">" + name + "</a>";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public static void removeOld(int pos) {
		MapNodeBean node = FarmWorld.one.getMapNode(pos);
		if(node == null)
			return;
		synchronized(node.getObjs()) {
			Iterator iter = node.getObjs().iterator();
			while(iter.hasNext()) {
				Object obj = iter.next();
				if(obj instanceof SimpleDataBean)
					iter.remove();
			}
		}
	}
	public static void addLink(String name, String link, int pos, boolean bRemoveOld) {
		if(bRemoveOld)
			removeOld(pos);
		SimpleDataBean sd = new SimpleDataBean();
		sd.setName(name);
		sd.setLink(link);
		sd.setPos(pos);
		FarmWorld.nodeAddObj(sd);
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
