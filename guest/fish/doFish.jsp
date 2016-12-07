<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.fish.FishAction,net.joycool.wap.action.job.fish.AreaBean,net.joycool.wap.action.job.fish.PullBean" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
if (action.getFishUser() == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
action.doFish();
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
<%if(action.isResult("catch")){%>
<%PullBean pull = (PullBean)request.getAttribute("pull");%>
有鱼出现了!<br/>
<%=pull.getPattern()%><br/>
<img src="/job/fish/img/<%=pull.getImage()%>" alt="o" /><br/>
快决定怎样拉杆：<br/>
<%
int part = (pull.getId() - 1) / 4;	// 只显示4个一组的同组
Iterator iter = FishAction.getWorld().pullList.iterator();
while(iter.hasNext()){
	pull = (PullBean)iter.next();
	if((pull.getId() - 1) / 4 == part) {
%>
<a href="getFish.jsp?pullId=<%=pull.getId()%>"><%=pull.getPullMode()%></a><br/>
<%}}%>
<%}else{%>
5分钟过去了，鱼钩没有任何动静，再试一次吧<br/>
<a href="waitFish.jsp">继续放鱼竿</a><br/>
<%}%>
<%@include file="info.jsp"%>
<a href="areaList.jsp">换个地方钓</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>