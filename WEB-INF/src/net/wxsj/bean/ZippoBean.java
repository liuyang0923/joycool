/*
 * Created on 2006-12-10
 *
 */
package net.wxsj.bean;

import net.joycool.wap.util.Constants;
import net.wxsj.framework.ZippoFrk;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-10
 * 
 * 说明：
 */
public class ZippoBean {
    public int id;

    public String name;

    public String summary;

    public String introduction;

    public int typeId;

    ZippoTypeBean type;

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
     * @return Returns the introduction.
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param introduction
     *            The introduction to set.
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
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the summary.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary
     *            The summary to set.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return Returns the type.
     */
    public ZippoTypeBean getType() {
        return ZippoFrk.getTypeById(typeId);
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(ZippoTypeBean type) {
        this.type = type;
    }

    /**
     * @return Returns the typeId.
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     *            The typeId to set.
     */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getImageUrl() {
        return Constants.URL_PREFIX + "/wxsj/zippo/images/zippo/" + id + ".gif";
    }
}
