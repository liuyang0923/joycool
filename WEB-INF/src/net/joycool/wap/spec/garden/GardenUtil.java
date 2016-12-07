package net.joycool.wap.spec.garden;

import java.util.*;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.util.RandomUtil;

public class GardenUtil {
	
	public static int defaultCount = 6;
	public static int defaultGold = 300;
	public static String GAME_NAME = "黄金农场";
	public static GardenUserBean NULL_Garden = new GardenUserBean(0);
	
	public static GardenService gardenService = GardenService.getInstance();
	
	public static int[] typeId = {0,1,2,3};
	
	public static String[] typeName = {"", "作物", "花卉","工具"};
	
	
	public static ICacheMap fieldStrealUser = CacheManage.fieldStrealUser;
	
	public static ICacheMap fieldGrassUser = CacheManage.fieldGrassUser;
	public static ICacheMap fieldBugUser = CacheManage.fieldGrassUser;
	
	public static ICacheMap badTimeUser = CacheManage.badTimeUser;
	
	//public static HashMap newMsg = new HashMap();
	
	public static int DAY_SEC = 24 * 60 * 60;
	
	public static ICacheMap gardenUserCache = CacheManage.gardenUserCache;
	public static GardenUserBean getUserBean(int uid) {
		GardenUserBean bean = null;
		synchronized(gardenUserCache) {
			bean = (GardenUserBean)gardenUserCache.get(new Integer(uid));
			if(bean == null) {
				bean = gardenService.getGardenUser(uid);
				if(bean != null) {
					bean.setTodayExp(GardenAction.ONE_DAY_EXP);
					bean.setBugCount(GardenAction.BUG_COUNT);
					bean.setGrassCount(GardenAction.GRASS_COUNT);
					gardenUserCache.put(new Integer(uid), bean);
				}
			}
			
		}
		return bean;
	}
	
	public static boolean updateExp(int uid, int exp) {
		synchronized(gardenUserCache) {
			GardenUserBean bean = getUserBean(uid);
			gardenService.updateGardenUser("exp = exp+"+exp, "uid="+uid);
			bean.setExp(bean.getExp()+exp);
		}
		return true;
	}
	
	//一季度的时间
	public static int QUARTER_SEC = 4 * 3600;
	
	public static int[] needExp;
	public static int MAX_LEVEL = 200;
	static {
		needExp = new int[MAX_LEVEL + 1];
		for(int i = 1;i <= MAX_LEVEL;i++)
			needExp[i] = needExp[i - 1] + 200;
	}
	/*
	public static int[] needExp = {0,200,600,1200,2000,3000,4200,5600,7200,9000,11000,13200,15600,18200,21000
		,24000,27200,30600,34200,38000,42000,46200,50600,55200,60000,65000,70200,75600,81200,87000,93000
		,99200,105600,112200,119000,126000,133200,140600,148200,156000,164000,172200,180600,189200,198000
		,207000,216200,225600,235200,245000,255000};*/
	public static int[] levelExp = new int[MAX_LEVEL + 1];
	
	
	public static int[][] condition = {{10000,5},{20000,7},{30000,9},{50000,11},{70000,13},{90000,15},{120000,17},{150000,19},{180000,21},{230000,23},{300000,25},{500000,27}};
	
	public static int getLevel(int exp) {
		if(exp < needExp[1])
			return 0;
		if(levelExp[0] == 0) {
			levelExp[0] = 1;
			int exps = 0;
			for(int i = 1; i<MAX_LEVEL;i++){
				exps += needExp[i];
				levelExp[i] = exps;
			}
		}
		int i = 1;
		for(; i<MAX_LEVEL;i++){
			if(exp < levelExp[i])
				return i-1;
		}
		return MAX_LEVEL;
	}
	
	public static int getNeedExp(int exp) {
		return needExp[getLevel(exp)+1];
	}
	
	public static int getCurrentExp(int exp) {
		return exp - (levelExp[getLevel(exp)]==1?0:levelExp[getLevel(exp)]);
	}
	
	public static int getNowSec(){
		return (int)(System.currentTimeMillis() / 1000);
	}
	
	public static void addMsg(int uid, String content, int fromUid){
		GardenMessage msg = new GardenMessage();
		msg.setUid(uid);
		msg.setMessage(content);
		msg.setFromUid(fromUid);
		msg.setReaded(0);
		gardenService.addMessage(msg);
	}
	
	public static void main(String[] args){
		StringBuffer sbf = new StringBuffer();
		sbf.append("abc");
		System.out.println(sbf.reverse().toString());
	}
	
	
	
	
	public static ICacheMap latestUser = new LinkedCacheMap(50);
	public static void addLatestUser(int id) {
		latestUser.spt(new Integer(id), "");
	}
	public static List getLatestUserList() {
		return latestUser.keyList();
	}
	public static List getRandomUserList(int limit) {
		List list =  getLatestUserList();
		if(limit >= list.size())
			return list;
		else {
			int rnd = RandomUtil.nextInt(list.size() - limit);
			return list.subList(rnd, rnd + limit - 1);
		}
	}
}
