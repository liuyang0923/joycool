/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.wgame.WGamePKBean;
import net.joycool.wap.bean.wgame.big.HistoryBigBean;
import net.joycool.wap.bean.wgame.big.WGamePKBigBean;

/**
 * @author lbj
 *  
 */
public interface IWGamePKService {
    public boolean addWGamePK(WGamePKBean dice);

    public boolean updateWGamePK(String set, String condition);

    public boolean deleteWGamePK(String condition);

    public WGamePKBean getWGamePK(String condition);

    public Vector getWGamePKList(String condition);

    public int getWGamePKCount(String condition);
    
    
    public boolean addWGamePKBig(WGamePKBigBean dice);

    public boolean updateWGamePKBig(String set, String condition);

    public boolean deleteWGamePKBig(String condition);

    public WGamePKBigBean getWGamePKBig(String condition);

    public Vector getWGamePKBigList(String condition);
    
    public Vector getWGamePKBigListByCache(String condition);

    public int getWGamePKBigCount(String condition);
    
    public HistoryBigBean getHistoryBig(String condition);
    public boolean addHistoryBig(HistoryBigBean history);
    public Vector getHistoryBigList(String condition);
    public int getHistoryBigCount(String condition);
    public boolean updateHistoryBig(String set, String condition);
    public boolean deleteHistoryBig(String condition);
}
