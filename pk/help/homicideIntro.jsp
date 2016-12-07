<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
关于杀人<br/>
为了在保护新手的同时增加游戏乐趣，系统每天会开放一定时间的人人战斗，在时间之内可以对其他角色进行攻击。大家在战斗中可以充分发挥战术能力，使用各种技能、道具，叫上好友和兄弟一起上吧！不久的将来就会有杀人王的活动，有丰厚的奖励等你拿！<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>