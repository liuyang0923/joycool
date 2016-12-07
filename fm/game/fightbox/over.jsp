<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsGame.getState() != vsGame.gameEnd) {
	response.sendRedirect("check.jsp");
	return;
}
int result = vsGame.getGameResult();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (result == 2) {%>
双方经过数番鏖战,最终达成平手<br/>
<%} else {
if (result == 0) {%><%=vsGame.getFmANameWml()%><%} else {%><%=vsGame.getFmBNameWml()%><%}
%>家族全歼对手,获得本次家族挑战赛胜利!<br/>
<%}%>
<%if (vsUser != null) {%>
&gt;&gt;个人战斗统计：<br/>
击杀:<%=vsUser.getHitEnemyTime()%>人|伤害量:<%=vsUser.getHitEnemyBlood()%><br/>
误杀:<%=vsUser.getHitFriendTime()%>人|伤害量:<%=vsUser.getHitFriendBlood()%><br/>
<%
int countTotalFm = vsGame.getFmCountA();
int countFm = vsGame.getCountFmA();
if (vsUser.getSide() == 1) {
	countTotalFm = vsGame.getFmCountB();
	countFm = vsGame.getCountFmB();
}%>
&gt;&gt;本方统计：<br/>
参与人数：<%=countTotalFm%>人<br/>
击杀：<%=vsGame.getKilled(vsUser.getSide())%>人<br/>
阵亡：<%=countTotalFm - countFm%>人<br/>
<%} else {%>
战斗统计：<br/>
&gt;&gt;攻方-<%=vsGame.getFmANameWml()%>家族<br/>
参与人数：<%=vsGame.getFmCountA()%>人<br/>
击杀：<%=vsGame.getKilled(0)%>人<br/>
&gt;&gt;守方-<%=vsGame.getFmBNameWml()%>家族<br/>
参与人数：<%=vsGame.getFmCountB()%>人<br/>
击杀：<%=vsGame.getKilled(1)%>人<br/>
<%}%>
<a href="heros.jsp">英雄榜</a><br/>
<a href="check.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>