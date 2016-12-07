<%@ page import="net.joycool.wap.util.IP"%><%@ page import="net.joycool.wap.util.ForbidUtil"%><%@ page import="net.joycool.wap.util.SecurityUtil"%><%@ page import="java.util.*"%><%!
static String allowString="allowVisit";
static String NOT_ALLOW = "false";
static String ALLOW = "true";
static String IP_ADDRESS = "ipaddr";

public static boolean isForbidUser(net.joycool.wap.bean.UserBean loginUser){
if(loginUser==null) return true;
return net.joycool.wap.util.ForbidUtil.isForbid("game",loginUser.getId());
}
%><%
boolean notMobile;	// 用户用电脑上网…
String allowVisit = (String)session.getAttribute(allowString);
String ip=request.getRemoteAddr();
if(allowVisit!=null) {
String oldIp = (String)session.getAttribute(IP_ADDRESS);
	if(oldIp==null) {
		session.setAttribute(IP_ADDRESS, ip);
		allowVisit=null;
	} else {
		if(!ip.equals(oldIp))
			allowVisit=null;
	}
}
if(allowVisit==null){
net.joycool.wap.bean.UserBean loginUser = (net.joycool.wap.bean.UserBean) session.getAttribute(net.joycool.wap.util.Constants.LOGIN_USER_KEY);
Long ipl = new Long(net.joycool.wap.util.IP.ipToLong(ip));
if(loginUser!=null&&ForbidUtil.isForbid("op",loginUser.getId()) || (SecurityUtil.permitIpSet.contains(ipl)||SecurityUtil.isMobileIp(ip)&&!SecurityUtil.isCmwap(ip))&&!isForbidUser(loginUser)){
	allowVisit = ALLOW;
	session.setAttribute(allowString,ALLOW);
	notMobile = false;
} else {
	allowVisit = NOT_ALLOW;
	session.setAttribute(allowString,NOT_ALLOW);
	notMobile = true;
}
session.setAttribute(IP_ADDRESS, ip);
} else {
notMobile = allowVisit.equals(NOT_ALLOW);
}
%>