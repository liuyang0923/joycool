package net.joycool.wap.util;

import net.joycool.wap.bean.bank.MoneyLogBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;

public class BankCacheUtil {

	private static IBankService service = ServiceFactory.createBankService();

	/**
	 * 清空每个用户的银行存款属性缓存。
	 * 
	 * @param userId
	 */
	public static void flushBankStoreById(int userId) {
		String key = getKey(userId);
		OsCacheUtil.flushGroup(OsCacheUtil.BANK_CACHE_GROUP, key);
	}
	
	/**
	 * 清空所有用户的银行存款属性缓存。
	 * 
	 * @param userId
	 */
	public static void flushBankStore() {
		OsCacheUtil.flushGroup(OsCacheUtil.BANK_CACHE_GROUP);
	}

	/**
	 * 取得某个用户的存款记录。
	 * 
	 * @param userId
	 * @return StoreBean
	 */
	static byte[] lock = new byte[0];
	public static StoreBean getBankStoreCache(int userId) {
		String key = getKey(userId);
		// 从缓存中取
		StoreBean store = null;
		synchronized (lock) {
			store = (StoreBean) OsCacheUtil.get(key,
					OsCacheUtil.BANK_CACHE_GROUP,
					OsCacheUtil.BANK_CACHE_FLUSH_PERIOD);
			// 缓存中没有
			if (store == null) {
				// 从数据库中取
				store = service.getStore("user_id = " + userId);
				// 为空
				if (store == null) {
					return null;
				}
				// 放到缓存中
				OsCacheUtil.put(key, store, OsCacheUtil.BANK_CACHE_GROUP);
			}
		}
		return store;
	}

	/**
	 * 增加某个用户的存款记录。
	 * 
	 * @param StoreBean
	 * @return boolean
	 */
	public static boolean addBankStoreCache(StoreBean bean) {
		if (bean == null)
			return false;
		boolean flag = service.addStore(bean);
		if (flag) {
		// 清空缓存
		flushBankStoreById(bean.getUserId());
		}
		MoneyLogBean moneyLog = new MoneyLogBean();
		moneyLog.setUserId(bean.getUserId());
		moneyLog.setRUserId(0);
		moneyLog.setMoney(bean.getMoney());
		moneyLog.setType(Constants.BANK_STORE_TYPE);
		return service.addMoneyLog(moneyLog);
	}

	/**
	 * 更新某个用户的存款记录。
	 * 
	 * @param set
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	static byte[] lock2 = new byte[0];
	public static boolean updateBankStoreCacheById(long number,int userId,int rUserId,int type) {
		//搜索此用户是否存在存款记录,
		synchronized(lock2) {	
			StoreBean store = BankCacheUtil.getBankStoreCache(userId);
			//不存在存款记录,插入一条
			if (store == null) {
				if(number <= 0)
					return false;
				store = new StoreBean();
				store.setUserId(userId);
				store.setMoney(0);
				if(!BankCacheUtil.addBankStoreCache(store))
					return false;
			}
		
			//如果修改后的存款数量为负,插入失败
			if (store.getMoney() + number < 0) {
				return false;
			}
			//修改money值
			long current = store.getMoney();
			store.setMoney(current + number);
		
			//银行存款
			String set = "money="+store.getMoney();

			String condition = "user_id="+userId;
			// 更新银行存款记录
			if (!service.updateStore(set, condition)) {
				return false;
			}
			// 银行log
			MoneyLogBean moneyLog = new MoneyLogBean();
			moneyLog.setUserId(userId);
			moneyLog.setRUserId(rUserId);
			moneyLog.setMoney(number);
			moneyLog.setType(type);
			moneyLog.setCurrent(current);
			service.addMoneyLog(moneyLog);
		}
		flushBankStoreById(userId);
		return true;
	}
	// 获得银行同步锁
	public static byte[] getBankLock() {
		return lock2;
	}
	
	/**
	 * 更新所有用户的存款记录。
	 * 
	 * @param set
	 * @param condition
	 * @return boolean
	 */
	public static boolean updateBankStoreCahce(String set, String condition) {
		// 更新银行存款记录
		if (!service.updateStore(set, condition)) {
			return false;
		}
		// 清空缓存
		flushBankStore();
		return true;
	}

	/**
	 * 删除某个用户的存款记录。
	 * 
	 * @param condition
	 * @param userId
	 * @return boolean
	 */
	public static boolean deleteBankStoreCache(String condition, int userId) {
		// 删除银行存款记录
		if (!service.deleteStore(condition)) {
			return false;
		}
		// 清空缓存
		flushBankStoreById(userId);
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
	
	public static long getStoreMoney(int userId) {
		long storeMoney = 0;
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(userId);
		if (storeBean != null) {
			storeMoney = storeBean.getMoney();
		}
		return storeMoney;
	}
}
