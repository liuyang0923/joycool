<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*" %><%
response.setHeader("Cache-Control","no-cache");
GuessAction action = new GuessAction(request);
action.index();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=GuessAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
<a href="guess.jsp">进入游戏</a><br/>
每次游戏系统随机给你一个四位数字，这四位数中没有重复数字（0-9都只出现一次），你来猜这个四位数字是什么。如果数字和位置都对得一个A，数字对位置不对得一个B。在10次机会内猜对的有乐币和经验的奖励，猜对用的次数越少奖励就越多。<br/>
<a href="help.jsp">查看详细游戏规则</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>