package net.joycool.wap.bean.pk;

/**
 * @author macq
 * @datetime 2007-1-30 上午10:00:25
 * @explain
 */
public class PKMonsterSkillBean {
	int id;

	String description;

	int monsterId;
	
	String aggressGrowthRadix;

	/**
	 * @return aggressGrowthRadix
	 */
	public String getAggressGrowthRadix() {
		return aggressGrowthRadix;
	}

	/**
	 * @param aggressGrowthRadix 要设置的 aggressGrowthRadix
	 */
	public void setAggressGrowthRadix(String aggressGrowthRadix) {
		this.aggressGrowthRadix = aggressGrowthRadix;
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
	 * @return monsterId
	 */
	public int getMonsterId() {
		return monsterId;
	}

	/**
	 * @param monsterId
	 *            要设置的 monsterId
	 */
	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

}
