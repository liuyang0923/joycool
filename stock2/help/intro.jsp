<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
各种名词的解释与说明<br/>
股票行情:股票的交易情况，包括名称、上一笔成交价格、上笔成交价格与当天开盘价（等于昨日收盘价，昨天最后一笔成功交易的价格）相比的涨跌幅度、当天交易量。<br/>
进行股票交易：进去填交易委托单，把乐币或者股票委托给系统进行自动交易。<br/>
交易委托单：买股票就填希望每股的买入价格（必须在上一比成交价格的上下10％以内）和想买的数量（必须是100的整数），卖股票就填希望每股以多少乐币卖出去（必须在上一比成交价格的10％以内）和想卖出的数量（必须是100的整数），然后把这个单子委托给系统。<br/>
股票交易：系统自动检查一支股票的所有买和卖的委托单，为买卖双方选择最好的价格，如果价格一样则按填委托单的先后选择。<br/>
转乐币入股市：把乐币从银行转进自己的股市账户，没有金额限制。<br/>
股市账户：为了保证太有钱的人扰乱股市，股市里的乐币和社区的乐币是相对独立的，社区里的乐币不能直接在股市花，需要转入，具体参见“转乐币入股市”。<br/>
兑换乐币：将股市里的钱转到社区。这个没有数量限制，赚到的钱可以随时转到社区消费。<br/>
股市交易时间：每天的时间是上午11点到晚上11点，期间股市一直进行自动交易，其余时间股市休市，进行各种结算，也保证股民正常休息时间避免股市波动受损失。<br/>
为什么要限制每张委托单的价格不能高低超过上笔的10％，交易数量必须是100的整数：真实股市中为了限制股价波动过大有涨停和跌停限制，为了方便系统交易每次交易的最小单位也是“手”（100股）。乐酷股市做这样的限制是为了使股市更真实有趣。<br/>
持有股票：您现在手里有的股票，能查看价格和数量。<br/>
用户信息：查看您的昵称、开户时间、股市之外的乐币数量、股市总资本（股市中的现金＋股票市值）、股票市值（股票数量×当前价格）。<br/>
委托记录：您已经填好提交给股市的交易委托单，包括买和卖股票的，这里也能撤销您的单子。<br/>
交易记录：您已经完成的股票买卖的记录，包括买和卖股票的价格和数量。<br/>
<br/>
[请一定点击]<a href="/stock2/help/step1.jsp">炒股五步指南</a><br/>
<a href="../index.jsp">返回股市大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>