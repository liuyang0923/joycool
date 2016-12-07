<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock.StockNoticeBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.getStockNotice(request);
String result=(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result!=null && result.equals("success")){
StockNoticeBean stockNotice=(StockNoticeBean)request.getAttribute("stockNotice");
%>
<card title="证券公告">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(stockNotice.getTitle())%><br/>
<%=StringUtil.toWml(stockNotice.getContent())%><br/>
<a href="/stock/stockNotice.jsp">返回证券公告</a><br/>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
Vector stockNoticeList=(Vector)request.getAttribute("stockNoticeList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String datetime = (String) request.getAttribute("datetime");
%>
<card title="证券公告">
<p align="left">
<%=BaseAction.getTop(request, response)%>
证券公告<br/>
每日上市公司业绩报告<br/>
<%
StockNoticeBean stockNotice= null;
for (int i = 0; i < stockNoticeList.size(); i++) {
	stockNotice = (StockNoticeBean) stockNoticeList.get(i);
%>
<%=i+1%>.
<a href="<%=( "/stock/stockNotice.jsp?noticeId="+stockNotice.getId())%>"><%=StringUtil.toWml(stockNotice.getTitle())%></a><br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){%>
<%=fenye%><br/>
<%}%>
<a href="/stock/index.jsp">返回交易大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>
