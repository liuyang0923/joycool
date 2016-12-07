package net.joycool.wap.bean.friendlink;

/*
 * Created on 2005-2-24
 *
 */

public class LinkTypeBean {
	/**
	 * @author mcq
	 * 
	 */
	int id;

	String name;

	String linkUrl;

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
	 * @return Returns the linkUrl.
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	/**
	 * @param linkUrl
	 *            The linkUrl to set.
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}
