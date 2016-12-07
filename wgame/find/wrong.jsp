<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="一起找乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
一起找乐酷<br/>
-------------------<br/>
恭喜您被忽悠了，没有正确访问到乐酷社区，所以奖励泡汤了。<br/>
注意：访问乐酷的正确链接可能会很不起眼，同时移动会用一些含糊的、诱惑的语言来欺骗大家，要好好分辨哦！<br/>
<a href="play.jsp">再来一次</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>