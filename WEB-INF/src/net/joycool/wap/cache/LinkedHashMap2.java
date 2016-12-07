/*
 * Created on 2006-8-7
 *
 */
package net.joycool.wap.cache;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * @author bomb
 *  长缓存，用于游戏
 */
public class LinkedHashMap2 extends LinkedHashMap{

	int maxCache;

	protected boolean removeEldestEntry(Entry e) {
		return size() > maxCache;
	}

	public LinkedHashMap2(int init, int max) {
		super(init);
		maxCache = max;
	}
	
	public LinkedHashMap2(int init, int max, boolean accessOrder) {
		super(init, 0.75f, accessOrder);
		maxCache = max;
	}

    
}
