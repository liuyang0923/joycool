package net.joycool.wap.spec.castle;

import java.util.*;

import net.joycool.wap.framework.Integer2;
import net.joycool.wap.util.DateUtil;

public class ResNeed {
	
	public static String[] raceNames = {"未知", "拜索斯", "伊斯", "杰彭", "自然界", "纳塔"};
	public static String[] raceInfos = {"这是一个神秘的种族,没有人知道他们来自哪里,见过的人也没有能活着离开.",
		"拜索斯拥有高度社会文明及发达科技，拜索斯人是一个攻守俱佳的种族，但反之因为需要确保每一名士兵都有出色的攻防能力，拜索斯部队需要较多的资源及时间去训练。换句话说，拜索斯部队的开发成本是三个种族中最高亦是最慢的。<br/>拜索斯拥有优秀的步兵，但步兵对敌方的骑兵防守力比另外两个种族来得弱。", 
		"善于攻击的伊斯人拥有英勇无畏的精神，使他们成为最令人畏惧的部队。初级部队开发成本较低而攻击力不俗，使得其在游戏初期拥有一定优势。<br/>但伊斯人缺少了拜索斯人及杰彭人拥有的军事纪律，导致他们行军速度缓慢及防御力弱。", 
		"杰彭人是热爱和平的防守型种族。他们有优秀的防守部队。但这并不代表他们不善于攻击，杰彭人全部是优秀的骑士，行军速度是三个种族中最快的，都给敌人以突如其来的强力袭击。<br/>杰彭人是最灵活多变的种族，强大的防守加上高速的移动使杰彭族玩家能成为团队中最佳支援部队。不论是强力快速的救援或是物资的输送，杰彭人都可以胜任。",
		"来自大自然的打击力量,告诫人们破坏自然无疑是错误的行为.",
		"纳塔族是纯粹的NPC种族,玩家无法选择纳塔族进行游戏."};
	// 种族特色
	public static String[] raceAdvs = {"没有什么比神秘更难对付!",
		"拜索斯是唯一一个能够同时开发建筑物和资源的种族<br/>拜索斯的护城墙是三个种族中最坚固的<br/>每位商人可运送500个资源（速度: 每小时16格）<br/>拥有非常强大的步兵，比较普通的骑兵<br/>开发成本高及开发时间长",
		"抢劫时对方的山洞只能够保护四分之三的资源<br/>城墙很难被完全摧毁，但是防御力一般<br/>每位商人可运送1000个单位数量得资源（速度: 每小时12格）<br/>部队开发成本和速度是三族中最便宜最快的<br/>防御力弱",
		"拥有全游戏中速度最快的部队<br/>城墙防御力一般<br/>每位商人可运送750个资源（速度:每小时24格）<br/>两倍大的山洞<br/>昂贵的攻城武器<br/>拓荒者开发成本较低",
		"大自然的力量深不可测",
		"纳塔族的分城可以被占领,被占领后该城堡保留纳塔的种族特性<br/>纳塔族的建筑时间是其他种族的一半<br/>纳塔族内所有士兵(包括支援)粮食消耗减半<br/>纳塔族的主城地处险要,兵力强大,无法攻打和占领<br/>(未知)<br/>(未知)<br/>"
	};
	public static String getRaceName(int race) {
		return raceNames[race];
	}
	public static String getRaceInfo(int race) {
		return raceInfos[race];
	}
	public static int heroGrainCost = 6;
	public static int maxCastleCount = 20;
	public static int natarUser = 100;	// 纳塔用户的id
	
	static HashMap buildingTMap = null;	// map里是int int对应的一个建筑
	static BuildingTBean[][] buildingsT = null;	// 二维数组保存所有建筑不同等级的数据
	static BuildingTBean[] buildTemplate = null;
	public static int buildingTypeCount = 45;	// 最多44种建筑
	public static BuildingTBean[] getBuildingTs() {
		if(buildTemplate != null)
			return buildTemplate;
		
		synchronized(ResNeed.class) {
			if(buildTemplate != null)
				return buildTemplate;
			buildTemplate = CacheService.getBuildingTemplate();
		}
		
		return buildTemplate;
	}
	public static HashMap getBuildingTMap() {
		if(buildingTMap != null)
			return buildingTMap;
		synchronized(ResNeed.class) {
			loadBuildingTMap();
		}
		return buildingTMap;
	}
	public static BuildingTBean[][] getBuildingsT() {
		if(buildingsT != null)
			return buildingsT;
		
		getBuildingTs();
		
		synchronized(ResNeed.class) {
			loadBuildingTMap();
		}
		return buildingsT;
	}
	public static synchronized void loadBuildingTMap() {
		if(buildingTMap != null)
			return;
		
		buildingTMap = new HashMap();
		buildingsT = new BuildingTBean[buildingTypeCount][];

		//从数据库读取资源
		List list = CastleService.loadAllBuildingResourceNeed();			
		//加入到缓存中
		Iterator it = list.iterator();
		while(it.hasNext()) {
			BuildingTBean bean = (BuildingTBean)it.next();
			buildingTMap.put(new Integer2(bean.getBuildType(), bean.getGrade()), bean);
			int maxGrade = buildTemplate[bean.getBuildType()].getMaxGrade();
			if(buildingsT[bean.getBuildType()] == null)
				buildingsT[bean.getBuildType()] = new BuildingTBean[maxGrade + 1];	// 最高20级
			if(bean.getGrade() <= maxGrade)
			buildingsT[bean.getBuildType()][bean.getGrade()] = bean;
		}
		// 计算 totalpeople 字段
		for(int i = 1;i < buildingTypeCount;i++) {
			BuildingTBean[] bs = buildingsT[i];
			if(bs != null) {
				int people = 0;
				for(int j = 1;j < bs.length;j++) {
					if(bs[j] == null)
						break;
					people += bs[j].getPeople();
					bs[j].setTotalPeople(people);
				}
			}
		}
	}
	// 资源0级时候的产量
	public static int RES_BASE = 6;		// 3x服务器的特点
	// 三个种族的城墙
	public static int[] raceWall = {
		0,
		ResNeed.WALL_BUILD,
		ResNeed.WALL2_BUILD,
		ResNeed.WALL3_BUILD,
		ResNeed.WALL_BUILD,
		ResNeed.WALL_BUILD,
		ResNeed.WALL_BUILD,
	};
	// 清空缓存，重新载入数据
	public static synchronized void clearBuildingT() {
		buildingsT = null;
		buildTemplate = null;
	}
	public static BuildingTBean getBuildingT(int type, int grade) {
		return getBuildingsT()[type][grade];
	}
	public static BuildingTBean[] getBuildingsT(int type) {
		return getBuildingsT()[type];
	}
	
	public static BuildingTBean getBuildingT(int type) {
		return getBuildingTs()[type];
	}
	
	static SoldierResBean[][] soldierResBeans;
	
	public static SoldierResBean[] getSoldierTs(int race){
		if(soldierResBeans != null)
			return soldierResBeans[race];
		
		synchronized(ResNeed.class) {
			if(soldierResBeans != null)
				return soldierResBeans[race];
			
			soldierResBeans = CacheService.getAllSoldierInfo();
			
			return soldierResBeans[race];
		}
		
	}
	public static SoldierResBean getSoldierRes(int race, int type) {
		return getSoldierTs(race)[type];
	}
	
	
	public static final int BUILDING_INTERVAL = 10;
	
	public static final int SOLDIER_INTERVAL = 10;
	
	public static final int DEFENCE_INTERVAL = 10;
	
	public static final int ATTACK_INTERVAL = 10;
	

	public static final int GRAIN_BUILD 		= 1;	// 粮食建筑
	public static final int FE_BUILD 			= 2;	// 铁矿建筑
	public static final int WOOD_BUILD 		= 3;	// 木头建筑
	public static final int CITY_BUILD		= 4;	// 城市中心
	public static final int CASERN_BUILD		= 5;	// 兵营
	public static final int WHISTLE_BUILD		= 6;	// 哨塔 或者是城堡
	public static final int HIDDEN_BUILD		= 7;	// 藏兵洞
	public static final int SMITHY_BUILD 		= 8;	// 铁匠铺
	public static final int STONE_BUILD		= 9;	// 采石场
	public static final int RESEARCH_BUILD	= 10;	// 研究所

	public static final int STORAGE_BUILD 	= 11;	// 仓库 所有上限
	public static final int BARN_BUILD		= 12;	// 粮仓 //粮食上限
	public static final int WOOD_FACTORY_BUILD = 13;	// 木材厂
	public static final int STONE_FACTORY_BUILD = 14;	// 砖块厂
	public static final int FOUNDRY_BUILD = 	15;	// 铸造厂
	public static final int MOFANG_BUILD = 	16;	// 磨坊
	
	public static final int MARKET_BUILD = 	17;	// 市场

	public static final int GUN_ROOM_BUILD = 18;	// 军械库

	public static final int BREAD_BUILD = 	19;	// 面包房

	public static final int PALACE_BUILD = 20;	// 行宫
	
	public static final int CAVE_BUILD = 21;		// 山洞
	
	public static final int GATHER_BUILD = 22;		// 集结点
	
	public static final int HEVO_BUILD = 23;		// 马厩
	public static final int FACTORY_BUILD = 24;		// 工场
	public static final int EMMBASSY_BUILD = 25;	// 大使馆
	public static final int WALL_BUILD = 26;		// 城墙
	public static final int HALL_BUILD = 27;		// 广场
	public static final int WALL2_BUILD = 28;		// 城墙
	public static final int WALL3_BUILD = 29;		// 城墙
	public static final int TRADE_BUILD = 30;		// 交易所
	public static final int ARENA_BUILD = 31;		// 竞技场
	public static final int PALACE2_BUILD = 32;		// 皇宫
	public static final int STONE2_BUILD = 33;		// 石匠铺
	public static final int TRAP_BUILD = 34;		// 陷阱机
	public static final int CASERN2_BUILD = 35;		// 大兵营
	public static final int HEVO2_BUILD = 36;		// 大靶场
	public static final int HERO_BUILD = 37;		// 英雄园
	public static final int WONDER_BUILD = 38;		// 奇迹
	public static final int TREASURE_BUILD = 39;		// 宝库
	public static final int STORAGE2_BUILD 	= 40;	// 仓库 所有上限
	public static final int BARN2_BUILD		= 41;	// 粮仓 //粮食上限
	
	static int[] maxRes = {800, 1200, 1700, 2300, 3100, 4000, 5000, 6300, 7800, 9600, 11800, 14400, 17600, 21400, 25900, 31300, 37900, 45700, 55100, 66400, 80000};
	public static int getStorageStore(int grade) {
		
		return maxRes[grade];
	}
	

	
	
	
	public static int getGrainStore(int grade) {
		
		return maxRes[grade];
	}
	
	///-------------------------------------------------------------------------


	public static int getWoodFactoryFactor(int grade){
		
		return 5;
	}

	public static int getStoneFactoryFactor(int grade){
		
		return 5;
	}
	

	public static int getFoundryFactor(int grade){
		
		return 5;
	}
	

	public static int getmoFangFactor(int grade){
		
		return 5;
	}
	
	public static int[] advanceBuild = {
		CITY_BUILD,
		STORAGE_BUILD,
		BARN_BUILD,
		STORAGE2_BUILD,
		BARN2_BUILD,
		MARKET_BUILD,
		CAVE_BUILD,
		EMMBASSY_BUILD,
		GATHER_BUILD, 
		CASERN_BUILD, 
		RESEARCH_BUILD,
		TRAP_BUILD,
		HERO_BUILD,
		WALL_BUILD,
		WALL2_BUILD,
		WALL3_BUILD,
		SMITHY_BUILD,
		WHISTLE_BUILD,
		HIDDEN_BUILD,
		HEVO_BUILD,
		GUN_ROOM_BUILD,
		MOFANG_BUILD,
		WOOD_FACTORY_BUILD,
		STONE_FACTORY_BUILD,
		FOUNDRY_BUILD,
		BREAD_BUILD,
		FACTORY_BUILD,
		PALACE_BUILD,
		PALACE2_BUILD,
		HALL_BUILD,
		TRADE_BUILD,
		ARENA_BUILD,
		STONE2_BUILD,
		CASERN2_BUILD,
		HEVO2_BUILD,
		TREASURE_BUILD,
		WONDER_BUILD,
	};
	
	
/*------------------------------------------士兵相关------------------------------------------------------*/	
	/**
	 * 获取建造一个士兵所需要的资源
	 * @param soldierType
	 * @return
	 */
	public static ResTBean getSoldierNeedRes(int race, int soldierType) {
		SoldierResBean g = getSoldierRes(race, soldierType);
		return new ResTBean(
				g.getWood(),
				g.getFe(),
				g.getGrain(),
				g.getStone(),
				g.getTime());
	}
	/**
	 * 获取士兵的行军速度
	 * @param type
	 * @return
	 */
	public static float getSoldierSpeed(int race, int type) {
		return getSoldierRes(race, type).getSpeed();
	}
	
	/**
	 * 获取士兵的初始攻击值，attack1是近战，attack2是远战
	 */
	public static int getBaseAttack(int race, int type) {
		return getSoldierRes(race, type).getAttack();
	}
	
	/**
	 * 获取士兵的初始防御值
	 */
	public static int getBaseDefense(int race, int type) {
		return getSoldierRes(race, type).getDefense();
	}	
	/**
	 * 根据士兵类型获取士兵名称
	 * @param type
	 * @return
	 */
	public static String getSoldierType(int race, int type) {
		return getSoldierRes(race, type).getSoldierName();
	}
	
	/**
	 * 不同士兵能抢夺的资源数量
	 */
	public static int getSoldierStore(int race, int type) {
		return getSoldierRes(race, type).getStore();
	}	
	
/*-------------------------------------建筑相关------------------------------------------------------*/
	
	//根据类型获取建筑名称
	public static String getTypeName(int type) {
		BuildingTBean[] bts = getBuildingTs();
		if(type < bts.length)
			return bts[type].getName();
		else 
			return "空地";
	}
	
	/**
	 * 获取建筑的最高等级
	 * @param buildType
	 * @return
	 */
	public static int getBuildTopGrade(int buildType){
		BuildingTBean[] bts = getBuildingTs();
		if(buildType < bts.length)
			return bts[buildType].getMaxGrade();
		else 
			return 0;
	}
	
	//不同等级的粮食增长速度
	public static int[] grainSpeed = {
		2,5,9,15,22,33,57,81,100,145,200
	};
	/**
	 * 获取粮食建造速度
	 * @param grade
	 * @return
	 */
	public static int getGrainSpeed(int grade) {
		return grainSpeed[grade] - woodSpeed[grade - 1];
	}
	
	//不同等级的铁的增长速度
	public static int[] feSpeed = {
		2,5,9,15,22,33,57,81,100,145,200
	};
	/**
	 * 获取铁矿建造速度
	 * @param grade
	 * @return
	 */
	public static int getFeSpeed(int grade) {
		return feSpeed[grade] - woodSpeed[grade - 1];
	}
	
	//不同等级的木头增长速度
	public static int[] woodSpeed = {
		2,5,9,15,22,33,57,81,100,145,200
	};
	/**
	 * 获取木材建造速度
	 * @param grade
	 * @return
	 */
	public static int getWoodSpeed(int grade) {
		return woodSpeed[grade] - woodSpeed[grade - 1];
	}
	
	//不同等级的石头增长速度
	public static int[] stoneSpeed = {
		2,5,9,15,22,33,57,81,100,145,200
	};
	/**
	 * 获取木材建造速度
	 * @param grade
	 * @return
	 */
	public static int getStoneSpeed(int grade) {
		return woodSpeed[grade] - woodSpeed[grade - 1];
	}
	
	
	//不同等级的城市中心保护被抢资源百分比
	public static float[] cityProtected ={
		.5f, .5f,.6f, .65f, .7f, .75f
	};
	/**
	 * 获取城市中心保护资源百分比
	 * @param grade
	 * @return
	 */
	public static float getCityProtected(int grade) {
		return cityProtected[grade];
	}
	
	/*---------不同兵营等级，影响造兵速度的因子 start---------*/
	public static float[] soldierSpeedFactor = {
		1f,1f,0.9f,0.81f,0.73f,0.66f,0.59f,0.53f,0.48f,0.43f,0.39f,0.35f,0.31f,0.28f,0.25f,0.23f,0.21f,0.19f,0.17f,0.15f,0.14f
	};
	/**
	 * 获取造兵速度百分比
	 * @param grade
	 * @return
	 */
	public static float getSoldierSpeedFactor(int grade) {
		return soldierSpeedFactor[grade];
	}
	/*---------不同兵营等级，影响造兵速度的因子 end-----------*/	
	
	//哨塔不同等级不同的攻击值
	public static int[] wAttack = {
		0,10,30,60,100,150,210,280,360,450,550
	};
	/**
	 * 获取哨塔攻击力
	 * @param grade
	 * @return
	 */
	public static int getWhistleAttact(int grade) {
		return wAttack[grade];
	}
	
	//藏兵洞藏兵的容量
	public static int[] hCount = {
		0,10,20,30,40,50,60,70,80,90,100
	};
	
	/**
	 * 获取藏兵数藏兵数量
	 * @param grade
	 * @return
	 */
	public static int getHiddenCount(int grade) {
		return hCount[grade];	
	}
	
	//不同等级的主城人口上限
	public static int tPeople[] = {
		50, 50, 80,130,160,200,300,400,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500
	};
	
	/**
	 * 得到用户的总的能容纳的人口数量，根据城镇中心等级
	 * @param grade
	 * @return
	 */
	public static int getTotalPeople(int grade) {
		return tPeople[grade];
	}
	
/*----------------------攻击相关：包括每种士兵的攻防，每种士兵能抢夺的资源-------------------------------------*/
	//每级防御值所需要的资源倍数
	public static float[] defenseResource = {
		1f, 5f, 10f, 15f, 20f, 25f, 30f, 35f, 40f, 45f, 50f, 
	};
	
	//每级攻击值所需要的资源倍数
	public static float[] attackResource = {
		1f, 5f, 10f, 15f, 20f, 25f, 30f, 35f, 40f, 45f, 50f, 
	};
	
	//每级攻防升级所需要的升级时间
	public static float[] upgradeFactor = new float[21];
	static {
		for(int i = 0;i <= 20;i++)
			upgradeFactor[i] = (float) Math.pow(i, 0.8f);
	};
	
	//获得每级防御值所需要的资源，包括所需时间
	public static ResTBean getDefenceResource(int race, int type, int grade) {
		SoldierResBean soldier = getSoldierRes(race, type);
		ResTBean r = new ResTBean(soldier.getWood3(), soldier.getFe3(), 
				soldier.getGrain3(), soldier.getStone3(), soldier.getTime2(), upgradeFactor[grade]);
		return r;
	}
	
	//获得每级攻击值所需要的资源，包括所需时间
	public static ResTBean getAttackResource(int race, int type, int grade) {
		SoldierResBean soldier = getSoldierRes(race, type);
		ResTBean r = new ResTBean(soldier.getWood3(), soldier.getFe3(), 
				soldier.getGrain3(), soldier.getStone3(), soldier.getTime2(), upgradeFactor[grade]);
		return r;
	}
	
	public static ResTBean getResearchResource(int race, int type) {
		SoldierResBean soldier = getSoldierRes(race, type);
		ResTBean r = new ResTBean(soldier.getWood2(), soldier.getFe2(), 
				soldier.getGrain2(), soldier.getStone2(), soldier.getTime2());

		return r;
	}
	
	//每级对应的攻防升级效果
	public static int attackUpMax = 20;
	public static float[] attackFactor = new float[attackUpMax + 1];
	static {
		for(int i = 0;i < attackUpMax + 1;i++)
			attackFactor[i] = (float) Math.pow(1.015, i);
	}
	// 攻防升级效果
	public static float getAttackFactor(SoldierSmithyBean bean) {
		if(bean == null)
			return 1f;
		return attackFactor[bean.getAttack()];
	}
	public static float getDefenseFactor(SoldierSmithyBean bean) {
		if(bean == null)
			return 1f;
		return attackFactor[bean.getDefence()];
	}
	
	//计算士兵的总攻击值
	public static float[] calcAttack(int race, List soldiers, SoldierSmithyBean[] fromSmithy) {
		SoldierResBean[] g = getSoldierTs(race);
		float[] sum = {0f,0f};
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0) {
				SoldierResBean s = g[i + 1];
				if(fromSmithy[i + 1] != null) {
					sum[0] += s.getAttack() * attackFactor[fromSmithy[i + 1].getAttack()] * count;
					sum[1] += s.getAttack2() * attackFactor[fromSmithy[i + 1].getAttack()] * count;
				} else {
					sum[0] += s.getAttack() * attackFactor[0] * count;
					sum[1] += s.getAttack2() * attackFactor[0] * count;
				}
			}
				
		}
		return sum;
	}
	//计算士兵的总侦察值，防御也是同样方式计算
	public static float calcScout(int race, List soldiers, SoldierSmithyBean[] fromSmithy) {
		SoldierResBean[] g = getSoldierTs(race);
		float sum = 0f;
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0) {
				SoldierResBean s = g[i + 1];
				if(fromSmithy[i + 1] != null) {
					sum += s.getAttack3() * attackFactor[fromSmithy[i + 1].getAttack()] * count;
				} else {
					sum += s.getAttack3() * attackFactor[0] * count;
				}
			}
				
		}
		return sum;
	}
	//计算士兵的总侦察值，防御也是同样方式计算
	public static int calcScoutDefense(int race, List soldiers, SoldierSmithyBean[] fromSmithy) {
		SoldierResBean[] g = getSoldierTs(race);
		int sum = 0;
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0) {
				SoldierResBean s = g[i + 1];
				if(fromSmithy[i + 1] != null) {
					sum += s.getDefense3() * attackFactor[fromSmithy[i + 1].getDefence()] * count;
				} else {
					sum += s.getDefense3() * attackFactor[0] * count;
				}
			}
				
		}
		return sum;
	}
	//计算士兵的总人口
	public static int calcPeople(int race, List soldiers) {
		SoldierResBean[] g = getSoldierTs(race);
		int sum = 0;
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0)
				sum += count * g[i + 1].getPeople();

		}
		return sum;
	}
	//计算总的防御值
	public static int[] calcDefense(int race, List soldiers, SoldierSmithyBean[] fromSmithy) {
		int[] sum = {0,0};
		SoldierResBean[] g = getSoldierTs(race);
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((SoldierBean)soldiers.get(i)).getCount();
			if(count > 0) {
				SoldierResBean s = g[i + 1];
				if(fromSmithy[i + 1] != null) {
					sum[0] += s.getDefense() * attackFactor[fromSmithy[i + 1].getDefence()] * count;
					sum[1] += s.getDefense2() * attackFactor[fromSmithy[i + 1].getDefence()] * count;
				} else {
					sum[0] += s.getDefense() * attackFactor[0] * count;
					sum[1] += s.getDefense2() * attackFactor[0] * count;
				}
			}
				
		}
		return sum;
	}
	
	/**
	 * 计算掠夺的总数
	 * @param soldiers
	 * @return
	 */
	public static int calcStore(int race, List soldiers) {
		int sum = 0;
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0)
				sum += getSoldierStore(race, i + 1) * count;
		}
		return sum;
	}
	public static int calcStore(int race, int[] soldiers) {
		int sum = 0;
		for(int i = 1;i < soldiers.length;i++) {
			int count = soldiers[i];
			if(count > 0)
				sum += getSoldierStore(race, i) * count;
		}
		return sum;
	}
	
	
	public static String getTimeLeft(long time) {
		long now = System.currentTimeMillis();
		long left = time - now;
		
		if(left < 0) {
			left = 0;
		}
		
		StringBuilder sb = new StringBuilder();
		
		if(left > 0) {
			int hourLeft = (int)(left / DateUtil.MS_IN_HOUR);
			left = (int)(left % DateUtil.MS_IN_HOUR);
			int minLeft = (int)left / 60000;
			int secondLeft = (int)left % 60000 / 1000;		
			
			sb.append("还剩");
			if(hourLeft > 0) {
				sb.append(hourLeft);
				sb.append("小时");
			}
			
			sb.append(minLeft);
			sb.append("分");
			sb.append(secondLeft);
			sb.append("秒");
		} else {
			sb.append("还剩1秒");
		}
		
		return sb.toString();
	}
	// 建造条件
	public static String getBuildingPreString(int type) {
		BuildingTBean[] ts = getBuildingTs();
		BuildingTBean bt = ts[type];
		List list = bt.getPreList();
		if(list == null || list.size() == 0) {
			if(bt.isFlagMain())
				return "建造条件:只能在主城建造";
			else if(bt.isFlagNotMain())
				return "建造条件:不能在主城建造";
			else
				return "建造条件:无";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("建造条件:");
		for(int i = 0;i < list.size();i++) {
			int[] is = (int[])list.get(i);
			if(i > 0)
				sb.append(',');
			if(is[1] > 0) {
				sb.append(ts[is[0]].getName());
				sb.append("(等级");
				sb.append(is[1]);
				sb.append(')');
			} else {
				sb.append('无');
				sb.append(ts[is[0]].getName());
			}
		}
		if(bt.isFlagMain())
			sb.append(",只能在主城建造");
		else if(bt.isFlagNotMain())
			sb.append(",不能在主城建造");
		if(bt.isFlagNatar())
			sb.append(",世界奇迹");
		else if(bt.isFlagNotNatar())
			sb.append(",无世界奇迹");
		return sb.toString();
	}
	// 研发条件
	public static String getResearchPreString(int race, int type) {
		BuildingTBean[] ts = getBuildingTs();
		List list = getSoldierRes(race, type).getPreList();
		if(list == null || list.size() == 0)
			return "研发条件:无";
		StringBuilder sb = new StringBuilder();
		sb.append("研发条件:");
		for(int i = 0;i < list.size();i++) {
			int[] is = (int[])list.get(i);
			if(i > 0)
				sb.append(',');
			sb.append(ts[is[0]].getName());
			sb.append("(等级");
			sb.append(is[1]);
			sb.append(')');
		}
		return sb.toString();
	}
	// 大型活动60小时，小型活动24小时，升级广场时间每级减少3.6%
	public static ResTBean celeRes = new ResTBean(6400, 5940, 1340,6650,  24 * 60 * 60);
	public static ResTBean cele2Res = new ResTBean(29700, 32000, 6700, 33250, 60 * 60 * 60);
	// 等级和时间缩短的关系，适用于举行活动和铁匠铺升级等等
	public static float getGradeTime(int grade) {
		return gradeTime[grade];
	}
	public static int getGradeTime(int grade, int initTime) {
		return Math.round(gradeTime[grade] * initTime * 0.1f) * 10;
	}
	public static float[] gradeTime = new float[21];
	public static int soldierTypeCount = 10;	// 10个兵种
	
	public static int castlePosCount = 40;		// 40个建筑位置
	public static int castlePosCountNatar = 37;		// Natar只有37个建筑位置
	static {
		gradeTime[0] = 5f;	// 如果城堡砸平了，所有建筑建造时间5倍
		gradeTime[1] = 1f;
		for(int i = 2;i <= 20;i++)
			gradeTime[i] = gradeTime[i-1] * 0.964f;
	}
	
	public static List questList = null;
	public static List getQuestList() {
		if(questList != null)
			return questList;
		synchronized(ResNeed.class) {
			if(questList != null)
				return questList;
			questList = CastleService.getQuestList(" 1 order by id");
		}
		return questList;
	}
	public static CastleQuestBean getQuest(int i) {
		return (CastleQuestBean)getQuestList().get(i);
	}
	public static int  getQuestCount() {
		return getQuestList().size();
	}
	// 需要的文明度civilNeed[n]建造第n+1个城堡
	public static int[] civilNeed = {
		0, 2000, 8000, 20000, 39000, 65000, 99000, 141000, 191000, 251000, 319000, 
		397000, 486000, 584000, 692000, 811000, 941000, 1082000, 1234000, 1397000, 
		1572000, 1759000, 1957000, 2168000, 2391000, 2627000, 2874000, 3135000, 
		3409000, 3695000, 3995000, 4308000, 4634000, 4974000, 5327000, 5695000, 
		6076000, 6471000, 6881000, 7304000, 7742000, 8195000, 8662000, 9143000, 
		9640000, 10151000, 10677000, 11219000, 11775000, 12347000, 12935000, 13537000,
		14156000, 14790000, 15439000, 16105000, 16786000, 17484000, 18197000, 18927000,
		19673000, 20435000, 21214000, 22009000, 22821000, 23649000, 24495000, 25357000,
		26236000, 27131000, 28044000, 28974000, 29922000, 30886000, 31868000, 32867000,
		33884000, 34918000, 35970000, 37039000, 38127000, 39232000, 40354000, 41495000,
		42654000, 43831000, 45026000, 46240000, 47471000, 48721000, 49989000, 51276000,
		52581000, 53905000, 55248000, 56609000, 57989000, 59387000, 60805000, 62242000,
		63697000, 65172000, 66665000, 68178000, 69710000, 71262000, 72832000, 74422000,
		76032000, 77661000, 79309000, 80977000, 82665000, 84372000, 86100000, 87847000,
		89613000, 91400000, 93207000, 95034000, 96881000, 98748000, 100635000, 
		102542000, 104470000, 
	};
	public static int getNeedCivil(int castleCount) {
		return civilNeed[castleCount];
	}
	
	// 三个种族的商人移动速度
	public static int[] merchantSpeed = {1, 16, 12, 24};
	public static int getMerchantSpeed(int race) {
		return merchantSpeed[race];
	}
	// 每个建筑对应可以训练的士兵种类
	public static int[] canTrainTypes = {0, 
		0, 0, 0, 0, 5, 0, 0, 0 ,0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0 ,20, 
		0, 0, 23, 24, 0, 0, 0, 0, 0 ,0, 
		0, 20, 0, 0, 5, 23, 0, 0, 0 ,0};
	// 每个建筑对应可以训练的士兵的训练倍数（资源多倍)
	public static int[] trainCosts = {0, 
		0, 0, 0, 0, 1, 0, 0, 0 ,0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0 ,1, 
		0, 0, 1, 1, 0, 0, 0, 0, 0 ,0, 
		0, 1, 0, 0, 3, 3, 0, 0, 0 ,0};
	
	// 不同类型的地形城堡，拥有的普通资源田分布
	public static int[][] baseBuildRes = {
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,9,2,1,1,1},
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,9,2,1,1,1},
		{1,3,9,2,1,3,9,2,1,3,9,2,1,2,9,2,1,1,1},
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,3,2,1,1,1},
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,9,9,1,1,1},
		{1,3,9,2,1,3,9,2,1,3,9,2,1,1,1,1,1,1,1},
		{1,3,9,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,9,1,1,1,1},	// 7田
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,1,2,1,1,1},
		{1,3,9,2,1,3,9,2,1,3,9,2,1,1,9,2,1,1,1},
		
		{1,3,9,2,1,3,9,2,1,3,9,2,1,9,9,2,1,1,1},	// 345类反向
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,2,2,1,1,1},
		{1,3,9,2,1,3,9,2,1,3,9,2,1,3,9,3,1,1,1},
	};
	// 城堡初始资源计算
	public static int[][] initResSpeed;
	public static int[][] initResBlock;		// 每种资源的块数
	static {
		int cnt = baseBuildRes.length;
		initResSpeed = new int[cnt][];
		initResBlock = new int[cnt][];
		for(int i = 0;i < cnt;i++) {
			initResSpeed[i] = new int[4];
			initResBlock[i] = new int[4];
			int[] base = baseBuildRes[i];
			for(int j = 1;j < base.length;j++) {
				switch(base[j]) {
				case 1:
					initResSpeed[i][3] += RES_BASE;
					initResBlock[i][3] += 1;
					break;
				case 2:
					initResSpeed[i][2] += RES_BASE;
					initResBlock[i][2] += 1;
					break;
				case 3:
					initResSpeed[i][0] += RES_BASE;
					initResBlock[i][0] += 1;
					break;
				case 9:
					initResSpeed[i][1] += RES_BASE;
					initResBlock[i][1] += 1;
					break;
				}
			}
		}
	}
	// 绿洲自身资源产量
	public static int[][] oasisResSpeed = {
		{0,0,0,0},
		{40,10,10,10},	// 第一种绿洲
		{10,40,10,10},
		{10,10,40,10},
		{40,10,10,40},	// 粮食+资源
		{10,40,10,40},
		{10,10,40,40},
		{10,10,10,40},
		{10,10,10,80},
	};
	// 绿洲的产量加成
	public static int[][] oasisRes = {
		{0,0,0,0},
		{25,0,0,0},	// 第一种绿洲
		{0,25,0,0},
		{0,0,25,0},
		{25,0,0,25},	// 粮食+资源
		{0,25,0,25},
		{0,0,25,25},
		{0,0,0,25},
		{0,0,0,50},
	};
	// 绿洲描述（占领前）
	public static String[] oasisInfo = {
		"",
		"产木材",
		"产石头",
		"产铁块",
		"产木材,产粮食",
		"产石头,产粮食",
		"产铁块,产粮食",
		"产粮食",
		"产粮食,产粮食",
	};
	//	 绿洲描述（占领后）
	public static String[] oasisInfo2 = {
		"",
		"木材产量+25%",
		"石头产量+25%",
		"铁块产量+25%",
		"木材产量+25%,粮食产量+25%",
		"石头产量+25%,粮食产量+25%",
		"铁块产量+25%,粮食产量+25%",
		"粮食产量+25%",
		"粮食产量+50%",
	};
	// 指定不同的建筑类型需要的集结点等级
	public static int[] gatherOpt = {
		100, 100, 100, 100, 19, 11, 8, 100, 10, 100,
		100, 5, 5, 7, 7, 7, 11, 10, 10, 11,
		100, 100, 9, 12, 13, 9, 100, 14, 100, 100,
		16, 17, 100, 100, 100, 15, 15, 18, 10, 10,
		10, 10, 100, 100, 100, 100, 100, 100, 100, 100,
	};
	// 每个等级来进攻的纳塔军队
	public static String[] natarAttacks = {
		null, null, null, null, null,
		"0,6402,5280,7798,6666,0,17,0,0,0",
		null, null, null, null,
		"0,7042,5913,8577,7465,0,19,0,0,0",
		null, null, null, null,
		"0,7746,6623,9435,8361,0,22,0,0,0",
		null, null, null, null,
		"0,8521,7418,10379,9365,0,26,0,0,0",
		null, null, null, null,
		"0,9373,8308,11417,10489,0,30,0,0,0",
		null, null, null, null,
		"0,10310,9305,12558,11747,0,35,0,0,0",
		null, null, null, null,
		"0,11341,10421,13814,13157,0,41,0,0,0",
		null, null, null, null,
		"0,12475,11672,15196,14736,0,48,0,0,0",
		null, null, null, null,
		"0,13723,13073,16715,16504,0,55,0,0,0",
		null, null, null, null,
		"0,15095,14641,18387,18485,0,64,0,0,0",
		
		null, null, null, null,
		"0,16605,16398,20226,20703,0,74,0,0,0",
		null, null, null, null,
		"0,18265,18366,22248,23188,0,85,0,0,0",
		null, null, null, null,
		"0,20092,20570,24473,25970,0,97,0,0,0",
		null, null, null, null,
		"0,22101,23039,26920,29087,0,110,0,0,0",
		null, null, null, null,
		"0,24311,25803,29612,32577,0,124,0,0,0",
		null, null, null, null,
		"0,26742,28900,32574,36486,0,139,0,0,0",
		null, null, null, null,
		"0,29417,32368,35831,40865,0,155,0,0,0",
		null, null, null, null,
		"0,32358,36252,39414,45769,0,172,0,0,0",
		null, null, null, null,
		"0,35594,40603,43356,51261,0,190,0,0,0",
		"0,39154,45475,47691,57412,0,210,0,0,0",
		"0,43069,50932,52461,64302,0,330,0,0,0",
		"0,47376,57044,57707,72018,0,383,0,0,0",
		"0,52114,63889,63477,80660,0,445,0,0,0",
		null,
	};
	
	public static String[] natarAttacks2 = {
		null, null, null, null, null,
		"0,131,0,455,201,0,48,31,0,0",
		null, null, null, null,
		"0,146,0,500,225,0,53,35,0,0",
		null, null, null, null,
		"0,164,0,550,252,0,60,39,0,0",
		null, null, null, null,
		"0,184,0,605,282,0,67,45,0,0",
		null, null, null, null,
		"0,206,0,666,316,0,75,51,0,0",
		null, null, null, null,
		"0,230,0,732,354,0,84,58,0,0",
		null, null, null, null,
		"0,258,0,806,396,0,94,66,0,0",
		null, null, null, null,
		"0,289,0,886,444,0,106,75,0,0",
		null, null, null, null,
		"0,324,0,975,497,0,118,85,0,0",
		null, null, null, null,
		"0,363,0,1072,557,0,133,96,0,0",
		
		null, null, null, null,
		"0,406,0,1180,624,0,149,108,0,0",
		null, null, null, null,
		"0,455,0,1298,699,0,166,120,0,0",
		null, null, null, null,
		"0,510,0,1427,783,0,187,135,0,0",
		null, null, null, null,
		"0,571,0,1570,877,0,209,150,0,0",
		null, null, null, null,
		"0,640,0,1727,982,0,234,165,0,0",
		null, null, null, null,
		"0,717,0,1900,1100,0,262,180,0,0",
		null, null, null, null,
		"0,803,0,2090,1232,0,294,195,0,0",
		null, null, null, null,
		"0,899,0,2299,1380,0,329,210,0,0",
		null, null, null, null,
		"0,1007,0,2529,1545,0,369,225,0,0",
		"0,1128,0,2782,1731,0,413,240,0,0",
		"0,1263,0,3061,1938,0,463,390,0,0",
		"0,1415,0,3367,2171,0,518,442,0,0",
		"0,1585,0,3703,2432,0,580,502,0,0",
		null,
	};
}
