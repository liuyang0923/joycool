/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.bean.mall;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public class ReplyBean {
    public int id;

    public int userId;

    public int parentId;

    public String content;

    public String createDatetime;
    
    public String userNick;

    /**
     * @return Returns the userNick.
     */
    public String getUserNick() {
        return userNick;
    }

    /**
     * @param userNick
     *            The userNick to set.
     */
    public void setUserNick(String userNick) {
        this.userNick = userNick;
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
}
