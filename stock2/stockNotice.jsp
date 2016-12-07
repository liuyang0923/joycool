<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="net.joycool.wap.bean.stock2.StockNoticeBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.stockNotice();
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
StockNoticeBean stockNotice=(net.joycool.wap.bean.stock2.StockNoticeBean)request.getAttribute("stockNotice");
%>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
证券公告<br/>
------------<br/>
<%=StringUtil.toWml(stockNotice.getTitle())%><br/>
<%=stockNotice.getCreateDatetime().substring(0, 10)%><br/>
<%=StringUtil.toWml(stockNotice.getContent())%><br/>
<a href="noticeList.jsp">返回乐酷股市公告</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>
