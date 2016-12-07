/*
 * Created on 2005-12-8
 *
 */
package net.joycool.wap.bean.game;

import net.joycool.wap.util.Constants;

/**
 * @author lbj
 *  
 */
public class GameBean {

    int id;

    String name;

    String description;

    String fitMobile;

    int kb;

    String fileUrl;

    String remoteUrl;

    String picUrl;

    int hits;

    int catalogId;

    int createUserId;

    String createDatetime;

    int updateUserId;

    String updateDatetime;

    int providerId;

    String linkUrl;

    /**
     * @return Returns the linkUrl.
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * @param linkUrl
     *            The linkUrl to set.
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * @return Returns the catalogId.
     */
    public int getCatalogId() {
        return catalogId;
    }

    /**
     * @param catalogId
     *            The catalogId to set.
     */
    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
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
     * @return Returns the createUserId.
     */
    public int getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId
     *            The createUserId to set.
     */
    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the fileUrl.
     */
    public String getFileUrl() {
        return fileUrl;
    }
    
    public String getRealFileUrl() {
        return Constants.GAME_RESOURCE_ROOT_URL + "jar/" + fileUrl;
    }

    /**
     * @param fileUrl
     *            The fileUrl to set.
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * @return Returns the fitMobile.
     */
    public String getFitMobile() {
        return fitMobile;
    }

    /**
     * @param fitMobile
     *            The fitMobile to set.
     */
    public void setFitMobile(String fitMobile) {
        this.fitMobile = fitMobile;
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
     * @return Returns the kb.
     */
    public int getKb() {
        return kb;
    }

    /**
     * @param kb
     *            The kb to set.
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
     * @param name
     *            The name to set.
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
    
    public String getRealPicUrl() {
        return Constants.GAME_RESOURCE_ROOT_URL + "pic/" + picUrl;
    }

    /**
     * @param picUrl
     *            The picUrl to set.
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
     * @param providerId
     *            The providerId to set.
     */
    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    /**
     * @return Returns the remoteUrl.
     */
    public String getRemoteUrl() {
        return remoteUrl;
    }

    /**
     * @param remoteUrl
     *            The remoteUrl to set.
     */
    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    /**
     * @return Returns the updateDatetime.
     */
    public String getUpdateDatetime() {
        return updateDatetime;
    }

    /**
     * @param updateDatetime
     *            The updateDatetime to set.
     */
    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    /**
     * @return Returns the updateUserId.
     */
    public int getUpdateUserId() {
        return updateUserId;
    }

    /**
     * @param updateUserId
     *            The updateUserId to set.
     */
    public void setUpdateUserId(int updateUserId) {
        this.updateUserId = updateUserId;
    }
}
