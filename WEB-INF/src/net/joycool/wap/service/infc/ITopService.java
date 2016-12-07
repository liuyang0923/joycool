package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.top.MoneyTopBean;
import net.joycool.wap.bean.top.UserTopBean;

public interface ITopService {
	public MoneyTopBean getMoneyTop(String condition);
	public Vector getMoneyTopList(String condition);
	public boolean addMoneyTop(MoneyTopBean bean);
	public boolean delMoneyTop(String condition);
	public boolean updateMoneyTop(String set, String condition);
	public int getMoneyTopCount(String condition);
	

	public UserTopBean getUserTop(String condition);
	public Vector getUserTopList(String condition);
	public boolean addUserTop(UserTopBean bean);
	public boolean delUserTop(String condition);
	public boolean updateUserTop(String set, String condition);
	public int getUserTopCount(String condition);
	
	public long getUserLoadTotal(String sql);
}
