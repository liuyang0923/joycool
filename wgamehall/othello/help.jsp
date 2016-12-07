<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
OthelloAction action = new OthelloAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="黑白棋帮助">
<p align="center">黑白棋帮助</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
游戏规则：<br/>
把自己颜色的棋子放在棋盘的空格上，而当自己放下的棋子在横、竖、斜八个方向內有一个自己的棋子，则被夹在中间的全部翻转会成为自己的棋子。并且，只有在可以翻转棋子的地方才可以下子。<br/>
在黑白棋游戏中输赢的是乐币，赢加5000乐币，输不减乐币，对方超时则赢得500乐币。不管乐币多少都可以在游戏大厅中玩游戏。<br/>
<br/>
<a href="/wgamehall/othello/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>