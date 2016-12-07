/*
 * Created on 2006-4-28
 *
 */
package net.joycool.wap.bean;

/**
 * @author lbj
 * 
 */
public class NoticeBean {
	public static int GENERAL_NOTICE = 3;

	public static int SYSTEM_NOTICE = 4;

	public static int KEEP_NOT_READ_NOTICE = 10;

	public static int NOT_READ = 0;

	public static int READED = 1;

	int tongId;

	int id;

	int userId;

	String title;

	String content;

	String link;

	String hideUrl;

	int status;

	int type;

	String createDatetime;

	// macq_2007-5-16_增加消息类型字段
	int mark;
	//pk游戏
	public static int PK_GAME = 1;
	//家园回复
	public static int HOME_REVIEW = 2;
	//帮会通知
	public static int TONG = 3;
	//上线提示
	public static int FRIEND_JY = 4;
	//信箱
	public static int MESSAGE = 5;
	//聊天
	public static int CHAT = 6;
	//多回合游戏
	public static int PK_GAME_HALL = 7;
	//宠物系统
	public static int PET = 8;
	//动作
	public static int SENDACTION = 9;
	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return Returns the createDatetime.
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            The createDatetime to set.
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the hideUrl.
	 */
	public String getHideUrl() {
		return hideUrl;
	}

	/**
	 * @param hideUrl
	 *            The hideUrl to set.
	 */
	public void setHideUrl(String hideUrl) {
		this.hideUrl = hideUrl;
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
	 * @return Returns the link.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            The link to set.
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
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

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the tongId.
	 */
	public int getTongId() {
		return tongId;
	}

	/**
	 * @param tongId
	 *            The tongId to set.
	 */
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}
}
