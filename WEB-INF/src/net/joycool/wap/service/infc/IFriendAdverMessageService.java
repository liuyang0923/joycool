/**
 * 
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.friendadver.FriendAdverMessageBean;

/**
 * @author zhul 2006-06-20 处理交友广告信息评论的db类接口
 */
public interface IFriendAdverMessageService {

	public boolean addFriendAdverMessage(
			FriendAdverMessageBean friendAdverMessage);

	public FriendAdverMessageBean getFriendAdverMessage(String condition);

	public Vector getFriendAdverMessageList(String condition);

	public boolean deleteFriendAdverMessage(String condition);

	public boolean updateFriendAdverMessage(String set, String condition);

	public int getFriendAdverMessageCount(String condition);

}
