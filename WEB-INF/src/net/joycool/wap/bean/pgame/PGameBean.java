/*
 * Created on 2005-12-30
 *
 */
package net.joycool.wap.bean.pgame;

import net.joycool.wap.util.Constants;

/**
 * @author lbj
 *
 */
public class PGameBean {
    int id;
    String name;
    String description;
    String fitMobile;
    int kb;
    String picUrl;
    int providerId;
    String gameFileUrl;
    int downloadSum;
    String linkUrl;
    int free;
    
    /**
     * @return Returns the free.
     */
    public int getFree() {
        return free;
    }
    /**
     * @param free The free to set.
     */
    public void setFree(int free) {
        this.free = free;
    }
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
     * @return Returns the downloadSum.
     */
    public int getDownloadSum() {
        return downloadSum;
    }
    /**
     * @param downloadSum The downloadSum to set.
     */
    public void setDownloadSum(int downloadSum) {
        this.downloadSum = downloadSum;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the fitMobile.
     */
    public String getFitMobile() {
        return fitMobile;
    }
    /**
     * @param fitMobile The fitMobile to set.
     */
    public void setFitMobile(String fitMobile) {
        this.fitMobile = fitMobile;
    }
    /**
     * @return Returns the gameFileUrl.
     */
    public String getGameFileUrl() {
        return gameFileUrl;
    }
    /**
     * @param gameFileUrl The gameFileUrl to set.
     */
    public void setGameFileUrl(String gameFileUrl) {
        this.gameFileUrl = gameFileUrl;
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
     * @return Returns the kb.
     */
    public int getKb() {
        return kb;
    }
    /**
     * @param kb The kb to set.
     */
    public void setKb(int kb) {
        this.kb = kb;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the picUrl.
     */
    public String getPicUrl() {
        return picUrl;
    }
    /**
     * @param picUrl The picUrl to set.
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    /**
     * @return Returns the providerId.
     */
    public int getProviderId() {
        return providerId;
    }
    /**
     * @param providerId The providerId to set.
     */
    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }
    
    public String getRealFileUrl(){
        return Constants.PGAME_RESOURCE_ROOT_URL + this.gameFileUrl;
    }
    
    public String getRealPicUrl(){
        return "http://www.joycool.net/" + this.picUrl;
    }
}
