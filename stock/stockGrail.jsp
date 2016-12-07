<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="net.joycool.wap.bean.stock.StockGrailBean" %><%@ page import="java.util.Calendar" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action=new StockAction(request);
StockGrailBean bean=action.getStockGrail();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大盘信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(bean!=null){%>
大盘信息<br/>
实时指数：<%=bean.getNowPrice()%>（<%=action.getTime()%>）<br/>
昨收：<%=bean.getYesterdayPrice()%><br/>
今开：<%=bean.getTodayPrice()%><br/>
<%
boolean isWindows = false;
String osName = System.getProperty("os.name");
if(osName!=null){
	isWindows = (osName.toLowerCase().indexOf("windows")!=-1);
}
String imagePath = (isWindows)?"/img/stock/":Constants.STOCK_RESOURCE_ROOT_URL;
String imageFile = imagePath + "stockgrail.jpg";
%>
<img src="<%= imageFile %>" alt="loading..."/><br/>
<%}%>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
