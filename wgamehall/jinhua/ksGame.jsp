<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
action.ksGame(request);
String result = (String) request.getAttribute("result");
String type = (String) request.getAttribute("type");
String url=("/wgamehall/jinhua/index.jsp?type="+type);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="砸金花" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转砸金花)<br/>
<a href="/wgamehall/jinhua/index.jsp?type=<%=type%>">返回砸金花</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("wait")){%>
<card title="砸金花" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转砸金花)<br/>
<a href="/wgamehall/jinhua/index.jsp?type=<%=type%>">返回砸金花</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("ksWait")){%>
<card title="砸金花" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转砸金花)<br/>
<a href="/wgamehall/jinhua/index.jsp?type=<%=type%>">返回砸金花</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
WGameHallBean jinhua = (WGameHallBean) request.getAttribute("jinhua"); 
out.clearBuffer();
response.sendRedirect("playing.jsp?gameId=" + jinhua.getId());
return;
}%>
</wml>