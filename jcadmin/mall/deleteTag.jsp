<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.mall.*"%><%
MallAdminAction action = new MallAdminAction();
action.deleteTag(request, response);

response.sendRedirect("tagList.jsp");
%>