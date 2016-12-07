<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.wc.*"%><%
response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
int questionId = Integer.parseInt(request.getParameter("questionId"));
int answerId = Integer.parseInt(request.getParameter("answerId"));
action.result(questionId, answerId);
//response.sendRedirect("index.jsp");
BaseAction.sendRedirect("/jcadmin/worldcup/index.jsp", response);
%>