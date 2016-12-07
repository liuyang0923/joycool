package net.joycool.wap.bean.friend;

public class FriendCartoonBean {
	int id;

	int type;

	String pic;

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
	 * @return Returns the pic.
	 */
	public String getPic() {
		return pic;
	}

	/**
	 * @param pic
	 *            The pic to set.
	 */
	public void setPic(String pic) {
		this.pic = pic;
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

	public String getUrl() {
		if (null != pic)
			return "http://wap.joycool.net/img/friend/attach/" + pic + ".gif";
		return null;
	}
}
