/*
 * Created on 2006-12-10
 *
 */
package net.wxsj.bean;

import net.joycool.wap.util.Constants;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-10
 * 
 * 说明：
 */
public class ZippoStarBean {
    public int id;

    public String name;

    public String introduction;

    public int typeId;

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
     * @return Returns the introduction.
     */
    public String getIntroduction() {
        return introduction;
    }
    /**
     * @param introduction The introduction to set.
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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
     * @return Returns the typeId.
     */
    public int getTypeId() {
        return typeId;
    }
    /**
     * @param typeId The typeId to set.
     */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    
    public String getImageUrl() {
        return Constants.URL_PREFIX + "/wxsj/zippo/images/star/" + id + ".gif";
    }
}
