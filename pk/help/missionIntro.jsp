<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
任务介绍<br/>
任务堂里的"乐酷老油条"能够给你提供不同的任务，完成任务一般是收集几个不同的任务物品。任务物品收集齐之后在他那里提交任务，就能获得不同的任务奖励，都是极品装备哦！<br/>
任务难度越，奖励也就越丰厚，提交任务之后记得去行囊查看。如果个人完成有困难，不妨叫上朋友和帮会兄弟一起上。<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>