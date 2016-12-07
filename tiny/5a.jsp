<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.answer2();
TinyGame5 tg = (TinyGame5)action.getGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tg.isLastWin()){%>
恭喜答对了!<br/>
<%}else{%>
很遗憾，回答错误!<br/>
<%}%>
<%if(action.isGameOver()){%>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
还需要答对<%=tg.getCount()-tg.getCorrect()%>题<br/>
<a href="5q.jsp">继续</a><br/>
<br/>
<a href="giveup.jsp">放弃</a><br/>
<%}%>
<%@include file="footer.jsp"%>
</p>
</card>
</wml>