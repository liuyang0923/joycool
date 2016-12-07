/*
 * 作者:马长青
 * 日期:2006-6-20
 * 功能:用户位置信息
 */
package net.joycool.wap.bean;

public class PositionBean {
	private int id;

	private String positionName;

	private String positionInfo;
	
	private String url;

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
	 * @return Returns the positionInfo.
	 */
	public String getPositionInfo() {
		return positionInfo;
	}

	/**
	 * @param positionInfo
	 *            The positionInfo to set.
	 */
	public void setPositionInfo(String positionInfo) {
		this.positionInfo = positionInfo;
	}

	/**
	 * @return Returns the positionName.
	 */
	public String getPositionName() {
		return positionName;
	}

	/**
	 * @param positionName
	 *            The positionName to set.
	 */
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
