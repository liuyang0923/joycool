<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsUser.isDeath() || !vsUser.isAhead() || vsGame.getState()==2) {response.sendRedirect("war.jsp");return;} // 观众或者已经死亡或者处于防御状态的玩家无法攻击
if (vsUser.getOperation() == 0 && !action.hasParam("w")) {response.sendRedirect("war.jsp");return;}// 无参数直接返回
if (action.hasParam("w")) {
	int way = action.getParameterInt("w");
	boolean copy = false;
	if (vsUser.getRole().getSkillId() == 10 && way == 2) {copy = true;}
	if (!action.checkAttack(vsGame,vsUser,way,copy)) {response.sendRedirect("war.jsp");return;} // 攻击参数不正确
}
int num = EmperorGameBean.outNum;
int endA = num;
int endB = num;
if(vsGame.getRound() == 0) {endA = 0; endB = 0;}
LinkedList listA = vsGame.getUserListA();
LinkedList listB = vsGame.getUserListB();
String fmNameA = vsGame.getFmANameWml();
String fmNameB = vsGame.getFmBNameWml();
int aliveA = vsGame.getAliveNumA();
int aliveB = vsGame.getAliveNumB();
if (vsUser.getSide() == 1) {
	listA = vsGame.getUserListB();
	listB = vsGame.getUserListA();
	fmNameA = vsGame.getFmBNameWml();
	fmNameB = vsGame.getFmANameWml();
}
if (action.setHitUser(vsGame,vsUser,listA,listB,false)) {response.sendRedirect("war.jsp");return;}
EmperorRoleBean role = vsUser.getRole();
int effectSide = role.getEffectSide();
int effectRange = role.getEffectRange();
int operation = vsUser.getOperation();
//if (vsUser.getCopySkillUser() != null) {role=vsUser.getCopySkillUser().getRole();}
if (action.hasParam("c")) {
	role=vsUser.getCopySkillUser().getRole();
	if (role == null) {response.sendRedirect("war.jsp");return;}
	effectSide = role.getEffectSide();
	effectRange = role.getEffectRange();
} else {
	if (vsUser.getRole().getSkillId() == 10 && operation != 1) {response.sendRedirect("war.jsp");return;}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%><% 
if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
if (vsUser.getOperation() != 1) {
	%>技能:<%=role.getSkillName()%><br/>介绍:<%=role.getSkillIntroduction()%><br/><%
}
%>请选择要操作的对象:<br/><%
%>防御:<%
if(listA.size() > num) {
	for (int i = num;i<listA.size();i++) {
		EmperorUserBean userA = (EmperorUserBean) listA.get(i);
		if (userA.isDeath()) {break;}
		if (operation != 1 && effectSide == 0 && effectRange == 0) {
		%><a href="hit.jsp?l=a&amp;i=<%=i%>"><%=userA.getRole().getName()%></a><%
		} else {
		%><%=userA.getRole().getName()%><%
		}
		if (i != listA.size() - 1) {%>|<%}
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
		if(operation != 1 && effectSide == 0) {
			%><a href="hit.jsp?l=a&amp;i=<%=i%>"><%=userA.getRole().getName()%></a><%
		} else {
		%><%=userA.getRole().getName()%><%
		}
		if (i != endA - 1) {%>|<%}
	}
	%><br/><%
} else {
	%>无<br/><%
}
%>友方:<%=fmNameA%>,<%=aliveA%>/<%=listA.size()%>人<br/><a href="war.jsp">取消</a><br/>敌方:<%=fmNameB%>,<%=aliveB%>/<%=listB.size()%>人<br/><%
%>先锋:<%
if (listB.size() > 0) {
	if (listB.size() < endB) {endB = listB.size();}
	for (int i=0;i<endB;i++) {
		EmperorUserBean userB = (EmperorUserBean) listB.get(i);
		if (userB.isDeath()) {break;}
		if(operation == 1 || effectSide == 1) {
		%><a href="hit.jsp?l=b&amp;i=<%=i%>"><%=userB.getRole().getName()%></a><%
		} else {
		%><%=userB.getRole().getName()%><%
		}
		if (i != endB - 1) {%>|<%}
	}
	%><br/><%
} else {
	%>无<br/><%
}
%>防御:<%
if (listB.size() > num) {
	for (int i = num; i < listB.size(); i++) {
		EmperorUserBean userB = (EmperorUserBean) listB.get(i);
		if (userB.isDeath()) {break;}
		if(operation == 1 || (effectSide == 1 && effectRange == 0)) {
		%><a href="hit.jsp?l=b&amp;i=<%=i%>"><%=userB.getRole().getName()%></a><%
		} else {
		%><%=userB.getRole().getName()%><%
		}
		if (i != listB.size() - 1) {%>|<%}
	}
	%><br/><%
} else {
	%>无<br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>