<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
action.invite(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGameHallBean jinhua = (WGameHallBean) request.getAttribute("jinhua");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请玩家游戏">
<p align="center">邀请玩家游戏</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//失败
if("failure".equals(result)){
%>
<%=tip%><br/>
<%
}
//进入游戏
else if(jinhua != null){
	String url = ("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());
	out.clearBuffer();
	response.sendRedirect(url);
	return;
}
%>
<br/>
<a href="/wgamehall/jinhua/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>