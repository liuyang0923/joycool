<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="问答接龙">
<p align="center">游戏规则</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
点击开始进行答题。
从第1至5题，如果期间任一题没有答对将返回至第1题；从第6-9题期间任一题没有答对，返回至第2题；如果第10题没有答对，本次游戏结束。
连续答对10题，等阶增长1，100阶进级，分别为书生、秀才、举人、进士、状元。<br/>
<br/>
<a href="index.jsp">返回游戏</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>