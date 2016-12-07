<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%@ page import="java.util.Iterator" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
//action.index();
DhhUserBean dhhUser = action.getDhhUser();
DhhShipBean ship = action.getUserShip();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
造船厂<br/>
现金：<%=dhhUser.getMoney()%>￥
存款：<%=dhhUser.getSaving()%>￥<br/>
===你现有的船只===<br/>
船只类型：<%=ship.getName()%><br/>
船只速度：<%=ship.getSpeed()%><br/>
运载容量：<%=ship.getVolume()%><br/>
===船厂出售===<br/>
<%
Iterator iter = action.getWorld().shipList.iterator();
while(iter.hasNext()) {
ship = (DhhShipBean)iter.next();
%>
<a href="ship.jsp?id=<%=ship.getId()%>"><%=ship.getName()%></a>
<%=ship.getPrice()%>￥<br/>
<%}%>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>