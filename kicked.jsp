<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="系统提示">
<p align="left">
有其他人登陆这个帐号，您被迫下线。<a href="/user/login.jsp">重新登陆</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>