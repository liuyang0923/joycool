<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.factoryProducts();
List procList = (List)request.getAttribute("procList");
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">

<%
for(int i=0;i<procList.size();i++){
FactoryProcBean proc = (FactoryProcBean)procList.get(i);
FactoryComposeBean compose = action.world.getFactoryCompose(proc.getComposeId());
%>
<a href="factoryp.jsp?id=<%=proc.getId()%>"><%=compose.getName()%></a>
<a href="factoryp.jsp?c=1&amp;id=<%=proc.getId()%>">直接领取</a><br/>
<%}%>
加工是需要时间的,如果加工还没有完成,请耐心等待或者过一段时间再来吧!<br/>
<a href="factory.jsp">返回加工厂</a><br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>