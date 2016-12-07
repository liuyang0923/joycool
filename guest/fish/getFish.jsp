<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.fish.FishAction,net.joycool.wap.action.job.fish.AreaBean,net.joycool.wap.action.job.fish.FishBean,net.joycool.wap.action.job.fish.PullEventBean" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
if (action.getFishUser() == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
action.getFish();
AreaBean area = action.getFishUser().getArea();
if(area == null){
action.sendRedirect("index.jsp", response);
return;
}
if(!action.isCooldown("tong",3000)){
action.sendRedirect("fish.jsp", response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="waitFish.jsp">继续放鱼竿</a><br/>
<%if(action.isResult("get")){%>
	<%FishBean fish = (FishBean)request.getAttribute("fish");%>
	<%=action.getTip()%><br/>
	<%if(fish != null){%>
	<img src="/job/fish/img/<%=fish.getImage()%>" alt="o"></img><br/>
	<a href="fishInfo.jsp?id=<%=fish.getId()%>">查看详细信息</a><br/>
	<%}else{	PullEventBean pullEvent = (PullEventBean)request.getAttribute("pullEvent");
	if(pullEvent != null){
	%>
	<img src="/job/fish/img/<%=pullEvent.getImage()%>" alt="o"/><br/>
	<%}}%>
<%}else{
out.clearBuffer();
action.sendRedirect("fish.jsp", response);
return;
}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>