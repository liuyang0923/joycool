/*
 * Created on 2005-12-21
 *
 */
package net.joycool.wap.bean;

/**
 * @author lbj
 *
 */
public class WapServiceBean {
    public static int BY_MONTH = 1;
    public static int BY_COUNT = 2;
    public static int FORUM = 3;
    
    int id;
    String serviceId;
    String name;
    String orderAddress;
    int type;        
    
    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(int type) {
        this.type = type;
    }
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
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the orderAddress.
     */
    public String getOrderAddress() {
        return orderAddress;
    }
    /**
     * @param orderAddress The orderAddress to set.
     */
    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
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
}
