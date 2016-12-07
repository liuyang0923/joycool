<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.action.stock2.StockWorld" %><%@ page import="net.joycool.wap.bean.stock2.StockCJBean" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.cjHistory();
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
Vector stockCJList = (Vector)request.getAttribute("stockCJList");
String orderBy = (String)request.getAttribute("orderBy");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
成交记录<br/>
---------
<%
if("1".equals(orderBy)){

%>买|<a href="cjHistory.jsp?orderBy=0">卖</a><%

}else{

%><a href="cjHistory.jsp?orderBy=1">买</a>|卖<%

}%>-----<br/><%

if(stockCJList.size()>0){%>
成交价|成交数|初始数|手续费|成交时间<br/><%
StockCJBean stockCJ = null;
StockBean stock = null;
for(int i = 0 ; i < stockCJList.size();i++){
	stockCJ = (StockCJBean)stockCJList.get(i);
	stock = StockWorld.getStock("id="+stockCJ.getStockId());
	if(stock==null)continue;

%><a href="wt.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>(<%=stock.getCode()%>)<br/>
	<%=StringUtil.numberFormat(stockCJ.getPrice())%>乐币|<%=stockCJ.getCjCount()%>股/<%=stockCJ.getCount()%>|<%=stockCJ.getCharge()%>乐币|<%=DateUtil.formatDate2(stockCJ.getCreatedatetime())%><br/>
<%}%>
<%String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);if(!"".equals(fenye)){%><%=fenye%><br/><%}
}else{%>
（无）<br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>