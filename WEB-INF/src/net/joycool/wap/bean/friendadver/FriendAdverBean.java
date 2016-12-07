/**
 * 
 */
package net.joycool.wap.bean.friendadver;

import net.joycool.wap.util.Constants;

/**
 * @author zhul 2006-06-20 交友中心模块 
 * 用来传递用户交友广告信息的javaBean
 */
public class FriendAdverBean {

	private int id;

	private int userId;

	private String title;

	private int sex;		// 期望的性别

	private int age;

	private int area;

	private String remark;

	private String attachment;

	private String createDatetime;

	private int hits;
	int cityno;
	int gender;		// 自己的性别

	public int getCityno() {
		return cityno;
	}

	public void setCityno(int cityno) {
		this.cityno = cityno;
	}

	/**
	 * @return Returns the age.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            The age to set.
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return Returns the area.
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @param area
	 *            The area to set.
	 */
	public void setArea(int area) {
		this.area = area;
	}

	/**
	 * @return Returns the attachment.
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment
	 *            The attachment to set.
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return Returns the createDatetime.
	 */
	public String getCreateDatetime() {
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

	/**
	 * @param createDatetime
	 *            The createDatetime to set.
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the hits.
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * @param hits
	 *            The hits to set.
	 */
	public void setHits(int hits) {
		this.hits = hits;
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
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return Returns the sex.
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            The sex to set.
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAttachmentURL() {
		return Constants.FRIENDADVER_RESOURCE_ROOT_URL + attachment;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

}
