package net.joycool.wap.spec.buyfriends;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.UserInfoUtil;

public class ServiceMaster {

	static UserServiceImpl userService = new UserServiceImpl();
	private static ServiceMaster serviceMaster = new ServiceMaster();
	
	public static ServiceMaster getInstance(){
		
		if(serviceMaster == null) {
			synchronized(ServiceMaster.class) {
				if(serviceMaster == null) {
					serviceMaster = new ServiceMaster();
				}
			}
		}
		return serviceMaster;
	}
	
	private ServiceMaster(){}
	
	static ICacheMap buyFriendMaster = CacheManage.buyFriendMaster;
	
	DAOMaster masterDAO = new DAOMaster();
	
	public boolean addMaster(BeanMaster master) {		
		boolean flag = masterDAO.addMaster(master);
		
		if(flag) {
			buyFriendMaster.spt(new Integer(master.getUid()), master);
		}
		return flag;
	}
	
	public BeanMaster addMaster(int uid) {
		BeanMaster master = new BeanMaster();
		master.setUid(uid);
		master.setSlaveCount(0);
		master.setPrice(600);
		master.setMoney(1000);
		master.setRansomTime(new Date(System.currentTimeMillis() - DateUtil.MS_IN_DAY * 3));
		UserBean user = UserInfoUtil.getUser(uid);
		if(user != null) {
			master.setNickName(user.getNickName());
		}else {
			master.setNickName(""+uid);
		}
		
		boolean flag = masterDAO.addMaster(master);
		
		if(flag) {
			buyFriendMaster.spt(new Integer(master.getUid()), master);
		}
		
		return master;
	}
	
	public BeanMaster getMasterByUid(int uid) {
		
		synchronized(buyFriendMaster) {
			BeanMaster master =  (BeanMaster)buyFriendMaster.get(new Integer(uid));
			
			if(master == null) {
				master = masterDAO.getMasterByUid(uid);
				if(master == null) {
					master = addMaster(uid);
				} else {
					buyFriendMaster.put(new Integer(uid), master);
				}
			}
			return master;
		}
		
	}
	
	
//	public BeanMaster getMasterByUidIfNull(int uid) {
//		
//		BeanMaster master =  (BeanMaster)buyFriendMaster.sgt(new Integer(uid));
//		
//		if(master == null) {
//			master = new BeanMaster();
//			master.setUid(uid);
//		}
//		
//	}
	
	/**
	 * 增加用户的资产
	 * @param uid
	 * @param moneyOffSet 增加的资产多少
	 * @param slaveLock 是否奴隶数减1
	 */
	public boolean increaseMoneyByUid(int uid, int moneyOffSet, boolean slaveLock) {
		
		boolean flag = masterDAO.increaseMoneyByUid(uid, moneyOffSet, slaveLock);
		
		if(flag) {
			BeanMaster master =  (BeanMaster)buyFriendMaster.sgt(new Integer(uid));
			
			if(master == null) {
				master = masterDAO.getMasterByUid(uid);
				if(master == null) {
					master = addMaster(uid);
				}
				buyFriendMaster.spt(new Integer(uid), master);
			} else {
				if(slaveLock) {
					master.setSlaveCount(master.getSlaveCount() - 1);
				}
				master.setMoney(master.getMoney() + moneyOffSet);
			}
		}
		return flag;
	}
	
	/**
	 * 用户赎身
	 * @param uid
	 * @param priceOffSet
	 * @return
	 */
	public boolean ransom(int uid, int priceOffSet, int moneyOffSet) {
		boolean flag = masterDAO.ransom(uid, priceOffSet, moneyOffSet);
		
		if(flag) {
			BeanMaster master =  (BeanMaster)buyFriendMaster.sgt(new Integer(uid));
			
			if(master == null) {
				master = masterDAO.getMasterByUid(uid);
				if(master == null) {
					master = addMaster(uid);
				}
				buyFriendMaster.spt(new Integer(uid), master);
			} else {
				master.setPrice(master.getPrice() + priceOffSet);
				master.setMoney(master.getMoney() - moneyOffSet);
				master.setRansomTime(new Date(System.currentTimeMillis() + DateUtil.MS_IN_DAY));
			}
			
		}
		
		return flag;
	}
	
	/**
	 * 增加用户的身价
	 */
	public boolean increasePriceByUid(int uid, int priceOffSet) {
		
		boolean flag = masterDAO.increasePriceByUid(uid, priceOffSet);
		
		if(flag) {
			BeanMaster master =  (BeanMaster)buyFriendMaster.sgt(new Integer(uid));
			
			if(master == null) {
				master = masterDAO.getMasterByUid(uid);
				if(master == null) {
					master = addMaster(uid);
				}
				buyFriendMaster.spt(new Integer(uid), master);
			}else {
				master.setPrice(master.getPrice() + priceOffSet);
			}
			
		}
		
		return flag;
	}
	
	/**
	 * 减少用户的金钱
	 * @param uid
	 * @param moneyOffSet
	 * @param slaveLock 是否奴隶加1
	 */
	public boolean decreaseMoneyByUid(int uid, int moneyOffSet, boolean slaveLock) {
		
		boolean flag = masterDAO.decreaseMoneyByUid(uid, moneyOffSet, slaveLock);
		
		if(flag) {
			BeanMaster master =  (BeanMaster)buyFriendMaster.sgt(new Integer(uid));
			
			if(master == null) {
				master = masterDAO.getMasterByUid(uid);
				if(master == null) {
					master = addMaster(uid);
				}
				buyFriendMaster.spt(new Integer(uid), master);
			} else {
				if(slaveLock) {
					master.setSlaveCount(master.getSlaveCount() + 1);
				}
				master.setMoney(master.getMoney() - moneyOffSet);
			}
			
			
		}
		
		
		return flag;
	}
	
	/**
	 * 取得我能购买的奴隶，仅仅从我的好友里面显示
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getMastersICanBuyOfMyFriend(int uid, int money, int start, int limit) {
		
		int count = 1;
		List masterList = new ArrayList();
		List list = UserInfoUtil.getUserFriends(uid);
		int startIndex = start * limit;
		
		if(list.size() <= startIndex) {
			return masterList;
		}
		
		for(int i = startIndex; i < list.size(); i++) {
			if(count <= limit) {
				int theUid = (Integer.valueOf((String)list.get(i))).intValue();
				BeanMaster master = this.getMasterByUid(theUid);
				if(master != null) {
					if(master.getPrice() <= money) {
						masterList.add(master);
						count++;
					}
				}
				
				
			} else {
				break;
			}
			
		}
		
		return  masterList;
	}
	
	public int getCountMastersICanBuyOfMyFriend(int uid, int money) {
		
		return this.masterDAO.getCountMastersICanBuyOfMyFriend(uid, money);
	}
	
	/**
	 * 取得我的所有好友的奴隶价格
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getMastersOfMyFriend(int uid, int start, int limit) {
		int count = 1;
		List masterList = new ArrayList();
		
		List list = UserInfoUtil.getUserFriends(uid);
		int startIndex = start * limit;
		if(list.size() <= startIndex) {
			return masterList;
		}
		for(int i = startIndex; i < list.size(); i++) {
			if(count <= limit) {
				int theUid = (Integer.valueOf((String)list.get(i))).intValue();
				//if( userService.getUserFriend(theUid, uid) != null) {
					masterList.add(this.getMasterByUid(theUid));
					count++;
				//}
			} else {
				break;
			}
			
		}
		return  masterList;
	}
	
	public int getCountMasterOfMyFriend(int uid) {
		//int count = 0;
		List list = UserInfoUtil.getUserFriends(uid);
		
//		Iterator it = list.iterator();
//		while(it.hasNext()) {
//			int theUid = (Integer.valueOf((String)it.next())).intValue();
//			if(userService.getUserFriend(theUid, uid) != null) {
//				count ++;
//			}
//		}
		
		return list.size();
	}
	
	public boolean decreasePricePercentByUid(int uid, float percent){
		
		boolean flag = masterDAO.decreasePricePercentByUid(uid, percent);
		
		if(flag) {
			BeanMaster master =  (BeanMaster)buyFriendMaster.sgt(new Integer(uid));
			
			if(master == null) {
				master = masterDAO.getMasterByUid(uid);
				if(master == null) {
					master = addMaster(uid);
				}
				buyFriendMaster.spt(new Integer(uid), master);
			} else {
				master.setPrice((int)(master.getPrice() * (1 - percent)));
			}
			
		}
		return flag;
	}
}
