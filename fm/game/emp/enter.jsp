<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.emperor.*,jc.family.*"%><%
EmperorAction action=new EmperorAction(request,response);
%><%@include file="../vs/enterinc.jsp"%><%
if(true){
	response.sendRedirect("choro.jsp");
	return;
}
%>