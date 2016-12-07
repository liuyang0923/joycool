package net.joycool.wap.bean.home;

public class HomeTypeBean {
	int id;
	String name;
	String roomIds;
	int goods;
	String pic;
	int money;
	/**
	 * @return Returns the money.
	 */
	public int getMoney() {
		return money;
	}
	/**
	 * @param money The money to set.
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return Returns the goods.
	 */
	public int getGoods() {
		return goods;
	}
	/**
	 * @param goods The goods to set.
	 */
	public void setGoods(int goods) {
		this.goods = goods;
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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the pic.
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * @param pic The pic to set.
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getBigImageUrl(){
		if(null!=pic)
			return "http://wap.joycool.net/img/home/house/"+pic;
		return null;
	}
	public String getSmallImageUrl(){
		if(null!=pic)
			return "http://wap.joycool.net/img/home/house/"+pic;
		return null;
	}
	public String getRoomIds() {
		return roomIds;
	}
	public void setRoomIds(String roomIds) {
		this.roomIds = roomIds;
	}
}
