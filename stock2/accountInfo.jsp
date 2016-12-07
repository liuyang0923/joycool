<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.accountInfo();
String result=(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean userStatus = (UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
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
String stockPrice=(String)request.getAttribute("stockPrice");
String stockTotalPrice=(String)request.getAttribute("stockTotalPrice");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/stock2/index.gif" alt="loading"/><br/>
昵称:<%=StringUtil.toWml(loginUser.getNickName())%><br/>
开户时间：<%=account.getCreateDatetime()%><br/>
<%--股市之外的乐币：<%=userStatus.getGamePoint()%>乐币<br/>--%>
股市帐户里总资本：<%=stockTotalPrice%>乐币<br/>
股市流动资金：：<%=account.getMoney()%>乐币<br/>
股市冻结资金：：<%=account.getMoneyF()%>乐币<br/>
持有股票市值：<%=stockPrice%>乐币<br/><br/>

<a href="toStockMoney.jsp">把社区乐币转到股市</a><br/>
<a href="toGamePoint.jsp">把股市乐币转到社区</a><br/><br/>
<a href="stockList.jsp">行情</a>|<a href="accountInfo.jsp">帐户</a>|<a href="holdStock.jsp">持仓</a>|<a href="index.jsp">买卖</a>|<a href="accountWTList.jsp">委托</a>|<a href="cjHistory.jsp">成交</a><br/>
<a href="index.jsp">返回股市交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>