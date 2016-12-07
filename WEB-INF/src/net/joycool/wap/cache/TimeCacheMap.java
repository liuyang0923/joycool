/*
 * Created on 2006-8-7
 *
 */
package net.joycool.wap.cache;

import java.util.*;

/**
 * @author bomb
 *  定时过期的缓存
 */
public class TimeCacheMap extends AbstractCacheMap{

	static class Entry {
		long life;
		Object value;
		public Entry(Object value, long life) {
			this.life = life + System.currentTimeMillis();
			this.value = value;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public void setLife(long life) {
			this.life = life;
		}
		public long getLife() {
			return life;
		}
		
	}
	
	HashMap map;
	long period;	// 存活时间，单位是毫秒
	int scan;		// 定期检查，单位是分钟

	public TimeCacheMap(int cache, int period) {
		map = new HashMap(cache);
		this.period = period * 60000;
		scan = period;
	}
	public TimeCacheMap(int cache, int period, int scan) {
		map = new HashMap(cache);
		this.period = period * 60000;
		this.scan = scan;
	}

	public void hourCheck(int hour) {
		if(hour % scan == 0) {	// 一定时间清空一次数据
			synchronized(this) {
				long now = System.currentTimeMillis();
				Iterator iter = map.values().iterator();
				while(iter.hasNext()) {
					Entry e = (Entry)iter.next();
					if(e.getLife() < now)
						iter.remove();
				}
			}
		}
	}

	public int size() {
		return map.size();
	}
	
	public void clear() {
		map.clear();
	}
	
	public void put(Object key, Object value) {
		putValue(key, value);
	}

	public Object get(Object key) {
		return getValue(key);
	}
	
	public Object rm(Object key) {
		return map.remove(key);
	}

	public synchronized void spt(Object key, Object value) {
		putValue(key, value);
	}

	public synchronized Object sgt(Object key) {
		return getValue(key);
	}

	public synchronized void spt(int key, Object value) {
		putValue(Integer.valueOf(key), value);
	}

	public synchronized Object sgt(int key) {
		return getValue(Integer.valueOf(key));
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

	public synchronized List keyList() {
		return new ArrayList(map.keySet());
	}
	
	public Object getValue(Object key) {
		Entry e = (Entry)map.get(key);
		if(e == null || e.getLife() < System.currentTimeMillis())
			return null;
		return e.getValue();
	}
	
    public void putValue(Object key, Object value) {
    	map.put(key, new Entry(value, period));
    }
    
	public void put(Object key, Object value, int life) {
		map.put(key, new Entry(value, life * 60000));
	}
	public synchronized void spt(int key, Object value, int life) {
		map.put(Integer.valueOf(key), new Entry(value, life * 60000));
	}
	public synchronized void spt(Object key, Object value, int life) {
		map.put(key, new Entry(value, life * 60000));
	}
    public List valueList() {
    	return new ArrayList(map.values());
    }
}
