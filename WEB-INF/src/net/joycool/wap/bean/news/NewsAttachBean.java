/*
 * Created on 2005-11-24
 *
 */
package net.joycool.wap.bean.news;

import net.joycool.wap.util.Constants;

/**
 * @author lbj
 *
 */
public class NewsAttachBean {
    int id;
    int newsId;
    String title;
    String fileUrl;
    String type;
    
    
    /**
     * @return Returns the fileUrl.
     */
    public String getFileUrl() {
        return fileUrl;
    }
    /**
     * @param fileUrl The fileUrl to set.
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
     * @return Returns the newsId.
     */
    public int getNewsId() {
        return newsId;
    }
    /**
     * @param newsId The newsId to set.
     */
    public void setNewsId(int newsId) {
        this.newsId = newsId;
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
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    
    public String getFileURL(){
        return Constants.NEWS_RESOURCE_ROOT_URL + fileUrl;
    }
}
