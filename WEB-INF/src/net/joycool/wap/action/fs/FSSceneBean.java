package net.joycool.wap.action.fs;

import java.util.HashMap;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-26 15:40:56
 */
public class FSSceneBean {
	int id;

	String description;

	String picture;

	// 黑市事件
	int blackEvent;

	// 特殊事件
	int specialEvent;

	// 场景内物品列表明细
	HashMap sceneProductMap = new HashMap();;

	/**
	 * @return 返回 sceneProductMap。
	 */
	public HashMap getSceneProductMap() {
		return sceneProductMap;
	}

	/**
	 * @param sceneProductMap
	 *            要设置的 sceneProductMap。
	 */
	public void setSceneProductMap(HashMap sceneProductMap) {
		this.sceneProductMap = sceneProductMap;
	}

	/**
	 * @return 返回 description。
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            要设置的 description。
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 picture。
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            要设置的 picture。
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
	 * @return 返回 blackEvent。
	 */
	public int getBlackEvent() {
		return blackEvent;
	}

	/**
	 * @param blackEvent
	 *            要设置的 blackEvent。
	 */
	public void setBlackEvent(int blackEvent) {
		this.blackEvent = blackEvent;
	}

	/**
	 * @return 返回 specialEvent。
	 */
	public int getSpecialEvent() {
		return specialEvent;
	}

	/**
	 * @param specialEvent
	 *            要设置的 specialEvent。
	 */
	public void setSpecialEvent(int specialEvent) {
		this.specialEvent = specialEvent;
	}

}
