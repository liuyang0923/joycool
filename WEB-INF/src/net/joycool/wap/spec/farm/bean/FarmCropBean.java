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
 * @explain： 采集的作物
 * @datetime:1007-10-24
 */
public class FarmCropBean {
	int id;
	int proId;			// 对应的专业
	String name;		// 作物名称
	int growTime;		// 生长需要的时间，单位秒
	int rotTime;		// 多少时间后腐烂，收成减少，加倍后消失
	int rank;			// 需要等级
	int product;		// 产品
	int[] extraProduct;	// 附加产品，随机
	int actInterval;	// 这个庄稼的灌溉周期
	int productCount;	// 灌溉次数除以productCount等于真正的收获
	/**
	 * @return Returns the actInterval.
	 */
	public int getActInterval() {
		return actInterval;
	}
	/**
	 * @param actInterval The actInterval to set.
	 */
	public void setActInterval(int actInterval) {
		this.actInterval = actInterval;
	}
	/**
	 * @return Returns the extraProduct.
	 */
	public int[] getExtraProduct() {
		return extraProduct;
	}
	/**
	 * @param extraProduct The extraProduct to set.
	 */
	public void setExtraProduct(int[] extraProduct) {
		this.extraProduct = extraProduct;
	}
	/**
	 * @return Returns the growTime.
	 */
	public int getGrowTime() {
		return growTime;
	}
	/**
	 * @param growTime The growTime to set.
	 */
	public void setGrowTime(int growTime) {
		this.growTime = growTime;
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
	public int getProduct() {
		return product;
	}
	/**
	 * @param product The product to set.
	 */
	public void setProduct(int product) {
		this.product = product;
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
	 * @return Returns the rotTime.
	 */
	public int getRotTime() {
		return rotTime;
	}
	/**
	 * @param rotTime The rotTime to set.
	 */
	public void setRotTime(int rotTime) {
		this.rotTime = rotTime;
	}
	/**
	 * @return Returns the productCount.
	 */
	public int getProductCount() {
		return productCount;
	}
	/**
	 * @param productCount The productCount to set.
	 */
	public void setProductCount(int productCount) {
		this.productCount = productCount;
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
}
