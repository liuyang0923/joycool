package net.joycool.wap.util;

import java.util.Vector;

import net.joycool.wap.bean.home.HomeHitsBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IHomeService;

public class HomeHitsCacheUtil {

	private static Object homeHitsLock = new Object(); // 当在内存中不存在查询的聊天记录,改为查询数据库时的同步锁

	private static IHomeService homeService = ServiceFactory.createHomeService();

	/**
	 * 清空家园的缓存组。
	 * 
	 * @param contentId
	 */
	public static void flushHomeHitsGroup() {
		OsCacheUtil.flushGroup(OsCacheUtil.HOME_HITS_GROUP);
	}

	/**
	 * 取得某各家园的历史访问次数记录。
	 * 
	 * @param contentId
	 * @return
	 */
	public static int getHomeHitsCount(int userId) {
		String key = getKey(userId);
		// 从缓存中取
		Integer homeHitsCount = (Integer) OsCacheUtil.get(key,
				OsCacheUtil.HOME_HITS_GROUP,
				OsCacheUtil.HOME_HITS_FLUSH_PERIOD);
		// 缓存中没有
		if (homeHitsCount == null) {
			//liuyi 2006-12-01 程序优化 start
			//synchronized (homeHitsLock) 
			{
				// 从缓存中取
				homeHitsCount = (Integer) OsCacheUtil.get(key,
						OsCacheUtil.HOME_HITS_GROUP,
						OsCacheUtil.HOME_HITS_FLUSH_PERIOD);
				if (homeHitsCount == null) {	
					Vector homeHitsList=null;
					int count=0;
					// 从数据库中取
					homeHitsList = homeService.getHomeHitsList("user_id = " + userId);
					HomeHitsBean homeHits=null;
					for (int i = 0; i < homeHitsList.size(); i++) {
						homeHits=(HomeHitsBean)homeHitsList.get(i);
						count+=homeHits.getHits();
					}
					homeHitsCount=new Integer(count);
					// 放到缓存中
					OsCacheUtil.put(key, homeHitsCount,
							OsCacheUtil.HOME_HITS_GROUP);
				}
			}
			//liuyi 2006-12-01 程序优化 end
		}

		return Integer.parseInt(homeHitsCount+"");
	}

	/**
	 * 取得缓存的key。
	 * 
	 * @param contentId
	 * @return
	 */
	public static String getKey(int userId) {
		return "" + userId;
	}

}
