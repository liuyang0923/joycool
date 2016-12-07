<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.ship();
DhhShipBean ship = (DhhShipBean)request.getAttribute("ship");
DhhUserBean dhhUser = action.getDhhUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=ship.getName()%><br/>
<img src="img/<%=ship.getImage()%>" alt="logo"/><br/>
船只速度：<%=ship.getSpeed()%><br/>
运载容量：<%=ship.getVolume()%><br/>
水手数量：<%=ship.getSailor()%><br/>
出售价格：<%=ship.getPrice()%>￥<br/>
<a href="/dhh/buyshipresult.jsp?shipid=<%=ship.getId()%>">购买</a>
<a href="buyship.jsp">返回造船长</a><br/>

<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>