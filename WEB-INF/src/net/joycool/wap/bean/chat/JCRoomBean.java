package net.joycool.wap.bean.chat;

public class JCRoomBean {
	int id;

	String name;

	// mcq_2006-6-26_增加自建聊天室相关字段属性_start
	int creatorId;

	int maxOnlineCount;

	int payWay;

	int payDays;
	
	//String introduce;

	String createDatetime;

	String expireDatetime;

	String thumbnail;

	int grantMode;

	int status;

	int currentOnlineCount;

	int mark;
	
	String description;
	
	int top;



	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	// mcq_2006-6-26_增加自建聊天室相关字段属性_end
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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return Returns the creatorId.
	 */
	public int getCreatorId() {
		return creatorId;
	}

	/**
	 * @param creatorId
	 *            The creatorId to set.
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * @return Returns the currentOnlineCount.
	 */
	public int getCurrentOnlineCount() {
		return currentOnlineCount;
	}

	/**
	 * @param currentOnlineCount
	 *            The currentOnlineCount to set.
	 */
	public void setCurrentOnlineCount(int currentOnlineCount) {
		this.currentOnlineCount = currentOnlineCount;
	}

	/**
	 * @return Returns the expireDatetime.
	 */
	public String getExpireDatetime() {
		return expireDatetime;
	}

	/**
	 * @param expireDatetime
	 *            The expireDatetime to set.
	 */
	public void setExpireDatetime(String expireDatetime) {
		this.expireDatetime = expireDatetime;
	}

	/**
	 * @return Returns the grantMode.
	 */
	public int getGrantMode() {
		return grantMode;
	}

	/**
	 * @param grantMode
	 *            The grantMode to set.
	 */
	public void setGrantMode(int grantMode) {
		this.grantMode = grantMode;
	}

	/**
	 * @return Returns the introduce.
	 */
	/*public String getIntroduce() {
		return introduce;
	}*/

	/**
	 * @param introduce
	 *            The introduce to set.
	 */
	/*public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}*/

	/**
	 * @return Returns the maxOnlineCount.
	 */
	public int getMaxOnlineCount() {
		return maxOnlineCount;
	}

	/**
	 * @param maxOnlineCount
	 *            The maxOnlineCount to set.
	 */
	public void setMaxOnlineCount(int maxOnlineCount) {
		this.maxOnlineCount = maxOnlineCount;
	}

	/**
	 * @return Returns the payWay.
	 */
	public int getPayWay() {
		return payWay;
	}

	/**
	 * @param payWay
	 *            The payWay to set.
	 */
	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}

	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return Returns the thumbnail.
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail
	 *            The thumbnail to set.
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @return Returns the payDays.
	 */
	public int getPayDays() {
		return payDays;
	}

	/**
	 * @param payDays The payDays to set.
	 */
	public void setPayDays(int payDays) {
		this.payDays = payDays;
	}

	/**
	 * @return 返回 description。
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description 要设置的 description。
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return 返回 top。
	 */
	public int getTop() {
		return top;
	}

	/**
	 * @param top 要设置的 top。
	 */
	public void setTop(int top) {
		this.top = top;
	}
}
