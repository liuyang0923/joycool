<%@ page pageEncoding="utf-8" session="false"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
Tiny2Action action = new Tiny2Action(request, response);
if(action.getGame()==null){
response.sendRedirect(("/lswjs/index.jsp"));
return;
}
Tiny2Game game = action.getGame();
%>