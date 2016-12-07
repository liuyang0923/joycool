package net.joycool.wap.bean.friend;

public class FriendReviewBean {
	int id;

	int marriageId;

	int reviewUserId;

	String review;

	String createDatetime;
	int file;

	/**
	 * @return Returns the file.
	 */
	public int getFile() {
		return file;
	}

	/**
	 * @param file The file to set.
	 */
	public void setFile(int file) {
		this.file = file;
	}

	/**
	 * @return Returns the createDatetime.
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            The createDatetime to set.
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
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
	 * @return Returns the marriageId.
	 */
	public int getMarriageId() {
		return marriageId;
	}

	/**
	 * @param marriageId
	 *            The marriageId to set.
	 */
	public void setMarriageId(int marriageId) {
		this.marriageId = marriageId;
	}

	/**
	 * @return Returns the review.
	 */
	public String getReview() {
		return review;
	}

	/**
	 * @param review
	 *            The review to set.
	 */
	public void setReview(String review) {
		this.review = review;
	}

	/**
	 * @return Returns the reviewUserId.
	 */
	public int getReviewUserId() {
		return reviewUserId;
	}

	/**
	 * @param reviewUserId
	 *            The reviewUserId to set.
	 */
	public void setReviewUserId(int reviewUserId) {
		this.reviewUserId = reviewUserId;
	}

}
