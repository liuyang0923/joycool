<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(2,request,response)%>
<a href="index.jsp">返回股市大厅</a><br/>
<a href="help/step1.jsp">[请一定点击]</a>炒股五步指南：不仔细看你会炒股我就服了你<br/>
第一步：将银行资金转入股市账户<a href="help/step1.jsp">[查看详细]</a><br/>
第二步：观察股票行情，股票交易的依据<a href="help/step2.jsp">[查看详细]</a><br/>
第三步：填交易委托单，填好单子才能成功交易赚钱<a href="help/step3.jsp">[非常重要查看详细]</a><br/>
第四步：系统完成交易，系统根据您的委托单自动进行交易<a href="help/step4.jsp">[查看详细]</a><br/>
第五步：转乐币到社区花<a href="help/step5.jsp">[查看详细]</a><br/>
[必读]<a href="help/intro.jsp">各种名词的解释与说明</a><br/>
~~~~~~~~~~~~~~<br/>
<a href="index.jsp">返回股市大厅</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>