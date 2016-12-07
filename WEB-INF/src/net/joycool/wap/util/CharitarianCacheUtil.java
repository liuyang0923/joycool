package net.joycool.wap.util;

import net.joycool.wap.bean.charitarian.CharitarianBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICharitarianService;

public class CharitarianCacheUtil{

	private static ICharitarianService service = ServiceFactory.createCharitarianService();

	/**
	 * 清空某个用户的捐款属性缓存。
	 * 
	 * @param userId
	 */
	public static void flushCharitarianById(int userId) {
		String key = getKey(userId);
		OsCacheUtil.flushGroup(OsCacheUtil.CHARITARIAN_CACHE_GROUP, key);
	}
	
	/**
	 * 清空所有用户的捐款属性缓存。
	 * 
	 */
	public static void flushCharitarian() {
		OsCacheUtil.flushGroup(OsCacheUtil.CHARITARIAN_CACHE_GROUP);
	}

	/**
	 * 取得某个用户的捐款记录。
	 * 
	 * @param userId
	 * @return CharitarianBean
	 */
	public static CharitarianBean getCharitarianCahceById(int userId) {
		String key = getKey(userId);
		// 从缓存中取
		CharitarianBean charitarian = (CharitarianBean) OsCacheUtil.get(key,
				OsCacheUtil.CHARITARIAN_CACHE_GROUP,
				OsCacheUtil.CHARITARIAN_CACHE_FLUSH_PERIOD);
		// 缓存中没有
		if (charitarian == null) {
			// 从数据库中取
			charitarian = service.getCharitarian("user_id = " + userId);
			// 为空
			if (charitarian == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, charitarian, OsCacheUtil.CHARITARIAN_CACHE_GROUP);
		}

		return charitarian;
	}

	/**
	 * 增加某个用户的捐款记录。
	 * 
	 * @param CharitarianBean
	 * @return boolean
	 */
	public static boolean addCharitarianCahce(CharitarianBean bean) {
		if (bean == null)
			return false;
		boolean flag = service.addCharitarian(bean);
		if (flag) {
		// 清空缓存
		flushCharitarianById(bean.getUserId());
		}
		return true;
	}

	/**
	 * 更新某个用户的捐款记录。
	 * 
	 * @param set
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean updateBankStoreCahceById(String set, String condition,int userId) {
		// 更新聊天记录
		if (!service.updateCharitarian(set, condition)) {
			return false;
		}
		// 清空缓存
		flushCharitarianById(userId);
		return true;
	}
	
	/**
	 * 删除某个用户的捐款记录。
	 * 
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean deleteBankStoreCahce(String condition, int userId) {
		// 删除聊天记录
		if (!service.delCharitarian(condition)) {
			return false;
		}
		// 清空缓存
		flushCharitarianById(userId);
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
}
