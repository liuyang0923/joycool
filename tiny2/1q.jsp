<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
Tiny2Game1 tg = (Tiny2Game1)action.getGame();
String[] options = tg.getOptions();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="小游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择下面最大的答案<br/>
<%for(int i=0;i<options.length;i++){%>
<a href="1a.jsp?o=<%=i%>"><%=options[i]%></a><br/>
<%}%>
<br/>
<a href="giveup.jsp">放弃</a><br/>
<%@include file="footer.jsp"%>
</p>
</card>
</wml>