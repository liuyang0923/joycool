<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.sellEquip();
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
DummyProductBean item = FarmWorld.getItem(userBag.getProductId());
%>

*<%=item.getName()%><%if(item.getClass1()!=1&&item.getClass1()!=2){%><%=userBag.getTimeString(item)%><%}%>
<%if(item.getRank()>0){%>
(等级<%=item.getRank()%>)
<%}%>[<%=item.getGradeName()%>]<br/>
<%if(item.getClass1()>0&&item.getClass1()<10){%>
<%if(item.getClass1()==1||item.getClass1()==2){%>
耐久度:<%=userBag.getTime()%>/<%=item.getTime()%><br/>
<%}else{%>
数量:<%=userBag.getTime()%><br/>
<%}%>
<%if(item.getStack()>1){%>
单价:<%=FarmWorld.formatMoney(item.getPrice())%><br/>
<%}%>
总价:<%=FarmWorld.formatMoney(item.getPrice()*userBag.getTime()/item.getTime())%><br/>
<a href="selle.jsp?a=1&amp;id=<%=userBag.getId()%>">确认出售</a><br/>

<%}%>
<%if(item.getIntroduction().length()>0){%>
“<%=item.getIntroduction()%>”<br/>
<%}%>
<%=FarmWorld.itemString(userBag)%>
<% ItemSetBean set = FarmWorld.one.getItemToSet(item.getId());
	if(set != null){	%>
--<a href="itemSet.jsp?id=<%=set.getId()%>"><%=set.getName()%></a>(套装)--<br/>
<%=FarmWorld.one.itemSetString(set, null).toString()%>
<%}%>
<br/>
<%}%>
<a href="selles.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>