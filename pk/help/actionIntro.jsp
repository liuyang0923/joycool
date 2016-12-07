<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
战斗介绍<br/>
战斗时需要先选择怪物或其他人，然后选择要使用的主动技能，就可以攻击目标了。如果选择休息一会，可以恢复一定的气力。<br/>
战斗中可以选择我的行囊吃药补充体力、气力、更换装备。<br/>
由于游戏是实时互动的，所以经常有许多人在攻击同一个目标，目标掉落的物品也是大家可以拾取，所以一定要手疾眼快哦！为了减少网络差异带来的不平衡，战斗限制了３秒之内只能攻击一次目标。<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>