/*
 * Created on 2006-3-9
 *
 */
package net.joycool.wap.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author lbj
 * 
 */
public class DateUtil {
	public static Calendar normal = Calendar.getInstance();
	public static long ZONE_OFFSET = normal.get(Calendar.ZONE_OFFSET);
	public static long MS_IN_DAY = 86400l * 1000;
	public static long MS_IN_HOUR = 3600l * 1000;
	
	public static final String normalTimeFormat = "yyyy-MM-dd HH:mm:ss";

	public static final String normalDateFormat = "yyyy-MM-dd";

	public static String formatDate(Date date) {
		String ret = "";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(normalDateFormat);
			ret = sdf.format(date);
		} catch (Exception e) {
		}

		return ret;
	}

	public static String formatDate(Date date, String format) {
		String ret = "";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			ret = sdf.format(date);
		} catch (Exception e) {
		}

		return ret;
	}
	
	public static Date parseTime(String dateString) {
		return parseDate(dateString, normalTimeFormat);
	}
	
	public static Date parseDate(String dateString) {
		return parseDate(dateString, normalDateFormat);
	}

	public static Date parseDate(String dateString, String formate) {
		Date ret = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formate);
			ret = sdf.parse(dateString);
		} catch (Exception e) {
		}
		return ret;
	}

	public static Date rollDate(int rollDateCount) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, rollDateCount);
		return cal.getTime();
	}
	//wucx 2006-10-29 start
	public static Date rollDate(Date date,int rollDateCount) {
		int a=date.getDate()+rollDateCount;
		date.setDate(a);
		return date;
	}
	
	public static Date rollHour(Date date,int rollHoueCount) {
		int b =date.getHours()+rollHoueCount;
		date.setHours(b);
		return date;
	}
	//wucx 2006-10-29 start
	public static long getCurrentTime() {
		Date date = new Date();
		return date.getTime();
	}

	/**
	 * fanys 2006-09-19 当天日期
	 * 
	 * @return
	 */
	public static String getToday() {
		Calendar cal = Calendar.getInstance();
		return formatDate(cal.getTime());
	}

	/**
	 * fanys 2006-09-19 一个月的头一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DAY_OF_MONTH, -(dayOfMonth - 1));
		return formatDate(cal.getTime());
	}

	/**
	 * fanys 2006-09-19 一周的头一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return formatDate(c.getTime());
	}

	public static String getCurrentTimeAsStr() {
		return formatTime(new Date());
	}
	
	public static String getCurrentDatetimeAsStr() {
		return formatDatetime(new Date());
	}
	
	/**
	 * liuyi 2007-01-25 日期显示
	 * @param time
	 * @return
	 */
	public static String getDateString(String time){
		String ret = "2006年1月1日";
		Date date = DateUtil.parseDate(time, DateUtil.normalDateFormat);
		if(date==null){
			return ret;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		ret = (c.get(Calendar.YEAR)) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) +  "日";
		return ret;
	}

	public static void main(String[] args) {
		System.out.println(getCurrentTimeAsStr());
	}
	
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年M月d日");

	public static String formatDate1(Date date) {
		String ret = sdf1.format(date);
		return ret;
	}
	
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d/H:mm");
	static SimpleDateFormat sdf2s = new SimpleDateFormat("M-d/H:mm");
	static SimpleDateFormat sdf5 = new SimpleDateFormat("H:mm:ss");

	public static String formatDate2(Date date) {
		String ret = sdf2.format(date);
		return ret;
	}
	public static String formatDate2(long time) {
		String ret = sdf2.format(new Date(time));
		return ret;
	}
	public static String formatTime3(Date date) {
		String ret = sdf5.format(date);
		return ret;
	}
	public static String formatTime3(long time) {
		String ret = sdf5.format(new Date(time));
		return ret;
	}
	public static String sformatTime(long time) {
		long now = System.currentTimeMillis();
		if(dayDiff(time, now) == 0)
			return sdf3.format(new Date(time));
		else if(now - time < 300 * 86400000l)
			return sdf2s.format(new Date(time));
		else
			return sdf2.format(new Date(time));
	}
	public static String sformatTime(Date date) {
		long now = System.currentTimeMillis();
		if(dayDiff(date.getTime(), now) == 0)
			return sdf3.format(date);
		else if(now - date.getTime() < 300 * 86400000l)
			return sdf2s.format(date);
		else
			return sdf2.format(date);
	}
	
	static SimpleDateFormat sdfSql = new SimpleDateFormat(normalTimeFormat);
	// 转换为mysql格式的datetime
	public static String formatSqlDatetime(Date date) {
		String ret = sdfSql.format(date);
		return ret;
	}
	public static String formatSqlDatetime(long time) {
		String ret = sdfSql.format(new Date(time));
		return ret;
	}
	
	static SimpleDateFormat sdf3 = new SimpleDateFormat("H:mm");
	
	public static String formatTime(Date date) {
		String ret = sdf3.format(date);
		return ret;
	}
	public static String formatTime(long time) {
		Date date = new Date(time);
		String ret = sdf3.format(date);
		return ret;
	}
	
	static SimpleDateFormat sdf4 = new SimpleDateFormat("M月d日/E/H:mm");
	
	public static String formatDatetime(Date date) {
		String ret = sdf4.format(date);
		return ret;
	}
	
	public static String formatTimeInterval(long ms) {
		return formatTimeInterval((int)(ms / 1000));
	}
	public static String formatTimeInterval3(long ms) {
		return formatTimeInterval3((int)(ms / 1000));
	}
	// 格式化显示时间间隔
	public static String formatTimeInterval(int sec) {
		if(sec < 60) {
			if(sec < 0)
				return "0秒";
			else
				return sec + "秒";
		}
		sec /= 60;
		if(sec < 60)
			return sec + "分钟";
		sec /= 60;
		if(sec < 24)
			return sec + "小时";
		sec /= 24;
		return sec + "天";
	}
	private static DecimalFormat numFormat = new DecimalFormat("0.0");
	
	public static String formatTimeInterval3(int sec) {
		if(sec < 60) {
			if(sec < 0)
				return "0秒";
			else
				return sec + "秒";
		}
		sec /= 60;
		if(sec < 60)
			return sec + "分钟";
		sec /= 60;
		if(sec < 24)
			return sec + "小时";
		if(sec >= 2400)
			return (sec / 24) + "天";
		else
			return numFormat.format((float)sec / 24) + "天";
	}
	
	// 时间间隔，格式为324:04:22
	public static String formatTimeInterval2(int sec) {
		if(sec <= 0)
			return "0:00:0?";
		StringBuilder sb = new StringBuilder(8);
		int sih = sec % 3600;
		sb.append(sec / 3600);
		sb.append(':');
		int min = sih / 60;
		if(min < 10)
			sb.append('0');
		sb.append(min);
		sb.append(':');
		sec = sih % 60;
		if(sec < 10)
			sb.append('0');
		sb.append(sec);
		
		return sb.toString();
	}
	public static String formatTimeInterval2(long end) {
		return formatTimeInterval2((int)((end - System.currentTimeMillis()) / 1000));
	}
	
	// 是否超过了某个时间，past单位秒
	public static boolean timepast(Date from, int past) {
		return timepast(from.getTime(), past);
	}
	
	public static boolean timepast(long from, int past) {
		return (System.currentTimeMillis() - from) / 1000 > past;
	}
	
	// 返回两个日期之间的天数（自然日）
	public static int dayDiff(Date a, Date b) {
		long day1 = (a.getTime() + ZONE_OFFSET) / MS_IN_DAY;
		long day2 = (b.getTime() + ZONE_OFFSET) / MS_IN_DAY;
		return (int)(day2 - day1);
	}
	public static int dayDiff(long a, long b) {
		long day1 = (a + ZONE_OFFSET) / MS_IN_DAY;
		long day2 = (b + ZONE_OFFSET) / MS_IN_DAY;
		return (int)(day2 - day1);
	}
	
	public static int dayDiff(Date a) {
		return dayDiff(a, new Date());
	}
	// 当前是几点，0-23
	public static int dayHour(long time) {
		return (int)((time + ZONE_OFFSET) / MS_IN_HOUR) % 24 ;
	}
	
	static final long MIN_MILLIS = 60000l;
	static final long HOUR_MILLIS = 60 * MIN_MILLIS;
	static final long DAY_MILLIS = 24 * HOUR_MILLIS;
	static final long WEEK_MILLIS = 7 * DAY_MILLIS;
	static final long MONTH_MILLIS = 30 * DAY_MILLIS;
	public static String converDateToBefore(Date date) {
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
}
