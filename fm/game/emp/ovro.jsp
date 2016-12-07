<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
//if (System.currentTimeMillis() > vsGame.getStartTime()){response.sendRedirect("war.jsp");return;} // 比赛开始,直接跳入
if (vsUser == null) {response.sendRedirect("war.jsp");return;}//观众可以直接进入
if (action.hasParam("cid")) {action.chooseRole(vsGame,vsUser,action.getParameterInt("cid"));}
if ("success".equals(request.getAttribute("result"))) {response.sendRedirect("seat.jsp");return;}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
%><a href="choro.jsp">重新选择角色</a><br/><%
%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>