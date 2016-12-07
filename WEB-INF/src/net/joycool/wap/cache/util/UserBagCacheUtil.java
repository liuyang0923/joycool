package net.joycool.wap.cache.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.TradeBean;
import net.joycool.wap.bean.TradeUserBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.item.ComposeBean;
import net.joycool.wap.cache.*;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.service.impl.UserbagItemServiceImpl;
/**
 * @author macq
 * @datetime 2006-12-13 上午09:58:27
 * @explain 用户行囊缓存方法
 */
public class UserBagCacheUtil {
	public static long MINUTE_MS = 60 * 1000;
	static IUserService service = ServiceFactory.createUserService();

	static IDummyService dummyService = ServiceFactory.createDummyService();

	static INoticeService noticeService = ServiceFactory.createNoticeService();

	static UserbagItemServiceImpl serviceImpl = new UserbagItemServiceImpl();
	
	public static ICacheMap userBagCache = CacheManage.userBag;
	public static ICacheMap userBagListCache = CacheManage.userBagList;
	public static ICacheMap userBagMapCache = CacheManage.userBagMap;
	static ICacheMap itemShowCache = CacheManage.itemShow;
	
	/**
	 * @author macq explain :清空用户的行囊记录id属性缓存。 datetime:2006-12-13 上午09:57:23
	 * @param userId
	 */
	public static void flushUserBagById(int userBagId) {
		Integer key = Integer.valueOf(userBagId);
		userBagCache.srm(key);
	}

	/**
	 * @author macq explain :清空每个用户的行囊物品id属性缓存。 datetime:2006-12-13 上午09:57:23
	 * @param userId
	 */
	public static void flushUserBagListById(int userId) {
		Integer key = Integer.valueOf(userId);
		itemShowCache.srm(key);
		userBagListCache.srm(key);
		userBagMapCache.srm(key);	// 列表和map同步
	}
	
	public static void flushUserBagListOnly(int userId) {
		Integer key = Integer.valueOf(userId);
		itemShowCache.srm(key);
		userBagListCache.srm(key);
	}
	
	public static void flushUserBagMapById(int userId) {
		Integer key = Integer.valueOf(userId);
		userBagMapCache.srm(key);
	}

	/**
	 * 
	 * @author macq explain : 取得某个用户行囊内所有物品记录id(time大于0是可以使用的物品)
	 *         datetime:2006-12-13 上午11:28:54
	 * @param userId
	 * @return List
	 */
	public static List getUserBagListCacheById(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(userBagListCache) {
			List userBagList = (List) userBagListCache.get(key);

			if (userBagList == null) {
				String sql = "SELECT id from user_bag where user_id=" + userId
						+ " and time>0 order by id desc";
				userBagList = (List) SqlUtil.getIntList(sql, Constants.DBShortName);

				if (userBagList == null) {
					userBagList = new ArrayList();
				}
				userBagListCache.put(key, userBagList);
			}
			return userBagList;
		}
	}
	/**
	 * explain :取得某个用户行囊内一条物品记录 datetime:2006-12-13 上午09:45:11
	 * 
	 * @param userId
	 * @return UserBagBean
	 */
	public static UserBagBean getUserBagCache(int userBagId) {
		Integer key = Integer.valueOf(userBagId);
		synchronized(userBagCache) {
			UserBagBean userBag = (UserBagBean) userBagCache.get(key);

			if (userBag == null) {
				userBag = service.getUserBag("id = " + userBagId + " and time>0");

				if (userBag == null) {
					return null;
				}
				userBagCache.put(key, userBag);
			}
			return userBag;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：通过类型获取一条行囊记录
	 * @datetime:2007-6-28 2:51:41
	 * @param userBagId
	 * @return
	 * @return UserBagBean
	 */
	public static int getUserBagById(int productId, int userId) {
		int id = SqlUtil.getIntResult("select id from user_bag where user_id="
				+ userId + " and product_id=" + productId
				+ " and time>0 limit 1", Constants.DBShortName);
		return id;
	}
	
	/**
	 * 返回一个用户拥有的某个物品的数量
	 * @param productId
	 * @param userId
	 * @return
	 */
	public static int getUserBagItemCount(int productId, int userId) {
		HashMap userBagMap = getUserBagItemMap(userId);
		if(userBagMap == null)
			return 0;
		int[] num = (int[])userBagMap.get(Integer.valueOf(productId));
		if(num == null)
			return 0;
		return num[0];
	}
	
	public static int getUserBagItemUseCount(int productId, int userId) {
		HashMap userBagMap = getUserBagItemMap(userId);
		if(userBagMap == null)
			return 0;
		int[] num = (int[])userBagMap.get(Integer.valueOf(productId));
		if(num == null)
			return 0;
		return num[1];
	}
	
	/** 
	 * 得到某个用户拥有的行囊物品分类map
	 * @param userId
	 * @return
	 */
	public static HashMap getUserBagItemMap(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(userBagMapCache) {
			HashMap userBagMap = (HashMap) userBagMapCache.get(key);

			if (userBagMap == null) {
				userBagMap = new HashMap();
				List userBagList = getUserBagListCacheById(userId);
				for(int i = 0;i < userBagList.size();i++) {
					Integer iid = (Integer)userBagList.get(i);
				    UserBagBean tmp = getUserBagCache(iid.intValue());
				    if(tmp == null)
				    	continue;
				    Integer iitem = Integer.valueOf(tmp.getProductId());
				    
				    DummyProductBean item = dummyService.getDummyProducts(tmp.getProductId());
				    if(item == null)
				    	continue;
				    
				    int[] data = (int[])userBagMap.get(iitem);
				    if(data == null) {
				    	data = new int[2];
				    	userBagMap.put(iitem, data);
				    }

				    if(item.getTime() == 1)	// 如果为1表示堆叠物品，大于1表示可以多次使用
				    	data[0] += tmp.getTime();
				    else
				    	data[0]++;
				    data[1] += tmp.getTime();
				}

				if (userBagMap == null) {
					return null;
				}
				userBagMapCache.put(key, userBagMap);
			}

			return userBagMap;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：判断用户行囊是否已满
	 * @datetime:2007-7-5 10:55:33
	 * @param userId
	 * @return
	 * @return boolean
	 */
	public static boolean checkUserBagCount(int userId) {
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(userId);
		if (userStatus == null) {
			return false;
		}
		List userBagList = getUserBagListCacheById(userId);
		return userStatus.getUserBag() > userBagList.size();
	}

	/**
	 * 
	 * explain :增加某个用户的行囊物品记录。 datetime:2006-12-13 上午09:49:15
	 * 
	 * @param bean
	 * @return boolean
	 */
	public static boolean addUserBagCache(UserBagBean bean) {
		if (bean == null)
			return false;
		service.addItemLog(0, bean.getUserId(), bean.getId(), bean.getProductId(), bean.getTime(), 4);
		if(bean.getProductId() == 0)		// 为什么会出现？
			return false;
		boolean flag = service.addUserBag(bean);
		if (flag) {
			// 清空缓存
			flushUserBagListById(bean.getUserId());
		}
		return true;
	}

	/**
	 * 
	 * explain :更新某个用户的行囊记录。 datetime:2006-12-13 上午09:50:43
	 * 
	 * @param set
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean updateUserBagCacheById(String set, String condition,
			int userId, int userBagId) {
		// 更新记录
		if (!service.updateUserBag(set, condition)) {
			return false;
		}
		// 清空缓存
		flushUserBagListById(userId);
		flushUserBagById(userBagId);
		return true;
	}
	
	public static boolean updateUserBagTime(UserBagBean userbag, int time) {
		if (!service.updateUserBag("time=" + time, "id=" + userbag.getId())) {
			return false;
		}
		userbag.setTime(time);
		return true;
	}
	
	public static boolean changeUserBagOwner(int userId, int userBagId) {
		// 更新记录
		if (!service.updateUserBag("user_id=" + userId, "id=" + userBagId)) {
			return false;
		}
		// 清空缓存
		flushUserBagListById(userId);
		flushUserBagById(userBagId);
		return true;
	}
	
	public static boolean changeUserBagOwner(int userId, int userBagId, int fromUserId) {
		// 更新记录
		if (!service.updateUserBag("user_id=" + userId, "id=" + userBagId)) {
			return false;
		}
		// 清空缓存
		flushUserBagListById(userId);
		flushUserBagListById(fromUserId);
		flushUserBagById(userBagId);
		return true;
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新某个用户行囊中的特殊道具记录
	 * @datetime:2007-6-29 5:25:11
	 * @param userId
	 * @param userBagId
	 * @return
	 * @return boolean
	 */
	public static boolean UseUserBagCacheById(int userId, int userBagId) {
		return UseUserBagCacheById(userId, userBagId, 1);
	}
	
	// 给一个物品增加堆叠
	public static boolean addUserBagCacheById(int userId, int userBagId, int count) {

		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);

		if (!service.updateUserBag("time=time+" + count, "id=" + userBagId)) {
			return false;
		}
		service.addItemLog(0, userId, userBag.getId(), userBag.getProductId(), count, 4);

		flushUserBagById(userBagId);
		return true;
	}
	
	// 直接删除一个物品
	public static void removeUserBagCache(int userBagId) {
		UserBagBean userBag = getUserBagCache(userBagId);
		if(userBag != null)
			removeUserBagCache(userBag);
	}
	public static void removeUserBagCache(UserBagBean userBag) {
		service.deleteUserBag("id=" + userBag.getId());
		if(userBag.getUserId() > 0) {
			flushUserBagListById(userBag.getUserId());
		}
		flushUserBagById(userBag.getId());
	}
	
	public static boolean UseUserBagCacheById(int userId, int userBagId, int count) {

		UserBagBean userBag = getUserBagCache(userBagId);

		return UseUserBagCache(userBag, count);
	}
	
	public static boolean UseUserBagCache(UserBagBean userBag, DummyProductBean item) {
		if(item.getCooldown() > 0) {// 这个物品需要冷却！
			service.updateUserBag("use_datetime=now()", "id=" + userBag.getId());
		}
		
		return UseUserBagCache(userBag, 1);
	}
	
	public static boolean UseUserBagCache(UserBagBean userBag) {
		return UseUserBagCache(userBag, 1);
	}

	public static boolean UseUserBagCache(UserBagBean userBag, int count) {

		if (userBag == null || userBag.getTime() < count)
			return false;
		
		// 更新记录
		if (!service.updateUserBag("time=time-" + count, "id=" + userBag.getId())) {
			return false;
		}
		service.addItemLog(userBag.getUserId(), 0, userBag.getId(), userBag.getProductId(), count, 5);
		if(userBag.getTime() == count)
			flushUserBagListById(userBag.getUserId());
		else		// 次数没用完，至少也要更新数量map
			flushUserBagMapById(userBag.getUserId());
			
		// 清空缓存
		
		flushUserBagById(userBag.getId());
		return true;
	}
	
	public static boolean useItem(int userId, int itemId) {
		int userBagId = getUserBagById(itemId, userId);
		if(userBagId > 0) {
			UserBagCacheUtil.UseUserBagCacheById(userId, userBagId);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * explain :用户虚拟物品转让,更新2个人的物品ID列表,清空对应用户物品记录。 datetime:2006-12-13 上午09:50:43
	 * 
	 * @param set
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean updateUserBagChangeCache(String set,
			String condition, int userId, int friendId, int userBagId) {
		// 更新记录
		if (!service.updateUserBag(set, condition)) {
			return false;
		}
		// 清空缓存
		flushUserBagListById(userId);
		flushUserBagListById(friendId);
		flushUserBagById(userBagId);
		return true;
	}

	/**
	 * 
	 * explain :删除某个用户的行囊记录。 datetime:2006-12-13 上午09:52:38
	 * 
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean deleteUserBagCache(String condition, int userId,
			int userBagId) {
		// 删除记录
		if (!service.deleteUserBag(condition)) {
			return false;
		}
		// 清空缓存中的一个记录ID
		flushUserBagListById(userId);
		flushUserBagById(userBagId);
		return true;
	}

	/**
	 * 
	 * explain :取得缓存的key。 datetime:2006-12-13 上午09:53:39
	 * 
	 * @param userId
	 * @return
	 */
	public static String getKey(int userId) {
		return String.valueOf(userId);
	}

	static byte[] lock = new byte[0];
	// 获得用户行囊同步锁
	public static byte[] getBagLock() {
		return lock;
	}
	/**
	 * 合成道具
	 * 
	 * @param userId
	 * @param userBagId
	 * @return -1 材料不足或其他错误 -2合成失败，材料丢失 >0 composeId
	 */
	public static int compose(int userId, int userBagId) {
		ComposeBean compose = null;
		int composeId;

		UserBagBean item = getUserBagCache(userBagId);
		if (item == null || item.getUserId() != userId) // 用户没有这个物品
			return -1;

		HashMap map = service.getItemComposeMap("1");
		compose = (ComposeBean) map.get(Integer.valueOf(item
				.getProductId())); // 得到物品对应的合成公式
		if (compose == null) // 如果没有得到，说明该物品不是一个合成物品
			return -1;

		List material = compose.getMaterialList();
		if(!checkMaterial(material, userId))	// 材料不足
			return -1;
		
		Iterator iter = compose.getProductList().iterator(); // 生成物品放入行囊
		while (iter.hasNext()) {
			Integer iid = (Integer) iter.next();
			DummyProductBean dummyProduct = dummyService.getDummyProducts(iid.intValue());
			if(dummyProduct.isUnique() && getUserBagItemCount(iid.intValue(), userId) > 0)
				return -3;		// 有些生成物品是唯一的而且已经拥有一个
		}

		removeMaterial(material, userId);

		UseUserBagCache(item); // 删除该合成卡

		if(compose.getFail() > 0 && RandomUtil.percentRandom(compose.getFail())) {		// 合成失败
			composeId = -2;
		} else {
			composeId = compose.getItemId();
			iter = compose.getProductList().iterator(); // 生成物品放入行囊
			while (iter.hasNext()) {
				Integer iid = (Integer) iter.next();
				addUserBag(userId, iid.intValue());
			}
		}
		
		flushUserBagListById(userId);

		return composeId;
	}
	
	// 直接用一个公式进行合成，不消耗合成卡
	public static int composeDirectly(int userId, int itemId) {
		ComposeBean compose = null;
		int composeId;
		
		UserBagBean item = null;
		HashMap map = service.getItemComposeMap("1");
		compose = (ComposeBean) map.get(Integer.valueOf(itemId)); // 得到物品对应的合成公式
		if (compose == null) // 如果没有得到，说明该物品不是一个合成物品
			return -1;

		List material = compose.getMaterialList();
		if(!checkMaterial(material, userId))	// 材料不足
			return -1;

		removeMaterial(material, userId);

		if(compose.getFail() > 0 && RandomUtil.percentRandom(compose.getFail())) {		// 合成失败
			composeId = -2;
		} else {
			composeId = compose.getItemId();
			Iterator iter = compose.getProductList().iterator(); // 生成物品放入行囊
			while (iter.hasNext()) {
				Integer iid = (Integer) iter.next();
				addUserBag(userId, iid.intValue());
			}
		}
		flushUserBagListById(userId);

		return composeId;
	}
	
	// 直接合成
	public static int composeDirectly(int userId, List material, int[] product) {
		int composeId;
		UserBagBean item = null;

		if(!checkMaterial(material, userId))	// 材料不足
			return -1;

		removeMaterial(material, userId);

		composeId = 1;
		if(product != null)
			for(int i=0;i < product.length;i++) {
				addUserBag(userId, product[i]);
			}

		flushUserBagListById(userId);

		return composeId;
	}
	
	// 直接合成
	public static int composeDirectly(int userId, List material, List product) {
		int composeId;
		UserBagBean item = null;

		if(!checkMaterial(material, userId))	// 材料不足
			return -1;

		removeMaterial(material, userId);

		composeId = 1;
		addProduct(userId, product);

		flushUserBagListById(userId);

		return composeId;
	}
	
	// 删除材料
	public static void removeMaterial(List material, List bag) {
		Iterator iter = material.iterator();
		while (iter.hasNext()) {
			int[] mat = (int[])iter.next();
			int matCount = mat[1];
			
			for(int i = 0;i < bag.size();i++) {
				Integer iid = (Integer) bag.get(i);
				UserBagBean item = getUserBagCache(iid.intValue());

				
				if(item != null && item.getProductId() == mat[0]) {		// 合成使用多个物品的时候，可能上一个循环删除的物品，这次取出来就是null
					int count = 1;
					if(item.getTime() > 1) {
						DummyProductBean dummy = dummyService.getDummyProducts(item.getProductId());
						if(dummy.getTime() == 1)	{	// 确认time为堆叠
							if(matCount > item.getTime())
								count = item.getTime();
							else
								count = matCount;
						}
					}
					matCount -= count;
					UseUserBagCache(item, count);
				}
				if(matCount <= 0)
					break;
			}
		}
	}
	public static void removeMaterial(List material, int userId) {
		removeMaterial(material, getUserBagListCacheById(userId));
	}
	
	// 给产品
	public static void addProduct(int userId, List product) {
		if(product != null)
			for(int i=0;i < product.size();i++) {
				int[] p = (int[])product.get(i);
				addUserBagCacheStack(userId, p[0], p[1]);
			}
	}
	// 给产品 if not exist
	public static void addProductINE(int userId, List product) {
		if(product != null)
			for(int i=0;i < product.size();i++) {
				int[] p = (int[])product.get(i);
				addUserBagCacheINE(userId, p[0], p[1]);
			}
	}
	
	// 给产品，如果是唯一的并且已经拥有则不给，返回给了的物品列表
	public static List checkAddProduct(int userId, List product) {
		List list = new ArrayList();
		for(int i=0;i < product.size();i++) {
			int[] p = (int[])product.get(i);
			DummyProductBean dummyProduct = dummyService.getDummyProducts(p[0]);
			if(dummyProduct.isUnique() && getUserBagItemCount(p[0], userId) > 0)
				continue;		// 有些生成物品是唯一的而且已经拥有一个
			addUserBagCache(userId, p[0], p[1], 0);
			list.add(p);
		}
		return list;
	}
	
	// 检查材料是否够
	public static boolean checkMaterial(List material, HashMap userBagMap) {
		Iterator iter = material.iterator();
		while (iter.hasNext()) {
			int[] mat = (int[])iter.next();
			int[] count = (int[])userBagMap.get(Integer.valueOf(mat[0]));
			if(count == null || mat[1] > count[0])	// 材料大于用户拥有的此类物品总和，按个数算，堆叠算，次数不算
				return false;
		}
		return true;
	}
	public static boolean checkMaterial(List material, int userId) {
		return checkMaterial(material, getUserBagItemMap(userId));
	}
	
	public static boolean checkRemoveMaterial(List material, int userId) {
		if(!checkMaterial(material, userId))
			return false;
		removeMaterial(material, userId);
		return true;
	}
	/**
	 * 送给用户一个道具
	 * 
	 * @param userId
	 *            用户
	 * @param itemId
	 *            道具
	 */
	public static void addUserBag(int userId, int itemId) {
//		DummyProductBean dummyProduct = dummyService.getDummyProducts(itemId);
//		UserBagBean userBagBean = new UserBagBean();
//		userBagBean.setMark(0);
//		userBagBean.setProductId(itemId);
//		userBagBean.setUserId(userId);
//		userBagBean.setTypeId(dummyProduct.getDummyId());
//		userBagBean.setTime(dummyProduct.getTime());
//		UserBagCacheUtil.addUserBagCache(userBagBean);
		addUserBagCache(userId, itemId, 1, 0);
	}

	// 添加物品到行囊，如果可以堆叠，寻找并堆叠
	public static DummyProductBean addUserBagCacheStack(int userId, int itemId) {
		return addUserBagCacheStack(userId, itemId, 1);
	}
	
	public static DummyProductBean addUserBagCacheStack(int userId, int itemId, int time) {
		DummyProductBean dummyProduct = dummyService.getDummyProducts(itemId);
		boolean stack = false;
		UserBagBean userBag2 = null;
		if(dummyProduct.getStack() > 1) {
			
			List userBagList = getUserBagListCacheById(userId);
			for(int i = 0;i < userBagList.size();i++) {
				Integer iid = (Integer)userBagList.get(i);
				userBag2 = getUserBagCache(iid.intValue());
			    if(userBag2.getProductId() == itemId && userBag2.getTime() + time <= dummyProduct.getStack()) {
			    	stack = true;
			    	break;
			    }
			}
		} 
		if(stack) {
			service.updateUserBag("time=time+" + time, "id=" + userBag2.getId());
			flushUserBagById(userBag2.getId());
			flushUserBagMapById(userId);
			stack = true;
		} else		// 找不到堆叠目标，添加
			addUserBagCache(userId, itemId, time);

		return dummyProduct;
	}
	// 如果用户没有某个物品则添加
	public static void addUserBagCacheINE(int userId, int itemId, int time) {
		if(getUserBagItemCount(itemId, userId) == 0) {
			addUserBagCache(userId, itemId, time, 0);
		}
	}
	
	public static DummyProductBean addUserBagCache(int userId, int itemId) {
		return addUserBagCache(userId, itemId, 1, 0);
	}
	
	public static DummyProductBean addUserBagCache(int userId, int itemId, int time, int due) {
		DummyProductBean item = dummyService.getDummyProducts(itemId);
		UserBagBean userBagBean = new UserBagBean();
		userBagBean.setMark(0);
		userBagBean.setProductId(itemId);
		userBagBean.setUserId(userId);
		userBagBean.setTypeId(item.getDummyId());
		if(time == 1) {
			userBagBean.setTime(item.getTime());
			if(item.getStack() == 1 && !item.isBind())	// 不可堆叠而且不是直接绑定物品才记录创造者
				userBagBean.setCreatorId(userId);
		} else
			userBagBean.setTime(time);

		if(due > 0)
			userBagBean.setEndTime(System.currentTimeMillis() + due * MINUTE_MS);
		else if(item.getDue() > 0)	// 有过期
			userBagBean.setEndTime(System.currentTimeMillis() + item.getDue() * MINUTE_MS);

		addUserBagCache(userBagBean);

		return item;
	}
	
	public static DummyProductBean addUserBagCache(int userId, int itemId, int time) {
		return addUserBagCache(userId, itemId, time, 0);
	}

	public static void addUserBagCacheNotice(int userId, int itemId, String note) {
		DummyProductBean item = addUserBagCache(userId, itemId);

		if (note == null)
			note = "你无意间拣到一张卡片";

		NoticeBean notice = new NoticeBean();
		notice.setTitle(note);
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(userId);
		notice.setHideUrl("/user/userBag.jsp");
		notice.setLink("/user/userBag.jsp");
		notice.setMark(777);
		noticeService.addNotice(notice);
	}

	/**
	 * 
	 * @author macq
	 * @explain：系统给用户分配帽子
	 * @datetime:2007-8-13 16:18:26
	 * @param userId
	 * @param itemId
	 * @return
	 * @return DummyProductBean
	 */
	public static void addUserBagCacheByHat(int userId, int itemId) {
		DummyProductBean dummyProduct = dummyService.getDummyProducts(itemId);
		UserBagBean userBagBean = new UserBagBean();
		userBagBean.setMark(0);
		userBagBean.setProductId(itemId);
		userBagBean.setUserId(userId);
		userBagBean.setTypeId(dummyProduct.getDummyId());
		userBagBean.setTime(dummyProduct.getTime());
		UserBagCacheUtil.addUserBagCache(userBagBean);

		flushUserBagListById(userId);

		if (dummyProduct.getDummyId() == 6) {
			// 更新用户帽子
			UserInfoUtil.updateUserStatus("image_path_id=" + userBagBean.getId(),
					"user_id=" + userId, userId, UserCashAction.OTHERS,
					"更新用户帽子");
			UserStatusBean.flushUserHat(userId);
		}
	}
	
	static HashMap tradeMap = new HashMap();
	
	static byte[] tradeLock = new byte[0];
	// 一个用户只能发起一个交易请求
	public static TradeBean newTrade(int fromUserId, int toUserId) {
		Integer key = Integer.valueOf(fromUserId);
		TradeBean bean;
		synchronized(tradeLock) {
			bean = (TradeBean)tradeMap.get(key);
			if(bean != null) {
				if(bean.getUser2().getUserId() == toUserId)
					return bean;
				else
					return null;		// 限制一个人只能发起一个交易请求
			}
			bean = new TradeBean();
			tradeMap.put(key, bean);
		}
		bean.setUser1(new TradeUserBean(fromUserId));
		bean.setUser2(new TradeUserBean(toUserId));
		
		return bean;
	}
	
	public static TradeBean getTrade(int id) {
		return (TradeBean)tradeMap.get(Integer.valueOf(id));
	}
	
	public static void removeTrade(int id) {
		tradeMap.remove(Integer.valueOf(id));
	}
	
	// 试图合并此物品
	public static boolean stack(UserBagBean userBag) {
		DummyProductBean item = dummyService.getDummyProducts(userBag.getProductId());
		if(item.getStack() == 1 || item.getStack() <= userBag.getTime())		// 不能堆叠或者堆叠满了的情况
			return false;

		UserBagBean userBag2 = null;
		boolean stack = false;
		List userBagList = getUserBagListCacheById(userBag.getUserId());
		DummyProductBean dummyProduct = null;
		for(int i = 0;i < userBagList.size();i++) {
			Integer iid = (Integer)userBagList.get(i);
			userBag2 = getUserBagCache(iid.intValue());
		    if(userBag2.getProductId() == userBag.getProductId() && userBag2.getId() != userBag.getId() && userBag2.getTime() < item.getStack()) {
		    	stack = true;
		    	break;
		    }
		}
		
		if(!stack)
			return false;
		int count = item.getStack() - userBag2.getTime();	// 移动的数量
		if(count > userBag.getTime())
			count = userBag.getTime();
		
		service.updateUserBag("time=time+" + count, "id=" + userBag2.getId());
		flushUserBagById(userBag2.getId());

		UseUserBagCacheById(userBag.getUserId(), userBag.getId(), count);

		return true;
	}
	// 拆散物品，拆出count的一堆
	public static boolean unstack(UserBagBean userBag, int count) {
		DummyProductBean item = dummyService.getDummyProducts(userBag.getProductId());
		if(item.getTime() > 1 || count < 1)		// 无法拆的情况
			return false;

		int left = userBag.getTime() - count;
		if(left <= 0)
			return false;
		
		service.updateUserBag("time=" + left, "id=" + userBag.getId());
		
		addUserBagCache(userBag.getUserId(), userBag.getProductId(), count);
		flushUserBagById(userBag.getId());

		return true;
	}
	
	//物品是否显示
	public static boolean isItemShow(int itemId) {
		return service.getItemShowMap("1").containsKey(Integer.valueOf(itemId));
	}
	
	public static DummyProductBean getItem(int itemId) {
		return dummyService.getDummyProducts(itemId);
	}

	// 将一个东西绑定
	public static void setBind(UserBagBean userBag) {
		userBag.addMarkBind();
		service.updateUserBag("mark=" + userBag.getMark(), "id=" + userBag.getId());
	}
}
