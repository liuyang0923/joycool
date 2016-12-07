<%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction ca = new CustomAction(request);
JinhuaAction action = new JinhuaAction(request);
if(ca.isCooldown("jinhua", 10000))
action.addStake(request);
String url = ("/wgamehall/jinhua/playing.jsp?gameId=" + request.getParameter("gameId"));
response.sendRedirect(url);
%>