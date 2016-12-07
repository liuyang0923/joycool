<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.cars();
FarmUserBean farmUser = action.getUser();
boolean noline = true;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
List cars = (List)request.getAttribute("cars");
if(cars!=null)
for(int i = 0;i < cars.size();i++){
Integer iid = (Integer)cars.get(i);
FarmCarBean car = action.world.getCar(iid.intValue());
if(car==null)continue;
if(car.getQuestId() > 0 && !farmUser.isQuestEnd(car.getQuestId())){
%><%=car.getName()%>(无法乘坐)<br/><%
continue;}
noline = false;
%>
<a href="car.jsp?id=<%=car.getId()%>"><%=car.getName()%></a>-
<a href="car.jsp?a=1&amp;id=<%=car.getId()%>">直接乘坐</a><br/>
<%}%>
<%if(noline){%>
很抱歉,这里没有你能乘坐的线路.<br/>
<%}%>
<%}%>
<a href="npc.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>