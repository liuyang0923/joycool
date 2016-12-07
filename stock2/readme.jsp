<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<img src="../img/stock2/readme.gif" alt="loading"/><br/>
<%=BaseAction.getTop(request, response)%>
[必读]<a href="readme.jsp">详细游戏指南</a>:123123<br/>
[必读]<a href="readme.jsp">详细游戏指南</a>:123123<br/>
[必读]<a href="readme.jsp">详细游戏指南</a>:123123<br/>
[必读]<a href="readme.jsp">详细游戏指南</a>:123123<br/>
[必读]<a href="readme.jsp">详细游戏指南</a>:123123<br/>
[必读]<a href="readme.jsp">详细游戏指南</a>:123123<br/>
[必读]<a href="readme.jsp">详细游戏指南</a>:123123<br/>
<br/>
<a href="index.jsp">返回游戏首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>