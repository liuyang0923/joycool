package net.joycool.wap.spec.castle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class CastleUtil {

	static CastleService castleService = CastleService.getInstance();
	static CacheService cacheService = CacheService.getInstance();
	
	public static int mapSize = 801;	// 坐标0 - 400
	public static int mapHalfSize = mapSize / 2;
	public static int mapCenter = 200;
	public static int PROTECT_DAY = 14;// 保护期，天数
	public static int[][] map = null;
	public static byte[][] mapType = null;		// 每个格子的类型
	// 拆分地图，每个方环内的城堡数量保存在这里
	public static int[][] sideCount = new int[5][];	// 1-4有效，0不使用
	
	public static int[] fieldXs = {0, 0, 0, 400, 400};
	public static int[] fieldYs = {0, 0, 400, 0, 400};
	public static int[] fullSides;
	
	public static int count = 0;
	public static int stage;		// 当前阶段，例如奇迹开始，宝物开放等等
	public static String name = "城堡战争";	// 本区名称
	
	public static int trainingSpeedup = 1;
	public static int buildingSpeedup = 1;
	public static int movingSpeedup = 1;
	public static int productSpeedup = 1;
	
	public static int[][] getMap() {
		if(map != null)
			return map;
		synchronized(CastleUtil.class) {
			if(map != null)
				return map;
			
			map = loadAllCastle();
		}
		return map;
	}
	public static byte[][] getMapType() {
		if(mapType != null)
			return mapType;
		synchronized(CastleUtil.class) {
			if(mapType != null)
				return mapType;
			
			mapType = loadAllMapType();
		}
		return mapType;
	}
	
	// 各种地图类型出现的几率
	public static byte[] typeRate = {
		1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,
		2,2,2,2,2,2,2,2,
		3,3,3,3,3,3,3,3,
		4,4,4,4,4,4,4,4,
		5,
		6,
		7,7,7,
		8,8,8,
		9,9,9,
		10,10,10,10,10,10,10,10,
		11,11,11,11,11,11,11,11,
		12,12,12,12,12,12,12,12,
		17,18,19,20,21,22,23,24,
		17,18,19,23
	};
	
	public static byte getMapType(int x, int y) {
		byte[][] map = getMapType();
		byte ret = map[x][y];
		if(ret == 0) {
			 ret = typeRate[RandomUtil.nextInt(typeRate.length)];
			 SqlUtil.executeUpdate("insert into castle_map_type set pos=" + xy2Pos(x, y) + ",type=" + ret, 5);
			 map[x][y] = ret;
		}
		return ret;
	}
	// 判断坐标是否正确
	public static boolean isInMap(int x, int y) {
		return x>=0 && y>=0 && x < mapSize && y < mapSize;
	}
	
	public static int getMapCastleId(int x, int y) {
		return getMap()[x][y];
	}
	
	public static String produceMap() {
		
		StringBuilder sb = new StringBuilder();
		
		/*
		 * 产生0~7的8个数字，其中0代表野田,1代表野矿,2代表野木
		 * */
		for(int i = 0; i < 8; i++) {
			
			sb.append((int)(Math.random() * 8));
			if(i != 7)
				sb.append(",");
		}
		return sb.toString();
	}
	
	public static String[] mapTypeStr = {"树林","田野","矿藏","草地","山脉","泥坑","河流","荒野","沙漠"};
	
	//城堡周围的其他东西
	public static String getMapType(String type) {
		int i = StringUtil.toInt(type);
		if(i < 0 || i > 8)
			return "树林";
		return mapTypeStr[i];
	}
	
	public static boolean[] hrefLink = {true,true,true,false,true,false,false,false,false};
	
	public static boolean isHrefLink(String type) {
		int i = StringUtil.toInt(type);
		if(i < 0 || i > 8)
			return false;
		return hrefLink[i];
	}
	
	// 随机分配一个坐标，从中心开始扩散，尽量平均，field表示象限，分别是1 2 3 4
	public static boolean randomCastle(CastleBean bean, int field) {
		int[][] map = getMap();
		int[] ct = sideCount[field];
		int n = RandomUtil.nextInt(4);	// 第几圈？（例如是2表示从内向外可用的第三圈）
		int fieldX = fieldXs[field];
		int fieldY = fieldYs[field];
		synchronized(map) {
			int i = 1;
			for(;i <= mapCenter;i++) {
				if(ct[i] < i + (i >> 1)) {	// 格子数的1/6
					if(n <= 0) 
						break;
					n--;
				}
			}
			if(i > mapCenter)	// 地图满
				return false;
			n = i;	// 选择n这一圈
			for(i = 0;i < 10;i++) {	// 尝试10次
				int side = RandomUtil.nextInt(n + n);
				int s = RandomUtil.nextInt(4);
				int x, y;
				switch(s) {
				case 0: {
					x = (mapCenter - n + side);
					y = (mapCenter - n);
				} break;
				case 1: {
					x = (mapCenter - n + side + 1);
					y = (mapCenter + n);
				} break;
				case 2: {
					x = (mapCenter - n);
					y = (mapCenter - n + side + 1);
				} break;
				default: {
					x = (mapCenter + n);
					y = (mapCenter - n + side);
				} break;
				}
				x += fieldX;
				y += fieldY;
				if(map[x][y] == 0 && getMapType(x, y) <= 16) {
					bean.setX(x);
					bean.setY(y);
					ct[n]++;
					return true;
				}
			}
		}
		return false;
	}
	
	public static byte[] createCastleLock = new byte[0];
	
	/**
	 * 随机产生坐标，从中心向四周扩散，每次取5以内，5以内的满了则再增加5
	 * 		*
	 * 		*
	 * 		*
	 * 		*
	 * 		*
	 * ************
	 * 		*
	 * 		*
	 * 		*
	 * 		*
	 */
	public static CastleBean produceCastle(int uid, int race, String userName, int field) {
		CastleBean bean = new CastleBean();
		bean.setUid(uid);
		bean.setRace(race);
		bean.setType(1);
		bean.setCastleName("新的城堡");
		synchronized(createCastleLock) {
			if(randomCastle(bean, field)) {
				bean.setMap(produceMap());
				castleService.addCastle(bean);
				CastleUtil.getMap()[bean.getX()][bean.getY()] = bean.getId();
				return bean;
			}
		}
		return null;
		
	}
	public static void deleteCastle(int x, int y) {
		getMap()[x][y] = 0;
		decSide(x, y);
	}
	// 载入配置数据castle_config
	public static void castleInit(){
		DbOperation db = new DbOperation(5);
		String query = "select * from castle_config limit 1";
		try{
			ResultSet rs = db.executeQuery(query);
			
			if(rs.next()) {
				fullSides = new int[5];
				fullSides[1] = rs.getInt("side1");
				fullSides[2] = rs.getInt("side2");
				fullSides[3] = rs.getInt("side3");
				fullSides[4] = rs.getInt("side4");
				ResNeed.maxCastleCount = rs.getInt("max_castle");
				stage = rs.getInt("stage");
				PROTECT_DAY = rs.getInt("protect_day");
				name = rs.getString("name");
			}
			
		}catch(SQLException e) {
			System.out.println("载入城堡配置数据出错,检查数据库中的castle_config是否正确");
			e.printStackTrace();
		}finally{
			db.release();
		}
	}
	
	public static void incSide(int x, int y) {
		updateSide(x, y, 1);
	}
	public static void decSide(int x, int y) {
		updateSide(x, y, -1);
	}
	public static void updateSide(int x, int y, int add) {
		int field;
		if(x < 400) {
			if(y < 400) {
				field = 1;
				x = Math.abs(x - mapCenter);
				y = Math.abs(y - mapCenter);
			} else {
				field = 2;
				x = Math.abs(x - mapCenter);
				y = Math.abs(y - mapCenter - 400);
			}
		} else {
			if(y < 400) {
				field = 3;
				x = Math.abs(x - mapCenter - 400);
				y = Math.abs(y - mapCenter);
			} else {
				field = 4;
				x = Math.abs(x - mapCenter - 400);
				y = Math.abs(y - mapCenter - 400);
			}
		}
		if(x >= mapCenter || y >= mapCenter)
			return;
		sideCount[field][Math.max(x, y)] += add;
	}
	
	public static int[][] loadAllCastle(){
		int[][] map = new int[mapSize][mapSize];
		sideCount[1] = new int[mapCenter];
		sideCount[2] = new int[mapCenter];
		sideCount[3] = new int[mapCenter];
		sideCount[4] = new int[mapCenter];
		
		List list = castleService.getAllCacheCastle();
		count = list.size();
		Iterator iterator = list.iterator();
		while(iterator.hasNext()) {
			CastleBean bean = (CastleBean)iterator.next();
			int x = bean.getX();
			int y = bean.getY();
			map[x][y] = bean.getId();
			incSide(x, y);
		}
		return map;
	}
	public static byte[][] loadAllMapType(){
		byte[][] map = new byte[mapSize][mapSize];
		
		List list = SqlUtil.getIntsList("select pos,type from castle_map_type", 5);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()) {
			int[] bean = (int[])iterator.next();
			int x = pos2X(bean[0]);
			int y = pos2Y(bean[0]);
			if(x > 800 || y > 800) {
				int b = 1;
			}
			map[x][y] = (byte)bean[1];
		}
		return map;
	}
	
	public static AttackThread thread1 = new AttackThread();
	public static BuildingThread thread2 = new BuildingThread();
	public static SmithyThread thread3 = new SmithyThread();
	public static SoldierThread thread4 = new SoldierThread();
	public static void startThread() {
		castleInit();
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		
		thread1.setName("castle-AttackThread");
		thread2.setName("castle-BuildingThread");
		thread3.setName("castle-SmithyThread");
		thread4.setName("castle-SoldierThread");
	}
	public static void endThread() {
		thread1.interrupt();
		thread2.interrupt();
		thread3.interrupt();
		thread4.interrupt();
	}
	
	public static UserResBean getUserResBeanById(int id) {
		UserResBean userResBean = null;
		Integer key = new Integer(id);
		synchronized(CacheManage.castleUserRes) {
			userResBean = (UserResBean)CacheManage.castleUserRes.get(key);
			if(userResBean == null) {
				userResBean = castleService.getUserRes(id);
				CacheManage.castleUserRes.put(key, userResBean);
			}
		}
		return userResBean;
	}
	public static CastleBean zeroCastle = new CastleBean();	// 自然界
	public static OasisBean zeroOasis = new OasisBean();	// 未生成的绿洲
	static {
		zeroCastle.setCastleName("自然界");
		zeroCastle.setRace(4);
		zeroOasis.setId(-1);
	}
	
	public static CastleBean getCastleByUid(int uid) {
		if(uid <= 0)
			return zeroCastle;


		CastleBean castleBean = castleService.getCastle("uid=" + uid);

		return castleBean;
	}
	public static CastleBean getCastleById(int id) {
		if(id <= 0)
			return zeroCastle;
		CastleBean castleBean = null;
		Integer key = new Integer(id);
		synchronized(CacheManage.castle) {
			castleBean = (CastleBean)CacheManage.castle.get(key);
			if(castleBean == null) {
				castleBean = castleService.getCastle("id=" + id);
				CacheManage.castle.put(key, castleBean);
			}
		}
		return castleBean;
	}
	// 对于绿洲，id = pos
	public static OasisBean getOasisById(int id) {
		OasisBean bean = null;
		Integer key = new Integer(id);
		synchronized(CacheManage.castle) {
			bean = (OasisBean)CacheManage.oasis.get(key);
			if(bean == null) {
				bean = castleService.getOasis("id=" + id);
				if(bean == null)
					bean = zeroOasis;
				CacheManage.oasis.put(key, bean);
			}
		}
		return bean;
	}
	public static OasisBean getOasisByXY(int x, int y) {
		if(x < 0 || x >= mapSize || y < 0 || y >= mapSize)
			return null;
		return getOasisById(xy2Pos(x, y));
	}
	
	public static CastleUserBean getCastleUser(int uid) {
		Integer key = new Integer(uid);
		CastleUserBean bean = null;
		synchronized(CacheManage.castleUser) {
			bean = (CastleUserBean)CacheManage.castleUser.get(key);
			if(bean == null) {
				bean = castleService.getCastleUser(uid);
				CacheManage.castleUser.put(key, bean);
			}
		}
		return bean;
	}
	
	public static CastleUserBean getCastleUserCache(int uid) {
		Integer key = new Integer(uid);
		return (CastleUserBean)CacheManage.castleUser.sgt(key);
	}
	
	public static void decreaseUserRes(
			UserResBean userResBean, int wood, 
			int fe, int grain, int stone) {
		synchronized(userResBean) {
			userResBean.reCalc(System.currentTimeMillis());
			userResBean.setWood(userResBean.getWood() - wood);
			userResBean.setFe(userResBean.getFe() - fe);
			userResBean.setGrain(userResBean.getGrain() - grain);
			userResBean.setStone(userResBean.getStone() - stone);
			castleService.updateUserRes(userResBean);
		}
	}
	public static void decreaseOasisRes(
			OasisBean oasis, int wood, 
			int fe, int grain, int stone) {
		synchronized(oasis) {
			oasis.reCalc(System.currentTimeMillis());
			oasis.setWood(oasis.getWood() - wood);
			oasis.setFe(oasis.getFe() - fe);
			oasis.setGrain(oasis.getGrain() - grain);
			oasis.setStone(oasis.getStone() - stone);
			castleService.updateOasisRes(oasis);
		}
	}
	// 减少资源的同时增加!!士兵!!人口消耗
	public static void decreaseUserRes(
			UserResBean userResBean, int wood, 
			int fe, int grain, int stone, int people2) {
		synchronized(userResBean) {
			userResBean.reCalc(System.currentTimeMillis());
			userResBean.setWood(userResBean.getWood() - wood);
			userResBean.setFe(userResBean.getFe() - fe);
			userResBean.setGrain(userResBean.getGrain() - grain);
			userResBean.setStone(userResBean.getStone() - stone);
			userResBean.addPeople2(people2);	// 减少资源但是人口增加
			castleService.updateUserResAll(userResBean);
		}
	}
	public static void decreaseUserRes(
			int cid, int wood, 
			int fe, int grain, int stone, int people2) {
		
		UserResBean user = getUserResBeanById(cid);
		if(user != null)
			decreaseUserRes(user, wood, fe, grain, stone, people2);
	}
	
	public static void increaseUserRes(
			UserResBean userResBean, int wood, 
			int fe, int grain, int stone) {
		userResBean.increaseRes(wood, stone, fe, grain);
	}
	// 增加文明增长速度（建筑），同时更新总人口
	public static void addUserCivil(int uid, int add, int people) {
		CastleUserBean user = CastleUtil.getCastleUser(uid);
		if(user != null) {
			addUserCivil(user, add, people);
		}
	}
	public static void addUserCivil(CastleUserBean user, int add, int people) {
		long now = System.currentTimeMillis();
		user.reCalc(now);
		user.addCivilSpeed(add);
		user.addPeople(people);
		castleService.updateUserCivil(user);
	}

	public static CastleArmyBean getCastleArmy(int cid) {
		CastleArmyBean army = castleService.getCastleArmy(cid);
		if(army == null) {
			army = new CastleArmyBean();
			army.setCid(cid);
			army.setAt(cid);
			if(!castleService.addCastleArmy(army))
				return null;
		}
		return army;
	}

	public static CastleArmyBean getCastleArmy(int cid, int at) {
		CastleArmyBean army = castleService.getCastleArmy(cid, at);
		if(army == null) {
			army = new CastleArmyBean();
			army.setCid(cid);
			army.setAt(at);
			if(!castleService.addCastleArmy(army))
				return null;
		}
		return army;
	}
	
	public static CastleArmyBean getOasisArmy(int cid, int at, int atCid) {
		CastleArmyBean army = castleService.getOasisArmy(cid, at);
		if(army == null) {
			army = new CastleArmyBean();
			army.setCid(cid);
			army.setAt(at);
			if(!castleService.addOasisArmy(army, atCid))
				return null;
		}
		return army;
	}
	
	public static CastleArmyBean getCastleHiddenArmy(int cid) {
		CastleArmyBean army = castleService.getCastleHiddenArmy(cid);
		if(army == null) {
			army = new CastleArmyBean();
			army.setCid(cid);
			army.setAt(cid);
			if(!castleService.addHiddenSoldier(army))
				return null;
		}
		return army;
	}
	
	public static ICacheMap castleTong = CacheManage.castleTong;
	
	public static TongBean getTong(int id) {
		TongBean tong = null;
		synchronized(castleTong) {
			tong = (TongBean)castleTong.get(new Integer(id));
			if(tong == null) {
				tong = castleService.getTong(id);
				if(tong != null) {
					castleTong.put(new Integer(id), tong);
				}
			}
		}
		return tong;
	}
	
	//更新SP账号
	public static void updateSPAccount(CastleUserBean userBean, int gold, int day) {
		synchronized(userBean) {
			if(gold > 0) {
				userBean.setGold(userBean.getGold() - gold);
				if(userBean.getSpTime() > System.currentTimeMillis()) {
					userBean.setSpTime(userBean.getSpTime() + DateUtil.MS_IN_DAY * day);
					SqlUtil.executeUpdate("update castle_user set gold = gold - " + gold + ", sp_time = date_add(sp_time, interval " + day + " day) where uid = " + userBean.getUid(), 5);
				} else {
					userBean.setSpTime(System.currentTimeMillis() + DateUtil.MS_IN_DAY * day);
					SqlUtil.executeUpdate("update castle_user set gold = gold - " + gold + ", sp_time = date_add(now(), interval " + day + " day) where uid = " + userBean.getUid(), 5);
				}
			} else {
				if(userBean.getSpTime() > System.currentTimeMillis()) {
					userBean.setSpTime(userBean.getSpTime() + DateUtil.MS_IN_DAY * day);
					SqlUtil.executeUpdate("update castle_user set sp_time = date_add(sp_time, interval " + day + " day) where uid = " + userBean.getUid(), 5);
				} else {
					userBean.setSpTime(System.currentTimeMillis() + DateUtil.MS_IN_DAY * day);
					SqlUtil.executeUpdate("update castle_user set sp_time = date_add(now(), interval " + day + " day) where uid = " + userBean.getUid(), 5);
				}
			}
		}
	}
	
	public static void updateGold(CastleUserBean userBean, int gold) {
		synchronized(userBean) {
			userBean.setGold(userBean.getGold() - gold);
			SqlUtil.executeUpdate("update castle_user set gold = gold - " + gold + " where uid = " + userBean.getUid(), 5);
		}
	}
	
	public static void updateUserResFlag(UserResBean userResBean, int flag){
//		synchronized(userResBean) {
			userResBean.reCalc(System.currentTimeMillis());
			userResBean.addFlag(flag);
			castleService.updateUserResFlag(userResBean);
//		}
	}
	
	// 根据人口排名，生成数据库
	public static int[] lockStat = new int[0];
	public static long statPeopleTime = 0;
	public static long stat2PeopleTime = 0;
	public static long STAT_PEOPLE_INTERVAL = 300 * 1000;
	public static void statPeople() {
		long now = System.currentTimeMillis();
		if(now < statPeopleTime)
			return;
		synchronized(lockStat) {
			if(now < statPeopleTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_stat");
			db.executeUpdate("insert into castle_stat (uid,people,name,castle_count) (select uid,people,name,castle_count from castle_user where race!=5 order by people desc)");
			db.release();
			statPeopleTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	// 联盟排名
	public static void stat2People() {
		long now = System.currentTimeMillis();
		if(now < stat2PeopleTime)
			return;
		synchronized(lockStat) {
			if(now < stat2PeopleTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_stat2");
			db.executeUpdate("insert into castle_stat2 (tong_id,people,name,count) (select id,people,name,count from castle_tong order by people desc)");
			db.release();
			stat2PeopleTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	//攻击排名
	public static long stat3AttackTime = 0;
	public static void stat3Attack() {
		long now = System.currentTimeMillis();
		if(now < stat3AttackTime)
			return;
		synchronized(lockStat) {
			if(now < stat3AttackTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_stat3");
			db.executeUpdate("insert into castle_stat3 (uid,name,attack_total) (select uid,name,attack_total from castle_user where attack_total > 0 and race!=5 order by attack_total desc)");
			db.release();
			stat3AttackTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	//防御排名
	public static long stat4AttackTime = 0;
	public static void stat4Defense() {
		long now = System.currentTimeMillis();
		if(now < stat4AttackTime)
			return;
		synchronized(lockStat) {
			if(now < stat4AttackTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_stat4");
			db.executeUpdate("insert into castle_stat4 (uid,name,defense_total) (select uid,name,defense_total from castle_user where defense_total > 0 and race!=5 order by defense_total desc)");
			db.release();
			stat4AttackTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	//抢夺排名
	public static long stat5RobTime = 0;
	public static void stat5Rob() {
		long now = System.currentTimeMillis();
		if(now < stat5RobTime)
			return;
		synchronized(lockStat) {
			if(now < stat5RobTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_stat5");
			db.executeUpdate("insert into castle_stat5 (uid,name,rob_total) (select uid,name,rob_total from castle_user where rob_total > 0 and race!=5 order by rob_total desc)");
			db.release();
			stat5RobTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	// 英雄排名
	public static long stat6HeroTime = 0;
	public static void stat6Hero() {
		long now = System.currentTimeMillis();
		if(now < stat6HeroTime)
			return;
		synchronized(lockStat) {
			if(now < stat6HeroTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_stat6");
			db.executeUpdate("insert into castle_stat6 (uid,name,exp,type) (select uid,name,exp,type from castle_hero where exp > 0 and status=0 order by exp desc)");
			db.release();
			stat6HeroTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	// 每周排名 //
	
	
	//攻击排名
	public static long stat3AttackWTime = 0;
	public static void stat3AttackW() {
		long now = System.currentTimeMillis();
		if(now < stat3AttackWTime)
			return;
		synchronized(lockStat) {
			if(now < stat3AttackWTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_statw3");
			db.executeUpdate("insert into castle_statw3 (uid,name,attack_total) (select uid,name,attack_week from castle_user where attack_week > 0 and race!=5 order by attack_week desc)");
			db.release();
			stat3AttackWTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	//防御排名
	public static long stat4AttackWTime = 0;
	public static void stat4DefenseW() {
		long now = System.currentTimeMillis();
		if(now < stat4AttackWTime)
			return;
		synchronized(lockStat) {
			if(now < stat4AttackWTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_statw4");
			db.executeUpdate("insert into castle_statw4 (uid,name,defense_total) (select uid,name,defense_week from castle_user where defense_week > 0 and race!=5 order by defense_week desc)");
			db.release();
			stat4AttackWTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	//抢夺排名
	public static long stat5RobWTime = 0;
	public static void stat5RobW() {
		long now = System.currentTimeMillis();
		if(now < stat5RobWTime)
			return;
		synchronized(lockStat) {
			if(now < stat5RobWTime)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table castle_statw5");
			db.executeUpdate("insert into castle_statw5 (uid,name,rob_total) (select uid,name,rob_week from castle_user where rob_week > 0 and race!=5 order by rob_week desc)");
			db.release();
			stat5RobWTime = now + STAT_PEOPLE_INTERVAL;
		}
	}
	
	public static boolean isResEnough(UserResBean userResBean, BuildingTBean bt) {
		long now = System.currentTimeMillis();
		if(userResBean.getWood(now) < bt.getWood()) return false;
		if(userResBean.getFe(now) < bt.getFe()) return false;
		if(userResBean.getStone(now) < bt.getStone()) return false;
		if(userResBean.getGrain(now) < bt.getGrain()) return false;
		
		return true;
	}
	
	//返回过多久才能建造
	public static long getCanBuildTime(UserResBean userResBean, BuildingTBean bt) {
		
		long now = System.currentTimeMillis();
		long needTime = 0;
		long woodTime = userResBean.getWood(now) > bt.getWood() ? 0l : (long)(((float)(bt.getWood() - userResBean.getWood(now)) / userResBean.getWoodSpeed2()) * 3600000);
		long feTime = userResBean.getFe(now) > bt.getFe() ? 0l : (long)(((float)(bt.getFe() - userResBean.getFe(now)) / userResBean.getFeSpeed2()) * 3600000);
		long stoneTime = userResBean.getStone(now) > bt.getStone() ? 0l : (long)(((float)(bt.getStone() - userResBean.getStone(now)) / userResBean.getStoneSpeed2()) * 3600000);
		long grainTime = userResBean.getGrain(now) > bt.getGrain() ? 0l : (long)(((float)(bt.getGrain() - userResBean.getGrain(now)) / userResBean.getGrainSpeed2()) * 3600000);
		
		needTime = woodTime;
		if(feTime > needTime)
			needTime = feTime;
		if(stoneTime > needTime)
			needTime = stoneTime;
		if(grainTime > needTime)
			needTime = grainTime;		
		return needTime;
	}
	/*** cache building相关函数	*/
	// 有资源在建造中
	public static boolean containCacheBuilding1(List list) {
		return getCacheBuildingTime1(list) != 0;
	}
	public static long getCacheBuildingTime1(List list) {
		if(list.size() == 0)
			return 0;
		for(int i = 0;i < list.size();i++) {
			BuildingThreadBean cache = (BuildingThreadBean)list.get(i);
			if(cache.getType() < 4 || cache.getType() == 9)
				return cache.getEndTime();
		}
		return 0;
	}
	// 有建筑在建造中
	public static boolean containCacheBuilding2(List list) {
		return getCacheBuildingTime2(list) != 0;
	}
	public static long getCacheBuildingTime2(List list) {
		if(list.size() == 0)
			return 0;
		for(int i = 0;i < list.size();i++) {
			BuildingThreadBean cache = (BuildingThreadBean)list.get(i);
			if(cache.getType() >= 4 && cache.getType() != 9)
				return cache.getEndTime();
		}
		return 0;
	}
	// 有资源或者建筑在建造中
	public static boolean containCacheBuilding3(List list) {
		return getCacheBuildingTime3(list) != 0;
	}
	public static long getCacheBuildingTime3(List list) {
		if(list.size() == 0)
			return 0;
		for(int i = 0;i < list.size();i++) {
			BuildingThreadBean cache = (BuildingThreadBean)list.get(i);
			return cache.getEndTime();
		}
		return 0;
	}
	// 有建造队列
	public static boolean containCacheBuildingQueue(List list) {
		if(list.size() == 0)
			return false;
		long now = System.currentTimeMillis();
		for(int i = 0;i < list.size();i++) {
			BuildingThreadBean cache = (BuildingThreadBean)list.get(i);
			if(cache.getStartTime() > now)
				return true;
		}
		return false;
	}
	// 某位置有建造
	public static boolean containCacheBuildingPos(List list, int pos) {
		if(list.size() == 0)
			return false;
		long now = System.currentTimeMillis();
		for(int i = 0;i < list.size();i++) {
			BuildingThreadBean cache = (BuildingThreadBean)list.get(i);
			if(cache.getBuildPos() == pos)
				return true;
		}
		return false;
	}
	// 某类型有建造
	public static boolean containCacheBuildingType(List list, int type) {
		if(list.size() == 0)
			return false;
		long now = System.currentTimeMillis();
		for(int i = 0;i < list.size();i++) {
			BuildingThreadBean cache = (BuildingThreadBean)list.get(i);
			if(cache.getType() == type)
				return true;
		}
		return false;
	}
	// 根据list返回士兵数量的描述字符串
	public static String getSoldierString(int race, List soldiers, int hero) {
		SoldierResBean[] g = ResNeed.getSoldierTs(race);
		StringBuilder sb = new StringBuilder(64);
		
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0) {
				SoldierResBean s = g[i + 1];
				sb.append(s.getSoldierName());
				sb.append('(');
				sb.append(count);
				sb.append(')');
			}
		}
		if(hero != 0) {
			sb.append("指挥官(");
			sb.append(hero);
			sb.append(')');
		}
		return sb.toString();
	}
	public static void getSoldierString(int race, List soldiers, int hero, StringBuilder sb) {
		SoldierResBean[] g = ResNeed.getSoldierTs(race);
		
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0) {
				SoldierResBean s = g[i + 1];
				sb.append(s.getSoldierName());
				sb.append('(');
				sb.append(count);
				sb.append(')');
			}
		}
		if(hero != 0) {
			sb.append("指挥官(");
			sb.append(hero);
			sb.append(')');
		}
	}
	// 兵力-损失
	public static StringBuilder getSoldierLostString(int race, int[] count1, int[] count2, StringBuilder sb) {
		SoldierResBean[] g = ResNeed.getSoldierTs(race);
		boolean none = true;
		for(int i = 1;i < g.length;i++) {
			if(count1[i] > 0) {
				SoldierResBean s = g[i];
				sb.append(s.getSoldierName());
				sb.append(count1[i]);
				sb.append('-');
				sb.append(count2[i]);
				none = false;
			}	
		}
		if(count1[0] > 0) {
			sb.append("指挥官");
			sb.append(count1[0]);
			sb.append('-');
			sb.append(count2[0]);
			none = false;
		}	
		if(none)
			sb.append('无');
		return sb;
	}
	// 无兵力损失
	public static StringBuilder getSoldierNoLostString(int race, int[] count1, StringBuilder sb) {
		SoldierResBean[] g = ResNeed.getSoldierTs(race);
		boolean none = true;
		for(int i = 1;i < g.length;i++) {
			if(count1[i] > 0) {
				SoldierResBean s = g[i];
				sb.append(s.getSoldierName());
				sb.append(count1[i]);
				sb.append("-0");
				none = false;
			}	
		}
		if(none)
			sb.append('无');
		return sb;
	}
	public static StringBuilder getSoldierLostString(int race, List count1, int[] count2, StringBuilder sb) {
		SoldierResBean[] g = ResNeed.getSoldierTs(race);
		boolean none = true;
		for(int i = 1;i < g.length;i++) {
			int count = ((Integer)count1.get(i - 1)).intValue(); 
			if(count > 0) {
				SoldierResBean s = g[i];
				sb.append(s.getSoldierName());
				sb.append(count);
				sb.append('-');
				sb.append(count2[i]);
				none = false;
			}
		}
		if(none)
			sb.append('无');
		return sb;
	}
	
	public static boolean addMsg2(String str1,String str2,CastleBean castle, int uid) {
		CastleUserBean user = CastleUtil.getCastleUserCache(uid);
		if(user != null)
			user.addUnread();
		
		return addMsg(str1, str2, castle, uid);
	}
	protected static boolean addMsg(String str1,String str2,CastleBean castle, int uid) {
		StringBuilder receiveContent = new StringBuilder(str1);
		receiveContent.append(castle.getX());
		receiveContent.append("|");
		receiveContent.append(castle.getY());
		receiveContent.append(str2);
		CastleMessage msg = new CastleMessage(receiveContent.toString(), uid);
		return castleService.addCastleMessage(msg);
	}
	// 添加详细的战报
	public static void addDetailMsg(int uid, int type, String content, String detail) {
		CastleUserBean user = getCastleUser(uid);
		if(user == null)
			return;
		user.addUnread();
		addDetailMsg(user, type, content, detail, 0);
	}
	public static void addDetailMsg(int uid, int type, String content, String detail, int pos) {
		CastleUserBean user = getCastleUser(uid);
		if(user == null)
			return;
		user.addUnread();
		addDetailMsg(user, type, content, detail, pos);
	}
	public static void addDetailMsg(CastleUserBean user, int type, String content, String detail, int pos) {
		CastleMessage msg = new CastleMessage(content, user.getUid());
		msg.setTongId(user.getTong());
		msg.setType(type);
		msg.setDetail(detail);
		msg.setPos(pos);
		castleService.addCastleMessage(msg);
		if(type == 2 && user.getTong() != 0) {		// 添加到联盟战报
			msg.setTime(new Date());
			TongBean tong = getTong(user.getTong());
			tong.addReport(msg);
		}
	}
	// 玩家建立新城堡
	public static void addUserCastle(CastleUserBean user, int x, int y, int cid) {
		CastleBean castle = new CastleBean();
		castle.setUid(user.getUid());
		castle.setCastleName("新的城堡");
		castle.setMap("");
		castle.setX(x);
		castle.setY(y);
		castle.setType(getMapType(x, y));
		castle.setRace(user.getRace());
		castle.setCreateTime(System.currentTimeMillis());
		castleService.addCastle(castle);
		
		BuildingBean city = new BuildingBean(ResNeed.CITY_BUILD, 1, castle.getId(), 1,19);
		castleService.addBuilding(city);
		
		
		UserResBean userResBean = new UserResBean(user.getUid(), castle.getType2());
		userResBean.setId(castle.getId());		
		castleService.addUserRes(userResBean);
		CacheManage.castleUserRes.spt(new Integer(userResBean.getId()), userResBean);
		
		
		map[x][y] = castle.getId();
		incSide(x, y);
		
		CacheManage.castle.spt(new Integer(castle.getId()), castle);
		addUserCastle(user, castle);
		// 添加扩张记录
		CastleBean oldCastle = CastleUtil.getCastleById(cid);
		if(oldCastle != null) {
			oldCastle.expand++;
			castleService.addCastleExpand(oldCastle, castle);
		}
	}
	// 把一个城堡加为某个玩家的
	public static void addUserCastle(CastleUserBean user, CastleBean castle) {
		user.addCastle(castle);
		SqlUtil.executeUpdate("update castle_user set castle_count=" + user.getCastleCount()
				+ " where uid=" + user.getUid(), 5);
		// 加入新城堡的人口和文明度
		CastleUtil.addUserCivil(user, 2, 2);
	}
	
	// 更新联盟总人口
	public static void updateTongPeople(TongBean tong, int people) {
		tong.setPeople(people);
		SqlUtil.executeUpdate("update castle_tong set people =" + tong.getPeople() + " where id = " + tong.getId(), 5);
	}
	public static void addTongPeople(TongBean tong, int people) {
		tong.addPeople(people);
		SqlUtil.executeUpdate("update castle_tong set people =" + tong.getPeople() + " where id = " + tong.getId(), 5);
	}
	
	// 建筑下降到等级grade2
	public static void destroyBuilding(int uid, BuildingBean building, int grade2) {
		if(building == null)
			return;
		if(building.getGrade() == 0) {		// 正在建造，还没到1级……直接删除即可
			castleService.deleteBuilding(building);
			return;
		}
		UserResBean userResBean = CastleUtil.getUserResBeanById(building.getCid());

		int grade = building.getGrade();
		int type = building.getBuildType();
		BuildingTBean bt1 = null;	// 建筑拆毁后级别
				
		BuildingTBean bt2 = ResNeed.getBuildingT(type, grade);		// 建筑当前级别
		
		int addValue = -bt2.getValue();
		
		if(grade2 == 0) {
			castleService.deleteBuilding(building);
			userResBean.addCivil(-bt2.getCivil());
			CastleUtil.addUserCivil(uid, -bt2.getCivil(), -bt2.getTotalPeople());
			userResBean.setPeople(userResBean.getPeople() - bt2.getTotalPeople());		// 建筑拆除后影响人口
			
			if(building.getBuildType() == ResNeed.PALACE2_BUILD) {	// 皇宫要特殊处理
				CastleUserBean castleUser = CastleUtil.getCastleUser(uid);
				castleUser.deleteFlag(CastleUserBean.FLAG_PALACE);
				castleService.updateUserFlag(castleUser);
			}
		} else {
			building.setGrade(grade2);
			castleService.updateBuilding(building);
			bt1 = ResNeed.getBuildingT(type, grade2);
			
			addValue += bt1.getValue();
			userResBean.addCivil(-bt2.getCivil() + bt1.getCivil());
			CastleUtil.addUserCivil(uid, -bt2.getCivil() + bt1.getCivil(), - bt2.getTotalPeople() + bt1.getTotalPeople());
			userResBean.setPeople(userResBean.getPeople() - bt2.getTotalPeople() + bt1.getTotalPeople());		// 建筑拆除后影响人口
		}
		
		userResBean.dupdateBuilding(type, grade2);
		
		long now = System.currentTimeMillis();
		userResBean.reCalc(now);
		synchronized(CacheManage.castleUserRes) {
			switch(building.getBuildType()) {
				case ResNeed.CITY_BUILD:
					break;
				case ResNeed.WOOD_BUILD:
					if(grade2 == 0) addValue += ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setWoodSpeed(userResBean.getWoodSpeed() + addValue);
					break;
				case ResNeed.FE_BUILD:
					if(grade2 == 0) addValue += ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setFeSpeed(userResBean.getFeSpeed() + addValue);
					break;
				case ResNeed.GRAIN_BUILD:
					if(grade2 == 0) addValue += ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setGrainSpeed(userResBean.getGrainSpeed() + addValue);
					break;
				case ResNeed.STONE_BUILD:
					if(grade2 == 0) addValue += ResNeed.RES_BASE;	// 第一级扣除0级的产量
					userResBean.setStoneSpeed(userResBean.getStoneSpeed() + addValue);
					break;
				case ResNeed.WOOD_FACTORY_BUILD:
					userResBean.setOtherWoodSpeed(userResBean.getOtherWoodSpeed() + addValue);
					break;
				case ResNeed.STONE_FACTORY_BUILD:
					userResBean.setOtherStoneSpeed(userResBean.getOtherStoneSpeed() + addValue);
					break;
				case ResNeed.FOUNDRY_BUILD:
					userResBean.setOtherFeSpeed(userResBean.getOtherFeSpeed() + addValue);
					break;
				case ResNeed.MOFANG_BUILD:
				case ResNeed.BREAD_BUILD:
					userResBean.setOtherGrainSpeed(userResBean.getOtherGrainSpeed() + addValue);
					break;
				case ResNeed.STORAGE_BUILD:
				case ResNeed.STORAGE2_BUILD:
					userResBean.setMaxRes(userResBean.getMaxRes() + addValue);
					if(userResBean.getMaxRes() <= 0)
						userResBean.setMaxRes(800);		// 全部拆除后返回到初始的800
					break;
				case ResNeed.BARN_BUILD:
				case ResNeed.BARN2_BUILD:
					userResBean.setMaxGrain(userResBean.getMaxGrain() + addValue);
					if(userResBean.getMaxGrain() <= 0)
						userResBean.setMaxGrain(800);		// 全部拆除后返回到初始的800
					break;
				case ResNeed.CAVE_BUILD:
					userResBean.setCave(userResBean.getCave() + addValue);
					break;
				case ResNeed.WALL_BUILD:
				case ResNeed.WALL2_BUILD:
				case ResNeed.WALL3_BUILD:
					if(bt1 == null)
						userResBean.setWall(0);
					else
						userResBean.setWall(bt1.getValue());
					break;
				case ResNeed.TRAP_BUILD:
					int trap = grade2 * 10;
					if(userResBean.getTrap() > trap)
						userResBean.addTrapDB(trap - userResBean.getTrap());
					break;
				case ResNeed.PALACE_BUILD:
				case ResNeed.PALACE2_BUILD:
					if(userResBean.getLoyal() != UserResBean.MAX_LOYAL)	{	// 和升级建筑不同，如果当前值不是最大，就算是calc出来是满的一样要保存
						userResBean.reCalcLoyal(now);
						userResBean.setLoyalSpeed(grade2 * 10000);
						castleService.updateLoyal(userResBean);
					}		
					break;
				case ResNeed.WONDER_BUILD:
					SqlUtil.executeUpdate("update castle_ww set lvl=" + grade2 + " where cid=" + building.getCid(), 5);
					break;
				default:
					break;
			}
			
		}
		castleService.updateUserResAll(userResBean);
	}

	// 直接删除军队前，计算人口people2
	public static void removeArmyPeople(int race, CastleArmyBean army) {
		int people = army.getGrainCost(ResNeed.getSoldierTs(race));

		if(people != 0)
			decreaseUserRes(army.getAt(), 0, 0, 0, 0, -people);
	}
	public static void removeArmy2People(int race, CastleArmyBean army) {
		int people = army.getGrainCost(ResNeed.getSoldierTs(race));
		OasisBean oasis = getOasisById(army.getAt());
		if(people != 0 && oasis != null)
			decreaseUserRes(oasis.getCid(), 0, 0, 0, 0, -people);
	}
	
	//删除账号
	public static boolean delAccount(int uid) {

		CastleUserBean userBean = CastleUtil.getCastleUser(uid);
		if(userBean == null)
			return false;
		List list = castleService.getCastleList(uid);
		DbOperation db = new DbOperation(5);
		try {
			for(int i = 0; i < list.size() ; i++) {
				CastleBean castle = (CastleBean)list.get(i);
				int cid = castle.getId();
	
				// 删除所有军队（只是计算人口）
				List armyList = castleService.getCastleArmyList(cid);
				for(int j = 0;j < armyList.size();j++) {
					CastleArmyBean army = (CastleArmyBean)armyList.get(j);
					removeArmyPeople(userBean.getRace(), army);
					db.executeUpdate("delete from castle_soldier where id = " + army.getId());
				}
				// 删除所有支援出去到绿洲的军队
				armyList = castleService.getOasisArmyList(cid);
				for(int j = 0;j < armyList.size();j++) {
					CastleArmyBean army = (CastleArmyBean)armyList.get(j);
					removeArmy2People(userBean.getRace(), army);
					db.executeUpdate("delete from castle_soldier2 where id = " + army.getId());
				}
				armyList = castleService.getCastleArmyAtList(cid);
				for(int j = 0;j < armyList.size();j++) {
					CastleArmyBean army = (CastleArmyBean)armyList.get(j);
					if(army.getCid() == cid) continue;
					CastleBean c = getCastleById(army.getCid());
					if(c != null && c.getUid() != uid) {
						backArmy(army, c, castle);
						db.executeUpdate("delete from castle_soldier where id=" + army.getId());
					}
				}
				armyList = castleService.getOasisArmyAtCidList(cid);
				for(int j = 0;j < armyList.size();j++) {
					CastleArmyBean army = (CastleArmyBean)armyList.get(j);
					if(army.getCid() == cid) continue;
					CastleBean c = getCastleById(army.getCid());
					if(c != null && c.getUid() != uid) {
						backArmy(army, c, castle);
						db.executeUpdate("delete from castle_soldier2 where id=" + army.getId());
					}
				}
				
				
				CastleUtil.deleteCastle(castle.getX(), castle.getY());
	
				CacheManage.castle.srm(cid);
				db.executeUpdate("delete from castle where id = " + cid);
				db.executeUpdate("delete from castle_user_resource where id = " + cid);
				CacheManage.castleUserRes.srm(cid);
				
				db.executeUpdate("delete from castle_building where cid = " + cid);
				db.executeUpdate("delete from castle_hidden_soldier where cid = " + cid);
				//db.executeUpdate("delete from castle_soldier where cid = " + cid); // 除了在自己城堡的军队，其他在前面删除（需要计算人口）
				db.executeUpdate("delete from castle_soldier where at = " + cid);	// 删除所有支援的军队
	
				db.executeUpdate("delete from castle_soldier_smithy where cid = " + cid);
	
				db.executeUpdate("delete from cache_attack where cid = " + cid);
				db.executeUpdate("delete from cache_merchant where from_cid = " + cid);
				db.executeUpdate("delete from cache_building where cid = " + cid);
				db.executeUpdate("delete from cache_common where cid = " + cid);
				db.executeUpdate("delete from cache_soldier where cid = " + cid);
				db.executeUpdate("delete from cache_soldier_smithy where cid = " + cid);
				CacheManage.castleSmithy.srm(cid);
				
				db.executeUpdate("delete from castle_trade where cid = " + cid);
				db.executeUpdate("delete from castle_expand where from_cid = " + cid);
				// 删除占领的绿洲
				db.executeUpdate("delete from castle_soldier2 where at_cid = " + cid);
				List olist = castleService.getOasisList("cid="+cid);
				for(int j = 0;j < olist.size();j++) {
					OasisBean oasis = (OasisBean)olist.get(j);
					db.executeUpdate("update castle_oasis set cid=0,uid=0,update_time=now() where id=" + oasis.getId());
					CacheManage.oasis.srm(oasis.getId());
				}
			}
			
			db.executeUpdate("delete from castle_user where uid = " + uid);
			if(userBean.getTong() != 0) {
				TongBean tong = CastleUtil.getTong(userBean.getTong());
				if(tong != null && tong.getCount() > 0) {
					tong.setCount(tong.getCount() - 1);
					db.executeUpdate("update castle_tong set count = count - 1 where id = " + userBean.getTong());
				}
			}
			db.executeUpdate("delete from castle_hero where uid = " + uid);
			db.executeUpdate("delete from castle_tong_invite where from_uid = " + uid);
			db.executeUpdate("delete from castle_tong_invite where to_uid = " + uid);
			db.executeUpdate("delete from castle_tong_power where uid = " + uid);
			// castle_message不必删除
			CacheManage.castleUser.srm(uid);
		} catch(Exception e) {
			e.printStackTrace();
		}
		db.release();
		return true;
	}

	// 士兵开始饿死，g表示当前粮食负储量，past表示过去的小时数
	// 返回饿死之后剩余的粮食
	public static int starv(UserResBean bean, int left, float past) {
		List armyList = castleService.getCastleArmyAtList(bean.getId());
		for(int i = 0;i < armyList.size();i++) {
			CastleArmyBean army = (CastleArmyBean)armyList.get(i);
			if(army.getCid() == bean.getId())
				continue;
			left = starv(bean, army, left, past);
			if(army.isEmpty())
				castleService.deleteCastleArmyById(army.getId());	// 死光，记录删除
			else
				castleService.updateSoldierCount(army);
			if(left <= 0)
				break;
		}
		if(left > 0) {
			CastleArmyBean army = castleService.getCastleHiddenArmy(bean.getId());
			if(army != null) {
				left = starv(bean, army, left, past);
				castleService.updateHiddenSoldierCount(army);
			}
		}
		if(left > 0) {
			List armyList2 = castleService.getOasisArmyAtCidList(bean.getId());
			for(int i = 0;i < armyList2.size();i++) {
				CastleArmyBean army = (CastleArmyBean)armyList2.get(i);
				left = starv(bean, army, left, past);
				if(army.isEmpty())
					castleService.deleteOasisArmyById(army.getId());	// 死光，记录删除
				else
					castleService.updateOasisSoldierCount(army);
				if(left <= 0)
					break;
			}
		}
		if(left > 0) {
			for(int i = 0;i < armyList.size();i++) {
				CastleArmyBean army = (CastleArmyBean)armyList.get(i);
				if(army.getCid() != bean.getId())
					continue;
				left = starv(bean, army, left, past);
				castleService.updateSoldierCount(army);
				if(left <= 0)
					break;
			}
		}
		castleService.updateUserResAll(bean);
		if(left > 0)
			return 0;
		return left;
	}
	// 一批军队种饿死
	public static int starv(UserResBean bean, CastleArmyBean army, int left, float past) {
		SoldierResBean[] g = ResNeed.getSoldierTs(getCastleById(army.getCid()).getRace());
		int[] counts = army.getCount();
		int deadTotal = 0;	// 死亡的人口数
		for(int c = 1;c <= ResNeed.soldierTypeCount;c++) {
			if(counts[c] == 0)
				continue;
			float each = past * g[c].getPeople() + g[c].getGrain();	// 每个士兵可以换回的粮食
			
			int dead = (int)(left / each) + 1;
			if(dead >= counts[c])
				dead = counts[c];

			left -= dead * each;
			counts[c] -= dead;
			
			bean.addPeople2(-dead * g[c].getPeople());
			bean.setGrain(bean.getGrain() + dead * g[c].getGrain());
			if(left <= 0)
				return left;
		}

		return left;
	}
	// 计算距离
	public static float calcDistance(int dx, int dy) {
		if(dx < 0)
			dx = -dx;
		if(dy < 0)
			dy = -dy;
		if(dx > mapHalfSize)
			dx = mapSize - dx;
		if(dy > mapHalfSize)
			dy = mapSize - dy;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	// 计算是否在方块距离
	public static boolean inSquare(int dx, int dy, int r) {
		if(dx < 0)
			dx = -dx;
		if(dy < 0)
			dy = -dy;
		if(dx > mapHalfSize)
			dx = mapSize - dx;
		if(dy > mapHalfSize)
			dy = mapSize - dy;
		return dx <= r && dy <= r;
	}
	// 根据x y 计算pos
	public static int xy2Pos(int x, int y) {
		return (x << 10) + y;
	}
	public static int pos2X(int pos) {
		return pos >> 10;
	}
	public static int pos2Y(int pos) {
		return pos & 0x3ff;
	}
	// 把小于0或者大于800的坐标参数进行规范化
	public static int formatAxis(int a) {
		if (a < 0)
			return a + mapSize;
		else if (a >= mapSize)
			return a - mapSize;
		else
			return a;
	}
	// 判断坐标是否正确
	public static boolean isXY(int x, int y) {
		return x >= 0 && y >= 0 && x < mapSize && y < mapSize;
	}
	// 把castle设置为主城
	public static void setMain(CastleUserBean user, CastleBean castle) {
		if(user.getMain() == castle.getId())
			return;
		
		CastleBean old = CastleUtil.getCastleById(user.getMain());
		List list = castleService.getBuildingBeanList("cid=" + old.getId() + " and build_pos<=18");
		// 所有的资源田下降到10级
		for(int i = 0;i < list.size();i++) {
			BuildingBean b = (BuildingBean)list.get(i);
			if(b.getGrade() > 10)
				destroyBuilding(user.getUid(), b, 10);
		}
		BuildingBean b = castleService.getBuildingBean(ResNeed.STONE2_BUILD, old.getId());
		if(b != null)
			destroyBuilding(user.getUid(), b, 0);
		
		DbOperation db = new DbOperation(5);
		db.executeUpdate("delete from cache_building where cid=" + old.getId() + " and type=" + ResNeed.STONE2_BUILD);
		
		user.setMain(castle.getId());
		db.executeUpdate("update castle_user set main=" + user.getMain() + " where uid=" + user.getUid());
		
		db.executeUpdate("delete from cache_building where cid=" + castle.getId() + " and type=" + ResNeed.CASERN2_BUILD);
		db.executeUpdate("delete from cache_building where cid=" + castle.getId() + " and type=" + ResNeed.HEVO2_BUILD);
		db.release();
		
		b = castleService.getBuildingBean(ResNeed.CASERN2_BUILD, castle.getId());
		if(b != null)
			destroyBuilding(user.getUid(), b, 0);
		b = castleService.getBuildingBean(ResNeed.HEVO2_BUILD, castle.getId());
		if(b != null)
			destroyBuilding(user.getUid(), b, 0);
	}
	public static byte[] oasisLock = new byte[0];
	// 产生一个绿洲记录，当第一次攻击到达的时候产生
	public static OasisBean buildOasis(int x, int y, int type) {
		if(!isXY(x, y))
			return null;
		synchronized(oasisLock) {
			OasisBean oasis = CastleUtil.getOasisByXY(x, y);
			if(oasis.getCid() != 0)
				return oasis;
			oasis = new OasisBean();
			oasis.setId(xy2Pos(x, y));
			oasis.setType(type);
			long now = System.currentTimeMillis();
			oasis.setCreateTime(now);
			oasis.setUpdateTime(now);

			castleService.addOasis(oasis);
			CacheManage.oasis.spt(oasis.getId(), oasis);
			return oasis;
		}
	}
	public static long OASIS_UPDATE_TIME = 7 * 3600000l;
	// 根据绿洲情况，无主的会随机生长自然界生物
	public static void updateOasisArmy(OasisBean oasis, long now) {
		CastleArmyBean army = castleService.getOasisArmyAt(oasis.getId());
		if(army == null) {
			army = new CastleArmyBean();
			army.setCid(0);
			army.setAt(oasis.getId());
			
			updateOasisArmyCount(army, natureArmy[oasis.getType()]);
			
			castleService.addOasisArmyFull(army);
			return;
		}
		if(now - oasis.getUpdateTime() >= OASIS_UPDATE_TIME) {// 7小时更新一次
			
			updateOasisArmyCount(army, natureArmy[oasis.getType()]);
			
			castleService.updateOasisSoldierCount(army);
			oasis.setUpdateTime(now);
			SqlUtil.executeUpdate("update castle_oasis set update_time=now() where id=" + oasis.getId(), 5);
		}
	}
	public static void updateOasisArmyCount(CastleArmyBean army, int[] count) {
		int[] count2 = army.getCount();
		for(int i = 1;i < ResNeed.soldierTypeCount;i++) {
			if(count2[i] >= count[i])
				continue;
			int c = count[i] / 3;
			if(count2[i] < c) {
				count2[i] += c + (RandomUtil.nextInt(c / 5 == 0 ? 2 : c / 5 + 1));
				continue;
			}
			c = count[i] * 2 / 3;
			int a = count[i] / 5;
			if(count2[i] < c) {
				count2[i] += a + (RandomUtil.nextInt(a / 5 == 0 ? 2 : a / 5 + 1));
				continue;
			}
			a = count[i] / 10;
			count2[i] += a + (RandomUtil.nextInt(a / 5 == 0 ? 2 : a / 5 + 1));
		}
	}
	// 自然界生物在不同绿洲的类型
	public static int[][] natureArmy = {
		null,
		{0,	0,	0,	0,	0,	40,	35,	30,	0,	0,	0},
		{0,	60,	45,	0,	0,	25,	0,	0,	0,	0,	0},
		{0,	60,	40,	0,	30,	0,	0,	0,	0,	0,	0},
		{0,	0,	0,	0,	0,	40,	35,	30,	0,	0,	0},
		{0,	60,	45,	0,	0,	25,	0,	0,	0,	0,	0},
		{0,	60,	40,	0,	30,	0,	0,	0,	0,	0,	0},
		{0,	40,	0,	40,	0,	0,	0,	20,	0,	16,	0},
		{0,	40,	0,	40,	0,	0,	0,	0,	20,	15,	0},
	};
	
	public static List artList = null;
	public static HashMap artMap = null;
	public static HashMap artMap2 = null;	// 全帐户生效的宝物，保存在这里
	public static List getArtList() {
		if(artList != null)
			return artList;
		synchronized(CastleUtil.class) {
			if(artList != null)
				return artList;
			artList = castleService.getArtList("1 order by id");
			artMap = new HashMap();
			artMap2 = new HashMap();
			for(int i = 0;i < artList.size();i++) {
				ArtBean art = (ArtBean)artList.get(i);
				CastleBean castle = getCastleById(art.getCid());
				if(castle != null)
				art.setUid(castle.getUid());
				artMap.put(new Integer(art.getCid()), art);
				if(art.isFlagAccount())
					artMap2.put(new Integer(art.getUid()), art);
			}
		}
		return artList;
	}
	public static HashMap getArtMap() {
		getArtList();
		return artMap;
	}
	public static HashMap getArtMap2() {
		getArtList();
		return artMap2;
	}
	// 用户占领另一个用户的某个城堡
	public static void occupyUserCastle(CastleUserBean fromUser, CastleBean castleFrom, CastleBean castle, UserResBean userRes, CastleUserBean toUser) {
		long now = System.currentTimeMillis();
		castle.setUid(fromUser.getUid());
		castle.setRace(fromUser.getRace());
		userRes.setUserId(fromUser.getUid());
		
		castleFrom.setExpand(castleFrom.getExpand() + 1);
		fromUser.setCastleCount(fromUser.getCastleCount() + 1);
		CastleUtil.addUserCivil(fromUser, userRes.getCivil(), userRes.getPeople());
		
		
		toUser.setCastleCount(toUser.getCastleCount() - 1);
		CastleUtil.addUserCivil(toUser, -userRes.getCivil(), -userRes.getPeople());
		
		DbOperation db = new DbOperation(5);
		
		if(toUser.getCur() == castle.getId()) {
			toUser.setCur(toUser.getMain());
			db.executeUpdate("update castle_user set cur=" + toUser.getMain() + " where uid=" + toUser.getUid());
		}
		db.executeUpdate("update castle_user set castle_count=" + toUser.getCastleCount() + " where uid=" + toUser.getUid());
		
		db.executeUpdate("update castle_user set castle_count=" + fromUser.getCastleCount() + " where uid=" + fromUser.getUid());
		db.executeUpdate("update castle set uid=" + fromUser.getUid() + ",race=" + castle.getRace() + " where id=" + castle.getId());
		db.executeUpdate("update castle_user_resource set uid=" + fromUser.getUid() + " where id=" + castle.getId());
		int expandId = -1;
		try {
			expandId = db.getIntResult("select id from castle_expand where to_cid=" + castle.getId());
			
		} catch (SQLException e) {	}
		
		db.executeUpdate("update castle set expand=" + castleFrom.getExpand() + " where id=" + castleFrom.getId());
		
		if(expandId != -1) {
			int oldFromId = 0;
			try {
				oldFromId = db.getIntResult("select from_cid from castle_expand where id=" + expandId);
			} catch (SQLException e) {		}
			
			
			db.executeUpdate("update castle_expand set create_time=now(),from_cid=" + castleFrom.getId() + " where id=" + expandId);
			db.executeUpdate("update castle set expand=" + castleFrom.getExpand() + " where id=" + castleFrom.getId());
			if(oldFromId != 0) {
				CastleBean oldFrom = getCastleById(oldFromId);
				if(oldFrom != null) {
					oldFrom.setExpand(oldFrom.getExpand() - 1);
					db.executeUpdate("update castle set expand=" + oldFrom.getExpand() + " where id=" + oldFrom.getId());
				}
			}
		} else
			db.executeUpdate("insert into castle_expand set create_time=now(),from_cid=" + castleFrom.getId()
					+ ",to_cid=" + castle.getId());
		
//		if(userRes.getPeople2() != 0) {
//			userRes.addPeople2Calc(-userRes.getPeople2(), now);
//			db.executeUpdate("delete from castle_soldier where at=" + castle.getId());
//		}
		db.executeUpdate("delete from castle_soldier_smithy where cid = " + castle.getId());
		db.executeUpdate("delete from cache_soldier_smithy where cid = " + castle.getId());
		db.executeUpdate("delete from castle_hidden_soldier where cid = " + castle.getId());	// 士兵消耗粮食是不是会出错
		
		db.release();
		CacheManage.castleSmithy.srm(castle.getId());
		// 拆除所有特色建筑
		List buildings = castleService.getAllBuilding(castle.getId());
		for(int j = 0;j < buildings.size();j++){
			BuildingBean b = (BuildingBean)buildings.get(j);
			if (b.getBuildType() == 6 || b.getBuildType() == 7 || b.getBuildType() == 34
					|| b.getBuildType() == 26 || b.getBuildType() == 28 || b.getBuildType() == 29
					|| b.getBuildType() == 40 || b.getBuildType() == 41) {
				destroyBuilding(fromUser.getUid(), b, 0);
			}
		}
		
		// TODO 城堡的merchant trade？还有没有其他？
	}
	// 获得活着的指挥官
	public static HeroBean getHero(int uid) {
		return castleService.getHero("uid=" + uid + " and status=0");
	}
	// 宝物相关，如果抢夺成功返回0
	public static int moveArt(CastleBean fromCastle, CastleBean toCastle, int buildingGrade) {
		getArtList();
		Integer fromKey = new Integer(fromCastle.getId());
		Integer toKey = new Integer(toCastle.getId());
		ArtBean art = (ArtBean)artMap.get(fromKey);
		if(art != null) {
			if(buildingGrade < 10 || art.isFlagBig() && buildingGrade < 20)
				return 2;
			if(artMap.containsKey(toKey))
				return 1;
			if(userHasArt(toCastle.getUid()))	// 已经拥有一个帐号宝物
				return 1;
			
			// 先设置captureTime以免再次被激活
			long now = System.currentTimeMillis();
			art.setCaptureTime(now);
			// 宝物移动，将处于非激活状态
			if(art.isActive())
				inactivateArt(art);

			art.setCid(toCastle.getId());
			
			if(art.isFlagAccount())
				artMap2.remove(new Integer(art.getUid()));
			art.setUid(toCastle.getUid());
			if(art.isFlagAccount())
				artMap2.put(new Integer(art.getUid()), art);
			
			artMap.remove(fromKey);
			artMap.put(toKey, art);
			DbOperation db = new DbOperation(5);
			db.executeUpdate("update castle_art set status=0,uid=" + art.getUid() + ",cid=" + art.getCid() + 
					",capture_time=" + now + " where id=" + art.getId());
			db.executeUpdate("insert into castle_art_his set uid=" + art.getUid() + ",cid=" + art.getCid() + 
					",time=now(),art_id=" + art.getId());
			db.release();
			return 0;
		}
		return -1;
	}

	// 判断用户拥有几个宝物，暂时设置为只能拥有一个
	public static boolean userHasArt(int uid) {
		for(int i = 0;i < artList.size();i++) {
			ArtBean art = (ArtBean)artList.get(i);
			if(art.getUid() == uid && art.isFlagAccount())
				return true;
		}
		return false;
	}
	// 判断用户是否拥有某个宝物并且已经激活
	public static boolean userHasArt(int uid, int type) {
		getArtList();
		for(int i = 0;i < artList.size();i++) {
			ArtBean art = (ArtBean)artList.get(i);
			if(art.isActive() && art.getUid() == uid && art.getType() == type)
				return true;
		}
		return false;
	}
//	 判断用户所在的帮会其他人是否拥有某个宝物并且已经激活
	public static boolean tongHasArt(CastleUserBean user, int type) {
		getArtList();
		for(int i = 0;i < artList.size();i++) {
			ArtBean art = (ArtBean)artList.get(i);
			if(art.isActive() && art.getUid() != user.getUid() && art.getType() == type) {
				CastleUserBean user2 = CastleUtil.getCastleUser(art.getUid());
				if(user2 != null && user2.getTong() == user.getTong())
					return true;
			}
		}
		return false;
	}
//	 获取一个城堡的宝物
	public static ArtBean getCastleArt(int cid) {
		return (ArtBean)getArtMap().get(new Integer(cid));
	}
//	 获取一个激活的宝物，当前城堡优先，没有则取全帐户的
	public static int getActiveArtType(CastleBean castle) {
		ArtBean art = (ArtBean)getArtMap().get(new Integer(castle.getId()));
		if(art != null && art.isActive())
			return art.getType();
		art = (ArtBean)artMap2.get(new Integer(castle.getUid()));
		if(art != null && art.isActive())
			return art.getType();
		return 0;
	}
	public static ArtBean getActiveArt(CastleBean castle) {
		ArtBean art = (ArtBean)getArtMap().get(new Integer(castle.getId()));
		if(art != null && art.isActive())
			return art;
		art = (ArtBean)artMap2.get(new Integer(castle.getUid()));
		if(art != null && art.isActive())
			return art;
		return null;
	}
	public static void addArt(int cid, int type, String name, int flag, int effect) {
		CastleBean castle = getCastleById(cid);
		if(castle == null)
			return;
		getArtList();
		ArtBean art = new ArtBean();
		art.setCid(cid);
		art.setUid(castle.getUid());
		art.setType(type);
		art.setName(name);
		art.setFlag(flag);
		art.setEffect(effect);
		castleService.addArt(art);
		
		artList.add(art);
		artMap.put(new Integer(cid), art);
		if(art.isFlagAccount())
			artMap2.put(new Integer(castle.getUid()), art);
	}
	public static void modifyArt(int id, int cid, int type, String name, int flag, int effect, int status) {
		ArtBean art = getArt(id);
		boolean oldAccount = art.isFlagAccount();
		if(cid != art.getCid()) {	// 换城堡
			artMap.remove(new Integer(art.getCid()));
			artMap.put(new Integer(cid), art);
			CastleBean castle = CastleUtil.getCastleById(cid);
			if(castle != null)
				art.setUid(castle.getUid());
		}
		art.setType(type);
		art.setName(name);
		art.setFlag(flag);
		art.setEffect(effect);
		art.setStatus(status);
		art.setCid(cid);
		castleService.modifyArt(art);
		if(oldAccount) {
			if(!art.isFlagAccount()) {
				artMap2.remove(new Integer(art.getUid()));
			}
		} else {
			if(art.isFlagAccount()) {
				artMap2.put(new Integer(art.getUid()), art);
			}
		}
	}
	public static ArtBean getArt(int id) {
		getArtList();
		for(int i = 0;i < artList.size();i++) {
			ArtBean art = (ArtBean)artList.get(i);
			if(art.getId() == id)
				return art;
		}
		return null;
	}
	public static void deleteArt(int cid) {
		getArtList();
		Integer key = new Integer(cid);
		ArtBean art = (ArtBean)artMap.get(key);
		if(art != null) {
			artList.remove(art);
			artMap.remove(new Integer(cid));
			if(art.isFlagAccount())
				artMap2.remove(new Integer(art.getUid()));
			SqlUtil.executeUpdate("delete from castle_art where id=" + art.getId(), 5);
		}
		
	}
	// 计算拓荒和执政官最多能造多少个
	public static int calcTrainCount(CastleBean castle, int soldierType, int buildingType, int buildingGrade) {
		int canBuild = -castle.getExpand();	// 计算能训练的拓荒、皇帝数量
		if(buildingType == ResNeed.PALACE_BUILD) {
			if(buildingGrade == 20)
				canBuild += 2;
			else if(buildingGrade >= 10)
				canBuild += 1;
		} else if(buildingType == ResNeed.PALACE2_BUILD) {
			if(buildingGrade == 20)
				canBuild += 3;
			else if(buildingGrade >= 15)
				canBuild += 2;
			else if(buildingGrade >= 10)
				canBuild += 1;
		}
		if(canBuild <= 0) {
			return 0;
		}
		int sum = SqlUtil.getIntResult("select t1.c+t2.c+t3.c from (select ifnull(sum(count9)+sum(count10)*3,0) c from castle_soldier where cid=" +
					castle.getId() + ") t1,(select ifnull(sum(count9)+sum(count10)*3,0) c from castle_soldier2 where cid=" +
					castle.getId() + ") t2,(select ifnull(sum((case soldier_type when 9 then 1 when 10 then 3 end)*count),0) c from cache_soldier where cid=" + 
					castle.getId() + " and soldier_type>=9) t3", 5);
		if(sum == -1) {	// 要确保查询如果出错还是无法训练士兵
			return 0;
		}

		List attackList = cacheService.getCacheAttackList("cid=" + castle.getId());
		for(int i = 0;i < attackList.size();i++) {
			AttackThreadBean bean = (AttackThreadBean)attackList.get(i);
			CastleArmyBean army = bean.toArmy();
			sum += army.getCount(9) + army.getCount(10) * 3;
		}

		if(soldierType == 9)
			canBuild = canBuild * 3 - sum;
		else
			canBuild = canBuild - (sum + 2) / 3;
		
		if(canBuild <= 0)
			return 0;
		return canBuild;
	}
	// 创建系统城堡
	public static CastleBean createCastle(int uid, String name, int race, int x, int y, int type, String castleName, boolean setMain) {
		if(uid <= 0 || x < 0 || x >= mapSize || y < 0 || y >= mapSize || castleName == null || type <= 0)
			return null;
		getMap();
		CastleUserBean user;
		CastleBean bean;
		synchronized(createCastleLock) {
			if(map[x][y] != 0)
				return null;
			long now = System.currentTimeMillis();
			user = getCastleUser(uid);

			if(user == null) {
				if(name == null)
					return null;
				user = new CastleUserBean(uid);
				user.setName(name);
				user.setCreateTime(now);
				user.setLockTime(now + DateUtil.MS_IN_DAY * 1000);
				user.setRace(race);
				user.setCivilTime(now);
				user.setCivilSpeed(2);
				castleService.addCastleUser(user);
				user.setProtectTime(0);
				CacheManage.castleUser.spt(uid, user);
			} else {
				user.setCastleCount(user.getCastleCount() + 1);
			}
			
			bean = new CastleBean();
			bean.setUid(uid);
			bean.setRace(race);
			bean.setType(type);
			bean.setX(x);
			bean.setY(y);
			bean.setCastleName(castleName);
			bean.setMap("");
			castleService.addCastle(bean);
			
			map[x][y] = bean.getId();
			incSide(x, y);
			
		}
		if(setMain) {
			user.setMain(bean.getId());
		}
		
		SqlUtil.executeUpdate("update castle_user set main=" + user.getMain()
				+ " where uid=" + user.getUid(), 5);
		UserResBean userResBean = new UserResBean(uid, bean.getType2());
		userResBean.setId(bean.getId());
		castleService.addUserRes(userResBean);
		return bean;
	}
	public static CastleUserBean natarUser = null;
	public static CastleUserBean getNatarUser() {
		if(natarUser == null) {
			natarUser = getCastleUser(ResNeed.natarUser);
		}
		return natarUser;
	}
	
	// 回派整个军队
	public static boolean backArmy(CastleArmyBean army, CastleBean from, CastleBean at) {
		if(army.isEmpty() || from == null || from.isNature())
			return false;

		int people = army.getGrainCost(ResNeed.getSoldierTs(from.getRace()));

		UserResBean userRes = CastleUtil.getUserResBeanById(from.getId());
		if(userRes == null)
			return false;
		
		int speedAdd = userRes.getSpeedAdd();

		float heroSpeed = 20f;	// 默认20
		if(army.getHero() != 0) {
			CastleUserBean fromUser = CastleUtil.getCastleUser(from.getUid());
			if(fromUser == null)
				return false;
			HeroBean hero = fromUser.getHero();
			if(hero != null)
				heroSpeed = hero.getHeroSoldier().getSpeed();
		}
		AttackThreadBean attackThreadBean = new AttackThreadBean(army.getCount(),
				at.getId(), at.getX(), at.getY(), from,
				from.getId(), from.getX(), from.getY(), speedAdd, heroSpeed);		
		attackThreadBean.setCid(from.getId());
		attackThreadBean.setType(5);	// 移动指令都属于支援
		cacheService.addCacheAttack(attackThreadBean);
		
		if(people != 0) {
			CastleUtil.decreaseUserRes(from.getId(), 0, 0, 0, 0, people);
		}
		return true;
	}
	
	// 城堡castle放弃对oasis的占领，回派所有军队
	public static void abandonOasis(CastleBean castle, OasisBean oasis) {
		long now = System.currentTimeMillis();
		oasis.setUpdateTime(now);
		oasis.setCid(0);
		oasis.setUid(0);
		
		if(castle.getExpand2() > 0)
			castle.setExpand2(castle.getExpand2() - 1);
		
		DbOperation db = new DbOperation(5);
		// 把绿洲所有的军队遣返
		List armyList = castleService.getOasisArmyAtList(oasis.getId());
		for(int j = 0;j < armyList.size();j++) {
			CastleArmyBean army = (CastleArmyBean)armyList.get(j);
			if(army.getCid() == castle.getId()) continue;
			CastleBean c = getCastleById(army.getCid());
			if(c != null && c.getUid() != castle.getUid()) {
				backArmy(army, c, castle);
				db.executeUpdate("delete from castle_soldier2 where id=" + army.getId());
			}
		}
		
		db.executeUpdate("update castle_oasis set cid=0,uid=0,update_time=now() where id=" + oasis.getId());
		db.executeUpdate("update castle set expand2 = " + castle.getExpand2() + " where id=" + castle.getId());
		
		db.release();
		
		CastleUtil.getUserResBeanById(castle.getId()).addOasis(oasis.getType(), -25);
	}
	// 删除城堡
	public static void deleteCastle(CastleBean castle) {
		if(castle == null || castle.getId() == 0)
			return;
		CastleUserBean user = CastleUtil.getCastleUser(castle.getUid());
		int uid = 0;
		if(user != null)
			uid = user.getUid();
		
		DbOperation db = new DbOperation(5);
		int cid = castle.getId();

		// 删除所有军队（只是计算人口）
		List armyList = castleService.getCastleArmyList(cid);
		for(int j = 0;j < armyList.size();j++) {
			CastleArmyBean army = (CastleArmyBean)armyList.get(j);
			removeArmyPeople(user.getRace(), army);
			db.executeUpdate("delete from castle_soldier where id = " + army.getId());
		}
		// 删除所有支援出去到绿洲的军队
		armyList = castleService.getOasisArmyList(cid);
		for(int j = 0;j < armyList.size();j++) {
			CastleArmyBean army = (CastleArmyBean)armyList.get(j);
			removeArmy2People(user.getRace(), army);
			db.executeUpdate("delete from castle_soldier2 where id = " + army.getId());
		}
		armyList = castleService.getCastleArmyAtList(cid);
		for(int j = 0;j < armyList.size();j++) {
			CastleArmyBean army = (CastleArmyBean)armyList.get(j);
			if(army.getCid() == cid) continue;
			CastleBean c = getCastleById(army.getCid());
			if(c != null && c.getUid() != uid) {
				backArmy(army, c, castle);
				db.executeUpdate("delete from castle_soldier where id=" + army.getId());
			}
		}
		armyList = castleService.getOasisArmyAtCidList(cid);
		for(int j = 0;j < armyList.size();j++) {
			CastleArmyBean army = (CastleArmyBean)armyList.get(j);
			if(army.getCid() == cid) continue;
			CastleBean c = getCastleById(army.getCid());
			if(c != null && c.getUid() != uid) {
				backArmy(army, c, castle);
				db.executeUpdate("delete from castle_soldier2 where id=" + army.getId());
			}
		}
		
		
		CastleUtil.deleteCastle(castle.getX(), castle.getY());

		CacheManage.castle.srm(cid);
		db.executeUpdate("delete from castle where id = " + cid);
		db.executeUpdate("delete from castle_user_resource where id = " + cid);
		CacheManage.castleUserRes.srm(cid);
		
		db.executeUpdate("delete from castle_building where cid = " + cid);
		db.executeUpdate("delete from castle_hidden_soldier where cid = " + cid);
		//db.executeUpdate("delete from castle_soldier where cid = " + cid); // 除了在自己城堡的军队，其他在前面删除（需要计算人口）
		db.executeUpdate("delete from castle_soldier where at = " + cid);	// 删除所有支援的军队

		db.executeUpdate("delete from castle_soldier_smithy where cid = " + cid);

		db.executeUpdate("delete from cache_attack where cid = " + cid);
		db.executeUpdate("delete from cache_merchant where from_cid = " + cid);
		db.executeUpdate("delete from cache_building where cid = " + cid);
		db.executeUpdate("delete from cache_common where cid = " + cid);
		db.executeUpdate("delete from cache_soldier where cid = " + cid);
		db.executeUpdate("delete from cache_soldier_smithy where cid = " + cid);
		CacheManage.castleSmithy.srm(cid);
		
		db.executeUpdate("delete from castle_trade where cid = " + cid);
		db.executeUpdate("delete from castle_expand where from_cid = " + cid);
		// 删除占领的绿洲
		db.executeUpdate("delete from castle_soldier2 where at_cid = " + cid);
		List olist = castleService.getOasisList("cid="+cid);
		for(int j = 0;j < olist.size();j++) {
			OasisBean oasis = (OasisBean)olist.get(j);
			db.executeUpdate("update castle_oasis set cid=0,uid=0,update_time=now() where id=" + oasis.getId());
			CacheManage.oasis.srm(oasis.getId());
		}
		user.addCastleCount(-1);
		db.executeUpdate("update castle_user set castle_count=" + user.getCastleCount() + " where uid=" + user.getUid());
		db.release();
	}
	
//	 快速删除城堡，一般只能用于删除系统城堡(无外派军队无绿洲)
	public static void deleteCastleQuick(CastleBean castle) {
		if(castle == null || castle.getId() == 0)
			return;
		CastleUserBean user = CastleUtil.getCastleUser(castle.getUid());
		int uid = 0;
		if(user != null)
			uid = user.getUid();
		
		DbOperation db = new DbOperation(5);
		int cid = castle.getId();
		
		CastleUtil.deleteCastle(castle.getX(), castle.getY());

		CacheManage.castle.srm(cid);
		db.executeUpdate("delete from castle where id = " + cid);
		db.executeUpdate("delete from castle_user_resource where id = " + cid);
		CacheManage.castleUserRes.srm(cid);
		
		db.executeUpdate("delete from castle_building where cid = " + cid);
		db.executeUpdate("delete from castle_hidden_soldier where cid = " + cid);
		//db.executeUpdate("delete from castle_soldier where cid = " + cid); // 除了在自己城堡的军队，其他在前面删除（需要计算人口）
		db.executeUpdate("delete from castle_soldier where at = " + cid);	// 删除所有支援的军队
		db.executeUpdate("delete from castle_soldier_smithy where cid = " + cid);

		user.addCastleCount(-1);
		db.executeUpdate("update castle_user set castle_count=" + user.getCastleCount() + " where uid=" + user.getUid());
		db.release();
	}
	// 检查所有的宝物是否到了激活时间
	public static void task(int count) {
		if(artList != null) {
			// 如果早于activeTime捕获的，激活 之
			long activeTime = System.currentTimeMillis() - DateUtil.MS_IN_DAY;
			for(int i = 0;i < artList.size();i++) {
				ArtBean art = (ArtBean)artList.get(i);
				if(art.isActive() || art.getCaptureTime() == 0)
					continue;
				if(art.getCaptureTime() < activeTime)
					activateArt(art);
			}
		}
	}
	
	public static void activateArt(ArtBean art) {
		art.setStatus(1);
		SqlUtil.executeUpdate("update castle_art set status=1 where id=" + art.getId(), 5);
		CastleBean castle = getCastleById(art.getCid());
		if(castle == null || castle.getRace() == 5)
			return;
		switch(art.getType()) {
		case 1: {		// 建筑图纸
			
		} break;
		case 5: {
			UserResBean userResBean = CastleUtil.getUserResBeanById(castle.getId());
			
			userResBean.reCalc(System.currentTimeMillis());
			userResBean.addFlag(UserResBean.FLAG_ART);
			castleService.updateUserResFlag(userResBean);
		} break;
		case 15: {
			long now = System.currentTimeMillis();
			List list = castleService.getCastleList(art.getUid());
			for(int i = 0;i < list.size();i++) {
				castle = (CastleBean)list.get(i);
				UserResBean userResBean = CastleUtil.getUserResBeanById(castle.getId());
				
				userResBean.reCalc(now);
				userResBean.addFlag(UserResBean.FLAG_ART2);
				castleService.updateUserResFlag(userResBean);
			}
		} break;
		}
	}
	public static void inactivateArt(ArtBean art) {
		art.setStatus(0);
		SqlUtil.executeUpdate("update castle_art set status=0 where id=" + art.getId(), 5);
		CastleBean castle = getCastleById(art.getCid());
		if(castle == null || castle.getRace() == 5)
			return;
		switch(art.getType()) {
		case 1: {		// 建筑图纸
			
		} break;
		case 5: {
			UserResBean userResBean = CastleUtil.getUserResBeanById(castle.getId());
			
			userResBean.reCalc(System.currentTimeMillis());
			userResBean.deleteFlag(UserResBean.FLAG_ART);
			castleService.updateUserResFlag(userResBean);
		} break;
		case 15: {
			long now = System.currentTimeMillis();
			List list = castleService.getCastleList(art.getUid());
			for(int i = 0;i < list.size();i++) {
				castle = (CastleBean)list.get(i);
				UserResBean userResBean = CastleUtil.getUserResBeanById(castle.getId());
				
				userResBean.reCalc(now);
				userResBean.deleteFlag(UserResBean.FLAG_ART2);
				castleService.updateUserResFlag(userResBean);
			}
		} break;
		case 7: {		// 大仓库粮仓
			// 拆除所有大仓库大粮仓
			if(art.isFlagAccount()) {
				List list = castleService.getCastleList(art.getUid());
				for(int i = 0;i < list.size();i++) {
					castle = (CastleBean)list.get(i);
					List buildings = castleService.getAllBuilding(castle.getId());
					for(int j = 0;j < buildings.size();j++){
						BuildingBean b = (BuildingBean)buildings.get(j);
						if (b.getBuildType() == 40 || b.getBuildType() == 41) {	// 40 41带art flag
							destroyBuilding(castle.getUid(), b, 0);
						}
					}
				}
			} else {
				List buildings = castleService.getAllBuilding(castle.getId());
				for(int j = 0;j < buildings.size();j++){
					BuildingBean b = (BuildingBean)buildings.get(j);
					if (b.getBuildType() == 40 || b.getBuildType() == 41) {	// 40 41带art flag
						destroyBuilding(castle.getUid(), b, 0);
					}
				}
			}
		} break;
		}
	}
	// 宝物随机变化
	public static int[] randomArt = {2, 3, 4, 6, 8};
	public static void changeArt(ArtBean art) {
		int r = randomArt[RandomUtil.nextInt(randomArt.length)];
		if(art.isFlagAccount())
			r += 10;
		art.setType(r);
		SqlUtil.executeUpdate("update castle_art set type=" + r + " where id=" + art.getId(), 5);
	}	
}
