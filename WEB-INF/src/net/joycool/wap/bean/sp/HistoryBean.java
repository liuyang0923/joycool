/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.bean.sp;

/**
 * @author lbj
 *  
 */
public class HistoryBean {
    public static int ORDER = 1;

    public static int CANCEL = 0;

    int id;

    String mid;

    int operType;

    String operDatetime;

    String serviceId;

    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId
     *            The serviceId to set.
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
     * @return Returns the mid.
     */
    public String getMid() {
        return mid;
    }

    /**
     * @param mid
     *            The mid to set.
     */
    public void setMid(String mid) {
        this.mid = mid;
    }

    /**
     * @return Returns the operDatetime.
     */
    public String getOperDatetime() {
        return operDatetime;
    }

    /**
     * @param operDatetime
     *            The operDatetime to set.
     */
    public void setOperDatetime(String operDatetime) {
        this.operDatetime = operDatetime;
    }

    /**
     * @return Returns the operType.
     */
    public int getOperType() {
        return operType;
    }

    /**
     * @param operType
     *            The operType to set.
     */
    public void setOperType(int operType) {
        this.operType = operType;
    }
}
