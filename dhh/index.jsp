<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
//action.index();
//DhhUserBean dhhUser = action.getDhhUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
购买自己的帆船，在祖国沿海进行海上贸易，倒买倒卖的都是家乡的特产。向着大海出发吧！<br/>
<a href="play.jsp">马上进入游戏</a><br/>
<a href="help.jsp">查看详细游戏指南</a><br/>
切记，游戏可以在港口中保存，只有保存之后下线重新上线也可以继续上一次的进度。进入游戏之后你拥有一条小帆船,有钱了可以去造船厂更换别的船，不过你只能拥有一条船，种类不同，速度、装载量和水手数都不同。一个港口的货物价格根据买卖情况浮动,买的人多价格上涨,卖的人多价格下跌。船只航行需要消耗时间和水手补给，还有各种未知的事件在航行中等待着你!选择游戏结束时，如果海币超过10000，将按一定比例自动兑换成的乐币！<br/>
<a href="affirm.jsp">结束游戏重新开始</a><br/>
<a href="top.jsp">海商王排行榜</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>