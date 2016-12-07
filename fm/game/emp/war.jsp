<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser != null && (vsUser.getRole() == null || vsGame.getState()==0)) {response.sendRedirect("choro.jsp");return;}// 未开赛或未选择角色不让进
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
int timeRemain = (int)(vsGame.getLastRoundTime() - System.currentTimeMillis())/1000 + 20;
if(timeRemain<0)timeRemain=0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="战场"><p align="left"><%=BaseAction.getTop(request, response)%>
<%
if(vsGame.getState()==2){%><a href="over.jsp">比赛已结束</a><br/><%} else if (vsUser != null && vsUser.getBlood() == 0) {%><a href="die.jsp">您已经阵亡</a><br/><%}%>
第<%=vsGame.getRound()%>回合<%if (vsGame.isStart()) {%>(剩余<%=timeRemain%>秒!)<%}%><br/>
<%if (vsUser != null) { 
	int skillType = vsUser.getRole().getSkillType();
	String skillTypeStr = "主动";
	if (skillType == 2) {skillTypeStr = "辅助";} else if (skillType == 3) {skillTypeStr = "被动";}
	%>所使用的角色:<%=vsUser.getRole().getName()%>,血<%=vsUser.getBlood()%><br/><%
	%><%=skillTypeStr%>技能:<%=vsUser.getRole().getSkillName()%><br/>介绍:<%=vsUser.getRole().getSkillIntroduction()%><br/><%
}%>
战斗信息:<a href="info.jsp">更多</a><br/>
<%
LinkedList infoList = vsGame.getInformationList();
if (infoList.size() > 0) {
	int end = infoList.size() < 3 ? infoList.size() : 3;
	for (int i=0;i<end;i++) {
		InfoBean fightBean = (InfoBean) infoList.get(i);
		%><%=i+1%>.<%=fightBean.getInfo()%><br/><%	
	}
} else {
	%>无<br/><%
}
%>
<a href="war.jsp">刷新</a>|战场|<a href="check.jsp">查看</a>|<a href="chat.jsp">聊天</a><br/>防御:
<%if(listA.size() > num) {
	for (int i = num;i<listA.size();i++) {
		EmperorUserBean userA = (EmperorUserBean) listA.get(i);
		if (userA.isDeath()) {break;}
		%><%=userA.getRole().getName()%>(<%=userA.getBlood()%><%
		if (userA.getHasVampire() > 0) {%>,噬<%}
		if (userA.getBePoision() > 0) {%>,毒<%}
		 %>)<%
		if (i != listA.size() - 1) {%>|<%}
	}
}%><br/>先锋:<%
if (listA.size() > 0) {
	if (listA.size() < endA) {endA = listA.size();}
	for (int i=0;i<endA;i++) {
		EmperorUserBean userA = (EmperorUserBean) listA.get(i);
		if (userA.isDeath()) {break;}
		%><%=userA.getRole().getName()%>(<%=userA.getBlood()%><%
		if (userA.getHasVampire() > 0) {%>,噬<%}
		if (userA.getBePoision() > 0) {%>,毒<%}%>)<%
		if (i != endA - 1) {%>|<%}
	}
}
%><br/><%
if (vsUser!= null) {%>友方:<%} else {%>攻方:<%}%><%=fmNameA%>,<%=aliveA%>/<%=listA.size()%>人<br/><%
if (vsUser != null) {
	if (vsGame.getState()!=2 && vsUser.isAhead() && !vsUser.isDeath()) {
		%><a href="hit.jsp?w=1">普攻</a>|<% if(vsUser.getRole().getSkillType() != 3) {%><%if (vsUser.getRole().getSkillId() == 10) {%><a href="copy.jsp?w=2">技能</a><%} else {%><a href="hit.jsp?w=2">技能</a><%} } else {%>技能<%}
	} else {
		%>普攻|技能<%
	}
	%>|下方敌军!<br/><%
	if (vsUser.getEffectUser() != null) {%>本回合已经下达指令<br/><%}
}
%>—————————<br/><%
if (vsUser!= null) {%>敌方:<%} else {%>守方:<%}%><%=fmNameB%>,<%=aliveB%>/<%=listB.size()%>人<br/><%
%>先锋:<%
if (listB.size() > 0) {
	if (listB.size() < endB) {endB = listB.size();}
	for (int i=0;i<endB;i++) {
		EmperorUserBean userB = (EmperorUserBean) listB.get(i);
		if (userB.isDeath()) {break;}
		%><%=userB.getRole().getName()%>(<%=userB.getBlood()%>
		<%
		if (userB.getHasVampire() > 0) {%>,噬<%}
		if (userB.getBePoision() > 0) {%>,毒<%}
		 %>)<%
		if (i != endB - 1) {%>|<%}
	}
}
%><br/>防御:<%
if (listB.size() > num) {
	for (int i = num; i < listB.size(); i++) {
		EmperorUserBean userB = (EmperorUserBean) listB.get(i);
		if (userB.isDeath()) {break;}
		%><%=userB.getRole().getName()%>(<%=userB.getBlood()%><%
		if (userB.getHasVampire() > 0) {%>,噬<%}
		if (userB.getBePoision() > 0) {%>,毒<%}
		 %>)<%
		if (i != listB.size() - 1) {%>|<%}
	}
}
%><br/>
<a href="war.jsp">刷新</a>|战场|<a href="check.jsp">查看</a>|<a href="chat.jsp">聊天</a><br/>
<%if(vsUser==null){%>
<%=vsGame.getChat().getChatString(0, 3)%><a href="chat2.jsp">更多聊天信息</a><br/>
<%}else{%>
聊天信息&gt;&gt;家族|<a href="chat2.jsp">公共</a><br/>
<%=vsGame.getChat(vsUser.getSide()).getChatString(0, 3)%>
<input name="fchat"  maxlength="100"/>
<anchor>发言<go href="chat.jsp" method="post"><postfield name="content" value="$fchat"/></go></anchor><br/>
<a href="chat.jsp">更多聊天信息</a><br/>
<%}%>
<a href="/fm/index.jsp">&lt;&lt;返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>