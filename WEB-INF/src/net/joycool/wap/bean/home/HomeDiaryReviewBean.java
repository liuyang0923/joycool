package net.joycool.wap.bean.home;

import net.joycool.wap.util.DateUtil;

public class HomeDiaryReviewBean {
	int id;

	int diaryId;

	int reviewUserId;

	String review;

	long createTime;

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
	 * @return 返回 diaryId。
	 */
	public int getDiaryId() {
		return diaryId;
	}

	/**
	 * @param diaryId 要设置的 diaryId。
	 */
	public void setDiaryId(int diaryId) {
		this.diaryId = diaryId;
	}



}
