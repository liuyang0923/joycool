package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.charitarian.CharitarianBean;
import net.joycool.wap.bean.charitarian.CharitarianHistoryBean;

public interface ICharitarianService {
	public CharitarianBean getCharitarian(String condition);

	public Vector getCharitarianList(String condition);

	public boolean addCharitarian(CharitarianBean bean);

	public boolean delCharitarian(String condition);

	public boolean updateCharitarian(String set, String condition);

	public int getCharitarianCount(String condition);
	
	public CharitarianHistoryBean getCharitarianHistory(String condition);

	public Vector getCharitarianHistoryList(String condition);

	public boolean addCharitarianHistory(CharitarianHistoryBean bean);

	public boolean delCharitarianHistory(String condition);

	public boolean updateCharitarianHistory(String set, String condition);

	public int getCharitarianHistoryCount(String condition);
	
	public int getCharitarianHistoryCacheCount(String condition);
	
	public Vector getCharitarianHistoryCacheList(String condition);
}
