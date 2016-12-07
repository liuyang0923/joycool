/*
 * Created on 2005-12-30
 *
 */
package net.joycool.wap.bean.pgame;

/**
 * @author lbj
 *
 */
public class UserOrderBean {
    int id;
    int userId;
    String serviceId;
    String mid;
    String orderDatetime;
    int status;
    
    
    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }
    /**
     * @param id The id to set.
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
     * @param mid The mid to set.
     */
    public void setMid(String mid) {
        this.mid = mid;
    }
    /**
     * @return Returns the orderDatetime.
     */
    public String getOrderDatetime() {
        return orderDatetime;
    }
    /**
     * @param orderDatetime The orderDatetime to set.
     */
    public void setOrderDatetime(String orderDatetime) {
        this.orderDatetime = orderDatetime;
    }
    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }
    /**
     * @param serviceId The serviceId to set.
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    /**
     * @return Returns the status.
     */
    public int getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
