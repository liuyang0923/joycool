<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,net.joycool.wap.spec.castle.*"%><%
	
	TongAction action = new TongAction(request);
	action.quitTong();
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="退出联盟"><p><%@include file="top.jsp"%>
<%=(request.getAttribute("msg") != null)?request.getAttribute("msg")+"<br/>":"" %>
<%if(!action.hasParam("uid") && request.getAttribute("del_tong") == null) {%>
请输入银行密码确认移除:<br/>
<input name="pwd" value=""/><br/>
<anchor>确认移除联盟<go href="quitTong.jsp?pos=<%=request.getParameter("pos") %>">
<postfield name="pwd" value="$pwd"/>
</go></anchor><br/>
<%} %>
<a href="../fun.jsp?pos=<%=request.getParameter("pos") %>">返回大使馆</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>