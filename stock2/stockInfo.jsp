<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.stockInfo();
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
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
个股信息<br/>
--------------<br/>
<%=stock.getName()%>(<%=stock.getCode()%>)<a href="wt.jsp?stockId=<%=stock.getId()%>">买卖</a><br/>
现价：<%=StringUtil.numberFormat(stock.getPrice())%>乐币<br/>
状态：<%=stock.getStatusName()%><br/>
涨幅：<%=stock.getChangePercent()%><br/>
成交量：<%=StringUtil.bigNumberFormat(stock.getCount()/100)%>手<br/>
换手率：<%=StringUtil.numberFormat((float)stock.getCount() / stock.getTotalCount()*100)%>%<br/>
昨收：<%=StringUtil.numberFormat(stock.getEndPrice())%>乐币<br/>
发行时间：<%=DateUtil.formatDate1(stock.getCreateDatetime())%><br/>
发行价：<%=StringUtil.numberFormat(stock.getStartPrice())%>乐币<br/>
发行量：<%=StringUtil.bigNumberFormat(stock.getTotalCount()/100)%>手<br/>
公司简介：<%=StringUtil.toWml(stock.getDesc())%><br/>
公司地址：<a href="<%=(stock.getUrl())%>">点击访问</a><br/>
所属板块：<%=stock.getTypeName()%>股<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>