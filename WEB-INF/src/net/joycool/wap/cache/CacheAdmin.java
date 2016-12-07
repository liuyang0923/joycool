/**
 *作者：李北金
 *创建日期：2006-08-07
 *说明：本类用于保存缓存。
 */
package net.joycool.wap.cache;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import net.joycool.wap.util.DateUtil;

/**
 * @author lbj
 * 
 */
public class CacheAdmin {
	/**
	 * 用于保存所有缓存。
	 */
	// liuyi 2006-09-15 增加缓存 start
	public static Hashtable cacheMap = new Hashtable();

	public static Object cacheMapLock = new Object();

	// 扫描缓存过期的间隔,以秒计
	public static int scanInterval = 4 * 60 * 60;

	// 默认的缓存群
	public static String defaultGroup = "default";

	// 默认的缓存过期时间,以秒计
	public static long defaultLifeTime = 24 * 60 * 60;

	// 最大缓存过期时间,以秒计
	public static long MAX_EXPIRE_TIME = 24 * 60 * 60;

	// liuyi 2006-09-15 增加缓存 end
	public static CacheScanThread scanner = new CacheScanThread();

	/**
	 * 每组缓存最大容量（条）。
	 */
	public static int MAX_SIZE = 5000;

	/**
	 * 取得所有缓存。
	 * 
	 * @return
	 */
	public static Hashtable getCacheMap() {
		return cacheMap;
	}

	/**
	 * 取得一组缓存。
	 * 
	 * @param group
	 * @return
	 */
	public static Hashtable getCacheMap(String group) {
		// liuyi 2006-09-15 增加缓存 start
		if (group == null) {
			group = defaultGroup;
		}
		Hashtable ht = (Hashtable) cacheMap.get(group);
		if (ht == null) {
			// liuyi 2006-12-01 程序优化 start
			// synchronized (cacheMapLock)
			{
				ht = (Hashtable) cacheMap.get(group);
				if (ht == null) {
					ht = new Hashtable();
					cacheMap.put(group, ht);
				}
			}
			// liuyi 2006-12-01 程序优化 end
		}
		// liuyi 2006-09-15 增加缓存 end
		return ht;
	}

	/**
	 * 保存缓存。
	 * 
	 * @param key
	 * @param value
	 * @param group
	 */
	public static void putInCache(String key, Object value, String group) {
		// liuyi 2006-09-15 增加缓存
		putInCache(key, value, group, defaultLifeTime);
	}

	/**
	 * liuyi 2006-09-15 保存缓存。
	 * 
	 * @param key
	 * @param value
	 * @param group
	 */
	public static void putInCache(String key, Object value, String group,
			long flushPeriod) {
		if (key == null || value == null) {
			return;
		}
		if (group == null) {
			group = defaultGroup;
		}
		Hashtable ht = getCacheMap(group);
		if (ht.size() > MAX_SIZE) {
			ht.clear();
		}

		CacheEntry cache = new CacheEntry();
		cache.setObject(value);
		cache.setLifeTime(flushPeriod);
		cache.setCacheTime(System.currentTimeMillis() / 1000);

		ht.put(key, cache);
	}

	/**
	 * 取出缓存。
	 * 
	 * @param key
	 * @param group
	 * @param flushPeriod
	 * @return
	 */
	public static Object getFromCache(String key, String group, int flushPeriod) {
		if (key == null) {
			return null;
		}
		if (group == null) {
			group = defaultGroup;
		}
		Hashtable ht = getCacheMap(group);
		CacheEntry cache = (CacheEntry) ht.get(key);
		if (cache == null) {
			return null;
		} else if ((System.currentTimeMillis() / 1000 - cache.getCacheTime()) > flushPeriod) {
			ht.remove(key);
			return null;
		}

		return cache.getObject();
	}

	/**
	 * 取出缓存。
	 * 
	 * @param key
	 * @param flushPeriod
	 * @return
	 */
	public static Object getFromCache(String key, int flushPeriod) {
		// if (key == null) {
		// return null;
		// }
		// Hashtable cm = getCacheMap();
		// Hashtable ht = null;
		// Collection list = cm.values();
		// CacheEntry cache = null;
		// if (list != null) {
		// Iterator itr = list.iterator();
		// while (itr.hasNext()) {
		// ht = (Hashtable) itr.next();
		// cache = (CacheEntry) ht.get(key);
		// if (cache != null) {
		// break;
		// }
		// }
		// }
		//
		// if (cache == null) {
		// return null;
		// } else if ((System.currentTimeMillis() / 1000 - cache.getCacheTime()
		// > flushPeriod)) {
		// ht.remove(key);
		// return null;
		// }

		// liuyi 2006-09-15 增加缓存 start
		return getFromCache(key, defaultGroup, flushPeriod);
		// liuyi 2006-09-15 增加缓存 end
	}

	/**
	 * 清空一组缓存。
	 * 
	 * @param group
	 */
	public static void flushGroup(String group) {
		if (group == null) {
			return;
		}
		try {
			getCacheMap(group).clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空所有缓存。
	 */
	public static void flushAll() {
		// liuyi 2006-12-29 清所有缓存修改 start
		Hashtable cacheMap = getCacheMap();
		Enumeration enu = cacheMap.keys();
		while(enu.hasMoreElements()){
			String group = (String)enu.nextElement();
			flushGroup(group);
		}
		// liuyi 2006-12-29 清所有缓存修改 end
	}

	// liuyi 2006-09-15 增加缓存 start
	public static class CacheScanThread extends Thread {
		public void run() {
			while (true) {
				try {
					Thread.sleep(scanInterval * 1000);
				} catch (InterruptedException e) {
					return;
				}
				try {
					// 删除过期的缓存对象
					System.out.println("*************"
							+ DateUtil.formatDate(new Date(),
									DateUtil.normalTimeFormat)
							+ "*******scan cache********");

					long currentTime = System.currentTimeMillis() / 1000;
					// 每个group的map
					Enumeration groups = cacheMap.keys();
					while (groups.hasMoreElements()) {
						String groupKey = (String) groups.nextElement();
						Hashtable groupMap = (Hashtable) cacheMap.get(groupKey);
						if (groupMap == null) {
							continue;
						}
						if(groupMap.size() >= 100) {
							System.out.println("group:" + groupKey + "    size:" + groupMap.size());
						}
						// 每个group里缓存的对象
						Enumeration group = groupMap.keys();
						while (group.hasMoreElements()) {
							String key = (String) group.nextElement();
							CacheEntry cache = (CacheEntry) groupMap.get(key);
							if (cache == null) {
								continue;
							}
							// 过期删除
							if ((currentTime - cache.getCacheTime()) > cache
									.getLifeTime()) {
								groupMap.remove(key);
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}
	// liuyi 2006-09-15 增加缓存 end
}
