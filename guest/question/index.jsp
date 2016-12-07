<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
//GoBangAction action = new GoBangAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="问答接龙">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/questions/gamequestion.gif" alt="问答接龙"/><br/>
<a href="playing.jsp">开始游戏</a><br/>
<a href="help.jsp">游戏规则</a><br/>
<a href="historyToday.jsp?topage=0">每日龙榜</a><br/>
<a href="historyTotal.jsp?topage=0">龙榜历史大排名</a><br/><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>