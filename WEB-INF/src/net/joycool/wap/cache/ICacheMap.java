/*
 * Created on 2006-8-7
 *
 */
package net.joycool.wap.cache;

import java.util.List;

/**
 * @author bomb
 *  长缓存，用于游戏
 */
public interface ICacheMap {
	
	public int size();
	public int maxSize();
	public void clear();
	
	public void spt(Object key, Object value, int life);	// 一定存活时间的缓存，单位是分钟
	public void spt(Object key, Object value);
	public Object sgt(Object key);
	public Object srm(Object key);
	
	public void put(Object key, Object value, int life);
	public void put(Object key, Object value);
	public Object get(Object key);
	public Object rm(Object key);
	
	public void spt(int key, Object value, int life);
	public void spt(int key, Object value);
	public Object sgt(int key);
	public Object srm(int key);
	
	public List sgts(List keys);	// 一次获得多个
	public void spts(List keys, List values);	// 一次写入多个
	
	public List keyList();
	public List valueList();
	
	public void hourCheck(int hour);
}
