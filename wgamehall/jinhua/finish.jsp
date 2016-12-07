<%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
action.finish(request);
int gameId = ((Integer) request.getAttribute("gameId")).intValue();
String url = ("/wgamehall/jinhua/playing.jsp?gameId=" + gameId);
response.sendRedirect(url);
%>