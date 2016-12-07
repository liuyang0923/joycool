<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.stock2.StockAccountBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.accountInfo();
String result=(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean userStatus = (UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
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
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
帐户信息<br/>
------------<br/>
昵称：<%=StringUtil.toWml(loginUser.getNickName())%><br/>
开户日期：<%=DateUtil.formatDate1(account.getCreateDatetime())%><br/>
总资产：<%=account.getAsset()%>乐币<br/>
可用资金：<%=account.getMoney()%>乐币<br/>
冻结资金：<%=account.getMoneyF()%>乐币<br/>
股票市值：<%=account.getStockPrice()%>乐币<br/>
------------<br/>
<a href="holdStock.jsp">持仓信息</a>（我持有的股票信息）<br/>
<a href="accountWTList.jsp">委托记录</a>（等待成交的委托信息）<br/>
<a href="cjHistory.jsp">成交记录</a>（已经成交的历史记录）<br/>
------------<br/>
<a href="toStockMoney.jsp">注入股市资金</a>（把乐币换成股市账户的钱）<br/>
<a href="toGamePoint.jsp">取出股市资金</a>（把股市账户的钱换成乐币）<br/>
<a href="/bank/bank.jsp">查看我的银行</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>