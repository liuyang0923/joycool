<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	SoldierAction action = new SoldierAction(request);
	if(action.produceSoldier()) {
		response.sendRedirect("fun.jsp?pos="+request.getParameter("pos"));
		return;
	}
	%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="训练士兵"><p><%@include file="top.jsp"%>
<%=request.getAttribute("msg")%><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>