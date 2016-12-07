<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
商人介绍<br/>
商人出售和回收游戏中的各种武器、装备、道具，并向你提供任务、接受你报告任务。<br/>
武器店：出售游戏中的各种武器<br/>
装备店：出售游戏中的防具装备<br/>
佩饰店：出售游戏中的各种佩饰<br/>
内功馆：出售游戏中的内功技能<br/>
外功馆：出售游戏中的外功技能<br/>
药品店：出售游戏中的各种药品道具<br/>
任务堂：接受任务和提交完成的任务<br/>
游戏中的任何物品都能在除了任务堂之外的商人处回收。<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>