package net.joycool.wap.bean.stock2;

/**
 * @author macq
 * @explain：
 * @datetime:2007-5-15 13:59:03
 */
public class StockNoticeBean {
	int id;

	String title;

	String content;

	String createDatetime;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
