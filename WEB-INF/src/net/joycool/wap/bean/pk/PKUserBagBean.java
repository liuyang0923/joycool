package net.joycool.wap.bean.pk;

public class PKUserBagBean {
	int id;

	int userId;

	int equipId;

	int siteId;
	//如果是装备代表当前耐久度，如果是药品或暗器代表当前拥有个数
	int enduranceDegree;
	//1为装备，2为药品，3为暗器
	int type;

	// 物品原型引用
	PKProtoBean porto;

	/**
	 * @return 返回 enduranceDegree。
	 */
	public int getEnduranceDegree() {
		return enduranceDegree;
	}

	/**
	 * @param enduranceDegree
	 *            要设置的 enduranceDegree。
	 */
	public void setEnduranceDegree(int enduranceDegree) {
		this.enduranceDegree = enduranceDegree;
	}

	/**
	 * @return 返回 equipId。
	 */
	public int getEquipId() {
		return equipId;
	}

	/**
	 * @param equipId
	 *            要设置的 equipId。
	 */
	public void setEquipId(int equipId) {
		this.equipId = equipId;
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
	 * @return 返回 siteId。
	 */
	public int getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            要设置的 siteId。
	 */
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return 返回 type。
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            要设置的 type。
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 porto。
	 */
	public PKProtoBean getPorto() {
		return porto;
	}

	/**
	 * @param porto
	 *            要设置的 porto。
	 */
	public void setPorto(PKProtoBean porto) {
		this.porto = porto;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain： 获取行囊内物品属性介绍
	 * @datetime:2007-3-8 14:29:05
	 * @return
	 * @return String
	 */
	public String toDetail() {
		StringBuffer sb = new StringBuffer();
		switch(type){
		//装备
		case 0:
			sb.append("当前耐久度：");
			sb.append(enduranceDegree);
			sb.append("<br/>");
			break;
		//物品
		case 1:
			sb.append("当前数量：");
			sb.append(enduranceDegree);
			sb.append("<br/>");
			break;
		//暗器
		case 2:
			sb.append("当前数量：");
			sb.append(enduranceDegree);
			sb.append("<br/>");
			break;
		}
		return sb.toString();
	}
}
