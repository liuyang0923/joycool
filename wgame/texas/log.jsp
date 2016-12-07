<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.util.UserInfoUtil,java.util.List"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
response.setHeader("Cache-Control","no-cache");
TexasAction action = new TexasAction(request);
TexasGame game = (TexasGame)session.getAttribute("texas");
if(game == null) {
	response.sendRedirect("index.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=game.getBoardId() + 1%>号<%=game.getGameTypeName()%>桌" ontimer="<%=response.encodeURL("log.jsp")%>"><timer value="200"/>
<p align="left">
<a href="play.jsp">返回游戏</a><br/>
==最近历史信息==<br/>
<%=game.getLog().getLogString(30)%>
<a href="play.jsp">返回游戏</a><br/>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>

</card>
</wml>