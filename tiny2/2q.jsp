<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%@ page import="net.joycool.wap.util.LoginFilter.SessionClick"%><%
Tiny2Game2 tg = (Tiny2Game2)game;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="小游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="<%=response.encodeURL("2p.jsp")%>" alt="?" /><a href="2q.jsp">刷新</a><br/>

<%if(tg.failCount>=1){%>
请输入图片中<%=tg.question.getColorName()%>数字之和:<br/>
<input name="op" maxlength="6"/><br/>
<anchor>提交
	<go href="2a.jsp" method="get">
		<postfield name="o" value="$op" />
	</go>
</anchor>
<%}else{%>
请计算图片中<%=tg.question.getColorName()%>数字之和:<br/>
<%
int[] answers = tg.question.answers;
for(int i=0;i<answers.length;i++){
%>
<a href="2a.jsp?o=<%=answers[i]%>"><%=answers[i]%></a> , 
<%}}%>
<br/>
<%@include file="footer.jsp"%>
</p>
</card>
</wml>