package net.joycool.wap.bean.job;

/**
 * fanys 2006-09-13 贺卡类型
 * 
 * @author Administrator
 * 
 */
public class HappyCardTypeBean {
	int id;

	String name;

	String description;

	int categoryId;

	/**
	 * @return Returns the categoryId.
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId
	 *            The categoryId to set.
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
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
}
