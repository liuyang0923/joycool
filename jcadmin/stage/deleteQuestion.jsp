<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.stage.*"%><%
StageAdminAction action = new StageAdminAction();
action.deleteQuestion(request, response);

response.sendRedirect("questionList.jsp");
%>