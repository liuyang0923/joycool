package net.joycool.wap.call;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.joycool.wap.bean.home.HomeDiaryBean;
import net.joycool.wap.bean.home.HomePhotoBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.impl.HomeServiceImpl;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 家园相关
 * 
 * @author bomb
 *  
 */
public class Home {
	static HomeServiceImpl homeService = new HomeServiceImpl();
	static ReentrantLock lock = new ReentrantLock();
    public static String photoPost(CallParam callParam) {
    	boolean cap = false;
    	try {
    		cap = lock.tryLock(100, TimeUnit.MILLISECONDS);
    		if(cap)
    			return getPhoto(callParam.request, callParam.response);
    	} catch(Exception e) {
    		
    	} finally {
    		if(cap)
    			lock.unlock();
    	}
    	return "";
    }
    
	public static String getPhoto(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder(128);
		
		Vector photoMark = homeService.getHomePhotoTopList2("order by b.id desc limit 2");
		HomePhotoBean homePhoto = null;
		for (int i = 0; i < photoMark.size(); i++) {
			homePhoto = (HomePhotoBean) photoMark.get(i);
			ret.append("<a href=\"/home/homePhoto.jsp?userId=");
			ret.append(homePhoto.getUserId());
			ret.append("&amp;hit=");
			ret.append(homePhoto.getId());
			ret.append("\">");
			ret.append("<img src=\"/rep");
			ret.append(homePhoto.getAttach());
			ret.append("\" alt=\"pic\"/>");
			ret.append("</a>");
		}
		ret.append("<br/>");

		return ret.toString();
	}
    
    public static String firstDiary(CallParam callParam) {
    	boolean cap = false;
    	String[] params = callParam.getParams();
    	int limit = 50;
    	if(params.length > 0)
    		limit = StringUtil.toId(params[0]);
    	HomeDiaryBean homeDiary = null;
    	try {
    		cap = lock.tryLock(100, TimeUnit.MILLISECONDS);
    		if(cap) {
    			List list = getDiaryList(limit);
    			if(list.size() > 0)
    				homeDiary = (HomeDiaryBean)RandomUtil.randomObject(list);
    		}
    	} catch(Exception e) {
    	} finally {
    		if(cap)
    			lock.unlock();
    	}
    	if(homeDiary != null) {
			StringBuilder ret = new StringBuilder(64);
			ret.append("<a href=\""
					+ ("/home/homeDiary.jsp?userId=" + homeDiary.getUserId())
					+ "&amp;diaryId=" + homeDiary.getId() + "\">");
			ret.append(StringUtil.toWml(StringUtil.limitString(homeDiary.getTitel(), 20)));
			ret.append("</a>");
			return ret.toString();
    	}
    	return "";
    }
    public static List getDiaryList(int limit) {
    	String key = "diary" + limit;
    	List list = (List) OsCacheUtil.get(key, "latest", 600);
    	if (list == null) {
    		synchronized(Joycool.class) {
    			list = (List) OsCacheUtil.get(key, "latest", 600);
    			if(list == null) {
    				int maxId = SqlUtil.getIntResult("select max(id) from jc_home_diary");
    				list = homeService.getHomeDiaryTopList("id>=" + (maxId - limit * 10) + " and del=0 order by hits desc limit " + limit);
    			}
    		}
    	}
    	return list;
    }
    
    public static String chatFriend(CallParam callParam) {
    	boolean cap = false;
    	try {
    		cap = lock.tryLock(100, TimeUnit.MILLISECONDS);
    		if(cap) {
    			return JoycoolSpecialUtil.getFriend(callParam.request, callParam.response);
    		}
    	} catch(Exception e) {
    	} finally {
    		if(cap)
    			lock.unlock();
    	}
    	return "";
    }
    
    public static String chatRoom(CallParam callParam) {
    	int count = StringUtil.toId(callParam.getParam());
    	return JoycoolSpecialUtil.getChatMessage(callParam.request, callParam.response, 0, count);
    }
    
}