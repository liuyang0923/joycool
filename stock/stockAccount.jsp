<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的帐户">
<p align="left">
<%=BaseAction.getTop(request, response)%>
我的帐户：<br/>
<a href="/stock/stockHolder.jsp">持有股票</a><br/>
<a href="/stock/stockUserInfo.jsp">用户信息</a><br/>
<a href="/stock/stockBusinessInfo.jsp">交易记录</a><br/>
<br/>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
