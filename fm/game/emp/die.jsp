<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || !vsUser.isDeath()) {response.sendRedirect("war.jsp");return;}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%>
<%=vsUser.getNickNameWml()%><br/>
使用角色:<%=vsUser.getRole().getName()%><br/>
贡献:<%=vsUser.getContribute()%>点<br/>
<a href="war.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>