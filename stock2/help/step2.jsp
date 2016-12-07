<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
敬请细读:炒股五步指南<br/>
炒股第二步:观察股票行情<br/>
每支股票都有详细信息，点击股票行情就可以查看，是您炒股的重要依据。<br/>
上次成交价:上一笔成功卖出的股票的卖出价格。<br/>
涨跌幅:上次成交价与今日开盘价的涨跌百分比。<br/>
成交量:今日这支股票的交易手数。<br/>
股票符号说明：股市大厅里每支股票前都有一个表示股票状态的符号。↑表示当前价高于昨日收盘价，↓表示当前价低于昨日收盘价，#表示当前价等于昨日收盘价，×表示该股暂时停牌，*表示该股票是首发新股。<br/>
<br/>
<a href="/stock2/help/step1.jsp">炒股第一步:将乐币转入股市账户</a><br/>
<a href="/stock2/help/step3.jsp">炒股第三步:填交易委托单</a><br/>
<a href="../index.jsp">返回股市大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>