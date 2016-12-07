package jc.family.game.pvz;

import net.joycool.wap.util.StringUtil;

/**
 * 植物or僵尸(数据库)
 * 
 * @author qiuranke
 * 
 */
public class PVZ {

	public static int plant = 0;

	public static int zombie = 1;

	int id;// 类型

	String name;// 名字

	int attack;// 攻击力

	int attackspace;// 攻击范围

	int attackmun;// 攻击人数

	int attackCd;// 攻击间隔

	int maxHp;// 最大血量

	int price;// 价格

	int move;// 移动能力

	int moveCd;// 移动间隔

	String pic;// 图片

	String describe;// 描述

	int features;// 特点

	int type;// 0 植物 1僵尸

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getNameToWml() {
		return StringUtil.toWml(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getAttackspace() {
		return attackspace;
	}

	public void setAttackspace(int attackspace) {
		this.attackspace = attackspace;
	}

	public int getAttackmun() {
		return attackmun;
	}

	public void setAttackmun(int attackmun) {
		this.attackmun = attackmun;
	}

	public int getAttackCd() {
		return attackCd;
	}

	public void setAttackCd(int attackCd) {
		this.attackCd = attackCd;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public int getMoveCd() {
		return moveCd;
	}

	public void setMoveCd(int moveCd) {
		this.moveCd = moveCd;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getFeatures() {
		return features;
	}

	public void setFeatures(int features) {
		this.features = features;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 取前两个字符
	 */
	public String toString() {
		if (name.length() <= 2) {
			return name;
		}
		return name.substring(0, 2);
	}
}
