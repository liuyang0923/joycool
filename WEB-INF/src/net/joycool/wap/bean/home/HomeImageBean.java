/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

public class HomeImageBean {
	int id;

	String name;

	String file;

	int price;

	int typeId;

	int mark;

	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return 返回 file。
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 *            要设置的 file。
	 */
	public void setFile(String file) {
		this.file = file;
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
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 price。
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            要设置的 price。
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return 返回 typeId。
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            要设置的 typeId。
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

}
