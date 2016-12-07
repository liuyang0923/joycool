/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.mont;

/**
 * @author lbj
 *
 */
public class OrderRes {
    String transactionId;
    int hRet;    
    
    /**
     * @return Returns the hRet.
     */
    public int getHRet() {
        return hRet;
    }
    /**
     * @param ret The hRet to set.
     */
    public void setHRet(int ret) {
        hRet = ret;
    }
    /**
     * @return Returns the transactionId.
     */
    public String getTransactionId() {
        return transactionId;
    }
    /**
     * @param transactionId The transactionId to set.
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
