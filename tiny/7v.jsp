<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.view7();
TinyGame7 tg = (TinyGame7)action.getGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
已经过了<%=tg.getSecond()%>秒,打开了<%=tg.getOpenCount()%>格<br/>
<%
int pos = 0;
for(int j=0;j<tg.getHeight();j++){
for(int i=0;i<tg.getWidth();i++){

%><%if(tg.isLost()||tg.isOpen(i, j)){%><%=tg.getMark(i, j)%><%}else{%><a href="7v.jsp?x=<%=i%>&amp;y=<%=j%>"><%=tg.getMark(i, j)%></a><%}%><%

}%><br/><%}%>
<br/>
<%if(action.isGameOver()){%>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
点击打开，踩中地雷就输<br/>
<a href="giveup.jsp">放弃</a><br/>
<%}%>

</p>
</card>
</wml>