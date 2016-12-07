/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.bean.mall;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public class AreaTagInfoBean {
    public int id;

    public int tagId;

    public int infoId;

    public String createDatetime;

    public int adminId;

    /**
     * @return Returns the adminId.
     */
    public int getAdminId() {
        return adminId;
    }

    /**
     * @param adminId
     *            The adminId to set.
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
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
     * @return Returns the infoId.
     */
    public int getInfoId() {
        return infoId;
    }

    /**
     * @param infoId
     *            The infoId to set.
     */
    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    /**
     * @return Returns the tagId.
     */
    public int getTagId() {
        return tagId;
    }

    /**
     * @param tagId
     *            The tagId to set.
     */
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
