package net.joycool.wap.spec.tiny;

import java.util.HashMap;

import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏
 * @datetime:1007-10-24
 */
public class Tiny2World {
	static ICacheMap tiny2GameCache = new LinkedCacheMap(1000);
	
	public static Tiny2World one = new Tiny2World();
	
	public static HashMap userMap = new HashMap();		// 用户map

	
	public Tiny2Game getGame(int userId) {
		return (Tiny2Game)tiny2GameCache.sgt(userId);
	}
	
	public void setGame(int userId, Tiny2Game game) {
		tiny2GameCache.spt(userId, game);
	}
}
