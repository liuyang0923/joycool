package net.joycool.wap.bean.pk;

/**
 * @author macq
 * @datetime 2007-2-2 下午01:16:20
 * @explain 用户所拥有的技能
 */
public class PKUserHSkillBean {
	int id;

	int userId;

	int skillId;

	int skillKey;

	int skillType;

	int excersize;

	int rank;

	/**
	 * @return 返回 rank。
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank
	 *            要设置的 rank。
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return excersize
	 */
	public int getExcersize() {
		return excersize;
	}

	/**
	 * @param excersize
	 *            要设置的 excersize
	 */
	public void setExcersize(int excersize) {
		this.excersize = excersize;
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
	 * @return skillId
	 */
	public int getSkillId() {
		return skillId;
	}

	/**
	 * @param skillId
	 *            要设置的 skillId
	 */
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	/**
	 * @return skillKey
	 */
	public int getSkillKey() {
		return skillKey;
	}

	/**
	 * @param skillKey
	 *            要设置的 skillKey
	 */
	public void setSkillKey(int skillKey) {
		this.skillKey = skillKey;
	}

	/**
	 * @return skillType
	 */
	public int getSkillType() {
		return skillType;
	}

	/**
	 * @param skillType
	 *            要设置的 skillType
	 */
	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}

	/**
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
