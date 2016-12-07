/*
 * Created on 2006-8-7
 *
 */
package net.joycool.wap.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author bomb
 *  定期删除缓存
 */
public class ExpireCacheMap extends AbstractCacheMap{

	int maxCache;
	LinkedHashMap2 map;
	int expire;
	int lastInsert = 0;		// 上次插入的数据

	protected boolean removeEldestEntry(Entry e) {
		return map.size() > maxCache;
	}

	// expire单位是分钟
	public ExpireCacheMap(int cache, int expire) {
		map = new LinkedHashMap2(cache, cache * 2);
		this.expire = expire;
		maxCache = cache * 2;
	}

	public void hourCheck(int hour) {
		if(hour % expire != 0)
			return;
		synchronized(this) {
			if(lastInsert > 0) {
				Set k = map.keySet();
				Iterator iter = k.iterator();
				while(iter.hasNext() && --lastInsert > 0) {
					iter.next();
					iter.remove();
				}
			}
			lastInsert = map.size();
		}
	}

	public int size() {
		return map.size();
	}
	
	public void clear() {
		map.clear();
	}
	
	public void put(Object key, Object value) {
		map.put(key, value);
	}

	public Object get(Object key) {
		return map.get(key);
	}
	
	public Object rm(Object key) {
		return map.remove(key);
	}

	public synchronized void spt(Object key, Object value) {
		map.put(key, value);
	}

	public synchronized Object sgt(Object key) {
		return map.get(key);
	}

	public synchronized void spt(int key, Object value) {
		map.put(Integer.valueOf(key), value);
	}

	public synchronized Object sgt(int key) {
		return map.get(Integer.valueOf(key));
	}
	public synchronized Object srm(Object key) {
		return map.remove(key);
	}
	public synchronized Object srm(int key) {
		return map.remove(Integer.valueOf(key));
	}

	public int maxSize() {
		return maxCache;
	}

	public List keyList() {
		return new ArrayList(map.keySet());
	}

    public List valueList() {
    	return new ArrayList(map.values());
    }
}
