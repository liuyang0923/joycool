package net.joycool.wap.cache;

import java.util.*;

public abstract class AbstractCacheMap implements ICacheMap{

	public void hourCheck(int hour) {

	}

	public List sgts(List keys) {
		List list = new ArrayList();
		Iterator iter = keys.iterator();
		while(iter.hasNext()) {
			Object obj = get(iter.next());
			if(obj != null)
				list.add(obj);
		}
		return list;
	}

	public void spts(List keys, List values) {
		if(keys.size() != values.size())
			return;
		Iterator iter = keys.iterator();
		Iterator iter2 = values.iterator();
		while(iter.hasNext()) {
			put(iter.next(), iter2.next());
		}
	}

	public void put(Object key, Object value, int life) {
		put(key, value);
	}

	public synchronized void spt(int key, Object value, int life) {
		put(Integer.valueOf(key), value, life);
	}

	public synchronized void spt(Object key, Object value, int life) {
		put(key, value, life);
	}

	public String toString() {
		return getClass().getSimpleName() + "-" + maxSize();
	}


}
