package net.joycool.wap.world;

import java.util.LinkedList;

public class ChatWorldBean {

	public static String TONG_CHAT = "t";

	long id;

	int userId;

	String content;

	String createDatetime;

	/**
	 * @return 返回 content。
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            要设置的 content。
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
	 * @param createDatetime
	 *            要设置的 createDatetime。
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 id。
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id。
	 */
	public void setId(long id) {
		this.id = id;
	}
}
