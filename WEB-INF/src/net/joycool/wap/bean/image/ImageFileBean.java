/*
 * Created on 2005-11-30
 *
 */
package net.joycool.wap.bean.image;

import net.joycool.wap.util.Constants;

/**
 * @author lbj
 *
 */
public class ImageFileBean {
    int id;
    String code;
    String spec;
    String type;
    int size;
    String fileUrl;
    String realFileUrl;
    
    
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
     * @return Returns the realFileUrl.
     */
    public String getRealFileUrl() {
        return Constants.IMAGE_RESOURCE_ROOT_URL + getFileUrl();
    }
    /**
     * @param realFileUrl The realFileUrl to set.
     */
    public void setRealFileUrl(String realFileUrl) {
        this.realFileUrl = realFileUrl;
    }
    /**
     * @return Returns the size.
     */
    public int getSize() {
        return size;
    }
    /**
     * @param size The size to set.
     */
    public void setSize(int size) {
        this.size = size;
    }
    /**
     * @return Returns the spec.
     */
    public String getSpec() {
        return spec;
    }
    /**
     * @param spec The spec to set.
     */
    public void setSpec(String spec) {
        this.spec = spec;
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
