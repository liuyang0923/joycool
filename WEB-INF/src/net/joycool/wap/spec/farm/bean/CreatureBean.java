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
 * @explain： farm 生物（怪物和npc）实例
 * @datetime:1007-10-24
 */
public class CreatureBean extends MapDataBean {
	CreatureSpawnBean spawn;	// 怪物生长
	CreatureTBean template;		// 模板
	public int level;			// 级别
	public int hp, hpMax;
	public int mp, mpMax;
	public int attack;		// 攻击强度
	public int defense;
	long cooldown;		// 冷却时间，等于下次可以反击的时间
	long lifeTime;		// 下次复活时间


	public long getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

	public CreatureBean(CreatureTBean bean) {
		template = bean;
	}

	public void init() {
		hp = hpMax;
		mp = mpMax;
	}
	
	public String getName() {
		return template.getName();
	}
	public String getInfo() {
		return template.getInfo();
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMp() {
		return mp;
	}

	public void setMp(int mp) {
		this.mp = mp;
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

	public CreatureTBean getTemplate() {
		return template;
	}

	public void setTemplate(CreatureTBean template) {
		this.template = template;
	}

	public String getLink(HttpServletResponse response) {
		if(isDead())
			return "X" + "<a href=\"" + ("npc/cr.jsp?id=" + gid) + "\">" + template.getName() + "</a>";
		else
			return "m" + "<a href=\"" + ("npc/cr.jsp?id=" + gid) + "\">" + template.getName() + "</a>";
	}
	// 编辑对应的模板
	public String getEditLink(HttpServletResponse response) {
		return "m" + "<a href=\"" + ("editCreature.jsp?id=" + 
				template.getId()) + "\">" + template.getName() + "</a>" + "(等级" + level + ")";
	}

	/**
	 * @return Returns the hpMax.
	 */
	public int getHpMax() {
		return hpMax;
	}

	/**
	 * @param hpMax The hpMax to set.
	 */
	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	/**
	 * @return Returns the mpMax.
	 */
	public int getMpMax() {
		return mpMax;
	}

	/**
	 * @param mpMax The mpMax to set.
	 */
	public void setMpMax(int mpMax) {
		this.mpMax = mpMax;
	}

	public void damage(int damage) {
		hp -= damage;
		if(hp <= 0) {
			hp = 0;
		}
	}
	public void recover(float add) {
		hp += hpMax * add;
		if(hp > hpMax)
			hp = hpMax;
	}
	public boolean isAlive() {
		return hp > 0;
	}
	public boolean isDead() {
		return hp == 0;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	public boolean isCooldown(long now) {
		return cooldown <= now;
	}

	public CreatureSpawnBean getSpawn() {
		return spawn;
	}

	public void setSpawn(CreatureSpawnBean spawn) {
		this.spawn = spawn;
	}
	
	public boolean isVisible() {
		return lifeTime <= FarmAction.now;
	}
	
	public String getSimpleName() {
		return template.getName();
	}
}
