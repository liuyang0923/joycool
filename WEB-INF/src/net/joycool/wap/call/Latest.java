package net.joycool.wap.call;

import java.util.*;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.top.MoneyTopBean;
import net.joycool.wap.bean.top.UserTopBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.cache.util.TongCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.HomeServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.spec.buyfriends.ServiceTrend;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 最新动态，聊天、论坛、圈子等等各种信息的最新
 * 
 * @author bomb
 *  
 */
public class Latest {
	
	public static UserServiceImpl userService = new UserServiceImpl();
	// 新注册用户
	public static String newUser(CallParam callParam) {
		String[] params = callParam.getParams();
		int count1 = 2, count2 = 20;	// 从最新20个随机选出2个
		if(params.length > 0) {
			count1 = StringUtil.toId(params[0]);
			if(params.length > 1)
				count2 = StringUtil.toId(params[1]);
		}
		if(count1 == 0 || count1 > count2)
			return "";
		List list = getNewUserList(count2);
		if(list.size() == 0)
			return "";
		if(count1 > list.size())
			count1 = list.size();
		int start = RandomUtil.nextInt(list.size() - count1 + 1);
		StringBuilder sb = new StringBuilder(count1 * 32);
		for(int i = 0;i < count1;i++) {
			Object[] objs = (Object[])list.get(start + i);
			if(i > 0)
				sb.append(',');
			sb.append("<a href=\"/user/ViewUserInfo.do?userId=");
			sb.append(objs[0]);
			sb.append("\">");
			sb.append(StringUtil.toWml((String) objs[1]));
			sb.append("</a>");
		}
		return sb.toString();
	}
	
	public static List getNewUserList(int limit) {
		String key = "newUser";
		List list = (List) OsCacheUtil.get(key, "latest", 300);
		if (list == null) {
			synchronized (Latest.class) {
				list = (List) OsCacheUtil.get(key, "latest", 300);
				if (list == null) {
					list = SqlUtil.getObjectsList("select id,nickname from user_info order by id desc limit " + limit);
					OsCacheUtil.put(key, list, "latest");
				}
			}
		}
		return list;
	}
//	 最近新闻轮换显示
	public static String news(CallParam callParam) {
		String[] params = callParam.getParams();
		int type = 9, count1 = 2, count2 = 8;	// 从最新20个随机选出2个，新闻类型默认是乐酷热点
		if(params.length > 0) {
			type = StringUtil.toId(params[0]);
			if(params.length > 1) {
				count1 = StringUtil.toId(params[1]);
				if(params.length > 2) {
					count2 = StringUtil.toId(params[1]);
				}
			}
		}

		if(count1 == 0 || count1 > count2)
			return "";
		List list = getNewsList(type, count2);
		if(list.size() == 0)
			return "";
		if(count1 > list.size())
			count1 = list.size();
		int start = RandomUtil.nextInt(list.size() - count1 + 1);
		StringBuilder sb = new StringBuilder(count1 * 12 + 20);
		sb.append("<a href=\"/news/list.jsp?id=");
		sb.append(type);
		sb.append("\">");
		for(int i = 0;i < count1;i++) {
			Integer iid = (Integer)list.get(start + i);
			sb.append('*');
			ForumContentBean con = ForumCacheUtil.getForumContent(iid.intValue());
			if(con == null) continue;
			sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(), 24)));
			sb.append("<br/>");
		}
		sb.append("</a>");
		return sb.toString();
	}
	
	public static List getNewsList(int type, int limit) {
		String key = "news" + type;
		List list = (List) OsCacheUtil.get(key, "latest", 300);
		if (list == null) {
			synchronized (Latest.class) {
				list = (List) OsCacheUtil.get(key, "latest", 300);
				if (list == null) {
					list = SqlUtil.getIntList("select id from forum_news where type=" + type + " order by id desc limit " + limit, 2);
					OsCacheUtil.put(key, list, "latest");
				}
			}
		}
		return list;
	}
	
	// 最新动态轮换显示，如果是多个type可以用-分割
	public static String trend(CallParam callParam) {
		String[] params = callParam.getParams();
		int count1 = 2, count2 = 8;	// 从最新20个随机选出2个，新闻类型默认是乐酷热点
		String types = "15";
		if(params.length > 0) {
			count1 = StringUtil.toId(params[0]);
			if(params.length > 1) {
				count2 = StringUtil.toId(params[1]);
				if(params.length > 2) {
					types = params[2].replace('-', ',');
				}
			}
		}

		if(count1 == 0 || count1 > count2)
			return "";
		List list = getTrendList(count2, types);
		if(list.size() == 0)
			return "";
		if(count1 > list.size())
			count1 = list.size();
		int start = RandomUtil.nextInt(list.size() - count1 + 1);
		StringBuilder sb = new StringBuilder(count1 * 32);
		for(int i = 0;i < count1;i++) {
			Integer iid = (Integer)list.get(start + i);
			sb.append('*');
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(iid);
			if(trend == null) continue;
			sb.append(trend.getContentNoUserLink(0, null));
			sb.append("<br/>");
		}
		return sb.toString();
	}
	
	public static List getTrendList(int limit, String types) {
		String key = "trend" + types;
		List list = (List) OsCacheUtil.get(key, "latest", 600);
		if (list == null) {
			synchronized (Latest.class) {
				list = (List) OsCacheUtil.get(key, "latest", 600);
				if (list == null) {
					list = SqlUtil.getIntList("select id from trend where type in (" + types + ") order by id desc limit " + limit, 5);
					OsCacheUtil.put(key, list, "latest");
				}
			}
		}
		return list;
	}
	
	public static String moneyTop(CallParam callParam) {
		List topList = LoadResource.getMoneyTopList();
		if(topList == null || topList.size() == 0)
			return "";
		MoneyTopBean top = (MoneyTopBean)RandomUtil.randomObject(topList);
		UserBean user = UserInfoUtil.getUser(top.getUserId());
		if(user == null)
			return "";
		
		StringBuilder sb = new StringBuilder(32);
		sb.append("[<a href=\"/top/moneyTop.jsp\">富豪榜</a>]");
		sb.append("<a href=\"/user/ViewUserInfo.do?userId=");
		sb.append(user.getId());
		sb.append("\">");
		sb.append(user.getNickNameWml());
		sb.append("</a><br/>");
		return sb.toString();
	}
	
	public static String blackTop(CallParam callParam) {
		List topList = (Vector) OsCacheUtil.get("blacklist", OsCacheUtil.TOP_GROUP, 60 * 60);
		if (topList == null) {
			topList = ServiceFactory.createTopService().getUserTopList(
					" mark=1 order by priority asc limit 0,10");
			OsCacheUtil.put("blacklist", topList, OsCacheUtil.TOP_GROUP);
		}
		if(topList == null || topList.size() == 0)
			return "";
		UserTopBean top = (UserTopBean)RandomUtil.randomObject(topList);
		UserBean user = UserInfoUtil.getUser(top.getUserId());
		
		StringBuilder sb = new StringBuilder(32);
		sb.append("[<a href=\"/top/blackList.jsp\">恶人榜</a>]");
		sb.append("<a href=\"/user/ViewUserInfo.do?userId=");
		sb.append(user.getId());
		sb.append("\">");
		sb.append(user.getNickNameWml());
		sb.append("</a><br/>");
		return sb.toString();
	}
	
	public static String pkTop(CallParam callParam) {
		List topList = (List) OsCacheUtil.get("pklist", "top", 24 * 60 * 60);
		if(topList == null || topList.size() == 0)
			return "";
		Integer top = (Integer)RandomUtil.randomObject(topList);
		UserBean user = UserInfoUtil.getUser(top.intValue());
		
		StringBuilder sb = new StringBuilder(32);
		sb.append("[<a href=\"/top/pkList.jsp\">赢家榜</a>]");
		sb.append("<a href=\"/user/ViewUserInfo.do?userId=");
		sb.append(user.getId());
		sb.append("\">");
		sb.append(user.getNickNameWml());
		sb.append("</a><br/>");
		return sb.toString();
	}
	
	public static String homeTop(CallParam callParam) {
		List topList = getHomeTopList(10);
		if(topList == null || topList.size() == 0)
			return "";
		HomeUserBean top = (HomeUserBean)RandomUtil.randomObject(topList);
		UserBean user = UserInfoUtil.getUser(top.getUserId());
		
		StringBuilder sb = new StringBuilder(32);
		sb.append("[<a href=\"/home/viewAllHome.jsp\">家园星</a>]");
		sb.append("<a href=\"/home/home2.jsp?userId=");
		sb.append(user.getId());
		sb.append("\">");
		sb.append(user.getNickNameWml());
		sb.append("</a><br/>");
		return sb.toString();
	}
	
	static HomeServiceImpl homeService = new HomeServiceImpl();
	public static List getHomeTopList(int limit) {
		String key = "homeTop";
		List list = (List) OsCacheUtil.get(key, "latest", 60 * 60);
		if (list == null) {
			list = homeService.getHomeUserList(
					" 1=1 order by hits desc LIMIT " + limit);
			OsCacheUtil.put(key, list, "latest");
		}
		return list;
	}

	public static String tongTop(CallParam callParam) {
		List tongList = TongCacheUtil.getTongListById("honor");
		if(tongList.size() > 10)
			tongList = tongList.subList(0, 10);
		if(tongList == null || tongList.size() == 0)
			return "";
		Integer iid = (Integer)RandomUtil.randomObject(tongList);
		TongBean tong = TongCacheUtil.getTong(iid.intValue());
		StringBuilder sb = new StringBuilder(32);
		sb.append("[<a href=\"/tong/tongList.jsp\">帮会</a>]");
		sb.append("<a href=\"/tong/tong.jsp?tongId=");
		sb.append(tong.getId());
		sb.append("\">");
		sb.append(StringUtil.toWml(tong.getTitle()));
		sb.append("</a><br/>");
		return sb.toString();
	}
}