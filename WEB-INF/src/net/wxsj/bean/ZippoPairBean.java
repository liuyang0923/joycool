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
public class ZippoPairBean {
    public int id;

    public int userId;

    public int starId;

    public int zippoId;

    public int typeId;

    public String createDatetime;
    
    public int result;        

    /**
     * @return Returns the result.
     */
    public int getResult() {
        return result;
    }
    /**
     * @param result The result to set.
     */
    public void setResult(int result) {
        this.result = result;
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
     * @return Returns the starId.
     */
    public int getStarId() {
        return starId;
    }

    /**
     * @param starId
     *            The starId to set.
     */
    public void setStarId(int starId) {
        this.starId = starId;
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

    /**
     * @return Returns the zippoId.
     */
    public int getZippoId() {
        return zippoId;
    }

    /**
     * @param zippoId
     *            The zippoId to set.
     */
    public void setZippoId(int zippoId) {
        this.zippoId = zippoId;
    }
}
