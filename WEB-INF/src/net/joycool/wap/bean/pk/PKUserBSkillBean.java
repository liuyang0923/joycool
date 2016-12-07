package net.joycool.wap.bean.pk;

import net.joycool.wap.action.pk.PKAction;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-9 15:05:43
 */
public class PKUserBSkillBean extends PKProtoBean {

	String description;

	int grade;

	int[] physicalGrowthRadix;

	int[] physicalMaxRadix;

	int[] energyGrowthRadix;

	int[] energyMaxRadix;

	int[] flyingGrowthRadix;

	int[] flyingMaxRadix;

	int[] aggressGrowthRadix;

	int[] aggressMaxRadix;

	int[] recoveryGrowthRadix;

	int[] recoveryMaxRadix;

	String picture;

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
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/bskill/" + id
				+ ".gif\" alt=\"被动技能图片\"/>");
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
	 * @explain： 用户拥有技能描述
	 * @datetime:2007-3-19 9:09:22
	 * @return
	 * @return String
	 */
	public String toDetail1() {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src=\"" + PKAction.PK_IMAGE_PATH + "/bskill/" + id
				+ ".gif\" alt=\"被动技能图片\"/>");
		sb.append("<br/>");
		sb.append("技能名称:");
		sb.append(name);
		sb.append("<br/>");
		sb.append("购买价格:");
		sb.append(price);
		sb.append("<br/>");
		return sb.toString();
	}

	/**
	 * @return 返回 aggressGrowthRadix。
	 */
	public int[] getAggressGrowthRadix() {
		return aggressGrowthRadix;
	}

	/**
	 * @param aggressGrowthRadix
	 *            要设置的 aggressGrowthRadix。
	 */
	public void setAggressGrowthRadix(int[] aggressGrowthRadix) {
		this.aggressGrowthRadix = aggressGrowthRadix;
	}

	/**
	 * @return 返回 aggressMaxRadix。
	 */
	public int[] getAggressMaxRadix() {
		return aggressMaxRadix;
	}

	/**
	 * @param aggressMaxRadix
	 *            要设置的 aggressMaxRadix。
	 */
	public void setAggressMaxRadix(int[] aggressMaxRadix) {
		this.aggressMaxRadix = aggressMaxRadix;
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
	 * @return 返回 energyGrowthRadix。
	 */
	public int[] getEnergyGrowthRadix() {
		return energyGrowthRadix;
	}

	/**
	 * @param energyGrowthRadix
	 *            要设置的 energyGrowthRadix。
	 */
	public void setEnergyGrowthRadix(int[] energyGrowthRadix) {
		this.energyGrowthRadix = energyGrowthRadix;
	}

	/**
	 * @return 返回 energyMaxRadix。
	 */
	public int[] getEnergyMaxRadix() {
		return energyMaxRadix;
	}

	/**
	 * @param energyMaxRadix
	 *            要设置的 energyMaxRadix。
	 */
	public void setEnergyMaxRadix(int[] energyMaxRadix) {
		this.energyMaxRadix = energyMaxRadix;
	}

	/**
	 * @return 返回 flyingGrowthRadix。
	 */
	public int[] getFlyingGrowthRadix() {
		return flyingGrowthRadix;
	}

	/**
	 * @param flyingGrowthRadix
	 *            要设置的 flyingGrowthRadix。
	 */
	public void setFlyingGrowthRadix(int[] flyingGrowthRadix) {
		this.flyingGrowthRadix = flyingGrowthRadix;
	}

	/**
	 * @return 返回 flyingMaxRadix。
	 */
	public int[] getFlyingMaxRadix() {
		return flyingMaxRadix;
	}

	/**
	 * @param flyingMaxRadix
	 *            要设置的 flyingMaxRadix。
	 */
	public void setFlyingMaxRadix(int[] flyingMaxRadix) {
		this.flyingMaxRadix = flyingMaxRadix;
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
	 * @return 返回 physicalGrowthRadix。
	 */
	public int[] getPhysicalGrowthRadix() {
		return physicalGrowthRadix;
	}

	/**
	 * @param physicalGrowthRadix
	 *            要设置的 physicalGrowthRadix。
	 */
	public void setPhysicalGrowthRadix(int[] physicalGrowthRadix) {
		this.physicalGrowthRadix = physicalGrowthRadix;
	}

	/**
	 * @return 返回 physicalMaxRadix。
	 */
	public int[] getPhysicalMaxRadix() {
		return physicalMaxRadix;
	}

	/**
	 * @param physicalMaxRadix
	 *            要设置的 physicalMaxRadix。
	 */
	public void setPhysicalMaxRadix(int[] physicalMaxRadix) {
		this.physicalMaxRadix = physicalMaxRadix;
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
	 * @return 返回 recoveryGrowthRadix。
	 */
	public int[] getRecoveryGrowthRadix() {
		return recoveryGrowthRadix;
	}

	/**
	 * @param recoveryGrowthRadix
	 *            要设置的 recoveryGrowthRadix。
	 */
	public void setRecoveryGrowthRadix(int[] recoveryGrowthRadix) {
		this.recoveryGrowthRadix = recoveryGrowthRadix;
	}

	/**
	 * @return 返回 recoveryMaxRadix。
	 */
	public int[] getRecoveryMaxRadix() {
		return recoveryMaxRadix;
	}

	/**
	 * @param recoveryMaxRadix
	 *            要设置的 recoveryMaxRadix。
	 */
	public void setRecoveryMaxRadix(int[] recoveryMaxRadix) {
		this.recoveryMaxRadix = recoveryMaxRadix;
	}
}
