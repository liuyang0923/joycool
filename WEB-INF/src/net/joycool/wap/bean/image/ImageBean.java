/*
 * Created on 2005-11-30
 *
 */
package net.joycool.wap.bean.image;

import java.util.Vector;

/**
 * @author lbj
 *
 */
public class ImageBean {
    int id;
    String name;
    String code;
    int catalogId;
    int hits;
    ImageFileBean file7070;
    ImageFileBean file128128;
    Vector imageFiles;    
    
    
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
    String linkUrl;
    
    
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
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return Returns the file128128.
     */
    public ImageFileBean getFile128128() {
        return file128128;
    }
    /**
     * @param file128128 The file128128 to set.
     */
    public void setFile128128(ImageFileBean file128128) {
        this.file128128 = file128128;
    }
    /**
     * @return Returns the file7070.
     */
    public ImageFileBean getFile7070() {
        return file7070;
    }
    /**
     * @param file7070 The file7070 to set.
     */
    public void setFile7070(ImageFileBean file7070) {
        this.file7070 = file7070;
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
     * @return Returns the imageFiles.
     */
    public Vector getImageFiles() {
        return imageFiles;
    }
    /**
     * @param imageFiles The imageFiles to set.
     */
    public void setImageFiles(Vector imageFiles) {
        this.imageFiles = imageFiles;
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
}
