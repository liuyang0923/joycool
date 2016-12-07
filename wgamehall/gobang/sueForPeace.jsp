<%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
GoBangAction action = new GoBangAction(request);
action.sueForPeace(request);
int gameId = ((Integer) request.getAttribute("gameId")).intValue();
String url = ("/wgamehall/gobang/playing.jsp?gameId=" + gameId);
response.sendRedirect(url);
%>