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
public class TagBean {
    public static int HOT = 1;

    public static int NORMAL = 0;

    public int id;

    public String name;

    public String createDatetime;

    public int createUserId;

    public int mark;

    public int displayOrder;

    /**
     * @return Returns the displayOrder.
     */
    public int getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
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
}
