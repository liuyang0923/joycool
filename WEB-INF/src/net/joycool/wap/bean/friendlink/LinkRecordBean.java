package net.joycool.wap.bean.friendlink;

/*
 * Created on 2005-2-24
 *  
 */

public class LinkRecordBean {
    /**
     * @author mcq
     *  
     */
    int id;

    int linkId;

    String name;

    String url;

    int typeId;

    int mark;

    String createDatetime;

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
     * @return Returns the linkid.
     */
    public int getLinkId() {
        return linkId;
    }

    /**
     * @param linkid
     *            The linkid to set.
     */
    public void setLinkId(int linkid) {
        this.linkId = linkid;
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
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Returns the typeid.
     */
    public int getType_Id() {
        return typeId;
    }

    /**
     * @param typeid
     *            The typeid to set.
     */
    public void setType_Id(int typeid) {
        this.typeId = typeid;
    }

    /**
     * @return Returns the mark.
     */
    public int getMark() {
        return mark;
    }

    /**
     * @param mark
     *            The mark to set.
     */
    public void setMark(int mark) {
        this.mark = mark;
    }
}
