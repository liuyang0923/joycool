<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
技能介绍<br/>
主动技能(外功):在战斗中需要气力使用的技能，对攻击力提供加成。每次使用能获得技能经验，技能经验每累计100技能就能升级，提供更高的加成。每种外功的加成作用是不一样的，而每个角色能够修习的技能数有限，如果技能数不够之后又想修习别的技能，可以删除以前的技能。<br/>
被动技能(内功):在战斗中不需要使用自行发挥作用的技能，对各种属性都有加成。角色一共能够学习３种内功，但是每次只能修炼一种，战斗中被选择修炼的技能就会获得技能经验，技能升级之后加成也会提高。注意：一定要记得去被动技能里选择一个当前修炼的技能，才能获得技能经验。被动技能同样能删除重修。<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>