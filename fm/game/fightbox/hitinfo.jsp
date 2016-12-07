<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.*"%><%@ page import="jc.family.game.fightbox.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
action.opreMessage();
String[] showName = vsGame.weapons;
Object[][] show = vsGame.getShow(vsUser,false);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}%>
<a href="check.jsp">返回战场</a><br/>
<%
for (int i=0;i<show.length;i++) {
	for (int j=0;j<show[i].length;j++) {
		BoxShowBean boxShowBean = (BoxShowBean) show[i][j];
		int weap = vsGame.checkWeap(boxShowBean, vsUser);
		%><%=showName[weap]%><%
	}
%><br/><%	
}	
%>
<a href="check.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>