/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.wgame.HistoryBean;

/**
 * @author lbj
 *
 */
public interface IWGameService {
    
    public HistoryBean getHistory(String condition);
    public boolean addHistory(HistoryBean history);
    public Vector getHistoryList(String condition);
    public int getHistoryCount(String condition);
    public boolean updateHistory(String set, String condition);
    public boolean deleteHistory(String condition);
}
