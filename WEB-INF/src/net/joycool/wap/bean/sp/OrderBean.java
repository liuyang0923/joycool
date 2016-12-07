/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.bean.sp;

/**
 * @author lbj
 *
 */
public class OrderBean {
    public static int ORDER = 1;
    public static int CANCEL = 0;
    
    int id;
    String mid;
    String mobile;
    int spServiceId;
    String serviceId;
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
     * @return Returns the mobile.
     */
    public String getMobile() {
        return mobile;
    }
    /**
     * @param mobile The mobile to set.
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
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
     * @return Returns the spServiceId.
     */
    public int getSpServiceId() {
        return spServiceId;
    }
    /**
     * @param spServiceId The spServiceId to set.
     */
    public void setSpServiceId(int spServiceId) {
        this.spServiceId = spServiceId;
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
}
