<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%@ page import="java.util.HashMap" %><%@ page import="java.util.Iterator" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.blackMarket();
String result =(String)request.getAttribute("result");
String url=("/fs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
运气太差了!没有商品可以买卖!(3秒后返回城市)<br/>
<a href="/fs/index.jsp">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
FSUserBean fsUser = action.getFsUser();
HashMap sceneProductMap = fsUser.getScene().getSceneProductMap();
HashMap productMap = fsUser.getProductMap();
%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
黑市<br/>
<%
int eventId = fsUser.getScene().getBlackEvent();
if(eventId>0){
FSEventBean event = (FSEventBean)FSWorld.loadFSEvent().get(new Integer(eventId));%>
==号外==<br/>
<%=event.getDescription()%><br/>
============<br/>
<%}%>
现金:<%=fsUser.getMoney()%>元<br/>
====黑市商品====<br/>
<%
FSUserBagBean userbag = null;
Iterator it = sceneProductMap.values().iterator();
while(it.hasNext()){
	userbag = (FSUserBagBean)it.next();%>
	<%=userbag.getName()%>
	<%=userbag.getPrice()%>
	<a href="/fs/buy.jsp?productId=<%=userbag.getProductId()%>">买</a><br/>
<%}%>
====我的货物====<br/>
<%
if(productMap.size()>0){ %>
<%	
	it = productMap.values().iterator();
	while(it.hasNext()){
		userbag = (FSUserBagBean)it.next();%>
		<%=userbag.getName()%>
		<%=userbag.getPrice()%>元
		<%=userbag.getCount()%>个
		<a href="/fs/sell.jsp?productId=<%=userbag.getProductId()%>">卖</a><br/>
	<%}
}else{%>
（无）<br/>
<%}%>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>