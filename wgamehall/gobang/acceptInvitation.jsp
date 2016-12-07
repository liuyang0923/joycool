<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
GoBangAction action = new GoBangAction(request);
action.acceptInvitation(request);
String result = (String) request.getAttribute("result");
WGameHallBean gobang = (WGameHallBean) request.getAttribute("gobang");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="接受邀请">
<p align="center">接受邀请</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//失败
if("failure".equals(result)){
%>
接受失败,可能这局游戏已经被对方取消<br/>
<%
}
else if(gobang != null){
	String url = ("/wgamehall/gobang/playing.jsp?gameId=" + gobang.getId());
	out.clearBuffer();
	response.sendRedirect(url);
	return;
}
%>
<br/>
<a href="/wgamehall/gobang/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>