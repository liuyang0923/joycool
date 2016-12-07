<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	HiddenSoldierAction action = new HiddenSoldierAction(request);
	if(action.hiddenSoldier()) {
		response.sendRedirect("fun.jsp?pos="+action.getParameterString("pos"));
		return;
	}
	%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="藏兵"><p><%@include file="top.jsp"%>
<%=request.getAttribute("msg")%><br/><a href="fun.jsp?pos=<%=request.getParameter("pos") %>">返回藏兵洞</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>