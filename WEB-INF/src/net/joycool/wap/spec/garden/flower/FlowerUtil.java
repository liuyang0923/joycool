package net.joycool.wap.spec.garden.flower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class FlowerUtil {
	
	public static int MAX_FIELD_COUNT = 4;
	public static int COMP_FAIL_TIME = 32400000;
	public static int COMP_FAIL_EXP = 10;
	public static int FLOWET_TASK_COUNT = 5;
	
	public static byte[] initLock = new byte[0];
	public static byte[] lock = new byte[0];
	
	public static FlowerService service = new FlowerService();
	public static HashMap flowerMap = null;				
	public static HashMap taskMap = null;				// 任务列表
	public static HashMap describeMap = new HashMap(); 	// 药品描述
	public static HashMap effectMap = new HashMap(); 	// 药品功能
	public static HashMap especialMap = null;			// 特殊物品列表
	public static List flowerList = null;
	public static List fieldTypeList = new ArrayList();
	public static List flowerTypeList = null;
	public static long statComposeTime = 0;
	public static long STAT_COMPOSE_INTERVAL = 1800 * 1000;
	public static String[] sortNames = {"", "普通", "精品", "珍贵", "稀有", "特殊"};
	
	static {
		fieldTypeList.add("null");
		fieldTypeList.add("雪山");
		fieldTypeList.add("山崖");
		fieldTypeList.add("平原");
		fieldTypeList.add("河畔");

		describeMap.put(new Integer(21), "一颗人形的古参,有很多长须.");
		describeMap.put(new Integer(22), "一粒金光闪闪的大药丸,发出淡淡的清香.");
		describeMap.put(new Integer(23), "生长在森林深处的一种罕见的灵芝.");
		describeMap.put(new Integer(24), "颜色鲜红如血的一种藻类植物.");

		effectMap.put(new Integer(21), "使用后能增加体力上限.");
		effectMap.put(new Integer(22), "使用后能增加攻击力上限.");
		effectMap.put(new Integer(23), "使用后能增加防御上限.");
		effectMap.put(new Integer(24), "使用后能增加体力上限.");
	}
	
	// 更新排行榜
	public static void statCompose() {
		long now = System.currentTimeMillis();
		if(now < statComposeTime)
			return;
		synchronized(initLock) {
			if(now < statComposeTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table flower_rank");
			db.executeUpdate("insert into flower_rank (user_id,exp) (select user_id,exp from flower_user order by exp desc,user_id desc)");
			db.release();
			statComposeTime = now + STAT_COMPOSE_INTERVAL;
			if(SqlUtil.isTest)
				COMP_FAIL_TIME /= 1000;
		}
	}
	
	//从缓存中取得一位用户
	public static ICacheMap flowerUserCache = CacheManage.flowerUser;
	public static FlowerUserBean getUserBean(int uid) {
		FlowerUserBean fub = null;
		synchronized(flowerUserCache) {
			fub = (FlowerUserBean)flowerUserCache.get(new Integer(uid));
			if(fub == null) {
				fub = service.getFieldType(uid);
				if(fub != null) {
					flowerUserCache.put(new Integer(uid), fub);
				}
			}
		}
		return fub;
	}
	
	//从缓存中取得一位用户的培养皿
	public static ICacheMap userDishCache = CacheManage.userDish;
	public static DishBean getUserDish(int uid) {
		DishBean dish = null;
		synchronized(userDishCache) {
			dish = (DishBean)userDishCache.get(new Integer(uid));
			if(dish == null) {
				dish = getUserDishFromDB(uid);
				if(dish != null) {
					userDishCache.put(new Integer(uid), dish);
				}
			}
		}
		return dish;
	}
	
	// 经验值操作
	public static boolean updateExp(int uid, int exp) {
		FlowerUserBean fub = getUserBean(uid);
		if(fub == null)
			return false;
		synchronized(fub) {
			if (exp > 0) {
				fub.setExp(fub.getExp() + exp);
				service.updateFlowerUser("set exp = " + fub.getExp() + " where user_id =" + uid);
			} else {
				fub.setUsedExp(fub.getUsedExp() - exp);
				service.updateFlowerUser("set used_exp=" + fub.getUsedExp() + " where user_id =" + uid);
				
			}
		}
		return true;
	}
	
	// 返回用户培养皿
	public static DishBean getUserDishFromDB(int uid) {
		DishBean dish = service.getCultureDish(uid);
		if (dish == null) {
			dish = service.createDish(uid);
		}
		return dish;
	}
	
	// 任务
	public static HashMap getTaskMap() {
		if (taskMap != null) {
			return taskMap;
		}
		synchronized (initLock) {
			if (taskMap != null) {
				return taskMap;
			}
			// 载入任务Map
			taskMap = service.getTaskes();
		}
		return taskMap;
	}
	
	// 取得某一任务信息
	public static FlowerTask getTaskInfo(int taskId){
		return (FlowerTask)taskMap.get(new Integer(taskId));
	}
	
	// 花属性
	public static HashMap getFlowerMap() {
		if (flowerMap != null) {
			return flowerMap;
		}
		synchronized (initLock) {
			if (flowerMap != null) {
				return flowerMap;
			}
			// 载入花类型数据
			flowerList = service.getFlowerList("1 order by id");
			flowerTypeList = new ArrayList(flowerList.size());
			flowerMap = new HashMap(flowerList.size());
			flowerTypeList.add("");
			for (int i = 0; i < flowerList.size(); i++) {
				FlowerBean f = (FlowerBean) flowerList.get(i);
				flowerMap.put(new Integer(f.getId()), f);
				flowerTypeList.add(f.getName());
			}
		}
		return flowerMap;
	}
	
	// 特殊物品
	public static HashMap getEspecialMap() {
		if (especialMap != null){
			return especialMap;
		}
		synchronized (initLock){
			if (especialMap != null){
				return especialMap;
			}
			// 载入特殊物品列表
			especialMap = service.getEspecialMap("1");
		}
		return especialMap;
	}

	// 重置花属性(后台调用)
	public static void resetFlowerMap() {
		flowerMap = null;
		flowerMap = getFlowerMap();
	}
	
	public static FlowerBean getFlower(int id) {
		return (FlowerBean) getFlowerMap().get(new Integer(id));
	}

	public static String getFlowerName(int id) {
		return getFlower(id).getName();
	}

	public static EspecialBean getEspecial(int id){
		return (EspecialBean)getEspecialMap().get(new Integer(id));
	}
	
	public static HashMap getDescribeMap() {
		return describeMap;
	}

	public static void setDescribeMap(HashMap describeMap) {
		FlowerUtil.describeMap = describeMap;
	}

	public static HashMap getEffectMap() {
		return effectMap;
	}

	public static void setEffectMap(HashMap effectMap) {
		FlowerUtil.effectMap = effectMap;
	}
	
	public static String getSortName(int i) {
		return sortNames[i];
	}

	public static String[] getSortNames() {
		return sortNames;
	}

	public void setSortNames(String[] sortNames) {
		FlowerUtil.sortNames = sortNames;
	}

	public static List getFieldTypeList() {
		return fieldTypeList;
	}

	public static List getFlowerTypeList() {
		return flowerTypeList;
	}

	public static void setEspecialMap(HashMap especialMap) {
		FlowerUtil.especialMap = especialMap;
	}
}