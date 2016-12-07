package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.joycool.wap.spec.farm.FarmUserBean;
import net.joycool.wap.spec.farm.FarmWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： farm 地图的每个结点
 * @datetime:1007-10-24
 */
public class MapNodeBean {
	int id;
	String name;		// 名称
	String info;		// 介绍
	int mapId;			// 所属的map
	String link;		// 相邻的结点，格式 方向-结点id
	int exp;			// 发现这个地点的经验值
	MapNodeBean[] links = null;	// 八个方向的结点
	List objs = new LinkedList();
	List players = new LinkedList();
	LinkedList drops = null;
	public int x, y;		// 结点坐标
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	// 添加掉落物品
	public synchronized void addDrop(Object obj) {
		if(drops == null) {
			drops = new LinkedList();
		}
		drops.addFirst(obj);
	}
	public synchronized void removeDrop(Object obj) {
		if(drops != null) {
			drops.remove(obj);
		}
	}
	public List getDrops() {
		return drops;
	}
	public int getDropCount() {
		if(drops == null)
			return 0;
		return drops.size();
	}
	
	public synchronized void addPlayer(FarmUserBean obj) {
		synchronized(players) {
			players.add(obj);
		}
	}
	public synchronized boolean removePlayer(FarmUserBean obj) {
		synchronized(players) {
			return players.remove(obj);
		}
	}
	public List getPlayers() {
		return players;
	}
	public synchronized void addObj(Object obj) {
		synchronized(objs) {
			objs.add(obj);
		}
	}
	public synchronized boolean removeObj(Object obj) {
		synchronized(objs) {
			return objs.remove(obj);
		}
	}
	public List getObjs() {
		return objs;
	}
	public void init() {
		
	}
	public void initLink(FarmWorld world) {
		links = new MapNodeBean[9];
		List l = StringUtil.toIntss(link, 2, null);
		for(int j = 0;j < l.size();j++) {
			int[] tmp = (int[])l.get(j);
			setLinks(tmp[0] - 1, world.getMapNode(tmp[1]));
		}
	}
	
	public void addLink(int dir, int id) {
		if(links == null)
			links = new MapNodeBean[9];
		else if(links[dir] != null)
			return;
		if(link == null)
			link = "";
		else if(link.length() > 0)
			link += ",";
		link += (dir + 1) + "-" + id;
	}
	
	public void delLink(int dir) {
		if(links[dir] == null)
			return;
		links[dir] = null;
		String newLink = "";
		boolean first = true;
		for(int i=0;i<links.length;i++)
			if(links[i] != null) {
				if(!first)
					newLink += ",";
				newLink += (i + 1) + "-" + links[i].getId(); 
				first = false;
			}
		link = newLink;
	}
	
	public MapNodeBean[] getLinks() {
		return links;
	}
	public MapNodeBean getLinks(int dir) {
		if(dir >= 0 && dir <= 8)
			return links[dir];
		return null;
	}
	
	public void setLinks(int dir, MapNodeBean node) {
		if(dir >= 0 && dir <= 8)
			links[dir] = node;
	}
	/**
	 * @return Returns the mapId.
	 */
	public int getMapId() {
		return mapId;
	}
	/**
	 * @param mapId The mapId to set.
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
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
	// 如果有行人以外的物体，显示"@"
	public String getName2() {
		if(objs.size() > 0) {
			for(int i = 0;i < objs.size();i++) {
				Object obj = objs.get(i);
				if(obj instanceof MapDataBean) {
					if(((MapDataBean)obj).isVisible())
						return name + "@";
				} else {
					return name + "@";
				}
			}
			return name;
		}
		return name;
	}
	public String getNoName() {
		if(objs.size() > 0) {
			for(int i = 0;i < objs.size();i++) {
				Object obj = objs.get(i);
				if(obj instanceof MapDataBean) {
					if(((MapDataBean)obj).isVisible())
						return "@";
				} else {
					return "@";
				}
			}
			return "";
		}
		return "";
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the link.
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link The link to set.
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return Returns the exp.
	 */
	public int getExp() {
		return exp;
	}
	/**
	 * @param exp The exp to set.
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getObjCount() {
		return objs.size();
	}
	
	public String getObjNames() {
		String n = "";
		for(int i = 0;i < objs.size();i++) {
			if(i > 0)
				n += ",";
			Object obj = objs.get(i);
			if(obj instanceof MapDataBean) {
				n += ((MapDataBean)obj).getSimpleName();
			} else if(obj instanceof FarmNpcBean) {
				n += "N" + ((FarmNpcBean)obj).getName();
			}
		}
		return n;
	}
}
