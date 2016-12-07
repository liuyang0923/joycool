<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request, response);
if(action.pick2()) return;
PickBean pick = (PickBean)request.getAttribute("pick");
if(pick == null && !action.isResult("tip"))
	action.tip("tip","不存在的目标");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>

<%}else{%>

<%=pick.getName()%>(等级<%=pick.getLandItem().getRank()%>)<br/>
<a href="pick2.jsp?a=1&amp;id=<%=pick.getGid()%>">立刻采集</a><br/>
<%}%>
<a href="map.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>