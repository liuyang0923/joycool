package net.joycool.wap.bean.video;

public class VideoBean {
	int id;

	int catalogId;

	String name;

	String introduction;

	String createDatetime;

	String updateDatetime;

	int downloadSum;
	
    String linkUrl;

	/**
	 * @return Returns the linkUrl.
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	/**
	 * @param linkUrl The linkUrl to set.
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	/**
	 * @return Returns the catalogId.
	 */
	public int getCatalogId() {
		return catalogId;
	}

	/**
	 * @param catalogId
	 *            The catalogId to set.
	 */
	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
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
	 * @return Returns the downloadSum.
	 */
	public int getDownloadSum() {
		return downloadSum;
	}

	/**
	 * @param downloadSum
	 *            The downloadSum to set.
	 */
	public void setDownloadSum(int downloadSum) {
		this.downloadSum = downloadSum;
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
	 * @return Returns the introduction.
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction
	 *            The introduction to set.
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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
	 * @return Returns the updateDatetime.
	 */
	public String getUpdateDatetime() {
		return updateDatetime;
	}

	/**
	 * @param updateDatetime
	 *            The updateDatetime to set.
	 */
	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

}
