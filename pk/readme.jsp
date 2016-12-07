<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<img src="../img/pk/help.gif" alt="loading"/><br/>
<%=BaseAction.getTop(request, response)%>
详细游戏指南<br/>
游戏中每人都有自己的角色，通过运用各种游戏技巧打怪做任务获得经验、练习武功、获得各种武器装备提升实力，从而挑战更强的敌人，获得更强的装备。游戏中有固定时间可以进行人与人之间的对战，在游戏里了结个人与帮会之间的恩怨吧！<br/>
<a href="/pk/help/placeIntro.jsp">地点介绍</a>
<a href="/pk/help/roleIntro.jsp">个人属性</a><br/>
<a href="/pk/help/shopIntro.jsp">商店介绍</a>
<a href="/pk/help/skillIntro.jsp">武功介绍</a><br/>
<a href="/pk/help/equipIntro.jsp">装备介绍</a>
<a href="/pk/help/bagIntro.jsp">行囊介绍</a><br/>
<a href="/pk/help/missionIntro.jsp">任务介绍</a>
<a href="/pk/help/actionIntro.jsp">战斗介绍</a><br/>
<a href="/pk/help/homicideIntro.jsp">关于杀人</a><br/><br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>