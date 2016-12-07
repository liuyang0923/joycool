<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.stock.StockHolderBean" %><%@ page import="net.joycool.wap.bean.stock.StockBean" %><%@ page import="net.joycool.wap.bean.stock.StockDealBean" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.stockBusinessInfo(request);
Vector buy = (Vector)request.getAttribute("buy");
Vector sale = (Vector)request.getAttribute("sale");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="交易记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
交易记录：<br/>
买入<br/>
<%for(int i=0;i<buy.size();i++){
  StockDealBean deal=(StockDealBean)buy.get(i);
  if(deal!=null){
  StockBean stock=action.getStock(deal.getStockId());
  if(stock!=null){
%>
<a href="/stock/stockBusiness.jsp?id=<%=stock.getId()%>"><%=stock.getName()%></a><br/>
<%=deal.getTotalCount()%>股<br/>
价格:<%=deal.getPrice()%><br/>
<%}}}%>
<br/>
抛出<br/>
<%for(int i=0;i<sale.size();i++){
  StockDealBean deal=(StockDealBean)sale.get(i);
    if(deal!=null){
  StockBean stock=action.getStock(deal.getStockId());
   if(stock!=null){
%>
<a href="/stock/stockBusiness.jsp?id=<%=stock.getId()%>"><%=stock.getName()%></a><br/>
<%=deal.getTotalCount()%>股<br/>
价格:<%=deal.getPrice()%><br/>
<%}}}%>
<br/>
<a href="/stock/stockAccount.jsp">返回我的帐户</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
