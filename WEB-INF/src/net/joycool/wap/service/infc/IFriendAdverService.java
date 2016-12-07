/**
 * 
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.friendadver.FriendAdverBean;

/**
 * @author zhul 2006-06-20 处理交友广告信息的db类接口
 */
public interface IFriendAdverService {

	public boolean addFriendAdver(FriendAdverBean friendAdver);

	public FriendAdverBean getFriendAdver(String condition);

	public Vector getFriendAdverList(String condition);

	public boolean deleteFriendAdver(String condition);

	public boolean updateFriendAdver(String set, String condition);

	public int getFriendAdverCount(String condition);

	//macq_2006-11-22_交友广告缓存_start
	public int getFriendAdverCacheCount(String condition);
	
	public Vector getFriendAdverCacheList(String condition);
	//macq_2006-11-22_交友广告缓存_end
}
