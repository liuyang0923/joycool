<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
		
	CastleAction action = new CastleAction(request);
	CastleBean bean = action.getCastle();
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="攻击">	<p><%@include file="top.jsp"%>
<a href="<%=("attack.jsp?x=$(x:e)&amp;y=$(y:e)")%>">攻击坐标</a>：X:<input name="x" format="*N"/> Y:<input name="y" format="*N"/><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>