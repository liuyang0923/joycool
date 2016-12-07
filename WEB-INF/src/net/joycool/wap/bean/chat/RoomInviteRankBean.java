package net.joycool.wap.bean.chat;

import net.joycool.wap.bean.CrownBean;

/**
 * fanys 2006-08-15
 * 
 * @author Administrator
 * 
 */
public class RoomInviteRankBean {
	private int id;//

	private int userId;

	private int count;// 邀请人数

	private int resourceId;// 资源ID

	private String createDateTime;// 排行榜创建时间

	private String nickName;

	private CrownBean crown;

	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return Returns the createDateTime.
	 */
	public String getCreateDateTime() {
		return createDateTime;
	}

	/**
	 * @param createDateTime
	 *            The createDateTime to set.
	 */
	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
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
	 * @return Returns the resourceId.
	 */
	public int getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId
	 *            The resourceId to set.
	 */
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
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
	 * @return Returns the nickName.
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            The nickName to set.
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return Returns the crown.
	 */
	public CrownBean getCrown() {
		return crown;
	}

	/**
	 * @param crown
	 *            The crown to set.
	 */
	public void setCrown(CrownBean crown) {
		this.crown = crown;
	}
}
