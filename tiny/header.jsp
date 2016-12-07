<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TinyAction action = new TinyAction(request, response);
if(action.getGame()==null){
response.sendRedirect(("/lswjs/index.jsp"));
return;
}
%>