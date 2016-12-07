<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%@ page import="java.util.Iterator" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.play();
DhhUserBean dhhUser = action.getDhhUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
交易所<br/>
现金：<%=dhhUser.getMoney()%>￥
存款：<%=dhhUser.getSaving()%>￥<br/>
====港口出售====<br/>
<%
DhhCityBean city = action.getUserCity();
Iterator iter = city.getProductMap().values().iterator();
while(iter.hasNext()){
DhhCitProBean pro = (DhhCitProBean)iter.next();%>
<%=pro.getProductname()%>
<%=pro.getBuyrate()%>
<%if(pro.getQuantity() > 0){%>
<a href="/dhh/buy.jsp?productId=<%=pro.getProductid()%>">买</a>
<%}%><br/>
<%}%>
====我的货物====<br/>
<% if(dhhUser.getProductMap().size() > 0) {
iter = dhhUser.getProductMap().values().iterator();
while(iter.hasNext()){
UserBagBean pro = (UserBagBean)iter.next();%>
<%=pro.getProductname()%> <%=action.getWorld().getProductPrice(city, pro)%> <%=pro.getNumber()%>个
<a href="/dhh/sell.jsp?productId=<%=pro.getProductid()%>">卖</a><br/>
<%}} else {%>（无）<br/>
<%}%>
<%@include file="map.jsp"%>
===港口交易信息===<br/>
<%=action.toString()%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>