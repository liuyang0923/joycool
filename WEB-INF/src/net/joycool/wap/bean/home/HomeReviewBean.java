/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

import net.joycool.wap.util.DateUtil;

public class HomeReviewBean {
	int id;

	int userId;

	int reviewUserId;

	String review;

	long createTime;

	/**
	 * @return 返回 createDdatetime。
	 */
	public String getCreateDdatetime() {
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
	 * @return 返回 review。
	 */
	public String getReview() {
		return review;
	}

	/**
	 * @param review
	 *            要设置的 review。
	 */
	public void setReview(String review) {
		this.review = review;
	}

	/**
	 * @return 返回 reviewUserId。
	 */
	public int getReviewUserId() {
		return reviewUserId;
	}

	/**
	 * @param reviewUserId
	 *            要设置的 reviewUserId。
	 */
	public void setReviewUserId(int reviewUserId) {
		this.reviewUserId = reviewUserId;
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
