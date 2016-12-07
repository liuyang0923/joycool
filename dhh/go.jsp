<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.FSAction" %><%@ page import="net.joycool.wap.action.dhh.*;" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.go();
String message =(String)request.getAttribute("message");
String event =(String)request.getAttribute("event");
String wages =(String)request.getAttribute("wages");
DhhUserBean dhhUser = action.getDhhUser();
String url=("/dhh/play.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
<%=message%>(10秒后转入港口)<br/>
<%=event%><br/>
<%=wages%><br/>
<a href="<%=url%>">进入港口</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>