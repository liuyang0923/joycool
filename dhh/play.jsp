<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.play();
DhhUserBean dhhUser = action.getDhhUser();
if(dhhUser.isGameOver()) {
action.sendRedirect("result.jsp", response);
return;
}
DhhShipBean ship = action.getUserShip();
String url=("/lswjs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getUserCity().getName()%>
第<%=dhhUser.getPasttime()%>天<br/>
------我的状态------<br/>
现金：<%=dhhUser.getMoney()%>￥
存款：<%=dhhUser.getSaving()%>￥<br/>
船只类型：<%=ship.getName()%><br/>
船只速度：<%=ship.getSpeed()%><br/>
运载能力：<%=ship.getVolume()%><br/>
<%@include file="map.jsp"%>
===买卖动态信息===<br/>
<%=action.toString()%>
<a href="/dhh/savegame.jsp">保存游戏</a>(无货品才可保存，保存后下次才能继续游戏)<br/>
<%if(action.getDhhUser().getPasttime() > 10){%>
<a href="result.jsp">提前结束游戏</a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>