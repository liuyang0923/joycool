/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.mont;

/**
 * @author lbj
 *
 */
public class Address {
    String deviceType;
    String deviceId;
    
    
    /**
     * @return Returns the deviceId.
     */
    public String getDeviceId() {
        return deviceId;
    }
    /**
     * @param deviceId The deviceId to set.
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    /**
     * @return Returns the deviceType.
     */
    public String getDeviceType() {
        return deviceType;
    }
    /**
     * @param deviceType The deviceType to set.
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
