/*
 * Created on 2005-12-13
 *
 */
package net.joycool.wap.cache;

import java.lang.reflect.Field;
import java.util.*;

import net.joycool.wap.call.CallMethod;

/**
 * @author bomb
 *  
 */
public class CacheManage {
	//交友可信度
//	public static ICacheMap userBase = new LinkedCacheMap(1000, true);
	//花园
	public static ICacheMap userDish = new LinkedCacheMap(1000, true);
	public static ICacheMap flowerUser = new LinkedCacheMap(1000, true);
	//菜园
	public static ICacheMap gardenUserCache = new LinkedCacheMap(1000, true);
	public static ICacheMap noGardenCache = new LinkedCacheMap(2000, true);
	public static ICacheMap fieldStrealUser = new LinkedCacheMap(4000);
	public static ICacheMap fieldGrassUser = new LinkedCacheMap(8000);
	public static ICacheMap fieldBugUser = new LinkedCacheMap(8000);
	public static ICacheMap badTimeUser = new LinkedCacheMap(1000);
	
	//乐秀商城
//	public static ICacheMap shopItemIdCache = new LinkedCacheMap(20);
//	public static ICacheMap shopItemCache = new LinkedCacheMap(20);
	//public static ICacheMap lookRecordCache = new LinkedCacheMap(200, true);
	
	public static ICacheMap appUser = new LinkedCacheMap(1000, true);
	public static ICacheMap userSettingSeqCache = new LinkedCacheMap(2000);
	
	//英语单词问答
	public static ICacheMap wordCache = new LinkedCacheMap(2000);
	public static ICacheMap wordIDCache = new LinkedCacheMap(10);
	
	//好友动态缓存
	public static ICacheMap trend = new LinkedCacheMap(4000);	// 动态缓存
	public static ICacheMap friendTrend = new LinkedCacheMap(1000);	// 好友动态id列表缓存
	
	//buyfriend start
	
	public static ICacheMap buyFriendAction = new LinkedCacheMap(5000);
	public static ICacheMap buyFriendInfo = new LinkedCacheMap(5000);
	public static ICacheMap buyFriendMaster = new LinkedCacheMap(5000);
	public static ICacheMap buyFriendSlave = new LinkedCacheMap(5000);
	//buyfriend end
	
	// 城堡战争 start
	public static ICacheMap castleUser = new LinkedCacheMap(1000, true);
	public static ICacheMap castleTong = new LinkedCacheMap(500);
	public static ICacheMap castle = new LinkedCacheMap(4000);
	public static ICacheMap oasis = new LinkedCacheMap(2000);
	public static ICacheMap castleUserRes = new LinkedCacheMap(4000, true);
	public static ICacheMap castleSmithy = new LinkedCacheMap(2000);
	
	// 城堡战争 end
	
    public static ICacheMap farmField = new LinkedCacheMap(250);
    public static ICacheMap farmFeed = new LinkedCacheMap(250);
    public static ICacheMap userBag = new LinkedCacheMap(12000);
    public static ICacheMap userBagList = new LinkedCacheMap(4000, true);
    public static ICacheMap userBagMap = new LinkedCacheMap(1000, true);
    
    public static ICacheMap farmUser = new LinkedCacheMap(1000, true);
    public static ICacheMap farmUserCollect = new LinkedCacheMap(2000);
    public static ICacheMap farmUserCollectList = new LinkedCacheMap(1000, true);
    public static ICacheMap farmUserHonor = new LinkedCacheMap(2000);
    public static ICacheMap farmUserHonorList = new LinkedCacheMap(1000, true);
    public static ICacheMap farmTong = new LinkedCacheMap(20);
    public static ICacheMap farmTongUser = new LinkedCacheMap(200);
    
    public static ICacheMap forumVote = new LinkedCacheMap(100, true);
    public static ICacheMap forumUser = new LinkedCacheMap(3000);
    public static ICacheMap forumContent = new LinkedCacheMap(6000, true);
    public static ICacheMap forumReply = new LinkedCacheMap(15000, true);
    public static ICacheMap forumContents = new StaticCacheMap(1000);
    public static ICacheMap forumReplys = new LinkedCacheMap(4000, true);
    public static ICacheMap forum = new StaticCacheMap(1000);
    public static ICacheMap forumTop = new StaticCacheMap(1000);		// 置顶
    
    public static ICacheMap roomContent = new LinkedCacheMap(8000);
    public static ICacheMap notice = new LinkedCacheMap(15000);
    public static ICacheMap noticeList = new LinkedCacheMap(4000, true);
    public static ICacheMap userInfo = new LinkedCacheMap(8000, true);
    public static ICacheMap userStatus = new LinkedCacheMap(4000, true);
    public static ICacheMap userHonor = new LinkedCacheMap(2000);
    public static ICacheMap itemShow = new LinkedCacheMap(1000, true);
    public static ICacheMap userHat = new LinkedCacheMap(100);
    public static ICacheMap userPosition = new LinkedCacheMap(8000, true);
    
    public static ICacheMap tongUser = new LinkedCacheMap(2000, true);
    public static ICacheMap jyFriends = new LinkedCacheMap(2000);
    public static ICacheMap userFriend = new LinkedCacheMap(8000, true);
    public static ICacheMap badGuy = new LinkedCacheMap(8000, true);
    public static ICacheMap friendInfo = new LinkedCacheMap(2000, true);
    public static ICacheMap friendSearch = new TimeCacheMap(100, 15);
    public static ICacheMap chatList = new LinkedCacheMap(15000, true);
    public static ICacheMap chat2List = new LinkedCacheMap(4000, true);
    public static ICacheMap roomManager = new LinkedCacheMap(2000, true);
    
    public static ICacheMap roomOnline = new TimeCacheMap(1000, 60);	// 聊天室用户是否在线
    public static ICacheMap tongCityRecord = new TimeCacheMap(500, 15);
    
    public static ICacheMap userNote = new LinkedCacheMap(2000);
    public static ICacheMap userFriends = new LinkedCacheMap(4000, true);
    public static ICacheMap shortcut = new LinkedCacheMap(3000, true);
    public static ICacheMap image = new LinkedCacheMap(3000, true);
    public static ICacheMap ebook = new LinkedCacheMap(1000, true);
    public static ICacheMap hunt = new LinkedCacheMap(500, true);

    public static ICacheMap auction = new LinkedCacheMap(500, true);
    public static ICacheMap itemProto = new StaticCacheMap(500);		// for item prototype = dummy product
        
    public static ICacheMap tinyGame = new LinkedCacheMap(250);
    
    public static ICacheMap jspAdver = new StaticCacheMap(50);
    
    public static ICacheMap factoryProc = new LinkedCacheMap(1000);
    
    public static ICacheMap farmUserQuest = new LinkedCacheMap(2000);
    public static ICacheMap farmUserTrigger = new LinkedCacheMap(500);
    
    public static ICacheMap userInterval = new LinkedCacheMap(1000);
    public static ICacheMap stuff = new StaticCacheMap(8);
    
    public static ICacheMap intResult = new TimeCacheMap(250, 25);		// 缓存查询得到的整数
    public static ICacheMap intsResult = new TimeCacheMap(250, 25);
    
    public static ICacheMap rankAction = new StaticCacheMap(50);		// 动作
    public static ICacheMap column = new StaticCacheMap(125);			// 树型页面
    public static ICacheMap call = new StaticCacheMap(10);
    
    public static ICacheMap forbidGroup = new StaticCacheMap(32);
    public static ICacheMap questionSet = new LinkedCacheMap(100, true);
    public static ICacheMap chess = new LinkedCacheMap(200, true);
    public static ICacheMap chessUser = new LinkedCacheMap(200, true);
    public static ICacheMap pkg = new LinkedCacheMap(500, true);
    
    // 商城相关
    public static ICacheMap lookRecordCache = new LinkedCacheMap(100, true);
    
    public static CallMethod callMethod = new CallMethod();
    
    public static List getCacheList() {
    	return cacheList;
    }
    
    public static void flush(int index) {
    	try {
    		ICacheMap c = (ICacheMap)cacheList.get(index);
    		c.clear();
    	} catch(Exception e) {
    	}
    }
    
    
    public static String getCacheName(int index) {
    	Object obj = cacheInfoList.get(index);
    	if(obj instanceof String) {
    		return (String)obj;
    	} else if(obj instanceof Field) {
    		Field f = (Field)obj;
    		return f.getName();
    	}
    	return "ERROR";
    }
    
    public static String getCacheType(int index) {
    	try {
    		ICacheMap c = (ICacheMap)cacheList.get(index);
    		return c.getClass().getSimpleName();
    	} catch(Exception e) {
    		return "ERROR";
    	}
    }
    
    
    public static int time = 0;
    // 一定时间运行一次
    public static void timeCheck() {
    	time++;
    	for(int i = 0;i < cacheList.size();i++) {
    		try{
    			ICacheMap c = (ICacheMap)cacheList.get(i);
    			c.hourCheck(time);
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    // 清空所有缓存
    public static void clearAll() {
    	for(int i = 0;i < cacheList.size();i++) {
    		ICacheMap cache = (ICacheMap)cacheList.get(i);
    		cache.clear();
    	}
    }
    
    // 把cache添加到列表以便管理
    public static ICacheMap addCache(ICacheMap cache, String name) {
    	cacheList.add(cache);
    	cacheInfoList.add(name);
    	return cache;
    }
    
    
    static List cacheList = new ArrayList();
    static List cacheInfoList = new ArrayList();

    static {
    	Field[] fs = CacheManage.class.getFields();
    	for(int i = 0;i < fs.length;i++) {
    		Field f = fs[i];
    		if(ICacheMap.class.isAssignableFrom(f.getType())) {
    			try {
    				cacheList.add(f.get(null));
    				cacheInfoList.add(f);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    }
}
