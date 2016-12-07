package net.joycool.wap.action.dhh;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author s
 *
 */
public class DhhCityBean {

	/**
	 * city表的id
	 */
	private int id;
	
	/**
	 * 城市名称
	 */
	private String name;
	
	/**
	 *城市图片 
	 */
	private String image;

	public  List log = new LinkedList();
	
	/**
	 * 这个城市销售的物品
	 */
	private HashMap ProductMap = null;
	
	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id 要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 image。
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image 要设置的 image。
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 productMap。
	 */
	public HashMap getProductMap() {
		return ProductMap;
	}

	/**
	 * @param productMap 要设置的 productMap。
	 */
	public void setProductMap(HashMap productMap) {
		ProductMap = productMap;
	}

	/**
	 * @return 返回 log。
	 */
	public List getLog() {
		return log;
	}

	/**
	 * @param log 要设置的 log。
	 */
	public void setLog(List log) {
		this.log = log;
	}

}
