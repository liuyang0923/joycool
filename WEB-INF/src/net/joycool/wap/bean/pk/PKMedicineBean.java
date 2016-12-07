package net.joycool.wap.bean.pk;

import net.joycool.wap.action.pk.PKAction;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-7 14:01:52
 */
public class PKMedicineBean extends PKProtoBean {

	String description;

	int grade;

	int physicalGrowth;

	int energyGrowth;

	int aggressGrowth;

	int recoveryGrowth;

	int skillGrowth;

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
	 * @return 返回 aggressGrowth。
	 */
	public int getAggressGrowth() {
		return aggressGrowth;
	}

	/**
	 * @param aggressGrowth
	 *            要设置的 aggressGrowth。
	 */
	public void setAggressGrowth(int aggressGrowth) {
		this.aggressGrowth = aggressGrowth;
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
	 * @return 返回 energyGrowth。
	 */
	public int getEnergyGrowth() {
		return energyGrowth;
	}

	/**
	 * @param energyGrowth
	 *            要设置的 energyGrowth。
	 */
	public void setEnergyGrowth(int energyGrowth) {
		this.energyGrowth = energyGrowth;
	}

	/**
	 * @return 返回 grade。
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            要设置的 grade。
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * @return 返回 physicalGrowth。
	 */
	public int getPhysicalGrowth() {
		return physicalGrowth;
	}

	/**
	 * @param physicalGrowth
	 *            要设置的 physicalGrowth。
	 */
	public void setPhysicalGrowth(int physicalGrowth) {
		this.physicalGrowth = physicalGrowth;
	}

	/**
	 * @return 返回 recoveryGrowth。
	 */
	public int getRecoveryGrowth() {
		return recoveryGrowth;
	}

	/**
	 * @param recoveryGrowth
	 *            要设置的 recoveryGrowth。
	 */
	public void setRecoveryGrowth(int recoveryGrowth) {
		this.recoveryGrowth = recoveryGrowth;
	}

	/**
	 * @return 返回 skillGrowth。
	 */
	public int getSkillGrowth() {
		return skillGrowth;
	}

	/**
	 * @param skillGrowth
	 *            要设置的 skillGrowth。
	 */
	public void setSkillGrowth(int skillGrowth) {
		this.skillGrowth = skillGrowth;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain： 物品属性信息
	 * @datetime:2007-3-8 13:51:47
	 * @return
	 * @return String
	 */
	public String toDetail() {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/medicine/"
				+ id + ".gif\" alt=\"\"/>");
		sb.append("<br/>");
		sb.append(name);
		sb.append("<br/>");
		sb.append("价格:");
		sb.append(price);
		sb.append("<br/>");
		if(physicalGrowth > 0) {
			sb.append("体力+");
			sb.append(physicalGrowth);
			sb.append("<br/>");
		}
		if(energyGrowth > 0) {
			sb.append("气力+");
			sb.append(energyGrowth);
			sb.append("<br/>");
		}

		int energyGrowth;
		return sb.toString();
	}
}
