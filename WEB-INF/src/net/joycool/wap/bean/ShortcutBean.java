package net.joycool.wap.bean;


/**
 * @author bomb
 *	快捷通道内容
 */
public class ShortcutBean {
	int id;

	int seq;

	String name;

	String url;
	
	String shortName;
	int hide;	// 如果是1表示不在列表中显示

	public int getHide() {
		return hide;
	}

	public void setHide(int hide) {
		this.hide = hide;
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
	 * @return Returns the seq.
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq The seq to set.
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return Returns the shortName.
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName The shortName to set.
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
