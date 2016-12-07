<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.buyWDStockResult();
String result=(String)request.getAttribute("result");
String url = ("index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=StockAction.STOCK_TITLE%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回股票首页)<br/>
<a href="index.jsp">股票首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
StockBean stock = (StockBean)request.getAttribute("stock");
url = ("wt.jsp?stockId="+stock.getId());
%>
<card title="<%=StockAction.STOCK_TITLE%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(33秒后返回股票买卖页面)<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>