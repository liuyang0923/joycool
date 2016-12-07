<%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
FootballAction action = new FootballAction(request);
action.kick(request);
int gameId = ((Integer) request.getAttribute("gameId")).intValue();
String url = ("/wgamehall/football/playing.jsp?gameId=" + gameId);
response.sendRedirect(url);
%>