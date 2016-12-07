<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%
	AdminAction.clearGroup();
	session.setAttribute("promptMsg", "缓存已成功清除");
	response.sendRedirect("index.jsp");
	return;
%>