/*
 * Created on 2005-7-28
 *
 */
package net.joycool.wap.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.call.CallParam;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;

/**
 * @author lbj
 * 
 */
public class StringUtil {
	public static int unique = 0;

	private static DecimalFormat numFormat = new DecimalFormat("0.00");

	// liuyi 2007-01-17 部分用户字符集问题 start
	public static boolean ignoreAnd = false;

	// liuyi 2007-01-17 部分用户字符集问题 end

	public static IUserService userService = ServiceFactory.createUserService();

	public static String convertNull(String s) {
		if (s == null) {
			return "";
		} else {
			return s;
		}
	}

	public static String cutString(String s, int count) {
		if (s == null) {
			return s;
		}
		if (s.length() < count) {
			return s;
		}
		s = s.substring(0, count);
		return s;
	}

	public static String trim(String s) {
		if (s == null) {
			return s;
		}
		s = s.replace("\r\n", " ");
		s = s.replace("\n", " ");
		s = s.replaceAll("  +", " ");	// 多个空格换成一个
		return s;
	}
	
	public static String trimAll(String s) {
		if (s == null) {
			return s;
		}
		s = s.replaceAll("[\r\n][\r\n]+", "\n");
		s = s.replaceAll("  +", " ");	// 多个空格换成一个
		return s;
	}

	public static String getURLContent(String url) {
		try {
			URL u = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(u
					.openStream()));
			String str = br.readLine();
			StringBuilder sb = new StringBuilder();
			while (str != null) {
				sb.append(str);
				str = br.readLine();
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean checkDomain(String domain) {
		if (domain.equals("www") || domain.equals("blog")
				|| domain.equals("my") || domain.equals("forum")
				|| domain.equals("wap")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getCurrentPageURL(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (queryString != null) {
			// liuyi 2007-01-22 请求地址修改 start
			int endIndex = queryString.indexOf("backTo");
			if (endIndex != -1) {
				queryString = queryString.substring(0, endIndex);
			}
			// liuyi 2007-01-22 请求地址修改 end
			return request.getRequestURI() + "?" + queryString;
		} else {
			return request.getRequestURI();
		}
	}
	
	public static String getURI(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (queryString != null) {
			return request.getRequestURI() + "?" + queryString;
		} else {
			return request.getRequestURI();
		}
	}
	
	public static String getUniqueURL(HttpServletRequest request) {
		String refreshUrl = PageUtil.getCurrentPageURL(request);
//		int ui = refreshUrl.indexOf("u=");
//		if(ui != -1){
//			refreshUrl = refreshUrl.substring(0, ui) + getUniqueParam();
//		}
//		if(refreshUrl.indexOf("?") != -1){
//			refreshUrl += "&" + getUniqueParam();
//		} else {
//			refreshUrl += "?" + getUniqueParam();
//		}
		return refreshUrl.replace("&", "&amp;");
	}

	public static String getBackTo(HttpServletRequest request) {
		try {
			return URLEncoder.encode(StringUtil.getCurrentPageURL(request),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isCorrect(String s) {
		if (s == null) {
			return false;
		} else {
			for (int i = 0; i < s.length(); i++) {
				int a = s.charAt(i);
				if ((a >= 48 && a <= 57) || (a >= 65 && a <= 90)
						|| (a >= 97 && a <= 122)) {
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static String convertString(String s) {
		if (s == null) {
			return null;
		}
		s = s.replace("&", "&amp;");
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		return s;
	}

	public static int toInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	// 逗号间隔的数字转化为数组
	public static List toInts(String ss) {
		List l = new ArrayList();
		if(ss != null) {
			String[] s = ss.split(",");
			for(int i = 0;i < s.length;i++) {
				int id = StringUtil.toInt(s[i]);
				if(id > 0)
					l.add(Integer.valueOf(id));
			}
		}
		return l;
	}
	// 支持1-10这样的写法
	public static List toInts2(String ss) {
		List l = new ArrayList();
		if(ss != null) {
			String[] s = ss.split(",");
			for(int i = 0;i < s.length;i++) {
				String[] s2 = s[i].split("-");
				if(s2.length == 2) {
					int id = StringUtil.toInt(s2[0]);
					int id2 = StringUtil.toInt(s2[1]);
					if(id > 0 && id2 - id < 1000)
						for(;id <= id2;id++)
							l.add(Integer.valueOf(id));
				} else {
					int id = StringUtil.toInt(s[i]);
					if(id > 0)
						l.add(Integer.valueOf(id));
				}
			}
		}
		return l;
	}
	
	//	 逗号间隔的数字组转化为数组，每个组内用-分割，如果数量不够，用def数字内的数字代替
	public static List toIntss(String ss, int count, int[] def) {
		if(ss != null && ss.length() > 0) {
			String[] ms = ss.split(",");
			List l = new ArrayList(ms.length);
			for(int i = 0;i < ms.length;i++) {
				String[] ms2 = ms[i].split("-", count);
				int[] mat = new int[count];
				int j = 0;
				for(;j < ms2.length;j++) {
					mat[j] = StringUtil.toInt(ms2[j]);
					if(mat[j] < 0)
						mat[j] = def[j];
				}
				for(;j < count;j++) {
					mat[j] = def[j];
				}
				if(mat[0] >= 0)
					l.add(mat);
			}
			return l;
		}
		return new ArrayList(0);
	}
	// 同上函数，但是每个切割分组内的数量不固定
	public static List toIntss(String ss) {
		if(ss != null && ss.length() > 0) {
			String[] ms = ss.split(",");
			List l = new ArrayList(ms.length);
			for(int i = 0;i < ms.length;i++) {
				String[] ms2 = ms[i].split("-");
				int[] mat = new int[ms2.length];
				for(int j = 0;j < ms2.length;j++) {
					mat[j] = StringUtil.toInt(ms2[j]);
				}
				if(mat[0] >= 0)
					l.add(mat);
			}
			return l;
		} else 
			return new ArrayList(0);
		
	}
	
	public static int hexToInt(String s) {
		try {
			return Integer.parseInt(s, 16);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static float toFloat(String s) {
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static long toLong(String s) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static String toWml(String src) {
		if (src == null)
			return src;

		// liuyi 2007-01-17 部分用户字符集问题 start
		if (ignoreAnd) {
			return toWmlIgnoreAnd(src);
		}
		// liuyi 2007-01-17 部分用户字符集问题 end

		src = src.replace("&", "&amp;");
		src = src.replace("$", "$$");
		// src = src.replace("\"", "&quot;");
		src = src.replace("<", "&lt;");
		src = src.replace(">", "&gt;");
		src = src.replace("\r\n", "<br/>");
		src = src.replace("\n", "<br/>");
		src = src.replace("\"", "“");

		// src = src.replace("'","&apos;");
		// src = src.replace("\"","&quot;");
		// src = src.replace("$","$$");
		// src = src.replace(" ","&nbsp;");
		// String result = src;
		return src;

	}
	
	/**
	 * 用于wml中引号内部的内容处理
	 * @param src
	 * @return
	 */
	public static String toWmlQ(String src) {
		if (src == null)
			return src;

		// liuyi 2007-01-17 部分用户字符集问题 start
//		if (ignoreAnd) {
//			return toWmlIgnoreAnd(src);
//		}
		// liuyi 2007-01-17 部分用户字符集问题 end

		src = src.replace("&", "&amp;");
		src = src.replace("$", "$$");
		// src = src.replace("\"", "&quot;");
		src = src.replace("<", "&lt;");
		src = src.replace(">", "&gt;");
		//src = src.replace("\r\n", "<br/>");
		//src = src.replace("\n", "<br/>");
//		src = src.replace("", "");
		src = src.replace("\"", "“");

		// src = src.replace("'","&apos;");
//		src = src.replace("\"","&quot;");
		// src = src.replace("$","$$");
		// src = src.replace(" ","&nbsp;");
		// String result = src;
		return src;

	}

	//2007.6.20 liq去掉字符串中的回车，和空格符
	public static String noEnter(String src) {
		if (src == null)
			return src;
		src = src.replace("　", "");
		src = removeSpecialAsc(src);
		return src;

	}
	
	public static final int high_surrogate_start = 0xD800;
	public static final int high_surrogate_end = 0xDBFF;
	
	public static final int low_surrogate_start = 0xDC00;
	public static final int low_surrogate_end = 0xDFFF;	// 解决&#55947;&#57075;的问题

	public static String removeCtrlAsc(String str) {
		if(str == null)
			return str;
		str = wapUnicode(str);
		StringBuilder sb = new StringBuilder(str.length());
		char[] cs = str.toCharArray();
		for(int i = 0;i < cs.length;i++) {
			if(cs[i] > 31 && cs[i] < high_surrogate_start && ((cs[i] & 0xFFF8) != 0x2028) || cs[i] > low_surrogate_end &&  cs[i] < 0xFFFE || cs[i] == '\r' || cs[i] == '\n')// 中文或者非table的ascii
				sb.append(cs[i]);
		}
		return sb.toString();
	}
	
	// 去掉所有控制字符，包括回车和空格
	public static String removeSpecialAsc(String str) {
		if(str == null)
			return str;
		str = wapUnicode(str);
		StringBuilder sb = new StringBuilder(str.length());
		char[] cs = str.toCharArray();
		for(int i = 0;i < cs.length;i++) {
			if(cs[i] > 32 && cs[i] < high_surrogate_start && ((cs[i] & 0xFFF8) != 0x2028) || cs[i] > low_surrogate_end && cs[i] < 0xFFFE)	// 中文或者非空格、换行、table的ascii
				sb.append(cs[i]);
		}
		return sb.toString();
	}
	
	// 去掉所有ascii字符
	public static String removeAsc(String str) {
		if(str == null)
			return str;
		str = wapUnicode(str);
		StringBuilder sb = new StringBuilder(str.length());
		char[] cs = str.toCharArray();
		for(int i = 0;i < cs.length;i++) {
			if(cs[i] >= 256 && cs[i] < high_surrogate_start && ((cs[i] & 0xFFF8) != 0x2028) || cs[i] > low_surrogate_end &&  cs[i] < 0xFFFE)
				sb.append(cs[i]);
		}
		return sb.toString();
	}
	
	public static boolean isChinese(String str) {
		if(str == null)
			return false;
		char[] cs = str.toCharArray();
		for(int i = 0;i < cs.length;i++) {
			if(cs[i] < 256)
				return false;
		}
		return true;
	}
	
	// 是否是中文
	public static boolean isChinese(char c) {
		return c < 0 || c >= 256;
	}
	public static boolean isAsc(char c) {
		return c >= 0 && c < 256;
	}
	
	// 英文算一个格子，中文算两个，计算总长度
	public static int getCLength(String str) {
		char[] cs = str.toCharArray();
		int sum = 0;
		for(int i = 0;i < cs.length;i++) {
			if(isAsc(cs[i]))
				sum++;
		}
		return cs.length + cs.length - sum;
	}
	
	
	// liuyi 2007-01-17 部分用户字符集问题 start
	public static String toWmlIgnoreAnd(String src) {
		if (src == null)
			return src;

		if (src.indexOf("&#") == -1) {
			src = src.replace("&", "&amp;");
		}
		src = src.replace("$", "$$");
		// src = src.replace("\"", "&quot;");
		src = src.replace("<", "&lt;");
		src = src.replace(">", "&gt;");
		src = src.replace("\r\n", "<br/>");
		src = src.replace("\n", "<br/>");
		src = src.replace("", "");
		src = src.replace("\"", "“");

		// src = src.replace("'","&apos;");
		// src = src.replace("\"","&quot;");
		// src = src.replace("$","$$");
		// src = src.replace(" ","&nbsp;");
		// String result = src;
		return src;

	}
	
	// 替换类似&#nnnnn;和&#x83B7;等格式
	protected static int pGroup = 1;
	protected static Pattern pattern = Pattern.compile("&#([0-9]{3,5}|x[0-9A-F]{3,4});");
	public static String wapUnicode(String content) {
		if(content == null)
			return null;
        Matcher m = pattern.matcher(content);
        StringBuilder sb = new StringBuilder(content.length());
        int pos = 0;
        String c = null;
        while (m.find()) {
            sb.append(content.substring(pos, m.start(0)));
            c = m.group(pGroup);
            int c2 = 0;
            if(c.startsWith("x")) {
            	c2 = hexToInt(c.substring(1));
            } else {
            	c2 = toInt(c);
            }
//          if(c2 > 256)
            	sb.append((char)c2);
            pos = m.end(0);
        }
        sb.append(content.substring(pos));
        return ForbidUtil.replaceBanWord(sb.toString());
	}
	
	public static String toSql(String src) {
		src = src.replace("\\", "\\\\");
		src = src.replace("'", "\\'");
		return src;
	}
	// 用于 like '??'
	public static String toSqlLike(String src) {
		src = src.replace("\\", "\\\\");
		src = src.replace("'", "\\'");
		src = src.replace("%", "\\%");
		src = src.replace("_", "\\_");
		return src;
	}

	// liuyi 2007-01-17 部分用户字符集问题 end

	public static String getUnique() {
		return String.valueOf( ++unique );
	}

	public static String getUniqueStr() {
		if(unique > 90)
			unique = 0;
		return String.valueOf( ++unique ) + RandomUtil.nextInt(10);
	}
	
	public static String getUniqueParam() {
		return "u=" + getUniqueStr();
	}

	public static String date2String(ResultSet rs, String col) {
		try {
			return rs.getString(col);
		} catch (Exception e) {
		}
		return "1900-01-01";
	}

	public static String datetime2String(ResultSet rs, String col) {
		try {
			return rs.getString(col);
		} catch (Exception e) {
		}
		return "1900-01-01 00:00:00";
	}

	/**
	 * 
	 * @author macq
	 * @explain： 数字转换
	 * @datetime:2007-5-10 16:55:18
	 * @param number
	 * @return
	 * @return String
	 */
	public static String numberFormat(float number) {
		return numFormat.format(number);
	}
	
	public static String bigNumberFormat(long number) {
		if(number <= -100000l) {
			if(number > -1000000000l)
				return String.valueOf(number / 10000l) + "万";
			else
				return String.valueOf(number / 100000000l) + "亿";
		} else if(number < 100000l)
			return String.valueOf(number);
		if(number < 1000000000l)
			return String.valueOf(number / 10000l) + "万";
		return String.valueOf(number / 100000000l) + "亿";
	}
	// 就算有10亿也显示万
	public static String bigNumberFormat2(long number) {
		if(number <= -100000l) {
			if(number > -1000000000l)
				return String.valueOf(number / 10000l) + "万";
			else
				return String.valueOf(number / 100000000l) + "亿";
		} else if(number < 100000l)
			return String.valueOf(number);
		if(number < 100000000l)
			return String.valueOf(number / 10000l) + "万";
		long left = (number % 100000000l) / 10000;
		if(left > 0)
			return (number / 100000000l) + "亿" + left + "万";
		return String.valueOf(number / 100000000l) + "亿";
	}
	
	public static int getCLength(char c) {
		if(isAsc(c))
			return 1;
		else
			return 2;
	}
	
	public static String limitString(String s, int limit) {
		char[] cs = s.toCharArray();
		int sum = 0;
		for(int i = 0;i < cs.length;i++) {
			sum += getCLength(cs[i]);
			if(sum > limit) {
				int cut = sum + 1 - limit - getCLength(cs[i]);
				while(cut > 0) {
					i--;
					cut -= getCLength(cs[i]);
				}
				return s.substring(0, i) + "…";
			}
		}
		
		return s;
	}

	public static String itemTimeString(int time) {
		if(time >= 100)
			return "??";
		else
			return String.valueOf(time);
	}

	public static int toId(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static String getLineWml(JaLineBean line, HttpServletRequest request,
            HttpServletResponse response) {

        if(line.getDisplayType() == JaLineBean.DT_NULL_FUNC_NULL) {
        	String[] content = line.getCenterWap10().split("&&");
        	String ret = "";
        	for(int i = 0;i < content.length;i++) {
        		content[i] = CacheManage.callMethod.call(content[i], new CallParam(request, response, line.getId()));
        		if(content[i] != null)
        			ret += content[i];
        	}
        	if(ret.length() > 0)
        		ret += line.getLineEnd();
        	return ret;
        }
        
        String content = line.getWap10Content();
        String linkAddress = line.getLinkURL();
        
        if (line.getLinkURL().equals("")) {
            return content + line.getLineEnd();
        }

        return "<a href=\"" + linkAddress + "\">" + content
                + "</a>" + line.getLineEnd();
    }
	
	public static String outputObject(Object obj) throws IllegalArgumentException,
			IllegalAccessException {
		StringBuilder sb = new StringBuilder(128);
		if (obj != null) {
			Class c = obj.getClass();
			Field[] fs = c.getDeclaredFields();
			sb.append("<font color=green>");
			sb.append(obj.toString());
			sb.append("</font>--");
			sb.append(c.getName());
			sb.append("<br/>");

			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				int m = f.getModifiers();
				if (Modifier.isStatic(m))
					continue;
				f.setAccessible(true);
				sb.append(f.getName());
				sb.append("&nbsp;=&nbsp;<font color=red>");
				sb.append(f.get(obj));
				sb.append("</font>&nbsp;[");
				sb.append(f.getType().getSimpleName());
				sb.append("]<br/>");
			}
			if (obj instanceof Collection) {
				sb.append("<br/>");
				Collection cl = (Collection) obj;
				int limit = 20;
				Iterator iter = cl.iterator();
				while (iter.hasNext() && --limit > 0) {
					sb.append(outputObject(iter.next()));
				}
			}
			if(obj instanceof Object[]) {
				Object[] objs = (Object[])obj;
				sb.append("<br/>");
				for(int i = 0;i < 20 && i < objs.length;i++) {
					sb.append(outputObject(objs[i]));
				}
			}
		}
		return sb.toString();
	}
	
	public static String intsToString(List list) {
		if(list == null)
			return null;

		StringBuilder sb = new StringBuilder();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			int[] ii = (int[])it.next();
			sb.append(ii[0]);
			for(int i = 1;i < ii.length;i++) {
				sb.append("-");
				sb.append(ii[i]);
			}
			if(it.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
