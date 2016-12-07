package net.joycool.wap.bean.friend;

public class FriendRingBean {
	int id;

	int price;

	String name;

	String picture;

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
	 * @return Returns the picture.
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            The picture to set.
	 */
	public void setPicture(String picture) {
		this.picture = picture;
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
	public String getImageUrl(){
		if(null!=picture)
			return "http://wap.joycool.net/img/friend/friendring/"+picture;
		return null;
	}
}
