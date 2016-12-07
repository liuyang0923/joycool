<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction"%><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.fight(request);
String result =(String)request.getAttribute("result");
if(result==null){
	String isType= (String)request.getAttribute("isType");
	if(isType.equals("monster")){
		String index = (String)request.getAttribute("index");
		BaseAction.sendRedirect("/pk/moster.jsp?index="+index, response);
	}else{
		String playerId = (String)request.getAttribute("playerId");
		BaseAction.sendRedirect("/pk/player.jsp?playerId="+playerId, response);
	}
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL("/pk/index.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml((String)request.getAttribute("tip")) %>(3秒后跳转回场景)<br/><br/>
<a href="/pk/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>