<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.floor.FloorAction" %><%
response.setHeader("Cache-Control","no-cache");
FloorAction action = new FloorAction(request);
action.treadFloor(request);
String result =(String)request.getAttribute("result");
String url=("/floor/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("prize")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/floor/index.jsp">回去继续踩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
out.clearBuffer();
BaseAction.sendRedirect("/floor/index.jsp", response);
return;
}else{
%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
(3秒钟跳转)
您没有踩中，祝好运!<br/>
<a href="/floor/index.jsp">回去继续踩</a><br/>
</p>
</card>
<%}%>
</wml>
