<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.factoryComposes();
FarmNpcWorld world = action.world;
FactoryBean factory = action.getUserFactory();
FarmUserBean farmUser = action.getUser();
List composeList = world.getFactoryComposeList(factory.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=factory.getName()%><br/>
<%
for(int i=0;i<composeList.size();i++){
FactoryComposeBean compose = (FactoryComposeBean)composeList.get(i);
%>
<a href="factoryc.jsp?id=<%=compose.getId()%>"><%=compose.getName()%></a>
<a href="factoryc.jsp?c=1&amp;id=<%=compose.getId()%>">直接加工</a><br/>
<%}%>

<a href="factory.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>