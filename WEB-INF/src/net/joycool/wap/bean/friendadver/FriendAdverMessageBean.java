/**
 * 
 */
package net.joycool.wap.bean.friendadver;

import net.joycool.wap.util.Constants;

/**
 * @author zhul 2006-06-20 交友中心模块
 * 用来传递用户对发表的交友广告的评论的javaBean
 */
public class FriendAdverMessageBean {

	private int id;

	private int friendAdverId;

	private int userId;

	private String content;

	private String attachment;

	private String createDatetime;

	private String userNickname;
	/**
	 * @return Returns the userNickname.
	 */
	public String getUserNickname() {
		return userNickname;
	}

	/**
	 * @param userNickname The userNickname to set.
	 */
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	/**
	 * @return Returns the attachment.
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment
	 *            The attachment to set.
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
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
        if (createDatetime != null) {
            if (createDatetime.indexOf("-") != -1) {
                createDatetime = createDatetime.substring(createDatetime
                        .indexOf("-") + 1);
            }
            if (createDatetime.lastIndexOf(":") != -1) {
                createDatetime = createDatetime.substring(0, createDatetime
                        .lastIndexOf(":"));
            }
        }
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
	 * @return Returns the friendAdverId.
	 */
	public int getFriendAdverId() {
		return friendAdverId;
	}

	/**
	 * @param friendAdverId
	 *            The friendAdverId to set.
	 */
	public void setFriendAdverId(int friendAdverId) {
		this.friendAdverId = friendAdverId;
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

    public String getAttachmentURL() {
        return Constants.FRIENDADVER_RESOURCE_ROOT_URL + attachment;
    }
	
}
