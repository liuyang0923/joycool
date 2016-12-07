<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.fish.*" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
action.fishInfo();
FishBean fish = (FishBean)request.getAttribute("fish");
PullBean pull = (PullBean)request.getAttribute("pull");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=fish.getName()%><br/>
常见的现象：<%=pull.getPattern()%><br/>
<img src="img/<%=fish.getImage()%>" alt=""></img><br/>
<a href="fish.jsp">继续钓鱼</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>