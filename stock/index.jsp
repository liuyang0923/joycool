<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="net.joycool.wap.bean.stock.StockGrailBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock.StockBean" %><%@ page import="net.joycool.wap.bean.stock.StockInfoBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="java.text.DecimalFormat" %><%response.setHeader("Cache-Control", "no-cache");
StockAction action = new StockAction(request);
action.getStock(request);
Vector stockList=(Vector)request.getAttribute("stockList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String datetime = (String) request.getAttribute("datetime");
StockGrailBean stockGrail=(StockGrailBean)request.getAttribute("stockGrail");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="股市交易大厅">
<p align="left">
<%=BaseAction.getTop(request, response)%>
股市交易大厅<br/>
<%if (stockGrail != null) {%>
大盘指数：<%=stockGrail.getNowPrice()%> （<%=action.getTime()%>）<br/>
昨收：<%=stockGrail.getYesterdayPrice()%><br/>
今开：<%=stockGrail.getTodayPrice()%><br/>
<%}%>
<a href="/stock/stockGuide.jsp">股市指南</a><br/>
<a href="/stock/stockNotice.jsp">证券公告</a><br/>
<a href="/stock/stockAccount.jsp">我的帐户</a><br/>
<a href="/stock/stockGrail.jsp">大盘信息</a><br/>
<a href="/bank/bank.jsp">乐酷银行</a><br/>

查询个股
<select name="stockName" value="聊天大厅">
    <option value="乐酷聊天">乐酷聊天</option>
    <option value="狩猎公园">狩猎公园</option>
    <option value="女性频道">女性频道</option>
    <option value="电子书城">电子书城</option>
</select>
<anchor title="确定">go
    <go href="stockInfo.jsp" method="post">
    <postfield name="stockName" value="$stockName"/>
    </go>
</anchor><br/>
股票行情：<br/>
<%for (int i = 0; i < stockList.size(); i++) {
	StockBean stock = (StockBean) stockList.get(i);
	StockInfoBean stockInfo = action.getStockInfo("stock_id="+ stock.getId()+" order by create_datetime desc limit 0,1");
%>
<a href="/stock/stockInfo.jsp?id=<%=stock.getId()%>"><%=stock.getName()%></a><br/>
<%=stock.getPrice()%>
<%
if(stockInfo!=null && stockInfo.getTodayPrice()!=0)
{DecimalFormat df = new DecimalFormat("0.##");
float rate = (stock.getPrice() - stockInfo.getTodayPrice())* 100 / stockInfo.getTodayPrice();
rate=Float.parseFloat(df.format(rate));
%>
| <%=rate%>%<br/>
<%}
else
{%>
<br/>
<%
}
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){%>
<%=fenye%><br/>
<%}%>
<%=BaseAction.getAdver(21,response)%><!--在页面中加广告-->
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
