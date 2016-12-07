<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	BuildingAction action = new BuildingAction(request);
	boolean flag = action.upgrade();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="建筑升级"><p><%@include file="top.jsp"%>
<%=request.getAttribute("msg")%><br/>
升级中的建筑<a href="amsg.jsp">查看</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>