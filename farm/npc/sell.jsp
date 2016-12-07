<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.sell();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>

<%}else{
UserBagBean userBag = (UserBagBean)request.getAttribute("userBag");
FarmShopBean shopItem = (FarmShopBean)request.getAttribute("sell");
DummyProductBean item = FarmWorld.getItem(shopItem.getItemId());
%>

<%=item.getName()%><%=userBag.getTimeString(item)%><br/>
单价:<%=FarmWorld.formatMoney(shopItem.getSellPrice())%><br/>
总价:<%=FarmWorld.formatMoney(shopItem.getSellPrice()*userBag.getTime())%><br/>
<a href="sell.jsp?a=1&amp;id=<%=userBag.getId()%>">出售</a><br/>

<%}%>
<a href="sells.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>