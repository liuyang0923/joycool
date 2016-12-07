<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.flush.*"%><%
FlushAdminAction action = new FlushAdminAction();
action.deleteLink(request, response);

response.sendRedirect("linkList.jsp");
%>