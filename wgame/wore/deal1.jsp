<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.WoreAction"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingWore") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/wore/start.jsp", response);
	return;
}
WoreAction da = new WoreAction();
da.deal1(request);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错
if("failure".equals(result)){
%>
<card title="决战单双">
<p align="left">
<%=BaseAction.getTop(request, response)%>
决战单双<br/>
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
<card title="决战单双" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
决战单双<br/>
-------------------<br/>
<img src="img/run.gif" alt="runing..."/><br/>
5秒后出结果!<br/>
心急了?<a href="<%=url%>">直接进入</a><br/>
</p>
</card>
<%
}
%>
</wml>