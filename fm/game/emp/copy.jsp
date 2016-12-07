<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsUser.isDeath() || !vsUser.isAhead()) {response.sendRedirect("war.jsp");return;} // 观众或者已经死亡的玩家无法攻击
int num = EmperorGameBean.outNum;
int endA = num;
int endB = num;
if(vsGame.getRound() == 0) {endA = 0; endB = 0;}
LinkedList listA = vsGame.getUserListA();
LinkedList listB = vsGame.getUserListB();
String fmNameA = vsGame.getFmANameWml();
String fmNameB = vsGame.getFmBNameWml();
if (vsUser.getSide() == 1) {
	listA = vsGame.getUserListB();
	listB = vsGame.getUserListA();
	fmNameA = vsGame.getFmBNameWml();
	fmNameB = vsGame.getFmANameWml();
}
if (action.setHitUser(vsGame,vsUser,listA,listB,true)) {response.sendRedirect("hit.jsp?c=1&w=2");return;}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
%>技能:<%=vsUser.getRole().getSkillName()%><br/>介绍:<%=vsUser.getRole().getSkillIntroduction()%><br/><%
%>请选择要操作的对象:<br/><%
%>防御:<%
if(listA.size() > num) {
	for (int i = num;i<listA.size();i++) {
		EmperorUserBean userA = (EmperorUserBean) listA.get(i);
		if (userA.isDeath()) {break;}
		%><%=userA.getRole().getName()%><%
		if (i != vsGame.getAliveNumA() - 1) {%>|<%}
	} 
	%><br/><%
} else {
	%>无<br/><%
}
%>先锋:<%
if (listA.size() > 0) {
	if (listA.size() < endA) {endA = listA.size();}
	for (int i=0;i<endA;i++) {
		EmperorUserBean userA = (EmperorUserBean) listA.get(i);
		if (userA.isDeath()) {break;}
		%><%=userA.getRole().getName()%><%
		if (i != endA - 1) {%>|<%} else {%><br/><%}
	}
} else {
	%>无<br/><%
}
%>友方:<%=fmNameA%>,<%=listA.size()%>人<br/><a href="war.jsp">取消</a><br/>敌方:<%=fmNameB%>,<%=listB.size()%>人<br/><%
%>先锋:<%
if (listB.size() > 0) {
	if (listB.size() < endB) {endB = listB.size();}
	for (int i=0;i<endB;i++) {
		EmperorUserBean userB = (EmperorUserBean) listB.get(i);
		if (userB.isDeath()) {break;}
		if(userB.getRole().getSkillType() < 3) {
		%><a href="copy.jsp?l=b&amp;i=<%=i%>"><%=userB.getRole().getName()%></a><%
		} else {
		%><%=userB.getRole().getName()%><%
		}
		if (i != endB - 1) {%>|<%} else {%><br/><%}
	}
} else {
	%>无<br/><%
}
%>防御:<%
if (listB.size() > num) {
	for (int i = num; i < listB.size(); i++) {
		EmperorUserBean userB = (EmperorUserBean) listB.get(i);
		if (userB.isDeath()) {break;}
		%><%=userB.getRole().getName()%><%
		if (i != vsGame.getAliveNumB() - 1) {%>|<%}
	}
	%><br/><%
} else {
	%>无<br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>