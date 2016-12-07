/*
 * Created on 2006-12-10
 *
 */
package net.wxsj.bean;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-10
 * 
 * 说明：
 */
public class ZippoMobileBean {
    public int id;

    public int userId;

    public String mobile;

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

    /**
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
