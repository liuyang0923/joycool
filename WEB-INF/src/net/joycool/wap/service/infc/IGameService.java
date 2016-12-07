/*
 * Created on 2005-12-8
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.game.GameBean;

/**
 * @author lbj
 *
 */
public interface IGameService {
    
    public GameBean getGame(String condition);
    public Vector getGamesList(String condition);
    public boolean updateGame(String set, String condition);
    public int getGamesCount(String condition);

}
