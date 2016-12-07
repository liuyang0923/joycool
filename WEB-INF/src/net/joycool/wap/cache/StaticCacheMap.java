/*
 * Created on 2006-8-7
 *
 */
package net.joycool.wap.cache;

import java.util.*;

/**
 * @author bomb
 *  静态长缓存，用于不变的数据，但要可以reload
 */
public class StaticCacheMap extends AbstractCacheMap{

	HashMap map;


	public StaticCacheMap(int cache) {
		map = new HashMap(cache);
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
		return -1;
	}

	public List keyList() {
		return new ArrayList(map.keySet());
	}
    
    public List valueList() {
    	return new ArrayList(map.values());
    }
}
