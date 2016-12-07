<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.SendAction" %><%
response.setHeader("Cache-Control","no-cache");
SendAction action  = new SendAction(request);
action.delMessage(request);
String pageIndex = (String)request.getAttribute("pageIndex");
String url=("/user/ViewMessages.do?pageIndex="+pageIndex);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="信箱" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回收信箱)<br/>
<a href="/user/ViewMessages.do?pageIndex=<%=pageIndex%>">返回收信箱</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>