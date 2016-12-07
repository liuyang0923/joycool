package net.joycool.wap.bean.pk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.joycool.wap.util.StringUtil;

/**
 * @author macq
 * @datetime 2007-1-31 下午01:03:05
 * @explain
 */
public class PKActBean extends PKSceneBean {
	List monsterList;

	// liuyi 2007-02-15 npc列表
	List npcList = new ArrayList();

	Set pkUserList;

	List log;

	int logSize = 10;

	// 打死怪兽后场景内掉落物品列表
	HashMap dropMap = new HashMap();

	/**
	 * @return log
	 */
	public List getLog() {
		return log;
	}

	/**
	 * @param log
	 *            要设置的 log
	 */
	public void setLog(List log) {
		this.log = log;
	}

	/**
	 * @return monsterList
	 */
	public List getMonsterList() {
		return monsterList;
	}

	/**
	 * @param monsterList
	 *            要设置的 monsterList
	 */
	public void setMonsterList(List monsterList) {
		this.monsterList = monsterList;
	}

	/**
	 * @return pkUserList
	 */
	public Set getPkUserList() {
		return pkUserList;
	}

	/**
	 * @param pkUserList
	 *            要设置的 pkUserList
	 */
	public void setPkUserList(Set pkUserList) {
		this.pkUserList = pkUserList;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加场景动态log
	 * @datetime:2007-2-1 下午01:22:16
	 * @param sceneId
	 * @param content
	 * @return
	 */
	public void addLog(String content) {
		synchronized (log) {
			// 判断场景log是否大于50条
			if (log.size() > logSize) {
				log.remove(0);
			}
			// 添加场景log
			log.add(content);
		}
	}

	/**
	 * @param log
	 */
	public String toString(List log) {
		StringBuffer result = new StringBuffer("");
		int listSize = log.size();
//		int startIndex = 0;
//		if (listSize > logSize) {
//			startIndex = listSize - logSize;
//		}
//		for (int i = startIndex; i < listSize; i++) {
//			result.append(StringUtil.toWml((String) log.get(i)));
//			result.append("<br/>");
//		}
		int startIndex = listSize-1;
		int endIndex = 0;
		if(listSize>10){
			endIndex=listSize-10;
		}
		for (int i = startIndex; i >= endIndex; i--) {
			result.append(StringUtil.toWml((String) log.get(i)));
			result.append("<br/>");
		}
		return result.toString();
	}

	public List getNpcList() {
		return npcList;
	}

	public HashMap getDropMap() {
		return dropMap;
	}

	public void setDropMap(HashMap dropMap) {
		this.dropMap = dropMap;
	}
}
