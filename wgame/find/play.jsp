<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("findjoycool", "");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="一起找乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
一起找乐酷<br/>
-------------------<br/>
要用什么方式登录乐酷？<br/>
wap.joycool.net
<a href="connect.jsp?mode=1">连接</a><br/>
<a href="connect.jsp?mode=2">使用乐酷书签</a><br/>
<a href="index.jsp">退出网络</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>