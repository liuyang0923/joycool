package net.joycool.wap.spec.farm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.SqlUtil;

/**
 * @author zhouj
 * @explain： NPC
 * @datetime:1007-10-24
 */
public class FarmNpcWorld {
	public static FarmNpcWorld one = new FarmNpcWorld();
	
	public boolean loaded = false;
	public static NpcService service = new NpcService();
	
	public static List factoryList;		// 工厂列表
	static Map factoryMap;
	public static List factoryComposeList;
	public static Map factoryComposeMap;
	static Map factoryComposeListMap;
	
	public static List questList;
	static Map questMap;
	static Map talkMap;
	public static List talkList;
	
	static Map creatureTMap;		// 怪物模板
	public static List creatureTList;	
	
	static Map creatureSpawnMap;		// 怪物生长
	public static List creatureSpawnList;	
	
	public static List shopList;
	static Map shopMap;		// 所有shop物品
	static Map shopListMap;	// 一个npc的物品
	static Map shopMapMap;
	static Map npcMap;
	public static List npcList;
	
	public Map objectMap;	// 所有farmobject的map，包括creature
	public List dropList;	// 所有掉落物品
	
	public Map stoneMap;		// 地图上的特种石头
	public List stoneList;
	
	public Map carMap;		// 驿站
	public List carList;
	
	public SimpleGameLog log = new SimpleGameLog();
	
	public static ICacheMap factoryProcCache = CacheManage.factoryProc;
	public static ICacheMap userQuestCache = CacheManage.farmUserQuest;
	
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
	public static FarmNpcWorld getWorld() {
		one.prepare();
		return one;
	}
	
	public void load() {
		talkList = service.getTalkList("1 order by id");
		talkMap = new HashMap();
		for(int i = 0;i < talkList.size();i++) {
			FarmTalkBean bean = (FarmTalkBean)talkList.get(i);
			talkMap.put(Integer.valueOf(bean.getId()), bean);
		}
		for(int i = 0;i < talkList.size();i++) {
			FarmTalkBean bean = (FarmTalkBean)talkList.get(i);
			bean.initLink(this);
		}
		
		factoryList = service.getFactoryList("1 order by id");
		factoryMap = new HashMap();
		for(int i = 0;i < factoryList.size();i++) {
			FactoryBean bean = (FactoryBean)factoryList.get(i);
			factoryMap.put(Integer.valueOf(bean.getId()), bean);
			bean.getProcessList().addAll(service.getFactoryProcList("factory_id=" + bean.getId() + " and status=0"));
			
			FarmWorld.nodeAddObj(bean.getPos(), bean);
		}
		
		factoryComposeList = service.getFactoryComposeList("1 order by factory_id,rank,id");
		factoryComposeMap = new HashMap();
		for(int i = 0;i < factoryComposeList.size();i++) {
			FactoryComposeBean bean = (FactoryComposeBean)factoryComposeList.get(i);
			factoryComposeMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		questList = service.getQuestList("1");
		questMap = new HashMap();
		for(int i = 0;i < questList.size();i++) {
			FarmQuestBean bean = (FarmQuestBean)questList.get(i);
			questMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		npcList = service.getNpcList("1 order by id");
		npcMap = new HashMap();
		for(int i = 0;i < npcList.size();i++) {
			FarmNpcBean bean = (FarmNpcBean)npcList.get(i);
			npcMap.put(Integer.valueOf(bean.getId()), bean);
			FarmWorld.nodeAddObj(bean.getPos(), bean);
			
			bean.initTalk(this);
		}
		
		shopList = service.getShopList("1");
		shopMap = new HashMap();		// 所有shop物品
		for(int i = 0;i < shopList.size();i++) {
			FarmShopBean bean = (FarmShopBean)shopList.get(i);
			shopMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		creatureTList = service.getCreatureTList("1");
		creatureTMap = new HashMap();
		for(int i = 0;i < creatureTList.size();i++) {
			CreatureTBean bean = (CreatureTBean)creatureTList.get(i);
			creatureTMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		objectMap = new HashMap();
		dropList = new LinkedList();
		
		creatureSpawnList = service.getCreatureSpawnList("1");
		creatureSpawnMap = new HashMap();
		for(int i = 0;i < creatureSpawnList.size();i++) {
			CreatureSpawnBean bean = (CreatureSpawnBean)creatureSpawnList.get(i);
			creatureSpawnMap.put(Integer.valueOf(bean.getId()), bean);
			processCreatureSpawn(bean);
		}
		
		stoneList = service.getStoneList("1");
		stoneMap = new HashMap();
		for(int i = 0;i < stoneList.size();i++) {
			FarmStoneBean bean = (FarmStoneBean)stoneList.get(i);
			stoneMap.put(Integer.valueOf(bean.getId()), bean);
			FarmWorld.nodeAddObj(bean);
		}
		
		carList = service.getCarList("1");
		carMap = new HashMap();
		for(int i = 0;i < carList.size();i++) {
			FarmCarBean bean = (FarmCarBean)carList.get(i);
			carMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		shopListMap = new HashMap();
		shopMapMap = new HashMap();
		factoryComposeListMap = new HashMap();
		
	}
	
	public static long dropDispTime = 10 * 60000l;		// 掉落物品消失时间
	// 一分钟跑一次
	public void task(int count) {
		if(!loaded)
			return;
		long now = System.currentTimeMillis();
		
		for(int i = 0;i < factoryList.size();i++) {
			FactoryBean factory = (FactoryBean)factoryList.get(i);
			if(count % factory.getInterval() == 0) {
				processFactory(factory);
			}
		}
		
		for(int i = 0;i < shopList.size();i++) {
			FarmShopBean shop = (FarmShopBean)shopList.get(i);
			if(shop.getBrush() > 0 && count % shop.getBrush() == 0) {
				processShop(shop);
			}
		}
//		
//		for(int i = 0;i < creatureSpawnList.size();i++) {
//			CreatureSpawnBean cs = (CreatureSpawnBean)creatureSpawnList.get(i);
//			if(count % 1 == 0 && !cs.isFlagClosed()) {
//				processCreatureSpawn(cs);
//			}
//		}
		
		if(count % 10 == 0 && dropList.size() > 10) {
			synchronized(dropList) {
				Iterator iter = dropList.iterator();
				while(iter.hasNext()) {
					FarmDropBean drop = (FarmDropBean)iter.next();
					if(now - drop.getCreateTime() > dropDispTime) {
						iter.remove();
						FarmWorld.nodeRemoveDrop(drop);
					}
				}
			}
		}
	}
	
	public void spawn(CreatureBean creatureOld) {
		CreatureSpawnBean cs = creatureOld.getSpawn();
		CreatureTBean t = getCreatureT(cs.getTemplateId());

		CreatureBean creature = t.create();
		creature.setSpawn(cs);
		creature.setLifeTime(FarmAction.now + cs.getCooldown());
		if(cs.isFlagRandomPos()) {
			Integer pos = (Integer)RandomUtil.randomObject(cs.getPosList());
			creature.setPos(pos.intValue());
		} else {
			creature.setPos(creatureOld.getPos());
		}
		FarmWorld.nodeAddObj(creature);
		addObject(creature);
	}
	
	public void processCreatureSpawn(CreatureSpawnBean cs) {
		if(cs.isFlagClosed())
			return;
		CreatureTBean t = getCreatureT(cs.getTemplateId());
		if(t == null)
			return;
		if(cs.isFlagRandomPos()) {
			for(int i = 0;i < cs.getCount();i++) {
				CreatureBean creature = t.create();
				creature.setSpawn(cs);
				Integer pos = (Integer)RandomUtil.randomObject(cs.getPosList());
				creature.setPos(pos.intValue());	
				FarmWorld.nodeAddObj(creature);
				addObject(creature);
			}
		} else {
			for(int i = 0;i < cs.getPosList().size();i++) {
				Integer pos = (Integer)cs.getPosList().get(i);
				CreatureBean creature = t.create();
				creature.setSpawn(cs);
				creature.setPos(pos.intValue());	
				FarmWorld.nodeAddObj(creature);
				addObject(creature);
			}
		}
	}
	// 一定时间执行一次加工
	public void processFactory(FactoryBean factory) {
		List ps = factory.getProcessList();
		Iterator iter = ps.iterator();
		factory.growTime();
		while(iter.hasNext()) {
			FactoryProcBean proc = (FactoryProcBean)iter.next();
			FactoryComposeBean compose = getFactoryCompose(proc.getComposeId());

			if(factory.getTimeLeft() >= compose.getTime()) {		// 时间足够，处理之
				factory.decTime(compose.getTime());
				iter.remove();
				SqlUtil.executeUpdate("update farm_factory_proc set status=1,done_time=now() where id=" + proc.getId(), 4);
			} else {
				return;
			}
		}
		factory.setTimeLeft(0);		// 所有任务完成，时间归零
	}
//	 每小时更新所有物品的价格和库存，如果该物品价格和库存是浮动……
	public void processShop(FarmShopBean shop) {
		shop.updatePriceStack();
	}
	
	public FactoryBean getFactory(int id) {
		return (FactoryBean)factoryMap.get(Integer.valueOf(id));
	}
	
	public FactoryComposeBean getFactoryCompose(int id) {
		return (FactoryComposeBean)factoryComposeMap.get(Integer.valueOf(id));
	}
	
	public FarmQuestBean getQuest(int id) {
		return getQuest(Integer.valueOf(id));
	}
	public FarmQuestBean getQuest(Integer key) {
		return (FarmQuestBean)questMap.get(key);
	}
	
	public FarmTalkBean getTalk(int id) {
		return getTalk(Integer.valueOf(id));
	}
	public FarmTalkBean getTalk(Integer key) {
		return (FarmTalkBean)talkMap.get(key);
	}
	
	public FarmNpcBean getNpc(int id) {
		return (FarmNpcBean)npcMap.get(Integer.valueOf(id));
	}
	
	public FarmObject getObject(int id) {
		return (FarmObject)objectMap.get(Integer.valueOf(id));
	}
	
	public CreatureTBean getCreatureT(int id) {
		return (CreatureTBean)creatureTMap.get(Integer.valueOf(id));
	}
	public CreatureSpawnBean getCreatureSpawn(int id) {
		return (CreatureSpawnBean)creatureSpawnMap.get(Integer.valueOf(id));
	}
	
	public FarmStoneBean getStone(int id) {
		return (FarmStoneBean)stoneMap.get(Integer.valueOf(id));
	}
	public FarmCarBean getCar(int id) {
		return (FarmCarBean)carMap.get(Integer.valueOf(id));
	}
	
	public synchronized void addObject(FarmObject obj) {
		objectMap.put(Integer.valueOf(obj.getGid()), obj);
	}
	public synchronized void removeObject(FarmObject obj) {
		objectMap.remove(Integer.valueOf(obj.getGid()));
	}
	
	// 根据npc id获得其买卖列表
	public List getShopList(int npcId) {
		Integer key = Integer.valueOf(npcId);
		List list2 = (List) shopListMap.get(key);
		if (list2 == null) {
			List list = SqlUtil.getIntList("select id from farm_shop where npc_id=" + npcId + " order by id", 4);
			if(list != null) {
				list2 = new ArrayList();
				for(int i = 0;i < list.size();i++) {
					Integer iid = (Integer)list.get(i);
					list2.add(getShop(iid));
				}
				shopListMap.put(key, list2);
			}
		}
		return list2;
	}
	// 一个npc的买卖根据物品做map
	public Map getShopMap(int npcId) {
		Integer key = Integer.valueOf(npcId);
		Map map = (Map) shopMapMap.get(key);
		if (map == null) {
			List list = SqlUtil.getIntList("select id from farm_shop where npc_id=" + npcId + " order by id", 4);
			
			if(list != null) {
				map = new HashMap();
				for(int i = 0;i < list.size();i++) {
					Integer iid = (Integer)list.get(i);
					FarmShopBean shop = getShop(iid);
					map.put(Integer.valueOf(shop.getItemId()), shop);
				}
				shopMapMap.put(key, map);
			}
		}
		return map;
	}
	
	public FarmShopBean getShop(Integer iid) {
		return (FarmShopBean) shopMap.get(iid);
	}
	public FarmShopBean getShop(int id) {
		return getShop(Integer.valueOf(id));
	}
	
	public List getFactoryComposeList(int id) {
		Integer key = Integer.valueOf(id);
		List list = (List) factoryComposeListMap.get(key);
		if (list == null) {
			list = service.getFactoryComposeList("factory_id=" + id);
			if(list != null) {
				factoryComposeListMap.put(key, list);
			}
		}
		return list;
	}

	public void addFactoryProc(FactoryComposeBean compose, int userId) {
		FactoryProcBean proc = new FactoryProcBean();
		proc.setUserId(userId);
		proc.setComposeId(compose.getId());
		proc.setFactoryId(compose.getFactoryId());
		proc.setCreateTime(System.currentTimeMillis());
		service.addFactoryProc(proc);
		FactoryBean factory = getFactory(compose.getFactoryId());
		factory.addProc(proc);
	}

	public FactoryProcBean getFactoryProc(int id) {
		Integer key = Integer.valueOf(id);
		synchronized(factoryProcCache) {
			FactoryProcBean proc = (FactoryProcBean) factoryProcCache.get(key);
			if (proc == null) {
				proc = service.getFactoryProc("id=" + id);
				if(proc != null) {
					factoryProcCache.put(key, proc);
				}
			}
			return proc;
		}
	}
	
	public FarmUserQuestBean getUserQuest(int id) {
		return getUserQuest(Integer.valueOf(id));
	}
	public FarmUserQuestBean getUserQuest(Integer key) {
		synchronized(userQuestCache) {
			FarmUserQuestBean bean = (FarmUserQuestBean) userQuestCache.get(key);
			if (bean == null) {
				bean = service.getUserQuest("id=" + key);
				if(bean != null) {
					userQuestCache.put(key, bean);
				}
			}
			return bean;
		}
	}

	// 接收任务啦
	public void addUserQuest(FarmUserBean farmUser, FarmQuestBean quest) {
		FarmUserQuestBean userQuest = new FarmUserQuestBean();
		userQuest.setUserId(farmUser.getUserId());
		userQuest.setQuestId(quest.getId());
		userQuest.setNpcId(farmUser.getNpcId());

		service.addUserQuest(userQuest);
		userQuestCache.put(Integer.valueOf(userQuest.getId()), userQuest);
		farmUser.beginQuest(userQuest);
	}
	
	// 拒绝任务啦
	public void removeUserQuest(FarmUserBean farmUser, FarmUserQuestBean userQuest) {
		SqlUtil.executeUpdate("delete from farm_user_quest where id=" + userQuest.getId(), 4);
		userQuestCache.srm(Integer.valueOf(userQuest.getId()));
		farmUser.removeQuest(userQuest);
	}
	
	// 完成任务啦
	public void endUserQuest(FarmUserBean farmUser, FarmUserQuestBean userQuest) {
		SqlUtil.executeUpdate("update farm_user_quest set status=1,done_time=now() where id=" + userQuest.getId(), 4);
		userQuest.setStatus(1);
		farmUser.endQuest(userQuest);
	}
	
	public static String getQuestObject(FarmQuestBean quest) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < quest.getMaterialList().size();i++) {
			int[] tmp = (int[])quest.getMaterialList().get(i);
			sb.append(UserBagCacheUtil.getItem(tmp[0]).getName());
			sb.append("x");
			sb.append(tmp[1]);
			sb.append("<br/>");
		}
		if(quest.getPrice() > 0) {
			sb.append(FarmWorld.formatMoney(quest.getPrice()));
			sb.append("<br/>");
		}
		return sb.toString();
	}
	
	// 判断一个玩家是否能做某个任务，根据各种限制和先修任务等条件
	public static boolean canDoQuest(FarmUserBean farmUser, FarmQuestBean quest) {
		for(int i = 0;i < quest.getPreQuestList().size();i++) {
			Integer iid = (Integer)quest.getPreQuestList().get(i);
			if(!farmUser.isQuestEnd(iid))
				return false;
		}
		for(int i = 0;i < quest.getMutexList().size();i++) {
			Integer iid = (Integer)quest.getMutexList().get(i);
			if(farmUser.isQuestEnd(iid) || farmUser.isQuestStart(iid.intValue()))
				return false;
		}
		if(!FarmWorld.isCondition(quest.getPreConditionList(), farmUser))
			return false;
		return farmUser.getRank() >= quest.getRank() - 1;
	}
	
	// 根据creature tmplate创建一个新的creature
	public CreatureBean newCreature(CreatureTBean template, int pos) {
		CreatureBean creature = template.create();
		creature.setPos(pos);
		addObject(creature);
		FarmWorld.nodeAddObj(creature);
		return creature;
	}
	// 杀一个怪之后设置玩家任务完成情况
	public static void checkQuestCreature(FarmUserBean farmUser, int creatureId) {
		Iterator iter = farmUser.getQuestCreatureFinish().iterator();
		while(iter.hasNext()) {
			int[] cr = (int[])iter.next();
			if(creatureId == cr[1]) {
				cr[2]++;
				break;
			}
		}
	}
	// 杀一个怪之后设置玩家任务完成情况
	public static void checkQuestTalk(FarmUserBean farmUser, int talkId) {
		Iterator iter = farmUser.getQuestTalkFinish().iterator();
		while(iter.hasNext()) {
			int[] cr = (int[])iter.next();
			if(talkId == cr[1]) {
				cr[2]++;
				break;
			}
		}
	}
	
	// 用于后台的npc修改
	public void updateNpc(FarmNpcBean bean) {
		service.updateNpc(bean, false);
	}
	public void addNpc(FarmNpcBean bean) {
		if(!service.updateNpc(bean, true))
			return;
		npcMap.put(Integer.valueOf(bean.getId()), bean);
		npcList.add(bean);
	}
	
//	 用于后台的npc talk修改
	public void updateTalk(FarmTalkBean bean) {
		service.updateTalk(bean, false);
	}
	public void addTalk(FarmTalkBean bean) {
		if(!service.updateTalk(bean, true))
			return;
		talkMap.put(Integer.valueOf(bean.getId()), bean);
		talkList.add(bean);
	}
	
//	 用于后台的quest修改
	public void updateQuest(FarmQuestBean bean) {
		service.updateQuest(bean, false);
	}
	public void addQuest(FarmQuestBean bean) {
		if(!service.updateQuest(bean, true))
			return;
		questMap.put(Integer.valueOf(bean.getId()), bean);
		questList.add(bean);
	}
	
//	 用于后台的factory修改
	public void updateFactory(FactoryBean bean) {
		service.updateFactory(bean, false);
	}
	public void addFactory(FactoryBean bean) {
		if(!service.updateFactory(bean, true))
			return;
		factoryMap.put(Integer.valueOf(bean.getId()), bean);
		factoryList.add(bean);
	}
	
//	 用于后台的factory compose修改
	public void updateFactoryCompose(FactoryComposeBean bean) {
		service.updateFactoryCompose(bean, false);
		factoryComposeListMap.clear();
	}
	public void addFactoryCompose(FactoryComposeBean bean) {
		if(!service.updateFactoryCompose(bean, true))
			return;
		factoryComposeMap.put(Integer.valueOf(bean.getId()), bean);
		factoryComposeList.add(bean);
		factoryComposeListMap.clear();
	}
//	 用于后台的creature spawn修改
	public void updateCreatureSpawn(CreatureSpawnBean bean) {
		service.updateCreatureSpawn(bean, false);
	}
	public void addCreatureSpawn(CreatureSpawnBean bean) {
		if(!service.updateCreatureSpawn(bean, true))
			return;
		creatureSpawnMap.put(Integer.valueOf(bean.getId()), bean);
		creatureSpawnList.add(bean);
	}
//	 用于后台的creature template修改
	public void updateCreatureT(CreatureTBean bean) {
		service.updateCreatureT(bean, false);
	}
	public void addCreatureT(CreatureTBean bean) {
		if(!service.updateCreatureT(bean, true))
			return;
		creatureTMap.put(Integer.valueOf(bean.getId()), bean);
		creatureTList.add(bean);
	}
	
//	 用于后台的stone修改
	public void updateStone(FarmStoneBean bean) {
		service.updateStone(bean, false);
	}
	public void addStone(FarmStoneBean bean) {
		if(!service.updateStone(bean, true))
			return;
		stoneMap.put(Integer.valueOf(bean.getId()), bean);
		stoneList.add(bean);
	}
	
//	 用于后台的驿站路线修改
	public void updateCar(FarmCarBean bean) {
		service.updateCar(bean, false);
	}
	public void addCar(FarmCarBean bean) {
		if(!service.updateCar(bean, true))
			return;
		carMap.put(Integer.valueOf(bean.getId()), bean);
		carList.add(bean);
	}
	
//	 用于后台的npc shop买卖物品修改
	public void updateShop(FarmShopBean bean) {
		service.updateShop(bean, false);
		shopListMap.clear();
		shopMapMap.clear();
	}
	public void addShop(FarmShopBean bean) {
		if(!service.updateShop(bean, true))
			return;
		shopMap.put(Integer.valueOf(bean.getId()), bean);
		shopList.add(bean);
		shopListMap.clear();
		shopMapMap.clear();
	}
	public void killCreature(CreatureBean creature) {
		removeObject(creature);
		FarmWorld.nodeRemoveObj(creature);
		spawn(creature);
	}
	public static boolean checkQuestFinish(FarmUserBean farmUser, FarmQuestBean quest) {
		if(quest.getCreatureList().size() > 0) {
			for(int i = 0;i < quest.getCreatureList().size();i++) {
				int[] tmp = (int[])quest.getCreatureList().get(i);
				int finish = farmUser.getCreatureFinishCount(tmp[0]);
				if(finish < tmp[1])
					return false;
			}
		}
		return true;
	}
	public static void updateUserBindPos(FarmUserBean farmUser) {
		SqlUtil.executeUpdate("update farm_user set bind_pos=" + farmUser.getPos() + " where user_id=" + farmUser.getUserId(), 4);
		farmUser.setBindPos(farmUser.getPos());
	}
	public static List getQuestBeginNpc(int id) {
		List list = new ArrayList();
		for(int i = 0;i < npcList.size();i++) {
			FarmNpcBean npc = (FarmNpcBean)npcList.get(i);
			if(npc.hasQuestBegin(id))
				list.add(npc);
		}
		return list;
	}
	public static List getQuestEndNpc(int id) {
		List list = new ArrayList();
		for(int i = 0;i < npcList.size();i++) {
			FarmNpcBean npc = (FarmNpcBean)npcList.get(i);
			if(npc.hasQuestEnd(id))
				list.add(npc);
		}
		return list;
	}
}
