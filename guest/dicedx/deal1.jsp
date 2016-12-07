<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.wgame.DiceDXAction"%><%@ page import="jc.guest.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingDiceDX2") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/guest/dicedx/start.jsp", response);
	return;
}
DiceDXAction da = new DiceDXAction();
GuestHallAction action = new GuestHallAction(request);
GuestUserInfo guestUser = action.getGuestUser();
if (guestUser == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
da.deal1(request,guestUser);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错
if("failure".equals(result)){
%>
<card title="掷骰子比大小">
<p align="left">
<%=BaseAction.getTop(request, response)%>
掷骰子比大小<br/>
-------------------<br/>
<%=request.getAttribute("tip")%><br/>
<a href="start.jsp">返回上一级</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {
	String url = ("deal2.jsp");
%>
<card title="掷骰子比大小" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
掷骰子比大小<br/>
-------------------<br/>
<img src="/wgamepk/dice/img/run.gif" alt="o"/><br/>
5秒后出结果!<br/>
心急了?<a href="<%=url%>">直接进入</a><br/>
</p>
</card>
<%
}
%>
</wml>