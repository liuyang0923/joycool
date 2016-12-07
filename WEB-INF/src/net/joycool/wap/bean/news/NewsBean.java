/*
 * Created on 2005-11-24
 *
 */
package net.joycool.wap.bean.news;

import java.util.Vector;

/**
 * @author lbj
 *
 */
public class NewsBean {
    int id;
    String title;
    String releaseDate;
    String content;
    int havePic;
    String type;
    int catalogId;
    int hits;
    Vector attachList;
    String linkUrl;
    
    
    
    
    /**
     * @return Returns the linkUrl.
     */
    public String getLinkUrl() {
        return linkUrl;
    }
    /**
     * @param linkUrl The linkUrl to set.
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
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
     * @return Returns the attachList.
     */
    public Vector getAttachList() {
        return attachList;
    }
    /**
     * @param attachList The attachList to set.
     */
    public void setAttachList(Vector attachList) {
        this.attachList = attachList;
    }
    /**
     * @return Returns the catalogId.
     */
    public int getCatalogId() {
        return catalogId;
    }
    /**
     * @param catalogId The catalogId to set.
     */
    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
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
     * @return Returns the havePic.
     */
    public int getHavePic() {
        return havePic;
    }
    /**
     * @param havePic The havePic to set.
     */
    public void setHavePic(int havePic) {
        this.havePic = havePic;
    }
    /**
     * @return Returns the hits.
     */
    public int getHits() {
        return hits;
    }
    /**
     * @param hits The hits to set.
     */
    public void setHits(int hits) {
        this.hits = hits;
    }
    /**
     * @return Returns the releaseDate.
     */
    public String getReleaseDate() {
        if(releaseDate != null){
            if(releaseDate.indexOf("-") != -1){
                releaseDate = releaseDate.substring(releaseDate.indexOf("-") + 1);
            }
            if(releaseDate.lastIndexOf(":") != -1){
                releaseDate = releaseDate.substring(0, releaseDate.lastIndexOf(":"));
            }
        }
        return releaseDate;
    }
    /**
     * @param releaseDate The releaseDate to set.
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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
}
