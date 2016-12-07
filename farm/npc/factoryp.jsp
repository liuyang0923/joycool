<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.factoryProduct();
FactoryProcBean proc = (FactoryProcBean)request.getAttribute("proc");
FactoryComposeBean compose = action.world.getFactoryCompose(proc.getComposeId());
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{%>

<%=compose.getName()%><br/>
<a href="factoryp.jsp?c=1&amp;id=<%=proc.getId()%>">领取</a><br/>

<%}%>

<a href="factoryps.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>