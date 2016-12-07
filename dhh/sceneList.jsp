<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
//action.index();
DhhUserBean dhhUser = action.getDhhUser();
DhhCityBean city = action.getUserCity();
DhhShipBean ship = action.getUserShip();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
出航<br/>
所在港口:<%=city.getName()%><br/>
船只类型：<%=ship.getName()%><br/>
=============<br/>
你准备向哪个港口航行？<br/>
<%
for(int i=0;i<DHHWorld.cityNumber;i++){
	int dis = DHHWorld.cityDist[dhhUser.getCity()-1][i];
	if(dis != 0){%>
	<a href="/dhh/go.jsp?cityid=<%=(i+1)%>"><%=(((DhhCityBean)DHHWorld.getWorld().cityMap.get(new Integer(i+1))).getName())%></a>：
	<%=dis%>公里<br/>
<%}}%>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>