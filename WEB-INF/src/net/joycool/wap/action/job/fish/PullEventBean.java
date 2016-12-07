package net.joycool.wap.action.job.fish;


/**
 * @author bomb
 *
 */
public class PullEventBean {
	int id;
	int areaId;		// 出现区域
	String desc;	// 自己看的描述
	String log;		// 给别人看的描述
	int money;		// 乐币奖励
	int exp;		// 经验奖励
	String image;
	/**
	 * @return Returns the image.
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image The image to set.
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * @return Returns the areaId.
	 */
	public int getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId The areaId to set.
	 */
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return Returns the exp.
	 */
	public int getExp() {
		return exp;
	}
	/**
	 * @param exp The exp to set.
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}
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
	 * @return Returns the log.
	 */
	public String getLog() {
		return log;
	}
	/**
	 * @param log The log to set.
	 */
	public void setLog(String log) {
		this.log = log;
	}
	/**
	 * @return Returns the point.
	 */
	public int getMoney() {
		return money;
	}
	/**
	 * @param point The point to set.
	 */
	public void setMoney(int money) {
		this.money = money;
	}

}
