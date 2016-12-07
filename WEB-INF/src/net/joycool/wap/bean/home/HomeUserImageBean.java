/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

public class HomeUserImageBean {
	int id;

	int userId;

	int ImageId;

	int typeId;

	String createDatetime;
	int homeId;

	/**
	 * @return Returns the homeId.
	 */
	public int getHomeId() {
		return homeId;
	}

	/**
	 * @param homeId The homeId to set.
	 */
	public void setHomeId(int homeId) {
		this.homeId = homeId;
	}

	/**
	 * @return 返回 createDatetime。
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            要设置的 createDatetime。
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
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
	 * @return 返回 imageId。
	 */
	public int getImageId() {
		return ImageId;
	}

	/**
	 * @param imageId
	 *            要设置的 imageId。
	 */
	public void setImageId(int imageId) {
		ImageId = imageId;
	}

	/**
	 * @return 返回 typeId。
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            要设置的 typeId。
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
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

}
