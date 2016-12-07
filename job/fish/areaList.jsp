<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.fish.*" %><%@ page import="java.util.*" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
action.areaList();
if(action.isResult("change")) {
action.sendRedirect("fish.jsp", response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FishAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
想去哪里钓鱼？<br/>
<%
Iterator iter = action.getWorld().areaList.iterator();
while(iter.hasNext()){
	AreaBean bean = (AreaBean)iter.next();
%>
<a href="<%=(FishAction.URL_PREFIX + "/areaList.jsp?areaId=" + bean.getId())%>"><%=bean.getName()%></a><br/>
<%}%>
<%@include file="info.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>