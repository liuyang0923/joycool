<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.lhc.LhcAction" %><%
response.setHeader("Cache-Control","no-cache");
LhcAction action = new LhcAction(request);
action.buyResult();
String result =(String)request.getAttribute("result");
String url=("/lhc/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=LhcAction.LHC_NAME%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后返回六合彩首页)<br/>
<a href="/lhc/index.jsp">返回六合彩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/><br/>
继续购买：<br/>
<%--<a href="/lhc/buy.jsp?type=8">平码</a>|--%><a href="/lhc/buy.jsp?type=9">特码</a>|<a href="/lhc/buy.jsp?type=1">大小</a>|<a href="/lhc/buy.jsp?type=2">六肖</a>|<a href="/lhc/buy.jsp?type=4">前后</a><br/>
<a href="/lhc/buy.jsp?type=5">波色</a>|<a href="/lhc/buy.jsp?type=6">五行</a>|<a href="/lhc/buy.jsp?type=7">单双</a>|<a href="/lhc/buy.jsp?type=3">家禽野兽</a><br/>
<a href="/lhc/index.jsp">返回六合彩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>