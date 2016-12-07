package net.joycool.wap.spec.buyfriends;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.ShortcutBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.spec.app.AppAction;
import net.joycool.wap.spec.app.AppBean;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LinkBuffer;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class BeanVisit {
	static final long MIN_MILLIS = 60000l;
	static final long HOUR_MILLIS = 60 * MIN_MILLIS;
	static final long DAY_MILLIS = 24 * HOUR_MILLIS;
	static final long WEEK_MILLIS = 7 * DAY_MILLIS;
	static final long MONTH_MILLIS = 30 * DAY_MILLIS;
	
	public static final int CACHE_RECENT_VISIT_COUNT = 5;
	
	int id;
	int fromUid;
	String fromNickName;
	int toUid;
	String toNickName;
	Date visitTime;
	
	public boolean equals(Object obj) {
		
		if(obj instanceof BeanVisit) {
			BeanVisit bean = (BeanVisit)obj;
			if(bean.fromUid == fromUid) {
				return true;
			}
		}
		
		return false;
	}
	public int hashCode() {
		return fromUid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFromUid() {
		return fromUid;
	}
	public void setFromUid(int fromUid) {
		this.fromUid = fromUid;
	}
	public String getFromNickName() {
		return fromNickName;
	}
	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
	}
	public int getToUid() {
		return toUid;
	}
	public void setToUid(int toUid) {
		this.toUid = toUid;
	}
	public String getToNickName() {
		return toNickName;
	}
	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}
	public String getVisitTime() {
		return converDateToBefore2(visitTime);
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
	public static String converDateToBefore(String date){
		return converDateToBefore(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm:ss"));
	}
	
	public static String converDateToBefore2(String date){
		return converDateToBefore2(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm:ss"));
	}
	
	public static String converDateToBefore(Date date) {
		   long cur = System.currentTimeMillis();
		        Calendar calDate = Calendar.getInstance();
		    calDate.setTime(date);
		        String time;
		        
		        long millis = cur - calDate.getTimeInMillis();
		        
		        if(millis < MONTH_MILLIS) {
		        if(millis < WEEK_MILLIS ) {
		         if(millis < DAY_MILLIS) {
		            if(millis < HOUR_MILLIS) {
		            	if(millis < MIN_MILLIS) {
		            		long sec = millis / 1000;
		            		return sec + "秒钟前";
		            	}
		             long min = millis / MIN_MILLIS;
		             return min + "分钟前";
		            } else {
		             long hour = millis / HOUR_MILLIS;
		             return hour + "小时前";
		            }
		           
		         } else {
		            long day = millis / DAY_MILLIS;
		            long hour = (millis % DAY_MILLIS) / HOUR_MILLIS;
		            if(hour > 0) {
		             return day + "天" + hour + "小时前";
		            } else {
		             return day + "天前";
		            }
		           
		         }
		         } else {
		         long week = millis / WEEK_MILLIS;
		         long day = (millis % WEEK_MILLIS) / DAY_MILLIS;
		         if(day > 0) {
		            return week + "周" + day + "天前";
		         } else {
		            return week + "周前";
		         }
		         
		         }
		        } else {
		        try {
		            time = DateUtil.formatDate(date, "MM月dd HH:mm");
		       } catch (Exception e) {
		        return "很久以前";
		       }
		        }
		return time;
	}
	
	public static String converDateToBefore2(Date date) {
		long cur = System.currentTimeMillis();
		 long millis = cur - date.getTime();
		        
		 	if(millis < MONTH_MILLIS) {
		 		if(millis < DAY_MILLIS) {
		 			if(millis < HOUR_MILLIS) {
		 				if(millis < MIN_MILLIS) {
		 					long sec = millis / 1000;
		 					return sec + "秒前";
		 				}
		 				long min = millis / MIN_MILLIS;
		 				return min + "分前";
		            } else {
		            	long hour = millis / HOUR_MILLIS;
		            	return hour + "时前";
		            }
		           
		         } else {
		        	 long day = millis / DAY_MILLIS;
		        	 return day + "天前";
		         }
		         
		   } else {
			   return "1月前";
		  }
	}
	
	//static ICacheMap shortcutCache = CacheManage.shortcut;
	public static String getUserShortcut2String(UserBean user, HttpServletResponse response) {
		
		int	userId = user.getId();
		
		
		//synchronized(shortcutCache) {
			//s = (List) shortcutCache.get(key);
			//if(s == null) {
		LinkBuffer lb = new LinkBuffer(response);
				HashMap map = UserInfoUtil.getShortcutMap();
				//String shortcut = "1";
				String[] ss = user.getUserSetting().getShortcut2().split(",");
				
				for(int i = 0;i < ss.length;i++) {
					int ssid = StringUtil.toInt(ss[i]);
					if(ssid > 500) {	// 插件的链接
						AppBean bean = AppAction.getApp(ssid - 500);
						if(bean != null) {
							lb.appendLink(bean.getDirFull(), bean.getShortName());
						}
					} else if(ssid > 0) {
						ShortcutBean bean = (ShortcutBean)map.get(Integer.valueOf(ssid));
						if(bean != null) {
							lb.appendLink(bean.getUrl(), bean.getShortName());
						}
						
					}
				}			
				//shortcutCache.put(key, s);
			//}
		//}
//		LinkBuffer lb = new LinkBuffer(response);
//		//int col = 0;
//		Iterator iter = s.iterator();
//		while(iter.hasNext()) {
//			ShortcutBean bean = (ShortcutBean)iter.next();
//			System.out.println(bean.getUrl());
//			System.out.println(bean.getShortName());
//			lb.appendLink(bean.getUrl(), bean.getShortName());
//		}
		
		return lb.toString();
	}
}
