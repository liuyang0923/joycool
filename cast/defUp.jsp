<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*"%><%
	
	CastleBaseAction action = new CastleBaseAction(request);
	CastleBean castleBean = action.getCastle();
	%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="攻防升级"><p><%@include file="top.jsp"%>
长枪兵<br/>攻击<a href="upDef.jsp?t=0&amp;s=1">升级</a>|防御<a href="upDef.jsp?t=1&amp;s=1">升级</a><br/>
弓箭手<br/>攻击<a href="upDef.jsp?t=0&amp;s=2">升级</a>|防御<a href="upDef.jsp?t=1&amp;s=2">升级</a><br/>
剑士<br/>攻击<a href="upDef.jsp?t=0&amp;s=3">升级</a>|防御<a href="upDef.jsp?t=1&amp;s=3">升级</a><br/>
骑士<br/>攻击<a href="upDef.jsp?t=0&amp;s=4">升级</a>|防御<a href="upDef.jsp?t=1&amp;s=4">升级</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>