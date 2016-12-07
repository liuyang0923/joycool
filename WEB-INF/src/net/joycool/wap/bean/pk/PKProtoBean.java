package net.joycool.wap.bean.pk;

/**
 * @author macq
 * @explain：pk系统虚拟物品基类（包括 装备，暗器，药品）
 * @datetime:2007-3-6 11:09:37
 */
public class PKProtoBean {
	// public String todescription(int type){
	// String ret="";
	// switch(type){
	// //装备
	// case 1:
	// System.out.println("win a Car!");break;
	// //物品
	// case 2:
	// System.out.println("picked the goat");break;
	// //暗器
	// case 3:
	// System.out.println("get to keep your 100"); break;
	// //默认装备
	// default:
	// System.out.println("entry");
	// }
	// return ret;
	// }
	protected int id;

	protected int type;

	protected int price;

	protected String name;

	protected int count;

	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
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

	/**
	 * @return Returns the price.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            The price to set.
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
}
