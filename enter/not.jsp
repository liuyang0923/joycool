<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.HashMap"%><%@ page import="net.joycool.wap.util.*"%><%!
static HashMap proxy = new HashMap();
static{
proxy.put("192.168.0.143","乐酷代理");	// wapx.joycool.net
proxy.put("211.157.107.143","乐酷代理");	// wapx.joycool.net
proxy.put("61.164.108.156","乐酷代理");	// wapx.joycool.net
proxy.put("61.156.45.24","G世界");		// ggg.cn
proxy.put("98.126.5.178","3gtk");		// wap.3gtk.net
proxy.put("210.51.57.177","pctowap");
proxy.put("61.142.66.12","timewe");
proxy.put("112.65.244.80","好的wap");

proxy.put("58.56.110.73","159机客");	// w.159.com
proxy.put("58.56.110.72","159机客");	// w.159.com
proxy.put("58.56.110.66","159机客");	// w.159.com

proxy.put("222.92.117.22","儒豹");	// www.roboo.com

proxy.put("219.136.248.243","GoBrowser");
proxy.put("219.136.248.242","GoBrowser");
proxy.put("219.136.248.241","GoBrowser");
proxy.put("219.136.248.101","GoBrowser");
proxy.put("219.136.248.102","GoBrowser");

proxy.put("111.1.32.151","HTTP代理");
proxy.put("112.5.129.111","3G3B代理");
}
%><%
String ip = request.getRemoteAddr();
if(ip.startsWith("127.0.")) {
	session.setAttribute("allowVisit", "true");
	response.sendRedirect("/lswjs/index.jsp");
	return;
}
String info = (String)proxy.get(ip);
UserBean loginUser = (UserBean) session.getAttribute("loginUser");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐酷游戏社区"><p align="left">
<%if(loginUser!=null&&ForbidUtil.isForbid("game",loginUser.getId())){%>
你的账号被禁止访问该功能.<a href="/admin/query.jsp">查询</a><br/>
<%}else{%>
您的访问并非来自移动手机而被拒绝.<br/>
也可能由于您使用了代理访问或者中转访问.<br/>
<%if(info!=null){%>您现在访问使用的代理是[<%=info%>],请直接使用joycool.net访问并手动登陆保存新的书签.<%}else{%>如果有任何疑问(比如你的确是使用手机访问)请点击<a href="/enter/request.jsp?type=0">申请开通IP</a><%}%><br/>
如果是uc等浏览器,请在关闭web压缩中转等选项后重新登陆再试.<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%></p></card></wml>