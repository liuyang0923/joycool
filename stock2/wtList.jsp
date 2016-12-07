<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.wtList();
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
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector stockList = (Vector)request.getAttribute("stockList");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/stock2/index.gif" alt="loading"/><br/>
股票名称(代码) <br/>
成交价|涨幅|成交量<br/>
请选择要交易的股票：<br/>
<%
StockBean stock = null;
for(int i = 0 ; i < stockList.size();i++){
	stock = (StockBean)stockList.get(i);%>
	<a href="wt.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>
	(<%=stock.getCode()%>)<br/>
	<%=StringUtil.numberFormat(stock.getPrice())%>|<%=StringUtil.numberFormat(((stock.getPrice()-stock.getEndPrice())/stock.getEndPrice())*100)%>%|<%=stock.getCount()%><br/>
<%}%>
<%String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);if(!"".equals(fenye)){%><%=fenye%><%}%><br/>
<a href="index.jsp">返回股市交易大厅</a>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>