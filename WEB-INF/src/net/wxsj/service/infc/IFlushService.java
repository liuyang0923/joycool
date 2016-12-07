/*
 * Created on 2007-8-28
 *
 */
package net.wxsj.service.infc;

import java.util.ArrayList;

import net.wxsj.bean.flush.HistoryBean;
import net.wxsj.bean.flush.LinkBean;
import net.wxsj.bean.flush.MobileBean;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-28
 * 
 * 说明：
 */
public interface IFlushService extends IBaseService{
    //  link
    public boolean addLink(LinkBean bean);

    public boolean updateLink(String set, String condition);

    public boolean deleteLink(String condition);

    public int getLinkCount(String condition);

    public LinkBean getLink(String condition);

    public ArrayList getLinkList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getLinkList(String query, String fieldPrefix);

    //  history
    public boolean addHistory(HistoryBean bean);

    public boolean updateHistory(String set, String condition);

    public boolean deleteHistory(String condition);

    public int getHistoryCount(String condition);

    public HistoryBean getHistory(String condition);

    public ArrayList getHistoryList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getHistoryList(String query, String fieldPrefix);

    //  mobile
    public boolean addMobile(MobileBean bean);

    public boolean updateMobile(String set, String condition);

    public boolean deleteMobile(String condition);

    public int getMobileCount(String condition);

    public MobileBean getMobile(String condition);

    public ArrayList getMobileList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getMobileList(String query, String fieldPrefix);

}
