<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.fish.FishAction,net.joycool.wap.action.job.fish.AreaBean" %><%@ page import="java.util.*" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
if (action.getFishUser() == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
action.waitFish();
AreaBean area = action.getFishUser().getArea();
if(area == null){
action.sendRedirect("index.jsp", response);
return;
}
String url = ("doFish.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
鱼竿放下去了，请耐心等待，5秒后再看看~~<br/>
<a href="<%=url%>">心烦气躁不等了</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>