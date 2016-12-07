package net.joycool.wap.cache.util;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.bean.auction.AuctionBean;
import net.joycool.wap.bean.auction.AuctionHistoryBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IAuctionService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;

/**
 * @author macq
 * @datetime 2006-12-13 上午09:58:27
 * @explain 拍卖系统缓存方法
 */
public class AuctionCacheUtil {
	public static ICacheMap auctionCache = CacheManage.auction;
	
	static IAuctionService service = ServiceFactory
			.createAuctionService();

	/**
	 * 
	 * @author macq explain : 清空用户所拍卖物品id列表 datetime:2006-12-15 下午05:14:04
	 * @param userId
	 */
	public static void flushActionByUserId(int userId) {
		String key = getKey(userId);
		OsCacheUtil.flushGroup(OsCacheUtil.USER_AUCTION_CACHE_GROUP, key);
	}

	/**
	 * 
	 * @author macq explain : 清空所有用户所拍卖物品id列表 datetime:2006-12-15 下午05:44:48
	 */
	public static void flushActionALLByUserId() {
		auctionCache.clear();
	}

	/**
	 * 
	 * @author macq explain : 清空一条用户所拍卖物品记录 datetime:2006-12-15 下午05:45:03
	 * @param auctionId
	 */
	public static void flushActionById(int auctionId) {
		auctionCache.srm(auctionId);
	}

	/**
	 * 
	 * @author macq explain : 清空所有用户所拍卖物品记录 datetime:2006-12-15 下午05:45:32
	 */
	public static void flushActionAllById() {
		OsCacheUtil.flushGroup(OsCacheUtil.USER_AUCTION_BY_ID_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq explain : 获取一个用户所拍卖物品id列表 datetime:2006-12-15 下午05:45:50
	 * @param userId
	 * @return
	 */
	public static List getAuctionCacheByUserId(int userId) {
		String key = getKey(userId);
		// 从缓存中取
		List auctionList = (List) OsCacheUtil.get(key,
				OsCacheUtil.USER_AUCTION_CACHE_GROUP,
				OsCacheUtil.USER_AUCTION_FLUSH_PERIOD);
		// 缓存中没有
		if (auctionList == null) {
			// 从数据库中取
			String sql = "SELECT id from jc_auction where Left_user_id="
					+ userId + " and time>0 order by id desc";
			auctionList = (List) SqlUtil.getIntList(sql, Constants.DBShortName);
			// 为空
			if (auctionList == null) {
				// liuyi 2007-01-09 程序优化 start
				// return null;
				auctionList = new ArrayList(1);
				// liuyi 2007-01-09 程序优化 end
			}
			// 放到缓存中
			OsCacheUtil.put(key, auctionList,
					OsCacheUtil.USER_AUCTION_CACHE_GROUP);
		}
		return auctionList;
	}

	/**
	 * 
	 * @author macq explain : 获取一条用户所拍卖物品记录 datetime:2006-12-15 下午05:46:29
	 * @param auctionId
	 * @return
	 */
	public static AuctionBean getAuctionCacheById(int auctionId) {
		Integer key = Integer.valueOf(auctionId);
		synchronized(auctionCache) {
			AuctionBean auction = (AuctionBean) auctionCache.get(key);
	
			if (auction == null) {
				auction = service.getAuction("id = " + auctionId);
	
				if (auction == null) {
					return null;
				}
				auctionCache.put(key, auction);
			}
			return auction;
		}
		
	}

	/**
	 * @author macq explain : 添加拍卖记录 datetime:2006-12-15 下午05:46:45
	 * @param bean
	 * @return
	 */
	public static boolean addAction(AuctionBean bean) {
		if (bean == null)
			return false;
		boolean flag = service.addAuction(bean);
		if (flag) {
			// 清空缓存
			flushActionByUserId(bean.getLeftUserId());
			// 加入到拍卖大厅列表
			List auctionList = (List) OsCacheUtil.get("auctionList",
					OsCacheUtil.USER_AUCTION_BY_ID_CACHE_GROUP,
					OsCacheUtil.USER_AUCTION_BY_ID_FLUSH_PERIOD);
			if (auctionList != null) {
				auctionList.add(Integer.valueOf(bean.getId()));
			}
		}
		return true;
	}

	/**
	 * 
	 * @author macq explain : 更新一条拍卖记录 datetime:2006-12-15 下午05:47:18
	 * @param set
	 * @param condition
	 * @param userId
	 * @param auctionId
	 * @return
	 */
	public static boolean updateActionCacheByUserId(String set,
			String condition, int userId, int auctionId) {
		// 更新记录
		if (!service.updateAuction(set, condition)) {
			return false;
		}
		// 清空缓存
		flushActionByUserId(userId);
		flushActionById(auctionId);
		return true;
	}

	/**
	 * 
	 * @author macq explain : 更新所有拍卖记录 datetime:2006-12-15 下午05:47:30
	 * @param set
	 * @param condition
	 * @return
	 */
	public static boolean updateActionCahce(String set, String condition) {
		// 更新记录
		if (!service.updateAuction(set, condition)) {
			return false;
		}
		// 清空缓存
		flushActionAllById();
		return true;
	}

	/**
	 * 
	 * @author macq explain : 删除拍卖记录 datetime:2006-12-15 下午05:47:49
	 * @param condition
	 * @param userId
	 * @param auctionId
	 * @return
	 */
	public static boolean deleteActionCache(String condition, int userId,
			int auctionId) {
		// 删除记录
		if (!service.deleteAuction(condition)) {
			return false;
		}
		// 清空缓存中的一个记录ID
		flushActionByUserId(userId);
		flushActionById(auctionId);
		return true;
	}

	/**
	 * 
	 * @author macq explain : 取得缓存的key datetime:2006-12-15 下午04:47:08
	 * @param userId
	 * @return
	 */
	public static String getKey(int userId) {
		return "" + userId;
	}

	/**
	 * 
	 * @author macq explain : 清空用户所拍卖物品历史记录id列表 datetime:2006-12-15 下午05:14:04
	 * @param userId
	 */
	public static void flushActionHistoryByUserId(int userId) {
		String key = getKey(userId);
		OsCacheUtil.flushGroup(
				OsCacheUtil.USER_AUCTION_HISTORY_BY_ID_CACHE_GROUP, key);
	}

	/**
	 * 
	 * @author macq explain : 清空所有用户所拍卖物品历史记录id列表 datetime:2006-12-15 下午05:44:48
	 */
	public static void flushActionHistoryALLByUserId() {
		OsCacheUtil
				.flushGroup(OsCacheUtil.USER_AUCTION_HISTORY_BY_ID_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq explain : 清空一条用户所拍卖物品历史记录 datetime:2006-12-15 下午05:45:03
	 * @param auctionId
	 */
	public static void flushActionHistoryById(int auctionId) {
		String key = getKey(auctionId);
		OsCacheUtil.flushGroup(OsCacheUtil.USER_AUCTION_HISTORY_CACHE_GROUP,
				key);
	}

	/**
	 * 
	 * @author macq explain : 清空所有用户所拍卖物品历史记录 datetime:2006-12-15 下午05:45:32
	 */
	public static void flushActionHistoryAllById() {
		OsCacheUtil.flushGroup(OsCacheUtil.USER_AUCTION_HISTORY_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq explain : 获取一个用户所拍卖物品id列表 datetime:2006-12-15 下午05:45:50
	 * @param userId
	 * @return
	 */
	public static List getAuctionHistoryCacheByUserId(int userId) {
		String key = getKey(userId);
		// 从缓存中取
		List auctionList = (List) OsCacheUtil.get(key,
				OsCacheUtil.USER_AUCTION_HISTORY_BY_ID_CACHE_GROUP,
				OsCacheUtil.USER_AUCTION_HISTORY_BY_ID_FLUSH_PERIOD);
		// 缓存中没有
		if (auctionList == null) {
			// 从数据库中取
			String sql = "SELECT id from jc_auction_history where user_id="
					+ userId + " order by id desc";
			auctionList = (List) SqlUtil.getIntList(sql, Constants.DBShortName);
			// 为空
			if (auctionList == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, auctionList,
					OsCacheUtil.USER_AUCTION_HISTORY_BY_ID_CACHE_GROUP);
		}
		return auctionList;
	}

	/**
	 * 
	 * @author macq explain : 获取一条用户所拍卖物品历史记录 datetime:2006-12-15 下午05:46:29
	 * @param auctionId
	 * @return
	 */
	public static AuctionHistoryBean getAuctionHistoryCacheById(
			int auctionHistoryId) {
		String key = getKey(auctionHistoryId);
		// 从缓存中取
		AuctionHistoryBean auctionHistroy = (AuctionHistoryBean) OsCacheUtil
				.get(key, OsCacheUtil.USER_AUCTION_HISTORY_CACHE_GROUP,
						OsCacheUtil.USER_AUCTION_HISTORY_FLUSH_PERIOD);
		// 缓存中没有
		if (auctionHistroy == null) {
			// 从数据库中取
			auctionHistroy = service.getAuctionHistory("id = "
					+ auctionHistoryId);
			// 为空
			if (auctionHistroy == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, auctionHistroy,
					OsCacheUtil.USER_AUCTION_HISTORY_CACHE_GROUP);
		}
		return auctionHistroy;
	}

	/**
	 * @author macq explain : 添加拍卖历史记录 datetime:2006-12-15 下午05:46:45
	 * @param bean
	 * @return
	 */
	public static boolean addActionHistory(AuctionHistoryBean bean) {
		if (bean == null)
			return false;
		boolean flag = service.addAuctionHistory(bean);
		if (flag) {
			// 清空缓存
			flushActionHistoryByUserId(bean.getUserId());
		}
		return true;
	}

	/**
	 * 
	 * @author macq explain : 更新一条拍卖历史记录 datetime:2006-12-15 下午05:47:18
	 * @param set
	 * @param condition
	 * @param userId
	 * @param auctionId
	 * @return
	 */
	public static boolean updateActionHistoryCacheByUserId(String set,
			String condition, int userId, int auctionHistoryId) {
		// 更新记录
		if (!service.updateAuction(set, condition)) {
			return false;
		}
		// 清空缓存
		flushActionHistoryByUserId(userId);
		flushActionHistoryById(auctionHistoryId);
		return true;
	}

	/**
	 * 
	 * @author macq explain : 更新所有拍卖历史记录 datetime:2006-12-15 下午05:47:30
	 * @param set
	 * @param condition
	 * @return
	 */
	public static boolean updateActionHistoryCahce(String set, String condition) {
		// 更新记录
		if (!service.updateAuction(set, condition)) {
			return false;
		}
		// 清空缓存
		flushActionHistoryAllById();
		return true;
	}

	/**
	 * 
	 * @author macq explain : 删除拍卖历史记录 datetime:2006-12-15 下午05:47:49
	 * @param condition
	 * @param userId
	 * @param auctionId
	 * @return
	 */
	public static boolean deleteActionHistoryCache(String condition,
			int userId, int auctionHistoryId) {
		// 删除记录
		if (!service.deleteAuction(condition)) {
			return false;
		}
		// 清空缓存中的一个记录ID
		flushActionHistoryByUserId(userId);
		flushActionHistoryById(auctionHistoryId);
		return true;
	}
}
