<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
敬请细读:炒股五步指南<br/>
炒股第一步:将乐币<a href="/stock2/toStockMoney.jsp">转入股市账户</a><br/>
点击"将乐币转入股市"，设定自己转入股市账户的乐币，就在乐酷股市中成功建立起了股市账户。<br/>
1、股市资金必须从银行直接转帐<br/>
2、注入股市的资金没有限制<br/>
<br/>
<a href="/stock2/help/step2.jsp">炒股第二步:观察股票行情</a><br/>
<a href="../index.jsp">返回股市大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>