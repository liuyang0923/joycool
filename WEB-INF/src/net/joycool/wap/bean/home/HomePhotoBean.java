/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

import net.joycool.wap.bean.UserBean;

public class HomePhotoBean {
	int id;

	int userId;

	String title;

	String attach;

	int hits;

	int mark;

	String createDatetime;
	
	UserBean user = new UserBean();
	//zhul add 2006-10-10 
	private int dailyHits;
	
	private int reviewCount;
	
    String recommendTime;
    
    int catId;
    
	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	/**
	 * @return 返回 recommendTime。
	 */
	public String getRecommendTime() {
		return recommendTime;
	}

	/**
	 * @param recommendTime 要设置的 recommendTime。
	 */
	public void setRecommendTime(String recommendTime) {
		this.recommendTime = recommendTime;
	}

	/**
	 * @return Returns the reviewCount.
	 */
	public int getReviewCount() {
		return reviewCount;
	}

	/**
	 * @param reviewCount The reviewCount to set.
	 */
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	/**
	 * @return Returns the dailyHits.
	 */
	public int getDailyHits() {
		return dailyHits;
	}

	/**
	 * @param dailyHits The dailyHits to set.
	 */
	public void setDailyHits(int dailyHits) {
		this.dailyHits = dailyHits;
	}

	/**
	 * @return Returns the user.
	 */
	public UserBean getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(UserBean user) {
		this.user = user;
	}

	/**
	 * @return 返回 attach。
	 */
	public String getAttach() {
		return attach;
	}

	/**
	 * @param attach
	 *            要设置的 attach。
	 */
	public void setAttach(String attach) {
		if(attach.length() != 0 && !attach.startsWith("/"))
			this.attach = "/home/myalbum/" + attach;
		else
			this.attach = attach;
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
	 * @return 返回 mark。
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark。
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return 返回 title。
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            要设置的 title。
	 */
	public void setTitle(String title) {
		this.title = title;
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
