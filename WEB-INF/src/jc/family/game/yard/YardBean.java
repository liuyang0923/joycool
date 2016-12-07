package jc.family.game.yard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 家族yard信息
 * 
 * @author qiuranke
 * 
 */
public class YardBean {

	int fmid;
	int money;// 农币兑换乐币以万为单位
	long createTime;
	String name;
	String info;
	int exp;
	int rank; // 等级

	int landRank; // 地的数量
	int plantRank;// 加工厂的等级
	int mateRank; // 库房等级，增加容量百分比
	int kitchenRank; // 厨房等级，增加同时烹饪数量百分比
	int kitchenRank2; // 冰箱等级，增加菜肴容量百分比
	int kitchenRank3; // 中级厨房的等级
	int factoryRank; // 工厂等级

	int deploying = 0; // 正在烹饪中的总数

	HashMap itemMap = new HashMap();// 物品
	HashSet recipeSet = new HashSet(); // 初级食谱
	HashSet middleRecipeSet = new HashSet(); // 中级食谱
	HashSet worksRecipeSet = new HashSet(); // 工厂资料
	LinkedList cookList = new LinkedList();// 烹饪进度
	SimpleGameLog productLog = new SimpleGameLog(200);
	SimpleGameLog kitchenLog = new SimpleGameLog(200);

	List landList; // 土地列表
	List plantList; // 工厂列表

	long lastProduceTime; // 上一次生产完成时间
	int coopRate; // 协作度 0 - n
	int lastProduceId;

	int totalDishCount; // 总菜肴存量

	public int getDeploying() {
		return deploying;
	}

	public void setDeploying(int deploying) {
		this.deploying = deploying;
	}

	public void addTotalDishCount(int add) {
		totalDishCount += add;
	}

	public int getTotalDishCount() {
		return totalDishCount;
	}

	public void setTotalDishCount(int totalDishCount) {
		this.totalDishCount = totalDishCount;
	}

	public int getLastProduceId() {
		return lastProduceId;
	}

	public void setLastProduceId(int lastProduceId) {
		this.lastProduceId = lastProduceId;
	}

	public int getCoopRate() {
		return coopRate;
	}

	public void setCoopRate(int coopRate) {
		this.coopRate = coopRate;
	}

	public static long maxCoopTime = 10000l;

	public void checkCoopRate() {
		long now = System.currentTimeMillis();
		if (now - lastProduceTime > maxCoopTime) {
			coopRate = 0;
		} else if (coopRate < 10) {
			coopRate++;
		}
		lastProduceTime = now;
	}

	public YardBean() {
		recipeSet.add(new Integer(2));
	}

	public long getLastProduceTime() {
		return lastProduceTime;
	}

	public void setLastProduceTime(long lastProduceTime) {
		this.lastProduceTime = lastProduceTime;
	}

	public String getName() {
		return name;
	}

	public String getNameWml() {
		return StringUtil.toWml(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFmid() {
		return fmid;
	}

	public void setFmid(int fmid) {
		this.fmid = fmid;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void addMoney(int money) {
		this.money += money;
		SqlUtil.executeUpdate("update fm_yard_info set money=" + this.money
				+ " where fmid=" + fmid, 5);
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	// 餐厅的各种容量升级，以及对应的容量
	public static int[] landUpdateMoney = { 500, 1000, 2000, 4000, 8000, 16000,
			32000, 64000, 128000, 256000, 512000, 1024000 }; // 土地升级需要的钱
	public static int[] landCounts = { 4, 5, 6, 7, 8, 9, 10 };
	public static int[] landPrice = { 2000, 4000, 8000, 16000, 32000, 64000,
			128000, 256000, 512000, 1024000 };
	public static int[] mateCounts = { 100, 200, 300, 400, 500, 600, 700, 800,
			900, 1000 };
	public static int[] mateCountsMoney = { 1000, 2000, 4000, 8000, 16000,
			32000, 64000, 128000, 256000, 512000 };
	public static int[] kitchenCounts = { 5, 10, 15, 20, 25, 30, 35, 40, 45,
			100 };
	public static int[] kitchenCountsMoney = { 1000, 2000, 4000, 8000, 16000,
			32000, 64000, 128000, 256000, 512000 };
	public static int[] kitchenCounts2 = { 100, 200, 300, 400, 500, 600, 700,
			800, 900, 1000 };
	public static int[] kitchenCounts2Money = { 1000, 2000, 4000, 8000, 16000,
			32000, 64000, 128000, 256000, 512000 };
	// 中级厨房用
	public static int[] kitchenCounts3 = { 5, 10, 15, 20, 25, 30, 35, 40, 45,
			100 };
	public static int[] kitchenCounts3Money = { 1000, 2000, 4000, 8000, 16000,
			32000, 64000, 128000, 256000, 5120000 };
	// 工厂等级
	public static int[] factoryRankCounts = { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	public static int[] factoryRankMoney = { 1000, 2000, 4000, 8000, 16000,
			32000, 64000, 128000, 256000, 512000 };
	// 车间价格
	public static int[] factoryMoney = { 1000, 2000, 4000, 8000, 16000, 32000,
			64000, 128000, 256000, 512000, 1024000, 2048000, 4096000 };

	// 返回当前数量所需要的价格
	public int getFactoryMoney(int count) {
		return factoryMoney[count > factoryMoney.length - 1 ? factoryMoney.length - 1
				: count];
	}

	public int getFactoryMoneyCount() {
		return factoryMoney.length;
	}

	public static int KITCHEN_ENTER_EXP = 300; // 进入中级厨房需要的经验值
	public static int FACTORY_ENTER_EXP = 120; // 进入工厂需要的经验值
	
	public boolean canEnterKitchen() {
		return getExp() >= KITCHEN_ENTER_EXP;
	}
	public boolean canEnterFactory() {
		return getExp() >= FACTORY_ENTER_EXP;
	}

	public int getLandRank() {
		return landRank;
	}

	public void setLandRank(int landRank) {
		this.landRank = landRank;
	}

	public int getPlantRank() {
		return plantRank;
	}

	public void setPlantRank(int plantRank) {
		this.plantRank = plantRank;
	}

	public int getMateRank() {
		return mateRank;
	}

	public void setMateRank(int mateRank) {
		this.mateRank = mateRank;
	}

	public int getFactoryRank() {
		return factoryRank;
	}

	public void setFactoryRank(int factoryRank) {
		this.factoryRank = factoryRank;
	}

	public int getKitchenRank() {
		return kitchenRank;
	}

	public void setKitchenRank(int kitchenRank) {
		this.kitchenRank = kitchenRank;
	}

	public int getKitchenRank2() {
		return kitchenRank2;
	}

	public void setKitchenRank2(int kitchenRank2) {
		this.kitchenRank2 = kitchenRank2;
	}

	public int getKitchenRank3() {
		return kitchenRank3;
	}

	public void setKitchenRank3(int kitchenRank3) {
		this.kitchenRank3 = kitchenRank3;
	}

	public int getkitchenCountsMoney(int rank) {
		return kitchenCountsMoney[rank > kitchenCountsMoney.length - 1 ? kitchenCountsMoney.length - 1
				: rank];
	}

	public int getkitchenCountsMoney3(int rank) {
		return kitchenCounts3Money[rank > kitchenCounts3Money.length - 1 ? kitchenCounts3Money.length - 1
				: rank];
	}

	public int getFactoryRankMoney(int factoryRank) {
		return factoryRankMoney[factoryRank > factoryRankMoney.length - 1 ? factoryRankMoney.length - 1
				: factoryRank];
	}

	public int gekitchenCounts(int rank) {
		return kitchenCounts[rank > kitchenCounts.length - 1 ? kitchenCounts.length - 1
				: rank];
	}

	public int gekitchenCounts2(int rank) {
		return kitchenCounts2[rank > kitchenCounts2.length - 1 ? kitchenCounts2.length - 1
				: rank];
	}

	public int getKitchenCounts3(int rank) {
		return kitchenCounts3[rank > kitchenCounts3.length - 1 ? kitchenCounts3.length - 1
				: rank];
	}

	public int getFactoryRankCounts(int factoryRank) {
		return factoryRankCounts[factoryRank > factoryRankCounts.length - 1 ? factoryRankCounts.length - 1
				: factoryRank];
	}

	public HashMap getItemMap() {
		return itemMap;
	}

	public YardItemBean getItem(int itemId) {
		if (itemMap == null) {
			return null;
		}
		return (YardItemBean) itemMap.get(Integer.valueOf(itemId));
	}

	public void setItemMap(List list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				YardItemBean item = (YardItemBean) list.get(i);
				itemMap.put(Integer.valueOf(item.getItemId()), item);
			}
		}
	}

	public void setItemMap(HashMap itemMap) {
		this.itemMap = itemMap;
	}

	public HashSet getRecipeSet() {
		return recipeSet;
	}

	public HashSet getMiddleRecipeSet() {
		return middleRecipeSet;
	}

	public HashSet getWorksRecipeSet() {
		return worksRecipeSet;
	}

	/**
	 * 得到物品列表
	 * 
	 * @return
	 */
	public List getItemList() {
		if (itemMap == null) {
			return null;
		}
		return new ArrayList(itemMap.values());
	}

	/**
	 * 是否有食谱
	 * 
	 * @param recipId
	 * @return
	 */
	public boolean isRecipeSet(int recipId) {
		return recipeSet.contains(Integer.valueOf(recipId));
	}

	/**
	 * 设置食谱set
	 * 
	 * @param list
	 */
	public void setRecipeSet(List list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Integer item = (Integer) list.get(i);
				recipeSet.add(item);
			}
		}
	}

	public void setRecipeSet(HashSet recipeSet) {
		this.recipeSet = recipeSet;
	}

	/**
	 * 是否有中级食谱
	 * 
	 * @param recipId
	 * @return
	 */
	public boolean isMiddleRecipeSet(int recipId) {
		return middleRecipeSet.contains(Integer.valueOf(recipId));
	}

	/**
	 * 设置中级食谱set
	 * 
	 * @param list
	 */
	public void setMiddleRecipeSet(List list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Integer item = (Integer) list.get(i);
				middleRecipeSet.add(item);
			}
		}
	}

	public void setMiddleRecipeSet(HashSet middleRecipeSet) {
		this.middleRecipeSet = middleRecipeSet;
	}

	/**
	 * 是否有工厂资料
	 * 
	 * @param recipId
	 * @return
	 */
	public boolean isWorksRecipeSet(int recipId) {
		return worksRecipeSet.contains(Integer.valueOf(recipId));
	}

	/**
	 * 设置工厂资料set
	 * 
	 * @param list
	 */
	public void setWorksRecipeSet(List list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Integer item = (Integer) list.get(i);
				worksRecipeSet.add(item);
			}
		}
	}

	public void setWorksRecipeSet(HashSet worksRecipeSet) {
		this.worksRecipeSet = worksRecipeSet;
	}

	public LinkedList getCookList() {
		return cookList;
	}

	public void setCookList(LinkedList cookList) {
		this.cookList = cookList;
	}

	public void setCookList(List cookList) {
		this.cookList.addAll(cookList);
	}

	public int getLandCount() {
		return landCounts[landRank];
	}

	public SimpleGameLog getKitchenLog() {
		return kitchenLog;
	}

	// ----------- 生产基地动态
	public SimpleGameLog getProductLog() {
		return productLog;
	}

	public int getItemCount(int itemId) {
		YardItemBean item = (YardItemBean) itemMap.get(Integer.valueOf(itemId));
		return item != null ? item.getNumber() : 0;
	}

	/**
	 * 检查材料是否足够
	 * 
	 * @param material
	 * @return
	 */
	public boolean checkMaterial(List material) {
		Iterator iter = material.iterator();
		while (iter.hasNext()) {
			int[] mat = (int[]) iter.next();
			if (mat[1] > getItemCount(mat[0])) // 材料大于用户拥有的此类物品总和，按个数算，堆叠算，次数不算
				return false;
		}
		return true;
	}

	/**
	 * 删除材料
	 * 
	 * @param material
	 */
	public void removeMaterial(List material) {
		Iterator iter = material.iterator();
		while (iter.hasNext()) {
			int[] mat = (int[]) iter.next();
			removeMaterial(mat[0], mat[1]);
		}
	}

	/**
	 * 添加材料
	 * 
	 * @param material
	 */
	public void addMaterial(List material, int count) {
		Iterator iter = material.iterator();
		while (iter.hasNext()) {
			Integer mat = (Integer) iter.next();
			addMaterial(mat.intValue(), count);
		}
	}

	/**
	 * 删除单个种类的材料
	 * 
	 * @param itemId
	 * @param count
	 */
	public boolean removeMaterial(int itemId, int count) {
		YardItemBean item = getItem(itemId);
		if (item == null) {
			return false;
		}
		boolean result = count <= item.getNumber();
		item.addNumber(-count);
		SqlUtil.executeUpdate(
				"update fm_yard_fm_item set number=" + item.getNumber()
						+ " where fmid=" + fmid + " and itemid="
						+ item.getItemId(), 5);
		return result;
	}

	/**
	 * 增加单个种类的材料
	 * 
	 * @param itemId
	 * @param count
	 */
	public void addMaterial(int itemId, int count) {
		YardItemBean item = getItem(itemId);

		if (item == null) {
			int id = YardAction.yardService
					.upd("insert into fm_yard_fm_item(fmid,itemid,number)values("
							+ fmid + "," + itemId + "," + count + ")");
			item = new YardItemBean();
			item.setFmid(fmid);
			item.setItemId(itemId);
			item.setId(id);
			item.setNumber(count);
			itemMap.put(Integer.valueOf(itemId), item);
		} else {
			YardItemProtoBean itemProto = YardAction.getItmeProto(itemId);

			int max = 1000000000;
			if (itemProto.getType() != 3) {
				max = itemProto.getCapacity() * mateCounts[mateRank] / 100;
			}
			item.addNumber(count);
			if (item.getNumber() > max) {
				item.setNumber(max);
			}
			SqlUtil.executeUpdate(
					"update fm_yard_fm_item set number=" + item.getNumber()
							+ " where fmid=" + fmid + " and itemid="
							+ item.getItemId(), 5);
		}
	}

	public int getMateCount() {
		return mateCounts[mateRank];
	}

	public int getKitchenCount() {
		return kitchenCounts[kitchenRank];
	}

	public int getKitchen2Count() {
		return kitchenCounts2[kitchenRank2];
	}

	public int getKitchen3Count() {
		return kitchenCounts3[kitchenRank3];
	}

	// 添加菜肴
	public void addDish(int itemId, int count) {
		YardItemBean item = getItem(itemId);
		YardItemProtoBean itemProto = YardAction.getItmeProto(itemId);
		int left = kitchenCounts2[kitchenRank2] * 10 - totalDishCount;
		if (left <= 0)
			return;
		if (count > left)
			count = left;

		if (item == null) {
			int id = YardAction.yardService
					.upd("insert into fm_yard_fm_item(fmid,itemid,number)values("
							+ fmid + "," + itemId + "," + count + ")");
			item = new YardItemBean();
			item.setFmid(fmid);
			item.setItemId(itemId);
			item.setId(id);
			item.setNumber(count);
			itemMap.put(Integer.valueOf(itemId), item);
		} else {

			item.addNumber(count);
			SqlUtil.executeUpdate(
					"update fm_yard_fm_item set number=" + item.getNumber()
							+ " where fmid=" + fmid + " and itemid="
							+ item.getItemId(), 5);
		}
		addTotalDishCount(count);
	}

	public void addDish(List material, int count) {
		Iterator iter = material.iterator();
		while (iter.hasNext()) {
			Integer mat = (Integer) iter.next();
			addDish(mat.intValue(), count);
		}
	}

	/**
	 * 检查，如果成功则删除材料(包括扣钱)，加产品
	 * 
	 * @param material
	 * @param price
	 * @param product
	 * @param count
	 * @return
	 */
	public int compose(List material, int price, List product, int count) {
		synchronized (this) {
			if (material != null) {
				if (!checkMaterial(material)) {
					return -1; // 源料不够
				}
			}
			if (price != 0) {
				if (money < price) {
					return -2; // 没钱
				}
				// 开始扣除
				addMoney(-price);
			}
			if (material != null)
				removeMaterial(material);
			if (product != null) {
				addMaterial(product, count);
			}
		}
		return 0;
	}

	/**
	 * 卖菜
	 * 
	 * @param itemId
	 * @param count
	 * @return 失败返回-1,成功返回所卖出的钱数
	 */
	public int sell(int itemId, int count) {
		int count2 = count * 10; // 10个数量算一份！
		if (getItemCount(itemId) < count2) {
			return -1;
		}
		addTotalDishCount(-count2);
		removeMaterial(itemId, count2);
		YardItemProtoBean proto = YardAction.getItmeProto(itemId);
		int result = proto.getPrice() * count;
		addMoney(result);
		return result;
	}

	/**
	 * 购买
	 * 
	 * @param id
	 *            要购买的物品ID
	 * @param price
	 *            总价格
	 * @param count
	 *            总数量
	 * @param buy
	 *            购买类型.0:调味料,1:食谱
	 * @return 返回-1:参数错误,-2:钱不够,0:成功购买
	 */
	public int buy(int id, int price, int count, int buy) {
		if (id <= 0 || count <= 0 || price < 0)
			return -1;
		synchronized (this) {
			if (getMoney() < price)
				return -2;
			if (buy == 0) {
				addMaterial(id, count);
			} else {
				addFmRecipe(id);
			}
			addMoney(-price);
			return 0;
		}
	}

	/**
	 * 给本家族加上食谱(同时加数据库和缓存)
	 * 
	 * @param recipId
	 */
	public void addFmRecipe(int recipeId) {
		if (recipeId <= 0)
			return;
		YardRecipeProtoBean recipe = YardAction.getRecipeProto(recipeId);
		if (recipe == null)
			return;
		SqlUtil.executeUpdate(
				"insert into fm_yard_fm_recipe (fmid,recipeid) values ("
						+ getFmid() + "," + recipeId + ")", 5);
		// ******修改:不同类型的食谱放到不同类型的缓存中******
		if (recipe.getType() == 0)
			recipeSet.add(new Integer(recipeId));
		else if (recipe.getType() == 1)
			middleRecipeSet.add(new Integer(recipeId));
		else if (recipe.getType() == 5)
			worksRecipeSet.add(new Integer(recipeId));
	}

	/**
	 * 返回特定物品类别列表(列表中存的是YardItemBean)
	 * 
	 * @return
	 */
	public List getItemListByType(int type) {
		List list = new ArrayList();
		if (itemMap == null)
			return list;
		Entry entry = null;
		YardItemBean item = null;
		Iterator it = itemMap.entrySet().iterator();
		while (it.hasNext()) {
			entry = (Entry) it.next();
			item = (YardItemBean) entry.getValue();
			if (item != null && item.getItemType() == type
					&& item.getNumber() > 0) {
				list.add(item);
			}
		}
		return list;
	}

	// 经验值够了自动升级
	public static int MAX_RANK = 10;
	public static int[] rankExps = { 50, 200, 450, 800, 1250, 1800, 2450, 3200,
			4050, 5000, 17500, 2500, 5000, 10000, 20000, 35000, 60000, 100000 };

	public void addExp(int add) {
		exp += add;
		if (rank < MAX_RANK && exp >= rankExps[rank]) {
			++rank;
			SqlUtil.executeUpdate("update fm_yard_info set exp=" + exp
					+ ",rank=" + rank + " where fmid=" + fmid, 5);
		} else {
			SqlUtil.executeUpdate("update fm_yard_info set exp=" + exp
					+ " where fmid=" + fmid, 5);
		}

	}

	/**
	 * 种植
	 * 
	 * @param seedId
	 *            种植的种子ID
	 * @param count
	 *            数量
	 * @param uid
	 *            谁种的 land : 第几块地，0 - ?
	 * @return 0:成功 -1:参数错误<br/>
	 *         -2:已没有空地<br/>
	 */
	public int plant(int seedId, int count, int uid, YardLandBean land) {
		if (seedId <= 0 || count <= 0 || uid <= 0)
			return -1;
		synchronized (this) {
			// 扣物品
			removeMaterial(seedId, count);
			// 种上
			land.setCount(count);
			land.setSeedId(seedId);
			land.setUserId(uid);
			land.setPlantTime(System.currentTimeMillis());
			SqlUtil.executeUpdate("update fm_yard_fm_land set count=" + count
					+ ",seed_id=" + seedId + ",plant_time=now(),user_id=" + uid
					+ " where id=" + land.getId(), 5);
		}
		return 0;
	}

	/**
	 * 收获
	 * 
	 * @param landId
	 *            要收获的土地ID
	 * @return 0:成功<br/>
	 *         -1:参数错误<br/>
	 *         -2:不是本家族的土地<br/>
	 *         -3:仍未成熟<br/>
	 *         -4:地已经空了
	 */
	public int reap(int index, HttpServletRequest request) {
		synchronized (this) {
			if (index < 0 || index >= landList.size())
				return -1;
			YardLandBean land = (YardLandBean) landList.get(index);
			if (land.getCount() == 0) {
				return -4;
			} else if (land.getFmid() != fmid) {
				return -2;
			} else if (!land.isOk()) {
				return -3;
			}
			int newitem = 0;
			YardItemProtoBean yardItem = null;

			yardItem = land.getItemOnLand();
			newitem = yardItem.getRandomProduct();
			addMaterial(newitem, 1);
			yardItem = YardAction.getItmeProto(newitem);

			request.setAttribute("tip", "恭喜您收获了" + yardItem.getName());
			land.setCount(land.getCount() - 1);
			if (land.getCount() == 0) {
				// 清空当前土地
				land.setSeedId(0);
				SqlUtil.executeUpdate(
						"update fm_yard_fm_land set `count`=0,user_id=0,seed_id=0 where id="
								+ land.getId(), 5);
			} else {
				SqlUtil.executeUpdate("update fm_yard_fm_land set `count`="
						+ land.getCount() + " where id=" + land.getId(), 5);
			}
			return 0;
		}
	}

	// 升级土地容量
	public int updateLand(int index) {
		synchronized (this) {
			if (index < 0 || index >= landList.size())
				return -1;
			YardLandBean land = (YardLandBean) landList.get(index);
			int price = landUpdateMoney[land.getRank()];
			if (land.getRank() > 4 || getMoney() < price) {
				return -4;
			}

			addMoney(-price);

			land.setRank(land.getRank() + 1);

			SqlUtil.executeUpdate(
					"update fm_yard_fm_land set `rank`=" + land.getRank()
							+ " where id=" + land.getId(), 5);

			return 0;
		}
	}

	// 升级工厂容量
	public int updatePlant(int index) {
		synchronized (this) {
			if (index < 0 || index >= plantList.size())
				return -1;
			YardPlantBean plant = (YardPlantBean) plantList.get(index);
			int price = landUpdateMoney[plant.getRank()];
			if (plant.getRank() > 4 || getMoney() < price) {
				return -4;
			}

			addMoney(-price);

			plant.setRank(plant.getRank() + 1);

			SqlUtil.executeUpdate(
					"update fm_yard_fm_plant set `rank`=" + plant.getRank()
							+ " where id=" + plant.getId(), 5);

			return 0;
		}
	}

	/**
	 * 买地
	 * 
	 * @return
	 */
	public boolean buyLand() {
		synchronized (this) {
			int price = landPrice[getLandRank()];
			if (getMoney() < price || getLandRank() >= YardAction.LAND_MAX_RANK) {
				return false;
			} else {
				YardAction.yardService.addLand(1, this);
				SqlUtil.executeUpdate(
						"update fm_yard_info set land_rank=land_rank+1 where fmid="
								+ fmid, 5);
				setLandRank(getLandRank() + 1);
				addMoney(-price);
				return true;
			}
		}
	}

	/**
	 * 买工厂
	 * 
	 * @param recipeId
	 *            此工厂所能生产的菜谱ID
	 * @param price
	 *            价格
	 * @return
	 */
	public boolean buyPlant(int recipeId, int price) {
		synchronized (this) {
			if (recipeId <= 0)
				return false;
			YardRecipeProtoBean recipe = YardAction.getRecipeProto(recipeId);
			if (recipe == null)
				return false;
			if (getMoney() < price) {
				return false;
			} else {
				YardAction.yardService.addPlant(1, this, recipe.getId());
				SqlUtil.executeUpdate(
						"update fm_yard_info set plant_rank=plant_rank+1 where fmid="
								+ fmid, 5);
				setPlantRank(getPlantRank() + 1);
				addMoney(-price);
				return true;
			}
		}
	}

	/**
	 * 卖出工厂
	 * 
	 * @param money
	 * @param plantId
	 * @return
	 */
	public boolean sellPlant(int money, YardPlantBean plant) {
		if (money <= 0 || plant == null || plant.getFmid() != this.getFmid())
			return false;
		// 删除缓存
		List list = getPlantList();
		if (list != null)
			list.remove(plant);
		else
			return false;
		// 删除数据库
		SqlUtil.executeUpdate(
				"delete from fm_yard_fm_plant where id=" + plant.getId(), 5);
		// 加钱
		addMoney(money);
		return true;
	}

	public YardLandBean getLand(int index) {
		if (index < 0 || index >= landList.size())
			return null;
		return (YardLandBean) landList.get(index);
	}

	public List getLandList() {
		return landList;
	}

	public void setLandList(List landList) {
		this.landList = landList;
	}

	public YardPlantBean getPlant(int index) {
		if (index < 0 || index >= plantList.size())
			return null;
		return (YardPlantBean) plantList.get(index);
	}

	public YardPlantBean getPlant2(int id) {
		if (plantList != null && plantList.size() > 0) {
			YardPlantBean plant = null;
			for (int i = 0; i < plantList.size(); i++) {
				plant = (YardPlantBean) plantList.get(i);
				if (plant.getId() == id)
					return plant;
			}
		}
		return null;
	}

	public List getPlantList() {
		return plantList;
	}

	public void setPlantList(List plantList) {
		this.plantList = plantList;
	}

	/**
	 * 向锅里放入一种材料
	 * 
	 * @param cookList
	 *            某道菜的步骤列表
	 * @param materialId
	 *            放入的材料ID
	 * @param deploy
	 *            配菜列表
	 * @param deployingBean
	 *            谁正在做这道菜
	 * @param user
	 *            当前登陆用户
	 * @return -1:成功 0:错误 1:正常操作完成
	 */
	public synchronized int useMaterial(HttpServletRequest request,
			List cookList, int materialId, YardDeployBean deploy,
			YardDeployingBean deployingBean, UserBean user) {
		if (cookList == null || cookList.size() == 0 || materialId <= 0
				|| deploy == null || user == null) {
			request.setAttribute("tip", "参数错误.");
			return 0;
		}
		if (deploy.getFmid() != getFmid()) {
			request.setAttribute("tip", "不是您家族的配菜.");
			return 0;
		}

		// 根据当前的步骤，看是不是该放入materialId。如不是，则isCurrent=false
		CookBean2 cook = null;
		if (deploy.getStep() > cookList.size()) {
			request.setAttribute("tip", "参数错误.");
			return 0;
		} else {
			cook = (CookBean2) cookList.get(deploy.getStep() - 1);
			if (cook == null) {
				request.setAttribute("tip", "参数错误.");
				return 0;
			}
		}
		// 扣除
		if (deploy.materialCount(materialId) <= 0) {
			request.setAttribute("tip", "无材料.");
			return 0;
		}

		if (deploy.getIsCurrent2()) { // 只要错一次，下面就不用验证了。
			if (cook.getMaterialId() != materialId) {
				SqlUtil.executeUpdate(
						"update fm_yard_deploy set is_current=0 where id="
								+ deploy.getId(), 5);
				deploy.setIsCurrent(0);
			}
		}

		// 标记为“我”正在做此菜.这里只有第一次放菜时才会被执行到.
		if (deployingBean == null) {
			deployingBean = new YardDeployingBean();
			deployingBean.setFmid(this.fmid);
			deployingBean.setUid(user.getId());
			deployingBean.setDeployId(deploy.getId());
			deployingBean.setRecipeId(deploy.getProtoId());
			deployingBean.setCreateTime(System.currentTimeMillis());
			int lastId = YardAction.yardService.addDeploying(deployingBean);
			deployingBean.setId(lastId);
			deploying++; // 烹饪数++
			deploy.setInuse(1);
			SqlUtil.executeUpdate("update fm_yard_deploy set inuse=1 where id="
					+ deploy.getId(), 5);
		}
		// 放材料
		deploy.cutItemToList(materialId);
		if (deploy.getStep() >= cookList.size()) {
			// 已经完成了
			YardRecipeProtoBean recpie = YardAction.getRecipeProto(deploy
					.getProtoId());
			if (recpie == null) {
				request.setAttribute("tip", "参数错误.");
				return 0;
			}
			if (deploy.getIsCurrent2()) {
				// 做出一盘怎样的料理呢?
				int point = (int) (System.currentTimeMillis() - deploy
						.getStartTime()) / (cook.getNeedTime() / 100);
//				 System.out.println("第" + cook.getStep() + "步得分:" + point);
				int result = (deploy.getTotalPoint() + point) / cookList.size(); // 算平均分
				// System.out.println("总分:" + (deploy.getTotalPoint() + point) +
				// ",共有:" + cookList.size() + "步,平均分:" + result);
				int[] passTime = { 300, 150, 130, 105, 95, 70, 50, -1 };
				int[] reap = { 0, 1, 3, 6, 10, 6, 3, 1 };
				String[] reapStr = { "长毛的", "难吃的", "不新鲜的", "美味的", "完美的", "良好的",
						"一般的", "糟糕的" };
				int i = 0;
				for (; i < passTime.length; i++) {
					if (result > passTime[i]) {
						break;
					}
				}
				addMaterial(StringUtil.toInt(recpie.getProduct()), reap[i]);
				request.setAttribute(
						"tip",
						"您烹饪了" + reapStr[i] + recpie.getName() + "!"
								+ recpie.getName() + "+" + reap[i]);
			} else {
				request.setAttribute("tip", "你烹饪了一盘诡异的食物,没有任何价值,只好扔进垃圾桶!");
			}
			SqlUtil.executeUpdate("delete from fm_yard_deploy where id="
					+ deploy.getId(), 5);
			SqlUtil.executeUpdate("delete from fm_yard_deploying where id="
					+ deployingBean.getId(), 5);
			deploying--; // 烹饪数--
			return -1;
		} else {
			/*
			 * 评价规则： 设：第1步到第N步的放材料时间为T1…TN 第1步到第N步的标准放材料时间PT1…PTN
			 * 则评价值=(T1/PT1+T2/PT2+.……TN/PTN)/N
			 */
			int point = (int) (System.currentTimeMillis() - deploy
					.getStartTime()) / (cook.getNeedTime() / 100);
//			 System.out.println("第" + cook.getStep() + "步得分:" + point);
			// 更新时间
			deploy.setStartTime(System.currentTimeMillis());
			// 步骤+1
			deploy.setStep(deploy.getStep() + 1);
			// 增加当前步骤的得分
			deploy.setTotalPoint(deploy.getTotalPoint() + point);
			// 改数据库
			SqlUtil.executeUpdate(
					"update fm_yard_deploy set start_time=now(),step=step+1,total_point="
							+ deploy.getTotalPoint() + " where id="
							+ deploy.getId(), 5);
		}
		return 1;
	}

	/**
	 * 取消做菜
	 * 
	 * @param deploy
	 *            配菜
	 */
	public void cancel(YardDeployBean deploy, YardDeployingBean deployingBean) {
		if (deploy == null || deploy.getFmid() != this.fmid)
			return;
		SqlUtil.executeUpdate(
				"delete from fm_yard_deploy where id=" + deploy.getId(), 5);
		// 如果一个人刚进入做菜页面，还没有放入任何材料时，deployingBean==null，这时点取消，不需要删除deployingBean。
		if (deployingBean != null)
			SqlUtil.executeUpdate("delete from fm_yard_deploying where id="
					+ deployingBean.getId(), 5);
		deploying--; // 正在烹饪数量--
	}

	/**
	 * 返回当前所持有的同类型的工厂数量
	 * 
	 * @param recipeId
	 *            recipeId是指此工厂可使用的菜谱，相当于此工厂的类型
	 * @return
	 */
	public int getPlantCountByType(int recipeId) {
		int count = 0;
		List list = getPlantList();
		if (list == null || list.size() == 0 || recipeId <= 0)
			return count;
		else {
			YardPlantBean plant = null;
			for (int i = 0; i < list.size(); i++) {
				plant = (YardPlantBean) list.get(i);
				if (plant != null && plant.getRecipeId() == recipeId) {
					count++;
				}
			}

		}
		return count;
	}

}
