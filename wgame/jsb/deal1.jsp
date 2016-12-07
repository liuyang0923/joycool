<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.JsbAction"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingJsb") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/jsb/start.jsp", response);
	return;
}
JsbAction action = new JsbAction();
action.deal1(request);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错
if("failure".equals(result)){
%>
<card title="剪刀石头布">
<p align="left">
<%=BaseAction.getTop(request, response)%>
剪刀石头布<br/>
-------------------<br/>
<%=request.getAttribute("tip")%><br/>
<br/>
<a href="start.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {
	String url = ("deal2.jsp");
%>
<card title="剪刀石头布" ontimer="<%=response.encodeURL(url)%>">
<timer value="20"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
剪刀石头布<br/>
-------------------<br/>
出拳中...<br/>
2秒后出结果!<br/>
心急了?<a href="<%=url%>">直接进入</a><br/>
</p>
</card>
<%
}
%>
</wml>