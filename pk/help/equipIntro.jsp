<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
装备介绍<br/>
装备分武器和防具、佩饰三类。<br/>
武器：主要提升角色攻击力，也有别的属性提升。每个角色同时只能装备一件武器<br/>
防具：主要提升角色防御力，也有别的属性提升。防具可以在头、身、脚装备三件。<br/>
佩饰：不同的佩饰提升不同的角色属性，每个角色只能佩戴一件，选择不同角色的属性也会不同。<br/>
一般的装备在游戏里的商人处有出售，但是极品的装备只在游戏中的boss处掉落。每种装备商人都可以回收。<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>