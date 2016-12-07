<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.result();
if(!action.isResult("success")) {
action.sendRedirect("play.jsp", response);
return;
}
DhhUserBean dhhUser = action.getDhhUser();
DHHWorld.getDHHUserMap().remove(new Integer(dhhUser.getUserId()));
session.removeAttribute(DhhAction.DHH_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">    
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
游戏结束<br/>
<%=dhhUser.getGameResultTip()%><br/>
当前时间：第<%=dhhUser.getPasttime()%>天<br/>
所在港口：<%=action.getUserCity().getName()%><br/>
现金：<%=dhhUser.getMoney()%>￥<br/>
存款：<%=dhhUser.getSaving()%>￥<br/>
船只类型：<%=action.getUserShip().getName()%><br/>
<a href="/dhh/affirm.jsp">重新开始</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>