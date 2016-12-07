<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
GoBangAction action = new GoBangAction(request);
action.denyInvitation(request);
response.sendRedirect(("/wgamehall/gobang/index.jsp"));
%>