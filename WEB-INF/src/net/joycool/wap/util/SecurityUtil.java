/*
 * Created on 2005-12-14
 *
 */
package net.joycool.wap.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.BlackListUserBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;

/**
 * @author lbj
 *  
 */
public class SecurityUtil {
    public static int getMobile = 0;	// 0不取，1取ltone，2取新的接口
    
    public static int step = 1; //教育用户登陆：1:步骤1（取手机号，自动注册，自动登陆）；
                                //           2:步骤2（取手机号，自动注册，不自动登陆）；
                                //           3:步骤3（取手机号，自动注册，不自动登陆）；
                                //           4:步骤4（不取手机号，不自动注册，不自动登陆）；

    public static List forbidIpList;

    public static List forbidUaList;
    
    public static HashSet cmwapIpSet = new HashSet();
    
    public static HashSet permitIpSet;	// 确定是手机的ip
    public static HashSet denyIpSet;	// 确定不是手机的ip
    public static List denyIpsList;
    
    public static Pattern innerIp = Pattern.compile("124\\.207\\.34\\.1[34].|192\\.168\\..*|127\\.0\\.0\\.1|61\\.177\\.140\\.130");		// 允许op访问的ip
    
    public static HashMap forbidUserIdMap;

    public static void main(String[] args) {
        Pattern p = null;
        Matcher m = null;

        Iterator itr = getForbidNicks().iterator();
        while (itr.hasNext()) {
            p = Pattern.compile((String) itr.next());
            m = p.matcher("毛泽东毛泽东毛泽东毛泽东毛泽东毛泽东");
            if(m.find()){                
                System.out.println(m.group());
            }
        }        
    }

    public synchronized static void init() {
    	if(forbidIpList != null)
    		return;

        forbidUaList = new ArrayList();
        try {
        	permitIpSet = new HashSet();
	        List tmp = SqlUtil.getObjectList("select ip from ip_group where `group`='permit'", 0);
	        for(int i = 0;i < tmp.size();i++) {
	        	String s = (String)tmp.get(i);
	        	IP ip = new IP(s);
	        	permitIpSet.add(new Long(ip.getIp()));
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        try {
        	denyIpSet = new HashSet();
	        List tmp = SqlUtil.getObjectList("select ip from ip_group where `group`='deny'", 0);
	        for(int i = 0;i < tmp.size();i++) {
	        	String s = (String)tmp.get(i);
	        	IP ip = new IP(s);
	        	denyIpSet.add(new Long(ip.getIp()));
	        }
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
        try {
        	denyIpsList = new ArrayList();
	        List tmp = SqlUtil.getObjectList("select ip from ip_group where `group`='denys'", 0);
	        for(int i = 0;i < tmp.size();i++) {
	        	String s = (String)tmp.get(i);
	        	IP ip = new IP(s);
	        	denyIpsList.add(ip);
	        }
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
        try {
        	mobileIp = new ArrayList();
	        List tmp = SqlUtil.getObjectList("select ip from ip_group where `group`='mobile'", 0);
	        for(int i = 0;i < tmp.size();i++) {
	        	String s = (String)tmp.get(i);
	        	IP ip = new IP(s);
	        	mobileIp.add(ip);
	        }
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
        try {
        	forbidIpList = new ArrayList();
	        List tmp = SqlUtil.getObjectList("select ip from ip_group where `group`='forbid'", 0);
	        for(int i = 0;i < tmp.size();i++) {
	        	String s = (String)tmp.get(i);
	        	IP ip = new IP(s);
	        	forbidIpList.add(ip);
	        }
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
        //        BufferedReader br = new BufferedReader(new InputStreamReader(
        //                SecurityUtil.class.getResourceAsStream("/config/forbidIP.txt")));
        //        try {
        //            String ip = br.readLine();
        //            while (ip != null) {
        //                //System.out.println(ip);
        //                forbidIpList.add(ip);
        //                ip = br.readLine();
        //            }
        //            br.close();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //        try {
        //            URL url = new
        // URL("/jcadmin/forbid.jsp?clear=1");
        //            InputStream is = url.openStream();
        //            is.close();
        //        } catch (MalformedURLException e) {
        //            e.printStackTrace();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
	    
    	cmwapIpSet = new HashSet();
        try {
        	cmwapIpSet = new HashSet();
	        List tmp = SqlUtil.getObjectList("select ip from ip_group where `group`='cmwap'", 0);
	        for(int i = 0;i < tmp.size();i++) {
	        	String s = (String)tmp.get(i);
	        	IP ip = new IP(s);
	        	cmwapIpSet.add(new Long(ip.getIp()));
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    static Byte[] lock = new Byte[0];
    public static HashMap getForbidUserIdMap(){
    	if(forbidUserIdMap!=null){
    		return forbidUserIdMap;
    	}
    	synchronized(lock){
    		if(forbidUserIdMap!=null){
        		return forbidUserIdMap;
        	}
    		forbidUserIdMap= new HashMap();
    		IUserService userService= ServiceFactory.createUserService();
    		List userList = userService.getBlackListUserList(null);
    		Iterator it =userList.iterator();
    		while(it.hasNext()){
    			BlackListUserBean blackListUser=(BlackListUserBean)it.next();
    			forbidUserIdMap.put(new Integer(blackListUser.getUserId()),blackListUser);
    		}
    	}
    	return forbidUserIdMap;
    }
    
    public static boolean checkForbidUserId(int userId){
    	return getForbidUserIdMap().containsKey(new Integer(userId));
    }
    
	public static void flushForbidUserIdMap() {
		forbidUserIdMap=null;
	}

    public static boolean allowVisit(String ip) {
        if (forbidIpList == null) {
            init();
        }
        if (ipInForbidList(ip)) {
//            System.out.println("Kill ip: " + ip);
            return false;
        }
//        String mobile = (String) request.getSession()
//                .getAttribute("userMobile");
//        if (mobile == null) {
//            mobile = request.getParameter("cp");
//        }
//        if (mobile != null && mobile.startsWith("86")) {
//            mobile = mobile.substring(2);
//        }
//        if (uaInForbidList(mobile)) {
//            return false;
//        }
        return true;
    }

    public static boolean uaInForbidList(String ua) {
        if (ua == null) {
            return false;
        }
        int count = forbidUaList.size();
        for (int i = 0; i < count; i++) {
            if (ua.indexOf((String) forbidUaList.get(i)) != -1) {
                return true;
            }
        }
        return false;
    }

    public static boolean ipInForbidList(String ip) {
        if (ip == null) {
            return true;
        }
        int count = forbidIpList.size();
        long ipl = IP.ipToLong(ip);
        for (int i = 0; i < count; i++) {
            if (((IP) forbidIpList.get(i)).isInScope(ipl)) {
                return true;
            }
        }
        return false;
    }

    private static String ismobileString = "is_mobile";
    public static boolean isMobile(HttpServletRequest request) {
    	HttpSession session = request.getSession(false);
    	Integer is = null;
    	if(session != null)
    		is = (Integer)session.getAttribute(ismobileString);
    	if(is != null) 
    		return is.intValue() == 1;
	
    	boolean isMobile = isMobileFull(request);
    	if(isMobile)
    		is = Integer.valueOf(1);
    	else
    		is = Integer.valueOf(0);
    	if(session != null)
    		session.setAttribute(ismobileString, is);
    	return isMobile;
    }
    
    public static List mobileIp = new ArrayList();
    public static String[] mobileIPString = {
    	"211.103.0.0/17",
    	"211.136.0.0/13",
    	"218.200.0.0/13",
    	"221.130.0.0/15",
    	"221.176.0.0/13",
    	"211.96.28.105/15",
    	"211.90.241.6/13",
    	"125.91.4.128/26",
    	"125.91.5.43/27",
    	"61.141.5.22/26",
    	"125.91.253.18/24",
    	"67.228.166.99/29",
    	"67.228.174.48/28",
    	"67.228.174.64/28",
    	"218.30.107.226/27",
    	"218.30.110.82/28",
    	"218.30.110.162/27",
    	"203.81.19.12/24",
    	"123.150.187.83/24",
    	"113.107.72.0/24",
//    	"211.96.28.105",
//    	"211.157.107.128/27",
//    	"211.90.241.178/24",
//    	"220.193.98.245/24",
//    	"211.96.28.0/24",
//    	"211.90.119.0/24",
//    	"211.97.65.0/24",
//    	"220.202.97.0/24",
//    	"220.201.8.217/24",
//    	"211.94.164.215/26",	// 北京联通
//    	"61.242.223.0/28",		// 河南省郑州市 联通
//    	"220.196.52.0/23",	// 上海市联通
//    	"61.240.7.32/28",	// 天津联通
//    	"119.62.128.36/24",	// 云南网通
    	"117.128.0.0/10",	// cmnet
    	"120.192.0.0/10", 
    	"112.0.0.0/10", 
		"111.0.0.0/10",
    };
//    static {
//    	for(int i = 0;i < mobileIPString.length;i++)
//    		mobileIp.add(new IP(mobileIPString[i]));
//    }
    public static boolean isMobileIp(String ip) {
    	long ipl = IP.ipToLong(ip);
    	if(permitIpSet.contains(new Long(ipl)))
    		return true;
		for(int i = 0;i < mobileIp.size();i++) {
			IP m = (IP)mobileIp.get(i);
			if(m.isInScope(ipl))
				return true;
		}
		return false;
    }
    public static boolean isMobileFull(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if(ip != null) {
			if(isMobileIp(ip))
				return true;
			
		}
    	String forwardFor = request.getHeader("x-forwarded-for");
		String via = request.getHeader("via");
		String bearerType = request.getHeader("x-up-bearer-type");
		if (forwardFor != null || (via != null && via.toLowerCase().indexOf("wap") != -1)
				|| (bearerType != null && bearerType.toLowerCase().indexOf(
                "gprs") != -1)) { 
			return true;
		}
		return false;
    }

    /**
     * 监测昵称。
     * 
     * @param nickName
     * @return
     */
    public static boolean checkNick(String nickName) {
        Pattern p = null;
        Matcher m = null;

        Iterator itr = getForbidNicks().iterator();
        while (itr.hasNext()) {
            p = Pattern.compile((String) itr.next());
            m = p.matcher(nickName);
            if(m.find()){                
                return false;
            }
        }
        return true;
    }

    public static Vector forbidNicks = null;

    public static Vector getForbidNicks() {
        if (forbidNicks == null) {
            forbidNicks = new Vector();
            forbidNicks.add("乐[^酷]*酷");
            forbidNicks.add("管[^理]*理[^员]*员");
            forbidNicks.add("通[^吃]*吃[^岛]*岛");
            forbidNicks.add("毛[^泽]*泽[^东]*东");
            forbidNicks.add("邓[^小]*小[^平]*平");
            forbidNicks.add("江[^泽]*泽[^民]*民");
            forbidNicks.add("胡[^锦]*锦[^涛]*涛");
            forbidNicks.add("张[^小]*小[^玮]*玮");
            forbidNicks.add("退.*网");
            forbidNicks.add("系.*统");
            forbidNicks.add("信.*息");
            forbidNicks.add("[0-9.'`]");
        }
        return forbidNicks;
    }    
    
    public static void redirectGetMobile(HttpServletResponse response, String url) throws IOException {
		String backUrl = (url);
		backUrl = URLEncoder.encode(backUrl, "utf-8");
		if(getMobile == 1)
			backUrl = "http://wap.linktone.com:18000/ua/ub.ph?AN=joycool&URL=" + backUrl;
		else
			backUrl = "http://211.136.107.36/mnf/m-11534.fet?url=" + backUrl;
		response.sendRedirect(backUrl);
    }
    
	public static boolean oxcxMobile(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("oxcx") != null)
			return false;

		session.setAttribute("oxcx", "true");
		return needMobile(request);
	}
	
	public static String getUA(HttpServletRequest request) {
		if(getMobile == 1)
			return request.getParameter("UA");
		else
			return "";
	}

	public static String getPhone(HttpServletRequest request) {
		if(getMobile == 1)
			return request.getParameter("cp");
		else
			return request.getParameter("_mn_");
	}
	
	public static boolean needMobile(HttpServletRequest request) {
		if(getMobile == 0)
			return false;
		return getPhone(request) == null;
	}
	
	static String wap11 = "wap11";
	static String wap20 = "wap20";
	static String acMark = "acMark";
	public static boolean isWap20(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null)
			return false;
		String mark = (String)session.getAttribute(acMark);
		if(mark != null)
			return mark == wap20;
	
		String accept = request.getHeader("accept");
		if(accept != null && accept.indexOf("xhtml") != -1) {
			session.setAttribute(acMark, wap20);
			return true;
		}
		session.setAttribute(acMark, wap11);
		return false;
	}
	
	public static boolean isInnerIp(String ip) {
		return innerIp.matcher(ip).matches();
	}

	public static String getSessionId(HttpSession session) {
		String sessionId = (String)session.getAttribute("oldsid");
		if(sessionId == null)
			return session.getId();
		return session.getId() + "|" + sessionId;
	}
    public static HttpSession newSession(HttpServletRequest request) {
    	HttpSession session = request.getSession(false);
    	if(session != null) {
    		String sessionId = (String)session.getAttribute("oldsid");
    		if(sessionId == null)
    			sessionId = session.getId();
    		session.invalidate();
    		session = request.getSession();
    		session.setAttribute("oldsid", sessionId);
    	} else {
    		session = request.getSession();
    	}
    	return session;
    }
    
    public static int[] adminId = {
    	431, 519610, 914727, 100, 
    };
    public static boolean isAdmin(int userId) {
    	for(int i = 0;i < adminId.length;i++) {
    		if(userId == adminId[i])
    			return true;
    	}
    	return false;
    }
        
    // 之前的功能废弃，现在用于保存的是代理的ip
    public static boolean isCmwap(String ip) {
    	long ipl = IP.ipToLong(ip);
    	if(cmwapIpSet.contains(new Long(ipl)))
    		return true;
    	return false;
    }
    
	public static HashSet forbidPwd = new HashSet();	// 禁止的密码，过于简单
	public static HashSet forbidEncodedPwd = new HashSet();	// encoded 密码过于简单
	static {
		forbidPwd.add("1234");
		forbidPwd.add("12345");
		forbidPwd.add("123456");
		forbidPwd.add("1234567");
		forbidPwd.add("12345678");
		forbidPwd.add("123456789");
		forbidPwd.add("000000");
		forbidPwd.add("111111");
		forbidPwd.add("222222");
		forbidPwd.add("333333");
		forbidPwd.add("444444");
		forbidPwd.add("555555");
		forbidPwd.add("666666");
		forbidPwd.add("777777");
		forbidPwd.add("888888");
		forbidPwd.add("999999");
		forbidPwd.add("0000");
		forbidPwd.add("1111");
		forbidPwd.add("2222");
		forbidPwd.add("3333");
		forbidPwd.add("4444");
		forbidPwd.add("5555");
		forbidPwd.add("6666");
		forbidPwd.add("7777");
		forbidPwd.add("8888");
		forbidPwd.add("9999");
		forbidPwd.add("4321");
		forbidPwd.add("54321");
		forbidPwd.add("654321");
		Iterator iter = forbidPwd.iterator();
		while(iter.hasNext())
			forbidEncodedPwd.add(net.joycool.wap.util.Encoder.encrypt(iter.next().toString()));
	}

	public static boolean isPasswordSecure(String password) {
		return password.length() > 5 && !forbidPwd.contains(password);
	}
	
	public static boolean isEncodedPasswordSecure(String password) {
		return !forbidEncodedPwd.contains(password);
	}
}
