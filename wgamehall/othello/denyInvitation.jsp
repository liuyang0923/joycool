<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
OthelloAction action = new OthelloAction(request);
action.denyInvitation(request);
response.sendRedirect(("/wgamehall/othello/index.jsp"));
%>