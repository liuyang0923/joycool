/**
 *作者:macq
 *日期:2006-9-19
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

public class HomeHitsBean {
	int id;

	int userId;

	int hits;

	String createDatetime;

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
	 * @return 返回 hits。
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * @param hits
	 *            要设置的 hits。
	 */
	public void setHits(int hits) {
		this.hits = hits;
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
