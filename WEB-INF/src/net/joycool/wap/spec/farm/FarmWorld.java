package net.joycool.wap.spec.farm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.service.impl.DummyServiceImpl;
import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.cache.*;
import net.joycool.wap.framework.Integer2;

/**
 * @author zhouj
 * @explain： 采集
 * @datetime:1007-10-24
 */
public class FarmWorld {
	static int MAX_NODE_OBJ = 10;		// 一个结点最多的物体数量
	public static int MAX_BATTLE_ATTR = 5;	// 战斗属性的个数，最多5个
	static DummyServiceImpl dummyService = new DummyServiceImpl();
	
	static ICacheMap farmFieldCache = CacheManage.farmField;
	static ICacheMap farmFeedCache = CacheManage.farmFeed;
	static ICacheMap farmUserCache = CacheManage.farmUser;
	static ICacheMap userCollectCache = CacheManage.farmUserCollect;
	static ICacheMap userCollectListCache = CacheManage.farmUserCollectList;
	static ICacheMap userHonorCache = CacheManage.farmUserHonor;
	static ICacheMap userHonorListCache = CacheManage.farmUserHonorList;
	
	public static ICacheMap userTriggerCache = CacheManage.farmUserTrigger;
	
	public static FarmWorld one = new FarmWorld();
	public static HashMap groupUserMap = new HashMap();
	
	public boolean loaded = false;
	public static FarmService service = new FarmService();
	public static NpcService nService = new NpcService();
	public static TongService tService = new TongService();
	
	public SimpleGameLog log = new SimpleGameLog();
	
	public List cropList;
	public HashMap cropMap;		// 作物
	
	public List proList;
	public HashMap proMap;		// 专业技能
	
	public List skillList;
	public HashMap skillMap;		// 专业的各个技能
	
	public List landItemList;		// 地图上能长的采集物品
	public HashMap landItemMap;
	
	public List bookList;;		// 书
	public HashMap bookMap;
	
	public List nodeList;
	public HashMap nodeMap;		// map node map，地图结点
	public List signList;
	public HashMap signMap;		// map sign，地图路标
	
	public List mapList;
	public HashMap mapMap;		// 地图大区域
	
	public List pickList;
	public HashMap pickMap;		// 大地图采集
	
	public List landList;
	public HashMap landMap;
	
	public List triggerList;		// 触发器
	public HashMap triggerMap;
	
	public List collectList;		// 收藏盒列表
	public HashMap collectMap;
	public HashMap collectItemMap;	// 格式 (type,物品id)-(收藏盒id,index)
	
	public List itemSetList;		// 套装
	public HashMap itemSetMap;
	public HashMap itemToSetMap;		// 一个物品对应的套装
	
	public List pickTList;		// 大地图采集矿物药草
	public HashMap pickTMap;
	
	public static int MAX_GRADE_COUNT = 6;
	public static int MAX_ITEM_RANK = 60;
	public static List[][] dropCollection = null;		// 所有的可掉落物品，[级别][品质]
	public static List[] dropCollection2 = null;		// 符文和水晶的掉落
	
	byte[] lock = new byte[0];
	public void prepare() {
		if(!loaded) {
			synchronized(lock) {
				if(!loaded) {
					load();
					FarmNpcWorld.one.prepare();
					loaded = true;
					
					SimpleDataBean.addLink("桃源钱庄","bank.jsp", 35, true);
					SimpleDataBean.addLink("恶人榜","top/arena.jsp", 212, true);
					SimpleDataBean.addLink("恶人榜","top/arena.jsp", 768, true);
					SimpleDataBean.addLink("恶人榜","top/arena.jsp", 6, true);
					SimpleDataBean.addLink("初级擂台榜","top/arena.jsp?id=49", 4901, true);
					SimpleDataBean.addLink("中级擂台榜","top/arena.jsp?id=48", 4947, true);
					SimpleDataBean.addLink("高级擂台榜","top/arena.jsp?id=47", 4949, true);
					SimpleDataBean.addLink("顶级擂台榜","top/arena.jsp?id=149", 14914, true);
				}
				task(0);
			}
		}
		FarmAction.now = System.currentTimeMillis();
	}
	public static FarmWorld getWorld() {
		one.prepare();
		return one;
	}
	public static FarmNpcWorld getNpcWorld() {
		one.prepare();
		return FarmNpcWorld.one;
	}
	static byte[] bagLock = UserBagCacheUtil.getBagLock();
	
	public void load() {
		cropList = service.getCropList("1");
		cropMap = new HashMap();
		for(int i = 0;i < cropList.size();i++) {
			FarmCropBean bean = (FarmCropBean)cropList.get(i);
			cropMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		skillList = service.getSkillList("1");
		skillMap = new HashMap();
		for(int i = 0;i < skillList.size();i++) {
			FarmSkillBean bean = (FarmSkillBean)skillList.get(i);
			skillMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		landItemList = service.getLandItemList("1");
		landItemMap = new HashMap();
		for(int i = 0;i < landItemList.size();i++) {
			LandItemBean bean = (LandItemBean)landItemList.get(i);
			landItemMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		proList = service.getProList("1");
		proMap = new HashMap();
		for(int i = 0;i < proList.size();i++) {
			FarmProBean bean = (FarmProBean)proList.get(i);
			proMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		nodeList = service.getMapNodeList("1");
		nodeMap = new HashMap();
		for(int i = 0;i < nodeList.size();i++) {
			MapNodeBean bean = (MapNodeBean)nodeList.get(i);
			nodeMap.put(Integer.valueOf(bean.getId()), bean);
		}
		for(int i = 0;i < nodeList.size();i++) {
			MapNodeBean bean = (MapNodeBean)nodeList.get(i);
			bean.initLink(this);
		}
		generateMapCoord();
		signList = service.getMapSignList("1");
		signMap = new HashMap();
		for(int i = 0;i < signList.size();i++) {
			MapSignBean bean = (MapSignBean)signList.get(i);
			signMap.put(Integer.valueOf(bean.getId()), bean);
			bean.setNode(getMapNode(bean.getPosId()));
		}
		
		mapList = service.getMapList("1");
		mapMap = new HashMap();
		for(int i = 0;i < mapList.size();i++) {
			MapBean bean = (MapBean)mapList.get(i);
			mapMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		landList = service.getLandList("1");
		landMap = new HashMap();
		for(int i = 0;i < landList.size();i++) {
			LandMapBean bean = (LandMapBean)landList.get(i);
			landMap.put(Integer.valueOf(bean.getId()), bean);
			
			FarmWorld.nodeAddObj(bean.getPos(), bean);
		}
		
		bookList = service.getBookList("1");
		bookMap = new HashMap();
		for(int i = 0;i < bookList.size();i++) {
			FarmBookBean bean = (FarmBookBean)bookList.get(i);
			bookMap.put(Integer.valueOf(bean.getId()), bean);
			
			FarmWorld.nodeAddObj(bean.getPos(), bean);
		}
		
		pickList = service.getPickList("1");
		pickMap = new HashMap();
		for(int i = 0;i < pickList.size();i++) {
			MapPickBean bean = (MapPickBean)pickList.get(i);
			pickMap.put(Integer.valueOf(bean.getId()), bean);
			
			FarmWorld.nodeAddObj(bean.getPos(), bean);
		}
		
		pickTList = service.getPickTList("1");
		pickTMap = new HashMap();
		for(int i = 0;i < pickTList.size();i++) {
			PickTBean bean = (PickTBean)pickTList.get(i);
			pickTMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		pickTList = service.getPickTList("1");
		pickTMap = new HashMap();
		for(int i = 0;i < pickTList.size();i++) {
			PickTBean bean = (PickTBean)pickTList.get(i);
			pickTMap.put(Integer.valueOf(bean.getId()), bean);
		}
		
		triggerList = service.getTriggerList("1");
		triggerMap = new HashMap();
		for(int i = 0;i < triggerList.size();i++) {
			TriggerBean bean = (TriggerBean)triggerList.get(i);
			triggerMap.put(Integer.valueOf(bean.getId()), bean);
			setTrigger(bean);
		}
		
		collectList = service.getCollectList("1");
		collectMap = new HashMap();
		for(int i = 0;i < collectList.size();i++) {
			CollectBean bean = (CollectBean)collectList.get(i);
			collectMap.put(Integer.valueOf(bean.getId()), bean);
		}
		initCollectItem();
		
		itemSetList = service.getItemSetList("1");
		initItemSet();
		initDropCollection();
	}
	public void initCollectItem() {
		collectItemMap = new HashMap();
		for(int i = 0;i < collectList.size();i++) {
			CollectBean bean = (CollectBean)collectList.get(i);
			for(int j = 0;j < bean.getCount();j++) {
				int[] iid = {bean.getId(), j};
				int itemId = ((Integer)bean.getItemList().get(j)).intValue();
				collectItemMap.put(new Integer2(bean.getType(), itemId), iid);
			}
		}
	}
	public void initItemSet() {
		itemToSetMap = new HashMap();
		itemSetMap = new HashMap();
		for(int i = 0;i < itemSetList.size();i++) {
			ItemSetBean bean = (ItemSetBean)itemSetList.get(i);
			for(int j = 0;j < bean.getItemList().size();j++) {
				Integer iid = (Integer)bean.getItemList().get(j);
				itemToSetMap.put(iid, bean);
			}
			itemSetMap.put(Integer.valueOf(bean.getId()), bean);
		}
	}
	public void initDropCollection() {
		dropCollection = new List[MAX_ITEM_RANK][];
		dropCollection2 = new List[MAX_ITEM_RANK];
		for(int i = 0;i < MAX_ITEM_RANK;i++)
			dropCollection[i] = new List[MAX_GRADE_COUNT];
		// 把需要掉落的物品分类
		List itemList = dummyService.getDummyProductList("class1 > 0 and class1 != 10 and rank>0 order by id");
		for(int i = 0;i < itemList.size();i++) {
			DummyProductBean item = (DummyProductBean)itemList.get(i);
			if(item.isFlagNoDrop()) continue;
			if(item.getClass1() == 5) {		// 水晶和符文
				for(int j = item.getRank();j < MAX_ITEM_RANK;j++) {
					List ls = dropCollection2[j];
					if(ls == null)
						ls = dropCollection2[j] = new ArrayList();
					ls.add(Integer.valueOf(item.getId()));
				}
			} else {
				List ls = dropCollection[item.getRank() - 1][item.getGrade()];
				if(ls == null)
					dropCollection[item.getRank() - 1][item.getGrade()] = ls = new ArrayList();
				ls.add(Integer.valueOf(item.getId()));
	
				ls = dropCollection[item.getRank()][item.getGrade()];
				if(ls == null)
					dropCollection[item.getRank()][item.getGrade()] = ls = new ArrayList();
	
				ls.add(Integer.valueOf(item.getId()));
			}
		}
	}
	// 生成全地图坐标
	public void generateMapCoord() {
		generateMapCoord(1000);	// 避免死循环，最多1000层	
	}
	public void generateMapCoord(int level) {
		HashSet visited = new HashSet(8192);
		HashSet iterSet = new HashSet();
		MapNodeBean start = getMapNode(0);
		start.setX(100);
		start.setY(100);
		iterSet.add(start);

		while(--level >= 0) {
			HashSet curSet = new LinkedHashSet();
			Iterator iter = iterSet.iterator();
			while(iter.hasNext()) {
				MapNodeBean mi = (MapNodeBean)iter.next();
				
				MapNodeBean[] nodes = mi.getLinks();
				for(int i = 0;i < 9;i++) {
					int cx = mi.x + xgrid[i];
					int cy = mi.y + ygrid[i];
					if(nodes[i] != null && !visited.contains(nodes[i])) {
						if(level > 0) {
							visited.add(nodes[i]);
							nodes[i].setX(cx);
							nodes[i].setY(cy);
							curSet.add(nodes[i]);
						}
					}
				}
			}
			if(curSet.size() == 0)
				return;
			iterSet = curSet;
		}
	}
	public FarmCropBean getCrop(int id) {
		return (FarmCropBean)cropMap.get(Integer.valueOf(id));
	}
	
	public FarmProBean getPro(int id) {
		return (FarmProBean)proMap.get(Integer.valueOf(id));
	}
	
	public FarmSkillBean getSkill(int id) {
		return (FarmSkillBean)skillMap.get(Integer.valueOf(id));
	}
	
	public MapPickBean getPick(int id) {
		return (MapPickBean)pickMap.get(Integer.valueOf(id));
	}
	
	public ItemSetBean getItemSet(int id) {
		return (ItemSetBean)itemSetMap.get(Integer.valueOf(id));
	}
	public ItemSetBean getItemToSet(int itemId) {
		return (ItemSetBean)itemToSetMap.get(Integer.valueOf(itemId));
	}
	public PickTBean getPickT(int id) {
		return (PickTBean)pickTMap.get(Integer.valueOf(id));
	}
	
	public CollectBean getCollect(int id) {
		return (CollectBean)collectMap.get(Integer.valueOf(id));
	}
	public TriggerBean getTrigger(int id) {
		return (TriggerBean)triggerMap.get(Integer.valueOf(id));
	}
	
	public static String getUserName(int userId) {
		FarmUserBean user = one.getFarmUserCache(userId);
		if(user == null)
			return "未知目标";
		else
			return user.getNameWml();
	}
	// 只从缓存读
	public FarmUserBean getFarmUserCache(int userId) {
		Integer key = new Integer(userId);
		return (FarmUserBean) farmUserCache.sgt(key);
	}
	public FarmUserBean getFarmUserCache(Integer key) {
		return (FarmUserBean) farmUserCache.sgt(key);
	}
	
	public FarmUserBean getFarmUser(int userId) {
		Integer key = new Integer(userId);
		synchronized(farmUserCache) {
			FarmUserBean user = (FarmUserBean) farmUserCache.get(key);
			if (user == null) {	
				user = service.getFarmUser("user_id=" + userId);
				if(user == null) {
					return null;
				} else {
					service.getFarmUserPro(user);
					service.getFarmUserEquip(user);
					service.getFarmCooldown(user);
					user.setTongUser(FarmTongWorld.getTongUser(user.getUserId()));
					farmUserCache.put(key, user);
					user.setQuests(nService.getUserQuestList("user_id=" + userId + " and status=0"));
					for(int i = 0;i < user.getQuests().size();i++) {
						FarmUserQuestBean userQuest = (FarmUserQuestBean)user.getQuests().get(i);
						FarmQuestBean quest = FarmNpcWorld.one.getQuest(userQuest.getQuestId());
						if(quest != null)
							user.addQuestFinishStatus(quest);
					}
					List l = SqlUtil.getIntsList("select id,quest_id from farm_user_quest where user_id=" + userId + " and status=1 order by id", 4);
					for(int i = 0;i < l.size();i++) {
						int[] is = (int[])l.get(i);
						user.getEndQuests().put(Integer.valueOf(is[1]), Integer.valueOf(is[0]));
					}
					List l2 = SqlUtil.getIntsList("select id,trigger_id from farm_user_trigger where user_id=" + userId + " order by id", 4);
					for(int i = 0;i < l2.size();i++) {
						int[] is = (int[])l2.get(i);
						user.getEndTriggers().put(Integer.valueOf(is[1]), Integer.valueOf(is[0]));
					}
					user.updateQuestStatus();
					addOtherLog(user, user.getNameWml() + "突然出现在你的面前!");
					nodeAddPlayer(user.getPos(), user);
				}
				
			} else if(user.isFlagOffline()) {
				user.setFlag(0, false);
				addOtherLog(user, user.getNameWml() + "突然出现在你的面前!");
				nodeAddPlayer(user.getPos(), user);
			}
			return user;
		}
	}
	
	public FarmUserBean getAddFarmUser(int userId) {
		Integer key = new Integer(userId);
		synchronized(farmUserCache) {
			FarmUserBean user = (FarmUserBean) farmUserCache.get(key);
			if (user == null) {	
				user = service.getFarmUser("user_id=" + userId);
				if(user == null) {
					user = new FarmUserBean();
					user.setUserId(userId);
					user.setName("");
					service.addFarmUser(user);
				} else {
					service.getFarmUserPro(user);
					service.getFarmUserEquip(user);
					service.getFarmCooldown(user);
				}
				farmUserCache.put(key, user);
			}
			return user;
		}
	}
	
	public static UserCollectBean getUserCollect(int id) {
		Integer key = new Integer(id);
		synchronized(userCollectCache) {
			UserCollectBean bean = (UserCollectBean) userCollectCache.get(key);
			if (bean == null) {	
				bean = service.getUserCollect("id=" + id);
				if(bean != null) {
					userCollectCache.put(key, bean);
				}
			}
			return bean;
		}
	}
	
	public static List getUserCollectList(int userId) {
		Integer key = new Integer(userId);
		synchronized(userCollectListCache) {
			List list = (List) userCollectListCache.get(key);
			if (list == null) {	
				list = SqlUtil.getIntsList("select id,collect_id from farm_user_collect where user_id=" + userId, 4);
				if(list != null) {
					userCollectListCache.put(key, list);
				}
			}
			return list;
		}
	}
	// 判断用户是否有某个收藏盒，如果有，返回收藏盒id
	public static int getUserCollect(int userId, int collectId) {
		List list = getUserCollectList(userId);
		for(int i = 0;i < list.size();i++) {
			int[] ii = (int[])list.get(i);
			if(ii[1] == collectId)
				return ii[0];
		}
		return 0;
	}
//	 给用户添加收藏盒
	public static void addUserCollect(int userId, int collectId) {
		UserCollectBean uc = new UserCollectBean();
		uc.setUserId(userId);
		uc.setCollectId(collectId);
		uc.setStartTime(FarmAction.now);
		if(!service.addUserCollect(uc))
			return;
		int[] ii = {uc.getId(), collectId};
		List list = getUserCollectList(userId);
		list.add(ii);
		userCollectCache.spt(uc.getId(), uc);
	}
	// 给用户添加某个收藏，如果完成收藏，则写入finish_time
	public static void addUserCollectItem(UserCollectBean uc, int index) {
		uc.addCollect(index);
		CollectBean collect = one.getCollect(uc.getCollectId());
		if(uc.getCount() >= collect.getCount())
			SqlUtil.executeUpdate("update farm_user_collect set count=" + uc.getCount()
					+ ",collected=" + uc.getCollected() + ",finish_time=now() where id=" + uc.getId(), 4);
		else
			SqlUtil.executeUpdate("update farm_user_collect set count=" + uc.getCount()
					+ ",collected=" + uc.getCollected() + " where id=" + uc.getId(), 4);
	}
	// 给用户添加某个收藏
	public boolean addUserCollectItem(int userId, int itemId, int type) {
		Integer2 key = new Integer2(type, itemId);
		int[] iid = (int[])collectItemMap.get(key);
		int ucId = getUserCollect(userId, iid[0]);
		if(ucId == 0)
			return false;
		UserCollectBean uc = getUserCollect(ucId);
		synchronized(uc) {
			if(uc.hasCollect(iid[1]))
				return false;
			addUserCollectItem(uc, iid[1]);
		}
		return true;
	}
	// 查询用户荣誉
	public static UserHonorBean getUserHonor(int userId, int arena) {
		Integer2 key = new Integer2(userId, arena);
		synchronized(userHonorCache) {
			UserHonorBean bean = (UserHonorBean) userHonorCache.get(key);
			if (bean == null) {	
				bean = service.getUserHonor("user_id=" + userId + " and arena=" + arena);
				if(bean != null) {
					userHonorCache.put(key, bean);
				} else {
					bean = addUserHonor(userId, arena);
				}
			}
			return bean;
		}
	}
	public static UserHonorBean getUserHonor(int userId, MapBean map) {
		if(map.isFlagArena())
			return getUserHonor(userId, map.getId());
		return null;//getUserHonor(userId, 0);
	}
	
	public static List getUserHonorList(int userId) {
		Integer key = new Integer(userId);
		synchronized(userHonorListCache) {
			List list = (List) userHonorListCache.get(key);
			if (list == null) {	
				list = SqlUtil.getIntsList("select id,arena from farm_user_honor where user_id=" + userId, 4);
				if(list != null) {
					userHonorListCache.put(key, list);
				}
			}
			return list;
		}
	}
//	 给用户添加荣誉
	public static UserHonorBean addUserHonor(int userId, int arena) {
		UserHonorBean uh = new UserHonorBean();
		uh.setUserId(userId);
		uh.setArena(arena);
		uh.setCreateTime(FarmAction.now);
		if(!service.addUserHonor(uh))
			return null;
		int[] ii = {uh.getId(), arena};
		List list = getUserHonorList(userId);
		list.add(ii);
		userHonorCache.spt(new Integer2(userId, arena), uh);
		return uh;
	}
	public static void updateUserHonor(int winUserId, int loseUserId, int arena) {
		synchronized(userHonorCache) {
			UserHonorBean lose = getUserHonor(loseUserId, arena);
			UserHonorBean win = getUserHonor(winUserId, arena);
			if(win.getHonorWeek() - lose.getHonorWeek() >= 10)
				return;		// 荣誉相差10以上无效
			addUserHonorPoint(win, 1);
			addUserHonorPoint(lose, -1);
		}
	}
	public static void addUserHonorPoint(UserHonorBean userHonor, int add) {
		int res = userHonor.getHonorWeek() + add;
		if(res < 0)
			return;
		SqlUtil.executeUpdate("update farm_user_honor set honor_week=" + res + " where id=" + userHonor.getId(), 4);
		userHonor.setHonorWeek(res);
	}
	public static void updateUserHonor(FarmUserBean winUser, FarmUserBean loseUser) {
		MapNodeBean node = one.getMapNode(winUser.getPos());
		updateUserHonor(winUser.getUserId(), loseUser.getUserId(), one.getMap(node.getMapId()));
	}
	public static void updateUserHonor(int winUserId, int loseUserId, MapBean map) {
		if(map != null && map.isFlagArena())
			updateUserHonor(winUserId, loseUserId, map.getId());
		else
			updateUserHonor(winUserId, loseUserId, 0);
	}
	
	// 专业升级
	public static void addFUPExp(FarmUserProBean pro, int add) {
		if(pro.addExp(add))
			SqlUtil.executeUpdate("update farm_user_pro set exp=" + pro.getExp()
					+ " where id=" + pro.getId(), 4);
	}
	public static void addFUPRank(FarmUserProBean pro, int add) {
		pro.addRank(add);
		SqlUtil.executeUpdate("update farm_user_pro set rank=" + pro.getRank()
				+ " where id=" + pro.getId(), 4);
	}
	
	// 总经验值增加
	public static void addFUExp(FarmUserBean farmUser, int add) {
		int rank = farmUser.getRank();
		if(farmUser.addExp(add)) {
			SqlUtil.executeUpdate("update farm_user set exp=" + farmUser.getExp() + ",rank=" + farmUser.getRank()
					+ " where user_id=" + farmUser.getUserId(), 4);
			if(farmUser.getRank() > rank) {
				addProPoint(farmUser, 10);
			}
		}
	}
	
	public static void addProPoint(FarmUserBean user, int add) {
		user.addProPoint(add);
		SqlUtil.executeUpdate("update farm_user set pro_point=" + user.getProPoint()
				+ " where user_id=" + user.getUserId(), 4);
	}
	// 农业场地
	public static FarmFieldBean getField(Integer iid) {
		synchronized(farmFieldCache) {
			FarmFieldBean bean = (FarmFieldBean)farmFieldCache.get(iid);
			if(bean == null) {
				bean = service.getField("id=" + iid);
				if(bean != null)
					farmFieldCache.put(iid, bean);
			}
			return bean;
		}
	}
	public static FarmFieldBean getField(int id) {
		return getField(Integer.valueOf(id));
	}
	
	public static void addField(int userId) {
		FarmFieldBean field = new FarmFieldBean();
		field.setCropId(0);
		field.setUserId(userId);
		service.addField(field);
		farmFieldCache.spt(field.getId(), field);
	}
	// 畜牧业场地
	public static FarmFeedBean getFeed(Integer iid) {
		synchronized(farmFeedCache) {
			FarmFeedBean bean = (FarmFeedBean)farmFeedCache.get(iid);
			if(bean == null) {
				bean = service.getFeed("id=" + iid);
				if(bean != null)
					farmFeedCache.put(iid, bean);
			}
			return bean;
		}
	}
	public static FarmFeedBean getFeed(int id) {
		return getFeed(Integer.valueOf(id));
	}
	
	public static void addFeed(int userId) {
		FarmFeedBean bean = new FarmFeedBean();
		bean.setCropId(0);
		bean.setUserId(userId);
		service.addFeed(bean);
		farmFeedCache.spt(bean.getId(), bean);
	}
	
	// 增加或者减少用户的乐币
	public static void addMoney(FarmUserBean user, int add) {
		synchronized(user.getLock()) {
			user.addMoney(add);
			service.updateFarmUser("money=" + user.getMoney(), "user_id=" + user.getUserId());
		}
	}
	// 提升用户等级上限
	public static void addMaxRank(FarmUserBean farmUser, int i) {
		farmUser.setMaxRank(farmUser.getMaxRank() + i);
		service.updateFarmUser("max_rank=max_rank+" + i, "user_id=" + farmUser.getUserId());
	}
	// 提升用户某专业等级上限
	public static void addMaxRank(FarmUserBean farmUser, FarmUserProBean userPro, int i) {
		if(userPro == null)
			return;
		userPro.setMaxRank(userPro.getMaxRank() + i);
		SqlUtil.executeUpdate("update farm_user_pro set max_rank=max_rank+" + i + " where id=" + userPro.getId(), 4);
	}
	// 转变职业
	public static void updateClass1(FarmUserBean farmUser, int i) {
		farmUser.setClass1(i);
		service.updateFarmUser("class1=" + i, "user_id=" + farmUser.getUserId());
	}
	//五行轮回
	public static void updateUserElement(FarmUserBean farmUser, int i) {
		if(i == 0) {
			i = RandomUtil.nextInt(6);
		}
		farmUser.setElement(i);
		service.updateFarmUser("element=" + i, "user_id=" + farmUser.getUserId());
	}
	public static void moveUser(FarmUserBean user, MapNodeBean node) {
		int pos = user.getPos();
		user.setPos(node.getId());
		nodeMovePlayer(pos, node.getId(), user, 0);
	}
	public static void moveUser(FarmUserBean user, int newPos) {
		int pos = user.getPos();
		user.setPos(newPos);
		nodeMovePlayer(pos, newPos, user, 0);
	}
	public static void moveUser(FarmUserBean user, MapNodeBean node, int direction) {
		int pos = user.getPos();
		user.setPos(node.getId());
		nodeMovePlayer(pos, node.getId(), user, direction);
	}
	
	public static String formatMoney(int money) {
		int money2 = money / 100;
		if(money2 > 0) {
			money %= 100;
			int money3 = money2 / 100;
			if(money3 > 0) {
				money2 %= 100;
				String ret = money3 + "金块";
				if(money2 != 0)
					ret += money2 + "银元";
				if(money != 0)
					ret += money + "铜板";
				return ret;
			} else {
				if(money == 0)
					return money2 + "银元";
				else
					return money2 + "银元" + money + "铜板";
			}
		}
		return money + "铜板";
	}
	// 缩略显示铜板
	public static String formatSimpleMoney(int money) {
		if(money < 100)
			return money + "铜板";
		else if(money < 10000)
			return (money / 100) + "银元";
		return (money / 10000) + "金块";
	}
	
	public static DummyProductBean getItem(int itemId) {
		return dummyService.getDummyProducts(itemId);
	}
	
	public LandMapBean getLand(int id) {
		return (LandMapBean)landMap.get(Integer.valueOf(id));
	}
	
	public LandItemBean getLandItem(int id) {
		return (LandItemBean)landItemMap.get(Integer.valueOf(id));
	}
	
	public MapBean getMap(int id) {
		return (MapBean)mapMap.get(Integer.valueOf(id));
	}
	
	public MapNodeBean getMapNode(int id) {
		return (MapNodeBean)nodeMap.get(Integer.valueOf(id));
	}
	public MapSignBean getMapSign(int id) {
		return (MapSignBean)signMap.get(Integer.valueOf(id));
	}
	
	public FarmBookBean getBook(int id) {
		return (FarmBookBean)bookMap.get(Integer.valueOf(id));
	}
	
	public static void addSkill(FarmUserProBean pro, int skillId) {
		if(pro == null || pro.hasSkill(skillId))
			return;
		pro.addSkill(skillId);
		SqlUtil.executeUpdate("update farm_user_pro set skill='" + pro.getSkill() + "' where id=" + pro.getId(), 4);
	}
	
	// 废弃一个专业
	public void removeUserPro(FarmUserProBean pro) {
		service.removeUserPro(pro.getId());
		pro.setSkill("");
		SqlUtil.executeUpdate("update farm_user_pro set skill='' where id=" + pro.getId(), 4);
	}
	
	// 制造技能
	public static int skillCompose(FarmUserBean user, FarmSkillBean skill) {
		return UserBagCacheUtil.composeDirectly(user.getUserId(), skill.getMaterialList(), skill.getProductList());
	}
	
	// 一分钟跑一次
	public void task(int count) {
		if(!loaded)
			return;
		if(count % 10 == 0) {		// 30分钟采集点刷新一次
			for(int i = 0;i < landList.size();i++) {
				LandMapBean land = (LandMapBean)landList.get(i);
				processLand(land);
			}
		}
		for(int i = 0;i < pickTList.size();i++) {
			PickTBean pt = (PickTBean)pickTList.get(i);
			if(count % 1 == 0) {
				processPickSpawn(pt);
			}
		}
		
		FarmNpcWorld.one.task(count);
	}
	
	private void processPickSpawn(PickTBean pt) {
		List ptList = pt.getPickList();
		if(ptList.size() == 0) {
			for(int i = 0;i < pt.getPosList().size();i++) {
				Integer iid = (Integer)pt.getPosList().get(i);
				PickBean p = pt.create();
				
				p.setPos(iid.intValue());
				nodeAddObj(p);
				ptList.add(p);
				FarmNpcWorld.one.addObject(p);
			}
		}
	}
	// 长出采集的物品
	public void processLand(LandMapBean land) {
		List grid = land.getItem1GridList();
		for(int i = 0;i < grid.size();i++) {
			LandNodeBean node = (LandNodeBean)grid.get(i);
			if(!node.hasItem()) {
				int itemId = land.getRandomItem1();
				LandItemBean item = getLandItem(itemId);
				if(item != null) {
					node.setCount(item.getRandomCount());
					node.setItem(item);
				}
			}
		}
		grid = land.getItem2GridList();
		for(int i = 0;i < grid.size();i++) {
			LandNodeBean node = (LandNodeBean)grid.get(i);
			if(!node.hasItem()) {
				int itemId = land.getRandomItem2();
				LandItemBean item = getLandItem(itemId);
				if(item != null) {
					node.setCount(item.getRandomCount());
					node.setItem(item);
				}
			}
		}
	}
	// 奖励
	public static String getPrizeString(List prize) {
		return getActionString(prize);
	}
//	 执行
	public static String getActionString(List actionList) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < actionList.size();i++) {
			int[] p = (int[])actionList.get(i);
			switch(p[0]) {
			case 0: {		// 奖励铜板
				sb.append(FarmWorld.formatMoney(p[1]));
			} break;
			case 1: {		// 奖励经验值
				sb.append(p[1]);
				sb.append("经验");
			} break;
			case 2: {		// 奖励经验值
				sb.append("提升等级上限");
			} break;
			case 3: {		// 职业奖励
				sb.append("职业-");
				sb.append(FarmUserBean.class1Name[p[1]]);
			} break;
			case 4: {		// 专业经验值奖励
				FarmProBean pro = one.getPro(p[1]);
				if(pro != null)
					sb.append(pro.getName());
				else
					sb.append("(未知)");
				sb.append("[专业]经验");
				sb.append(p[2]);
			} break;
			case 5: {		// 学会某项技能
				FarmSkillBean skill = one.getSkill(p[1]);
				sb.append("[技能]");
				if(skill != null)
					sb.append(skill.getName());
				else
					sb.append("(未知)");
			} break;
			case 6: {		// 专业上限提升
				FarmProBean pro = one.getPro(p[1]);
				sb.append("提升");
				if(pro != null)
					sb.append(pro.getName());
				else
					sb.append("(未知)");
				sb.append("[专业]等级上限");
				sb.append(p[2]);
			} break;
			case 7: {		// 五行属性转变
				sb.append("五行轮回");
			} break;
			case 8: {		// 接受任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				sb.append("接受[任务]");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
			} break;
			case 9: {		// 完成任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				sb.append("完成[任务]");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
			} break;
			case 10: {		// 获得物品
				DummyProductBean item = getItem(p[1]);
				sb.append("获得[物品]");
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				if(p.length > 2 && p[2] > 1) {
					sb.append("x");
					sb.append(p[2]);
				}
			} break;
			case 11: {		// 失去物品
				DummyProductBean item = getItem(p[1]);
				sb.append("获得[物品]");
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				if(p.length > 2 && p[2] > 1) {
					sb.append("x");
					sb.append(p[2]);
				}
			} break;
			case 12: {		// 后续触发
				TriggerBean trigger = one.getTrigger(p[1]);
				sb.append("[触发]");
				if(trigger == null)
					sb.append("(未知)");
				else
					sb.append(trigger.getName());
			} break;
			case 13: {		// 触发聊天
				FarmTalkBean talk = FarmNpcWorld.one.getTalk(p[1]);
				sb.append("[聊天]");
				if(talk == null)
					sb.append("(未知)");
				else
					sb.append(StringUtil.limitString(talk.getTitle(),16));
			} break;
			case 14: {		// 移动到某个场景
				MapNodeBean node = one.getMapNode(p[1]);
				sb.append("[场景]");
				if(node == null)
					sb.append("(未知)");
				else
					sb.append(node.getName());
			} break;
			case 20: {		// 专业等级下降1
				FarmProBean pro = one.getPro(p[1]);
				if(pro != null)
					sb.append(pro.getName());
				else
					sb.append("(未知)");
				sb.append("[专业]等级下降");
			} break;
			case 21: {		// 重新分配属性
				sb.append("战斗属性重置");
			} break;
			}
			sb.append(",");
		}
		return sb.toString();
	}
//	 执行情况，显示给用户看
	public static String getActionDetail(List actionList) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < actionList.size();i++) {
			int[] p = (int[])actionList.get(i);
			switch(p[0]) {
			case 0: {		// 奖励铜板
				sb.append("获得了一些铜板");
			} break;
			case 1: {		// 奖励经验值
				sb.append("经验增加了");
			} break;
			case 2: {		// 提升等级上限
				sb.append("等级上限增加了");
			} break;
			case 3: {		// 职业奖励
				sb.append("成为了[职业]");
				sb.append(FarmUserBean.class1Name[p[1]]);
			} break;
			case 4: {		// 专业经验值奖励
				FarmProBean pro = one.getPro(p[1]);
				if(pro != null)
					sb.append(pro.getName());
				else
					sb.append("(未知)");
				sb.append("[专业]经验增加了");
			} break;
			case 5: {		// 学会某项技能
				sb.append("学会了[技能]");
				FarmSkillBean skill = one.getSkill(p[1]);
				if(skill != null)
					sb.append(skill.getName());
				else
					sb.append("(未知)");
			} break;
			case 6: {		// 专业上限提升
				FarmProBean pro = one.getPro(p[1]);
				if(pro != null)
					sb.append(pro.getName());
				else
					sb.append("(未知)");
				sb.append("[专业]等级上限增加了");
				sb.append(p[2]);
			} break;
			case 7: {		// 五行属性转变
				sb.append("金木水火土,往来天地之间");
			} break;
			case 8: {		// 接受任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				sb.append("接到了[任务]");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
			} break;
			case 9: {		// 完成任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				sb.append("完成了[任务]");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
			} break;
			case 10: {		// 获得物品
				DummyProductBean item = getItem(p[1]);
				sb.append("获得了[物品]");
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				if(p.length > 2 && p[2] > 1) {
					sb.append("x");
					sb.append(p[2]);
				}
			} break;
			case 11: {		// 失去物品
				DummyProductBean item = getItem(p[1]);
				sb.append("失去了[物品]");
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				if(p.length > 2 && p[2] > 1) {
					sb.append("x");
					sb.append(p[2]);
				}
			} break;
			case 12: {		// 后续触发
			} break;
			case 13: {		// 触发聊天
			} break;
			case 14: {		// 移动到某个场景
				sb.append("来到了[场景]");
				MapNodeBean node = one.getMapNode(p[1]);
				if(node == null)
					sb.append("(未知)");
				else
					sb.append(node.getName());
			} break;
			case 20: {		// 专业等级下降1
				FarmProBean pro = one.getPro(p[1]);
				if(pro != null)
					sb.append(pro.getName());
				else
					sb.append("(未知)");
				sb.append("[专业]等级下降了");
			} break;
			case 21: {		// 重新分配属性
				sb.append("战斗属性点恢复到了初始状态");
			} break;
			}
			sb.append("<br/>");
		}
		return sb.toString();
	}
	// 触发事件
	public static String getEventString(List eventList) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < eventList.size();i++) {
			int[] p = (int[])eventList.get(i);
			switch(p[0]) {
			case 1: {		// 完成任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				sb.append("完成[任务]");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
			} break;
			case 2: {		// 杀死怪物
				CreatureTBean c = FarmNpcWorld.one.getCreatureT(p[1]);
				sb.append("[猎杀]");
				if(c == null)
					sb.append("(未知)");
				else
					sb.append(c.getName());
			} break;
			case 3: {		// 到某个场景
				MapNodeBean node = one.getMapNode(p[1]);
				sb.append("[场景]");
				if(node == null)
					sb.append("(未知)");
				else
					sb.append(node.getName());
			} break;
			case 4: {		// 某聊天过程触发
				FarmTalkBean talk = FarmNpcWorld.one.getTalk(p[1]);
				sb.append("[聊天]");
				if(talk == null)
					sb.append("(未知)");
				else
					sb.append(StringUtil.limitString(talk.getTitle(),16));
			} break;
			case 5: {		// npc触发
				FarmNpcBean npc = FarmNpcWorld.one.getNpc(p[1]);
				sb.append("[NPC]");
				if(npc == null)
					sb.append("(未知)");
				else
					sb.append(npc.getName());
			} break;
			case 6: {		// 获得物品触发
				DummyProductBean item = FarmWorld.getItem(p[1]);
				sb.append("[物品]");
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
			} break;
			}
			sb.append(",");
		}
		return sb.toString();
	}
//	 触发条件
	public static String getConditionString(List conditionList) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < conditionList.size();i++) {
			int[] p = (int[])conditionList.get(i);
			switch(p[0]) {
			case 1: {		// 完成任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				sb.append("完成[任务]");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
			} break;
			case 2: {		// 接受任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				sb.append("接受[任务]");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
			} break;
			case 3: {		// 职业要求
				sb.append("职业-");
				sb.append(FarmUserBean.class1Name[p[1]]);
			} break;
			case 4: {		// 等级要求
				sb.append("人物等级");
				sb.append(p[1]);
			} break;
			case 5: {		// 专业等级要求
				FarmProBean pro = one.getPro(p[1]);
				if(pro == null)
					sb.append("(未知)");
				else
					sb.append(pro.getName());
				sb.append("[专业]等级");
				if(p.length > 2) {
					sb.append(p[2]);
				} else
					sb.append(0);
			} break;
			case 6: {		// 先修触发
				TriggerBean trigger = one.getTrigger(p[1]);
				sb.append("已[触发]");
				if(trigger == null)
					sb.append("(未知)");
				else
					sb.append(trigger.getName());
			} break;
			case 7: {		// 等级范围
				sb.append("人物等级");
				sb.append(p[1]);
				if(p.length > 2) {
					sb.append('-');
					sb.append(p[2]);
				}
			} break;
			case 8: {		// 专业等级范围
				FarmProBean pro = one.getPro(p[1]);
				if(pro == null)
					sb.append("(未知)");
				else
					sb.append(pro.getName());
				sb.append("[专业]等级");
				if(p.length > 2) {
					sb.append(p[2]);
					if(p.length > 3) {
						sb.append('-');
						sb.append(p[3]);
					}
				}
				else
					sb.append(0);
			} break;
			case 9: {		// 至少是帮派成员，并且duty高于
				sb.append("门派[地位]");
				sb.append(TongUserBean.dutyName[p[1]]);
			} break;
			case 10: {		// 拥有物品
				DummyProductBean item = getItem(p[1]);
				sb.append("[物品]");
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				if(p.length > 2 && p[2] > 1) {
					sb.append("x");
					sb.append(p[2]);
				}
			} break;
			case 11: {		// 完成收藏品
				CollectBean col = one.getCollect(p[1]);
				sb.append("[收藏盒]");
				if(col == null)
					sb.append("(未知)");
				else
					sb.append(col.getName());
			} break;
			case 12: {		// 五行
				sb.append("[五行]");
				for(int i2 = 1;i2 < p.length;i2++)
					sb.append(FarmUserBean.elementName[p[i2]]);
			} break;
			}
			sb.append(",");
		}
		return sb.toString();
	}
//	 执行
	public static void doAction(List actions, FarmUserBean farmUser) {
		for(int i = 0;i < actions.size();i++) {
			int[] p = (int[])actions.get(i);
			switch(p[0]) {
			case 0: {		// 奖励铜板
				addMoney(farmUser, p[1]);
			} break;
			case 1: {		// 奖励经验值
				addFUExp(farmUser, p[1]);
			} break;
			case 2: {		// 提升等级上限
				addMaxRank(farmUser, p[1]);
			} break;
			case 3: {		// 转变职业
				updateClass1(farmUser, p[1]);
			} break;
			case 4: {		// 加专业经验
				FarmUserProBean userPro = farmUser.getPro(p[1]);
				if(userPro != null)
					addFUPExp(userPro, p[2]);
			} break;
			case 5: {		// 学会某项技能
				FarmSkillBean skill = one.getSkill(p[1]);
				if(skill.getClass1() == 0 || skill.getClass1() == farmUser.getClass1())
					addSkill(farmUser.getPro(skill.getProId()), skill.getId());
			} break;
			case 6: {		// 专业上限提升
				FarmUserProBean userPro = farmUser.getPro(p[1]);
				addMaxRank(farmUser, userPro, p[2]);
			} break;
			case 7: {		// 五行轮回
				updateUserElement(farmUser, p[1]);
			} break;
			case 8: {		// 接受任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				FarmNpcWorld.one.addUserQuest(farmUser, quest);
				String tip = "你接受了[任务]" + quest.getName(); 
				farmUser.addLog(tip);
			} break;
			case 9: {		// 完成任务
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[1]);
				FarmUserQuestBean userQuest = farmUser.getStartQuest(p[1]);
				if(userQuest != null && userQuest.getStatus() == 0) {
					FarmNpcWorld.one.endUserQuest(farmUser, userQuest);
					userQuest.setDoneTime(System.currentTimeMillis());
					String tip = "你完成了[任务]" + quest.getName(); 
					farmUser.addLog(tip);
					doAction(quest.getPrizeList(), farmUser);
				}
			} break;
			case 10: {		// 获得物品
				if(p.length > 2)
					UserBagCacheUtil.addUserBagCacheStack(farmUser.getUserId(), p[1], p[2]);
				else
					UserBagCacheUtil.addUserBagCacheStack(farmUser.getUserId(), p[1], 1);
			} break;
			case 11: {		// 失去物品

			} break;
			case 12: {		// 后续触发
				TriggerBean t = one.getTrigger(p[1]);
				if(isCondition(t.getConditionList(), farmUser)) {
					farmUser.getCurTrigger().addLast(t);
					doAction(t.getActionList(), farmUser);
					FarmWorld.addUserTrigger(farmUser, t);
				}
			} break;
			case 13: {		// 触发聊天
				
			} break;
			case 14: {		// 触发移动（传送）
				moveUser(farmUser, p[1]);
			} break;
			case 20: {		// 专业等级下降，战斗专业无法降低
				FarmUserProBean userPro = farmUser.getPro(p[1]);
				if(userPro != null && userPro.getRank() > 1 && userPro.getPro() != FarmProBean.PRO_BATTLE) {
					FarmProBean pro = one.getPro(p[1]);
					FarmWorld.addFUPRank(userPro, -1);
					FarmWorld.addFUPExp(userPro, -userPro.getUpgradeExpAdd());
					FarmWorld.addProPoint(farmUser, pro.getPoint());
				}
			} break;
			case 21: {		// 战斗属性点重置
				farmUser.resetAttr();
				farmUser.setBattlePoint(rankBattlePoint[farmUser.getProRank(FarmProBean.PRO_BATTLE)]);
				farmUser.resetCurStat();
				saveBattlePoint(farmUser);
			} break;
			}
		}
	}
	
	public static boolean isCondition(List conditions, FarmUserBean farmUser) {
		return notCondition(conditions, farmUser) == null;
	}
	// 检查条件是否符合
	public static int[] notCondition(List conditions, FarmUserBean farmUser) {
		HashMap itemMap = null;
		for(int i = 0;i < conditions.size();i++) {
			int[] p = (int[])conditions.get(i);
			switch(p[0]) {
			case 1: {		// 完成任务
				if(!farmUser.isQuestEnd(p[1]))
					return p;
			} break;
			case 2: {		// 接受任务
				if(!farmUser.isQuestStart(p[1]))
					return p;
			} break;
			case 3: {		// 职业要求
				if(farmUser.getClass1() != p[1])
					return p;
			} break;
			case 4: {		// 等级要求
				if(farmUser.getRank() < p[1])
					return p;
			} break;
			case 5: {		// 专业等级要求
				if(farmUser.getProRank(p[1]) < p[2])
					return p;
			} break;
			case 6: {		// 先修触发
				if(!farmUser.isTriggered(p[1]))
					return p;
			} break;
			case 7: {		// 等级要求，不得高于
				if(farmUser.getRank() < p[1])
					return p;
				if(p.length > 2 && farmUser.getRank() > p[2])
					return p;
			} break;
			case 8: {		// 专业等级要求，不得高于
				int rank = farmUser.getProRank(p[1]);
				if(rank < p[2])
					return p;
				if(p.length > 3 && rank > p[3])
					return p;
			} break;
			case 9: {		// 门派要求，要求duty大于
				TongUserBean tu = FarmTongWorld.getTongUser(farmUser.getUserId());
				if(tu == null || tu.getDuty() < p[1])
					return p;
			} break;
			case 10: {		// 拥有某物品多少个
				if(itemMap == null)
					itemMap = UserBagCacheUtil.getUserBagItemMap(farmUser.getUserId());
				int[] count = (int[])itemMap.get(Integer.valueOf(p[1]));
				if(count == null)
					return p;
				if(p.length > 2 && p[2] > count[0])
					return p;
			} break;
			case 11: {		// 完成收藏盒
				int ucId = getUserCollect(farmUser.getUserId(), p[1]);
				if(ucId == 0)
					return p;
				UserCollectBean uc = getUserCollect(ucId);
				if(one.getCollect(p[1]).getCount() > uc.getCount())
					return p;
			} break;
			case 12: {
				boolean is = true;
				for(int i2 = 1;i2 < p.length;i2++)
					if(p[i2] == farmUser.getElement())
						is = false;
				if(is)
					return p;
			} break;
			}
		}
		return null;
	}
	
	// 技能效果描述
	public static String getSkillEffectString(List effectList) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < effectList.size();i++) {
			int[] ii = (int[])effectList.get(i);
			switch(ii[0]) {
			case 1: {		// 倍数伤害
				sb.append(ii[1]);
				sb.append("%伤害");
			} break;
			case 2: {		// 对不同目标造成伤害
				sb.append("(目标");
				sb.append(ii[2]);
				sb.append(")");
				sb.append(ii[1]);
				sb.append("%伤害");
			} break;
			case 3: {		// 击晕效果，只对怪物有效
				sb.append("击晕");
				sb.append(ii[1]);
				sb.append("秒");
			} break;
			case 4: {		// 防御恢复气力
				sb.append("恢复");
				sb.append(ii[1]);
				sb.append("%血");
			} break;
			case 5: {		// 防御恢复气力
				sb.append("恢复");
				sb.append(ii[1]);
				sb.append("%气力");
			} break;
			case 6: {		// 防御恢复气力
				sb.append("恢复");
				sb.append(ii[1]);
				sb.append("%体力");
			} break;
			case 7: {		// 倍数伤害
				sb.append(ii[1]);
				sb.append("%+");
				sb.append(ii[2]);
				sb.append("伤害");
			} break;
			case 8: {		// 防御恢复气力
				sb.append("恢复");
				sb.append(ii[1]);
				sb.append("点血");
			} break;
			case 9: {		// 防御恢复气力
				sb.append("恢复");
				sb.append(ii[1]);
				sb.append("点气力");
			} break;
			case 10: {		// 防御恢复气力
				sb.append("恢复");
				sb.append(ii[1]);
				sb.append("点体力");
			} break;
			}
			sb.append(",");
		}
		return sb.toString();
	}
	
	// 技能消耗描述
	public static String getSkillCostString(List costList) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < costList.size();i++) {
			int[] ii = (int[])costList.get(i);
			switch(ii[0]) {
			case 1: {		// 消耗
				sb.append("血");
				sb.append(ii[1]);
			} break;
			case 2: {		// 消耗
				sb.append("气力");
				sb.append(ii[1]);
			} break;
			case 3: {		// 消耗
				sb.append("体力");
				sb.append(ii[1]);
			} break;
			}
			sb.append(",");
		}
		return sb.toString();
	}
	
	// 物品列表工具
	public static String getItemListString(List items, int price) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < items.size();i++) {
			Object obj = items.get(i);
			if(obj instanceof Integer) {
				DummyProductBean item = UserBagCacheUtil.getItem(((Integer)obj).intValue());
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				sb.append(",");
			} else {
				int[] tmp = (int[])obj;
				DummyProductBean item = UserBagCacheUtil.getItem(tmp[0]);
				if(item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				sb.append("x");
				sb.append(tmp[1]);
				sb.append(",");
			}
		}
		if(price > 0)
			sb.append(formatMoney(price));
		return sb.toString();
	}
	// 怪物列表
	public static String getCreatureListString(List items) {
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < items.size();i++) {
			int[] tmp = (int[])items.get(i);
			if(tmp.length < 2) continue;
			sb.append("[猎杀]");
			CreatureTBean template = FarmNpcWorld.one.getCreatureT(tmp[0]);
			if(template != null)
				sb.append(template.getName());
			else
				sb.append("(未知)");
			sb.append("x");
			sb.append(tmp[1]);
			sb.append(",");
		}
		return sb.toString();
	}
	// 怪物猎杀列表
	public static String getCreatureFinishListString(List items, FarmUserBean farmUser) {
		if(items == null)
			return "";
		StringBuilder sb = new StringBuilder(32);
		for(int i = 0;i < items.size();i++) {
			int[] tmp = (int[])items.get(i);
			sb.append("[猎杀]");
			sb.append(FarmNpcWorld.one.getCreatureT(tmp[0]).getName());
			sb.append("x");
			sb.append(tmp[1]);
			int finish = farmUser.getCreatureFinishCount(tmp[0]);
			if(finish > 0) {
				if(finish > tmp[1])
					finish = tmp[1];
				sb.append('(');
				sb.append("完成");
				sb.append(finish);
				sb.append(')');
			}
			sb.append(",");
		}
		return sb.toString();
	}
	// 得到一堆物品的名字列表
	public static String getItemListString(List items) {
		return getItemListString(items, 0);
	}
	// 场景人物增加删除移动等
	public static void nodeAddPlayer(FarmUserBean obj) {
		nodeAddPlayer(obj.getPos(), obj);
	}
	public static void nodeAddPlayer(int nodeId, FarmUserBean obj) {
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.addPlayer(obj);
	}
	public static void nodeRemovePlayer(FarmUserBean obj) {
		nodeRemovePlayer(obj.getPos(), obj);
	}
	public static void nodeRemovePlayer(int nodeId, FarmUserBean obj) {
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.removePlayer(obj);
	}
	public static void nodeMovePlayer(int nodeId, int newNodeId, FarmUserBean obj, int dir) {
		if(nodeId == newNodeId)
			return;
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.removePlayer(obj);
		switch(dir) {
		case 1:
			addLog(node, obj.getNameWml() + "向北面走去");
			break;
		case 3:
			addLog(node, obj.getNameWml() + "向西面走去");
			break;
		case 5:
			addLog(node, obj.getNameWml() + "向东面走去");
			break;
		case 7:
			addLog(node, obj.getNameWml() + "向南面走去");
			break;
		}
		node = one.getMapNode(newNodeId);
		if(node != null) {
			switch(dir) {
			case 1:
				addLog(node, obj.getNameWml() + "从南面走来");
				break;
			case 3:
				addLog(node, obj.getNameWml() + "从东面走来");
				break;
			case 5:
				addLog(node, obj.getNameWml() + "从西面走来");
				break;
			case 7:
				addLog(node, obj.getNameWml() + "从北面走来");
				break;
			}
			node.addPlayer(obj);
		}
	}
	// 场景结点增加物体，例如npc等的功能
	public static void nodeAddObj(MapDataBean obj) {
		nodeAddObj(obj.getPos(), obj);
	}
	public static void nodeAddObj(int nodeId, Object obj) {
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.addObj(obj);
	}
	public static void nodeRemoveObj(MapDataBean obj) {
		nodeRemoveObj(obj.getPos(), obj);
	}
	public static void nodeRemoveObj(int nodeId, Object obj) {
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.removeObj(obj);
	}
	public static void nodeMoveObj(int nodeId, int newNodeId, Object obj) {
		if(nodeId == newNodeId)
			return;
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.removeObj(obj);
		if(newNodeId == 0)
			return;
		node = one.getMapNode(newNodeId);
		if(node != null)
			node.addObj(obj);
	}
	// 删除掉落物品
	public static void nodeRemoveDrop(MapDataBean obj) {
		nodeRemoveDrop(obj.getPos(), obj);
	}
	public static void nodeRemoveDrop(int nodeId, Object obj) {
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.removeDrop(obj);
	}
	public static void nodeAddDrop(MapDataBean obj) {
		nodeAddDrop(obj.getPos(), obj);
	}
	public static void nodeAddDrop(int nodeId, Object obj) {
		MapNodeBean node = one.getMapNode(nodeId);
		if(node != null)
			node.addDrop(obj);
	}
	// 掉落一个物品
	public static void addDrop(int nodeId, int[] data) {
		FarmDropBean drop = new FarmDropBean();
		drop.setPos(nodeId);
		drop.setData(data);
		nodeAddDrop(drop);
	}
//	 掉落一个物品并保护一段时间
	public static void addDrop(int nodeId, int[] data, int userId, int protect) {
		FarmDropBean drop = new FarmDropBean();
		drop.setPos(nodeId);
		drop.setData(data);
		drop.setUserId(userId);
		drop.setProtect(protect);
		nodeAddDrop(drop);
	}
	
	// 卸下装备
	public static void removeEquip(FarmUserBean farmUser, int part) {
		FarmUserEquipBean equip = farmUser.getEquip(part);
		synchronized(bagLock) {
			if(equip != null && equip.getUserbagId() != 0) {
			
				UserBagCacheUtil.updateUserBagCacheById("user_id=" + farmUser.getUserId(), "id=" + equip.getUserbagId(),
						farmUser.getUserId(), equip.getUserbagId());

				equip.setUserbagId(0);
				SqlUtil.executeUpdate("update farm_user_equip set userbag_id=0 where id=" + equip.getId(), 4);
			}
			farmUser.resetCurStat();
		}
	}
	// 装备
	public static String addEquip(FarmUserBean farmUser, int part, int userbagId) {
		FarmUserEquipBean[] equips = farmUser.getEquip();
		FarmUserEquipBean equip = equips[part];
		synchronized(bagLock) {
			UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userbagId);
			// 判断用户是否拥有该商品
			if (userBag == null || userBag.getUserId() != farmUser.getUserId()) {
				return "不存在的物品";
			}
			DummyProductBean item = UserBagCacheUtil.getItem(userBag.getProductId());
			if(FarmUserEquipBean.partClass[part] != item.getClass2())
				return "不匹配的物品";
			if(farmUser.getProRank(FarmProBean.PRO_BATTLE) < item.getRank())
				return "战斗等级不足,无法装备该物品";
			if(equip == null || equip.getUserbagId() == 0) {
				UserBagCacheUtil.updateUserBagCacheById("user_id=0", "id=" + userbagId,
						farmUser.getUserId(), userbagId);

				if(equip == null) {
					equip = new FarmUserEquipBean();
					equip.setUserId(farmUser.getUserId());
					equip.setPart(part);
					equip.setUserbagId(userbagId);
					service.addUserEquip(equip);
					equips[part] = equip;
				} else {
					equip.setUserbagId(userbagId);
					SqlUtil.executeUpdate("update farm_user_equip set userbag_id=" + userbagId
							+ " where id=" + equip.getId(), 4);
				}
				if(item.isEquipBind() && !userBag.isMarkBind()) {	// 装备绑定！
					UserBagCacheUtil.setBind(userBag);
				}
			}
			farmUser.resetCurStat();
		}
		return "装备成功";
	}
//	 增加冷却时间
	public static void addCooldown(FarmUserBean farmUser, int cooldownId, long cooldown) {
		addCooldown(farmUser, cooldownId, System.currentTimeMillis(), cooldown);
	}
	
	public static void addCooldown(FarmUserBean farmUser, int cooldownId, long now, long cooldown) {
		now += cooldown;
		boolean res = farmUser.addCooldown(cooldownId, now);
		if(cooldown > 600000) {		// 超过10分钟的冷却，才需要保存到数据库
			if(res) {	// 需要增加数据库记录
				SqlUtil.executeUpdate("insert into farm_cooldown set user_id=" + 
						farmUser.getUserId() + ",cooldown_id=" + cooldownId + ",time='" + DateUtil.formatSqlDatetime(now) + "'", 4);
			} else {	// 更新即可
				SqlUtil.executeUpdate("update farm_cooldown set time='" + DateUtil.formatSqlDatetime(now) + 
						"' where user_id=" + farmUser.getUserId() + " and cooldown_id=" + cooldownId, 4);
			}
		}
	}
	
	
	// 修改用户资料
	public static void updateUserInfo(FarmUserBean farmUser, String name) {
		farmUser.setName(name);
		SqlUtil.executeUpdate("update farm_user set name='" + StringUtil.toSql(name)
				+ "' where user_id=" + farmUser.getUserId(), 4);
	}
	// 移动用户位置
	public static void updateUserPos(FarmUserBean farmUser, int pos) {
		SqlUtil.executeUpdate("update farm_user set pos=" + pos
				+ " where user_id=" + farmUser.getUserId(), 4);
	}
	
//	 用于后台的map node修改
	public void updateMapNode(MapNodeBean bean) {
		service.updateMapNode(bean, false);
	}
	public void addMapNode(MapNodeBean bean) {
		if(!service.updateMapNode(bean, true))
			return;
		nodeMap.put(Integer.valueOf(bean.getId()), bean);
		nodeList.add(bean);
	}
	
//	 用于后台的map 路标修改
	public void updateMapSign(MapSignBean bean) {
		service.updateMapSign(bean, false);
	}
	public void addMapSign(MapSignBean bean) {
		if(!service.updateMapSign(bean, true))
			return;
		signMap.put(Integer.valueOf(bean.getId()), bean);
		signList.add(bean);
	}
	
//	 用于后台的收藏品修改
	public void updateCollect(CollectBean bean) {
		service.updateCollect(bean, false);
	}
	public void addCollect(CollectBean bean) {
		if(!service.updateCollect(bean, true))
			return;
		collectMap.put(Integer.valueOf(bean.getId()), bean);
		collectList.add(bean);
	}
	
//	 用于后台的skill修改
	public void updateSkill(FarmSkillBean bean) {
		service.updateSkill(bean, false);
	}
	public void addSkill(FarmSkillBean bean) {
		if(!service.updateSkill(bean, true))
			return;
		skillMap.put(Integer.valueOf(bean.getId()), bean);
		skillList.add(bean);
	}
	
//	 用于后台的skill修改
	public void updatePro(FarmProBean bean) {
		service.updatePro(bean, false);
	}
	public void addPro(FarmProBean bean) {
		if(!service.updatePro(bean, true))
			return;
		proMap.put(Integer.valueOf(bean.getId()), bean);
		proList.add(bean);
	}
	
//	 用于后台的crop修改
	public void updateCrop(FarmCropBean bean) {
		service.updateCrop(bean, false);
	}
	public void addCrop(FarmCropBean bean) {
		if(!service.updateCrop(bean, true))
			return;
		cropMap.put(Integer.valueOf(bean.getId()), bean);
		cropList.add(bean);
	}
	
//	 用于后台的land修改
	public void updateLand(LandMapBean bean) {
		service.updateLand(bean, false);
	}
	public void addLand(LandMapBean bean) {
		if(!service.updateLand(bean, true))
			return;
		landList.add(bean);
		landMap.put(Integer.valueOf(bean.getId()), bean);
	}
	
//	 用于后台的land_item修改
	public void updateLandItem(LandItemBean bean) {
		service.updateLandItem(bean, false);
	}
	public void addLandItem(LandItemBean bean) {
		if(!service.updateLandItem(bean, true))
			return;
		landItemMap.put(Integer.valueOf(bean.getId()), bean);
		landItemList.add(bean);
	}
	
//	 用于后台的dummy_product = item修改
	public void updateItem(DummyProductBean bean) {
		service.updateItem(bean, false);
	}
	public void addItem(DummyProductBean bean) {
		if(!service.updateItem(bean, true))
			return;
	}
	
//	 用于后台的book修改
	public void updateBook(FarmBookBean bean) {
		service.updateBook(bean, false);
	}
	public void addBook(FarmBookBean bean) {
		if(!service.updateBook(bean, true))
			return;
		bookMap.put(Integer.valueOf(bean.getId()), bean);
		bookList.add(bean);
	}
	
//	 用于后台的pick修改
	public void updatePick(MapPickBean bean) {
		service.updatePick(bean, false);
	}
	public void addPick(MapPickBean bean) {
		if(!service.updatePick(bean, true))
			return;
		pickMap.put(Integer.valueOf(bean.getId()), bean);
		pickList.add(bean);
	}
	
//	 用于后台的触发器修改
	public void updateTrigger(TriggerBean bean) {
		service.updateTrigger(bean, false);
	}
	public void addTrigger(TriggerBean bean) {
		if(!service.updateTrigger(bean, true))
			return;
		triggerMap.put(Integer.valueOf(bean.getId()), bean);
		triggerList.add(bean);
	}
	// 安装触发器
	public static void setTrigger(TriggerBean t) {
		for(int i = 0;i < t.getEventList().size();i++) {
			int[] p = (int[])t.getEventList().get(i);
			TriggerUtil.getTriggerMap(p[0]).addTrigger(p[1], t);
		}
	}
	public static void unsetTrigger(TriggerBean t) {
		for(int i = 0;i < t.getEventList().size();i++) {
			int[] p = (int[])t.getEventList().get(i);
			TriggerUtil.getTriggerMap(p[0]).deleteTrigger(p[1], t);
		}
	}
	
//	 用于后台的pick2大地图采矿修改
	public void updatePickT(PickTBean bean) {
		service.updatePickT(bean, false);
	}
	public void addPickT(PickTBean bean) {
		if(!service.updatePickT(bean, true))
			return;
		pickTMap.put(Integer.valueOf(bean.getId()), bean);
		pickTList.add(bean);
	}
	
//	 用于后台的大地图修改
	public void updateMap(MapBean bean) {
		service.updateMap(bean, false);
	}
	public void addMap(MapBean bean) {
		if(!service.updateMap(bean, true))
			return;
		mapMap.put(Integer.valueOf(bean.getId()), bean);
		mapList.add(bean);
	}
	
//	 用于后台的item set套装修改
	public void updateItemSet(ItemSetBean bean) {
		service.updateItemSet(bean, false);
	}
	public void addItemSet(ItemSetBean bean) {
		if(!service.updateItemSet(bean, true))
			return;
		itemSetMap.put(Integer.valueOf(bean.getId()), bean);
		itemSetList.add(bean);
	}
	
	public MapNodeBean[][] getRoundNode(MapNodeBean node, int level) {
		return getRoundNode(node, level, 0, 0);
	}
	// 得到临近的结点，返回结点阵列，迭代层次为level
	public MapNodeBean[][] getRoundNode(MapNodeBean node, int level, int limitX, int limitY) {
		int[] xy = new int[4];
		getRoundNodeXY(xy, node, level, 0, 0, new HashSet());
		if(limitX > 0) {
			xy[0] = -limitX;
			xy[1] = limitX;
		}
		if(limitY > 0) {
			if(xy[2] < -limitY)
				xy[2] = -limitY;
			if(xy[3] > limitY)
				xy[3] = limitY;
		}
		if(xy[2] == 0)
			xy[2] = -1;
		if(xy[3] == 0)
			xy[3] = 1;

		MapNodeBean[][] nodess = new MapNodeBean[xy[1] - xy[0] + 1][];
		for(int i = 0;i < nodess.length;i++)
			nodess[i] = new MapNodeBean[xy[3] - xy[2] + 1];
		getRoundNode(nodess, node, level, -xy[0], -xy[2]);
		return nodess;
	}
	
	static class MapNodeIter {
		public MapNodeBean node;
		public int x;
		public int y;
		public MapNodeIter(MapNodeBean node, int x, int y) {
			this.node = node;
			this.x = x;
			this.y = y;
		}
	};
	
	static int[] xgrid = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
	static int[] ygrid = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
	
	public void getRoundNode(MapNodeBean[][] nodess, MapNodeBean node, int level, int x, int y) {
		
		HashSet iterSet = new HashSet();
		iterSet.add(new MapNodeIter(node, x, y));
		nodess[x][y] = node;
		
		while(--level >= 0) {
			HashSet curSet =new LinkedHashSet();
			Iterator iter = iterSet.iterator();
			while(iter.hasNext()) {
				MapNodeIter mi = (MapNodeIter)iter.next();
				
				MapNodeBean[] nodes = mi.node.getLinks();
				for(int i = 0;i < 9;i++) {
					int cx = mi.x + xgrid[i];
					int cy = mi.y + ygrid[i];
					if(nodes[i] != null && cx >= 0 && cy >= 0 && cx < nodess.length && cy < nodess[0].length 
							&& nodess[cx][cy] == null) {
						if(level > 0)
							curSet.add(new MapNodeIter(nodes[i],  cx, cy));
						nodess[cx][cy] = nodes[i];
					}
				}
			}
			if(curSet.size() == 0)
				return;
			iterSet = curSet;
		}
	}
	/**
	 * @param xy 边界的x1 x2 y1 y2
	 * @param node 当前结点
	 * @param level 迭代层次
	 * @param x
	 * @param y
	 * @param visited
	 */
	public void getRoundNodeXY(int[] xy, MapNodeBean node, int level, int x, int y, HashSet visited) {
		
		HashSet iterSet = new HashSet();
		iterSet.add(new MapNodeIter(node, x, y));
		visited.add(new Integer2(x, y));
		
		while(--level >= 0) {
			HashSet curSet =new LinkedHashSet();
			Iterator iter = iterSet.iterator();
			while(iter.hasNext()) {
				MapNodeIter mi = (MapNodeIter)iter.next();
				
				MapNodeBean[] nodes = mi.node.getLinks();
				for(int i = 0;i < 9;i++) {
					int cx = mi.x + xgrid[i];
					int cy = mi.y + ygrid[i];
					Integer2 key = new Integer2(cx, cy);
					if(nodes[i] != null && !visited.contains(key)) {
						if(level > 0)
							curSet.add(new MapNodeIter(nodes[i],  cx, cy));
						
						visited.add(key);
						if(cx < xy[0])
							xy[0] = cx;
						else if(cx > xy[1])
							xy[1] = cx;
						
						if(cy < xy[2])
							xy[2] = cy;
						else if(cy > xy[3])
							xy[3] = cy;
					}
				}
			}
			if(curSet.size() == 0)
				return;
			iterSet = curSet;
		}
	}

	public static void creatureAttack(FarmUserBean farmUser, List targetList) {
		long now = System.currentTimeMillis();
		BattleStatus bs = farmUser.getCurStat();
		for(int i = 0;i < targetList.size();i++) {
			Object obj = targetList.get(i);
			if(obj instanceof CreatureBean) {
				CreatureBean creature = (CreatureBean)targetList.get(i);
				CreatureTBean template = creature.getTemplate();
				if(creature.isAlive() && !template.isFlagAnimal() && creature.isCooldown(now)) {		// 怪物反击
					int damage = calcDamage(creature.attack + RandomUtil.nextInt(3), bs.defense1, creature.getLevel());
					long next = creature.getCooldown() + template.getCooldown();
					if(next < now)
						creature.setCooldown(now);
					else
						creature.setCooldown(next);
					addLog(farmUser.getPos(), template.getName() + "反击" + farmUser.getNameWml() + "造成" + damage + "点伤害");
					farmUser.decHp(damage);
					if(farmUser.isDead())
						break;
				}
			}
		}
	}
	
	// 基本miss几率，和级别差有关，级别差levelDif = 防御方-攻击方
	static int[] baseMiss = {1, 2, 3, 4, 6, 10, 30, 60, 120, 200, 300};
	public static int getBaseMiss(int levelDif) {
		levelDif += 3;
		if(levelDif < 0)
			return 0;
		if(levelDif >= baseMiss.length)
			return 400;
		return baseMiss[levelDif];
	}
	
	// 计算各种攻击结果的概率数组，普通攻击，致命一击，偏斜, 躲闪，招架, 未命中
	public static int[] getAttackTypeRate(int rank, BattleStatus bs, CreatureBean creature) {
		int levelDif = creature.getLevel() - rank;
		int add = 0;
		if(levelDif >= 3)
			add = levelDif / 3;
		int[] typeRate = {100, bs.ds, 3 + add, 5 + add, 3 + add, getBaseMiss(levelDif)};
		
		return typeRate;
	}
	public static int[] getAttackTypeRate(int rank, BattleStatus bs, FarmUserBean user) {
		int levelDif = user.getProRank(FarmProBean.PRO_BATTLE) - rank;
		int add = 0;
		if(levelDif >= 3)
			add = levelDif / 3;
		int[] typeRate = {100, bs.ds, 3 + add, 5 + add, 3 + add, getBaseMiss(levelDif)};
		
		return typeRate;
	}
	
	public static void doAttack(FarmUserBean farmUser, Object obj, float mod, int mod2) {
		BattleStatus bs = farmUser.getCurStat();
		int rank = farmUser.getProRank(FarmProBean.PRO_BATTLE);
		int attack = bs.attack1;
		
		if(bs.attack1Float > 0) {
			int attackAdd = RandomUtil.nextInt(bs.attack1Float + bs.luck);
			if(attackAdd >= bs.attack1Float)
				attackAdd = bs.attack1Float - 1;
			attack += attackAdd;
		}
		attack = (int) (attack * mod) + mod2;
		if(obj instanceof CreatureBean) {
			CreatureBean creature = (CreatureBean)obj;
			if(creature.isDead()) {		// 怪物已经死亡
				return;
			}
			int[] typeRate = getAttackTypeRate(rank, bs, creature);
			int type = RandomUtil.randomRateIntDirect(typeRate);
			switch(type) {
			case 0: {		// 普通攻击
				int damage = (int) (calcDamage(attack, creature.defense, rank));
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "攻击" + creature.getName() + "造成" + damage + "点伤害");
				creature.damage(damage);
			} break;
			case 1: {		// 致命一击
				int damage = (int) (calcDamage(attack, creature.defense, rank) * (1.5f + bs.cb));
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "攻击" + creature.getName() + "造成" + damage + "点伤害[致命]");
				creature.damage(damage);
			} break;
			case 2: {		// 偏斜
				int damage = (int) (calcDamage(attack, creature.defense, rank) * 0.3f);
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "攻击" + creature.getName() + "造成" + damage + "点伤害[擦过]");
				creature.damage(damage);
			} break;
			case 3: {		// 躲闪
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "的攻击被" + creature.getName() + "躲开了");
			} break;
			case 4: {		// 招架
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "的攻击被" + creature.getName() + "招架了");
			} break;
			default: {		// 未命中
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "的攻击未命中" + creature.getName());
			} break;
			}
			
			if(creature.isDead()) {
				CreatureTBean template = creature.getTemplate();
				int creatureId = template.getId();
				addLog(farmUser.getPos(), creature.getName() + "死亡");
				if(template.isFlagCollect())
					if(one.addUserCollectItem(farmUser.getUserId(), creatureId, 1))
						farmUser.addLog("你完成了[收藏]" + template.getName());
				FarmNpcWorld.one.killCreature(creature);
				dropItem(farmUser, creature);
				FarmNpcWorld.checkQuestCreature(farmUser, creatureId);
				
				TriggerUtil.killTrigger.trigger(template.getId(), farmUser);
			}
		} else if(obj instanceof FarmUserBean) {
			FarmUserBean user = (FarmUserBean)obj;
			BattleStatus bs2 = user.getCurStat();
			
			int[] typeRate = getAttackTypeRate(rank, bs, user);
			int type = RandomUtil.randomRateIntDirect(typeRate);
			switch(type) {
			case 0: {		// 普通攻击
				int damage = (int) (calcDamage(attack, bs2.defense1, rank));
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "攻击" + user.getNameWml() + "造成" + damage + "点伤害");
				user.decHp(damage);
			} break;
			case 1: {		// 致命一击
				int damage = (int) (calcDamage(attack, bs2.defense1, rank) * (1.5f + bs.cb));
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "攻击" + user.getNameWml() + "造成" + damage + "点伤害[致命]");
				user.decHp(damage);
			} break;
			case 2: {		// 偏斜
				int damage = (int) (calcDamage(attack, bs2.defense1, rank) * 0.3f);
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "攻击" + user.getNameWml() + "造成" + damage + "点伤害[擦过]");
				user.decHp(damage);
			} break;
			case 3: {		// 躲闪
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "的攻击被" + user.getNameWml() + "躲开了");
			} break;
			case 4: {		// 招架
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "的攻击被" + user.getNameWml() + "招架了");
			} break;
			default: {		// 未命中
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "的攻击未命中" + user.getNameWml());
			} break;
			}
		}
		

	}
	public static int itemProtectTime = 60000;	// 保护60秒
	// 怪物死亡掉落物品
	public static void dropItem(FarmUserBean farmUser, CreatureBean creature) {
		CreatureTBean template = creature.getTemplate();
		int level = creature.getLevel();
		int userLevel = farmUser.getProRank(FarmProBean.PRO_BATTLE);
		int rank;
		if(level <= 5)
			rank = RandomUtil.nextInt(level);
		else
			rank = RandomUtil.nextInt(5) + level - 5;
		if(!template.isFlagAnimal()) {		// 小动物不掉落任务东西
			int[] data = new int[2];
			int rand = RandomUtil.nextInt(100);
			if(rand < 40) {
				data[1] = RandomUtil.nextInt(level * 5) + level * 5;
				if(template.isFlagElite())
					data[1] *= 3;
				addDrop(farmUser.getPos(), data);
			}
			int rate = 100;		// 掉落率变化
			if(template.isFlagRare())
				rate = 1000;
			else if(template.isFlagElite())
				rate = 300;
			if(userLevel - level > 10)		// 相差5级掉率下降
				rate /= 4;
			else if(userLevel - level > 5)
				rate /= 2;
				
			rand = RandomUtil.nextInt(1000);
			if(rand < rate / 8){
				List dropList = dropCollection2[rank];
				if(dropList != null) {
					Integer iid = (Integer)RandomUtil.randomObject(dropList);
					data[0] = iid.intValue();
					data[1] = 1;
					addDrop(farmUser.getPos(), data, farmUser.getUserId(), itemProtectTime);
				}
			} else if(rand < rate) {
				data = new int[2];
				int grade = getRandomGrade(creature.getLevel()) + 1;
				List dropList = dropCollection[rank][grade];
				while(dropList == null && grade > 0) {	// 没有这品质物品则降级
					grade--;
					dropList = dropCollection[rank][grade];
				}
				if(dropList != null) {	// 没有可以掉落的物品
					Integer iid = (Integer)RandomUtil.randomObject(dropList);
					data[0] = iid.intValue();
					data[1] = 1;
					addDrop(farmUser.getPos(), data, farmUser.getUserId(), itemProtectTime);
				}
			}
		}
		// 检查是否有任务物品
		List dropList = template.getDropList();
		if(dropList.size() > 0) {
			for(int i = 0;i < dropList.size();i++) {
				int[] drop = (int[])dropList.get(i);
				
				if(drop.length == 2) {	// 非任务物品，特定掉落
					if(RandomUtil.percentRandom(drop[0])) {
						int[] data2 = {drop[1], 1};
						addDrop(farmUser.getPos(), data2, farmUser.getUserId(), itemProtectTime);
					}
				} else {
					if(farmUser.isQuestStart(drop[2]) && RandomUtil.percentRandom(drop[0])) {
						DummyProductBean item = dummyService.getDummyProducts(drop[1]);
						UserBagCacheUtil.addUserBagCacheStack(farmUser.getUserId(), drop[1], 1);
						FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "获得了" + item.getName() + "[任务物品]");
					}
				}
			}
		}
	}
	public static int dropGradeRate[] = {200, 500, 64, 8, 1};	
	public static int dropGradeTotal;
	public static int dropGradeRate2[] = {150, 360, 56, 8, 1};	
	public static int dropGradeTotal2;
	public static int dropGradeRate3[] = {100, 250, 48, 8, 1};	
	public static int dropGradeTotal3;
	static {
		dropGradeTotal = RandomUtil.sumRate(dropGradeRate);
		dropGradeTotal2 = RandomUtil.sumRate(dropGradeRate2);
		dropGradeTotal3 = RandomUtil.sumRate(dropGradeRate3);
	}
	public static int getRandomGrade() {
		return RandomUtil.randomRateInt(dropGradeRate, dropGradeTotal);
	}
	public static int getRandomGrade(int rank) {
		if(rank <= 8)
			return RandomUtil.randomRateInt(dropGradeRate3, dropGradeTotal3);
		else if(rank <= 16)
			return RandomUtil.randomRateInt(dropGradeRate2, dropGradeTotal2);
		else
			return RandomUtil.randomRateInt(dropGradeRate, dropGradeTotal);
	}
	// 杀死了玩家
	public static void killPlayer(FarmUserBean user) {
		user.getTargetList().clear();
		MapNodeBean node = one.getMapNode(user.getPos());
		addLog(node, user.getNameWml() + "死亡");
		MapBean map = one.getMap(node.getMapId());
		if(!map.isFlagArena())		// 非竞技场有死亡惩罚
			user.kill();
	}
	// 对某场景内所有人添加日志
	public static void addLog(int pos, String string) {
		MapNodeBean node = one.getMapNode(pos);
		if(node == null)
			return;
		addLog(node, string);
	}
	public static void addLog(MapNodeBean node, String string) {
		Iterator iter = node.getPlayers().iterator();
		while(iter.hasNext()) {
			FarmUserBean user = (FarmUserBean)iter.next();
			user.addLog(string);
		}
	}
	public static void addOtherLog(FarmUserBean farmUser, String string) {
		MapNodeBean node = one.getMapNode(farmUser.getPos());
		if(node == null)
			return;
		Iterator iter = node.getPlayers().iterator();
		while(iter.hasNext()) {
			FarmUserBean user = (FarmUserBean)iter.next();
			if(user != farmUser)
				user.addLog(string);
		}
	}
	// 使用物品
	public static String useItem(FarmUserBean user, UserBagBean userbag) {
		DummyProductBean item = dummyService.getDummyProducts(userbag.getProductId());
		if(user.getProRank(FarmProBean.PRO_BATTLE) < item.getRank())
			return "战斗等级不足,无法使用该物品";
		List usageList = item.getUsageList();
		for(int ia = 0;ia < usageList.size();ia++) {
			int[] usage = (int[])usageList.get(ia);
			switch(usage[0]) {
			case 1: {		// 加血
				user.incHp(usage[1]);
			} break;
			case 2: {		// 加气力
				user.incMp(usage[1]);
			} break;
			case 3: {		// 加体力
				user.incSp(usage[1]);
			} break;
			case 4: {		// 加血百分比
				user.incHp((float)usage[1] / 100);
			} break;
			case 5: {		// 
				user.incMp((float)usage[1] / 100);
			} break;
			case 6: {		// 
				user.incSp((float)usage[1] / 100);
			} break;
			}
		}
		addLog(user.getPos(), user.getNameWml() + "使用了" + item.getName());
		return null;
	}
	// 技能消耗
	public static void skillCost(FarmUserBean user, List costList) {
		for(int ia = 0;ia < costList.size();ia++) {
			int[] usage = (int[])costList.get(ia);
			switch(usage[0]) {
			case 1: {		// 消耗血
				user.decHp(usage[1]);
			} break;
			case 2: {		// 消耗气力
				user.decMp(usage[1]);
			} break;
			case 3: {		// 消耗体力
				user.decSp(usage[1]);
			} break;
			}
		}
	}
	public static int calcDamage(int attack, int defense, int level) {
		int rate = level * 40 + 200;
		return attack * rate / (rate + defense); 
	}
	public static float calcDefenseRate(int defense, int level) {
		int rate = level * 40 + 200;
		return (float)defense / (rate + defense); 
	}
	
	public static int BATTLE_POINT_INTERVAL = 8;
	public static int[] battlePointAdd = {3, 4, 5, 6, 7, 8, 9, 10};		// 每级别加的属性点，每8级变化一次
	public static int[] rankBattlePoint;
	static {
		int sum = 0;
		int index = 0;
		
		rankBattlePoint = new int[battlePointAdd.length * BATTLE_POINT_INTERVAL + 1];
		
		rankBattlePoint[index] = sum;
		for(int i = 0;i < battlePointAdd.length;i++)
			for(int j = 0;j < BATTLE_POINT_INTERVAL;j++) {
				index++;
				sum += battlePointAdd[i];
				rankBattlePoint[index] = sum;
			}
	}
	// 专业学习或者升级后调用
	public static void updateUserPro(FarmUserBean user, FarmUserProBean userPro) {
		if(userPro.getPro() == FarmProBean.PRO_BATTLE) {		// 增加可分配的属性点
			int add = battlePointAdd[(userPro.getRank() - 1) / BATTLE_POINT_INTERVAL];
			user.setBattlePoint(user.getBattlePoint() + add);
			SqlUtil.executeUpdate("update farm_user set battle_point=battle_point+" + add + " where user_id=" + user.getUserId(), 4);
		}
	}
	// 废弃专业后调用
	public static void removeUserPro(FarmUserBean user, FarmUserProBean userPro) {
		if(userPro.getPro() == FarmProBean.PRO_BATTLE) {		// 废弃后去掉所有属性点
			user.resetAttr();
			user.setBattlePoint(0);
			user.resetCurStat();
			saveBattlePoint(user);
		}
	}
	public static void saveBattlePoint(FarmUserBean user) {
		SqlUtil.executeUpdate("update farm_user set attr1=" + user.getAttr1() + 
				",attr2=" + user.getAttr2() + ",attr3=" + user.getAttr3() + 
				",attr4=" + user.getAttr4() + ",attr5=" + user.getAttr5() + 
				",battle_point=" + user.getBattlePoint() + " where user_id=" + user.getUserId(), 4);		
	}
	// 返回怪物掉落的描述文字
	public static String creatureDropString(List dropList) {
		if(dropList == null || dropList.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < dropList.size();i++) {
			int[] p = (int[])dropList.get(i);
			if(i > 0)
				sb.append("<br/>");
			sb.append(p[0]);
			sb.append("%");
			DummyProductBean item = dummyService.getDummyProducts(p[1]);
			if(item == null) {
				sb.append("(未知)");
			} else {
				sb.append("<a href=\"editItem.jsp?id=");
				sb.append(p[1]);
				sb.append("\">");
				sb.append(item.getName());
				sb.append("</a>");
			}
			if(p.length > 2) {
				FarmQuestBean quest = FarmNpcWorld.one.getQuest(p[2]);
				sb.append("[<a href=\"editQuest.jsp?id=");
				sb.append(p[2]);
				sb.append("\">");
				if(quest == null)
					sb.append("(未知)");
				else
					sb.append(quest.getName());
				sb.append("</a>]");
			}
		}
		return sb.toString();
	}
	// 返回装备属性描述文字
	public static String itemString(int userbagId) {
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userbagId);
		return itemString(userBag);
	}
	public static StringBuilder itemString(int[] attr, StringBuilder sb) {
		if(sb == null)
			sb = new StringBuilder();
		switch(attr[0]) {
		case 1: {		// 加基础攻击力
			if(attr.length > 2) {
				if(attr[2] > 0) {
					sb.append("攻击:");
					sb.append(attr[1]);
					sb.append("-");
					sb.append(attr[1] + attr[2]);
				} else {
					sb.append("攻击+");
					sb.append(attr[1]);
				}
			} else {
				sb.append("(无效属性)");
			}
		} break;
		case 2: {		// 加浮动攻击力
			
		} break;
		case 3: {		// 加基础防御力
			sb.append("防御:");
			sb.append(attr[1]);
		} break;
		case 4: {		// 加hp
			sb.append("血+");
			sb.append(attr[1]);
		} break;
		case 5: {		// 加mp
			sb.append("气力+");
			sb.append(attr[1]);
		} break;
		case 6: {		// 加sp
			sb.append("体力+");
			sb.append(attr[1]);
		} break;
		case 9: {		// 加攻击速度
			sb.append("攻击速度+");
			sb.append(attr[1]);
		} break;
		case 10: {		// 基础攻击速度
			sb.append("速度:");
			sb.append(attr[1]);
			sb.append("(");
			sb.append(attackIntervalString(attr[1], 0));
			sb.append("秒)");
		} break;
		case 11: {		// 加属性1
			sb.append(FarmUserBean.attrName[0]);
			sb.append("+");
			sb.append(attr[1]);
		} break;
		case 12: {		// 加属性2
			sb.append(FarmUserBean.attrName[1]);
			sb.append("+");
			sb.append(attr[1]);
		} break;
		case 13: {		// 加属性3
			sb.append(FarmUserBean.attrName[2]);
			sb.append("+");
			sb.append(attr[1]);
		} break;
		case 14: {		// 加属性4
			sb.append(FarmUserBean.attrName[3]);
			sb.append("+");
			sb.append(attr[1]);
		} break;
		case 15: {		// 加属性5
			sb.append(FarmUserBean.attrName[4]);
			sb.append("+");
			sb.append(attr[1]);
		} break;
		case 21: {		// 加致命
			sb.append("致命一击+");
			sb.append(attr[1]);
			sb.append("%");
		} break;
		case 22: {		// 加爆发力
			sb.append("爆发力+");
			sb.append(attr[1]);
			sb.append("%");
		} break;
		case 24: {		// 加
			sb.append("命中+");
			sb.append(attr[1]);
		} break;
		case 25: {		// 加
			sb.append("闪避+");
			sb.append(attr[1]);
			sb.append("%");
		} break;
		case 31: {		// 加五行，金
			sb.append("金元素+");
			sb.append(attr[1]);
		} break;
		case 32: {		// 加五行
			sb.append("木元素+");
			sb.append(attr[1]);
		} break;
		case 33: {		// 加五行
			sb.append("水元素+");
			sb.append(attr[1]);
		} break;
		case 34: {		// 加五行
			sb.append("火元素+");
			sb.append(attr[1]);
		} break;
		case 35: {		// 加五行
			sb.append("土元素+");
			sb.append(attr[1]);
		} break;
		case 36: {		// 加五行
			sb.append("风元素+");
			sb.append(attr[1]);
		} break;
		case 37: {		// 加五行
			sb.append("雷元素+");
			sb.append(attr[1]);
		} break;
		}
		return sb;
	}
	public static String dropString(int[] attr) {
		if(attr[0] == 0) {
			return formatMoney(attr[1]);
		} else {
			DummyProductBean item = dummyService.getDummyProducts(attr[0]);
			if(item == null) {
				return "(未知)";
			} else {
				String ret = item.getName() + "[" + item.getGradeName() + "]";
				if(attr[1] > 1)
					ret += "x" + attr[1]; 
				return ret;
			}

		}
	}
	
	public static String itemString(UserBagBean userBag) {
		if(userBag == null) {
			return "未知";
		}
		DummyProductBean dummyProduct = dummyService.getDummyProducts(userBag.getProductId());
		if(dummyProduct == null) {
			return "未知";
		}
		return itemString(dummyProduct);
	}
	public static String itemString(DummyProductBean dummyProduct) {

		StringBuilder sb = new StringBuilder(64);
		
		List attrList = dummyProduct.getAttributeList();
		if(attrList.size() > 0) {
			sb.append("--装备属性--<br/>");
			for(int ia = 0;ia < attrList.size();ia++) {
				int[] attr = (int[])attrList.get(ia);
				itemString(attr, sb);
				sb.append("<br/>");
			}
		}
		
		List usageList = dummyProduct.getUsageList();
		if(usageList.size() > 0) {
			sb.append("--使用效果--<br/>");
			for(int ia = 0;ia < usageList.size();ia++) {
				int[] attr = (int[])usageList.get(ia);
				usageString(attr, sb);
				sb.append("<br/>");
			}
		}
		return sb.toString();
	}

	public static StringBuilder usageString(int[] attr, StringBuilder sb) {
		if(sb == null)
			sb = new StringBuilder();
		switch(attr[0]) {
		case 1: {		// 加基础攻击力
			sb.append("血+");
			sb.append(attr[1]);
		} break;
		case 2: {		// 加浮动攻击力
			sb.append("气力+");
			sb.append(attr[1]);
		} break;
		case 3: {		// 加浮动攻击力
			sb.append("体力+");
			sb.append(attr[1]);
		} break;
		case 4: {		// 加基础攻击力
			sb.append("血+");
			sb.append(attr[1]);
			sb.append('%');
		} break;
		case 5: {		// 加浮动攻击力
			sb.append("气力+");
			sb.append(attr[1]);
			sb.append('%');
		} break;
		case 6: {		// 加浮动攻击力
			sb.append("体力+");
			sb.append(attr[1]);
			sb.append('%');
		} break;
		}
		return sb;
	}
	public static String equipString(DummyProductBean dummyProduct) {
		StringBuilder sb = new StringBuilder(64);
		
		List attrList = dummyProduct.getAttributeList();
		if(attrList.size() > 0) {
			for(int ia = 0;ia < attrList.size();ia++) {
				int[] attr = (int[])attrList.get(ia);
				itemString(attr, sb);
				sb.append("<br/>");
			}
		}
		return sb.toString();
	}
	public static StringBuilder itemSetString(ItemSetBean set, int count) {
		return itemSetString(set, count, null);
	}
	public static StringBuilder itemSetString(ItemSetBean set, StringBuilder sb) {
		return itemSetString(set, 0, sb);
	}
	public static StringBuilder itemSetString(ItemSetBean set, int count, StringBuilder sb) {
		if(sb == null)
			sb = new StringBuilder();
		if(set != null) {
			List attrList = set.getAttributeList();
			List countList = set.getCountList();
			for(int ia = 0;ia < attrList.size();ia++) {
				int[] attr = (int[])attrList.get(ia);
				Integer c = (Integer)countList.get(ia);
				sb.append(c);
				sb.append("件:");
				itemString(attr, sb);
				if(c.intValue() <= count)
					sb.append("(生效)");
				sb.append("<br/>");
			}
		}
		return sb;
	}
	public static int attackInterval(int base, int attackSpeedAdd) {		// 毫秒为单位
		return 4000000 / base / (100 + attackSpeedAdd);
	}
	
	public static String attackIntervalString(int base, int attackSpeedAdd) {	// 秒为单位
		return formatNumber(4000f / base / (100 + attackSpeedAdd));
	}
	
	static DecimalFormat numFormat = new DecimalFormat("0.0");
	public static String formatNumber(float number) {
		return numFormat.format(number);
	}
	public static void logout(int id) {
		FarmUserBean farmUser = one.getFarmUserCache(id);
		if(farmUser != null) {
			MapNodeBean node = one.getMapNode(farmUser.getPos());
			if(node != null && node.removePlayer(farmUser)) {
				addLog(node, farmUser.getNameWml() + "的躯体渐渐消失了");
			}
			farmUser.setFlag(0, true);
		}
	}
	
	// 组队
	public static GroupUserBean addGroupUser(int id) {
		GroupUserBean user = new GroupUserBean();
		user.setUserId(id);
		groupUserMap.put(Integer.valueOf(id), user);
		return user;
	}
	
	public static void removeGroupUser(int id) {
		groupUserMap.remove(Integer.valueOf(id));
	}
	
	public static GroupUserBean getGroupUser(int id) {
		return (GroupUserBean)groupUserMap.get(Integer.valueOf(id));
	}
	
	public static void userLeaveGroup(FarmUserBean user) {
		GroupBean group = user.getGroup();
		user.setGroup(null);
		FarmWorld.removeGroupUser(user.getUserId());

		group.removeUser(user.getUserId());
	}
	
	public static String getDirectionString(MapNodeBean node, MapNodeBean node2) {
		if(node == node2)
			return "身边";
		int difX = node2.x - node.x;
		int difY = node2.y - node.y;
		String ret = "";
		if(difX < 0)
			ret += "西" + Math.abs(difX);
		else if(difX > 0)
			ret += "东" + Math.abs(difX);
		if(difY < 0)
			ret += "北" + Math.abs(difY);
		else if(difY > 0)
			ret += "南" + Math.abs(difY);
		return ret;
	}
	
	// 返回一个结点的名字，包括上级区域
	public String getNodeDetail(int nodeId) {
		return getNodeDetail(getMapNode(nodeId));
	}
	public String getNodeDetail(MapNodeBean node) {
		if(node == null)
			return "未知区域";
		MapBean map = getMap(node.getMapId());
		if(map == null)
			return node.getName() + ",未知区域";
		return node.getName() + "," + map.getName();
	}
	// 返回一个地图的名字，包含父地区
	public String getMapDetail(MapBean map) {
		if(map == null)
			return "未知区域";
		if(map.getParent() == 0)
			return map.getName();
		MapBean mapP = getMap(map.getParent());
		if(mapP == null)
			return map.getName();
		return  map.getName() + "," + mapP.getName();
	}
	
	// 返回修理需要的钱
	public static int getRepairMoney(DummyProductBean item, int time) {
		return item.getPrice() * (item.getTime() - time) / item.getTime();
	}
	
	// 改变装备耐久度
	public static void incEquipDurability(UserBagBean userbag, int add) {
		int time = userbag.getTime() + add;

		DummyProductBean item = getItem(userbag.getProductId());
		if(time > item.getTime()) {
			time = item.getTime();
		}
		UserBagCacheUtil.updateUserBagTime(userbag, time);
	}
	public static void decEquipDurability(UserBagBean userbag, int add) {
		int time = userbag.getTime() - add;
		
		if(time < 1)
			UserBagCacheUtil.updateUserBagTime(userbag, 1);
		else
			UserBagCacheUtil.updateUserBagTime(userbag, time);
	}
	
	// 改变所有装备的耐久度
	public static void decEquipsDurability(FarmUserBean user, float rate) {
		FarmUserEquipBean[] equips = user.getEquip();
		for(int i = 0;i < equips.length;i++) {
			FarmUserEquipBean equip = equips[i];
			if(equip != null && equip.getUserbagId() != 0) {
				UserBagBean userbag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
				DummyProductBean item = getItem(userbag.getProductId());
				decEquipDurability(userbag, (int) (item.getTime() * rate));
			}
		}
	}
	
	public static String getDurabilityInfo(int time, int total) {
		if(time < 2)
			return "(损毁)";
		if(time < total / 5)
			return "(损坏)";
		if(time < total / 2)
			return "(破损)";
		return "";
	}
	// 判断是不是组队成员
	public static boolean isGroup(FarmUserBean user1, FarmUserBean user2) {
		if(user1.getGroup() == null)
			return false;
		return user1.getGroup() == user2.getGroup();
	}
	static Pattern infoPattern = Pattern.compile("%([0-9]+)");
	// 替换特殊字符
	public static String replaceInfo(String content, FarmUserBean user) {
		
		Matcher m = infoPattern.matcher(content);
        StringBuilder sb = new StringBuilder(64);
        int pos = 0;
        String ma = null;
        while (m.find()) {
            sb.append(content.substring(pos, m.start(0)));
            ma = m.group(1);
            if(ma.equals("1")){
                sb.append(user.getNameWml());
            } else if(ma.equals("2")) {
            	sb.append(user.getClass1Name());
            } else if(ma.equals("3")) {
            	sb.append(user.getElementName());
            }
            pos = m.end(1);
        }
        sb.append(content.substring(pos));
		return sb.toString();
	}
	
	public static UserTriggerBean getUserTrigger(int id) {
		return getUserTrigger(Integer.valueOf(id));
	}
	public static UserTriggerBean getUserTrigger(Integer key) {
		synchronized(userTriggerCache) {
			UserTriggerBean bean = (UserTriggerBean) userTriggerCache.get(key);
			if (bean == null) {
				bean = service.getUserTrigger("id=" + key);
				if(bean != null) {
					userTriggerCache.put(key, bean);
				}
			}
			return bean;
		}
	}
	// 完成触发
	public static void addUserTrigger(FarmUserBean farmUser, TriggerBean trigger) {
		if(farmUser.isTriggered(trigger.getFlag()))		// 重复触发不再保存
			return;
		UserTriggerBean userTrigger = new UserTriggerBean();
		userTrigger.setUserId(farmUser.getUserId());
		userTrigger.setTriggerId(trigger.getId());

		service.addUserTrigger(userTrigger);
		userTriggerCache.put(Integer.valueOf(userTrigger.getId()), userTrigger);
		farmUser.endTrigger(userTrigger);
	}
}
