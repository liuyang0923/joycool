<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.wgame.*" %><%response.setHeader("Cache-Control","no-cache");
GuessAction action = new GuessAction(request);
action.reset();
if(action.isResult("reset")) {
action.sendRedirect("guess.jsp", response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=GuessAction.title%>" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
确认要重新开始游戏吗？之前的进度将会丢失！<br/>
<a href="reset.jsp?reset=1">是的，重新开始</a><br/>
<a href="guess.jsp">点错了，继续刚才的</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>