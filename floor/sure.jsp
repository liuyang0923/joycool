<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.floor.FloorAction" %><%@include file="../bank/checkpw.jsp"%><%
response.setHeader("Cache-Control","no-cache");
FloorAction action = new FloorAction(request);
action.sureFloor(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/floor/addPrize.jsp">返回重新填写</a><br/>
</p>
</card>
<%}else{%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/floor/index.jsp">返回乐酷踩踩乐</a><br/>
</p>
</card>
<%}%>
</wml>
