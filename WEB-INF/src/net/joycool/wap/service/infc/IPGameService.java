/*
 * Created on 2005-12-30
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.pgame.PGameBean;
import net.joycool.wap.bean.pgame.PGameProviderBean;
import net.joycool.wap.bean.pgame.UserOrderBean;

/**
 * @author lbj
 *
 */
public interface IPGameService {
    public PGameBean getPGame(String condition);
    public Vector getPGameList(String condition);
    public boolean updatePGame(String set, String condition);
    public int getPGameCount(String condition);
    public PGameProviderBean getProvider(String condition);
    
    public UserOrderBean getUserOrder(String condition);
    public boolean addUserOrder(UserOrderBean userOrder);
}
