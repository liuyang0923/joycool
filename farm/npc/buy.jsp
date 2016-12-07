<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.buy();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>

<%}else{
FarmShopBean shopItem = (FarmShopBean)request.getAttribute("buy");
DummyProductBean item = FarmWorld.getItem(shopItem.getItemId());
%>

<%=item.getName()%><br/>
单价:<%=FarmWorld.formatMoney(shopItem.getBuyPrice())%><br/>
<%if(shopItem.isFlagStackChange()){%>库存:<%=shopItem.getStack()%><br/><%}%>
购买<a href="buy.jsp?a=1&amp;id=<%=shopItem.getId()%>">1个</a>|
<a href="buy.jsp?cnt=5&amp;a=1&amp;id=<%=shopItem.getId()%>">5个</a>|
<a href="buy.jsp?cnt=20&amp;a=1&amp;id=<%=shopItem.getId()%>">20个</a><br/>

<%}%>
<a href="buys.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>