/*
 * Created on 2007-8-28
 *
 */
package net.wxsj.bean.flush;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-28
 * 
 * 说明：
 */
public class MobileBean {
    public int id;

    public int linkId;

    public String date;

    public String mobile;

    public String createDatetime;

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
     * @return Returns the date.
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     *            The date to set.
     */
    public void setDate(String date) {
        this.date = date;
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
     * @return Returns the linkId.
     */
    public int getLinkId() {
        return linkId;
    }

    /**
     * @param linkId
     *            The linkId to set.
     */
    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    /**
     * @return Returns the mobile.
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            The mobile to set.
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
