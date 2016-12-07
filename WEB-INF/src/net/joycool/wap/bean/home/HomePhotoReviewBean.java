/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

public class HomePhotoReviewBean {
	int id;

	int photoId;

	int reviewUserId;

	String review;

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
	 * @return 返回 photoId。
	 */
	public int getPhotoId() {
		return photoId;
	}

	/**
	 * @param photoId 要设置的 photoId。
	 */
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
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

	public String getCreateTime() {
		if (createDatetime != null) {
			if (createDatetime.indexOf("-") != -1) {
				createDatetime = createDatetime.substring(createDatetime
						.indexOf("-") + 1);
			}
			if (createDatetime.lastIndexOf(":") != -1) {
				createDatetime = createDatetime.substring(0, createDatetime
						.lastIndexOf(":"));
			}
		}
		return createDatetime;
	}
	
}
