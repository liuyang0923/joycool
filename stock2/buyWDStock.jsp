<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.bean.stock2.StockCCBean" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.buyWDStock();
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
StockBean stock = (StockBean)request.getAttribute("stock");
StockAccountBean account = (StockAccountBean)request.getAttribute("account");
StockCCBean  stockCC = (StockCCBean)request.getAttribute("stockCC");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
买系统股票<br/>
--------------<br/>
<%=stock.getMark()%><a href="stockInfo.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>(<%=stock.getCode()%>)<br/>
发行价：<%=stock.getStartPrice()%>乐币<br/>
股票剩余：<%=stock.getWithdraw()%><br/> 
目前拥有数量：<%=stockCC.getCount()%>股<br/>
目前冻结数量：<%=stockCC.getCountF()%>股<br/>
账户可用资金：<%=account.getMoney()%>乐币<br/>
购买:<input name="count"  maxlength="9" format="*N"  value="100"/>股<br/>
<anchor title="确定">确定
  <go href="buyWDStockResult.jsp" method="post">
    <postfield name="count" value="$count"/>
    <postfield name="stockId" value="<%=stock.getId()%>"/>
  </go>
</anchor><br/>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>