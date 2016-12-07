/*
 * Created on 2005-12-26
 *
 */
package net.joycool.wap.bean.guestbook;

/**
 * @author lbj
 *
 */
public class ContentBean {
    int id;
    int boardId;
    String nickname;
    String content;
    String createDatetime;
    
    
    /**
     * @return Returns the boardId.
     */
    public int getBoardId() {
        return boardId;
    }
    /**
     * @param boardId The boardId to set.
     */
    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }
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
     * @param createDatetime The createDatetime to set.
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
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return Returns the nickname.
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * @param nickname The nickname to set.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
