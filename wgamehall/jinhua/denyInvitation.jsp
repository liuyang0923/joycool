<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
action.denyInvitation(request);
//response.sendRedirect(("/wgamehall/jinhua/start.jsp"));
BaseAction.sendRedirect("/wgamehall/jinhua/start.jsp", response);
%>