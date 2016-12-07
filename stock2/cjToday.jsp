<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.action.stock2.StockWorld" %><%@ page import="net.joycool.wap.bean.stock2.StockCJBean" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
//action.cjToday();
int stockId = action.getParameterIntS("stockId");
Vector stockCJList = StockWorld
		.getStockService()
		.getStockCJList(
				"stock_id="
						+ stockId
						+ " and id>360000 and mark=1 and create_datetime>curdate() order by id desc limit 20");

StockBean stock = StockWorld.getStockService()
		.getStock("id=" + stockId);// 查询股票信息
				
String url = ("index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
盘口信息<br/>
---------<br/>
<%=stock.getMark()%><%=stock.getName()%>(<%=stock.getCode()%>)<a href="wt.jsp?stockId=<%=stock.getId()%>">买卖</a><br/>
<%
if(stockCJList.size()>0){%>
成交时间|成交价|成交数<br/><%
StockCJBean stockCJ = null;
for(int i = 0 ; i < stockCJList.size();i++){
	stockCJ = (StockCJBean)stockCJList.get(i);%>
	<%=DateUtil.formatTime(stockCJ.getCreatedatetime())%>|<%=StringUtil.numberFormat(stockCJ.getPrice())%>乐币|<%=StringUtil.bigNumberFormat(stockCJ.getCjCount()/100)%>手<br/>
<%}%>
<%}else{%>
（无）<br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>