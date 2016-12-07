/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.service.infc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.ActionRecordBean;
import net.joycool.wap.bean.BlackListUserBean;
import net.joycool.wap.bean.CartBean;
import net.joycool.wap.bean.CrownBean;
import net.joycool.wap.bean.OnlineBean;
import net.joycool.wap.bean.RankActionBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserFriendBean;
import net.joycool.wap.bean.UserHonorBean;
import net.joycool.wap.bean.UserMoneyLogBean;
import net.joycool.wap.bean.UserNoteBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;

/**
 * @author lbj
 * 
 */
public interface IUserService {
	public boolean addUser(UserBean user);

	public UserBean getUser(String condition);
	//macq_2007-3-19_获取用户最后一次登录用户信息_start
	public UserBean getLastLoginUser(String condition);
	public UserBean getLastLoginUserByMid(String mid);
	//macq_2007-3-19_获取用户最后一次登录用户信息_end

	public Vector getUserList(String condition);

	public int getUserCount(String condition);

	public int getFriendCount(String condition);

	public boolean updateUser(String set, String condition);

	public Vector getFriends(String condition);

	public boolean addFriend(int userId, int friendId);

	public boolean deleteFriend(int userId, int friendId);
	
	//liuyi 2006-10-31 获取某个用户的金兰id
	public List getJyFriendIds(int userId);
	
	public boolean updateFriend(String set, String condition);

	public boolean isUserFriend(int self, int userId);

	public Vector getBadGuys(String condition);

	public boolean addBadGuy(int userId, int friendId);

	public boolean deleteBadGuy(int userId, int friendId);

	public boolean isUserBadGuy(int self, int userId);

	public Vector getCartList(String condition);

	public int getCartCount(String condition);

	public boolean addCart(CartBean cart);

	public boolean deleteCart(String condition);

	public int getOnlineUserCount(String condition);

	// zhul_add new method 2006-06-23
	public OnlineBean getOnlineUser(String condition);

	public boolean addOnlineUser(OnlineBean online);

	public boolean deleteOnlineUser(String condition);

	public boolean updateOnlineUser(String set, String condition);

	// zhul-2006-06-06 start 将原IWGameService下的所有关于UserStatus的方法移到IUserService
	public UserStatusBean getUserStatus(String condition);

	public Vector getUserStatusList(String condition, boolean getUser);

	public boolean addUserStatus(UserStatusBean status);

	public boolean updateUserStatus(String set, String condition);

	public int getUserStatusCount(String condition);

	// zhul-2006-06-06 end 将原IWGameService下的所有关于UserStatus的方法移到IUserService

	// mqc_start_增加获取jc_rank记录的方法 时间 2006-6-7
	public Vector getRankList(String condition);

	// mqc_start_增加通过条件获取jc_rank_action记录的方法 时间 2006-6-7
	public Vector getRankActionList(String condition);

	// mqc_start_增加通过条件获取jc_rank_action记录的方法 时间 2006-6-8
	public RankActionBean getRankAction(String condition);

	// mqc_end

	// zt_start_获取jc_online_user的列表 时间 2006-6-9
	/**
	 * user_info a <br />
	 * user_status b <br />
	 * jc_online_user c <br />
	 * condition 查询条件：使用表别名加上表的字段来查询。 eg: c.position_id <> 0 AND c.position_id <>
	 * 1 <br />
	 * limitCount 需要的数量： 0 表示全部 != 0 表示需要limitCount 个记录 <br />
	 */
	public Vector getOnlineUserList(String condition);

	// zt_end
	// mqc_start_添加用户发送动作记录 时间 2006-6-9
	public boolean addActionRecord(ActionRecordBean actionRecord);

	// mqc_end

	// mqc_start_添加用户发送动作记录 时间 2006-6-9
	public Vector getActionRecordList(String condition);

	// mqc_end

	// mqc_start_通过条件返回用户动作记录数量 时间 2006-6-9
	public int getActionRecordCount(String condition);

	// mqc_end

	// mqc_start_通过条件返回等级信息数量 时间 2006-6-9
	public int getRankActionCount(String condition);

	// mqc_end
	// fanys_start 时间 2006-6-15
	// 获取动作列表，根据动作发送次数多少和动作等级进行排序
	public Vector getRankActionListOrderByAction(String strsql);

	// fanys--end

	// zhangyi_start_在用户表中查询所有城市 时间 2006-6-20
	public Vector getCityFromUserInfor();

	// zhangyi_end_在用户表中查询所有城市 时间 2006-6-20

	// zhangyi_end_每天一个用户的登录次数 时间 2006-6-30
	public int getDaysLoginUserCount(String condition);

	// zhangyi_end_每天一个用户的登录次数 时间 2006-6-30

	// zhangyi_start_每天一个用户表添加 时间 2006-6-30
	public boolean addDaysLoginUse(int userId);

	// zhangyi_end_每天一个用户表添加 时间 2006-6-30

	// mcq_2006-9-1_删除用户登录表 时间_start
	public boolean delDaysLoginUse(String condition);

	// mcq_2006-9-1_删除用户登录表 时间_end

	// mcq_2006-9-4_查询用户最后登录ID_start
	public Vector getUserLogoutIdList(String condition);

	// mcq_2006-9-4_查询用户最后登录ID_end
	//fanys 2006_09_07 start 王冠
	public CrownBean getCrown(String condition);

	public boolean addCrown(CrownBean crown);

	public boolean deleteCrown(String condition);

	public boolean updateCrown(String set, String condition);

	public Vector getCrownList(String condition);

	public int getCrownCount(String condition);
	//fanys 2006_09_07 end 王冠
	
	// mcq_2006-9-25_获取用户好友列表_start
	public Vector getUserFriendList(String condition);
	// mcq_2006-9-25_获取用户好友列表_end
	//zhul 2006-10-12 获取在线用户id列表
	public ArrayList getOnlineUserIdList();
	//zhul 2006-10-13 获取用户好友列表
	public ArrayList getUserFriendList(int userId);
	
	//liuyi 2006-10-28 获取UserFriendBean start
	public UserFriendBean getUserFriend(int userId, int friendId);
	
	public UserFriendBean getUserFriend(String condition);
	//liuyi 2006-10-28 获取UserFriendBean end
	
	//liuyi 2006-10-31 判断有无该两个用户的好友记录，有则更新友好度 start
	public boolean addOrupdateFriendLevel(int fromId, int toId);
	//liuyi 2006-10-31 判断有无该两个用户的好友记录，有则更新友好度 end
	
    //macq_2006-12-12_用户行囊_start
	public boolean addUserBag(UserBagBean bean);
	public UserBagBean getUserBag(String condition);
	public Vector getUserBagList(String condition);
	public boolean deleteUserBag(String condition);
	public boolean updateUserBag(String set, String condition);
	public int getUserBagCount(String condition);
    //macq_2006-12-12_用户行囊-end
	
	public HashMap getItemComposeMap(String condition);		// 取得合成公式
	public HashMap getItemShowMap(String condition);		// 取得物品对应显示图片
	
	//	更新用户mid
	public void updateMid(UserBean user, String mid);	
	
	public boolean addMoneyLog(UserMoneyLogBean bean);
	public UserMoneyLogBean getMoneyLog(String condition);
	public List getMoneyLogList(String condition);
	public int getMoneyLogCount(String condition);
	
	public boolean addUserSetting(UserSettingBean bean);
	public UserSettingBean getUserSetting(String condition);
	public boolean updateUserSetting(String set, String condition);

	public UserHonorBean getUserHonor(String condition);
	public void calcHonorPlace();
	public boolean updateUserHonor(String set, String condition);

	public void addUserHonor(UserHonorBean bean);

	public HashMap getUserBagMap(String condition);
	
	public List getShortcutList();
	
	//网站用户黑名单
	public boolean addBlackListUser(BlackListUserBean bean);
	public BlackListUserBean getBlackListUser(String condition);
	public List getBlackListUserList(String condition);
	public boolean deleteBlackListUser(String condition);
	public boolean updateBlackListUser(String set, String condition);
	public int getBlackListUserCount(String condition);

	public void addItemLog(int userId, int toUserId, int item, int userBagId, int type);
	public void addItemLog(int userId, int toUserId, int item, int userBagId, int stack, int type);
	
	// 用户备注
	public List getUserNoteList(String condition);
	public boolean addUserNote(UserNoteBean note);
	public boolean updateUserNote(String set, String condition);
	public void removeUserNote(String condition);
	
	// user interval for 每周幸运抽奖
	public boolean addUserInterval(int type, int userId, int refresh);
	
	public void flushUserFriend(int fromId, int toId);
	// 刷新单方向的好友数据
	public void flushUserFriendSingle(int fromId, int toId);
}
