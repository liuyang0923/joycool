package net.joycool.wap.cache.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Vector;

import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.bean.home.HomePhotoBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.spec.buyfriends.ServiceTrend;
import net.joycool.wap.spec.buyfriends.ServiceVisit;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.GifEncoder;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;

public class HomeCacheUtil {
	private static IHomeService service = ServiceFactory.createHomeService();
	
	public static ICacheMap homeUserCache = CacheManage.addCache(new LinkedCacheMap(2000, true), "homeUser");

	public static HashMap HomeSmallImageMap = new HashMap();

	public static HashMap HomeBigImageMap = new HashMap();
	
	static byte[] lock = new byte[0];
	public static int HOME_STAR = 0;
	public static HomeUserBean getStarUser() {
		if(HOME_STAR == 0) {
			synchronized(lock) {
				if(HOME_STAR == 0) {
					HOME_STAR = SqlUtil.getIntResult("SELECT user_id from jc_home_user order by id desc limit " + RandomUtil.nextInt(100) + ",1");
					if(HOME_STAR == -1)
						HOME_STAR = 0;
				}
			}
		}
		return getHomeCache(HOME_STAR);
	}
	public static void resetStarUser() {
		synchronized(lock) {
			HOME_STAR = 0;
		}
	}
	

	/**
	 * 清空每个用户的家园属性缓存。
	 * 
	 * @param userId
	 */
	public static void flushHomeById(int userId) {
		homeUserCache.srm(new Integer(userId));
	}
	public static void flushHomePhoneById(int userId) {
		String key = getKey(userId);
		OsCacheUtil.flushGroup(OsCacheUtil.HOME_USER_PHONE_CACHE_GROUP, key);
	}
	/**
	 * 清空所有用户的家园缓存。
	 * 
	 * @param userId
	 */
	public static void flushHome() {
		homeUserCache.clear();
	}

	/**
	 * 取得某个用户的家园记录。
	 * 
	 * @param userId
	 * @return HomeUserBean
	 */
	public static HomeUserBean getHomeCache(int userId) {
		Integer key = new Integer(userId);
		HomeUserBean homeUser;
		synchronized(homeUserCache) {
			homeUser = (HomeUserBean) homeUserCache.get(key);
			if (homeUser == null) {
	
				homeUser = service.getHomeUser("user_id = " + userId);
				if(homeUser != null)
					homeUserCache.put(key, homeUser);
			} else {	// 缓存中存在，直接返回，否则需要读取最近好友动态
				return homeUser;
			}
		}
		if (homeUser != null) {
			homeUser.getRecentVisit().addAll(
					ServiceVisit.getInstance().getVisitByToUid(userId, 0, 10));

			homeUser.getTrend().addAll(
					ServiceTrend.getInstance().getTrendIdByUid(userId, 0, ServiceTrend.USER_TREND_COUNT));
			
		}
		return homeUser;
	}
	
	public static HomePhotoBean getHomephoneCache(int userId) {
		String key = getKey(userId);
		// 从缓存中取
		HomePhotoBean homePhone = (HomePhotoBean) OsCacheUtil.get(key,
				OsCacheUtil.HOME_USER_PHONE_CACHE_GROUP,
				OsCacheUtil.HOME_USER_FLUSH_PERIOD);
		// 缓存中没有
		if (homePhone == null) {
			// 从数据库中取
			homePhone = service.getHomePhoto("user_id = " + userId);
			// 为空
			if (homePhone == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, homePhone, OsCacheUtil.HOME_USER_PHONE_CACHE_GROUP);
		}

		return homePhone;
	}
	
	/**
	 * 取得某个用户的家园照片。
	 * 
	 * @param userId
	 * @return HomeUserBean
	 */
	public static Vector getHomePhoneListCache(int userId) {
		Vector homePhoneList = null;
		String key = getKey(userId);
		// 从缓存中取
		homePhoneList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.HOME_USER_PHONE_CACHE_GROUP,
				OsCacheUtil.HOME_USER_FLUSH_PERIOD);
		// 缓存中没有
		if (homePhoneList == null) {
			// 从数据库中取
			homePhoneList = service.getHomePhotoList("user_id = " + userId);
			// 为空
			if (homePhoneList == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, homePhoneList, OsCacheUtil.HOME_USER_PHONE_CACHE_GROUP);
		}

		return homePhoneList;
	}

	/**
	 * 增加某个用户的家园记录。
	 * 
	 * @param HomeUserBean
	 * @return boolean
	 */
	public static boolean addHomeCache(HomeUserBean bean) {
		if (bean == null)
			return false;
		return service.addHomeUser(bean);
	}

	/**
	 * 更新某个用户的家园记录。
	 * 
	 * @param set
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean updateHomeCacheById(String set, String condition,
			int userId) {
		// 更新家园记录
		if (!service.updateHomeUser(set, condition)) {
			return false;
		}
		// 清空缓存
		flushHomeById(userId);
		return true;
	}

	/**
	 * 更新所有用户的家园记录。
	 * 
	 * @param set
	 * @param condition
	 * @return boolean
	 */
	public static boolean updateHomeCahce(String set, String condition) {
		// 更新家园记录
		if (!service.updateHomeUser(set, condition)) {
			return false;
		}
		// 清空缓存
		flushHome();
		return true;
	}

	/**
	 * 删除某个用户的家园记录。
	 * 
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean deleteHomeCache(String condition, int userId) {
		// 删除家园记录
		if (!service.deleteHomeUser(condition)) {
			return false;
		}
		// 清空缓存
		flushHomeById(userId);
		return true;
	}
	/**
	 * 删除某个用户的家园照片。
	 * 
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean deleteHomePhoneCache(String condition, int userId) {
		if (!service.deleteHomePhoto(condition)) {
			return false;
		}
		// 清空缓存
		flushHomePhoneById(userId);
		return true;
	}

	/**
	 * 取得缓存的key。
	 * 
	 * @param userId
	 * @return
	 */
	public static String getKey(int userId) {
		return "" + userId;
	}

	/**
	 * 获取家园大图片缓存
	 * 
	 * @param roomId
	 * @return
	 */
	public static void getHomeBigImageFileNameList(String fileName) {
		boolean flag = false;
		// 判断Map中是否包含该图片名称
		flag = HomeBigImageMap.containsKey(fileName);
		if (!flag) {
			try {
				// 判断操作系统
				boolean isWindows = false;
				String osName = System.getProperty("os.name");
				if (osName != null) {
					isWindows = (osName.toLowerCase().indexOf("windows") != -1);
				}
				// 根据操作系统类型选择文件路径
				String imagePath = (isWindows) ? Constants.CREATE_HOME_IMAGE_BIG_WINDOWS_ROOT
						: Constants.HOME_IMAGE_BIG_LINUX_ROOT;
				// 判断家园图片文件是否存在
				File f = new File(imagePath, fileName + ".gif");
				if (!f.exists()) {
					// 查分用户拥有家园的家具明细id
					String[] imageId = fileName.split("_");
					// 从内存中获取图片
					HashMap map = LoadResource.getHomImageBigFileMap();
					BufferedImage image = null;
					BufferedImage ret = null;
					// 循环取得每个用户家具并画图
					for (int i = 0; i < imageId.length; i++) {
						image = (BufferedImage) map.get(imageId[i]);
						if (i == 0) {
							ret = new BufferedImage(image.getWidth(), image
									.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
						}
						ret.getGraphics().drawImage(image, 0, 0, null);
					}
					FileOutputStream output = new FileOutputStream(imagePath
							+ "/" + fileName + ".gif");
					new GifEncoder(ret, output, true).encode();
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 添加家园图片名称到到map中
			HomeBigImageMap.put(fileName, "");
		}
		return;
	}

	/**
	 * 获取家园小图片缓存
	 * 
	 * @param roomId
	 * @return
	 */
	public static void getHomeSamllImageFileNameList(String fileName) {
		boolean flag = false;
		// 判断Map中是否包含该图片名称
		flag = HomeSmallImageMap.containsKey(fileName);
		if (!flag) {
			try {
				// 判断操作系统
				boolean isWindows = false;
				String osName = System.getProperty("os.name");
				if (osName != null) {
					isWindows = (osName.toLowerCase().indexOf("windows") != -1);
				}
				// 根据操作系统类型选择文件路径
				String imagePath = (isWindows) ? Constants.CREATE_HOME_IMAGE_SMALL_WINDOWS_ROOT
						: Constants.HOME_IMAGE_SMALL_LINUX_ROOT;
				// 判断家园图片文件是否存在
				File f = new File(imagePath, fileName + ".gif");
				if (!f.exists()) {
					// 循环取得每个用户家具并画图
					String[] imageId = fileName.split("_");
					HashMap map = LoadResource.getHomImageSmallFileMap();
					BufferedImage image = null;
					BufferedImage ret = null;
					for (int i = 0; i < imageId.length; i++) {
						image = (BufferedImage) map.get(imageId[i]);
						if (i == 0) {
							ret = new BufferedImage(image.getWidth(), image
									.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
						}
						ret.getGraphics().drawImage(image, 0, 0, null);
					}
					FileOutputStream output = new FileOutputStream(imagePath
							+ "/" + fileName + ".gif");
					new GifEncoder(ret, output, true).encode();
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 添加家园图片名称到到map中
			HomeSmallImageMap.put(fileName, "");
		}
		return;
	}

	// 清空家园大图片缓存列表
	public static void clearHomeBigImageMap() {
		HomeBigImageMap = new HashMap();
	}

	// 清空家园小图片缓存列表
	public static void clearHomeSmallImageMap() {
		HomeSmallImageMap = new HashMap();
	}
}
