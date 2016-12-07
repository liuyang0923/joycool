<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="老虎机">
<p align="center">
<%=BaseAction.getTop(request, response)%>
老虎机<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="help.jsp">游戏帮助</a><br/>
<br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>