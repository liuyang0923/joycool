<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%@ page import="net.joycool.wap.action.stock2.StockWorld" %><%@ page import="net.joycool.wap.bean.stock2.StockWTBean" %><%@ page import="net.joycool.wap.bean.bank.*" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.toStockMoney();
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
StockAccountBean account = (StockAccountBean)request.getAttribute("account");
StoreBean store = (StoreBean)request.getAttribute("store");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行存款：<%=store.getMoney()%>乐币<br/>
可用资金：<%=account.getMoney()%>乐币<br/>
*只有把普通乐币存到股市账户里，才能进行股票买卖。<br/>
*赚钱后你也可以到“账户信息”里把股市账户的钱换成乐币尽情花。<br/>
<br/>
把银行存款转帐到股市<br/>
数量：<input name="price"  maxlength="12" value="100" format="*N"/>乐币<br/>
<anchor title="确定">确定
  <go href="toStockMoneyResult.jsp" method="post">
    <postfield name="price" value="$price"/>
  </go>
</anchor><br/><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>