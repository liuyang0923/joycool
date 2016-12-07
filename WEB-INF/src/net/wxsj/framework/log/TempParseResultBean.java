/*
 * Created on 2007-2-5
 *
 */
package net.wxsj.framework.log;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-2-5
 * 
 * 说明：
 */
public class TempParseResultBean {
    int totalPv;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-10
     * 
     * 说明：key: sessionId value: pv数
     */
    Hashtable sessionPvs;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-10
     * 
     * 说明：key: pv数 value: session数
     */
    Hashtable pvSessions;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-10
     * 
     * 说明：key: mobile value: pv数
     */
    Hashtable mobilePvs;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-10
     * 
     * 说明：key: pv数 value: mobile数
     */
    Hashtable pvMobiles;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-10
     * 
     * 说明：key: userId value: pv数
     */
    Hashtable userPvs;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-10
     * 
     * 说明：key: pv数 value: user数
     */
    Hashtable pvUsers;

    int maxSpv = 0;

    int maxMpv = 0;

    int maxUpv = 0;

    /**
     * @return Returns the maxMpv.
     */
    public int getMaxMpv() {
        return maxMpv;
    }
    /**
     * @param maxMpv The maxMpv to set.
     */
    public void setMaxMpv(int maxMpv) {
        this.maxMpv = maxMpv;
    }
    /**
     * @return Returns the maxSpv.
     */
    public int getMaxSpv() {
        return maxSpv;
    }
    /**
     * @param maxSpv The maxSpv to set.
     */
    public void setMaxSpv(int maxSpv) {
        this.maxSpv = maxSpv;
    }
    /**
     * @return Returns the maxUpv.
     */
    public int getMaxUpv() {
        return maxUpv;
    }
    /**
     * @param maxUpv The maxUpv to set.
     */
    public void setMaxUpv(int maxUpv) {
        this.maxUpv = maxUpv;
    }
    public TempParseResultBean() {
        totalPv = 0;
        sessionPvs = new Hashtable();
        mobilePvs = new Hashtable();
        userPvs = new Hashtable();
    }

    /**
     * @return Returns the mobilePvs.
     */
    public Hashtable getMobilePvs() {        
        return mobilePvs;
    }

    /**
     * @param mobilePvs
     *            The mobilePvs to set.
     */
    public void setMobilePvs(Hashtable mobilePvs) {
        this.mobilePvs = mobilePvs;
    }

    /**
     * @return Returns the pvMobils.
     */
    public Hashtable getPvMobiles() {
        if (pvMobiles != null) {
            return pvMobiles;
        }
        
        pvMobiles = new Hashtable();
        int pv;
        Integer count = null;
        Collection col = mobilePvs.values();
        Iterator itr = col.iterator();
        while(itr.hasNext()){
            pv = ((Integer) itr.next()).intValue();
            if(pv > maxMpv){
                maxMpv = pv;
            }
            count = (Integer) pvMobiles.get(pv + "");
            if(count == null){
                pvMobiles.put(pv + "", new Integer(1));
            }
            else {
                pvMobiles.put(pv + "", new Integer(count.intValue() + 1));
            }
        }
        return pvMobiles;
    }

    /**
     * @param pvMobils
     *            The pvMobils to set.
     */
    public void setPvMobiles(Hashtable pvMobiles) {
        this.pvMobiles = pvMobiles;
    }

    /**
     * @return Returns the pvSessions.
     */
    public Hashtable getPvSessions() {
        if (pvSessions != null) {
            return pvSessions;
        }
        
        pvSessions = new Hashtable();
        int pv;
        Integer count = null;
        Collection col = sessionPvs.values();
        Iterator itr = col.iterator();
        while(itr.hasNext()){
            pv = ((Integer) itr.next()).intValue();
            if(pv > maxSpv){
                maxSpv = pv;
            }
            count = (Integer) pvSessions.get(pv + "");
            if(count == null){
                pvSessions.put(pv + "", new Integer(1));
            }
            else {
                pvSessions.put(pv + "", new Integer(count.intValue() + 1));
            }
        }
        return pvSessions;
    }

    /**
     * @param pvSessions
     *            The pvSessions to set.
     */
    public void setPvSessions(Hashtable pvSessions) {
        this.pvSessions = pvSessions;
    }

    /**
     * @return Returns the pvUsers.
     */
    public Hashtable getPvUsers() {
        if (pvUsers != null) {
            return pvUsers;
        }
        
        pvUsers = new Hashtable();
        int pv;
        Integer count = null;
        Collection col = userPvs.values();
        Iterator itr = col.iterator();
        while(itr.hasNext()){
            pv = ((Integer) itr.next()).intValue();
            if(pv > maxUpv){
                maxUpv = pv;
            }
            count = (Integer) pvUsers.get(pv + "");
            if(count == null){
                pvUsers.put(pv + "", new Integer(1));
            }
            else {
                pvUsers.put(pv + "", new Integer(count.intValue() + 1));
            }
        }
        return pvUsers;
    }

    /**
     * @param pvUsers
     *            The pvUsers to set.
     */
    public void setPvUsers(Hashtable pvUsers) {
        this.pvUsers = pvUsers;
    }

    /**
     * @return Returns the userPvs.
     */
    public Hashtable getUserPvs() {
        return userPvs;
    }

    /**
     * @param userPvs
     *            The userPvs to set.
     */
    public void setUserPvs(Hashtable userPvs) {
        this.userPvs = userPvs;
    }

    /**
     * @return Returns the sessionPvs.
     */
    public Hashtable getSessionPvs() {
        return sessionPvs;
    }

    /**
     * @param sessionPvs
     *            The sessionPvs to set.
     */
    public void setSessionPvs(Hashtable sessionPvs) {
        this.sessionPvs = sessionPvs;
    }

    /**
     * @return Returns the totalPv.
     */
    public int getTotalPv() {
        return totalPv;
    }

    /**
     * @param totalPv
     *            The totalPv to set.
     */
    public void setTotalPv(int totalPv) {
        this.totalPv = totalPv;
    }
}
