package net.joycool.wap.bean;

public class UrlMapBean {
	private int id;

	private int sortId;

	private String module;

	private int catalogId;

	private String returnUrl;

	private String title;

	/**
	 * @return Returns the catalogId.
	 */
	public int getCatalogId() {
		return catalogId;
	}

	/**
	 * @param catalogId The catalogId to set.
	 */
	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the module.
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module The module to set.
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return Returns the returnUrl.
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * @param returnUrl The returnUrl to set.
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	/**
	 * @return Returns the sortId.
	 */
	public int getSortId() {
		return sortId;
	}

	/**
	 * @param sortId The sortId to set.
	 */
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
