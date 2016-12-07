package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： farm 地图
 * @datetime:1007-10-24
 */
public class MapBean {
	
	static int FLAG_PEACE = (1 << 0);		// 安全区域，不能pvp
	static int FLAG_ARENA = (1 << 1);		// 死亡后无惩罚区域：擂台
	static int FLAG_NO_FLEE = (1 << 2);		// 无法逃跑的区域
	static int FLAG_CITY = (1 << 3);		// 属于城市
	static int FLAG_BATTLE = (1 << 4);		// 自动进入战斗，不论如何，门派内不能战争
	static int FLAG_OOO = (1 << 5);		// 只允许两人对战
	
	public static String[] flagString = {"安全区域", "竞技场", "无法逃跑", "城市", "自动战斗", "1v1"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	int id;
	String name;		// 名称
	String info;		// 介绍
	int entryNode;		// 地图入口的结点id
	int x;				// 入口所在的世界坐标
	int y;	
	int exp;
	int flag;	// 标志位
	int rank;	// 该区域的级别限制
	int sp = 2;	// 移动一步消耗的体力
	int cooldown;	// 移动到这个区域后需要多少毫秒才能继续移动
	int attackCount;	// 单次主动攻击怪物上限
	int parent;		// 父mapId
	int maxPlayer;	// 允许最大玩家数量
	String condition;		// 进入本地图的条件
	List conditionList;
	int city;		// 所属的城，可供帮派抢夺
	
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public int getMaxPlayer() {
		return maxPlayer;
	}
	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public void init() {
		conditionList = StringUtil.toIntss(condition);
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
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the entryNode.
	 */
	public int getEntryNode() {
		return entryNode;
	}
	/**
	 * @param entryNode The entryNode to set.
	 */
	public void setEntryNode(int entryNode) {
		this.entryNode = entryNode;
	}
	/**
	 * @return Returns the x.
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagPeace() {
		return (flag & FLAG_PEACE) != 0;
	}
	public boolean isFlagArena() {
		return (flag & FLAG_ARENA) != 0;
	}
	public boolean isFlagNoFlee() {
		return (flag & FLAG_NO_FLEE) != 0;
	}
	public boolean isFlagCity() {
		return (flag & FLAG_CITY) != 0;
	}
	public boolean isFlagBattle() {
		return (flag & FLAG_BATTLE) != 0;
	}
	public boolean isFlagOOO() {
		return (flag & FLAG_OOO) != 0;
	}
	public int getCooldown() {
		return cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public int getAttackCount() {
		return attackCount;
	}
	public void setAttackCount(int attackCount) {
		this.attackCount = attackCount;
	}
	public List getConditionList() {
		return conditionList;
	}
		
}
