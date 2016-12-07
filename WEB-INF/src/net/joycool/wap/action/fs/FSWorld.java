package net.joycool.wap.action.fs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.joycool.wap.util.RandomUtil;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-26 16:18:10
 */
public class FSWorld {
	public static FSService service = new FSService();

	// 浮生记在线用户map
	public static HashMap pkUserMap = null;

	// 浮生记物品map
	public static HashMap fsProductMap = null;

	// 浮生记事件map
	public static HashMap fsEventMap = null;

	public static FSService getFSService() {
		return service;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取浮生记用户属性
	 * @datetime:2007-3-27 12:11:41
	 * @param pkUserId
	 * @return
	 * @return PKUserBean
	 */
	public static FSUserBean getFsUser(int fsUserId) {
		FSUserBean fsUser = (FSUserBean) getFSUserMap().get(
				new Integer(fsUserId));
		if (fsUser == null) {
			fsUser = getFSService().getFSUser(
					"user_id=" + fsUserId + " limit 0 , 1");
			if (fsUser != null) {
				getFSUserMap().put(new Integer(fsUser.getUserId()), fsUser);
			}
		}
		return fsUser;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取所有浮生记在线用户信息
	 * @datetime:2007-3-26 17:49:48
	 * @return
	 * @return HashMap
	 */
	static byte[] lock = new byte[0];

	public static HashMap getFSUserMap() {
		if (pkUserMap != null) {
			return pkUserMap;
		}
		synchronized (lock) {
			if (pkUserMap != null) {
				return pkUserMap;
			}
			pkUserMap = new HashMap();
			return pkUserMap;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取所有物品信息
	 * @datetime:2007-3-26 17:37:16
	 * @return
	 * @return HashMap
	 */
	static byte[] lock1 = new byte[0];

	public static HashMap loadFSProduct() {
		if (fsProductMap != null) {
			return fsProductMap;
		}
		synchronized (lock1) {
			if (fsProductMap != null) {
				return fsProductMap;
			}
			fsProductMap = new HashMap();
			Vector fsProductList = getFSService().getFSProductList(" 1=1");
			FSProductBean fsProduct = null;
			for (int i = 0; i < fsProductList.size(); i++) {
				fsProduct = (FSProductBean) fsProductList.get(i);
				if (fsProductList != null) {
					fsProductMap.put(new Integer(fsProduct.getId()), fsProduct);
				}
			}
		}
		return fsProductMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取所有事件信息
	 * @datetime:2007-3-26 17:37:16
	 * @return
	 * @return HashMap
	 */
	static byte[] lock2 = new byte[0];

	public static HashMap loadFSEvent() {
		if (fsEventMap != null) {
			return fsEventMap;
		}
		synchronized (lock2) {
			if (fsEventMap != null) {
				return fsEventMap;
			}
			fsEventMap = new HashMap();
			Vector fsEventList = getFSService().getFSEventList("1=1");
			FSEventBean fsEvent = null;
			for (int i = 0; i < fsEventList.size(); i++) {
				fsEvent = (FSEventBean) fsEventList.get(i);
				if (fsEventList != null) {
					fsEventMap.put(new Integer(fsEvent.getId()), fsEvent);
				}
			}
		}
		return fsEventMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 更新场景内物品
	 * @datetime:2007-3-28 18:49:34
	 * @return
	 * @return HashMap
	 */
	public static void refreshScene(FSUserBean fsUser) {
		Map sceneProductMap = fsUser.getScene().getSceneProductMap();
		sceneProductMap.clear();
		FSProductBean product = null;
		Iterator it = fsUser.getFsProductMap().values().iterator();
		// int i=0;
		while (it.hasNext()) {
			// 随机出现最多6种物品
			product = (FSProductBean) it.next();
			// 获取1到100的随机数
			int rate = RandomUtil.nextInt(100);
			// 判断当前物品是否大于随机数
			if (product.getProbability() > rate) {
				sceneProductMap.put(new Integer(product.getId()),
						new FSUserBagBean(product));
				// i++;
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 更新场景内事件
	 * @datetime:2007-3-28 18:49:34
	 * @return
	 * @return HashMap
	 */
	public static void refreshEvent(FSUserBean fsUser) {
		// 初始化
		fsUser.getScene().setBlackEvent(0);
		fsUser.getScene().setSpecialEvent(0);
		// 获取出现随机纪律
		int probability = RandomUtil.nextInt(100);
		if(probability<=75){
			Map events = loadFSEvent();
			int size = events.size();
			int r = RandomUtil.nextInt(size);
			Integer eventId =(Integer)events.keySet().toArray()[r];
			FSEventBean event = (FSEventBean)FSWorld.loadFSEvent().get(eventId);
			
			// 调整价格
			FSUserBagBean userBag = (FSUserBagBean)fsUser.getScene().getSceneProductMap().get(new Integer(event.getProductId()));
			if(userBag!=null){
				// 设置事件id
				fsUser.getScene().setBlackEvent(event.getId());
				// 更新物品价格
				userBag.setPrice((int)(userBag.getPrice()*event.getPriceChange()));
			}
		}
		// 如果债务大于1000的特殊事件
		probability = RandomUtil.nextInt(100);
		if(fsUser.getDebt()>10000 && probability<fsUser.getDebt() / 1000){
			// 设置特殊事件id（健康减50）
			fsUser.getScene().setSpecialEvent(1);
			// 欠钱太多被揍健康度减30
				fsUser.setHealthLost(30);
		} else if(probability < 60-fsUser.getHealth()){
			// 设置特殊事件id
			fsUser.getScene().setSpecialEvent(2);
			//财产缩水30%
			fsUser.setMoneyLost(0.3f);
			//健康度加5
				fsUser.setHealth(fsUser.getHealth()+5);

		}else if(probability > 70){
			probability = RandomUtil.nextInt(13) + 3;
			fsUser.getScene().setSpecialEvent(probability);
			switch(probability-2){
			case 1:
				fsUser.setMoneyLost(0.1f);
				break;
			case 2:
				fsUser.setHealthLost(3);
				break;
			case 3:
				fsUser.setHealthLost(3);
				break;
			case 4:
				fsUser.setMoneyLost(0.1f);
				break;	
			case 5:
				fsUser.setMoneyLost(0.15f);
				break;	
			case 6:
				fsUser.setMoneyLost(0.1f);
				fsUser.setHealthLost(5);
				break;	
			case 7:		// 这条和以下的都是新增的，一共7条事件
				fsUser.setMoneyLost(0.1f);
				break;	
			case 8:
				fsUser.setHealthLost(5);
				break;	
			case 9:
				fsUser.setHealthLost(3);
				break;	
			case 10:
				fsUser.setMoneyLost(0.05f);
				break;	
			case 11:
				fsUser.setMoneyLost(0.15f);
				break;	
			case 12:
				fsUser.setMoneyLost(0.1f);
				break;	
			case 13:
				fsUser.setMoneyLost(0.1f);
				fsUser.setHealthLost(3);
				break;	
			}
			
		}
	}
	/**
	 * 
	 * @author macq
	 * @explain：清空浮生记缓存
	 * @datetime:2007-3-28 18:48:46
	 * @return void
	 */
	public static void clear() {
		pkUserMap = null;
		fsProductMap = null;
		fsEventMap = null;
	}
	
	public static void saveallgames() {
		HashMap fsUserMap = getFSUserMap();
		if (fsUserMap != null) {
			Iterator iter = fsUserMap.values().iterator();
			while (iter.hasNext()) {
				FSUserBean bean = (FSUserBean)iter.next();
				service.saveFSUser(bean);
			}
			fsUserMap.clear();
		}
	}
}
