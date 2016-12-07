// mcq_1_total 时间 2006-6-12
package net.joycool.wap.util;

import java.util.*;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.ShortcutBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserHonorBean;
import net.joycool.wap.bean.UserNoteBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.cache.*;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IUserCashService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.farm.FarmUserBean;
import net.joycool.wap.spec.farm.FarmWorld;
import net.joycool.wap.util.db.DbOperation;

public class UserInfoUtil {
	public static ICacheMap userInfoCache = CacheManage.userInfo;
	public static ICacheMap userStatusCache = CacheManage.userStatus;
	public static ICacheMap userHonorCache = CacheManage.userHonor;
	public static ICacheMap userNoteCache = CacheManage.userNote;
	public static ICacheMap userFriendsCache = CacheManage.userFriends;
	public static ICacheMap shortcutCache = CacheManage.shortcut;
	public static ICacheMap stuffCache = CacheManage.stuff;
	

	public static ICacheMap userSettingSeqCache = CacheManage.userSettingSeqCache;
	
	public static IUserService service = ServiceFactory.createUserService();

	private static IUserCashService cashService = ServiceFactory.createUserCashService();

	private static IBankService bankService = ServiceFactory.createBankService();

	final static int MAX_MONEY_STAT = 32;
	static long[] moneyStat;
	static long[] moneyStatTime = new long[MAX_MONEY_STAT];
	static {
		clearMoneyStat();
	}
	public static long[] getMoneyStat() {
		return moneyStat;
	}
	public static void clearMoneyStat() {
		moneyStat = new long[MAX_MONEY_STAT];
		long now = System.currentTimeMillis();
		Arrays.fill(moneyStatTime, now);
	}
	public static long[] getMoneyStatTime() {
		return moneyStatTime;
	}
	
	/**
	 * 清空某个用户的属性缓存。
	 * 
	 * @param userId
	 */
	public static void flushUserStatus(int userId) {
		userStatusCache.srm(userId);
	}

	/**
	 * 取得某个用户的属性。
	 * 
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static UserStatusBean getUserStatus(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(userStatusCache) {
			UserStatusBean us = (UserStatusBean) userStatusCache.get(key);
			if (us == null) {
				us = service.getUserStatus("user_id = " + userId);
	
				if (us == null) {
					return null;
				}
				userStatusCache.put(key, us);
			}
			return us;
		}
	}
	
	public static UserHonorBean getUserHonor(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(userHonorCache) {
			UserHonorBean uh = (UserHonorBean) userHonorCache.get(key);
			if (uh == null) {
				uh = service.getUserHonor("user_id = " + userId);
				if (uh == null) {
					return null;
				}
				userHonorCache.put(key, uh);
			}
	
			return uh;
		}
	}
	
	public static UserHonorBean addUserHonor(int userId, int add) {
		UserHonorBean bean = getUserHonor(userId);
		if(bean.getId() == 0) {		// 这个用户不存在荣誉
			bean.setHonor(add);
			bean.setUserId(userId);
			service.addUserHonor(bean);
			userHonorCache.srm(userId);	// 新增荣誉记录，删除旧的无效
		} else {
			service.updateUserHonor("honor=honor+" + add, "user_id=" + userId);
			bean.setHonor(bean.getHonor() + add);
		}
		
		return bean;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end
	static byte[] lock = new byte[0];
	// 更新用户当前现金，如果小于0强制为0
	public static boolean updateUserCash(int userId, long add, int type, String reason) {
		UserStatusBean usb = getUserStatus(userId);
		if(reason != null) {
			String log = userId + ":" + type + ":" + reason + "(当前乐币数:" + usb.getGamePoint();
			LogUtil.logMoney(log);
		}
		moneyStat[type] += add;
		synchronized(lock) {
			long ngp = usb.getGamePoint() + add;
			if(add > 0) {
				if(ngp > 2100000000l) {
					BankCacheUtil.updateBankStoreCacheById(add, userId, 0, Constants.BANK_OUT_OF_MONEY_TYPE);
					NoticeAction.sendNotice(userId, "增加乐币后数据会溢出，故帮您转入银行,请注意查收！", "", 
							NoticeBean.GENERAL_NOTICE, "", "/bank/bank.jsp");
				} else {
					service.updateUserStatus("game_point=game_point+" + add, "user_id=" + userId);
					usb.setGamePoint((int)ngp);
				}
			} else if(add < 0) {
				if(ngp < 0) {
					service.updateUserStatus("game_point=0", "user_id=" + userId);
					usb.setGamePoint(0);
				} else {
					service.updateUserStatus("game_point=game_point" + add, "user_id=" + userId);
					usb.setGamePoint((int)ngp);
				}
			}
		}
		return true;
	}
	
	// 设置用户的级别和经验值
	public static boolean updateUserRank(int userId, int rank, int point, String reason) {

		UserStatusBean usb = getUserStatus(userId);

		if(reason != null) {
			String log = userId + ":" + UserCashAction.OTHERS + ":" + reason;
			log = log + "(当前等级:" + usb.getRank() + ")";
			LogUtil.logMoney(log);
		}
		if(rank != usb.getRank()) {		
			if (!service.updateUserStatus("rank=" + rank + ",point=" + point, "user_id=" + userId)) {
				return false;
			}
		} else if(rank < 7 && point / 20 != usb.getPoint() / 20 || point / 100 != usb.getPoint() / 100) {	// 100点保存一次数据库
			if (!service.updateUserStatus("point=" + point, "user_id=" + userId)) {
				return false;
			}
		}
		usb.setRank(rank);
		usb.setPoint(point);
		return true;
	}
	
	public static boolean updateUserTong(int userId, int tongId) {
		UserStatusBean usb = getUserStatus(userId);
		if (usb == null || !service.updateUserStatus("tong=" + tongId, "user_id=" + userId)) {
			return false;
		}
		usb.setTong(tongId);
		return true;
	}
	
	public static boolean updateUserSocial(int userId, int social) {
		UserStatusBean usb = getUserStatus(userId);
		if (!service.updateUserStatus("social=social+" + social, "user_id=" + userId)) {
			return false;
		}
		usb.setSocial(usb.getSocial() + social);
		return true;
	}
	/**
	 * 更新某个用户的属性。
	 * 
	 * @param set
	 * @param condition
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static boolean updateUserStatus(String set, String condition,
			int userId, int type, String reason) {
		// zhul 2006-09-28 如果用户加乐币大于INT范围，返回false,不更新数据库 start
		String formatSet = set.replace(" ", "");
		// liuyi 2007-02-12 当前乐币日志 start
		String log = userId + ":" + type + ":" + reason;
		if (formatSet.indexOf("game_point") != -1 || formatSet.indexOf("rank")!=-1) {
			UserStatusBean usb = getUserStatus(userId);
			log = log + "(当前乐币数:" + usb.getGamePoint() + ",当前等级:" + usb.getRank() + ")";
		}

		LogUtil.logMoney(log);

		// 判断是否有加乐币的动作
		int index = formatSet.indexOf("game_point+");
		if (index != -1) {
			// 从指定的索引处开始,判断逗号出现的位置,没有则返回-1
			int offSet = formatSet.indexOf(',', index);
			if (offSet == -1) {
				// 从指定的索引处开始,判断右括号出现的位置,没有则返回-1
				offSet = formatSet.indexOf(')', index);
				if (offSet == -1) {
					offSet = formatSet.length();
				}
			}
			String sub = formatSet.substring(index + 11, offSet);
			UserStatusBean usb = getUserStatus(userId);
			// 如果超出乐币范围 ，则自动存到银行 wucx 2006-11-7 start
			long add = (long) usb.getGamePoint() + (long) StringUtil.toLong(sub);
			if (add > (long) Constants.SYS_MAX_INT) {
				// macq_2006-11-27_更改银行存款从缓存中获取_start
				StoreBean store = BankCacheUtil.getBankStoreCache(userId);
				if (store != null) {
					BankCacheUtil.updateBankStoreCacheById((long) StringUtil.toLong(sub), userId,0,Constants.BANK_OUT_OF_MONEY_TYPE);
				} else {
					store = new StoreBean();
					store.setMoney((long) StringUtil.toLong(sub));
					store.setUserId(userId);
					BankCacheUtil.addBankStoreCache(store);
				}
				
				NoticeAction.sendNotice(userId, "增加乐币后数据会溢出，故帮您转入银行,请注意查收！", "", 
						NoticeBean.GENERAL_NOTICE, "", "/bank/bank.jsp");
				return true;
			}
			// 如果超出乐币范围 ，则自动存到银行 wucx 2006-11-7 end
		}
		// zhul 2006-09-28 如果用户加乐币大于INT范围，返回false,不更新数据库 end

		if (!service.updateUserStatus(set, condition)) {
			return false;
		}

		flushUserStatus(userId);
		return true;
	}

	/**
	 * zhul 2006-10-12 用户信息缓存处理
	 * 
	 * @param userId
	 * @return userBean
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static UserBean getUser(int userId) {
		if (userId <= 0)
			return null;
		Integer key = Integer.valueOf(userId);
		synchronized(userInfoCache) {
			UserBean user = (UserBean) userInfoCache.get(key);
			if (user == null) {
				user = service.getUser("id = " + userId);

				if (user == null) {
					return null;
				} 
					
				userInfoCache.put(key, user);
			}
	
			return user;
		}
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * zhul 2006-10-12_包装userService.updateUser，更新用户信息同时清缓存
	 * 
	 * @param set
	 * @param condition
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static boolean updateUser(String set, String condition, String userId) {
		// 更新表
		if (!service.updateUser(set, condition))
			return false;
		// 清缓存
		userInfoCache.srm(StringUtil.toInt(userId));

		return true;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * zhul 2006-10-13 获取用户好友
	 * 
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static ArrayList getUserFriends(int userId) {
		// 对应用户信息的健值
		Integer key = Integer.valueOf(userId);

		synchronized(userFriendsCache) {
			ArrayList userFriends = (ArrayList) userFriendsCache.get(key);
			// 缓存中没有
			if (userFriends == null) {
				// 从数据库中取
				userFriends = service.getUserFriendList(userId);
				// 放到缓存中
				userFriendsCache.put(key, userFriends);
			}
			return userFriends;
		}
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * wucx 2006-10-16 获取用户在线好友个数
	 * 
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static int getUserOnlineFriendsCount(int userId) {
		// 得到用户好友列表
		ArrayList friends = getUserFriends(userId);
		String friendid = null;
		int friendCount = 0;
		for (int i = 0; i < friends.size(); i++) {
			friendid = (String) friends.get(i);
			// 判断是否在线好友
			if (OnlineUtil.isOnline(friendid))
				friendCount = friendCount + 1;
		}
		return friendCount;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * wucx 2006-10-16 获取用户离线好友个数
	 * 
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static int getUserOfflineFriendsCount(int userId) {
		// 得到用户好友列表
		ArrayList friends = getUserFriends(userId);
		String friendid = null;
		int friendCount = 0;
		for (int i = 0; i < friends.size(); i++) {
			friendid = (String) friends.get(i);
			// 判断是否在线好友
			if (!OnlineUtil.isOnline(friendid))
				friendCount = friendCount + 1;
		}
		return friendCount;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * wucx 2006-10-16 获取用户在线好友个列表
	 * 
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static Vector getUserOnlineFriendsList(int userId) {
		// 得到用户好友列表
		ArrayList friends = getUserFriends(userId);
		String friendid = null;
		Vector friend = new Vector();
		for (int i = 0; i < friends.size(); i++) {
			friendid = (String) friends.get(i);
			// 判断是否在线好友
			if (OnlineUtil.isOnline(friendid))
				friend.add(getUser(StringUtil.toInt(friendid)));
		}
		return friend;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * wucx 2006-10-16 获取用户离线好友列表
	 * 
	 * @param userId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static Vector getUserOfflineFriendsList(int userId) {
		// 得到用户好友列表
		ArrayList friends = getUserFriends(userId);
		String friendid = null;
		Vector friend = new Vector();
		for (int i = 0; i < friends.size(); i++) {
			friendid = (String) friends.get(i);
			// 判断是否在线好友
			if (!OnlineUtil.isOnline(friendid))
				friend.add(getUser(StringUtil.toInt(friendid)));
		}
		return friend;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * zhul 2006-10-20 增加用户的PK度
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean addUserPKTotal(int userId) {
		UserStatusBean usb = getUserStatus(userId);
		usb.setPk(usb.getPk() + 1);
		if(usb.getPk() % 20 == 0)
			if (!service.updateUserStatus("pk=pk+1", "user_id=" + userId)) {
				return false;
			}
		return true;
	}

	/**
	 * liuyi 2006-12-02 程序优化 获取在线用户id列表(按pk度排序)，缓存两分钟，以减少web服务器压力
	 * 
	 * @return
	 */
	public static Vector getOnlineUserOrderByPKFromCache() {
		Vector ret = null;

		String key = "order by pk desc";
		ret = (Vector) OsCacheUtil.get(key, OsCacheUtil.ONLINE_USER_IDS_GROUP,
				OsCacheUtil.ONLINE_USER_IDS_FLUSH_PERIOD);
		if (ret == null) {
			ArrayList onlineUser = OnlineUtil.getAllOnlineUser();
			Vector highPkList = new Vector();
			Vector lowPkList = new Vector();
			int highCount = 0;
			for (int i = 0; i < onlineUser.size(); i++) {
				String userId = (String) onlineUser.get(i);
				if (highCount < 50) {
					UserStatusBean user = UserInfoUtil.getUserStatus(StringUtil
							.toInt(userId));
					if (user != null && user.getPk() > 3) {
						highPkList.add(0, userId);
						highCount++;
					} else {
						lowPkList.add(userId);
					}
				} else {
					lowPkList.add(userId);
				}
			}
			ret = highPkList;
			ret.addAll(lowPkList);

			OsCacheUtil.put(key, ret, OsCacheUtil.ONLINE_USER_IDS_GROUP);
		}

		return ret;
	}

	/**
	 * liuyi 2006-12-02 程序优化 获取用户id列表(在线优先)，缓存两分钟，以减少web服务器压力
	 * 
	 * @return
	 */
	public static Vector getUserOrderByOnlineFromCache(List userIdList,
			int flushPeriod) {
		Vector ret = null;
		if (userIdList == null) {
			return new Vector();
		}

		// 根据userIdList对象本身来缓存，所以使用本方法的userIdList必须是同一个对象，才具有意义；
		String key = "" + userIdList.hashCode();
		ret = (Vector) OsCacheUtil
				.get(key, OsCacheUtil.ONLINE_USER_IDS_GROUP, Math.max(
						OsCacheUtil.ONLINE_USER_IDS_FLUSH_PERIOD, flushPeriod));
		if (ret == null) {
			Vector firstList = new Vector();
			Vector secondList = new Vector();
			for (int i = 0; i < userIdList.size(); i++) {
				Integer userId = (Integer) userIdList.get(i);
				if (userId == null)
					continue;

				String id = userId.intValue() + "";
				if (OnlineUtil.isOnline(id)) {
					firstList.add(userId);
				} else {
					secondList.add(userId);
				}

			}
			ret = firstList;
			ret.addAll(secondList);

			OsCacheUtil.put(key, ret, OsCacheUtil.ONLINE_USER_IDS_GROUP);
		}

		return ret;
	}
	
	/**
	 * 一周运行一次的荣誉排名 
	 */
	public static void weekUpdateHonor() {
		service.calcHonorPlace();
		userHonorCache.clear();
	}
	
	public static HashMap getShortcutMap() {
		HashMap map = (HashMap) stuffCache.sgt("scMap");
		if(map == null) {
			prepareShortcut();
			map = (HashMap) stuffCache.sgt("scMap");
		}
		return map;
	}
	
	public static List getShortcutList() {
		List list = (List) stuffCache.sgt("scList");
		if(list == null) {
			prepareShortcut();
			list = (List) stuffCache.sgt("scList");
		}
		return list;
	}
	
	public static void prepareShortcut() {
		synchronized(stuffCache) {
			HashMap map = new HashMap();
			List list = service.getShortcutList();
			Iterator iter = list.iterator();
			while(iter.hasNext()) {
				ShortcutBean bean = (ShortcutBean)iter.next();
				map.put(Integer.valueOf(bean.getId()), bean);
			}
			stuffCache.put("scMap", map);
			stuffCache.put("scList", list);
		}
	}
	
	public static String getUserSettingSeq(int userId) {
		String seq = null;
		synchronized(userSettingSeqCache) {
			seq = (String)userSettingSeqCache.get(new Integer(userId));
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			String query = "select bag_seq from user_setting where user_id = " + userId;
			seq = SqlUtil.getString(dbOp, query);
			dbOp.release();
			if(seq != null) {
				userSettingSeqCache.put(new Integer(userId), seq);
			} else {
				return "";
			}
		}		
		return seq;
	}
	
	public static UserSettingBean getUserSetting(int userId) {
//		UserSettingBean userSetting = null;
//		synchronized(userSettingCache) {
//			userSetting = (UserSettingBean)userSettingCache.get(new Integer(userId));
//			if(userSetting == null) {
//				userSetting = service.getUserSetting(" user_id = " + userId);
//				if(userSetting == null) {
////					userSetting = nullSetting;
////					userSetting.setUserId(userId);
////					service.addUserSetting(userSetting);
//					userSettingCache.put(new Integer(userId), nullSetting);
//					return null;
//				}
//			} else if(userSetting == nullSetting)
//				return null;
//		}
		return null;
	}
	
	public static HashMap getUserNoteMap(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(userNoteCache) {
			HashMap map = (HashMap)userNoteCache.get(key);
			if(map == null) {
				map = new HashMap();
				List list = service.getUserNoteList("user_id=" + userId);
				Iterator iter = list.iterator();
				while(iter.hasNext()) {
					UserNoteBean bean = (UserNoteBean)iter.next();
					map.put(Integer.valueOf(bean.getToUserId()), bean);
				}
				userNoteCache.put(key, map);
				
			}
			return map;
		}
	}
	
	public static UserNoteBean getUserNote(int userId, int toUserId) {
		HashMap map = getUserNoteMap(userId);
		return (UserNoteBean)map.get(Integer.valueOf(toUserId));
	}
	
	public static void addUserNote(int userId, int toUserId, String shortNote, String note) {
		HashMap map = getUserNoteMap(userId);
		UserNoteBean bean = (UserNoteBean)map.get(Integer.valueOf(toUserId));
		if(bean == null) {
			bean = new UserNoteBean();
			bean.setUserId(userId);
			bean.setToUserId(toUserId);
			bean.setShortNote(shortNote);
			bean.setNote(note);
			service.addUserNote(bean);
			
			map.put(Integer.valueOf(toUserId), bean);
		} else {
			bean.setShortNote(shortNote);
			bean.setNote(note);
			editUserNote(bean);
		}
	}
	
	public static void editUserNote(UserNoteBean bean) {
		service.updateUserNote("short_note='" + StringUtil.toSql(bean.getShortNote()) + "',note='" +
				StringUtil.toSql(bean.getNote()) + "'",
				"id=" + bean.getId());
	}
	
	public static void removeUserNote(int userId, int toUserId) {
		HashMap map = getUserNoteMap(userId);
		UserNoteBean bean = (UserNoteBean)map.remove(Integer.valueOf(toUserId));
		if(bean != null) {
			service.removeUserNote("id=" + bean.getId());
		}
	}
	
	static ICacheMap userIntervalCache = CacheManage.userInterval;
//	 用户每周或者每天等为周期的活动，例如每周可以免费转盘
	public static HashSet getUserIntervalSet(int type) {
		Integer key = Integer.valueOf(type);
		synchronized(userIntervalCache) {
			HashSet set = (HashSet)userIntervalCache.get(key);
			if(set == null) {
				
				List list = SqlUtil.getIntList("select user_id from user_interval where type=" + type);
				set = new HashSet(list);
	
				userIntervalCache.put(key, set);
			}
			return set;
		}
	}

	static byte[] lock2 = new byte[0];
	/**
	 * 判断这个用户这周是否已经使用过，执行过一次就置为已经使用过
	 * @param userId
	 */
	public static boolean isAddUserInterval(int type, int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(lock2) {
			HashSet set = getUserIntervalSet(type);
			if(set == null)
				return true;
			if(!set.contains(key)) {
				set.add(key);
				service.addUserInterval(type, userId, IntervalTypeRefresh[type]);
				return false;
			}
		}
		return true;
	}
	
	public static boolean isUserInterval(int type, int userId) {
		Integer key = Integer.valueOf(userId);

		HashSet set = getUserIntervalSet(type);
		if(set == null)
			return true;

		return set.contains(key);
	}
	
	/**
	 * 刷新间隔，每周执行一次
	 * @param refresh
	 */
	public static void refreshUserInterval(int refresh) {
		SqlUtil.executeUpdate("delete from user_interval where refresh=" + refresh);
		for(int i = 0;i < IntervalTypeRefresh.length;i++)
			if(IntervalTypeRefresh[i] == refresh)
				userIntervalCache.srm(Integer.valueOf(i));
				
	}
	
	public static FarmUserBean getFarmUser(UserBean loginUser) {
		if(loginUser == null)
			return null;
		return FarmWorld.one.getFarmUser(loginUser.getId());
	}
	
	public static boolean haveFarmUser(UserBean loginUser) {
		return getFarmUser(loginUser) != null;
	}
	
	// 每种类型对应的间隔类型, refresh: 0 每周 1每月 2每天
	public static int[] IntervalTypeRefresh = { 0 };
}
