<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
FootballAction action = new FootballAction(request);
action.cancelInvitation(request);
//response.sendRedirect(("/wgamehall/football/index.jsp"));
BaseAction.sendRedirect("/wgamehall/football/index.jsp", response);
%>