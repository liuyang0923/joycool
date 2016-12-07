package net.joycool.wap.bean.stock;

public class StockNoticeBean {
	int id;

	String title;

	String content;

	String createDatetime;

	/**
	 * @return 返回 content。
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content 要设置的 content。
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return 返回 createDatetime。
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime 要设置的 createDatetime。
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
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

}
