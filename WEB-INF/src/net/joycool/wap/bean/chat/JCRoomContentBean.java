package net.joycool.wap.bean.chat;

public class JCRoomContentBean {
	public static String ATTACH_ROOT = "/usr/local/joycool-rep/chat";

	// public static String ATTACH_ROOT ="E:/eclipse/workspace/joycool-portal/img";

	public static String ATTACH_URL_ROOT = "/rep";

	// public static String ATTACH_URL_ROOT = "http://wap.joycool.net/img";
	int id;

	int fromId;

	int toId;

	String fromNickName;

	String toNickName;

	String content;

	String attach;

	String sendDateTime;

	int isPrivate;

	int roomId;
	
    //mcq_2006-6-26_增加自建聊天室相关字段属性_start
	int mark;
    //mcq_2006-6-26_增加自建聊天室相关字段属性_end
	
	//zhul_2006-08-22 add for chat model
	int secRoomId;

	/**
	 * @return Returns the secRoomId.
	 */
	public int getSecRoomId() {
		return secRoomId;
	}

	/**
	 * @param secRoomId The secRoomId to set.
	 */
	public void setSecRoomId(int secRoomId) {
		this.secRoomId = secRoomId;
	}

	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the attach.
	 */
	public String getAttach() {
		return attach;
	}

	/**
	 * @param attach
	 *            The attach to set.
	 */
	public void setAttach(String attach) {
		if(attach.length() != 0 && !attach.startsWith("/"))
			this.attach = "/box/" + attach;
		else
			this.attach = attach;
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
	 * @return Returns the fromId.
	 */
	public int getFromId() {
		return fromId;
	}

	/**
	 * @param fromId
	 *            The fromId to set.
	 */
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	/**
	 * @return Returns the fromNickName.
	 */
	public String getFromNickName() {
		return fromNickName;
	}

	/**
	 * @param fromNickName
	 *            The fromNickName to set.
	 */
	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
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
	 * @return Returns the isPrivate.
	 */
	public int getIsPrivate() {
		return isPrivate;
	}

	/**
	 * @param isPrivate
	 *            The isPrivate to set.
	 */
	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}

	/**
	 * @return Returns the roomId.
	 */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId
	 *            The roomId to set.
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	/**
	 * @return Returns the toId.
	 */
	public int getToId() {
		return toId;
	}

	/**
	 * @param toId
	 *            The toId to set.
	 */
	public void setToId(int toId) {
		this.toId = toId;
	}

	/**
	 * @return Returns the toNickName.
	 */
	public String getToNickName() {
		return toNickName;
	}

	/**
	 * @param toNickName
	 *            The toNickName to set.
	 */
	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}

	/**
	 * @return Returns the sendDateTime.
	 */
	public String getSendDateTime() {
		return sendDateTime;
	}

	/**
	 * @param sendDateTime
	 *            The sendDateTime to set.
	 */
	public void setSendDateTime(String sendDateTime) {
		this.sendDateTime = sendDateTime;
	}
}
