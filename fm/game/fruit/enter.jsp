<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*"%><%
FruitAction action=new FruitAction(request,response);
%><%@include file="../vs/enterinc.jsp"%><%
session.setAttribute("chooseOG","1");
if(true){
	response.sendRedirect("game.jsp");
	return;
}
%>