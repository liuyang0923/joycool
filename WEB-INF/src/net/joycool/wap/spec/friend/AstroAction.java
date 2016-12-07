package net.joycool.wap.spec.friend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;

public class AstroAction extends CustomAction {
	
	public static Calendar c = Calendar.getInstance();
	
	public static ArrayList astroList = null;
	public static byte[] initLock = new byte[0];
	public static AstroService service = new AstroService();

	// 按日期查寻星座时使用
	public static int[] dateSplit = {0,20,19,21,20,21,21,23,23,23,23,22,22};
	public static int[] astroNum = {0,10,11,12,1,2,3,4,5,6,7,8,9};
	
	// 文字分页,每页的字数
	public static int MAX_WORD_COUNT = 500;
	
	public static int getMAX_WORD_COUNT() {
		return MAX_WORD_COUNT;
	}

	public static void setMAX_WORD_COUNT(int mAXWORDCOUNT) {
		MAX_WORD_COUNT = mAXWORDCOUNT;
	}

	public static AstroService getService() {
		return service;
	}

	public ArrayList getAstroList() {
		if (astroList != null){
			return astroList;
		}
		synchronized (initLock){
			if (astroList != null){
				return astroList;
			}
			astroList = new ArrayList();
			astroList.add("error");
			astroList.add("白羊座(03/21-04/19)");
			astroList.add("金牛座(04/20-05/20)");
			astroList.add("双子座(05/21-06/20)");
			astroList.add("巨蟹座(06/21-07/22)");
			astroList.add("狮子座(07/23-08/22)");
			astroList.add("处女座(08/23-09/22)");
			astroList.add("天秤座(09/23-10/22)");
			astroList.add("天蝎座(10/23-11/21)");
			astroList.add("射手座(11/22-12/21)");
			astroList.add("摩羯座(12/22-01/19)");
			astroList.add("水瓶座(01/20-02/18)");
			astroList.add("双鱼座(02/19-03/20)");
			return astroList;
		}
	}

	public AstroAction() {
	}

	public AstroAction(HttpServletRequest request) {
		super(request);
		astroList = getAstroList();
	}

	public int getYearNow(long millis){
		c.setTimeInMillis(millis);
		return c.get(Calendar.YEAR);
	}
	
	public int getMonthNow(long millis){
		c.setTimeInMillis(millis);
		return c.get(Calendar.MONTH) + 1;
	}
	
	public int getDayNow(long millis){
		c.setTimeInMillis(millis);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	// 显示小星星 = =||
	public StringBuffer showStar(int num){
		StringBuffer strb = new StringBuffer();
		if (num <= 0){
			return strb;
		}
		for (int i = 0;i < num;i++){
			strb.append("★");
		}
		return strb;
	}
	
	// 返回星座名(无日期)
	public String getAstroNameNoDate(int num){
		String str = "";
		String tmp[];
		if (num < 1 || num > 12){
			return str;
		}
		str = (String)this.getAstroList().get(num);
		tmp = str.split("\\(");
		str = tmp[0];
		return str;
	}
	
	// 返回星座名(无日期)
	public String getAstroNameNoDate(String str){
		String tmp[];
		if ("".equals(str)){
			return "";
		}
		tmp = str.split("\\(");
		str = tmp[0];
		return str;
	}
	
	// 根据月,日,返回相应的星座
	public static int getAstroIdByDate(int month,int day){
		if (month <=0 || month >12 || day <= 0 || day >31){
			return 0;
		}
		if (day < dateSplit[month]){
			// 上一个星座
			return astroNum[month];
		} else {
			// 下一个星座
			if ((month + 1) > 12){
				// 摩羯座
				return astroNum[1];
			} else {
				return astroNum[month + 1];
			}
		}
	}
	
	// 根据util.date，返回相应的星座
	public static String getAstroStringByDate(Date date){
		int month = 0;
		int day = 0;
		String str = "";
		String str2[];
		if (date == null){
			return "";
		}
		str = DateUtil.formatDate(date);
		str2 = str.split("\\-");
		month = StringUtil.toInt(str2[1]);
		day = StringUtil.toInt(str2[2]);
		if (day < dateSplit[month]){
			// 上一个星座
			return astroList.get(astroNum[month]).toString();
		} else {
			// 下一个星座
			if ((month + 1) > 12){
				// 摩羯座
				return astroList.get(astroNum[1]).toString();
			} else {
				return astroList.get(astroNum[month + 1]).toString();
			}
		}
	}
	
	// 判断一个日期是否存在
	public boolean isDateExist(int year,int month,int day){
		try{
			Calendar c = Calendar.getInstance();
			c.setLenient(false);
			c.set(c.YEAR, year);
			c.set(c.MONTH, month - 1);
			c.set(c.DATE, day);
			c.get(c.YEAR);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	// 文章分页
	public String pagingTopic(AstroStory story,String pageStr,boolean addAnd){
		// 小于最大字数,直接返回.
		if (story.getContent().length() <= MAX_WORD_COUNT){
			return story.getContent();
		}
		
		StringBuffer sb = new StringBuffer();
		int contentLength = story.getContent().length();
		StringBuffer pageFeet = new StringBuffer();
		
		PagingBean paging = new PagingBean(this, contentLength, MAX_WORD_COUNT, "p2");
		sb.append(story.getContent().substring(paging.getStartIndex(), paging.getEndIndex()));
		
		pageFeet.append(paging.shuzifenye(pageStr, addAnd, "|", null));
		
		this.setAttribute("pageFeet", pageFeet.toString());
		
		return sb.toString();
	}
}