package net.joycool.wap.action.fs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-26 15:38:13
 */
public class FSUserBean {
	public static int STATUS_PLAY = 0;
	public static int STATUS_END = 1;
	public static int STATUS_LOSE = 2;
	public static int STATUS_INIT = 3;		// 游戏未初始化
	
	public static int TYPE_MAX = 3;
	
	public static int totalDay[] = { 40, 30, 25 };	// 总天数，4个版本可以玩
	public static int INIT_BAG_COUNT = 100;
	public static int INIT_MONEY = 2000;
	
	int id;
	int userId;
	int sceneId;	// 所在场景
	int date;		// 游戏默认剩余天数，-1表示游戏未初始化

	HashMap productMap = null;		// 当前用户身上物品列表明细

	FSSceneBean scene = null;		// 用户场景数据

	HashMap fsProductMap = null;	// 用户这一局的物价表

	int health;		// 健康度
	int money;		// 当前现金
	int saving;		// 存款
	int debt;		// 贷款
	int honor;		// 名誉度
	int userBag;	// 行囊大小
		
	long actionTime;	// 动作时间

	boolean offline;	// 离线标志位offline

	// 游戏结束标志位
	int gameStatus = STATUS_INIT;
	
	int type = 0;	// 3种玩的模式，总天数不同而已
	
	String gameResultTip = "";	// 结束游戏后的提示信息

	public FSUserBean() {
		productMap = new HashMap();
		fsProductMap = new HashMap();
		scene = new FSSceneBean();
	}
	
	public void reset() {
		sceneId = 0;
		date = 1;
		health = 100;
		money = INIT_MONEY;
		saving = 0;
		debt = 5000;
		honor = 100;
		userBag = INIT_BAG_COUNT;
		gameStatus = STATUS_PLAY;
		productMap.clear();
		fsProductMap.clear();
		scene.getSceneProductMap().clear();
		scene.setSpecialEvent(0);
		scene.setBlackEvent(0);
		
		gameResultTip = "";
		// 初始化这一局的物价表
		for (Iterator iter = FSWorld.loadFSProduct().entrySet().iterator(); iter.hasNext();) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    FSProductBean product = new FSProductBean((FSProductBean)entry.getValue());
		    float rate = (float)(100 - RandomUtil.nextInt(40)) / 100;
		    product.setPriceBase((int) (product.getPriceBase() * rate));
		    product.setPriceChange((int) (product.getPriceChange() * rate));
		    
		    fsProductMap.put(entry.getKey(), product);
		}
	}

	/**
	 * @return 返回 userBag。
	 */
	public int getUserBag() {
		return userBag;
	}

	/**
	 * @param userBag
	 *            要设置的 userBag。
	 */
	public void setUserBag(int userBag) {
		this.userBag = userBag;
	}

	/**
	 * @return 返回 honor。
	 */
	public int getHonor() {
		return honor;
	}

	/**
	 * @param honor
	 *            要设置的 honor。
	 */
	public void setHonor(int honor) {
		this.honor = honor;
	}

	/**
	 * @return 返回 offline。
	 */
	public boolean isOffline() {
		return offline;
	}

	/**
	 * @param offline
	 *            要设置的 offline。
	 */
	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	/**
	 * @return 返回 actionTime。
	 */
	public long getActionTime() {
		return actionTime;
	}

	/**
	 * @param actionTime
	 *            要设置的 actionTime。
	 */
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 productMap。
	 */
	public HashMap getProductMap() {
		return productMap;
	}

	/**
	 * @param productMap
	 *            要设置的 productMap。
	 */
	public void setProductMap(HashMap productMap) {
		this.productMap = productMap;
	}

	/**
	 * @return 返回 debt。
	 */
	public int getDebt() {
		return debt;
	}

	/**
	 * @param debt
	 *            要设置的 debt。
	 */
	public void setDebt(int debt) {
		this.debt = debt;
	}

	/**
	 * @return 返回 health。
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * 扣除健康点
	 * @param lost
	 *            要设置的 health扣除数量。
	 */
	public void setHealthLost(int lost) {
		setHealth(health - lost);
	}
	
	/**
	 * @param health
	 *            要设置的 health。
	 */
	public void setHealth(int health) {
		if(health <= 0) {
			health = 0;
			setGameStatus(STATUS_LOSE);
			gameResultTip += "身体情况太差，早早的就回家了，没挣到一分钱。";
		}
		if(health > 100)
			health = 100;
		this.health = health;
	}

	/**
	 * @return 返回 saving。
	 */
	public int getSaving() {
		return saving;
	}

	/**
	 * @param saving
	 *            要设置的 saving。
	 */
	public void setSaving(int saving) {
		this.saving = saving;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 判断用户是否下线
	 * @datetime:2007-3-17 11:22:33
	 * @return
	 * @return boolean
	 */
	public boolean userOffline() {
		// 需要乐币复活
		if (System.currentTimeMillis() - actionTime > FSAction.PK_USER_INACTIVE) {
			return true;
		}
		return false;
	}

	/**
	 * @return 返回 money。
	 */
	public int getMoney() {
		return money;
	}
	
	/**
	 * 按一定百分比扣钱
	 * @param lost
	 *            要设置减少的 钱的百分比。
	 */
	public void setMoneyLost(float lost) {
		setMoney((int) (money * (1 - lost)));
	}
	
	/**
	 * @param money
	 *            要设置的 money。
	 */
	public void setMoney(int money) {
		if(money < 0)
			money = 0;
		this.money = money;
	}
	
	/**
	 * @return 返回 date。
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date
	 *            要设置的 date。
	 */
	public void setDate(int date) {
		this.date = date;
	}
	
	public int getDay() {
		return date;
	}

	/**
	 * @return 返回 sceneId。
	 */
	public int getSceneId() {
		return sceneId;
	}

	/**
	 * @param sceneId
	 *            要设置的 sceneId。
	 */
	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}



	/**
	 * @return Returns the gameStatus.
	 */
	public int getGameStatus() {
		return gameStatus;
	}

	/**
	 * @param gameStatus The gameStatus to set.
	 */
	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}

	public boolean isGameOver() {
		return gameStatus != STATUS_PLAY;
	}
	
	/**
	 * @return 返回 scene。
	 */
	public FSSceneBean getScene() {
		return scene;
	}

	/**
	 * @param scene 要设置的 scene。
	 */
	public void setScene(FSSceneBean scene) {
		this.scene = scene;
	}

	/**
	 * @return Returns the gameResultTip.
	 */
	public String getGameResultTip() {
		return gameResultTip;
	}

	/**
	 * @param gameResultTip The gameResultTip to set.
	 */
	public void setGameResultTip(String gameResultTip) {
		this.gameResultTip = gameResultTip;
	}

	/**
	 * @return Returns the fsProductMap.
	 */
	public HashMap getFsProductMap() {
		return fsProductMap;
	}

	/**
	 * @param fsProductMap The fsProductMap to set.
	 */
	public void setFsProductMap(HashMap fsProductMap) {
		this.fsProductMap = fsProductMap;
	}

	public void addDate() {
		date++;
	}

	/**
	 * @return 是否时间已经到
	 */
	public boolean isDateOver() {
		return date > totalDay[type];
	}
	
	/**
	 * 是否已经是最后一天
	 */
	public boolean isEndDate() {
		return date >= totalDay[type];
	}
	
	public int getTotalDay() {
		return totalDay[type];
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		if(type < 0 || type >= TYPE_MAX)
			type = 0;
		this.type = type;
	}

	/**
	 * 把用户物品保存到一个字符串（用于写入数据库）
	 */
	public String saveGoods() {
		StringBuilder sb = new StringBuilder();
		Iterator iter = productMap.values().iterator();
		while(iter.hasNext()) {
			FSUserBagBean bean = (FSUserBagBean)iter.next();
			sb.append(bean.getProductId());
			sb.append("-");
			sb.append(bean.getPrice());
			sb.append("-");
			sb.append(bean.getCount());
			sb.append(",");
		}
		
		return sb.toString();
	}

	public void loadGoods(String data) {
		HashMap worldMap = FSWorld.loadFSProduct();
		productMap.clear();
		String[] datas = data.split(",");
		for(int i = 0;i < datas.length;i++) {
			String[] each = datas[i].split("-");
			if(each.length == 3) {	// 检查数据是否正常
				int id = StringUtil.toInt(each[0]);
				Integer key = new Integer(id);
				int price = StringUtil.toInt(each[1]);
				int count = StringUtil.toInt(each[2]);
				FSProductBean ori = (FSProductBean)worldMap.get(key);
				if(ori != null) {
					FSUserBagBean p = new FSUserBagBean(ori);
					p.setPrice(price);
					p.setCount(count);
					productMap.put(key, p);
				}
			}
		}
	}
	
	/**
	 * 把场景物品保存到一个字符串（用于写入数据库）
	 */
	public String saveSceneGoods() {
		StringBuilder sb = new StringBuilder();
		Iterator iter = scene.getSceneProductMap().values().iterator();
		while(iter.hasNext()) {
			FSUserBagBean bean = (FSUserBagBean)iter.next();
			sb.append(bean.getProductId());
			sb.append("-");
			sb.append(bean.getPrice());
			sb.append(",");
		}
		
		return sb.toString();
	}

	public void loadSceneGoods(String data) {
		HashMap worldMap = FSWorld.loadFSProduct();
		HashMap map = scene.getSceneProductMap();
		map.clear();
		String[] datas = data.split(",");
		for(int i = 0;i < datas.length;i++) {
			String[] each = datas[i].split("-");
			if(each.length == 2) {	// 检查数据是否正常
				int id = StringUtil.toInt(each[0]);
				Integer key = new Integer(id);
				int price = StringUtil.toInt(each[1]);
				FSProductBean ori = (FSProductBean)worldMap.get(key);
				if(ori != null) {
					FSUserBagBean p = new FSUserBagBean(ori);
					p.setPrice(price);
					map.put(key, p);
				}
			}
		}
	}
	
	/**
	 * 把本局物品价格表保存到一个字符串（用于写入数据库）
	 */
	public String saveProducts() {
		StringBuilder sb = new StringBuilder();
		Iterator iter = fsProductMap.values().iterator();
		while(iter.hasNext()) {
			FSProductBean bean = (FSProductBean)iter.next();
			sb.append(bean.getId());
			sb.append("-");
			sb.append(bean.getPriceBase());
			sb.append("-");
			sb.append(bean.getPriceChange());
			sb.append(",");
		}
		
		return sb.toString();
	}

	public void loadProducts(String data) {
		HashMap worldMap = FSWorld.loadFSProduct();
		fsProductMap.clear();
		String[] datas = data.split(",");
		for(int i = 0;i < datas.length;i++) {
			String[] each = datas[i].split("-");
			if(each.length == 3) {	// 检查数据是否正常
				int id = StringUtil.toInt(each[0]);
				Integer key = new Integer(id);
				int base = StringUtil.toInt(each[1]);
				int change = StringUtil.toInt(each[2]);
				FSProductBean ori = (FSProductBean)worldMap.get(key);
				if(ori != null) {
					FSProductBean p = new FSProductBean(ori);
					p.setPriceBase(base);
					p.setPriceChange(change);
					fsProductMap.put(key, p);
				}
			}
		}
	}
}
