<%@ page contentType="text/html;charset=utf-8" language="java" %><%@ page import="java.util.*,jc.family.game.ask.*,net.joycool.wap.util.*" %><%
AskAction action=new AskAction(request,response);
AskBean ask=action.getAskBean();
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改题目</title>
<link href="/jcadmin/farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<form action="asklist.jsp" method="post">
输入题目<br/>
<input type="text" name="question" value="<%=StringUtil.toWml(ask.getQuestion())%>" /><br/>
输入答案<br/>
A:<input type="text" name="answer1" value="<%=StringUtil.toWml(ask.getAnswer1())%>"/><br/>
B:<input type="text" name="answer2" value="<%=StringUtil.toWml(ask.getAnswer2())%>"/><br/>
C:<input type="text" name="answer3" value="<%=StringUtil.toWml(ask.getAnswer3())%>"/><br/>
D:<input type="text" name="answer4" value="<%=StringUtil.toWml(ask.getAnswer4())%>"/>
<input type="hidden" name="cmd" value="edit"/><input type="hidden" name="askid" value="<%=ask.getId()%>"/><br/>
正确答案:<input type="radio" name="rightanswers" value="1" <%=ask.getRightanswers()==1?"checked":""%> />A
<input type="radio" name="rightanswers" value="2" <%=ask.getRightanswers()==2?"checked":""%> />B
<input type="radio" name="rightanswers" value="3" <%=ask.getRightanswers()==3?"checked":""%> />C
<input type="radio" name="rightanswers" value="4" <%=ask.getRightanswers()==4?"checked":""%> />D<br/>
<input type="submit" value="确定" /><br/>
<a href="asklist.jsp">返回上一页</a><br/>
</form>
</body>
</html>
