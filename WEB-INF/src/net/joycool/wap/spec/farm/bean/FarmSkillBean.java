package net.joycool.wap.spec.farm.bean;

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
 * @explain： 专业的技能
 * @datetime:1007-10-24
 */
public class FarmSkillBean {
	static int FLAG_BATTLE = (1 << 0);
	public static String[] flagString = {"战斗技能"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	int id;
	String name;		// 技能名称
	String info;		// 技能介绍
	int rank;			// 需要等级
	int proNpc;			// 是否从专业npc那里可以学 0表示可以，该属性暂时废弃
	int price;			// 学习需要的钱
	int proId;			// 所属专业
	int class1;			// 所属职业
	int type;			// 技能类型 0 制造技能，用一些材料生成一些物品
	
	int element;		// 五行
	int flag;			// 标志
	
	long cooldown;		// 冷却时间，单位是秒
	int cooldownId;		// 冷却id
	
	String material;	// 材料
	String product;		// 产品
	String cost;		// 消耗 例如0-2就是消耗2金币
	String effect;
	
	List materialList;
	List productList;
	List costList;
	List effectList;
	
	static int[] materialDef = {0, 1};
	static int[] productDef = {0, 1};
	static int[] costDef = {0, 0};
	
	// 根据数据库读入的内容初始化
	public void init() {
		materialList = StringUtil.toIntss(material, 2, materialDef);
		productList = StringUtil.toIntss(product, 2, productDef);
		costList = StringUtil.toIntss(cost, 2, costDef);
		effectList = StringUtil.toIntss(effect);
	}
	
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	public boolean isFromProNpc() {
		return proNpc == 0;
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
	 * @return Returns the price.
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price The price to set.
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @return Returns the proId.
	 */
	public int getProId() {
		return proId;
	}
	/**
	 * @param proId The proId to set.
	 */
	public void setProId(int proId) {
		this.proId = proId;
	}
	/**
	 * @return Returns the proNpc.
	 */
	public int getProNpc() {
		return proNpc;
	}
	/**
	 * @param proNpc The proNpc to set.
	 */
	public void setProNpc(int proNpc) {
		this.proNpc = proNpc;
	}
	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}
	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return Returns the cost.
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param cost The cost to set.
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}

	/**
	 * @return Returns the material.
	 */
	public String getMaterial() {
		return material;
	}

	/**
	 * @param material The material to set.
	 */
	public void setMaterial(String material) {
		this.material = material;
	}

	/**
	 * @return Returns the product.
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product The product to set.
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return Returns the costList.
	 */
	public List getCostList() {
		return costList;
	}

	/**
	 * @return Returns the materialList.
	 */
	public List getMaterialList() {
		return materialList;
	}

	/**
	 * @return Returns the productList.
	 */
	public List getProductList() {
		return productList;
	}

	/**
	 * @return Returns the cooldown.
	 */
	public long getCooldown() {
		return cooldown;
	}
	public int getCooldownMinute() {
		return (int)(cooldown / 60000);
	}
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	/**
	 * @param cooldown The cooldown to set.
	 * 以分钟为单位，转换为毫秒
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown * 60000;
	}

	/**
	 * @return Returns the cooldownId.
	 */
	public int getCooldownId() {
		return cooldownId;
	}

	/**
	 * @param cooldownId The cooldownId to set.
	 */
	public void setCooldownId(int cooldownId) {
		this.cooldownId = cooldownId;
	}

	public int getClass1() {
		return class1;
	}

	public void setClass1(int class1) {
		this.class1 = class1;
	}

	public int getElement() {
		return element;
	}

	public void setElement(int element) {
		this.element = element;
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
	public boolean isFlagBattle() {
		return (flag & FLAG_BATTLE) != 0;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public List getEffectList() {
		return effectList;
	}
}
