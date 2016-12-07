package net.joycool.wap.bean.pk;

public class PKNpcBean {
	int id;

	int sceneId;

	String name;

	// 装备
	String equip;

	// 药品
	String medicine;

	// 主动技能
	String skill;

	// 被动技能
	String bSkill;

	// 任务
	String mission;

	/**
	 * @return 返回 equip。
	 */
	public String getEquip() {
		return equip;
	}

	/**
	 * @param equip
	 *            要设置的 equip。
	 */
	public void setEquip(String equip) {
		this.equip = equip;
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
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 sceneId。
	 */
	public int getSceneId() {
		return sceneId;
	}

	/**
	 * @param sceneId
	 *            要设置的 sceneId。
	 */
	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	/**
	 * @return 返回 medicine。
	 */
	public String getMedicine() {
		return medicine;
	}

	/**
	 * @param medicine
	 *            要设置的 medicine。
	 */
	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	/**
	 * @return 返回 bSkill。
	 */
	public String getBSkill() {
		return bSkill;
	}

	/**
	 * @param skill
	 *            要设置的 bSkill。
	 */
	public void setBSkill(String skill) {
		bSkill = skill;
	}

	/**
	 * @return 返回 skill。
	 */
	public String getSkill() {
		return skill;
	}

	/**
	 * @param skill
	 *            要设置的 skill。
	 */
	public void setSkill(String skill) {
		this.skill = skill;
	}

	/**
	 * @return Returns the mission.
	 */
	public String getMission() {
		return mission;
	}

	/**
	 * @param mission
	 *            The mission to set.
	 */
	public void setMission(String mission) {
		this.mission = mission;
	}
}
