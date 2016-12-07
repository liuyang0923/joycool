package net.joycool.wap.bean.pk;

import net.joycool.wap.action.pk.PKAction;

/**
 * @author macq
 * @datetime 2007-1-30 上午09:58:02
 * @explain
 */
public class PKUserSkillBean extends PKProtoBean{
	String description;

	String description1;

	String description2;

	int gradeCount;

	String aggressGrowthRadix;

	int weaponType;

	String picture;

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
	 * @return aggressGrowthRadix
	 */
	public String getAggressGrowthRadix() {
		return aggressGrowthRadix;
	}

	/**
	 * @param aggressGrowthRadix
	 *            要设置的 aggressGrowthRadix
	 */
	public void setAggressGrowthRadix(String aggressGrowthRadix) {
		this.aggressGrowthRadix = aggressGrowthRadix;
	}

	/**
	 * @return gradeCount
	 */
	public int getGradeCount() {
		return gradeCount;
	}

	/**
	 * @param gradeCount
	 *            要设置的 gradeCount
	 */
	public void setGradeCount(int gradeCount) {
		this.gradeCount = gradeCount;
	}

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
	 * @return weaponType
	 */
	public int getWeaponType() {
		return weaponType;
	}

	/**
	 * @param weaponType
	 *            要设置的 weaponType
	 */
	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}

	/**
	 * @return description1
	 */
	public String getDescription1() {
		return description1;
	}

	/**
	 * @param description1
	 *            要设置的 description1
	 */
	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	/**
	 * @return description2
	 */
	public String getDescription2() {
		return description2;
	}

	/**
	 * @param description2
	 *            要设置的 description2
	 */
	public void setDescription2(String description2) {
		this.description2 = description2;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain： 技能属性信息
	 * @datetime:2007-3-8 13:51:47
	 * @return
	 * @return String
	 */
	public String toDetail() {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/skill/"
				+ id + ".gif\" alt=\"技能图片\"/>");
		sb.append("<br/>");
		sb.append("技能名称:");
		sb.append(name);
		sb.append("<br/>");
		sb.append("出售价格:");
		sb.append(price);
		sb.append("<br/>");
		return sb.toString();
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：
	 * @datetime:2007-3-19 9:30:12
	 * @return
	 * @return String
	 */
	public String toDetail1() {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/skill/"
				+ id + ".gif\" alt=\"技能图片\"/>");
		sb.append("<br/>");
		sb.append("技能名称:");
		sb.append(name);
		sb.append("<br/>");
		sb.append("购买价格:");
		sb.append(price);
		sb.append("<br/>");
		return sb.toString();
	}


}
