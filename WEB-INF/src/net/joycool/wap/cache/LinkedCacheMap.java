/*
 * Created on 2006-8-7
 *
 */
package net.joycool.wap.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author bomb
 *  长缓存，用于游戏
 */
public class LinkedCacheMap extends AbstractCacheMap{

	int maxCache;
	LinkedHashMap2 map;

	protected boolean removeEldestEntry(Entry e) {
		return map.size() > maxCache;
	}

	public LinkedCacheMap(int cache) {
		map = new LinkedHashMap2(cache, cache * 2);

		maxCache = cache * 2;
	}
	
	public LinkedCacheMap(int cache, boolean accessOrder) {
		map = new LinkedHashMap2(cache, cache * 2, accessOrder);

		maxCache = cache * 2;
	}

	public int size() {
		return map.size();
	}
	
	public synchronized void clear() {
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

	public synchronized List keyList() {
		return new ArrayList(map.keySet());
	}

    public List valueList() {
    	return new ArrayList(map.values());
    }

	public int getMaxCache() {
		return maxCache;
	}

	public void setMaxCache(int maxCache) {
		this.maxCache = maxCache;
	}
}
