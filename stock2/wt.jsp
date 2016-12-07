<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%@ page import="net.joycool.wap.action.stock2.StockWorld" %><%@ page import="net.joycool.wap.bean.stock2.StockWTBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.wt();
String result=(String)request.getAttribute("result");
String url = ("index.jsp");
String num[] = {"一", "二", "三", "四", "五"};
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
StockBean stock = (StockBean)request.getAttribute("stock");
StockAccountBean account = (StockAccountBean)request.getAttribute("account");
StockCCBean  stockCC = (StockCCBean)request.getAttribute("stockCC");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
买卖股票<br/>
--------------<br/>
<%=stock.getMark()%><a href="stockInfo.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>(<%=stock.getCode()%>)<br/>
现价：<%=StringUtil.numberFormat(stock.getPrice())%>乐币<br/>
<%if(stockCC.getId()!=0){%>成本价：<%=StringUtil.numberFormat(stockCC.getAveCost())%>乐币<br/><%}%>
可用数量：<%=stockCC.getCount()%>股<br/>
冻结数量：<%=stockCC.getCountF()%>股<br/>
可用资金：<%=account.getMoney()%>乐币<br/>
<%if(!StockBean.isOpen()){%>
<br/>休市中，暂时无法交易。<br/><br/>
<%}else if(stock.isStatusStop()){%>
<br/>该股票停牌，暂时无法交易。<br/><br/>
<%}else if(stock.getStatus()==3){%>
<br/>该股票已退市，无法交易。<br/><br/>
<%}else if(stock.isStatusNew()){%>
====新股申购====<br/>
新股申购流程和帮助请看<a href="new.jsp">这里</a><br/>
最大可申购数量：<%=stock.getNewCount()%>股<br/>
申购价：<%=StringUtil.numberFormat(stock.getPrice())%>乐币<br/>
申购数量(股)<input name="buyCount" format="*N" maxlength="15" value="100"/><br/>
<anchor title="确定">确认申购
  <go href="buyResult.jsp" method="post">
    <postfield name="buyCount" value="$buyCount"/>
    <postfield name="buyPrice" value="<%=stock.getPrice()%>"/>
    <postfield name="stockId" value="<%=stock.getId()%>"/>
  </go>
</anchor><br/>
<%}else{%>
====买卖下单====<br/>
涨停价：<%=StringUtil.numberFormat(stock.getMaxPrice())%>乐币<br/>
跌停价：<%=StringUtil.numberFormat(stock.getMinPrice())%>乐币<br/>
买入价格(乐币)<input name="buyPrice"  maxlength="9" value="100"/><br/>
买入数量(股)<input name="buyCount" format="*N" maxlength="15" value="100"/><br/>
<anchor title="确定">确认买入
  <go href="buyResult.jsp" method="post">
    <postfield name="buyCount" value="$buyCount"/>
    <postfield name="buyPrice" value="$buyPrice"/>
    <postfield name="stockId" value="<%=stock.getId()%>"/>
  </go>
</anchor><br/>
卖出价格(乐币)<input name="salePrice"  maxlength="9" value="100"/><br/>
卖出数量(股)<input name="saleCount" format="*N" maxlength="15" value="100"/><br/>
<anchor title="确定">确认卖出
  <go href="saleResult.jsp" method="post">
    <postfield name="saleCount" value="$saleCount"/>
    <postfield name="salePrice" value="$salePrice"/>
    <postfield name="stockId" value="<%=stock.getId()%>"/>
  </go>
</anchor><br/>
====<a href="cjToday.jsp?stockId=<%=stock.getId()%>">盘口信息</a>====<br/>
<% StockWTBean wt = null;
Vector saleTop5 = StockWorld.getStockTop5(stock.getId(),0,"asc");
for(int i = 4;i>=0;i--){%>
卖<%=num[i]%>：<%
if(i >= saleTop5.size()){%>无
<%}else{ wt= (StockWTBean)saleTop5.get(i);%>
<%=StringUtil.numberFormat(wt.getPrice())%>乐币
<%=StringUtil.bigNumberFormat(wt.getCount()/100)%>手<%}%><br/>
<%}%>
<%
Vector buyTop5 = StockWorld.getStockTop5(stock.getId(),1,"desc");
for(int i =0;i<5;i++){%>
买<%=num[i]%>：<%
if(i >= buyTop5.size()){%>无
<%}else{ wt= (StockWTBean)buyTop5.get(i);%>
<%=StringUtil.numberFormat(wt.getPrice())%>乐币
<%=StringUtil.bigNumberFormat(wt.getCount()/100)%>手<%}%><br/>
<%}%>
<%}%>
<a href="wdStock.jsp?stockId=<%=stock.getId()%>">系统回收股票</a><br/>
<a href="buyWDStock.jsp?stockId=<%=stock.getId()%>">购买原始股票</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>