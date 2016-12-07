<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%@ page import="net.joycool.wap.action.stock2.StockWorld" %><%@ page import="net.joycool.wap.bean.stock2.StockWTBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.accountWTList();
String result=(String)request.getAttribute("result");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector stockWTList = (Vector)request.getAttribute("stockWTList");
String orderBy = (String)request.getAttribute("orderBy");
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
<%}else{%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
委托记录<br/>
-----
<%
if("1".equals(orderBy)){%>
买|<a href="accountWTList.jsp?orderBy=0">卖</a>
<%}else{%>
<a href="accountWTList.jsp?orderBy=1">买</a>|卖
<%}%>-----<br/>
<%
if(stockWTList.size()>0){%>
委托价|委托数|成交数<br/><%
StockWTBean stockWT = null;
StockBean stock = null;
for(int i = 0 ; i < stockWTList.size();i++){
	stockWT = (StockWTBean)stockWTList.get(i);
	stock = StockWorld.getStock("id="+stockWT.getStockId());
	if(stock==null)continue;
	%>
	<%=stock.getMark()%><a href="wt.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>(<%=stock.getCode()%>)<a href="destroy.jsp?wtId=<%=stockWT.getId()%>">撤单</a><br/>
	<%=StringUtil.numberFormat(stockWT.getPrice())%>乐币|<%=stockWT.getCount()%>股|<%=stockWT.getCjCount()%>股<br/>
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