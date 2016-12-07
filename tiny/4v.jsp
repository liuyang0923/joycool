<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.view4();
TinyGame4 tg = (TinyGame4)action.getGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
已翻转了<%=tg.getMoveCount()%>次<br/>
<%
byte[][] grid = tg.getGrid();
int pos = 0;
for(int i=0;i<tg.getHeight();i++){%>
<%for(int j=0;j<tg.getWidth();j++){%><a href="4v.jsp?p=<%=pos%>"><%=tg.getGridMark(j,i)%></a><%pos++;}%><br/>
<%}%>
<br/>
<%if(action.isGameOver()){%>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
点击翻转，直到所有的图案都是空五角星<br/>
<a href="giveup.jsp">放弃</a><br/>
<%}%>

<%@include file="footer.jsp"%>
</p>
</card>
</wml>