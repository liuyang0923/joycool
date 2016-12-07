<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
GoBangAction action = new GoBangAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="五子棋帮助">
<p align="center">五子棋帮助</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
游戏规则:<br/>
连成五子即赢.无禁手.每局每人可以求和一次.<br/>
在五子棋游戏中输赢的是乐币,赢加5000乐币,输不减乐币,对方超时则赢得500乐币.不管乐币多少都可以在游戏大厅中玩游戏.<br/>
<br/>
<a href="/wgamehall/gobang/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>