<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.getStockIntroduce(request);%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="公司信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
上市公司简介：<br/>
<%String introduce=(String)request.getAttribute("stockIntroduce");
String id=(String)request.getAttribute("id");
if(null!=introduce){%>
<%=introduce%><br/>
<%}%>
<br/>
<a href="/stock/stockInfo.jsp?id=<%=id%>">返回个股信息</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>