package net.joycool.wap.util;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.INoticeService;

public class NewNoticeCacheUtil {

	// private static HashMap noticeCountMap = new HashMap(); // 每个聊天室公聊数量缓存
	public static ICacheMap noticeCache = CacheManage.notice;
	public static ICacheMap noticeListCache = CacheManage.noticeList;

	private static INoticeService service = ServiceFactory
			.createNoticeService();

	/**
	 * 取得某条消息记录。
	 * 
	 * @param noticeId
	 * @return NoticeBean
	 */
	public static NoticeBean getNotice(int noticeId) {
		Integer key = Integer.valueOf(noticeId);
		synchronized(noticeCache) {
			NoticeBean notice = (NoticeBean) noticeCache.get(key);
			if (notice == null) {

				notice = service.getNotice("id = " + noticeId);

				if (notice == null) {
					return null;
				}
				noticeCache.put(key, notice);
			}
			return notice;
		}
	}

	/**
	 * 取得缓存的key。
	 * 
	 * @param contentId
	 * @return String
	 */
	public static String getKey(int noticeId) {
		return "" + noticeId;
	}

	/**
	 * 获取用户未读普通通知Id列表
	 * 
	 * @param UserId
	 * @return Vector
	 */
	public static Vector getUserGeneralNoticeCountMap(int UserId) {
		Integer key = Integer.valueOf(UserId);
		synchronized(noticeListCache) {
			Vector userGeneralNoticeList = (Vector) noticeListCache.get(key);
			if (userGeneralNoticeList == null) {
				// 获取该用户所有未读消息id
				userGeneralNoticeList = service
						.getNoticeListById("select id from jc_notice where user_id = "
								+ UserId
								+ " and status = "
								+ NoticeBean.NOT_READ
								+ " and type != "
								+ NoticeBean.SYSTEM_NOTICE
								+ " order by id desc ");
				// 为空
				if (userGeneralNoticeList == null) {
					userGeneralNoticeList = new Vector();
				}
				noticeListCache.put(key, userGeneralNoticeList);
			}
			return userGeneralNoticeList;
		}
	}

	/**
	 * 获取系统消息Id列表
	 * 
	 * @return Vector
	 */
	public static Vector getSystemNoticeCountMap() {
		String key = "systemNotice";
		Vector systemNoticeList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.SYSTEM_NOTICE_GROUP,
				OsCacheUtil.SYSTEM_NOTICE_FLUSH_PERIOD);
		if (systemNoticeList == null) {
			// 获取该用户所有未读消息id
			systemNoticeList = service.getNoticeListById(""
					+ "select id from jc_notice where type = "
					+ NoticeBean.SYSTEM_NOTICE + " and tong_id=0");
			// 为空
			if (systemNoticeList == null) {
				systemNoticeList = new Vector();
			}
			// 放到缓存中
			OsCacheUtil.put(key, systemNoticeList,
					OsCacheUtil.SYSTEM_NOTICE_GROUP);
		}
		// 返回Vector
		return systemNoticeList;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会信息id列表
	 * @datetime:2007-1-3 上午11:02:57
	 * @param user
	 * @return Vector
	 */
	public static Vector getTongSystemNoticeCountMap(UserStatusBean us) {
		String key = us.getTong() + "";
		Vector tongSystemNoticeList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP,
				OsCacheUtil.TONG_SYSTEM_NOTICE_FLUSH_PERIOD);
		if (tongSystemNoticeList == null) {
			// 获取该用户所有未读消息id
			// liuyi 2007-01-23 帮会消息最多3条 start
			tongSystemNoticeList = service.getNoticeListById(""
					+ "select id from jc_notice where type = "
					+ NoticeBean.SYSTEM_NOTICE + " and tong_id=" + us.getTong()
					+ " order by id desc limit 0,3");
			// liuyi 2007-01-23 帮会消息最多3条 end
			// 为空
			if (tongSystemNoticeList == null) {
				tongSystemNoticeList = new Vector();
			}
			// 放到缓存中
			OsCacheUtil.put(key, tongSystemNoticeList,
					OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP);
		}
		// 返回Vector
		return tongSystemNoticeList;
	}

	/**
	 * 获取每个系统消息对应读取过的用户Id列表
	 * 
	 * @param UserId
	 * @return Vector
	 */
	public static byte[] lock = new byte[0];
	public static Vector getSystemNoticeReadedCountMap(UserBean user) {
		// 获取所有系统消息
		Vector systemNoticeIdListTotal = null;
//		Vector systemNoticeIdList = getSystemNoticeCountMap();
		UserStatusBean us = UserInfoUtil.getUserStatus(user.getId());
		if (us != null && us.getTong() > 0) {
			systemNoticeIdListTotal = new Vector();
			// macq_2007-1-3_添加帮会信息id列表到系统消息列表后_start
			Vector tongSystemNoticeList = getTongSystemNoticeCountMap(us);
//			systemNoticeIdListTotal.addAll(systemNoticeIdList);
			systemNoticeIdListTotal.addAll(tongSystemNoticeList);
		} else {
			return new Vector();
		}
		// macq_2007-1-3_添加帮会信息id列表到系统消息列表后_end
		// 初始化用户将要显示的系统信息Id
		Vector systemNoticeReadedList = new Vector();
		Integer noticeId = null;
		// 循环系统信息ID列表
		for (int i = 0; i < systemNoticeIdListTotal.size(); i++) {
			// 获取一条系统消息ID
			noticeId = (Integer) systemNoticeIdListTotal.get(i);
			// 转换为key
			String key = noticeId.toString();
			// 从缓存中取得对应度过系统消息的用户列表
			Set systemNoticeReadedListById = (Set) OsCacheUtil.get(key,
					OsCacheUtil.SYSTEM_NOTICE_READED_GROUP,
					OsCacheUtil.SYSTEM_NOTICE_READED_FLUSH_PERIOD);
			// 如果没有缓存中无该系统消息
			
			if (systemNoticeReadedListById == null) {
				synchronized(lock) {
					if (systemNoticeReadedListById == null) {
						// 获取读取过该系统消息的所有用户id列表
						Vector tmp = service.getNoticeListById(""
										+ "select user_id from jc_notice_history where notice_id = "
										+ noticeId.intValue());
						if(tmp != null)
						{
							systemNoticeReadedListById = new HashSet(tmp);
						} else {
							systemNoticeReadedListById = new HashSet();
						}
						OsCacheUtil.put(key, systemNoticeReadedListById,
								OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);
					}
				}

			}
			if (!systemNoticeReadedListById.contains(new Integer(user
					.getId()))) {
				// 不存在已读列表的时候将显示系统消息
				systemNoticeReadedList.add(noticeId);
			}

		}
		// 返回map
		return systemNoticeReadedList;
	}

	/**
	 * 清空某个用户普通消息Id的属性缓存。
	 * 
	 * @param userId
	 */
	public static void flushUserNotice(int userId) {
		noticeListCache.srm(userId);
	}

	/**
	 * 增加用户消息记录,清空对应用户消息id列表缓存组。
	 * 
	 * @param bean
	 * @return
	 */
	public static void addUserNoticeById(int userId) {
		flushUserNotice(userId);
	}

	/**
	 * 删除某条系统消息的属性缓存。
	 * 
	 * @param noticeId
	 */
	public static void flushSystemNotice(int noticeId) {
		String key = getKey(noticeId);
		// 清除对应系统消息id所在缓存组中的记录
		OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_GROUP, "systemNotice");
		// 清除对应系统消息id所在缓存组中的记录
		OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);
		// 清除缓存中对应记录
		noticeCache.clear();
	}

	/**
	 * 增加系统消息记录。
	 * 
	 * @param bean
	 * @return
	 */
	public static void addSystemNoticeById() {
		// 清除所有系统消息列表
		OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_GROUP);
	}

	/**
	 * 
	 * 向用户通知ID列表添加读取过用户通知的通知ID。
	 * 
	 * @param bean
	 * @return
	 */
	public static void updateUserNoticeById(int userId, int contentId) {
		Integer key = Integer.valueOf(userId);
		Vector userGeneralNoticeList = (Vector) noticeListCache.sgt(key);
		if (userGeneralNoticeList != null) {
			userGeneralNoticeList.remove(new Integer(contentId));
		}
	}

	/**
	 * 向系统消息ID列表添加读取过系统通知的用户id。
	 * 
	 * @param bean
	 * @return
	 */
	public static void updateSystemNoticeById(int userId, int contentId) {
		String key = getKey(contentId);
		HashSet systemNoticeReadedListById = (HashSet) OsCacheUtil.get(key,
				OsCacheUtil.SYSTEM_NOTICE_READED_GROUP,
				OsCacheUtil.SYSTEM_NOTICE_READED_FLUSH_PERIOD);
		if(systemNoticeReadedListById == null)
			return;
		systemNoticeReadedListById.add(new Integer(userId));
		// 放到缓存中
		OsCacheUtil.put(key, systemNoticeReadedListById,
				OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);
	}
}
