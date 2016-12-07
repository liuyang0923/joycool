<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="javax.servlet.http.Cookie"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.encoder.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");

Cookie cookie = new Cookie("jcal", "");	// joycool auto login cookie
cookie.setMaxAge(0);
cookie.setPath("/");
response.addCookie(cookie);
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷社区">
<p align="left">
自动登陆信息已清除.<br/>
<a href="/user/login.jsp">返回重新登陆</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>