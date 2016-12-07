/*
 * Created on 2006-1-13
 *
 */
package net.joycool.wap.bean;

/**
 * @author lbj
 *
 */
public class CartBean {
    int id;
    int userId;
    String title;
    String url;
    
    
    
    /**
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
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
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
