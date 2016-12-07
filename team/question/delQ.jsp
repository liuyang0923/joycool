<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.deleteQuestion();
QuestionSetBean set = (QuestionSetBean)request.getAttribute("set");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
int option = action.getParameterIntS("o");
if(option==-1) option = 4;
else if(option<2)option=2;
else if(option>10)option=10;
%>
<%if(option<10){%><a href="addQ.jsp?sid=<%=set.getId()%>&amp;o=<%=(option+2)%>">增加选项</a><%}else{%>增加选项<%}%>|
<%if(option>2){%><a href="addQ.jsp?sid=<%=set.getId()%>&amp;o=<%=(option-2)%>">减少选项</a><%}else{%>减少选项<%}%><br/>
题目:<input name="title"  maxlength="50" value=""/><br/>
<%for(int i = 0;i < option;i++){%>选项<%=(char)('A'+i)%>.<input name="option<%=i%>"  maxlength="50" value=""/><br/><%}%>
正确答案:<select name="answer">
<%for(int i = 0;i < option;i++){%><option  value="<%=i%>"><%=(char)('A'+i)%></option><%}%>
</select><br/>
<anchor title="确定">添加
<go href="addQ.jsp?sid=<%=set.getId()%>" method="post">
    <postfield name="title" value="$title"/>
    <postfield name="content" value="$content"/>
<%for(int i = 0;i < option;i++){%><postfield name="option" value="$option<%=i%>"/><%}%>
    <postfield name="answer" value="$answer"/>
</go></anchor><br/>
<%}%>
<a href="set.jsp?sid=<%=set.getId()%>">返回</a><br/>
<a href="index.jsp">缘分测试</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>