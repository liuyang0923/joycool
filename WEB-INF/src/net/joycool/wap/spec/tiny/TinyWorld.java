package net.joycool.wap.spec.tiny;

import java.util.*;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.util.SimpleGameLog;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏
 * @datetime:1007-10-24
 */
public class TinyWorld {
	static ICacheMap tinyGameCache = CacheManage.tinyGame;
	
	public static TinyWorld one = new TinyWorld();
	
	public boolean loaded = false;
	
	public static HashMap userMap = new HashMap();		// 用户map
	public SimpleGameLog log = new SimpleGameLog();
	
	public List cropList;
	public HashMap cropMap;
	
	byte[] lock = new byte[0];
	public void prepare() {
		if(!loaded) {
			synchronized(lock) {
				if(!loaded) {
					load();
					loaded = true;
				}
			}
		}
	}
	
	public void load() {
	}
	
	public TinyGame getGame(int userId) {
		return (TinyGame)tinyGameCache.sgt(userId);
	}
	
	public void setGame(int userId, TinyGame game) {
		tinyGameCache.spt(userId, game);
	}
}
