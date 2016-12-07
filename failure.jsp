<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction" %><%
response.setHeader("ServiceMonitor","0");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="系统提示">
<p align="left">
处理失败,页面已经过期。<br/>
<anchor title="back"><prev/>返回上页</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>