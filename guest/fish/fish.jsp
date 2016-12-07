<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.fish.FishAction,net.joycool.wap.action.job.fish.AreaBean" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
if (action.getFishUser() == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
action.fish();
AreaBean area = action.getFishUser().getArea();
if(area == null){
action.sendRedirect("index.jsp", response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/job/fish/img/<%=area.getImage()%>" alt="<%=area.getName()%>"/><br/>
<%=area.getName()%>现有<%=RandomUtil.nextInt(20) + 30%>人<br/>
<a href="<%=(FishAction.URL_PREFIX + "/waitFish.jsp")%>">放鱼竿，等鱼上钩</a><br/>
<%@include file="info.jsp"%>
<a href="areaList.jsp">换个地方钓</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>