<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.view8();
TinyGame8 tg = (TinyGame8)action.getGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
已经过了<%=tg.getSecond()%>秒,翻了<%=tg.getMoveCount()%>次<br/>
<%
int pos = 0;
for(int j=0;j<tg.getHeight();j++){
for(int i=0;i<tg.getWidth();i++){
byte value = tg.getGrid()[i][j];
%><%if(tg.isOpen(value)){%><%=tg.getMark(i, j)%><%}else{%><a href="8v.jsp?x=<%=i%>&amp;y=<%=j%>"><%=tg.getBlankMark()%></a><%}%><%

}%><br/><%}%>
<br/>
<%if(action.isGameOver()){%>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
请连续翻开相同的牌<br/>
<a href="giveup.jsp">放弃</a><br/>
<%}%>

</p>
</card>
</wml>