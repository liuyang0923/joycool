package net.joycool.wap.bean;
/**
 * @author Liq 
 * 在jsp中添加广告的bean
 */

public class AdverBean {

	int id;
	String name;
	String title;
	String url;
	int group;
	/**
	 * @return 返回 group。
	 */
	public int getGroup() {
		return group;
	}
	/**
	 * @param group 要设置的 group。
	 */
	public void setGroup(int group) {
		this.group = group;
	}
	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id 要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name 要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return 返回 title。
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title 要设置的 title。
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return 返回 url。
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url 要设置的 url。
	 */
	public void setUrl(String url) {
		this.url = url;
	}


}
