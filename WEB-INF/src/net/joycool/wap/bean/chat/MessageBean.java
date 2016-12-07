/*
 * Created on 2006-3-17
 *
 */
package net.joycool.wap.bean.chat;

/**
 * @author lbj
 *  
 */
public class MessageBean {
    public static String ATTACH_ROOT = "/usr/local/joycool-rep/chat";

    public static String ATTACH_URL_ROOT = "/rep/chat";

    String fromUserName;

    String fromNickName;

    String toUserName;

    String toNickName;

    String content;

    String dateTime;

    String attach;

    int isPrivate;

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
        this.attach = attach;
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
     * @return Returns the dateTime.
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime
     *            The dateTime to set.
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
     * @return Returns the fromUserName.
     */
    public String getFromUserName() {
        return fromUserName;
    }

    /**
     * @param fromUserName
     *            The fromUserName to set.
     */
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
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
     * @return Returns the toUserName.
     */
    public String getToUserName() {
        return toUserName;
    }

    /**
     * @param toUserName
     *            The toUserName to set.
     */
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
}
