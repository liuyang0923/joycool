<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.*" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.stockList();
String result=(String)request.getAttribute("result");
int type = action.getAttributeInt("type");
String url = ("index.jsp");
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
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
Vector stockList = (Vector)request.getAttribute("stockList");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
个股行情(<%=pagingBean.getTotalCount()%>)<br/>
--------------<br/>
<%if(type>=0){%><a href="stockList.jsp">全部</a><%}else{%>全部<%}%>
<% for(int i = 0;i < 3;i++){
if(type!=i){%>|<a href="stockList.jsp?type=<%=i%>"><%=StockBean.getTypeName(i)%>股</a><%}else{%>|<%=StockBean.getTypeName(i)%>股<%}}%>
<br/>
现价|涨幅|成交量<br/>
<%
StockBean stock = null;
for(int i = 0 ; i < stockList.size();i++){
stock = (StockBean)stockList.get(i);%>
<%=stock.getMark()%><a href="stockInfo.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>
(<%=stock.getCode()%>)<a href="wt.jsp?stockId=<%=stock.getId()%>">买卖</a><br/>
<%=StringUtil.numberFormat(stock.getPrice())%>乐币|<%=stock.getChangePercent()%>|<%=StringUtil.bigNumberFormat(stock.getCount()/100)%>手<br/>
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "stockList.jsp", false, "|", response)%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>