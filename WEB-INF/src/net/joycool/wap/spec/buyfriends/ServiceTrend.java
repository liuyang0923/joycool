package net.joycool.wap.spec.buyfriends;

import java.util.LinkedList;
import java.util.List;

import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;

import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.util.HomeCacheUtil;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.UserInfoUtil;

public class ServiceTrend {
	
	public static int USER_TREND_COUNT = 100;
	public static int FAMILY_TREND_COUNT = 100;
	
	private static ServiceTrend serviceTrend = new ServiceTrend();
	static UserServiceImpl userService = new UserServiceImpl();
	
	private ServiceTrend(){}
	
	public static ServiceTrend getInstance(){
		
		if(serviceTrend == null) {
			synchronized(ServiceTrend.class) {
				if(serviceTrend == null) 
					serviceTrend = new ServiceTrend();
			}
		}
		return serviceTrend;
	}
	
	DAOTrend trendDAO = new DAOTrend();
	
	public boolean addTrend(BeanTrend trend){
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(trend.getUid());
		boolean flag = this.trendDAO.addTrend(trend);
		//缓存
		Integer key = new Integer(trend.getId());
		trendCache.spt(key, trend);
		
		if(homeUser != null) {
			LinkedList list = homeUser.getTrend();
			if(list.size() < USER_TREND_COUNT) {
				list.addFirst(key);
			} else {
				list.removeLast();
				list.addFirst(key);
			}
		}
		
		List friends = UserInfoUtil.getUserFriends(trend.getUid());
		for(int i = 0;i < friends.size();i++) {
			Integer iid = Integer.valueOf((String)friends.get(i));
			LinkedList ft = (LinkedList)friendTrendCache.get(iid);
			if(ft != null && userService.getUserFriend(iid.intValue(), trend.getUid()) != null) {
				synchronized(ft) {
					ft.addFirst(key);
					if(ft.size() > 50)
						ft.removeLast();
				}
			}
		}
		UserStatusBean us = UserInfoUtil.getUserStatus(trend.getUid());
		if(us.getTong() >= 20000) {
			FamilyHomeBean fm = FamilyAction.getFmByID(us.getTong());
			if(fm != null) {
				LinkedList list = fm.getTrendListDirect();
				if(list != null) {
					if(list.size() < FAMILY_TREND_COUNT) {
						list.addFirst(key);
					} else {
						list.removeLast();
						list.addFirst(key);
					}
				}
			}
		}
		return flag;
	}
	
	public boolean deleteTrendById(int id){
		boolean flag = this.trendDAO.deleteTrendById(id);
		trendCache.srm(id);
		return flag;
	}
	
	public List getTrendByUid(int uid, int start, int limit){
		
		return this.trendDAO.getTrendByUid(uid, start, limit);
	}
	
	public int getCountTrendByUid(int uid) {
		
		return this.trendDAO.getCountTrendByUid(uid);
	}
	
	public List getTrendByType(int uid, int type, int start, int limit) {
		
		return this.trendDAO.getTrendByType(uid, type, start, limit);
	}
	
	public int getCountTrendByType(int uid, int type) {
		return this.trendDAO.getCountTrendByType(uid, type);
	}
	
	public List getFriendTrendByUid(int uid, int start, int limit) {
		return this.trendDAO.getFriendTrendByUid(uid, start, limit);
	}
	
	public int getCountFriendTrendByUid(int uid) {
		return getFriendTrendList(uid).size();
		//return this.trendDAO.getCountFriendTrendByUid(uid);
	}
	
	public List getFriendTypeTrendByUid(int uid, int type, int start, int limit) {
		return this.trendDAO.getFriendTypeTrendByUid(uid, type, start, limit);
	}
	
	public int getCountFriendTypeTrendByUid(int uid, int type) {
		return this.trendDAO.getCountFriendTypeTrendByUid(uid, type);
	}
	
	public boolean addGameTrend(BeanTrend trend) {
		return this.trendDAO.addGameTrend(trend);
	}
	
	public List getGameTrendByType(int uid, int type, int start, int limit) {
		return this.trendDAO.getGameTrendByType(uid, type, start, limit);
	}
	
	public int getCountGameTrendByType(int uid, int type) {
		return this.trendDAO.getCountGameTrendByType(uid, type);
	}
	// 取得家族动态
	public List getFamilyTrendList(int fmId) {
		List list = SqlUtil.getIntList("select id from fm_user where fm_id=" + fmId, 5);
		
		StringBuilder sb = new StringBuilder(list.size() * 8 + 32);
		sb.append("uid in(");
		for(int i = 0;i < list.size();i++) {
			if(i != 0)
				sb.append(',');
			sb.append(list.get(i));
		}
		sb.append(") order by id desc limit 50");
		return trendDAO.getTrendList(sb.toString());
	}
	// 取得好友动态
	public List getFriendTrendList(int userId) {
		return getFriendTrendList(new Integer(userId));
	}
	public List getFriendTrendList(Integer key) {
		synchronized(friendTrendCache) {
			List list = (List)friendTrendCache.get(key);
			if(list == null) {
				list = trendDAO.getFriendTrendIdByUid(key.intValue(), 0, 50);
				if(list != null)
					friendTrendCache.put(key, list);
			}
			return list;
		}
	}
	public List getFriendTrendIdByUid(int uid, int start, int limit){
		List list = getFriendTrendList(uid);
		int end = start + limit;
		if(end >= list.size())
			end = list.size() - 1;
		if(end == -1)
			return list;
		return list.subList(start, end);
		//return this.trendDAO.getFriendTrendIdByUid(uid, start, limit);
	}
	
	public List getTrendIdByUid(int uid, int start, int limit){
		return this.trendDAO.getTrendIdByUid(uid, start, limit);
	}
	
	/**
	 * 先从缓存取，取不到再从数据库取
	 * @param id
	 * @return
	 */
	static ICacheMap trendCache = CacheManage.trend;
	static ICacheMap friendTrendCache = CacheManage.friendTrend;
	public BeanTrend getTrendById(Integer key) {
		synchronized(trendCache) {
			BeanTrend trend = (BeanTrend)trendCache.get(key);
			
			if(trend == null) {
				trend = this.trendDAO.getTrendById(key.intValue());
				trendCache.put(key, trend);
			}
			return trend;
		}
	}
	
	// 取得一个用户的动态
	public List getOneUserThrend(int uid,int start,int end){
		List list = trendDAO.getTrendByUid(uid,start,end);
		return list;
	}
}
