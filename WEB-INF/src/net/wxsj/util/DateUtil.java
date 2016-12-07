/*
 * Created on 2006-10-26
 *
 */

package net.wxsj.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 作者：张毅
 * 
 * 创建日期：2007-1-26
 * 
 * 说明：
 */
public class DateUtil {
	
	public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    } 
    
	public static java.sql.Date getDate(){
		Calendar cal = Calendar.getInstance();
		long date = cal.getTime().getTime();
		return  new java.sql.Date(date);
	}
	
	public static java.sql.Time getTime(){
		Calendar cal = Calendar.getInstance();
		long time = cal.getTime().getTime();
		return  new java.sql.Time(time);
	}
	
    public static void main(String[] args) {
        System.out.println(getNow());
    }
}

