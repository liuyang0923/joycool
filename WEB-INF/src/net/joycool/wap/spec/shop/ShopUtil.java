package net.joycool.wap.spec.shop;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.spec.castle.CastleUserBean;
import net.joycool.wap.spec.castle.CastleUtil;
import net.joycool.wap.spec.pay.PayAction;
import net.joycool.wap.spec.pay.PayOrderBean;
import net.joycool.wap.util.SqlUtil;

public class ShopUtil {
	public static DecimalFormat numFormat = new DecimalFormat("0.0g");
	public static DecimalFormat numFormat2 = new DecimalFormat("0.0酷币");
	public static DecimalFormat numFormat3 = new DecimalFormat("0.0");
	public static String formatPrice(float p) {
		return numFormat.format(p);
	}
	public static String formatPrice2(float p) {
		return numFormat2.format(p);
	}
	public static String formatPrice3(float p) {
		return numFormat3.format(p);
	}
	/**
	 * 购买给自己的商品
	 */
	public static final int BUY = 0;	// 0 买乐秀 1 送乐秀 2 apppay 3 买酷秀 4 送酷秀
	/**
	 * 赠送给别人的商品
	 */
	public static final int SEND = 1;
//	/**
//	 * 系统充值
//	 */
//	public static final int CHARGE = 1;
	
	public static final int TYPE_ALL = 0;
	public static final int TYPE_CLUB = 1;
	public static final int TYPE_GAME = 2;
	public static final int TYPE_BET = 3;
	public static final int TYPE_GIFT = 6;
	public static final int TYPE_OTHER = 8;
	//public static String[] type;
	//public static final int ITEM_
	
	public static String[] getTypes(){
		return ShopService.getShopTypes();
	}
	
	public static List getAds() {
		return ShopService.getAllAds();
	}
	
	
	//购买结婚礼物的购买记录
	public static final int RECORD_TYPE_PROPOSAL = 1;
	
	//结婚的乐币最大值，超过10000000表示金币 5金币表示50000000
	public static int PROPOSAL_LEBI = 10000000;
	
	public static ShopService shopService = ShopService.getInstance();
	public static String GOLD_NAME = "<img src=\"img/gold.gif\" alt=\"酷币\"/>";
	public static String GOLD_NAME_PATH = "<img src=\"/shop/img/gold.gif\" alt=\"酷币\"/>";
	public static byte[] GOLD_LOCK = new byte[0];
	
	public static List sugguestList;
	public static int sugguestLimit = 6;
	
	public static String SESSION_SHOP_BUY = "session_shop_buy";
	
	public static List getSugguestList(){
//		if(sugguestList == null) {
//			synchronized(ShopUtil.class){
//				if(sugguestList != null) {
//					return sugguestList;
//				}
//				sugguestList = shopService.getSugguest(sugguestLimit);
//			}
//		}
		return shopService.getSugguest(sugguestLimit);
	}
	
	public static List topList;
	public static int topLimit = 6;
	
	public static List getTopSugguestList(){
//		if(topList == null) {
//			synchronized(ShopUtil.class) {
//				if(topList != null) {
//					return topList;
//				}
//				topList = shopService.getTop(topLimit);
//			}
//		}
		
		return shopService.getTop(topLimit);
	}
	
	/**
	 * 
	 * @param price 物品的价格
	 * @param money 用户拥有的钱
	 * @return
	 */
	public static boolean hasEnoughMoney(float price, float money) {
		int p = (int)Math.round(price * 100);
		int m = (int)Math.round(money * 100);
		return m >= p?true:false;		
	}
	
	/**
	 * 
	 * @param price 要增加或者减少的钱
	 * @param money 用户已有的钱
	 * @param add 是否是增加钱，flase表示减少钱
	 * @return 返回用户增加或减少后的钱
	 */
	public static float calMoney(float price, float money, boolean add) {
		int p = (int)Math.round(price * 100);
		int m = (int)Math.round(money * 100);
		//System.out.println(p);
		//System.out.println(m);
		int i = 0;
		float r = 0;
		if(add) {
			i = m + p;
		} else {
			i = m - p;
		}
		//System.out.println(i);
		r = (float)i / 100;
		return r;
	}
	
	public static void updateUserGold(int uid, float count, float price, int type, int itemId, int userId) {
		synchronized(GOLD_LOCK) {
			shopService.updateUserGold(uid, count,price);
		}
		GoldRecordBean goldRecord = new GoldRecordBean();
		goldRecord.setUid(uid);
		goldRecord.setGold(price);
		goldRecord.setType(type);
		goldRecord.setItemId(itemId);
		goldRecord.setUserId(userId);
		shopService.addGoldRecord(goldRecord);
	}
	
	public static void updateUserGold(int uid, float price, int type) {
		synchronized(GOLD_LOCK) {
			UserInfoBean userInfo = shopService.getUserInfo(uid);
			userInfo.decreaseGold(price);
			shopService.updateUserGold(uid, userInfo.getGold());
		}
			GoldRecordBean goldRecord = new GoldRecordBean();
			goldRecord.setUid(uid);
			goldRecord.setGold(price);
			goldRecord.setType(type);
			goldRecord.setItemId(0);
			shopService.addGoldRecord(goldRecord);
		
	}
	
	public static ICacheMap lookRecordCache = new LinkedCacheMap(100, true);
	public static void updateLookRecord(int uid,ItemBean bean) {
		synchronized(lookRecordCache) {
			List lookRecordList = (List)lookRecordCache.get(new Integer(uid));
			if(lookRecordList == null) {
				lookRecordList = new LinkedList();
				LookRecordBean lookRecordBean = new LookRecordBean();
				lookRecordBean.setItemId(bean.getId());
				lookRecordBean.setUid(uid);
				lookRecordBean.setTime(new Date());
				lookRecordList.add(lookRecordBean);
				
				lookRecordCache.put(new Integer(uid), lookRecordList);
			} else {
				LookRecordBean lookRecordBean = new LookRecordBean();
				lookRecordBean.setItemId(bean.getId());
				lookRecordBean.setUid(uid);
				lookRecordBean.setTime(new Date());
				boolean flag = false;
				for(int i =0; i < lookRecordList.size(); i++) {
					LookRecordBean lookBean = (LookRecordBean)lookRecordList.get(i);
					if(lookBean.getItemId() == lookRecordBean.getItemId()) {
						lookRecordList.remove(i);
						lookRecordList.add(lookRecordBean);
						flag = true;
						break;
					}
				}
				if(!flag) {
					if(lookRecordList.size() >= 3) {
						lookRecordList.remove(2);
					}
					lookRecordList.add(lookRecordBean);
				}
			}
		}
	}
	
	
	public static boolean toCastGold(int uid, DummyProductBean item) {
		CastleUserBean user = CastleUtil.getCastleUser(uid);
		
		if(user == null) {
			return false;
		}
		
		synchronized(user) {
			user.setGold(user.getGold() + item.getValue());
			SqlUtil.executeUpdate("update castle_user set gold=" + user.getGold() + " where uid=" + user.getUid(), 5);
			SqlUtil.executeUpdate("insert into castle_gold set uid=" + user.getUid() + ",gold=" + item.getValue() + ",type=0,create_time=now(),`left`=" + user.getGold(), 5);
		}
		
		return true;
	}
	
	
	public static void charge(int uid, int money) {
		synchronized(GOLD_LOCK) {
			UserInfoBean userInfo = shopService.getUserInfo(uid);
			if(userInfo == null) {
				userInfo = ShopAction.shopService.addUserInfo(uid);
			}
			userInfo.increaseGold((float)money);
			shopService.updateUserGold(uid, userInfo.getGold());
		}
	}
	
	public static void charge(int uid, float money) {
		synchronized(GOLD_LOCK) {
			UserInfoBean userInfo = shopService.getUserInfo(uid);
			if(userInfo == null) {
				userInfo = ShopAction.shopService.addUserInfo(uid);
			}
			userInfo.increaseGold(money);
			shopService.updateUserGold(uid, userInfo.getGold());
		}
	}
	
	
	public static void processCharge(int orderId, int userId, float amount, String result){
		ShopUtil.charge(userId, amount);
		NoticeAction.sendNotice(userId, "充值成功获酷币"+amount+"g", NoticeBean.GENERAL_NOTICE, "/shop/info.jsp" );
		PayAction.service.updateOrderResult2(orderId, PayOrderBean.TYPE_DONE, result);
	}
	
	public static int DAY = 60 * 24;
	
	public static int dueTime(int min) {
		return min / DAY;
	}
	
	public static float[] consumes = {10f,50f};
	public static float[] discounts = {0.9f,0.8f};
	public static float discount(float consume, float price) {
		int p = (int)Math.round(consume * 100);
		int m = (int)Math.round(price * 100);
		int size = consumes.length;
		for(int i = size - 1; i >= 0; i--) {
			int c = (int)Math.round(consumes[i] * 100);
			if(p >= c) {
				
				return (float)(m * discounts[i]) / 100;
			} else {
				continue;
			}
		}
		
		return price;
	}
	
	public static void main(String[] args) {
		System.out.println(discount(12.6f, 8.4f));
		
	}
}
