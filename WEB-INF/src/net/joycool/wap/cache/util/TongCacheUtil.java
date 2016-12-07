package net.joycool.wap.cache.util;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.tong.TongDestroyHistoryBean;
import net.joycool.wap.bean.tong.TongFundBean;
import net.joycool.wap.bean.tong.TongHockshopBean;
import net.joycool.wap.bean.tong.TongUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ITongService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;

/**
 * @author macq
 * @datetime 2007-1-2 上午10:22:30
 * @explain 帮会缓存
 */
public class TongCacheUtil {
	public static ICacheMap tongUserCache = CacheManage.tongUser;
	private static ITongService service = ServiceFactory.createTongService();

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中所有帮会ID信息
	 * @datetime 2007-1-2 上午10:22:30
	 */
	public static void flushTongListAll() {
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_ID_LIST_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取所有帮会ID列表
	 * @datetime:2007-1-3 下午04:59:39
	 * @return List
	 */
	public static List getTongListById(String orderBy) {
		// macq_2007-2-8_更改帮会列表显示顺序,按人气或者按开发度_start
		String key = "";
		if (orderBy.equals("userCount")) {
			key = "select id from jc_tong order by mark asc,user_count desc";
		} else if (orderBy.equals("id")) {
			key = "select id from jc_tong order by mark asc,id desc";
		}else if (orderBy.equals("honor")) {
			key = "select a.id from jc_tong a join jc_tong_hockshop b on a.id=b.tong_id order by a.mark asc,a.honor desc,b.develop desc";
		}else if (orderBy.equals("userId")) {
			key = "select a.user_id from jc_tong a join jc_tong_hockshop b on a.id=b.tong_id  where a.user_id>0 order by  b.develop desc limit 10";
		} else {
			key = "select a.id from jc_tong a join jc_tong_hockshop b on a.id=b.tong_id order by a.mark asc, b.develop desc";
		}
		// macq_2007-2-8_更改帮会列表显示顺序,按人气或者按开发度_end
		// 从缓存中取
		List tongList = (List) OsCacheUtil.get(key,
				OsCacheUtil.TONG_ID_LIST_CACHE_GROUP,
				OsCacheUtil.TONG_ID_LIST_FLUSH_PERIOD);
		// 缓存中没有
		if (tongList == null) {
			// 从数据库中取
			tongList = (List) SqlUtil.getIntList(key, Constants.DBShortName);
			// 为空
			if (tongList == null) {
				tongList = new ArrayList();
			}
			// 放到缓存中
			OsCacheUtil
					.put(key, tongList, OsCacheUtil.TONG_ID_LIST_CACHE_GROUP);
		}
		return tongList;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取所有帮会ID列表
	 * @datetime:2007-1-3 下午04:59:39
	 * @return List
	 */
	public static int getTongOrderById(int tongId, String orderBy) {
		// macq_2007-2-8_更改帮会列表显示顺序,按人气或者按开发度_start
		String key = "";
		if (orderBy.equals("userCount")) {
			key = "select count(a.id) from jc_tong a ,(select user_count from jc_tong where id="
					+ tongId + ") b  where  a.user_count>b.user_count";
		}else if (orderBy.equals("honor")) {
			key = "select count(a.id) from jc_tong a ,jc_tong_hockshop c,(select e.honor,d.develop from jc_tong e,jc_tong_hockshop d where e.id="
					+ tongId + " and d.tong_id=e.id) b  where a.id=c.tong_id and (a.honor>b.honor or (a.honor=b.honor and c.develop>b.develop))";
		} else {
			key = "select count(a.id) from jc_tong_hockshop a ,(select develop from jc_tong_hockshop where tong_id="
					+ tongId + ") b  where  a.develop>b.develop";
		}
		// macq_2007-2-8_更改帮会列表显示顺序,按人气或者按开发度_end
		// 从缓存中取
		Integer count = (Integer) OsCacheUtil.get(key,
				OsCacheUtil.TONG_ID_LIST_CACHE_GROUP,
				OsCacheUtil.TONG_ID_LIST_FLUSH_PERIOD);
		// 缓存中没有
		if (count == null) {
			// 从数据库中取
			int count1 = SqlUtil.getIntResult(key, Constants.DBShortName);
			if (count1 < 0) {
				count1 = 0;
			}
			count = new Integer(count1);
			// 放到缓存中
			OsCacheUtil.put(key, count, OsCacheUtil.TONG_ID_LIST_CACHE_GROUP);
		}
		return count.intValue();
	}

	/**
	 * 转让帮会时按帮会人数排序
	 * 
	 * @return List
	 */
	public static List getTongListByIdOrderByCount() {
		String key = "tonglistCount";
		// 从缓存中取
		List tongList = (List) OsCacheUtil.get(key,
				OsCacheUtil.TONG_ID_LIST_CACHE_GROUP,
				OsCacheUtil.TONG_ID_LIST_FLUSH_PERIOD);
		// 缓存中没有
		if (tongList == null) {
			// 从数据库中取
			String sql = "SELECT id from jc_tong where mark=0 order by user_count desc";
			tongList = (List) SqlUtil.getIntList(sql, Constants.DBShortName);
			// 为空
			if (tongList == null) {
				tongList = new ArrayList();
				// liuyi 2007-01-05 start
				// return tongList;
				// liuyi 2007-01-05 end
			}
			// 放到缓存中
			OsCacheUtil
					.put(key, tongList, OsCacheUtil.TONG_ID_LIST_CACHE_GROUP);
		}
		return tongList;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中帮会记录信息
	 * @datetime:2007-1-3 下午04:59:55
	 * @param tongId
	 */
	public static void flushTong(int tongId) {
		String key = getKey(tongId);
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_CACHE_GROUP, key);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中所有帮会记录信息
	 * @datetime:2007-1-3 下午05:00:11
	 */
	public static void flushTongAll() {
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会记录
	 * @datetime:2007-1-3 下午05:00:31
	 * @param tongId
	 * @return
	 */
	public static TongBean getTong(int tongId) {
		String key = getKey(tongId);
		// 从缓存中取
		TongBean tong = (TongBean) OsCacheUtil.get(key,
				OsCacheUtil.TONG_CACHE_GROUP, OsCacheUtil.TONG_FLUSH_PERIOD);
		// 缓存中没有
		if (tong == null) {
			// 从数据库中取
			tong = service.getTong("id = " + tongId);
			// 为空
			if (tong == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, tong, OsCacheUtil.TONG_CACHE_GROUP);
		}

		return tong;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加帮会信息
	 * @datetime:2007-1-3 下午05:00:41
	 * @param bean
	 * @return
	 */
	public static boolean addTong(TongBean bean) {
		if (bean == null)
			return false;
		boolean flag = service.addTong(bean);
		if (flag) {
			// 清空缓存
			flushTongListAll();
		}
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新帮会记录
	 * @datetime 2007-1-2 上午10:22:30
	 * @param set
	 * @param condition
	 * @param tongId
	 * @return
	 */
	public static boolean updateTong(String set, String condition, int tongId) {
		// 更新记录
		if (!service.updateTong(set, condition)) {
			return false;
		}
		// 清空缓存
		// flushTongListAll();
		flushTong(tongId);

		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新帮会城墙
	 * @datetime:2007-1-3 下午05:00:51
	 * @param set
	 * @param condition
	 * @param tongId
	 * @param nowEndure
	 * @param highEndure
	 * @return
	 */
	public static boolean updateTongEndure(String set, String condition,
			int tongId, int nowEndure, int highEndure) {
		// 更新记录
		if (!service.updateTong(set, condition)) {
			return false;
		}
		// 更新内存中开发度值
		TongBean tong = getTong(tongId);
		if (nowEndure == 0) {
			tong.setNowEndure(0);
		} else {
			tong.setNowEndure(tong.getNowEndure() + nowEndure);
		}
		tong.setHighEndure(tong.getHighEndure() + highEndure);
		return true;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain： 更新帮会商店开发度
	 * @datetime:2007-7-3 8:09:58
	 * @param set
	 * @param condition
	 * @param tongId
	 * @param nowEndure
	 * @param highEndure
	 * @return
	 * @return boolean
	 */
	public static boolean updateTongShop(String set, String condition,
			int tongId, int point) {
		// 更新记录
		if (!service.updateTong(set, condition)) {
			return false;
		}
		// 更新内存中开发度值
		TongBean tong = getTong(tongId);
		tong.setShop(tong.getShop() + point);
		return true;
	}
	//增加仓库开发度
	public static boolean updateTongDepot(int tongId, int add) {
		if (!service.updateTong("depot_week=depot_week+" + add, "id=" + tongId)) {
			return false;
		}
		TongBean tong = getTong(tongId);
		tong.setDepotWeek(tong.getDepotWeek() + add);
		return true;
	}

	/**
	 * @author macq
	 * @explain : 更新所有帮会记录
	 * @datetime 2007-1-2 上午10:22:30
	 * @param set
	 * @param condition
	 * @return
	 */
	public static boolean updateTong(String set, String condition) {
		// 更新记录
		if (!service.updateTong(set, condition)) {
			return false;
		}
		// 清空缓存
		// flushTongListAll();
		flushTongAll();
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 删除一条帮会记录
	 * @datetime 2007-1-2 上午10:22:30
	 * @param condition
	 * @param tongId
	 * @return
	 */
	public static boolean deleteTong(String condition, int tongId) {
		// 删除记录
		if (!service.delTong(condition)) {
			return false;
		}
		// 清空缓存
		flushTongListAll();
		flushTong(tongId);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中帮会会员ID信息
	 * @datetime:2007-1-2 上午11:26:54
	 * @param tongId
	 */
	public static void flushTongUserListById(int tongId) {
		String key = getKey(tongId);
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_USER_ID_LIST_CACHE_GROUP, key);
	}

	// public static void flushTongUserListByIdOrderByCount() {
	// String key = "tonglistCount";
	// OsCacheUtil.flushGroup(OsCacheUtil.TONG_USER_ID_LIST_CACHE_GROUP, key);
	// }

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中所有帮会会员信息
	 * @datetime:2007-1-2 上午11:27:31
	 */
	public static void flushTongUserListAll() {
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_USER_ID_LIST_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取所有帮会会员ID列表
	 * @datetime:2007-1-2 上午11:25:51
	 * @param tongId
	 * @return
	 */
	public static List getTongUserListById(int tongId) {
		String key = getKey(tongId);
		// 从缓存中取
		List tongUserList = (List) OsCacheUtil.get(key,
				OsCacheUtil.TONG_USER_ID_LIST_CACHE_GROUP,
				OsCacheUtil.TONG_USER_ID_LIST_FLUSH_PERIOD);
		// 缓存中没有
		if (tongUserList == null) {
			// 从数据库中取
			String sql = "SELECT user_id from jc_tong_user where tong_id="
					+ tongId + " order by donation desc";
			tongUserList = (List) SqlUtil
					.getIntList(sql, Constants.DBShortName);
			// 为空
			if (tongUserList == null) {
				tongUserList = new ArrayList();
				// liuyi 2007-01-05 start
				// return tongUserList;
				// liuyi 2007-01-05 end
			}
			// 放到缓存中
			OsCacheUtil.put(key, tongUserList,
					OsCacheUtil.TONG_USER_ID_LIST_CACHE_GROUP);
		}
		return tongUserList;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中帮会会员信息
	 * @datetime:2007-1-3 下午05:02:36
	 * @param tongId
	 * @param userId
	 */
	public static void flushTongUser(int tongId, int userId) {
		tongUserCache.srm(userId);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中所有帮会会员信息
	 * @datetime:2007-1-2 上午11:28:05
	 */
	public static void flushTongUserAll() {
		tongUserCache.clear();
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会会员记录
	 * @datetime:2007-1-3 下午05:02:51
	 * @param tongId
	 * @param userId
	 * @return
	 */
	public static TongUserBean getTongUser(int tongId, int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(tongUserCache) {
			TongUserBean tongUser = (TongUserBean) tongUserCache.get(key);

			if (tongUser == null) {

				tongUser = service.getTongUser("tong_id = " + tongId
						+ " and user_id = " + userId);
				// 为空或者不是该帮会成员
				if (tongUser == null || tongUser.getTongId() != tongId) {
					return null;
				}
				tongUserCache.put(key, tongUser);
			}
			return tongUser;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加帮会会员
	 * @datetime:2007-1-2 上午11:35:22
	 * @param bean
	 * @return
	 */
	public static boolean addTongUser(TongUserBean bean) {
		if (bean == null)
			return false;
		boolean flag = service.addTongUser(bean);
		if (flag) {
			// 清空缓存
			flushTongUserListById(bean.getTongId());
		}
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新帮会会员信息
	 * @datetime:2007-1-2 上午11:35:55
	 * @param set
	 * @param condition
	 * @param tongId
	 * @param userId
	 * @return
	 */
	public static boolean updateTongUser(String set, String condition,
			int tongId, int userId) {
		// 更新记录
		if (!service.updateTongUser(set, condition)) {
			return false;
		}
		// 清空缓存
		// flushTongUserListById(tongId);
		flushTongUser(tongId, userId);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新用户贡献度
	 * @datetime:2007-1-9 上午10:23:24
	 * @param set
	 * @param condition
	 * @param tongId
	 * @param userId
	 * @return
	 */
	public static boolean updateTongUserDonation(String set, String condition,
			int tongId, int userId, int donation) {
		// 更新记录
		TongUserBean tongUser = getTongUser(tongId, userId);
		tongUser.setDonation(tongUser.getDonation() + donation);
		if(tongUser.getDonation() % 10 + donation >= 10) {		// 10点贡献才保存到数据库
			if (!service.updateTongUser("donation=" + tongUser.getDonation(), condition)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新所有帮会会员信息
	 * @datetime:2007-1-3 下午05:03:11
	 * @param set
	 * @param condition
	 * @return
	 */
	public static boolean updateTongUser(String set, String condition) {
		// 更新记录
		if (!service.updateTongUser(set, condition)) {
			return false;
		}
		// 清空缓存
		// flushTongUserListAll();
		flushTongUserAll();
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 删除帮会会员
	 * @datetime:2007-1-2 上午11:37:28
	 * @param condition
	 * @param userId
	 * @param tongId
	 * @return
	 */
	public static boolean deleteTongUser(String condition, int userId,
			int tongId) {
		// 删除记录
		if (!service.delTongUser(condition)) {
			return false;
		}
		// 清空缓存
		flushTongUserListById(tongId);
		flushTongUser(tongId, userId);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 删除帮会会员
	 * @datetime:2007-1-2 上午11:37:28
	 * @param condition
	 * @return
	 */
	public static boolean deleteTongUserAll(String condition) {
		// 删除记录
		if (!service.delTongUser(condition)) {
			return false;
		}
		// 清空缓存
		flushTongUserListAll();
		flushTongUserAll();
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中帮会当铺信息
	 * @datetime:2007-1-2 上午11:27:54
	 * @param tongId
	 */
	public static void flushTongHockshop(int tongId) {
		String key = getKey(tongId);
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_HOCKSHOP_CACHE_GROUP, key);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中所有帮会当铺信息
	 * @datetime:2007-1-2 上午11:28:05
	 */
	public static void flushTongHockshopAll() {
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_HOCKSHOP_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会当铺记录
	 * @datetime:2007-1-2 上午11:26:17
	 * @param tongId
	 * @return
	 */
	public static TongHockshopBean getTongHockshop(int tongId) {
		String key = getKey(tongId);
		// 从缓存中取
		TongHockshopBean tongHockshop = (TongHockshopBean) OsCacheUtil.get(key,
				OsCacheUtil.TONG_HOCKSHOP_CACHE_GROUP,
				OsCacheUtil.TONG_HOCKSHOP_FLUSH_PERIOD);
		// 缓存中没有
		if (tongHockshop == null) {
			// 从数据库中取
			tongHockshop = service.getTongHockshop("tong_id = " + tongId);
			// 为空
			if (tongHockshop == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, tongHockshop,
					OsCacheUtil.TONG_HOCKSHOP_CACHE_GROUP);
		}
		return tongHockshop;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加帮会当铺
	 * @datetime:2007-1-2 上午11:35:22
	 * @param bean
	 * @return
	 */
	public static boolean addTongHockshop(TongHockshopBean bean) {
		if (bean == null)
			return false;
		boolean flag = service.addTongHockshop(bean);
		if (flag) {
			// 清空缓存
			flushTongHockshop(bean.getTongId());
		}
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新帮会当铺开发度
	 * @datetime:2007-1-3 下午04:59:24
	 * @param set
	 * @param condition
	 * @param tongId
	 * @param develop
	 * @return
	 */
	public static boolean updateTongHockshopDevelop(String set,
			String condition, int tongId, int develop) {
		// 更新记录
		if (!service.updateTongHockshop(set, condition)) {
			return false;
		}
		// 更新内存中开发度值
		TongHockshopBean tongHockshop = getTongHockshop(tongId);
		tongHockshop.setDevelop(tongHockshop.getDevelop() + develop);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新所有帮会当铺信息
	 * @datetime:2007-1-2 上午11:36:44
	 * @param set
	 * @param condition
	 * @return
	 */
	public static boolean updateTongHockshopAll(String set, String condition) {
		// 更新记录
		if (!service.updateTongHockshop(set, condition)) {
			return false;
		}
		// 清空缓存
		flushTongHockshopAll();
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 删除帮会当铺
	 * @datetime:2007-1-2 上午11:37:28
	 * @param condition
	 * @param tongId
	 * @return
	 */
	public static boolean deleteTongHockshop(String condition, int tongId) {
		// 删除记录
		if (!service.delTongHockshop(condition)) {
			return false;
		}
		// 清空缓存
		flushTongHockshop(tongId);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中帮会基金ID信息
	 * @datetime:2007-1-2 上午11:26:54
	 * @param tongId
	 * @param mark
	 * @param orderBy
	 */
	public static void flushTongFundListById(int tongId, int mark,
			String orderBy) {
		String key = tongId + "_" + mark + "_" + orderBy;
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_FUND_ID_LIST_CACHE_GROUP, key);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中所有帮会基金信息
	 * @datetime:2007-1-2 上午11:27:31
	 */
	public static void flushTongFundListAll() {
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_FUND_ID_LIST_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取所有帮会基金ID列表
	 * @datetime:2007-1-2 上午11:25:51
	 * @param tongId
	 * @param mark
	 * @param orderBy
	 * @return
	 */
	public static List getTongFundListById(int tongId, int mark, String orderBy) {
		String key = tongId + "_" + mark + "_" + orderBy;
		// 从缓存中取
		List tongFundList = (List) OsCacheUtil.get(key,
				OsCacheUtil.TONG_FUND_ID_LIST_CACHE_GROUP,
				OsCacheUtil.TONG_FUND_ID_LIST_FLUSH_PERIOD);
		// 缓存中没有
		if (tongFundList == null) {
			// 从数据库中取
			String sql = "SELECT id from jc_tong_fund where tong_id=" + tongId
					+ " and mark=" + mark + " order by " + orderBy + " desc";
			tongFundList = (List) SqlUtil
					.getIntList(sql, Constants.DBShortName);
			// 为空
			if (tongFundList == null) {
				tongFundList = new ArrayList();
				// liuyi 2007-01-05 start
				// eturn tongFundList;
				// liuyi 2007-01-05 end
			}
			// 放到缓存中
			OsCacheUtil.put(key, tongFundList,
					OsCacheUtil.TONG_FUND_ID_LIST_CACHE_GROUP);
		}
		return tongFundList;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中帮会基金信息
	 * @datetime:2007-1-2 上午11:27:54
	 * @param tongId
	 * @param userId
	 * @param mark
	 */
	public static void flushTongFund(int tongId, int userId, int mark) {
		String key = tongId + "_" + userId + "_" + mark;
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_FUND_CACHE_GROUP, key);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 清空缓存中所有帮会基金信息
	 * @datetime:2007-1-2 上午11:28:05
	 */
	public static void flushTongFundAll() {
		OsCacheUtil.flushGroup(OsCacheUtil.TONG_FUND_CACHE_GROUP);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会基金记录
	 * @datetime:2007-1-2 上午11:26:17
	 * @param tongId
	 * @param userId
	 * @param mark
	 * @return
	 */
	public static TongFundBean getTongFund(int tongId, int userId, int mark) {
		String key = tongId + "_" + userId + "_" + mark;
		// 从缓存中取
		TongFundBean tongFund = (TongFundBean) OsCacheUtil.get(key,
				OsCacheUtil.TONG_FUND_CACHE_GROUP,
				OsCacheUtil.TONG_FUND_FLUSH_PERIOD);
		// 缓存中没有
		if (tongFund == null) {
			// 从数据库中取
			tongFund = service.getTongFund("tong_id=" + tongId
					+ " and user_id=" + userId + " and mark=" + mark);
			// 为空
			if (tongFund == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, tongFund, OsCacheUtil.TONG_FUND_CACHE_GROUP);
		}
		return tongFund;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会基金使用记录
	 * @datetime:2007-1-2 上午11:26:17
	 * @param tongId
	 * @param userId
	 * @param mark
	 * @return
	 */
	public static TongFundBean getTongFundById(int tongFundId) {
		String key = getKey(tongFundId);
		// 从缓存中取
		TongFundBean tongFund = (TongFundBean) OsCacheUtil.get(key,
				OsCacheUtil.TONG_FUND_BY_ID_CACHE_GROUP,
				OsCacheUtil.TONG_FUND_BY_ID_FLUSH_PERIOD);
		// 缓存中没有
		if (tongFund == null) {
			// 从数据库中取
			tongFund = service.getTongFund("id=" + tongFundId);
			// 为空
			if (tongFund == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, tongFund,
					OsCacheUtil.TONG_FUND_BY_ID_CACHE_GROUP);
		}
		return tongFund;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加帮会基金
	 * @datetime:2007-1-2 上午11:35:22
	 * @param bean
	 * @param orderBy
	 * @return
	 */
	public static boolean addTongFund(TongFundBean bean, String orderBy) {
		if (bean == null)
			return false;
		boolean flag = service.addTongFund(bean);
		if (flag) {
			// 清空缓存
			flushTongFundListById(bean.getTongId(), bean.getMark(), orderBy);
		}
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新帮会基金信息
	 * @datetime:2007-1-3 下午05:05:26
	 * @param set
	 * @param condition
	 * @param tongId
	 * @param userId
	 * @param mark
	 * @param orderBy
	 * @return
	 */
	public static boolean updateTongFund(String set, String condition,
			int tongId, int userId, int mark, String orderBy) {
		// 更新记录
		if (!service.updateTongFund(set, condition)) {
			return false;
		}
		// 清空缓存
		flushTongFundListById(tongId, mark, orderBy);
		flushTongFund(tongId, userId, mark);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 更新所有帮会基金信息
	 * @datetime:2007-1-2 上午11:36:44
	 * @param set
	 * @param condition
	 * @return
	 */
	public static boolean updateTongFundAll(String set, String condition) {
		// 更新记录
		if (!service.updateTongFund(set, condition)) {
			return false;
		}
		// 清空缓存
		flushTongFundListAll();
		flushTongFundAll();
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 删除帮会基金
	 * @datetime:2007-1-3 下午05:05:51
	 * @param condition
	 * @param tongId
	 * @param tongFundId
	 * @param mark
	 * @param userId
	 * @param orderBy
	 * @return
	 */
	public static boolean deleteTongFund(String condition, int tongId,
			int tongFundId, int mark, int userId, String orderBy) {
		// 删除记录
		if (!service.delTongFund(condition)) {
			return false;
		}
		// 清空缓存
		flushTongFundListById(tongId, mark, orderBy);
		flushTongFund(tongId, userId, mark);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 键
	 * @datetime:2007-1-2 上午11:37:22
	 * @param userId
	 * @return
	 */
	public static String getKey(int userId) {
		return "" + userId;
	}

	/**
	 * liuyi 2007-01-25 获取某个帮会城墙的沦陷记录id列表
	 * 
	 * @param tongId
	 * @return
	 */
	public static List getTongDestroyHistoryIdList(int tongId) {
		String key = "" + tongId;
		List ret = (List) OsCacheUtil.get(key,
				OsCacheUtil.TONG_DESTROY_LIST_GROUP,
				OsCacheUtil.TONG_DESTROY_LIST_FLUSH_PERIOD);
		if (ret == null) {
			ret = SqlUtil.getIntList(
					"select id from jc_tong_destroy_history where tong_id="
							+ tongId + " order by id desc",
					Constants.DBShortName);
			if (ret == null) {
				ret = new ArrayList();
			}
			OsCacheUtil.put(key, ret, OsCacheUtil.TONG_DESTROY_LIST_GROUP);
		}
		return ret;
	}

	public static TongDestroyHistoryBean getTongDestroyHistory(int id) {
		String key = id + "";
		TongDestroyHistoryBean ret = (TongDestroyHistoryBean) OsCacheUtil.get(
				key, OsCacheUtil.TONG_DESTROY_GROUP,
				OsCacheUtil.TONG_DESTROY_FLUSH_PERIOD);
		if (ret == null) {
			ret = service.getTongDestroyHistoryBean(id);
			if (ret != null) {
				OsCacheUtil.put(key, ret, OsCacheUtil.TONG_DESTROY_GROUP);
			}
		}
		return ret;
	}

	public static boolean isDestroyed(int tongId) {
		boolean flag = false;
		String key = "destroy" + tongId;
		Object o = OsCacheUtil.get(key, OsCacheUtil.TONG_DESTROY_GROUP,
				OsCacheUtil.TONG_DESTROY_FLUSH_PERIOD * 4);
		flag = (o != null);
		return flag;
	}

	/**
	 * liuyi 2007-02-08 获取一个帮会的所有联盟帮会id
	 * 
	 * @param tongId
	 * @return
	 */
	public static List getFriendTongIds(int tongId) {
		if (tongId < 1) {
			return null;
		}
		String key = tongId + "";
		List ret = (List) OsCacheUtil.get(key, OsCacheUtil.FRIEND_TONG_GROUP,
				OsCacheUtil.FRIEND_TONG_FLUSH_PERIOD);
		if (ret == null) {
			String sql = "(select ftong_id from jc_tong_friend where tong_Id="
					+ tongId
					+ " and mark=1) union (select tong_id from jc_tong_friend where ftong_Id="
					+ tongId + " and mark=1)";
			ret = SqlUtil.getIntList(sql, Constants.DBShortName);

			if (ret == null) {
				ret = new ArrayList();
			}
			OsCacheUtil.put(key, ret, OsCacheUtil.FRIEND_TONG_GROUP);
		}
		return ret;
	}

	/**
	 * liuyi 2007-02-08 判断两个帮会是否是联盟帮会
	 * 
	 * @param tongId
	 * @param friendTongId
	 * @return
	 */
	public static boolean isFriendTong(int tongId, int friendTongId) {
		boolean flag = false;
		String key = tongId + "_" + friendTongId;
		if (tongId > friendTongId) {
			key = friendTongId + "_" + tongId;
		}
		Boolean ret = (Boolean) OsCacheUtil.get(key,
				OsCacheUtil.FRIEND_TONG_GROUP,
				OsCacheUtil.FRIEND_TONG_FLUSH_PERIOD);
		if (ret == null) {
			String sql = "(select ftong_id from jc_tong_friend where tong_id="
					+ tongId
					+ " and ftong_id="
					+ friendTongId
					+ " and mark=1) union (select tong_id from jc_tong_friend where ftong_id="
					+ tongId + " and tong_id=" + friendTongId + " and mark=1)";
			int id = SqlUtil.getIntResult(sql, Constants.DBShortName);
			flag = (id > 0);
			ret = new Boolean(flag);
			OsCacheUtil.put(key, ret, OsCacheUtil.FRIEND_TONG_GROUP);
		} else {
			flag = ret.booleanValue();
		}
		return flag;
	}

}
