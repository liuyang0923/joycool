<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.saveGame();
String url=("/dhh/play.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=action.getTip()%>(3秒后返回港口)<br/>
<a href="<%=url%>">返回港口</a><br/>  
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>