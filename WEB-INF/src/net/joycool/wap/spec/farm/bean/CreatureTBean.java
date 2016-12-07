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
 * @explain： farm 生物（怪物和npc）模板
 * @datetime:1007-10-24
 */
public class CreatureTBean {
	public static String[] typeName = {"人形", "野兽", "鬼", "妖", "元素"};
	
	static int FLAG_ELITE = (1 << 0);
	static int FLAG_ATTACK = (1 << 1);
	static int FLAG_NO_LEVEL = (1 << 2);
	static int FLAG_RARE = (1 << 3);
	static int FLAG_NO_HP = (1 << 4);
	static int FLAG_RECOVER = (1 << 5);
	static int FLAG_COLLECT = (1 << 6);
	static int FLAG_ANIMAL = (1 << 7);
	static int FLAG_BOSS = (1 << 8);
	static int FLAG_NO_FLEE = (1 << 9);
	
	public static String[] flagString = {"精英", "主动攻击", "隐藏等级", "稀有","隐藏血","自动回复","收藏品","小动物","无法猛击","无法逃跑"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	int id;
	String name;		// 名称
	String info;		// 介绍
	int level;			// 基础级别
	int levelRange;	// 可变级别，如果是0表示不变
	int hp;
	int mp;
	int attack;
	int defense;
	int type;			// 怪物类型
	int flag;			// 标志
	String drops;		// 特殊掉落
	List dropList;		// 格式：几率-物品id-任务，如果非任务，则省略该项
	
	int cooldown = 2000;	// 攻击速度冷却间隔

	public int getCooldown() {
		return cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public List getDropList() {
		return dropList;
	}
	public void setDropList(List dropList) {
		this.dropList = dropList;
	}
	public String getDrops() {
		return drops;
	}
	public void setDrops(String drops) {
		this.drops = drops;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void init() {
		dropList = StringUtil.toIntss(drops);
	}
	public String getTypeName() {
		return typeName[type];
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevelRange() {
		return levelRange;
	}
	public void setLevelRange(int levelRange) {
		this.levelRange = levelRange;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		this.mp = mp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CreatureBean create() {
		CreatureBean creature = new CreatureBean(this);
		spawn(creature);
		return creature;
	}
	public void spawn(CreatureBean creature) {
		int level = this.level;
		int levelAdd = 0;
		if(levelRange > 1)
			levelAdd = RandomUtil.nextInt(levelRange);

		creature.setLevel(level + levelAdd);
		creature.setHpMax(hp + (int) (hp * levelAdd * 0.1f));
		creature.setMpMax(mp + (int) (mp * levelAdd * 0.1f));
		creature.setAttack(attack + (int) (attack * levelAdd * 0.1f));
		creature.setDefense(defense + (int) (defense * levelAdd * 0.05f));
		if(isFlagElite()) {
			creature.setHpMax(creature.getHpMax() * 3);
			creature.setAttack(creature.getAttack() * 2);
		}
		creature.init();
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagElite() {
		return (flag & FLAG_ELITE) != 0;
	}
	public boolean isFlagAttack() {
		return (flag & FLAG_ATTACK) != 0;
	}
	public boolean isFlagNoLevel() {
		return (flag & FLAG_NO_LEVEL) != 0;
	}
	public boolean isFlagRare() {
		return (flag & FLAG_RARE) != 0;
	}
	public boolean isFlagNoHp() {
		return (flag & FLAG_NO_HP) != 0;
	}
	public boolean isFlagRecover() {
		return (flag & FLAG_RECOVER) != 0;
	}
	public boolean isFlagCollect() {
		return (flag & FLAG_COLLECT) != 0;
	}
	public boolean isFlagAnimal() {
		return (flag & FLAG_ANIMAL) != 0;
	}
	public boolean isFlagBoss() {
		return (flag & FLAG_BOSS) != 0;
	}
	public boolean isFlagNoFlee() {
		return (flag & FLAG_NO_FLEE) != 0;
	}
}
