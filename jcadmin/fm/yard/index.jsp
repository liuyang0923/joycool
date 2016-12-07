<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 20;	// 一页10个好友
%><%
response.setHeader("Cache-Control","no-cache");
FamilyAction action=new FamilyAction(request,response);
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家族后台</title>
	<link href="../common.css" rel="stylesheet" type="text/css">
  </head>
  <body><p>
	<a href="itemproto.jsp">物品</a><br/>
	<a href="recipeproto.jsp">菜谱</a><br/><br/>
	<a href="itemproto2.jsp">种子分析</a><br/>
	<a href="itemproto3.jsp">食材分析</a><br/>
	<a href="recipeproto2.jsp">菜谱分析</a><br/>
	<a href="recipeproto3.jsp">中级步骤</a><br/>
	</p>
  </body>
</html>