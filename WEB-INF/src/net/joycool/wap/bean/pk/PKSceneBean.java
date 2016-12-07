package net.joycool.wap.bean.pk;

import net.joycool.wap.action.pk.PKAction;

/**
 * @author macq
 * @datetime 2007-1-30 上午10:00:59
 * @explain
 */
public class PKSceneBean {
	int id;

	String name;

	String description;

	String monster;

	int maxRoleCount;
	
	String picture;

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            要设置的 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return maxRoleCount
	 */
	public int getMaxRoleCount() {
		return maxRoleCount;
	}

	/**
	 * @param maxRoleCount
	 *            要设置的 maxRoleCount
	 */
	public void setMaxRoleCount(int maxRoleCount) {
		this.maxRoleCount = maxRoleCount;
	}

	/**
	 * @return monster
	 */
	public String getMonster() {
		return monster;
	}

	/**
	 * @param monster
	 *            要设置的 monster
	 */
	public void setMonster(String monster) {
		this.monster = monster;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 picture。
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture 要设置的 picture。
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：展示场景图片
	 * @datetime:2007-3-14 9:51:33
	 * @return
	 * @return String
	 */
	public String toImage() {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/scene/"
				+ id + ".gif\" alt=\"场景图片\"/>");
		sb.append("<br/>");
		return sb.toString();
	}
}
