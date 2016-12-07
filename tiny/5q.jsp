<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.question2();
TinyGame5 tg = (TinyGame5)action.getGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tg.getShow()%><br/>
请选择出现次数最多的图案<br/>
<%for(int i=0;i<tg.getType();i++){%>
<%if(i>0){%>,<%}%><a href="5a.jsp?o=<%=i%>"><%=tg.mark[i]%></a>
<%}%>
<br/>
<a href="giveup.jsp">放弃</a><br/>
<%@include file="footer.jsp"%>
</p>
</card>
</wml>