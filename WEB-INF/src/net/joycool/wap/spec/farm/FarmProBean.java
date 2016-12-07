package net.joycool.wap.spec.farm;

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
 * @explain： 用户采集专业
 * @datetime:1007-10-24
 */
public class FarmProBean {
	public static int PRO_BATTLE = 9;		// 战斗级别
	public static int PRO_SKILL_A = 10;		// 攻击技能
	public static int PRO_SKILL_D = 11;		// 防御技能
	public static int PRO_SKILL_C = 14;		// 职业技能
	int id;
	int maxRank;		// 最高等级
	String name;		// 名称
	String info;		// 描述
	int point;			// 专业升级一次所需的点数
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
	 * @return Returns the maxRank.
	 */
	public int getMaxRank() {
		return maxRank;
	}
	/**
	 * @param maxRank The maxRank to set.
	 */
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the point.
	 */
	public int getPoint() {
		return point;
	}
	/**
	 * @param point The point to set.
	 */
	public void setPoint(int point) {
		this.point = point;
	}
	
}
