<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
敬请细读:炒股五步指南<br/>
炒股第四步:等待系统交易
系统在很短的时间内自动将股市中所有股民填写的买卖交易委托单进行对比，自动完成交易。交易的原则是先找价格最好的单子，如果价格一样就找时间最早的单子。比如您填了一张100乐币价格买100手股票，现在股市里有80卖100手的，也有100卖100手的，系统就会为您按80的价格买入。如果同时有两张以上80卖出的单子，系统就会从中选择卖出时间早的单子。<br/>
买卖成交时会收取0.3％的手续费，没有成交的委托不会收取手续费（新股首次发行时不收手续费）。<br/>
<br/>
<a href="/stock2/help/step3.jsp">炒股第三步:填交易委托单</a><br/>
<a href="/stock2/help/step5.jsp">炒股第五步:转乐币出股市花</a><br/>
<a href="../index.jsp">返回股市大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>