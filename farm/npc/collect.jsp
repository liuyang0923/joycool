<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.collect();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<a href="../user/collects.jsp">查看已购买的收藏盒</a><br/>
<%}else{
CollectBean collect = (CollectBean)request.getAttribute("collect");
%>

<%=collect.getName()%>(共<%=collect.getCount()%>件)<br/>
<%=collect.getInfo()%><br/>
需要:<%=FarmWorld.formatMoney(collect.getPrice())%><br/>
现有:<%=FarmWorld.formatMoney(action.getUser().getMoney())%><br/>
<a href="collect.jsp?a=1&amp;id=<%=collect.getId()%>">直接购买</a><br/>

<%}%>

<a href="collects.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>