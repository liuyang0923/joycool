<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="net.joycool.wap.bean.stock.StockBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.Date" %><%@ page import="java.util.Calendar" %><%@ page import="net.joycool.wap.bean.stock.StockInfoBean" %><%response.setHeader("Cache-Control", "no-cache");
			StockAction action = new StockAction(request);
			action.getStockInfo(request);%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个股信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%StockBean stock =(StockBean)request.getAttribute("stock");
StockInfoBean stockInfo =(StockInfoBean)request.getAttribute("stockInfo");
String tip=(String)request.getAttribute("tip");
if(null!=tip)
{%>
<%=tip%><br/>
<%}else{%>
<%= StringUtil.toWml(stock.getName()) %><br/>
实时指数：<%=stock.getPrice()%>（<%=action.getTime()%>）<br/>
<%if(stockInfo!=null){%>
昨收：<%=stockInfo.getYesterdayPrice()%><br/>
今开：<%=stockInfo.getTodayPrice()%><br/>
<%} 
boolean isWindows = false;
String osName = System.getProperty("os.name");
if(osName!=null){
	isWindows = (osName.toLowerCase().indexOf("windows")!=-1);
}
String imagePath = (isWindows)?"/img/stock/":Constants.STOCK_RESOURCE_ROOT_URL;
String imageFile = imagePath + stockInfo.getStockId() + ".jpg";
%>
<img src="<%= imageFile %>" alt="loading..."/><br/>
<a href="/stock/stockIntroduce.jsp?id=<%=stock.getId()%>">上市公司简介</a><br/>
	<a href="/stock/stockPartner.jsp?id=<%=stock.getId()%>">公司十大股东</a>
	<br/>
<anchor title="确定">交易
<go href="/stock/stockBusiness.jsp?id=<%=stock.getId()%>" method="post">
</go>
</anchor>
<br/>
<%}%>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
