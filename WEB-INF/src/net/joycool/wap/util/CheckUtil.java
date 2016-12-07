package net.joycool.wap.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	
	public static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdfDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	static {
		sdfDate.setLenient(false);
		sdfDatetime.setLenient(false);
		sdfTime.setLenient(false);
	}
	
	// 检查是否是正确的日期String
	public static boolean isDate(String date) {
		if(date == null)
			return false;

		try {
			return sdfDate.parse(date) != null;
		} catch (ParseException e) {
			return false;
		}
	}
	// 检查是否是正确的日期+时间String
	public static boolean isDatetime(String date) {
		if(date == null)
			return false;
		try {
			return sdfDatetime.parse(date) != null;
		} catch (ParseException e) {
			return false;
		}
	}
	// 检查是否是正确的时间String
	public static boolean isTime(String date) {
		if(date == null)
			return false;
		try {
			return sdfTime.parse(date) != null;
		} catch (ParseException e) {
			return false;
		}
	}
	public static Pattern intsPattern = Pattern.compile(" *, *");
	// 检查是否是id,id,id这样的格式
	public static boolean isIntList(String str) {
		if(str == null)
			return false;
		Matcher m = intsPattern.matcher(str);
		int pos = 0;
		while(m.find()) {
			int end = m.end();
			String num = str.substring(pos, m.start());
			try {
				Integer.parseInt(num);
			} catch(Exception e) {
				return false;
			}
			pos = end;
		}
		String num = str.substring(pos, str.length());
		try {
			Integer.parseInt(num);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
}
