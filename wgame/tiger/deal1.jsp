<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.TigerAction"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingTiger") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/tiger/start.jsp", response);
	return;
}
TigerAction action = new TigerAction();
action.deal1(request);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错
if("failure".equals(result)){
%>
<card title="老虎机">
<p align="left">
<%=BaseAction.getTop(request, response)%>
老虎机<br/>
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
<card title="老虎机" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
老虎机<br/>
-------------------<br/>
<img src="img/run.gif" alt="runing..."/><br/>
5秒后自动停下!<br/>
心急了?<a href="<%=url%>">马上停下</a><br/>
</p>
</card>
<%
}
%>
</wml>