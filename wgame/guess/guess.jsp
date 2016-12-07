<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*" %><%
response.setHeader("Cache-Control","no-cache");
GuessAction action = new GuessAction(request);
action.guess();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=GuessAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
第<%=action.getGuessUser().getTurn() + 1%>次<br/>
我猜：<input name="guess" format="*N" maxlength="10" value=""/><br/>
<anchor title="确定">试试这个
  <go href="doGuess.jsp" method="post">
    <postfield name="guess" value="$guess"/>
  </go>
</anchor><br/>
<%@include file="info.jsp"%>
<a href="reset.jsp?reset=">重新玩猜数字</a><br/>
<a href="help.jsp">查看游戏规则</a><br/>
<a href="<%=( "/lswjs/gameIndex.jsp")%>">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>