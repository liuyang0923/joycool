<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.stock2.StockWorld" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.holdStock();
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
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector stockCCList = (Vector)request.getAttribute("stockCCList");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
持仓信息<br/>
====我的股票====<br/>
现价|成本价|可用数量<br/>
<%
if(stockCCList.size()>0){
StockCCBean stockCC = null;
StockBean stock = null;
for(int i = 0 ; i < stockCCList.size();i++){
	stockCC = (StockCCBean)stockCCList.get(i);
	stock = StockWorld.getStock("id="+stockCC.getStockId());
	if(stock==null)continue;%>
	<%=stock.getMark()%><a href="stockInfo.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>(<%=stock.getCode()%>)
	<a href="wt.jsp?stockId=<%=stock.getId()%>">买卖</a><br/>
	<%=StringUtil.numberFormat(stock.getPrice())%>乐币|<%=StringUtil.numberFormat(stockCC.getAveCost())%>乐币|<%=stockCC.getCount()%>股<br/>
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "holdStock.jsp", false, "|", response)%>
<%}else{%>
（无）<br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>