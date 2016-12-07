<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.stock2.StockNoticeBean" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentBean" %><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.index();
UserBean loginUser = action.getLoginUser();
boolean newUser = true;
if(loginUser!=null){
	Date create = action.stockAccount.getCreateDatetime();
	newUser = create == null ? true : (System.currentTimeMillis()-create.getTime()<7*24*3600000l);
}
String accountCount =(String)request.getAttribute("accountCount");
float stockTotal = ((Float)request.getAttribute("stockTotal")).floatValue();
float stockTotalLast = ((Float)request.getAttribute("stockTotalLast")).floatValue();
if(stockTotalLast == 0)	stockTotalLast = 100;
float stockTotalChange = stockTotal - stockTotalLast;
Vector stockList = (Vector)request.getAttribute("stockList");
Vector stockNoticeList = (Vector)request.getAttribute("stockNoticeList");
Vector chatList = (Vector)request.getAttribute("chatList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(loginUser!=null){%>
<%=loginUser.showImg("/img/stock2/index.gif")%>
<%}else{%>
<img src="/img/stock2/index.gif" alt="股市"/><br/><%}%>
股市大厅<%if(newUser){%><br/>
交易时间为每天上午11点到晚上11点。<%}%><a href="help.jsp">[入市需谨慎]</a><br/>
<%if(loginUser!=null){%><a href="account.jsp">我的账户</a>|<a href="holdStock.jsp">我的股票</a><br/><%}%>
===股市大盘===<br/><%if(newUser){%>
入市人数:<%=accountCount%>人<br/><%}%>
乐酷大盘:<%=StringUtil.numberFormat(stockTotal)%>点<br/>
大盘涨跌:<%=StringUtil.numberFormat(stockTotalChange)%>点(<%=StringUtil.numberFormat(stockTotalChange/stockTotalLast*100)%>%)<br/>
===<a href="noticeList.jsp">股市公告</a>===<br/>
<%
StockNoticeBean stockNotice= null;
for (int i = 0; i < stockNoticeList.size(); i++) {
	stockNotice = (StockNoticeBean) stockNoticeList.get(i);
%>
<%=i+1%>.
<a href="<%=( "stockNotice.jsp?noticeId="+stockNotice.getId())%>"><%=StringUtil.toWml(stockNotice.getTitle())%></a><br/>
<%}%>

===<a href="stockList.jsp">个股行情</a>===<br/>
现价|涨幅|成交量<br/>
<%
StockBean stock = null;
for(int i = 0 ; i < stockList.size();i++){
	stock = (StockBean)stockList.get(i);%>
	<%=stock.getMark()%>
	<a href="stockInfo.jsp?stockId=<%=stock.getId()%>"><%=stock.getName()%></a>
	(<%=stock.getCode()%>)<a href="wt.jsp?stockId=<%=stock.getId()%>">买卖</a><br/>
	<%=StringUtil.numberFormat(stock.getPrice())%>乐币|<%=stock.getChangePercent()%>|<%=StringUtil.bigNumberFormat(stock.getCount()/100)%>手<br/>
<%}
PagingBean pagingBean = (PagingBean)request.getAttribute("page");%>
<%=PageUtil.shuzifenye(pagingBean, "index.jsp", false, "|", response)%>

<%if(loginUser!=null){%>==股市功能跳转==<br/>
<a href="index.jsp">股市大厅</a>|<a href="account.jsp">帐户</a>|<a href="holdStock.jsp">持仓</a>|<a href="stockList.jsp">行情</a><br/>
<a href="accountWTList.jsp">委托记录</a>|<a href="cjHistory.jsp">成交记录</a>|<a href="topall.jsp">排行榜</a><br/>
<a href="help.jsp">股市帮助</a>|<a href="new.jsp">新股申购流程</a><br/>
==<a href="/chat/hall.jsp?roomId=2">谈股论金聊天室</a>==<br/>
<%
JCRoomContentBean content = null;
JCRoomChatAction chatAction=new JCRoomChatAction(request);
for(int i = 0; i < chatList.size(); i ++){
	content = (JCRoomContentBean)chatList.get(i);
%>
<%=chatAction.getMessageDisplay(content, request, response)%><br/>
<%}%><%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>