/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;

public class HomeDiaryBean {
	int id;

	int userId;

	String titel;

	String content;

	String shortContent;

	int hits;

	long createTime;

	int del;
	
	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	// zhul add 2006-10-10
	private int dailyHits;

	private int mark;

	private int reviewCount;

	String recommendTime;
	
	int catId;

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}

	/**
	 * @return 返回 recommendTime。
	 */
	public String getRecommendTime() {
		return recommendTime;
	}

	/**
	 * @param recommendTime
	 *            要设置的 recommendTime。
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
	 * @param reviewCount
	 *            The reviewCount to set.
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
	 * @param dailyHits
	 *            The dailyHits to set.
	 */
	public void setDailyHits(int dailyHits) {
		this.dailyHits = dailyHits;
	}

	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return 返回 content。
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            要设置的 content。
	 */
	public void setContent(String content) {
		this.content = content;
		this.shortContent = StringUtil.limitString(content, 52);
	}

	/**
	 * @return 返回 createDatetime。
	 */
	public String getCreateDatetime() {
		return DateUtil.sformatTime(createTime);
	}


	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	 * @return 返回 titel。
	 */
	public String getTitel() {
		return titel;
	}

	/**
	 * @param titel
	 *            要设置的 titel。
	 */
	public void setTitel(String titel) {
		this.titel = titel;
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

}
