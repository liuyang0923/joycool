<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
OthelloAction action = new OthelloAction(request);
action.move(request);
int gameId = ((Integer) request.getAttribute("gameId")).intValue();
String url = ("/wgamehall/othello/playing.jsp?gameId=" + gameId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="下子" ontimer="<%=response.encodeURL(url)%>">
<timer value="1"/>
<p align="left">
下子完毕。<br/>
<a href="<%=url%>">返回</a>
</p>
</card>
</wml>