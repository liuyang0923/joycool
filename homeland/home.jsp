<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*,net.joycool.wap.action.user.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%
	response.setHeader("Cache-Control","no-cache");
	
	response.sendRedirect("/home/home2.jsp?userId="+request.getParameter("uid"));
	%>