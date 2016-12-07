/*
 * Created on 2007-8-28
 *
 */
package net.wxsj.service.impl;

import java.util.ArrayList;

import net.wxsj.bean.flush.HistoryBean;
import net.wxsj.bean.flush.LinkBean;
import net.wxsj.bean.flush.MobileBean;
import net.wxsj.service.infc.IFlushService;
import net.wxsj.util.db.DbOperation;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-28
 * 
 * 说明：
 */
public class FlushServiceImpl extends BaseServiceImpl implements IFlushService{
    public FlushServiceImpl(int useConnType, DbOperation dbOp) {
        this.useConnType = useConnType;
        this.dbOp = dbOp;
    }

    public FlushServiceImpl() {
        this.useConnType = CONN_IN_METHOD;
    }
    
    public String linkTableName = "wxsj_flush_link";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addLink(LinkBean bean) {
        return addXXX(bean, linkTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteLink(String condition) {
        return deleteXXX(condition, linkTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public LinkBean getLink(String condition) {
        return (LinkBean) getXXX(condition, linkTableName,
                "net.wxsj.bean.flush.LinkBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getLinkCount(String condition) {
        return getXXXCount(condition, linkTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getLinkList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, linkTableName,
                "net.wxsj.bean.flush.LinkBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateLink(String set, String condition) {
        return updateXXX(set, condition, linkTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getLinkList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, linkTableName,
                "net.wxsj.bean.flush.LinkBean");
    }
    
    public String historyTableName = "wxsj_flush_history";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addHistory(HistoryBean bean) {
        return addXXX(bean, historyTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteHistory(String condition) {
        return deleteXXX(condition, historyTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public HistoryBean getHistory(String condition) {
        return (HistoryBean) getXXX(condition, historyTableName,
                "net.wxsj.bean.flush.HistoryBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getHistoryCount(String condition) {
        return getXXXCount(condition, historyTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getHistoryList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, historyTableName,
                "net.wxsj.bean.flush.HistoryBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateHistory(String set, String condition) {
        return updateXXX(set, condition, historyTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getHistoryList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, historyTableName,
                "net.wxsj.bean.flush.HistoryBean");
    }
    
    public String mobileTableName = "wxsj_flush_mobile";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addMobile(MobileBean bean) {
        return addXXX(bean, mobileTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteMobile(String condition) {
        return deleteXXX(condition, mobileTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public MobileBean getMobile(String condition) {
        return (MobileBean) getXXX(condition, mobileTableName,
                "net.wxsj.bean.flush.MobileBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getMobileCount(String condition) {
        return getXXXCount(condition, mobileTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getMobileList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, mobileTableName,
                "net.wxsj.bean.flush.MobileBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateMobile(String set, String condition) {
        return updateXXX(set, condition, mobileTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getMobileList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, mobileTableName,
                "net.wxsj.bean.flush.MobileBean");
    }
}
