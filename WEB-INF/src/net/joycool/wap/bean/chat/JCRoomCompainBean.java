package net.joycool.wap.bean.chat;

public class JCRoomCompainBean {
    int id;
    int leftUserId;
    int rightUserId;
    String content;
    int mark;
    String enterDateTime;
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return Returns the entetDateTime.
	 */
	public String getEnterDateTime() {
		return enterDateTime;
	}
	/**
	 * @param entetDateTime The entetDateTime to set.
	 */
	public void setEnterDateTime(String entetDateTime) {
		this.enterDateTime = entetDateTime;
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
	 * @return Returns the leftUserId.
	 */
	public int getLeftUserId() {
		return leftUserId;
	}
	/**
	 * @param leftUserId The leftUserId to set.
	 */
	public void setLeftUserId(int leftUserId) {
		this.leftUserId = leftUserId;
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
	 * @return Returns the rightUserId.
	 */
	public int getRightUserId() {
		return rightUserId;
	}
	/**
	 * @param rightUserId The rightUserId to set.
	 */
	public void setRightUserId(int rightUserId) {
		this.rightUserId = rightUserId;
	}
}
