<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.wgame.ShowHandAction"%><%@ page import="jc.guest.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingShowHand2") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/guest/showhand/start.jsp", response);
	return;
}
ShowHandAction action = new ShowHandAction();
GuestHallAction guestAction = new GuestHallAction(request);
GuestUserInfo guestUser = guestAction.getGuestUser();
if (guestUser == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
action.deal1(request,guestUser);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错
if("failure".equals(result)){
%>
<card title="梭哈">
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
-------------------<br/>
<%=request.getAttribute("tip")%><br/>
<br/>
<a href="start.jsp">返回上一级</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {
	String url = ("round1.jsp");
%>
<card title="梭哈" ontimer="<%=response.encodeURL(url)%>">
<timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
-------------------<br/>
发牌中...<br/>
1秒后跳转!<br/>
心急了?<a href="<%=url%>">直接进入</a><br/>
</p>
</card>
<%
}
%>
</wml>