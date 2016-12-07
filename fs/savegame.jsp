<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.FSAction" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.savegame();
String url=("/fs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%>(3秒后返回城市)<br/>
<a href="<%=url%>">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>