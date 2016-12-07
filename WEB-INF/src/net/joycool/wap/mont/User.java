/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.mont;

/**
 * @author lbj
 *
 */
public class User {
    String userIdType;
    String msisdn;
    String pseudoCode;
    
    
    /**
     * @return Returns the msisdn.
     */
    public String getMsisdn() {
        return msisdn;
    }
    /**
     * @param msisdn The msisdn to set.
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    /**
     * @return Returns the pseudoCode.
     */
    public String getPseudoCode() {
        return pseudoCode;
    }
    /**
     * @param pseudoCode The pseudoCode to set.
     */
    public void setPseudoCode(String pseudoCode) {
        this.pseudoCode = pseudoCode;
    }
    /**
     * @return Returns the userIdType.
     */
    public String getUserIdType() {
        return userIdType;
    }
    /**
     * @param userIdType The userIdType to set.
     */
    public void setUserIdType(String userIdType) {
        this.userIdType = userIdType;
    }
}
