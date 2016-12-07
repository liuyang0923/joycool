<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
敬请细读:炒股五步指南<br/>
炒股第三步:填交易委托单<br/>
点击“进行股票交易”就能开始炒股。买和卖股票都需要填相应的交易委托单，委托给系统交易。<br/>
进去之后选择一支您想交易的股票，然后可以看见这支股票的上一笔成交价格、您持有的数量，系统中等待成交的股票数量、您股市账户中的流动资金。<br/>
这里还可以看见：委托卖前五手信息，包括价格和数量（数量1手＝100股），就是别人正在以这个价格卖出这么多的这支股票；委托买前五手信息，也包括数量和价格，就是别人正在以这个价格收购这么多的这支股票。如果用高于或等于别人买的价格买，用低于别人卖的价格卖，就能马上买卖成功。在看好股票的情况下高卖低买，就能赚钱。<br/>
注意:股票交易的最小单位是100股，买卖价格距昨日收盘价的涨跌幅不能超过10％，也不能高于1000，否则委托失败。<br/>
您填好委托单之后，与交易相关的卖出股票和如果买入需要花的乐币都会被暂时冻结，您不能在交易完成或者撤销委托单（撤单在“账户信息”中的“委托记录”那里）之前把这些股票、乐币挪作他用，比如再卖一次或者转出股市。<br/>
<br/>
<a href="/stock2/help/step2.jsp">炒股第二步:观察股票行情</a><br/>
<a href="/stock2/help/step4.jsp">炒股第四步:等待系统交易</a><br/>
<a href="../index.jsp">返回股市大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>