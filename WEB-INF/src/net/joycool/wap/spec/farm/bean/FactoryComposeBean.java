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
 * @explain： 加工厂处理公式
 * @datetime:1007-10-24
 */
public class FactoryComposeBean {
	int id;
	int factoryId;
	int rank;
	int price;		// 加工需要的铜板
	int time;		// 需要消耗的时间，单位秒
	String name;
	String info;
	
	String material;	// 材料
	String product;		// 产品
	
	List materialList;
	List productList;
	
	static int[] materialDef = {0, 1};
	static int[] productDef = {0, 1};
	
	// 根据数据库读入的内容初始化
	public void init() {
		materialList = StringUtil.toIntss(material, 2, materialDef);
		productList = StringUtil.toIntss(product, 2, productDef);
	}

	/**
	 * @return Returns the cost.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param cost The cost to set.
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return Returns the factoryId.
	 */
	public int getFactoryId() {
		return factoryId;
	}

	/**
	 * @param factoryId The factoryId to set.
	 */
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
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
	 * @return Returns the materialList.
	 */
	public List getMaterialList() {
		return materialList;
	}

	/**
	 * @param materialList The materialList to set.
	 */
	public void setMaterialList(List materialList) {
		this.materialList = materialList;
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
	 * @return Returns the productList.
	 */
	public List getProductList() {
		return productList;
	}

	/**
	 * @param productList The productList to set.
	 */
	public void setProductList(List productList) {
		this.productList = productList;
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
	 * @return Returns the time.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time The time to set.
	 */
	public void setTime(int time) {
		this.time = time;
	}
}
