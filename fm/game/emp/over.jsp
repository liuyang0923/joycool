<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
int result = vsGame.calcGameResult();
String resultA = "赢";
String resultB = "输";
if (result == 1) {
	resultA = "输";
	resultB = "赢";
} else if (result == 2) {
	resultA = "平";
	resultB = "平";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%>胜者为王败者寇!<br/><% 
if (vsUser != null) {
	%><%=vsUser.getRole().getName()%>|<%=vsUser.getNickNameWml()%>|贡献点:<%=vsUser.getContribute()%><br/><%
	%>所属家族:<%if(vsUser.getSide() == 0) {%><%=vsGame.getFmANameWml()%>[攻]<%} else {%><%=vsGame.getFmBNameWml()%>[守]<%}%><br/><%
}
%>家族:<%=vsGame.getFmANameWml()%>[攻]|<%=vsGame.getAliveNumA()%>/<%=vsGame.getUserListA().size()%>人|<%=resultA%><br/>
家族:<%=vsGame.getFmBNameWml()%>[守]|<%=vsGame.getAliveNumB()%>/<%=vsGame.getUserListB().size()%>人|<%=resultB%><br/>
<a href="hero.jsp">本场MVP榜</a><br/>
<a href="info.jsp">本场战斗信息</a><br/>
<a href="war.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>