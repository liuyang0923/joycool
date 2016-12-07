/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.wgamehall.WGameHallBean;

/**
 * @author lbj
 *
 */
public interface IWGameHallService {          
    public boolean addWGameHall(WGameHallBean hall);
    public boolean updateWGameHall(String set, String condition);
    public boolean deleteWGameHall(String condition);
    public WGameHallBean getWGameHall(String condition);
    public Vector getWGameHallList(String condition);
    public int getWGameHallCount(String condition);
}
