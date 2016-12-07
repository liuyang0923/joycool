<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String contentId=request.getParameter("contentId");
session.setAttribute("forumtitle","true");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
乐酷论坛故障，数据正在恢复中，请稍后再来。<br/>
期间您可以选择去:<br/>
<a href="<%=response.encodeURL("/team/chat.jsp?ti=135")%>">乐酷特快圈</a><br/>
<a href="<%=response.encodeURL("/team/chat.jsp?ti=137")%>">乐酷交友圈</a><br/>
<a href="<%=response.encodeURL("/team/chat.jsp?ti=136")%>">乐酷游戏圈</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>