<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsGame.getState()==0) {response.sendRedirect("choro.jsp");return;} // 未开赛不让进
int num = EmperorGameBean.outNum;
int endA = num;
int endB = num;
if(vsGame.getRound() == 0) {endA = 0; endB = 0; num=0;}
LinkedList listA = vsGame.getUserListA();
LinkedList listB = vsGame.getUserListB();
String fmNameA = vsGame.getFmANameWml();
String fmNameB = vsGame.getFmBNameWml();
int aliveA = vsGame.getAliveNumA();
int aliveB = vsGame.getAliveNumB();
if (vsUser != null && vsUser.getSide() == 1) {
	listA = vsGame.getUserListB();
	listB = vsGame.getUserListA();
	fmNameA = vsGame.getFmBNameWml();
	fmNameB = vsGame.getFmANameWml();
	aliveA = vsGame.getAliveNumB();
	aliveB = vsGame.getAliveNumA();
}
if (aliveA < endA) {endA = aliveA;}
if (aliveB < endB) {endB = aliveB;}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="查看"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (vsUser != null) { 
	%>所使用的角色:<%=vsUser.getRole().getName()%>,血<%=vsUser.getBlood()%><br/><%
}
%><a href="check.jsp">刷新</a>|<a href="war.jsp">战场</a>|查看|<a href="chat.jsp">聊天</a><br/><%
%>死亡:<%
for (int i = aliveA; i < listA.size();i++) {
	EmperorUserBean userA = (EmperorUserBean) listA.get(i);
	%><a href="hro.jsp?uid=<%=userA.getUserId()%>"><%=userA.getRole().getName()%></a><%
	if (i != listA.size() - 1) {%>|<%}
}
%><br/><%
%>防御:<%
for (int i = endA;i<aliveA;i++) {
	EmperorUserBean userA = (EmperorUserBean) listA.get(i);
	%><a href="hro.jsp?uid=<%=userA.getUserId()%>"><%=userA.getRole().getName()%></a><%
	if (i != aliveA - 1) {%>|<%}
}
%><br/><%
%>先锋:<%
if (listA.size() > 0) {
	for (int i=0;i<endA;i++) {
		EmperorUserBean userA = (EmperorUserBean) listA.get(i);
		%><a href="hro.jsp?uid=<%=userA.getUserId()%>"><%=userA.getRole().getName()%></a><%
		if (i != endA - 1) {%>|<%}
	}
}
%><br/><%
if (vsUser!= null) {%>友方:<%} else {%>攻方:<%}%><%=fmNameA%>,<%=aliveA%>/<%=listA.size()%>人<br/>—————————<br/><%
if (vsUser!= null) {%>敌方:<%} else {%>守方:<%}%><%=fmNameB%>,<%=aliveB%>/<%=listB.size()%>人<br/><%
%>先锋:<%
for (int i=0;i<endB;i++) {
	EmperorUserBean userB = (EmperorUserBean) listB.get(i);
	%><a href="hro.jsp?uid=<%=userB.getUserId()%>"><%=userB.getRole().getName()%></a><%
	if (i != endB - 1) {%>|<%}
}
%><br/><%
%>防御:<%
for (int i = endB; i < aliveB; i++) {
	EmperorUserBean userB = (EmperorUserBean) listB.get(i);
	%><a href="hro.jsp?uid=<%=userB.getUserId()%>"><%=userB.getRole().getName()%></a><%
	if (i != aliveB - 1) {%>|<%}
}
%><br/><%
%>死亡:<%
for (int i = aliveB; i < listB.size();i++) {
	EmperorUserBean userB = (EmperorUserBean) listB.get(i);
	%><a href="hro.jsp?uid=<%=userB.getUserId()%>"><%=userB.getRole().getName()%></a><%
	if (i != listB.size() - 1) {%>|<%}
}
%><br/><%
%><a href="check.jsp">刷新</a>|<a href="war.jsp">战场</a>|查看|<a href="chat.jsp">聊天</a><br/><%
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>