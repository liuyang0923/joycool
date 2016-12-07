/*
 * Created on 2006-4-11
 *
 */
package net.joycool.wap.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import net.joycool.wap.bean.AreaBean;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author bomb
 *	产生特定的同步锁
 */
public class LockUtil {
	
	static HashMap groups = new HashMap();

	public static Object getLock(Object groupKey, Object key) {
		HashMap group = (HashMap)groups.get(groupKey);
		if(group == null)
			synchronized(groups) {
				group = (HashMap)groups.get(groupKey);
				if(group == null) {
					group = new HashMap();
					groups.put(groupKey, group);
				}
			}
		
		Object lock = group.get(key);
		if(lock == null)
			synchronized(group) {
				lock = group.get(key);
				if(lock == null) {
					lock = new byte[0];
					group.put(key, lock);
				}
			}
		
		return lock;
	}
	
	HashMap group = new HashMap();	// 实例lockutil之后的方式来获得锁，更为快速
	public Object getLock(Object key) {
		Object lock = group.get(key);
		if(lock == null)
			synchronized(group) {
				lock = group.get(key);
				if(lock == null) {
					lock = new byte[0];
					group.put(key, lock);
				}
			}
		
		return lock;
	}
	
	public Object getLock(int key) {
		return getLock(Integer.valueOf(key));
	}
	
	public static LockUtil userLock = new LockUtil();	// 用户操作同步，例如银行、现金、物品等
	public static LockUtil tongLock = new LockUtil();	// 帮会取基金，取商店等
	
	public static LockUtil farmUserLock = new LockUtil();	// farm user lock
	
	static HashMap rGroup = new HashMap();
	public static ReentrantLock getRLock(Object key) {
		ReentrantLock lock = (ReentrantLock)rGroup.get(key);
		if(lock == null) {
			synchronized(rGroup) {
				lock = (ReentrantLock)rGroup.get(key);
				if(lock == null) {
					lock = new ReentrantLock();
					rGroup.put(key, lock);
				}
			}
		}
		return lock;
	}
	public static ReentrantLock getRLock(int id) {
		return getRLock(Integer.valueOf(id));
	}
}
