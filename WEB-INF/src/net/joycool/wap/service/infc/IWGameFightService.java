package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.wgamefight.WGameFightBean;
import net.joycool.wap.bean.wgamefight.WGameFightUserBean;

/** 
 * @author guip
 * @explain：
 * @datetime:2007-10-22 14:26:18
 */
public interface IWGameFightService {
	public int getWGameFightCount(String condition);
	public Vector getWGameFightList(String condition);
	 public boolean addWGameFight(WGameFightBean fight);
	 public boolean updateWGameFight(String set, String condition);
	 public boolean updateWGameFightUser(String set, String condition);
	 public boolean deleteWGameFight(String condition);
}
