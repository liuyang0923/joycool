<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.FSAction" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.sceneList();
String result =(String)request.getAttribute("result");
String url=("/lswjs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("redirect")){
out.clearBuffer();
BaseAction.sendRedirect("/fs/index.jsp",response);
return;
}else if(result.equals("timeOver")){
out.clearBuffer();
BaseAction.sendRedirect("/fs/result.jsp",response);
return;
}else{%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
火车站<br/>
想扒火车去哪里？<br/>
<a href="/fs/sceneList.jsp?sceneId=0">北京</a>|<a href="/fs/sceneList.jsp?sceneId=1">上海</a>|<a href="/fs/sceneList.jsp?sceneId=2">天津</a>|<a href="/fs/sceneList.jsp?sceneId=3">重庆</a><br/>
<a href="/fs/sceneList.jsp?sceneId=4">广州</a>|<a href="/fs/sceneList.jsp?sceneId=5">深圳</a>|<a href="/fs/sceneList.jsp?sceneId=6">东莞</a>|<a href="/fs/sceneList.jsp?sceneId=7">济南</a><br/>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>