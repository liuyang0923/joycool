package net.joycool.wap.bean.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.joycool.wap.util.StringUtil;

/**
 * @author bomb
 * 物品的特殊图片显示
 */

public class ShowBean {

	int id;
	int itemId;		// 物品的id
	String name;	// 图片alt=?
	String intro;
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
	 * @return Returns the itemId.
	 */
	public int getItemId() {
		return itemId;
	}
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
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
	 * @return Returns the intro.
	 */
	public String getIntro() {
		return intro;
	}
	/**
	 * @param intro The intro to set.
	 */
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
}
