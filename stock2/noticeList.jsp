<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%@ page import="net.joycool.wap.bean.stock2.StockNoticeBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.noticeList();
Vector stockNoticeList = (Vector)request.getAttribute("stockNoticeList");
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
股市公告<br/>
-----------<br/>
<%
StockNoticeBean stockNotice= null;
for (int i = 0; i < stockNoticeList.size(); i++) {
	stockNotice = (StockNoticeBean) stockNoticeList.get(i);
%>
<%=i+1%>.
<a href="<%=( "stockNotice.jsp?noticeId="+stockNotice.getId())%>"><%=StringUtil.toWml(stockNotice.getTitle())%></a><br/>
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "noticeList.jsp", false, "|", response)%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>
