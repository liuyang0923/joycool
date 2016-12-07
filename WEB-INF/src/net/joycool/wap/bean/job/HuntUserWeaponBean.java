package net.joycool.wap.bean.job;

public class HuntUserWeaponBean {
	
	int id;

	int userId;

	int weaponId;

	long createDatetime;

	long expireDatetime;
	
	String name;


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
	 * @return Returns the createDatetime.
	 */
	public long getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime The createDatetime to set.
	 */
	public void setCreateDatetime(long createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the expireDatetime.
	 */
	public long getExpireDatetime() {
		return expireDatetime;
	}

	/**
	 * @param expireDatetime The expireDatetime to set.
	 */
	public void setExpireDatetime(long expireDatetime) {
		this.expireDatetime = expireDatetime;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the weaponId.
	 */
	public int getWeaponId() {
		return weaponId;
	}

	/**
	 * @param weaponId
	 *            The weaponId to set.
	 */
	public void setWeaponId(int weaponId) {
		this.weaponId = weaponId;
	}

}
