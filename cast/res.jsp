<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	action.setAttribute2("casSwi","res.jsp");
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="资源"><p>
<%@include file="top.jsp"%>
==详细资源状况==<br/>
木材<%=us.getWood(nowTime)%>/<%=us.getMaxRes()%>上限|产量<%=us.getWoodSpeed2()%>/小时<br/>
石头<%=us.getStone(nowTime)%>/<%=us.getMaxRes()%>上限|产量<%=us.getStoneSpeed2()%>/小时<br/>
铁块<%=us.getFe(nowTime)%>/<%=us.getMaxRes()%>上限|产量<%=us.getFeSpeed2()%>/小时<br/>
粮食<%=us.getGrain(nowTime)%>/<%=us.getMaxGrain()%>上限|产量<%=us.getGrainRealSpeed2()%>/小时<br/>
人口<%=us.getPeople()%>/<%=us.getGrainSpeed2()%>上限<br/>
士兵消耗粮食<%=us.getPeople2x()%>/小时<br/>
本城堡增长<%=us.getCivil()%>文明度/天<br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>