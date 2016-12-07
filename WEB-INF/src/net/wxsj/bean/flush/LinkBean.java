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
public class LinkBean {
    public int id;

    public String name;

    public String link;

    public String remark;

    public String createDatetime;

    public int maxHits;

    public int maxMobile;

    public int currentHits;

    public int currentMobile;

    public String lastUpdateTime;

    public String script;

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
     * @return Returns the currentHits.
     */
    public int getCurrentHits() {
        return currentHits;
    }

    /**
     * @param currentHits
     *            The currentHits to set.
     */
    public void setCurrentHits(int currentHits) {
        this.currentHits = currentHits;
    }

    /**
     * @return Returns the currentMobile.
     */
    public int getCurrentMobile() {
        return currentMobile;
    }

    /**
     * @param currentMobile
     *            The currentMobile to set.
     */
    public void setCurrentMobile(int currentMobile) {
        this.currentMobile = currentMobile;
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
     * @return Returns the lastUpdateTime.
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     *            The lastUpdateTime to set.
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return Returns the link.
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link
     *            The link to set.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return Returns the maxHits.
     */
    public int getMaxHits() {
        return maxHits;
    }

    /**
     * @param maxHits
     *            The maxHits to set.
     */
    public void setMaxHits(int maxHits) {
        this.maxHits = maxHits;
    }

    /**
     * @return Returns the maxMobile.
     */
    public int getMaxMobile() {
        return maxMobile;
    }

    /**
     * @param maxMobile
     *            The maxMobile to set.
     */
    public void setMaxMobile(int maxMobile) {
        this.maxMobile = maxMobile;
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
     * @return Returns the remark.
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            The remark to set.
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return Returns the script.
     */
    public String getScript() {
        return script;
    }

    /**
     * @param script
     *            The script to set.
     */
    public void setScript(String script) {
        this.script = script;
    }
}
