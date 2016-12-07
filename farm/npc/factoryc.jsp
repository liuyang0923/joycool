<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.factoryCompose();
FactoryComposeBean compose = (FactoryComposeBean)request.getAttribute("compose");
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<a href="factorycs.jsp">返回</a><br/>
<%}else{%>
<%=compose.getName()%><br/>
<%=compose.getInfo()%><br/>
需要<%=DateUtil.formatTimeInterval(compose.getTime())%><br/>
材料:<%=FarmWorld.getItemListString(compose.getMaterialList(), compose.getPrice())%><br/>
产品:<%=FarmWorld.getItemListString(compose.getProductList())%><br/>
<a href="factoryc.jsp?c=1&amp;id=<%=compose.getId()%>">开始加工</a><br/>

<%}%>

<a href="factory.jsp">返回加工厂</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>