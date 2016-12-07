<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction fmUserAction=new FamilyAction(request,response);
//FamilyUserBean fmUser=fmUserAction.getFmUser();
%>
<wml>
<card title="解封成功" ontimer="game.jsp"><timer value="30" /><p align="left">
<% if(true){ %>
解禁成功.(3秒后自动返回)<br/>
<%}else{ %>
解禁失败,已解禁或超时.(3秒后自动返回)<br/>
<%} %>
<a href="game.jsp">直接返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>