<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%@ page import="net.joycool.wap.action.stock2.StockWorld" %><%@ page import="net.joycool.wap.bean.stock2.StockWTBean" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.toStockMoneyResult();
String result=(String)request.getAttribute("result");
String url = ("account.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=StockAction.STOCK_TITLE%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回我的帐户)<br/>
<a href="<%=url%>">我的帐户</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="<%=StockAction.STOCK_TITLE%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回我的帐户)<br/>
<a href="<%=url%>">我的帐户</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>