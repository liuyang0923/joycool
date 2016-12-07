<%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
OthelloAction action = new OthelloAction(request);
action.denyPeace(request);
int gameId = ((Integer) request.getAttribute("gameId")).intValue();
String url = ("/wgamehall/othello/playing.jsp?gameId=" + gameId);
response.sendRedirect(url);
%>