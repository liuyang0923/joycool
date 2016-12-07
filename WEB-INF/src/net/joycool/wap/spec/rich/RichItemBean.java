package net.joycool.wap.spec.rich;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 大富翁
 * @datetime:1007-10-24
 */
public class RichItemBean {
	int id;
	int price;		// 需要多少点券
	String name;	// 道具名称
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
}
