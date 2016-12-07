/*
 * Created on 2005-9-13
 *
 */
package net.joycool.wap.service.infc;

import java.util.List;

import net.joycool.wap.bean.AllyBean;
import net.joycool.wap.bean.broadcast.BroadcastBean;

/**
 * @author Bomb
 *  
 */
public interface IBroadcastService {
	
    public List getBroadcast(int start, int limit);
    
    public BroadcastBean getBroadcast(int id);
    
    public void deleteBroadcast(int id);
    
    public void updateBroadcast(int id, String broadcaster, String msg);;
    
    public int getBroadcastNum();
    
    public boolean addWapAlly(String name, String title, String url, String contact);	// 返回新建的id
    
    public AllyBean getWapAlly(String name);
    
    public void addBroadcast(String broadcaster, String msg);    
    
}
