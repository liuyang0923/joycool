/*
 * Created on 2005-12-24
 *
 */
package net.joycool.wap.bean.forum;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.Constants;

/**
 * @author lbj
 *  
 */
public class ForumMessageBean {
    int id;

    String title;

    String content;

    String attachment;

    int parentId;

    int userId;

    String userNickname;

    UserBean user;

    String createDatetime;

    int forumId;

    int hits;

    /**
     * @return Returns the userNickname.
     */
    public String getUserNickname() {
        return userNickname;
    }

    /**
     * @param userNickname
     *            The userNickname to set.
     */
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    /**
     * @return Returns the forumId.
     */
    public int getForumId() {
        return forumId;
    }

    /**
     * @param forumId
     *            The forumId to set.
     */
    public void setForumId(int forumId) {
        this.forumId = forumId;
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
     * @return Returns the hits.
     */
    public int getHits() {
        return hits;
    }

    /**
     * @param hits
     *            The hits to set.
     */
    public void setHits(int hits) {
        this.hits = hits;
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
     * @return Returns the parentId.
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            The parentId to set.
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
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
     * @return Returns the user.
     */
    public UserBean getUser() {
        return user;
    }

    /**
     * @param user
     *            The user to set.
     */
    public void setUser(UserBean user) {
        this.user = user;
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
        return Constants.FORUM_RESOURCE_ROOT_URL + attachment;
    }
}
