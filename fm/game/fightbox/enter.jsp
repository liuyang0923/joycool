<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fightbox.*,jc.family.*"%><%
FightBoxAction action=new FightBoxAction(request,response);
%><%@include file="../vs/enterinc.jsp"%><%
if(true){
	response.sendRedirect("chweap.jsp");
	return;
}
%>