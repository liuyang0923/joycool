package net.joycool.wap.util;

import javax.servlet.http.HttpServletRequest;

public class CountUtil {
	public static int totalCount;
	public static int otherCount;
	public static int specialCount;
	
	public static void count(HttpServletRequest request){
		totalCount ++;
	}
	public static void count(){
		totalCount ++;
	}
	public static void countOther(){
		otherCount ++;
	}
	public static void countSpecial(){
		specialCount ++;
	}
	
	public static void reset() {
		totalCount = 0;
		otherCount = 0;
		specialCount = 0;
	}
}
