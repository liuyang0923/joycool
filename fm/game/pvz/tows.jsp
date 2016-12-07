<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.pvz.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
long startTime = vsGame.getStartTime();
 %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (vsGame.getState() == vsGame.gameInit) {
%>本场比赛将开赛于<%=DateUtil.sformatTime(startTime)%><br/><%
} else {
	%>开赛于<%=DateUtil.sformatTime(startTime)%>,<%
	if (vsGame.getState() == vsGame.gameEnd) {
	%>总用时<%=GameAction.getFormatDifferTime(vsGame.getEndTime() - startTime)%><%
	} else {
	%>已用时<%=GameAction.getFormatDifferTime(System.currentTimeMillis() - startTime)%><%
	}
	%><br/><%
}
if (vsGame.getPlantSide() == 0) {
%>植物方：<a href="/fm/myfamily.jsp?id=<%=vsGame.getFmIdA()%>"><%=vsGame.getFmANameWml()%>家族</a><br/>参赛人数：<%=vsGame.getFmCountA()%>人<br/>
僵尸方：<a href="/fm/myfamily.jsp?id=<%=vsGame.getFmIdB()%>"><%=vsGame.getFmBNameWml()%>家族</a><br/>参赛人数：<%=vsGame.getFmCountB()%>人<br/><%
} else {
%>植物方：<a href="/fm/myfamily.jsp?id=<%=vsGame.getFmIdB()%>"><%=vsGame.getFmBNameWml()%>家族</a><br/>参赛人数：<%=vsGame.getFmCountB()%>人<br/>
僵尸方：<a href="/fm/myfamily.jsp?id=<%=vsGame.getFmIdA()%>"><%=vsGame.getFmANameWml()%>家族</a><br/>参赛人数：<%=vsGame.getFmCountA()%>人<br/><%
}
%><a href="view.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>