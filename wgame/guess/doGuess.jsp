<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*" %><%
response.setHeader("Cache-Control","no-cache");
GuessAction action = new GuessAction(request);
if(action.getGuessUser().isGameOver()) {
	action.sendRedirect("result.jsp", response);
	return;
}
action.doGuess();
String url=("guess.jsp");
if(action.getGuessUser().isGameOver()) {
	url=("result.jsp");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=GuessAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%if(action.isResult("ok")) {%>
<%=action.getTip()%><br/>
你猜的是：<%=request.getAttribute("guess")%><br/>
匹配结果是：<%=request.getAttribute("result")%>（<a href="help.jsp">不懂，什么意思</a>）<br/>
<%}else{%>
输入错误，请重新输入<br/>
<%}%>
<a href="<%=url%>">继续猜(5秒后自动跳转)</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>