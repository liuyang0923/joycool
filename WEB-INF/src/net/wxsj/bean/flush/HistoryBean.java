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
public class HistoryBean {
    public int id;

    public int linkId;

    public String date;

    public int hitsCount;

    public int mobileCount;

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
     * @return Returns the hitsCount.
     */
    public int getHitsCount() {
        return hitsCount;
    }

    /**
     * @param hitsCount
     *            The hitsCount to set.
     */
    public void setHitsCount(int hitsCount) {
        this.hitsCount = hitsCount;
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
     * @return Returns the mobileCount.
     */
    public int getMobileCount() {
        return mobileCount;
    }

    /**
     * @param mobileCount
     *            The mobileCount to set.
     */
    public void setMobileCount(int mobileCount) {
        this.mobileCount = mobileCount;
    }
}
