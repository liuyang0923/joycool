<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.view();
TinyGame2 tg = (TinyGame2)action.getGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
总共移动了<%=tg.getMoveCount()%>次<br/>
<%
char[][] grid = tg.getGrid();
for(int i=0;i<tg.getHeight();i++){%>
<a href="2v.jsp?d=1&amp;n=<%=i%>">←</a>

<%if(i==0){%>
<%for(int j=0;j<tg.getWidth();j++){%><a href="2v.jsp?d=3&amp;n=<%=j%>"><%=grid[j][i]%></a><%}%>
<%}else if(i==tg.getHeight()-1){%>
<%for(int j=0;j<tg.getWidth();j++){%><a href="2v.jsp?d=4&amp;n=<%=j%>"><%=grid[j][i]%></a><%}%>
<%}else{%>
<%for(int j=0;j<tg.getWidth();j++){%><%=grid[j][i]%><%}%>
<%}%>
<a href="2v.jsp?d=2&amp;n=<%=i%>">→</a>

<br/>

<%}%>
<%if(action.isGameOver()){%>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
移动一定的行列，直到同列的图案都相同<br/>
<%}%>
<br/>
<a href="giveup.jsp">放弃</a><br/>

<%@include file="footer.jsp"%>
</p>
</card>
</wml>